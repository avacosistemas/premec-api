package ar.com.avaco.factory;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import ar.com.avaco.model.PostLoginSAPDTO;
import ar.com.avaco.model.ResponseLoginSAPDTO;

public class RestTemplateFactory {

	private static RestTemplateFactory instance;

	private RestTemplateFactory() {
	}

	public static RestTemplateFactory getInstance() {
		if (instance == null) {
			instance = new RestTemplateFactory();
		}
		return instance;
	}

	public RestTemplate getLoggedRestTemplate() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		// Cargar el archivo de certificado
		InputStream certInputStream = classLoader.getResourceAsStream("myCA.cer");

		X509Certificate certificado = (X509Certificate) CertificateFactory.getInstance("X.509")
				.generateCertificate(certInputStream);

		// Crear un keystore con el certificado
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);
		keyStore.setCertificateEntry("cert", certificado);

		// Crear un TrustManagerFactory que confíe en el certificado
		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);

		// Crear un KeyManagerFactory que use el certificado para la autenticación
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, null);

		// Crear un contexto SSL con el KeyManagerFactory y TrustManagerFactory
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

		// Crear una fábrica de solicitudes HTTP que use la conexión SSL
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setHttpClient(HttpClients.custom().setSSLContext(sslContext).build());

		// Crear un objeto RestTemplate que use la fábrica de solicitudes HTTP
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);

		// Crear los encabezados HTTP
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		PostLoginSAPDTO dto = new PostLoginSAPDTO();
		dto.setCompanyDB("AR_PRD_PREMEC");
		dto.setPassword("Prd2019.");
		dto.setUserName("manager");

		String urlLogin = "https://vm-sap:50000/b1s/v1/Login";

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(dto.getAsMap(), headers);

		ResponseEntity<ResponseLoginSAPDTO> response = restTemplate.exchange(urlLogin, HttpMethod.POST, httpEntity,
				ResponseLoginSAPDTO.class);

		headers.add("Cookie", "B1SESSION=" + response.getBody().getSessionId() + "; ROUTEID=.node2");
		headers.setCacheControl("no-cache");

		restTemplate.setDefaultUriVariables(headers);

		return restTemplate;
	}

}

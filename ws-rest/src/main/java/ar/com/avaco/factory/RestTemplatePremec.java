package ar.com.avaco.factory;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RestTemplatePremec extends RestTemplate {

	public RestTemplatePremec(HttpComponentsClientHttpRequestFactory httpRequestFactory) {
		super(httpRequestFactory);
	}

	public <T> ResponseEntity<T> doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Object... uriVariables) throws SapBusinessException {
		try {
			return super.exchange(url, method, requestEntity, responseType, uriVariables);
		} catch (RestClientException rce) {
			throw new SapBusinessException(rce);
		}
	}

	public <T> ResponseEntity<T> doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType, Object... uriVariables) throws SapBusinessException {
		try {
			return super.exchange(url, method, requestEntity, responseType, uriVariables);
		} catch (RestClientException rce) {
			throw new SapBusinessException(rce);
		}
	}

}

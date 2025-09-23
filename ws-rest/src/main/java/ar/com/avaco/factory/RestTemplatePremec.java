package ar.com.avaco.factory;

import java.util.Calendar;
import java.util.Date;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RestTemplatePremec extends RestTemplate {

	private Date expiration;

	private HttpHeaders defaultHeaders;

	public RestTemplatePremec(HttpComponentsClientHttpRequestFactory httpRequestFactory) {
		super(httpRequestFactory);
	}

	public <T> ResponseEntity<T> doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Object... uriVariables) throws SapBusinessException {
		int tries = 1;
		ResponseEntity<T> exchange = null;
		do {
			try {
				exchange = super.exchange(url, method, requestEntity, responseType, uriVariables);
			} catch (ResourceAccessException e) {
				if (tries == 3)
					throw new SapBusinessException(e);
				tries++;
			} catch (RestClientException rce) {
				throw new SapBusinessException(rce);
			}
		} while (tries < 3 && exchange == null);
		return exchange;

	}

	public <T> ResponseEntity<T> doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType, Object... uriVariables) throws SapBusinessException {

		int tries = 1;
		ResponseEntity<T> exchange = null;
		do {
			try {
				exchange = super.exchange(url, method, requestEntity, responseType, uriVariables);
			} catch (ResourceAccessException e) {
				if (tries == 3)
					throw new SapBusinessException(e);
				tries++;
			} catch (RestClientException rce) {
				throw new SapBusinessException(rce);
			} catch (Exception e) {
				throw new SapBusinessException(e);
			}
		} while (tries < 3 && exchange == null);
		return exchange;

	}

	public void addSessionExpiration(Integer minutes) {
		minutes = minutes - 5;
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MINUTE, minutes);
		expiration = instance.getTime();

	}

	public boolean isSessionActive() {
		return Calendar.getInstance().getTime().before(expiration);
	}

	public HttpHeaders getDefaultHeaders() {
		return defaultHeaders;
	}

	public void setDefaultHeaders(HttpHeaders defaultHeaders) {
		this.defaultHeaders = defaultHeaders;
	}

}

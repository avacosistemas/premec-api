package ar.com.avaco.factory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;

@Component
public class SapResponseErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) {
		String error = "No se pudo parsear el error"; 
		try {
			error = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RestClientException(error);
	}

}
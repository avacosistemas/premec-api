package ar.com.avaco.factory;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestClientException;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class SapBusinessException extends Exception {

	private static final long serialVersionUID = -2580880615509146634L;

	public static final Integer ERROR_NEG_5002 = -5002;

	public static final Integer ERROR_35_SSL_CONNECT_ERROR = 35;
	public static final Integer ERROR_200_ENTRY_NOT_FOUND = 200;
	public static final Integer ERROR_299_SAML_LOGIN_FAILED = 299;
	public static final Integer ERROR_35_BUFFER = 35;
	public static final Integer ERROR_401_JWT = 401;

	public static final List<Integer> NOT_SEND_MAIL = Arrays.asList(
			ERROR_35_SSL_CONNECT_ERROR,
			ERROR_299_SAML_LOGIN_FAILED,
			ERROR_35_BUFFER,
			ERROR_401_JWT
			);

	private Integer errorCode;

	public SapBusinessException(RestClientException ex) {
		super(ex);
		this.errorCode = getErrorCode(ex);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public boolean hasToSendMail() {
		return !SapBusinessException.NOT_SEND_MAIL.contains(errorCode);
	}

	private int getErrorCode(RestClientException rce) {
		return ((Double) ((LinkedTreeMap<?, ?>) ((LinkedTreeMap<?, ?>) new Gson().fromJson(rce.getMessage(),
				Object.class)).get("error")).get("code")).intValue();
	}

}

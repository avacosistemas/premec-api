package ar.com.avaco.model;

public class ResponseLoginSAPDTO {

	public String SessionId;
	public String Version;
	public Long SessionTimeout;

	public String getSessionId() {
		return SessionId;
	}

	public void setSessionId(String sessionId) {
		SessionId = sessionId;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public Long getSessionTimeout() {
		return SessionTimeout;
	}

	public void setSessionTimeout(Long sessionTimeout) {
		SessionTimeout = sessionTimeout;
	}

}

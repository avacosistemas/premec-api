package ar.com.avaco.ws.rest.security.service;

public interface ReporteService {
	
	void sendMail(String email, String activityCode, String body, String subject);

}

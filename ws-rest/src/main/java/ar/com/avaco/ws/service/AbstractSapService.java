package ar.com.avaco.ws.service;

import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;

import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.factory.RestTemplatePremec;

public abstract class AbstractSapService {

	@Value("${urlSAP}")
	protected String urlSAP;

	@Value("${userSAP}")
	protected String userSAP;

	@Value("${passSAP}")
	protected String passSAP;

	@Value("${dbSAP}")
	protected String dbSAP;

	@Value("${monitor.maxpagesize}")
	protected String maxpagesize;

	@Value("${email.from}")
	protected String from;

	@Value("${email.errores}")
	protected String toErrores;

	@Value("${email.errores.cc}")
	protected String toErroresCC;

	protected Gson gson = new Gson();

	private RestTemplatePremec restTemplate;

	protected RestTemplatePremec getRestTemplate() {
		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP, this.restTemplate).get();
		return this.restTemplate;
	}

}

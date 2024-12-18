package ar.com.avaco.ws.service.impl;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ar.com.avaco.factory.ParentObjectIdNotFoundException;
import ar.com.avaco.factory.RestTemplatePremec;
import ar.com.avaco.factory.SapBusinessException;

public class SAPWSUtils {

	private static final Logger LOGGER = Logger.getLogger(SAPWSUtils.class);

	

	private RestTemplatePremec restTemplate;

	private Gson gson;

	private String urlSAP;

	public SAPWSUtils(RestTemplatePremec restTemplate, String urlSAP) {
		this.restTemplate = restTemplate;
		this.urlSAP = urlSAP;
		this.gson = new Gson();
	}

	

}

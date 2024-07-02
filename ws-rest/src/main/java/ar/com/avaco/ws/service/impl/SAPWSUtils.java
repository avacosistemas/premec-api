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

	private static final String ACTIVITIES_SELECT_PARENT_OBJECT_ID = "/Activities({id})?$select=ParentObjectId";
	private static final String SERVICE_CALLS_SELECT_SERVICE_CALL_ACTIVITIES = "/ServiceCalls({id})?$select=ServiceCallActivities";

	private RestTemplatePremec restTemplate;

	private Gson gson;

	private String urlSAP;

	public SAPWSUtils(RestTemplatePremec restTemplate, String urlSAP) {
		this.restTemplate = restTemplate;
		this.urlSAP = urlSAP;
		this.gson = new Gson();
	}

	public Long getParentObjectId(Long idActividad) throws ParentObjectIdNotFoundException, SapBusinessException {
		LOGGER.debug("Obteniendo ParentObjectId (ServiceCallID) de la actividad " + idActividad);
		String url = urlSAP + ACTIVITIES_SELECT_PARENT_OBJECT_ID;
		url = url.replace("{id}", idActividad.toString());
		LOGGER.debug(url);
		ResponseEntity<String> responseParentObjectId = null;
		responseParentObjectId = restTemplate.doExchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<String>() {
				});

		JsonElement jsonElement = gson.fromJson(responseParentObjectId.getBody(), JsonObject.class)
				.get("ParentObjectId");
		if (jsonElement == null || jsonElement.isJsonNull()) {
			throw new ParentObjectIdNotFoundException(
					"Actividad: " + idActividad + " - No se pudo obtener el parentObjectId . URL " + url);
		}
		return jsonElement.getAsLong();
	}

	public JsonArray getServiceCallActivities(Long idActividad, Long parentObjectId) throws SapBusinessException  {
		LOGGER.debug("Actividad: " + idActividad + " - Obteniendo ServiceCall Activities");
		String serviceCallActivitiesUrl = urlSAP + SERVICE_CALLS_SELECT_SERVICE_CALL_ACTIVITIES;
		serviceCallActivitiesUrl = serviceCallActivitiesUrl.replace("{id}", parentObjectId.toString());
		LOGGER.debug(urlSAP);

		ResponseEntity<String> serviceCallActivities = null;
			serviceCallActivities = restTemplate.doExchange(serviceCallActivitiesUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});

		LOGGER.debug("Actividad: " + idActividad + " - ServiceCall Activities Obtenidas");

		return gson.fromJson(serviceCallActivities.getBody(), JsonObject.class).get("ServiceCallActivities")
				.getAsJsonArray();
	}

}

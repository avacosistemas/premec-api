package ar.com.avaco.ws.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.service.FunctionBusiness;

public abstract class AbstractRestBaseController {

	private static final Logger LOGGER = Logger.getLogger(AbstractRestBaseController.class);

	protected static final String OK = "OK";

	public <R> ResponseEntity<JSONResponse> executeProcess(String processName, FunctionBusiness<Void, R> function) throws Exception {
		LOGGER.info("Run process " + processName);
		HttpStatus httpStatus = HttpStatus.OK;
		JSONResponse response = getResponseOK(function.apply(null));
		return new ResponseEntity<JSONResponse>(response, httpStatus);
	}

	protected <T> JSONResponse getResponseOK(T data) {
		JSONResponse response = new JSONResponse();
		response.setData(data);
		response.setStatus(OK);
		return response;
	}

}
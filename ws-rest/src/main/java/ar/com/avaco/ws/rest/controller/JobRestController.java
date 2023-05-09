package ar.com.avaco.ws.rest.controller;

import javax.annotation.Resource;

import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.service.PremecJobService;
import ar.com.avaco.ws.rest.dto.JSONResponse;

@RestController
public class JobRestController {

	private PremecJobService premecJobService;

	@Resource(name = "premecJobService")
	public void setPremecJobService(PremecJobService premecJobService) {
		this.premecJobService = premecJobService;
	}
	
	@RequestMapping(value = "/startStopJobService", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> startJobService() throws SchedulerException, InterruptedException {
		premecJobService.startStopJobService();
		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/isJobRunning", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> isJobRunning() throws SchedulerException, InterruptedException {
		boolean ret = premecJobService.isJobRunning();
		JSONResponse response = new JSONResponse();
		response.setData(ret);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	
}

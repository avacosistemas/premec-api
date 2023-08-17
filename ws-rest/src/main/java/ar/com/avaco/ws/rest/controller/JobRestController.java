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

	@RequestMapping(value = "/startStopEnvioFormularioSapJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> startStopEnvioFormularioSapJob()
			throws SchedulerException, InterruptedException {
		premecJobService.startStopEnvioFormularioSapJob();
		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/isEnvioFormularioSapJobRunning", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> isEnvioFormularioSapJobRunning()
			throws SchedulerException, InterruptedException {
		boolean ret = premecJobService.isEnvioFormularioSapJobRunning();
		JSONResponse response = new JSONResponse();
		response.setData(ret);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/startStopReporteDiarioJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> startStopReporteDiarioJob() throws SchedulerException, InterruptedException {
		premecJobService.startStopReporteDiarioJob();
		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/isReporteDiarioJobRunning", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> isReporteDiarioJobRunning() throws SchedulerException, InterruptedException {
		boolean ret = premecJobService.isReporteDiarioJobRunning();
		JSONResponse response = new JSONResponse();
		response.setData(ret);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

}

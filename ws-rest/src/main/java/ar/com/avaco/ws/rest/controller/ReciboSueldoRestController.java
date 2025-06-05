package ar.com.avaco.ws.rest.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.ws.dto.timesheet.LoteRecibosSueldoDTO;
import ar.com.avaco.ws.dto.timesheet.RegistroReciboPorUsuarioDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.ReciboSueldoService;

@RestController
public class ReciboSueldoRestController {

	private ReciboSueldoService reciboService;

	@RequestMapping(value = "/procesarRecibos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> procesarRecibos(String tipo) {
		JSONResponse response = new JSONResponse();
		try {
			response.setData(this.reciboService.procesarRecibos(tipo));
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e);
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/aprobarRecibos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> aprobarRecibos(@RequestBody LoteRecibosSueldoDTO lote) {
		JSONResponse response = new JSONResponse();
		try {
			this.reciboService.aprobarRecibos(lote);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e);
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rechazarRecibos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> rechazarRecibos(@RequestBody LoteRecibosSueldoDTO lote) {
		JSONResponse response = new JSONResponse();
		try {
			this.reciboService.rechazarRecibos(lote);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e);
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/listarRecibosPorUsuario", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> listarRecibosPorUsuario() {
		JSONResponse response = new JSONResponse();
		try {
			List<RegistroReciboPorUsuarioDTO> recibos = this.reciboService.listarRecibosPorUsuario();
			response.setData(recibos);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e);
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/descargarRecibo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> descargarRecibo(RegistroReciboPorUsuarioDTO recibo) {
		JSONResponse response = new JSONResponse();
		try {
			byte[] recibopdf = this.reciboService.obtenerReciboPDF(recibo);
			response.setData(recibopdf);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e);
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "reciboSueldoService")
	public void setReciboService(ReciboSueldoService reciboService) {
		this.reciboService = reciboService;
	}

}

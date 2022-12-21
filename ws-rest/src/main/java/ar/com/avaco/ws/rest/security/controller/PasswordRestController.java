package ar.com.avaco.ws.rest.security.controller;

import javax.annotation.Resource
;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.security.dto.PassworResetDTO;
import ar.com.avaco.ws.rest.security.dto.UpdatePasswordDTO;
import ar.com.avaco.ws.rest.security.service.UserService;
import ar.com.avaco.ws.rest.service.FunctionBusiness;

@RestController
public class PasswordRestController {

    private UsuarioService usuarioService;
	
	private UserService userEPservice;

	@RequestMapping(value = "/password/reset/", method = RequestMethod.POST)
	public ResponseEntity<JSONResponse> reset(@RequestBody PassworResetDTO dto) throws BusinessException {
		usuarioService.sendMissingPassword(dto.getUsername());
		JSONResponse jsonResponse = new JSONResponse();
		jsonResponse.setData(null);
		jsonResponse.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(jsonResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/password/reset/{id}", method = RequestMethod.PUT)
	public ResponseEntity<JSONResponse> resetById(@PathVariable("id") Long id) throws BusinessException {
		usuarioService.sendMissingPasswordById(id);
		JSONResponse jsonResponse = new JSONResponse();
		jsonResponse.setData(null);
		jsonResponse.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(jsonResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/password/update/", method = RequestMethod.POST)
    public  ResponseEntity<JSONResponse> resetPassword(HttpServletRequest request, @RequestBody UpdatePasswordDTO updatePassword) throws BusinessException {
    	return executeProcess("update-password", Void -> {this.userEPservice.updatePassword(updatePassword); return null;});
    }
	
	public <R> ResponseEntity<JSONResponse> executeProcess(String processName, FunctionBusiness<Void, R> function) throws BusinessException {
    	HttpStatus httpStatus = HttpStatus.OK;
    	JSONResponse response = getResponseOK(function.apply(null));
        return new ResponseEntity<JSONResponse>(response, httpStatus);
    }
	
	protected <T> JSONResponse  getResponseOK(T data) {
		JSONResponse response = new JSONResponse();
        response.setData(data);
   		response.setStatus(JSONResponse.OK);
		return response;
	}
	
	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
    @Resource(name = "userService")
	public void setUserService(UserService userService) {
		userEPservice = userService;
	}
    
}

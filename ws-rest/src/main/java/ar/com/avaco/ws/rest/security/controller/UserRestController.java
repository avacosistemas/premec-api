package ar.com.avaco.ws.rest.security.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.rest.controller.AbsctractRestController;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.security.dto.User;
import ar.com.avaco.ws.rest.security.service.UserService;
import ar.com.avaco.ws.rest.security.util.JwtTokenUtil;

/**
 * @author beto
 *
 */
@RestController
public class UserRestController extends AbsctractRestController<User, Long, UserService>{
    
	private JwtTokenUtil jwtTokenUtil;

    private UserDetailsService userDetailsService;
    
    @Value("${jwt.header}")
    private String tokenHeader;

	
	//-------------------Retrieve All permisos--------------------------------------------------------    

	@RequestMapping(value = "/users/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONResponse> list() {
    	return super.list();
    }
    
    //-------------------Retrieve single Pages--------------------------------------------------------  
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONResponse> get(@PathVariable("id") Long id) throws BusinessException {
    	return super.get(id);
    }
       
    //-------------------Create a Page--------------------------------------------------------    
    @RequestMapping(value = "/users/", method = RequestMethod.POST)
    public  ResponseEntity<JSONResponse> create(@RequestBody User user) throws BusinessException {
    	User result = this.service.saveUser(user);
		return new ResponseEntity<JSONResponse>(getResponseOK(result),HttpStatus.CREATED);
    }
    
    //------------------- Update a Page --------------------------------------------------------    
    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<JSONResponse> update(@PathVariable("id") Long id, @RequestBody User user) throws BusinessException {
    	return super.update(id, user);
    }
    
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<JSONResponse> delete(@PathVariable("id") Long id) throws BusinessException {
    	return super.delete(id);
    }
    
	@Resource(name = "userService")
	public void setUserService(UserService userService) {
		super.service = userService;
	}

    @SuppressWarnings("deprecation")
	@RequestMapping(value = "/users/update/validation/", method = RequestMethod.POST)
    public ResponseEntity<JSONResponse> updateValidation(@RequestBody User user) throws Exception {
    	return super.executeProcess("update-validation", Void -> { this.service.updateValidation(user); return null;});
    }
	
}
package ar.com.avaco.ws.rest.security.controller;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.rest.controller.AbsctractRestController;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.security.dto.Permission;
import ar.com.avaco.ws.rest.security.service.PermissionService;

@RestController
public class PermissionRestController extends AbsctractRestController<Permission, Long, PermissionService>{

	//-------------------Retrieve All permisos--------------------------------------------------------    

	@RequestMapping(value = "/permissions/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONResponse> list() {
    	return super.list();
    }
    
    //-------------------Retrieve single Pages--------------------------------------------------------  
    @RequestMapping(value = "/permissions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONResponse> get(@PathVariable("id") Long id) throws BusinessException {
    	return super.get(id);
    }
       
    //-------------------Create a Page--------------------------------------------------------    
    @RequestMapping(value = "/permissions/", method = RequestMethod.POST)
    public  ResponseEntity<JSONResponse> create(@RequestBody Permission permission) throws BusinessException {
    	return super.create(permission);
    }
    
    //------------------- Update a Page --------------------------------------------------------    
    @RequestMapping(value = "/permissions/{id}", method = RequestMethod.PUT)
    public ResponseEntity<JSONResponse> update(@PathVariable("id") Long id, @RequestBody Permission permission) throws BusinessException {
    	return super.update(id, permission);
    }
    //------------------- Delete a Page --------------------------------------------------------
    
    @RequestMapping(value = "/permissions/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<JSONResponse> delete(@PathVariable("id") Long id) throws BusinessException {
    	return super.delete(id);
    }
    	
	@Resource(name = "permissionService")
	public void setPermissionService(PermissionService permissionService) {
		super.service = permissionService;
	}

}
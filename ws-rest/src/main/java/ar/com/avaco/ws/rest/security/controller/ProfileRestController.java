package ar.com.avaco.ws.rest.security.controller;

import javax.annotation.Resource;

import org.springframework.dao.DataIntegrityViolationException;
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
import ar.com.avaco.ws.rest.security.dto.Profile;
import ar.com.avaco.ws.rest.security.service.ProfileService;

@RestController
public class ProfileRestController extends AbsctractRestController<Profile, Long, ProfileService>{

	//-------------------Retrieve All permisos--------------------------------------------------------    

	@RequestMapping(value = "/profiles/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONResponse> list() {
    	return super.list();
    }
    
    //-------------------Retrieve single Pages--------------------------------------------------------  
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONResponse> get(@PathVariable("id") Long id) throws BusinessException {
    	return super.get(id);
    }
       
    //-------------------Create a Page--------------------------------------------------------    
    @RequestMapping(value = "/profiles/", method = RequestMethod.POST)
    public  ResponseEntity<JSONResponse> create(@RequestBody Profile profile) throws BusinessException {
    	return super.create(profile);
    }
    
    //------------------- Update a Page --------------------------------------------------------    
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.PUT)
    public ResponseEntity<JSONResponse> update(@PathVariable("id") Long id, @RequestBody Profile profile) throws BusinessException {
    	return super.update(id, profile);
    }
    //------------------- Delete a Page --------------------------------------------------------
    
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<JSONResponse> delete(@PathVariable("id") Long id) throws BusinessException {
    	ResponseEntity<JSONResponse> resp;
    	try {
    		resp = super.delete(id);
    	} catch(DataIntegrityViolationException e) {
    		throw new BusinessException("No se puede borrar el Perfil porque existe una relacion con Acceso");
    	} catch(Exception e) {
    		throw new BusinessException("Ocurrio un error al eliminar el perfil");
    	}
    	
    	return resp;
    }
    	
	@Resource(name = "profileService")
	public void setProfileService(ProfileService profileService) {
		super.service = profileService;
	}

}
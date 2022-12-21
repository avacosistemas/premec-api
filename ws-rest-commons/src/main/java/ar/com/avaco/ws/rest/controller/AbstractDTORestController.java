package ar.com.avaco.ws.rest.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.rest.dto.DTOEntity;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.service.CRUDEPService;
import ar.com.avaco.ws.rest.service.FunctionBusiness;
import ar.com.avaco.ws.rest.service.SupplierBusiness;

/**
 * @author avaco
 */
public abstract class AbstractDTORestController<RDTO extends DTOEntity<ID>, ID extends Serializable, T extends CRUDEPService<ID,RDTO>> extends AbstractRestBaseController{	

	private static final Logger LOGGER = Logger.getLogger(AbstractDTORestController.class);
	protected static final String ENTITY_WITH_ID_0_NOT_FOUND = "Entity with id {0} not found";
	protected static final String ENTITY_WITH_NAME_0_NOT_FOUND = "Entity with name {0} not found";
	
	protected T service;
	
	//-------------------Retrieve All Formalities--------------------------------------------------------    
	//@RequestMapping(value = "/formalities/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONResponse> list() {
        return this.list(null);
    }
    
    public <R> ResponseEntity<JSONResponse>  list(Function<RDTO,R> function) {
    	LOGGER.info("Fetching entities list");
    	JSONResponse response = new JSONResponse();
    	List<RDTO> entities =  this.service.list();
    	if(function == null) {    		
    		response.setData(entities);
    	}else {
    		List<R> dtos = new ArrayList<R>();
    		for (RDTO es : entities) {
    			dtos.add(function.apply(es));
			}
    		response.setData(dtos);
    	}
		response.setStatus(JSONResponse.OK);	
        return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
    }
    
    public <R> ResponseEntity<JSONResponse>  listFiltered(Predicate<RDTO> predicate) {
    	LOGGER.info("Fetching entities list");
    	JSONResponse response = new JSONResponse();
    	List<RDTO> entities =  this.service.list();
    	if(predicate == null) {    		
    		response.setData(entities);
    	}else {
    		List<RDTO> dtos = entities
    				.stream()    				
    				.filter(predicate)
    				.collect(Collectors.toList());    		
    		response.setData(dtos);
    	}
		response.setStatus(JSONResponse.OK);	
        return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
    }
    
    //-------------------Retrieve Single newFormality--------------------------------------------------------  
    //@RequestMapping(value = "/formalities/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONResponse> get(ID id) throws BusinessException {
    	LOGGER.info("Fetching Entity with id " + id);
    	HttpStatus httpStatus = HttpStatus.OK;
    	RDTO entity = this.service.get(id);
    	JSONResponse response = null;
    	if(entity == null) {
    		response = new JSONResponse();
    		response.setStatus(JSONResponse.ERROR);
    		response.setData(entity);
    		httpStatus = HttpStatus.NOT_FOUND;
    	}else {
    		response = getResponseOK(entity);
    	}
        return new ResponseEntity<JSONResponse>(response, httpStatus);
    }

    public ResponseEntity<JSONResponse> getByFunction(FunctionBusiness<Void,RDTO> function) throws BusinessException {
    	LOGGER.info("Fetching Entity with supplier");
    	HttpStatus httpStatus = HttpStatus.OK;
    	JSONResponse response = getResponseOK(function.apply(null));
        return new ResponseEntity<JSONResponse>(response, httpStatus);
    }
    
    //-------------------Create a Entity--------------------------------------------------------    
    //@RequestMapping(value = "/entities/", method = RequestMethod.POST)
    public  ResponseEntity<JSONResponse> create(RDTO dto) throws BusinessException {
    	LOGGER.info("Create a entity");    	
    	RDTO result = this.service.save(dto);
    	LOGGER.info("Entity created" );    	
    	return new ResponseEntity<JSONResponse>(getResponseOK(result),HttpStatus.CREATED);
    }
    
    public  ResponseEntity<JSONResponse> create(SupplierBusiness<RDTO> supplier) throws BusinessException {
    	LOGGER.info("Create a entity with supplier");    	
    	RDTO entityService = supplier.get();
    	LOGGER.info("Entity created" );    	
    	return new ResponseEntity<JSONResponse>(getResponseOK(entityService),HttpStatus.CREATED);
    }

	//------------------- Update a Entity --------------------------------------------------------    
    //@RequestMapping(value = "/entities/{id}", method = RequestMethod.PUT)
    public ResponseEntity<JSONResponse> update(ID id, RDTO dto) throws BusinessException {
    	LOGGER.info("Updating Entity " + id);
    	dto.setId(id);
        dto = this.service.update(dto);
        return new ResponseEntity<JSONResponse>(getResponseOK(dto), HttpStatus.OK);
    }
    
	@SuppressWarnings("hiding")
	protected <T> JSONResponse  getResponseOK(T data) {
		JSONResponse response = new JSONResponse();
        response.setData(data);
   		response.setStatus(JSONResponse.OK);
		return response;
	}
    
    //------------------- Delete a Entity --------------------------------------------------------
    
	//@RequestMapping(value = "/entities/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<JSONResponse> delete(ID id) throws BusinessException {
    	LOGGER.info("Fetching & Deleting Entity with id " + id);
        try {
        	this.service.remove(id);
        } catch (Exception e) {
        	e.printStackTrace();
        	JSONResponse response = new JSONResponse();
            response.setData("No se puede borrar el elemento seleccionado. Es probable que esté asociado por otra sección de la aplicación.");
       		response.setStatus(JSONResponse.OK);
       		return new ResponseEntity<JSONResponse>(response, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<JSONResponse>(getResponseOK(null),HttpStatus.OK);
    }
    
//	@RequestMapping(value = "/entities/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<JSONResponse> deleteAll(Map<String, String> customQuery) throws BusinessException {
    	String ids = customQuery.get("ids");
    	LOGGER.info("Fetching & Deleting Entity with ids " + ids);
    	StringTokenizer tokenizer = new StringTokenizer(ids, ",");
    	while(tokenizer.hasMoreElements()) {    		
    		this.service.remove(convertStringToID(((String)tokenizer.nextElement())));
    	}
        return new ResponseEntity<JSONResponse>(getResponseOK(null),HttpStatus.OK);
    }
    
    @SuppressWarnings("unchecked")
	protected ID convertStringToID(String string) {
    	return (ID) Integer.valueOf(string);
    }
 
    public abstract void setService(T service);
    
    protected AbstractFilter transformToAbstractFilter(Map<String, String> customQuery) {
    	return null;
    }
}
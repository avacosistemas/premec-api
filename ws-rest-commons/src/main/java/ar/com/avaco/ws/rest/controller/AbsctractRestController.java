package ar.com.avaco.ws.rest.controller;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.rest.dto.ErrorResponse;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.service.FunctionBusiness;
import ar.com.avaco.ws.rest.service.SupplierBusiness;

/**
 * @author avaco
 */
@Deprecated
public abstract class AbsctractRestController<RDTO extends Entity<ID>, ID extends Serializable, T extends NJService<ID,RDTO>> {	

	private static final Logger LOGGER = Logger.getLogger(AbsctractRestController.class);
	protected static final String ENTITY_WITH_ID_0_NOT_FOUND = "Entity with id {0} not found";
	protected static final String ENTITY_WITH_NAME_0_NOT_FOUND = "Entity with name {0} not found";
	
	protected static final String ERROR = "ERROR";
	protected static final String OK = "OK";
	
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
		response.setStatus(OK);	
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
    		ErrorResponse eresp = new ErrorResponse();
    		eresp.setStatus(ERROR);
    		eresp.setError(MessageFormat.format(ENTITY_WITH_ID_0_NOT_FOUND,id));    		
    		eresp.setData(entity);
    		httpStatus = HttpStatus.NOT_FOUND;
    		response = eresp;
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
    
    public <R> ResponseEntity<JSONResponse> executeProcess(String processName, FunctionBusiness<Void, R> function) throws Exception {
    	LOGGER.info("Run process " + processName);
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
   		response.setStatus(OK);
		return response;
	}
    
    //------------------- Delete a Entity --------------------------------------------------------
    
	//@RequestMapping(value = "/entities/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<JSONResponse> delete(ID id) throws BusinessException {
    	LOGGER.info("Fetching & Deleting Entity with id " + id);
        this.service.remove(id);
        return new ResponseEntity<JSONResponse>(getResponseOK(null),HttpStatus.OK);
    }
    
	//@RequestMapping(value = "/entities/{id}", method = RequestMethod.DELETE)
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
}
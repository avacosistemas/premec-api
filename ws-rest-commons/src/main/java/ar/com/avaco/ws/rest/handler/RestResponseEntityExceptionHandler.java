package ar.com.avaco.ws.rest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ar.com.avaco.ws.rest.dto.ErrorResponse;

/**
 * 
 * @author avaco
 *
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> subhandleException(final Exception ex,WebRequest wr) {
        return new ResponseEntity<ErrorResponse>(getResponse(ex, HttpStatus.CONFLICT), HttpStatus.CONFLICT);
	}

	private ErrorResponse getResponse(final Exception ex,HttpStatus status) {
		ErrorResponse response = new ErrorResponse();
		if(ex.getMessage() != null && !ex.getMessage().isEmpty()) {
			response.setMessage(ex.getMessage());
		}
		
		if(ex.getCause() != null) {
			response.setError(ex.getCause().getMessage());			
		}
		
		response.setStatus(status.name());
		return response;
	}

}
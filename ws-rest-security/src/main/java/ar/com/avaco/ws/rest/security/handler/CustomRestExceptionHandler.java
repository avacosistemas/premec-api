package ar.com.avaco.ws.rest.security.handler;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.ws.rest.dto.ErrorResponse;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String CHANGE_PASSWORD_REQUIRED = "CHANGE_PASSWORD_REQUIRED";
    private static final String VALIDATIONS_ERRORS = "VALIDATIONS_ERRORS";
    private static final String BAD_CREDENTIAL = "BAD_CREDENTIAL";

	// 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        logger.info(ex.getClass().getName());
    	ex.printStackTrace();
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        logger.info(ex.getClass().getName());
    	ex.printStackTrace();
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        logger.info(ex.getClass().getName());
    	ex.printStackTrace();
        //
        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        logger.info(ex.getClass().getName());
    	ex.printStackTrace();
        //
        final String error = ex.getRequestPartName() + " part is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        logger.info(ex.getClass().getName());
    	ex.printStackTrace();
        //
        final String error = ex.getParameterName() + " parameter is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    //

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
//        logger.info(ex.getClass().getName());
    	ex.printStackTrace();
        //
        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
//        logger.info(ex.getClass().getName());
    	ex.printStackTrace();
        //
        final List<String> errors = new ArrayList<String>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        logger.info(ex.getClass().getName());
        //
    	ex.printStackTrace();
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        logger.info(ex.getClass().getName());
        //
    	ex.printStackTrace();
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        logger.info(ex.getClass().getName());
        //
    	ex.printStackTrace();
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 409

	@ExceptionHandler(CredentialsExpiredException.class)
	public ResponseEntity<ErrorResponse> subhandleException(final CredentialsExpiredException ex,WebRequest wr) {
		ex.printStackTrace();
        return new ResponseEntity<ErrorResponse>(getResponse(ex, CHANGE_PASSWORD_REQUIRED), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> subhandleException(final BadCredentialsException ex,WebRequest wr) {
		ex.printStackTrace();
        return new ResponseEntity<ErrorResponse>(getResponse(ex, BAD_CREDENTIAL), HttpStatus.CONFLICT);
	}
	
	
    // 500 - Generals errors
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> subhandleException(final Exception ex,WebRequest wr) {
		ex.printStackTrace();
        return new ResponseEntity<ErrorResponse>(getResponse(ex, HttpStatus.CONFLICT), HttpStatus.CONFLICT);
	}
	
	// Validations Errors
	@ExceptionHandler(ErrorValidationException.class)
	public ResponseEntity<ErrorResponse> subhandleException(final ErrorValidationException ex,WebRequest wr) {
		ex.printStackTrace();
		ErrorResponse response = getResponse(ex, VALIDATIONS_ERRORS);
		response.setErrors(ex.getErrors());
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.CONFLICT);
	}
	
	private ErrorResponse getResponse(final Exception ex,HttpStatus status) {
		ex.printStackTrace();
		ErrorResponse response = getErrorResponse(ex);
		response.setStatus(status.name());
		return response;
	}

	private ErrorResponse getErrorResponse(final Exception ex) {
		ex.printStackTrace();
		ErrorResponse response = new ErrorResponse();
		if(ex.getMessage() != null && !ex.getMessage().isEmpty()) {
			response.setMessage(ex.getMessage());
		}
		
		if(ex.getCause() != null) {
			response.setError(ex.getCause().getMessage());			
		}
		return response;
	}
	
	private ErrorResponse getResponse(final Exception ex,String status) {
		ex.printStackTrace();
		ErrorResponse response = getErrorResponse(ex);
		response.setStatus(status);
		return response;
	}

}
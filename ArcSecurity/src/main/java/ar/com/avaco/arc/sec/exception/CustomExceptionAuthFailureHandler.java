package ar.com.avaco.arc.sec.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/** */
public class CustomExceptionAuthFailureHandler implements Serializable {
	
	/** */
	private static final long serialVersionUID = -6811434050313978675L;
	
	/** */
	private final Map<String, String> failureUrlMap = new HashMap<String, String>(); 
	
	public String getRedirectAuthenticationFailureResponse(RuntimeException exception){
        String url = failureUrlMap.get(exception.getCause().getClass().getName());

        if (url == null) {
        	throw exception;
        } 
            
        return url;
	}
	
    /**
     * Sets the map of exception types (by name) to URLs.
     *
     * @param failureUrlMap the map keyed by the fully-qualified name of the exception class, with the corresponding
     * failure URL as the value.
     *
     * @throws IllegalArgumentException if the entries are not Strings or the URL is not valid.
     */
    public void setExceptionMappings(Map<?,?> failureUrlMap) {
        this.failureUrlMap.clear();
        for (Map.Entry<?,?> entry : failureUrlMap.entrySet()) {
            Object exception = entry.getKey();
            Object url = entry.getValue();
            Assert.isInstanceOf(String.class, exception, "Exception key must be a String (the exception classname).");
            Assert.isInstanceOf(String.class, url, "URL must be a String");
            this.failureUrlMap.put((String)exception, (String)url);
        }
    }
}
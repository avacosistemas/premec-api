/**
 * 
 */
package ar.com.avaco.ws.rest.dto;

/**
 * 
 *
 */
public class JSONResponse {
	
	public static final String ERROR = "ERROR";
	public static final String OK = "OK";
	
	private String status;
	private Object data;
	
	public JSONResponse() {

	}

	public JSONResponse(String status, Object data) {
		super();
		this.status = status;
		this.data = data;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
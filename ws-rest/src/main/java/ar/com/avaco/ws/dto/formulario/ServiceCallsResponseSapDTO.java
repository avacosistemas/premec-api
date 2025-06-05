package ar.com.avaco.ws.dto.formulario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCallsResponseSapDTO {

	@JsonProperty("ServiceCallID")
	private Long serviceCallID;

	@JsonProperty("ResponseAssignee")
	private Long responseAssignee;

	@JsonProperty("ItemCode")
	private String itemCode;

	@JsonProperty("Subject")
	private String subject;

	@JsonProperty("InternalSerialNum")
	private String internalSerialNum;

	@JsonProperty("CustomerName")
	private String customerName;

	@JsonProperty("ManufacturerSerialNum")
	private String manufacturerSerialNum;

	public Long getServiceCallID() {
		return serviceCallID;
	}

	public void setServiceCallID(Long serviceCallID) {
		this.serviceCallID = serviceCallID;
	}

	public Long getResponseAssignee() {
		return responseAssignee;
	}

	public void setResponseAssignee(Long responseAssignee) {
		this.responseAssignee = responseAssignee;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getInternalSerialNum() {
		return internalSerialNum;
	}

	public void setInternalSerialNum(String internalSerialNum) {
		this.internalSerialNum = internalSerialNum;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getManufacturerSerialNum() {
		return manufacturerSerialNum;
	}

	public void setManufacturerSerialNum(String manufacturerSerialNum) {
		this.manufacturerSerialNum = manufacturerSerialNum;
	}

}

package ar.com.avaco.ws.dto;

import java.util.ArrayList;
import java.util.List;

public class RegistroInformeServicioDTO {

	// Service Call
	private Long serviceCallID;
	private String itemCode;
	private String itemDescription;
	private String manufacturerSerialNum;
	private String InternalSerialNum;
	private List<RegistroInformeActividadDTO> actividades = new ArrayList<RegistroInformeActividadDTO>();

	public Long getServiceCallID() {
		return serviceCallID;
	}

	public void setServiceCallID(Long serviceCallID) {
		this.serviceCallID = serviceCallID;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public List<RegistroInformeActividadDTO> getActividades() {
		return actividades;
	}

	public void setActividades(List<RegistroInformeActividadDTO> actividades) {
		this.actividades = actividades;
	}

	public String getManufacturerSerialNum() {
		return manufacturerSerialNum;
	}

	public void setManufacturerSerialNum(String manufacturerSerialNum) {
		this.manufacturerSerialNum = manufacturerSerialNum;
	}

	public String getInternalSerialNum() {
		return InternalSerialNum;
	}

	public void setInternalSerialNum(String internalSerialNum) {
		InternalSerialNum = internalSerialNum;
	}

}

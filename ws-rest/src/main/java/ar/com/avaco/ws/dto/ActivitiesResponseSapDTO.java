package ar.com.avaco.ws.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivitiesResponseSapDTO {

	@JsonProperty("ActivityCode")
	private Long activityCode;

	@JsonProperty("U_Taller")
	private String esTaller;

	@JsonProperty("U_T_Tarea")
	private String tipoTarea;

	@JsonProperty("Priority")
	private String priority;

	@JsonProperty("StartDate")
	private String startDate;

	@JsonProperty("ActivityTime")
	private String activityTime;

	@JsonProperty("Details")
	private String details;

	@JsonProperty("U_ConCargo")
	private String conCargo;

	@JsonProperty("HandledByEmployee")
	private Long handledByEmployee;

	@JsonProperty("Location")
	private Long location;

	@JsonProperty("ParentObjectId")
	private Long parentObjectId;

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public String getEsTaller() {
		return esTaller;
	}

	public void setEsTaller(String esTaller) {
		this.esTaller = esTaller;
	}

	public String getTipoTarea() {
		return tipoTarea;
	}

	public void setTipoTarea(String tipoTarea) {
		this.tipoTarea = tipoTarea;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getConCargo() {
		return conCargo;
	}

	public void setConCargo(String conCargo) {
		this.conCargo = conCargo;
	}

	public Long getHandledByEmployee() {
		return handledByEmployee;
	}

	public void setHandledByEmployee(Long handledByEmployee) {
		this.handledByEmployee = handledByEmployee;
	}

	public Long getLocation() {
		return location;
	}

	public void setLocation(Long location) {
		this.location = location;
	}

	public Long getParentObjectId() {
		return parentObjectId;
	}

	public void setParentObjectId(Long parentObjectId) {
		this.parentObjectId = parentObjectId;
	}

}

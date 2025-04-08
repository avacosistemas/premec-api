package ar.com.avaco.ws.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCallActivitiesResponseSapDTO {

	@JsonProperty("ActivityCode")
	private Long activityCode;

	@JsonProperty("U_U_HsMaq")
	private String horasMaquina;

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public String getHorasMaquina() {
		return horasMaquina;
	}

	public void setHorasMaquina(String horasMaquina) {
		this.horasMaquina = horasMaquina;
	}

}

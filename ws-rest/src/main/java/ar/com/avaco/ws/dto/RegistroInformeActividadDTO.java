package ar.com.avaco.ws.dto;

public class RegistroInformeActividadDTO {

	private Long activityCode;
	private String activityDate;
	private String activityTime;
	private String estado;
	private Boolean imprimeReporte;

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Boolean getImprimeReporte() {
		return imprimeReporte;
	}

	public void setImprimeReporte(Boolean imprimeReporte) {
		this.imprimeReporte = imprimeReporte;
	}

}

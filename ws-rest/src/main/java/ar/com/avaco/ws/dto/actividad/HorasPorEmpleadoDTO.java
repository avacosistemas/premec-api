package ar.com.avaco.ws.dto.actividad;

import java.util.Date;

public class HorasPorEmpleadoDTO {

	private int attendEmpl;
	private Date seStartDat;
	private int duration;
	private String tieneActNoTallerDespuesMediodia;

	public HorasPorEmpleadoDTO() {
	}

	public HorasPorEmpleadoDTO(int attendEmpl, Date seStartDat, int duration, String tieneActNoTallerDespuesMediodia) {
		this.attendEmpl = attendEmpl;
		this.seStartDat = seStartDat;
		this.duration = duration;
		this.tieneActNoTallerDespuesMediodia = tieneActNoTallerDespuesMediodia;
	}

	public int getAttendEmpl() {
		return attendEmpl;
	}

	public void setAttendEmpl(int attendEmpl) {
		this.attendEmpl = attendEmpl;
	}

	public Date getSeStartDat() {
		return seStartDat;
	}

	public void setSeStartDat(Date seStartDat) {
		this.seStartDat = seStartDat;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getTieneActNoTallerDespuesMediodia() {
		return tieneActNoTallerDespuesMediodia;
	}

	public void setTieneActNoTallerDespuesMediodia(String tieneActNoTallerDespuesMediodia) {
		this.tieneActNoTallerDespuesMediodia = tieneActNoTallerDespuesMediodia;
	}

}
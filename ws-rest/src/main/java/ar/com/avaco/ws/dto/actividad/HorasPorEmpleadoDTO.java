package ar.com.avaco.ws.dto.actividad;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HorasPorEmpleadoDTO {

	private int attendEmpl;
	private Date seStartDat;
	private int duration;
	private String tieneActNoTallerDespuesMediodia;
	private int pagoHorasProductivas;

	public HorasPorEmpleadoDTO() {
	}

	public HorasPorEmpleadoDTO(int attendEmpl, Date seStartDat, int duration, String tieneActNoTallerDespuesMediodia,
			int pagoHorasProductivas) {
		this.attendEmpl = attendEmpl;
		this.seStartDat = seStartDat;
		this.duration = duration;
		this.tieneActNoTallerDespuesMediodia = tieneActNoTallerDespuesMediodia;
		this.pagoHorasProductivas = pagoHorasProductivas;
	}

	public int getPagoHorasProductivas() {
		return pagoHorasProductivas;
	}

	public void setPagoHorasProductivas(int pagoHorasProductivas) {
		this.pagoHorasProductivas = pagoHorasProductivas;
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

	@JsonProperty("durationHours")
	public String getDurationHours() {
		int hours = duration / 3600;
		int minutes = (duration % 3600) / 60;
		int seconds = duration % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

}
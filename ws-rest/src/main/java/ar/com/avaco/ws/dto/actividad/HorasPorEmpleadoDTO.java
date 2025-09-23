package ar.com.avaco.ws.dto.actividad;

import java.util.Date;

public class HorasPorEmpleadoDTO {

	private int attendEmpl;
	private Date seStartDat;
	private int duration;

	public HorasPorEmpleadoDTO() {
	}

	public HorasPorEmpleadoDTO(int attendEmpl, Date seStartDat, int duration) {
		this.attendEmpl = attendEmpl;
		this.seStartDat = seStartDat;
		this.duration = duration;
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

	@Override
	public String toString() {
		return "HorasPorEmpleadoDTO{" + "attendEmpl=" + attendEmpl + ", seStartDat=" + seStartDat + ", duration="
				+ duration + '}';
	}
}
package ar.com.avaco.ws.dto;

import java.util.HashMap;
import java.util.Map;

public class ActividadPach {

	private String Notes; // observacionesGenerales
	private String StartDate; // fechaHoraInicio fecha
	private String StartTime; // fechaHoraInicio hora
	private String EndDueDate; // fechaHoraFin fecha
	private String EndTime; // fechaHoraFin hora
	private String U_Valoracion; // valoracion;
	private String Details; // comentarios
	private String U_NomSupervisor; // nombreSupervisor
	private String U_DniSupervisor; // dniSupervisor
	private String U_Repuestos; // serializar listado en json
	private String U_Tareasreal; // listado de checks

	public Map<String, Object> getAsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Notes", this.Notes);
		map.put("StartDate", this.StartDate);
		map.put("StartTime", this.StartTime);
		map.put("EndDueDate", this.EndDueDate);
		map.put("EndTime", this.EndTime);
		map.put("U_Valoracion", this.U_Valoracion);
		map.put("Details", this.Details);
		map.put("U_NomSupervisor", this.U_NomSupervisor);
		map.put("U_DniSupervisor", this.U_DniSupervisor);
		map.put("U_Repuestos", this.U_Repuestos);
		map.put("U_Tareasreal", this.U_Tareasreal);
		return map;
	}
	
	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndDueDate() {
		return EndDueDate;
	}

	public void setEndDueDate(String endDueDate) {
		EndDueDate = endDueDate;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getU_Valoracion() {
		return U_Valoracion;
	}

	public void setU_Valoracion(String u_Valoracion) {
		U_Valoracion = u_Valoracion;
	}

	public String getDetails() {
		return Details;
	}

	public void setDetails(String details) {
		Details = details;
	}

	public String getU_NomSupervisor() {
		return U_NomSupervisor;
	}

	public void setU_NomSupervisor(String u_NomSupervisor) {
		U_NomSupervisor = u_NomSupervisor;
	}

	public String getU_DniSupervisor() {
		return U_DniSupervisor;
	}

	public void setU_DniSupervisor(String u_DniSupervisor) {
		U_DniSupervisor = u_DniSupervisor;
	}

	public String getU_Repuestos() {
		return U_Repuestos;
	}

	public void setU_Repuestos(String u_Repuestos) {
		U_Repuestos = u_Repuestos;
	}

	public String getU_Tareasreal() {
		return U_Tareasreal;
	}

	public void setU_Tareasreal(String u_Tareasreal) {
		U_Tareasreal = u_Tareasreal;
	}

}

package ar.com.avaco.ws.dto;

import java.util.HashMap;
import java.util.Map;

public class ActividadPatch {

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
	private String U_Estado;
	private String DocEntry;
	private String U_ConCargo;
	private String U_ValoracionComent;
	private String U_Tareas_Real;
	private String AttachmentEntry;

	public Map<String, Object> getAsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Notes", this.Notes);
		map.put("StartDate", this.StartDate);
		map.put("StartTime", this.StartTime);
		map.put("EndDueDate", this.EndDueDate);
		map.put("EndTime", this.EndTime);
		map.put("U_Valoracion", this.U_Valoracion);
		map.put("U_NomSupervisor", this.U_NomSupervisor);
		map.put("U_DniSupervisor", this.U_DniSupervisor);
		map.put("U_Repuestos", this.U_Repuestos);
		map.put("U_Tareasreal", this.U_Tareasreal);
		map.put("U_Estado", U_Estado);
		map.put("U_ConCargo", U_ConCargo);
		map.put("U_ValoracionComent", U_ValoracionComent);
//		map.put("DocEntry", DocEntry);
		map.put("U_Tareas_Real", U_Tareas_Real);
		map.put("AttachmentEntry", AttachmentEntry);
		return map;
	}

	public String getU_ConCargo() {
		return U_ConCargo;
	}

	public void setU_ConCargo(String u_ConCargo) {
		U_ConCargo = u_ConCargo;
	}

	public String getDocEntry() {
		return DocEntry;
	}

	public void setDocEntry(String docEntry) {
		DocEntry = docEntry;
	}

	public String getU_Estado() {
		return U_Estado;
	}

	public void setU_Estado(String u_Estado) {
		U_Estado = u_Estado;
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

	public String getU_ValoracionComent() {
		return U_ValoracionComent;
	}

	public void setU_ValoracionComent(String u_ValoracionComent) {
		U_ValoracionComent = u_ValoracionComent;
	}

	public String getU_Tareas_Real() {
		return U_Tareas_Real;
	}

	public void setU_Tareas_Real(String u_Tareas_Real) {
		U_Tareas_Real = u_Tareas_Real;
	}

	public String getAttachmentEntry() {
		return AttachmentEntry;
	}

	public void setAttachmentEntry(String attachmentEntry) {
		AttachmentEntry = attachmentEntry;
	}

}

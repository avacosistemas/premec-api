package ar.com.avaco.ws.dto.timesheet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectManagementTimeSheetGetDTO {

	@JsonProperty(value = "AbsEntry")
	private Long absEntry;

	@JsonProperty(value = "UserID")
	private String userId;

	@JsonProperty(value = "DateFrom")
	private String dateFrom;

	@JsonProperty(value = "DateTo")
	private String dateTo;

	@JsonProperty(value = "U_serviciotecnico")
	private String servicioTecnico;

	@JsonProperty(value = "U_objetivo")
	private String objetivo;

	@JsonProperty(value = "U_cumplimientoobjetivo")
	private String cumplimientoObjetivo;

	@JsonProperty(value = "U_viaticos")
	private String viaticos;

	@JsonProperty(value = "U_adelanto")
	private String adelanto;

	@JsonProperty(value = "U_prestamo")
	private String prestamo;

	@JsonProperty(value = "U_premio")
	private String premio;

	@JsonProperty(value = "U_porcentajevaloracion")
	private String porcentajevaloracion;

	@JsonProperty(value = "U_porcentajeefectividad")
	private String porcentajeEfectividad;

	@JsonProperty(value = "U_cantidadactividades")
	private String cantidadactividades;

	@JsonProperty(value = "PM_TimeSheetLineDataCollection")
	private Set<ProjectManagementTimeSheetLineGetDTO> lineas = new HashSet<>();

	private Set<Map<String, Object>> getLinesAsMap() {
		Set<Map<String, Object>> mapLines = new HashSet<>();
		lineas.forEach(linea -> mapLines.add(linea.getAsMapNewLine()));
		return mapLines;
	}
	
	public Map<String, Object> getAsMapUpdateTimeSheetLines() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PM_TimeSheetLineDataCollection", this.getLinesAsMap());
		return map;
	}
	
	public Long getAbsEntry() {
		return absEntry;
	}

	public void setAbsEntry(Long absEntry) {
		this.absEntry = absEntry;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getServicioTecnico() {
		return servicioTecnico;
	}

	public void setServicioTecnico(String servicioTecnico) {
		this.servicioTecnico = servicioTecnico;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getCumplimientoObjetivo() {
		return cumplimientoObjetivo;
	}

	public void setCumplimientoObjetivo(String cumplimientoObjetivo) {
		this.cumplimientoObjetivo = cumplimientoObjetivo;
	}

	public String getViaticos() {
		return viaticos;
	}

	public void setViaticos(String viaticos) {
		this.viaticos = viaticos;
	}

	public String getAdelanto() {
		return adelanto;
	}

	public void setAdelanto(String adelanto) {
		this.adelanto = adelanto;
	}

	public String getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(String prestamo) {
		this.prestamo = prestamo;
	}

	public String getPremio() {
		return premio;
	}

	public void setPremio(String premio) {
		this.premio = premio;
	}

	public String getPorcentajevaloracion() {
		return porcentajevaloracion;
	}

	public void setPorcentajevaloracion(String porcentajevaloracion) {
		this.porcentajevaloracion = porcentajevaloracion;
	}

	public String getPorcentajeEfectividad() {
		return porcentajeEfectividad;
	}

	public void setPorcentajeEfectividad(String porcentajeEfectividad) {
		this.porcentajeEfectividad = porcentajeEfectividad;
	}

	public String getCantidadactividades() {
		return cantidadactividades;
	}

	public void setCantidadactividades(String cantidadactividades) {
		this.cantidadactividades = cantidadactividades;
	}

	public Set<ProjectManagementTimeSheetLineGetDTO> getLineas() {
		return lineas;
	}

	public void setLineas(Set<ProjectManagementTimeSheetLineGetDTO> lineas) {
		this.lineas = lineas;
	}

}

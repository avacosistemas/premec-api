package ar.com.avaco.ws.dto;

import java.util.ArrayList;
import java.util.List;

public class FormularioDTO {

	private Long idActividad;
	private String observacionesGenerales;
	private String fechaHoraInicio;
	private String fechaHoraFin;
	private String valoracion;
	private String comentarios;
	private String nombreSupervisor;
	private Long dniSupervisor;
	private String conCargo;
	private String horasMaquina;

	private String tipoMaquina;
	private List<ItemCheckDTO> checkList = new ArrayList<>();
	private List<RepuestoDTO> repuestos = new ArrayList<>();
	private List<FotoDTO> fotos = new ArrayList<>();

	public Long getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(Long idActividad) {
		this.idActividad = idActividad;
	}

	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}

	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}

	public String getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(String fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	public String getFechaHoraFin() {
		return fechaHoraFin;
	}

	public void setFechaHoraFin(String fechaHoraFin) {
		this.fechaHoraFin = fechaHoraFin;
	}

	public String getValoracion() {
		return valoracion;
	}

	public void setValoracion(String valoracion) {
		this.valoracion = valoracion;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getNombreSupervisor() {
		return nombreSupervisor;
	}

	public void setNombreSupervisor(String nombreSupervisor) {
		this.nombreSupervisor = nombreSupervisor;
	}

	public Long getDniSupervisor() {
		return dniSupervisor;
	}

	public void setDniSupervisor(Long dniSupervisor) {
		this.dniSupervisor = dniSupervisor;
	}

	public String getConCargo() {
		return conCargo;
	}

	public void setConCargo(String conCargo) {
		this.conCargo = conCargo;
	}

	public String getTipoMaquina() {
		return tipoMaquina;
	}

	public void setTipoMaquina(String tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}

	public List<ItemCheckDTO> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<ItemCheckDTO> checkList) {
		this.checkList = checkList;
	}

	public List<RepuestoDTO> getRepuestos() {
		return repuestos;
	}

	public void setRepuestos(List<RepuestoDTO> repuestos) {
		this.repuestos = repuestos;
	}

	public List<FotoDTO> getFotos() {
		return fotos;
	}

	public void setFotos(List<FotoDTO> fotos) {
		this.fotos = fotos;
	}

	public String getHorasMaquina() {
		return horasMaquina;
	}

	public void setHorasMaquina(String horasMaquina) {
		this.horasMaquina = horasMaquina;
	}

}

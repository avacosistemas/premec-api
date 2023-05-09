package ar.com.avaco.ws.dto;

import java.util.List;

public class FormularioDTO {

	Long idActividad;
	String observacionesGenerales;
	String fechaHoraInicio;
	String fechaHoraFin;
	String valoracion;
	String comentarios;
	String nombreSupervisor;
	Long dniSupervisor;

	String tipoMaquina;
	List<ItemCheckDTO> checkList;
	List<RepuestoDTO> repuestos;
	List<FotoDTO> fotos;

	public Long getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(Long idActividad) {
		this.idActividad = idActividad;
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

	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}

	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}

	public List<RepuestoDTO> getRepuestos() {
		return repuestos;
	}

	public void setRepuestos(List<RepuestoDTO> repuestos) {
		this.repuestos = repuestos;
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

	public List<FotoDTO> getFotos() {
		return fotos;
	}

	public void setFotos(List<FotoDTO> fotos) {
		this.fotos = fotos;
	}

}

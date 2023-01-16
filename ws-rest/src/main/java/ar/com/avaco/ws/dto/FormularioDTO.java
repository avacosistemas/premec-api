package ar.com.avaco.ws.dto;

public class FormularioDTO {
	
	Long idActividad;
	//(puede ser C para combustion y E para electrico)
	String tipoMaquina;
	ItemCheckDTO itemchack;
	String observacionesGenerales;
	RepuestoDTO repusto;
	String fechaHoraInicio;
	String fechaHoraFin;
	String valoracion;
	String comentarios;
	String nombreSupervisor;
	Long dniSupervisor;
	FotoDTO foto;
	
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
	public ItemCheckDTO getItemchack() {
		return itemchack;
	}
	public void setItemchack(ItemCheckDTO itemchack) {
		this.itemchack = itemchack;
	}
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
	public RepuestoDTO getRepusto() {
		return repusto;
	}
	public void setRepusto(RepuestoDTO repusto) {
		this.repusto = repusto;
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
	public FotoDTO getFoto() {
		return foto;
	}
	public void setFoto(FotoDTO foto) {
		this.foto = foto;
	}
}

package ar.com.avaco.ws.dto;

import java.util.ArrayList;
import java.util.List;

public class ActividadTarjetaDTO {

	private Long idActividad;
	private String prioridad;
	private String asignadoPor;
	private String empleado;
	private String numero;
	private String llamadaID;
	private String fecha;
	private String hora;
	private String codigoArticulo;
	private String detalle;
	private String nroSerie;
	private String cliente;
	private String direccion;
	private String nroFabricante;
	private int horasMaquina;
	private String tareasARealizar;
	private Boolean conCargo;
	private Boolean actividadTaller;
	private String tipoActividad;
	private String tipoMaquina;
	private List<GrupoDTO> grupos = new ArrayList<>();

	public void agregarGrupo(GrupoDTO g) {
		grupos.add(g);
	}

	public Long getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(Long idActividad) {
		this.idActividad = idActividad;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public String getAsignadoPor() {
		return asignadoPor;
	}

	public void setAsignadoPor(String asignadoPor) {
		this.asignadoPor = asignadoPor;
	}

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getLlamadaID() {
		return llamadaID;
	}

	public void setLlamadaID(String llamadaID) {
		this.llamadaID = llamadaID;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getNroSerie() {
		return nroSerie;
	}

	public void setNroSerie(String nroSerie) {
		this.nroSerie = nroSerie;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNroFabricante() {
		return nroFabricante;
	}

	public void setNroFabricante(String nroFabricante) {
		this.nroFabricante = nroFabricante;
	}

	public int getHorasMaquina() {
		return horasMaquina;
	}

	public void setHorasMaquina(int horasMaquina) {
		this.horasMaquina = horasMaquina;
	}

	public String getTareasARealizar() {
		return tareasARealizar;
	}

	public void setTareasARealizar(String tareasARealizar) {
		this.tareasARealizar = tareasARealizar;
	}

	public Boolean getConCargo() {
		return conCargo;
	}

	public void setConCargo(Boolean conCargo) {
		this.conCargo = conCargo;
	}

	public Boolean getActividadTaller() {
		return actividadTaller;
	}

	public void setActividadTaller(Boolean actividadTaller) {
		this.actividadTaller = actividadTaller;
	}

	public String getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(String tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public List<GrupoDTO> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<GrupoDTO> grupos) {
		this.grupos = grupos;
	}

	public String getTipoMaquina() {
		return tipoMaquina;
	}

	public void setTipoMaquina(String tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}

}
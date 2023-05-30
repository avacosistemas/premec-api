package ar.com.avaco.ws.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActividadReporteDTO {

	private Long idActividad;
	private String prioridad;
	private String numero;
	private String asignadoPor;
	private String llamadaID;
	private String empleado;
	private String fecha;
	private String hora;

	private String codigoArticulo;
	private String fechaInicio;
	private String nroSerie;
	private String cliente;
	private String nroFabricante;
	private String direccion;
	private int horasMaquina;
	private String conCargo;
	private String detalle;
	private String tareasARealizar;

	private Map<String, List<ItemCheckDTO>> checks = new HashMap<String, List<ItemCheckDTO>>();

	private String observacionesGenerales;

	private List<RepuestoDTO> repuestos = new ArrayList<RepuestoDTO>();

	private String fechaInicioOperario;
	private String horaInicioOperario;

	private String fechaFinoOperario;
	private String horaFinOperario;

	private String valoracionResultado;
	private String valoracionNombreSuperior;
	private String valoracionDNISuperior;
	private String valoracionComentarios;

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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getAsignadoPor() {
		return asignadoPor;
	}

	public void setAsignadoPor(String asignadoPor) {
		this.asignadoPor = asignadoPor;
	}

	public String getLlamadaID() {
		return llamadaID;
	}

	public void setLlamadaID(String llamadaID) {
		this.llamadaID = llamadaID;
	}

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
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

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
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

	public String getNroFabricante() {
		return nroFabricante;
	}

	public void setNroFabricante(String nroFabricante) {
		this.nroFabricante = nroFabricante;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getHorasMaquina() {
		return horasMaquina;
	}

	public void setHorasMaquina(int horasMaquina) {
		this.horasMaquina = horasMaquina;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getTareasARealizar() {
		return tareasARealizar;
	}

	public void setTareasARealizar(String tareasARealizar) {
		this.tareasARealizar = tareasARealizar;
	}

	public Map<String, List<ItemCheckDTO>> getChecks() {
		return checks;
	}

	public void setChecks(Map<String, List<ItemCheckDTO>> checks) {
		this.checks = checks;
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

	public String getFechaInicioOperario() {
		return fechaInicioOperario;
	}

	public void setFechaInicioOperario(String fechaInicioOperario) {
		this.fechaInicioOperario = fechaInicioOperario;
	}

	public String getHoraInicioOperario() {
		return horaInicioOperario;
	}

	public void setHoraInicioOperario(String horaInicioOperario) {
		this.horaInicioOperario = horaInicioOperario;
	}

	public String getFechaFinoOperario() {
		return fechaFinoOperario;
	}

	public void setFechaFinoOperario(String fechaFinoOperario) {
		this.fechaFinoOperario = fechaFinoOperario;
	}

	public String getHoraFinOperario() {
		return horaFinOperario;
	}

	public void setHoraFinOperario(String horaFinOperario) {
		this.horaFinOperario = horaFinOperario;
	}

	public String getValoracionResultado() {
		return valoracionResultado;
	}

	public void setValoracionResultado(String valoracionResultado) {
		this.valoracionResultado = valoracionResultado;
	}

	public String getValoracionNombreSuperior() {
		return valoracionNombreSuperior;
	}

	public void setValoracionNombreSuperior(String valoracionNombreSuperior) {
		this.valoracionNombreSuperior = valoracionNombreSuperior;
	}

	public String getValoracionDNISuperior() {
		return valoracionDNISuperior;
	}

	public void setValoracionDNISuperior(String valoracionDNISuperior) {
		this.valoracionDNISuperior = valoracionDNISuperior;
	}

	public String getValoracionComentarios() {
		return valoracionComentarios;
	}

	public void setValoracionComentarios(String valoracionComentarios) {
		this.valoracionComentarios = valoracionComentarios;
	}

	public String getConCargo() {
		return conCargo;
	}

	public void setConCargo(String conCargo) {
		this.conCargo = conCargo;
	}

}
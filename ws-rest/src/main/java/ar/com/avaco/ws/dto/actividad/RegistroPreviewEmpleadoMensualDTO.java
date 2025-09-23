package ar.com.avaco.ws.dto.actividad;

import java.util.HashMap;
import java.util.Map;

public class RegistroPreviewEmpleadoMensualDTO {

	private Integer legajo;

	private Long usuarioSap;

	private String nombre;

	private String facturables;

	private String facturablesHora;

	private String ociosas;

	private String ociosasHora;

	private String fichado;

	private String fichadoHora;

	private String efectividad;

	private Integer cantMB;

	private Integer cantB;

	private Integer cantR;

	private Integer cantM;

	private Integer cantSV;

	private Integer cantidadActividades;

	private String porcentajeValoracion;

	private Integer objetivoActividades;

	private String cumplimientoObjetivo;

	private String salario;

	private String unidadSalario;

	// Campos ingresados a mano

	private String viaticos;

	private String adelanto;

	private String prestamo;

	private String premioAsistencia;

	// FIXME CARGAR A MANO
	private String novedades;

	private String gratificacionesAumentos;

	public Map<String, Object> generarMapUpdate() {

		Map<String, Object> map = new HashMap<>();

		map.put("U_cumplimientoobjetivo", this.cumplimientoObjetivo);
		map.put("U_viaticos", this.viaticos);
		map.put("U_adelanto", this.adelanto);
		map.put("U_prestamo", this.prestamo);
		map.put("U_premio", Boolean.valueOf(this.premioAsistencia) ? "SI" : "NO");
		map.put("U_porcentajevaloracion", this.porcentajeValoracion);
		map.put("U_porcentajeefectividad", this.efectividad);
		map.put("U_cantidadactividades", this.cantidadActividades);
		map.put("U_salario", this.salario);
		map.put("U_aumento", this.gratificacionesAumentos);

		return map;
	}

	public Integer getLegajo() {
		return legajo;
	}

	public void setLegajo(Integer legajo) {
		this.legajo = legajo;
	}

	public Long getUsuarioSap() {
		return usuarioSap;
	}

	public void setUsuarioSap(Long usuarioSap) {
		this.usuarioSap = usuarioSap;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFacturables() {
		return facturables;
	}

	public void setFacturables(String facturables) {
		this.facturables = facturables;
	}

	public String getFacturablesHora() {
		return facturablesHora;
	}

	public void setFacturablesHora(String facturablesHora) {
		this.facturablesHora = facturablesHora;
	}

	public String getOciosas() {
		return ociosas;
	}

	public void setOciosas(String ociosas) {
		this.ociosas = ociosas;
	}

	public String getOciosasHora() {
		return ociosasHora;
	}

	public void setOciosasHora(String ociosasHora) {
		this.ociosasHora = ociosasHora;
	}

	public String getFichado() {
		return fichado;
	}

	public void setFichado(String fichado) {
		this.fichado = fichado;
	}

	public String getFichadoHora() {
		return fichadoHora;
	}

	public void setFichadoHora(String fichadoHora) {
		this.fichadoHora = fichadoHora;
	}

	public String getEfectividad() {
		return efectividad;
	}

	public void setEfectividad(String efectividad) {
		this.efectividad = efectividad;
	}

	public Integer getCantMB() {
		return cantMB;
	}

	public void setCantMB(Integer cantMB) {
		this.cantMB = cantMB;
	}

	public Integer getCantB() {
		return cantB;
	}

	public void setCantB(Integer cantB) {
		this.cantB = cantB;
	}

	public Integer getCantR() {
		return cantR;
	}

	public void setCantR(Integer cantR) {
		this.cantR = cantR;
	}

	public Integer getCantM() {
		return cantM;
	}

	public void setCantM(Integer cantM) {
		this.cantM = cantM;
	}

	public Integer getCantSV() {
		return cantSV;
	}

	public void setCantSV(Integer cantSV) {
		this.cantSV = cantSV;
	}

	public Integer getCantidadActividades() {
		return cantidadActividades;
	}

	public void setCantidadActividades(Integer cantidadActividades) {
		this.cantidadActividades = cantidadActividades;
	}

	public Integer getObjetivoActividades() {
		return objetivoActividades;
	}

	public void setObjetivoActividades(Integer objetivoActividades) {
		this.objetivoActividades = objetivoActividades;
	}

	public String getSalario() {
		return salario;
	}

	public void setSalario(String salario) {
		this.salario = salario;
	}

	public String getCumplimientoObjetivo() {
		return cumplimientoObjetivo;
	}

	public void setCumplimientoObjetivo(String cumplimientoObjetivo) {
		this.cumplimientoObjetivo = cumplimientoObjetivo;
	}

	public String getUnidadSalario() {
		return unidadSalario;
	}

	public void setUnidadSalario(String unidadSalario) {
		this.unidadSalario = unidadSalario;
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

	public String getPremioAsistencia() {
		return premioAsistencia;
	}

	public void setPremioAsistencia(String premioAsistencia) {
		this.premioAsistencia = premioAsistencia;
	}

	public String getPorcentajeValoracion() {
		return porcentajeValoracion;
	}

	public void setPorcentajeValoracion(String porcentajeValoracion) {
		this.porcentajeValoracion = porcentajeValoracion;
	}

	public String getNovedades() {
		return novedades;
	}

	public void setNovedades(String novedades) {
		this.novedades = novedades;
	}

	public String getGratificacionesAumentos() {
		return gratificacionesAumentos;
	}

	public void setGratificacionesAumentos(String gratificacionesAumentos) {
		this.gratificacionesAumentos = gratificacionesAumentos;
	}

}

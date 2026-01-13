package ar.com.avaco.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REGISTRO_HORAS_MAQUINA")
@SequenceGenerator(name = "REGISTRO_HORAS_MAQUINA_SEQ", sequenceName = "REGISTRO_HORAS_MAQUINA_SEQ", allocationSize = 1)
public class RegistroHorasMaquina {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGISTRO_HORAS_MAQUINA_SEQ")
	@Column(name = "ID_REG_HS_MAQ")
	private Long id;

	@Column(name = "SERVICE_CALL")
	private Long serviceCall;

	@Column(name = "FECHA_ANTERIOR")
	private Date fechaAnterior;

	@Column(name = "HS_MAQ_ANTERIOR")
	private String hsMaqAnterior;

	@Column(name = "FECHA_ACTUAL")
	private String fechaActual;

	@Column(name = "HS_MAQ_ACTUAL")
	private String hsMaqActual;

	@Column(name = "ACTIVITY")
	private Long activity;

	public Long getActivity() {
		return activity;
	}

	public void setActivity(Long activity) {
		this.activity = activity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getServiceCall() {
		return serviceCall;
	}

	public void setServiceCall(Long serviceCall) {
		this.serviceCall = serviceCall;
	}

	public Date getFechaAnterior() {
		return fechaAnterior;
	}

	public void setFechaAnterior(Date fechaAnterior) {
		this.fechaAnterior = fechaAnterior;
	}

	public String getHsMaqAnterior() {
		return hsMaqAnterior;
	}

	public void setHsMaqAnterior(String hsMaqAnterior) {
		this.hsMaqAnterior = hsMaqAnterior;
	}

	public String getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}

	public String getHsMaqActual() {
		return hsMaqActual;
	}

	public void setHsMaqActual(String hsMaqActual) {
		this.hsMaqActual = hsMaqActual;
	}

}

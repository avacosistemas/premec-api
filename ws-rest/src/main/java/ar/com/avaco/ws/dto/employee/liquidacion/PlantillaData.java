package ar.com.avaco.ws.dto.employee.liquidacion;

import java.util.ArrayList;
import java.util.List;

public class PlantillaData {

	private List<Jornal> jornales = new ArrayList<>();
	private List<FueraConvenio> fueraConvenio = new ArrayList<>();
	private List<Mensual> mensual = new ArrayList<>();
	private String periodo;

	public List<Jornal> getJornales() {
		return jornales;
	}

	public void setJornales(List<Jornal> jornales) {
		this.jornales = jornales;
	}

	public List<FueraConvenio> getFueraConvenio() {
		return fueraConvenio;
	}

	public void setFueraConvenio(List<FueraConvenio> fueraConvenio) {
		this.fueraConvenio = fueraConvenio;
	}

	public List<Mensual> getMensual() {
		return mensual;
	}

	public void setMensual(List<Mensual> mensual) {
		this.mensual = mensual;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

}

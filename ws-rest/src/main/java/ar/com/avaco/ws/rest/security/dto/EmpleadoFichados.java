package ar.com.avaco.ws.rest.security.dto;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoFichados {

	private EmpleadoSap empleado;

	private List<RegistroFichadoDTO> fichados = new ArrayList<RegistroFichadoDTO>();

	public EmpleadoFichados() {
	}

	public EmpleadoFichados(EmpleadoSap x, List<RegistroFichadoDTO> y) {
		this.empleado = x;
		this.fichados.addAll(y);
	}

	public EmpleadoSap getEmpleado() {
		return empleado;
	}

	public void setEmpleado(EmpleadoSap empleado) {
		this.empleado = empleado;
	}

	public List<RegistroFichadoDTO> getFichados() {
		return fichados;
	}

	public void setFichados(List<RegistroFichadoDTO> fichados) {
		this.fichados = fichados;
	}

}

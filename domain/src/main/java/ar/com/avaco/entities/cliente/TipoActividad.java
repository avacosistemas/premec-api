package ar.com.avaco.entities.cliente;

public enum TipoActividad {

	REPARACION("R"),
	CHECKLIST("C"),
	PIEZAS_A_REPARAR("P"),
	MANTENIMIENTO_MAQUINARIA("M");

	private String codigo;

	TipoActividad(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

}

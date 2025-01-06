package ar.com.avaco.entities.cliente;

public enum TipoActividad {

	R("Reparación"), C("Checklist"), P("Piezas a Reparar"), M("Mantenimiento Maquinaria"),
	EE("Entrega Maquina Electrica"), EC("Entrega Maquina Combustion"), EP("Entrega Plataforma");

	private String nombre;

	TipoActividad(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

//	REPARACION("R"),
//	CHECKLIST("C"),
//	PIEZAS_A_REPARAR("P"),
//	MANTENIMIENTO_MAQUINARIA("M"),
//	ENTREGA_MAQUINA_ELECTRICA("EE"),
//	ENTREGA_MAQUINA_COMBUSTION("EC"),
//	ENTREGA_PLATAFORMA("EP")

}

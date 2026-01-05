package ar.com.avaco.entities.cliente;

/**
 * Listado de provincias de la Republica Argentina.
 * @author beto
 *
 */
public enum Provincia {
	
	BUENOS_AIRES("Buenos Aires"),
	CABA("CABA"),
	CATAMARCA("Catamarca"),
	CHACO("Chaco"),
	CHUBUT("Chubut"),
	CORDOBA("Córdoba"),
	CORRIENTES("Corrientes"),
	ENTRE_RIOS("Entre Ríos"),
	FORMOSA("Formosa"),
	JUJUY("Jujuy"),
	LA_PAMPA("La Pampa"),
	LA_RIOJA("La Rioja"),
	MENDOZA("Mendoza"),
	MISIONES("Misiones"),
	NEUQUEN("Neuquen"),
	RIO_NEGRO("Río Negro"),
	SALTA("Salta"),
	SAN_JUAN("San Juan"),
	SANTA_CRUZ("Santa Cruz"),
	SANTA_FE("Santa Fe"),
	SANTIAGO_DEL_ESTERO("Santiago del Estero"),
	TIERRA_DEL_FUEGO("Tierra del Fuego"),
	TUCUMAN("Tucumán");
	
	private String nombre;
	
	Provincia(String elnombre) {
		this.nombre = elnombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
}

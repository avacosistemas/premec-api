package ar.com.avaco.entities.cliente;

/**
 * Enum genero de un cliente.
 * Se emplea para determinar si es una persona o una empresa.
 * Masculino o Femenino es Persona.
 * Empresa es Empresa.
 * @author beto
 *
 */
public enum Genero {

    M("MASCULINO"),
    F("FEMENINO"),
    E("EMPRESA");

    private String descripcion;

    Genero(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
	
}

package ar.com.avaco.factory;

/**
 * Exception que se lanza cuando se quiere obtener el parent object id de una
 * actividad y no lo tiene asignado (o la actividad directamente no existe).
 * 
 * @author Alberto
 *
 */
public class ParentObjectIdNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParentObjectIdNotFoundException(String msj, Exception e) {
		super(msj, e);
	}

	public ParentObjectIdNotFoundException(String msj) {
		super(msj);
	}

}

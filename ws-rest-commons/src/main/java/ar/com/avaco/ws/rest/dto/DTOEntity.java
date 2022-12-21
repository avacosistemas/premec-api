package ar.com.avaco.ws.rest.dto;

import java.io.Serializable;

public abstract class DTOEntity<ID extends Serializable> {

	public abstract void setId(ID id);

	public abstract ID getId();

}

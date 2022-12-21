package ar.com.avaco.arc.core.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.hibernate.Hibernate;

/**
 * Default abstract class that represents a unique and independent domain
 * object.<br>
 * Provides a comparison capability using the unique identifier of the object.
 * 
 * @author gaston.ortiz@sofrecom.com.ar
 */
@MappedSuperclass
public abstract class Entity<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 7239163146107336896L;

	/**
	 * Sets the unique identifier of the object. This method should only be used
	 * by frameworks such as Hibernate as an object shouldn't change it's ID.
	 * 
	 * @param id
	 *            The ID of the object.
	 */
	public abstract void setId(ID id);

	/**
	 * Gets the unique identifier of the object.
	 * 
	 * @return <b>long</b> The ID of the object.
	 */
	public abstract ID getId();

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + getId();
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (Hibernate.getClass(this) != Hibernate.getClass(obj)) 
			return false;
		Entity<ID> other = (Entity<ID>) obj;
		if (getId() == null) {
//			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}
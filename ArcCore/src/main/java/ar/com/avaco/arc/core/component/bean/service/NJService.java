package ar.com.avaco.arc.core.component.bean.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.arc.core.domain.filter.AbstractFilter;

/**
 * Generic interface that provides basic CRUD methods.
 * 
 * @author GOrtiz
 *
 * @param <T>
 *            The type {@link Entity} that is handled on this service.
 */
public interface NJService<ID extends Serializable, T extends Entity<ID>> {

	/**
	 * Saves an {@link Entity} into the {@link NJRepository}.
	 * 
	 * @param entity
	 *            The {@link Entity} to save.
	 * @return {@link Serializable} The ID of the entity.
	 */
	T save(T entity);

	/**
	 * Updates an {@link Entity} into the {@link NJRepository}.
	 * 
	 * @param entity
	 *            The {@link Entity} to update.
	 */
	T update(T entity);
	
	/**
	 * Saves a {@link Collection} of {@link Entity Entities} into the
	 * {@link NJRepository}.
	 * 
	 * @param entities
	 *            The {@link Entity Entities} to save.
	 */
	List<T> save(Collection<T> entities);

	/**
	 * Gets an {@link Entity} from the {@link NJRepository} by it's ID.
	 * 
	 * @param id
	 *            The ID of the {@link Entity}.
	 * @return {@link Entity} The retrieved entity.
	 */
	T get(ID id);

	/**
	 * Lists all the {@link Entity Entities} on the {@link NJRepository}.
	 * 
	 * @return {@link List} The entities.
	 */
	List<T> list();
	
	/**
	 * Removes an {@link Entity} using it's ID.
	 * 
	 * @param id
	 *            The ID of the entity to remove.
	 * @return {@link Entity} The removed entity.
	 */
	void remove(ID id);

	int listCount(AbstractFilter abstractFilter);

	List<T> listFilter(AbstractFilter abstractFilter);
	
	List<T> listPattern(String field, String pattern);

	
}
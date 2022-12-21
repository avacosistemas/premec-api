package ar.com.avaco.arc.core.component.bean.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.arc.core.domain.filter.AbstractFilter;

/**
 * Default implementation of a {@link NJService} that redirects operations to
 * it's {@link NJRepository}.
 * 
 * @author gaston.ortiz@sofrecom.com.ar
 */
public abstract class NJBaseService<ID extends Serializable, T extends Entity<ID>, R extends NJRepository<ID, T>>
		implements NJService<ID, T> {

	protected R repository;

	/**
	 * @see NJService#save(Entity)
	 */
	public T save(T entity) {
		return getRepository().save(entity);
	}

	/**
	 * @see NJService#update(Entity)
	 */
	@Override
	public T update(T entity) {
		return getRepository().save(entity);
	}

	/**
	 * @see NJService#save(Collection)
	 */
	public List<T> save(Collection<T> entities) {
		List<T> tList = new ArrayList<T>();
		for (T t : entities) {
			tList.add(getRepository().save(t));
		}
		return tList;
	}

	/**
	 * @see NJService#get(Serializable)
	 */
	public T get(ID id) {
		return getRepository().findOne(id);
	}

	/**
	 * @see NJService#list()
	 */
	public List<T> list() {
		return getRepository().findAll();
	}

	/**
	 * @see NJService#remove(Serializable)
	 */
	public void remove(ID id) {
		getRepository().delete(id);
	}

	@Override
	public int listCount(AbstractFilter abstractFilter) {
		return getRepository().listCount(abstractFilter);
	}

	@Override
	public List<T> listFilter(AbstractFilter abstractFilter) {
		return getRepository().listFilter(abstractFilter);
	}

	@Override
	public List<T> listPattern(String field, String pattern) {
		return getRepository().listPattern(field, pattern);
	}

	/**
	 * Gets the repository that handled the CRUD operations on the service.
	 * 
	 * @return {@link NJRepository} The repository.
	 */
	protected final R getRepository() {
		return this.repository;
	}
	
}
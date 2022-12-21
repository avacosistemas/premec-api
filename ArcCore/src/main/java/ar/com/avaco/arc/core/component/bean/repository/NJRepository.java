package ar.com.avaco.arc.core.component.bean.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.arc.core.domain.filter.AbstractFilter;

@NoRepositoryBean
public interface NJRepository<ID extends Serializable, E extends Entity<ID>> extends JpaRepository<E, ID> {

	int listCount(AbstractFilter abstractFilter);

	List<E> listFilter(AbstractFilter abstractFilter);
	
	List<E> listPattern(String field, String pattern);
	
}
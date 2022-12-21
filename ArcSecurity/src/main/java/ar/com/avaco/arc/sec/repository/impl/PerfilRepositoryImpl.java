package ar.com.avaco.arc.sec.repository.impl;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.sec.domain.Perfil;
import ar.com.avaco.arc.sec.repository.PerfilRepository;
import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;

/**
 * @author avaco
 */
@Repository("perfilRepository")
public class PerfilRepositoryImpl extends NJBaseRepository<Long, Perfil> implements PerfilRepository {

	public PerfilRepositoryImpl(EntityManager entityManager) {
		super(Perfil.class, entityManager);
	}

	@Override
	public boolean existePerfil(String nombre, Long rolId) {
		Criteria c = this.getCurrentSession().createCriteria(this.getHandledClass());
		c.add(Restrictions.eq("nombre", nombre));
		c.add(Restrictions.eq("rol.id", rolId));
		
		return c.uniqueResult() != null;
	}
}
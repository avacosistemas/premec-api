package ar.com.avaco.arc.sec.repository.impl;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.sec.repository.PermisoRepository;
import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;

@Repository("permisoRepository")
public class PermisoRepositoryImpl extends NJBaseRepository<Long, Permiso>
		implements PermisoRepository {

	public PermisoRepositoryImpl(EntityManager em) {
		super(Permiso.class, em);
	}

	@Override
	public Permiso getPermisoPorCodigo(String codigo) {
		return (Permiso) getCurrentSession()
				.createCriteria(this.getHandledClass())
				.add(Restrictions.eq("codigo", codigo)).uniqueResult();
	}
}
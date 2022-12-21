package ar.com.avaco.arc.sec.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.arc.sec.domain.Acceso;
import ar.com.avaco.arc.sec.domain.Rol;
import ar.com.avaco.arc.sec.repository.RolRepository;

/**
 * @author avaco
 */
@Repository("rolRepository")
public class RolRepositoryImpl extends NJBaseRepository<Long, Rol> implements
		RolRepository {

	public RolRepositoryImpl(EntityManager em) {
		super(Rol.class, em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> rolPorNombre(String nombre) {
		Criteria c = this.getCurrentSession().createCriteria(getHandledClass());
		c.add(Restrictions.like("nombre", nombre, MatchMode.START));
		return c.list();
	}

	@Override
	public Rol getRolPorCodigo(String codigo) {
		Criteria c = getCurrentSession().createCriteria(getHandledClass());
		c.add(Restrictions.eq("codigo", codigo));
		return (Rol) c.uniqueResult();
	}

	@Override
	public Rol getRolPorNombre(String nombre) {
		Criteria c = this.getCurrentSession().createCriteria(getHandledClass());
		c.add(Restrictions.eq("nombre", nombre));
		return (Rol) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> getRolesPorCodigos(List<String> codigos) {
		Criteria c = getCurrentSession().createCriteria(getHandledClass());
		Disjunction or = Restrictions.or();
		for (String cod: codigos) {
			or.add(Restrictions.eq("codigo", cod));
		}
		c.add(or);
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> rolesNoTransportista() {
		Criteria c = getCurrentSession().createCriteria(getHandledClass(), "rol");
		c.add(Restrictions.ne("rol.nombre", "transportista").ignoreCase());
		return c.list();
	}

	@Override
	public boolean isRolAsignadoAcceso(Rol rol) {
		Criteria c = getCurrentSession().createCriteria(Acceso.class);
		c.createAlias("perfil", "perfil");
		c.createAlias("perfil.rol", "rol");
		c.add(Restrictions.eqOrIsNull("rol.id", rol.getId()));
		c.setProjection(Projections.count("rol.id"));
		return ((Long)c.uniqueResult()) > 0;
	}
}
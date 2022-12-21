package ar.com.avaco.arc.sec.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.repository.UsuarioRepository;


@Repository("usuarioRepository")
public class UsuarioRepositoryImpl extends NJBaseRepository<Long, Usuario>
		implements UsuarioRepository {

	protected UsuarioRepositoryImpl(EntityManager em) {
		super(Usuario.class, em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> getExternalUsersLike(String userLess,String userlike) {
		Criteria c = this.getCurrentSession().createCriteria(this.getHandledClass());
		c.add(Restrictions.eq("interno", false));
		c.add(Restrictions.ne("username", userLess));
		c.add(Restrictions.like("username", userlike, MatchMode.START));
		return c.list();
	}

	@Override
	protected void initialize(Usuario entity) {
		Hibernate.initialize(entity.getAccesos());
		entity.getAccesos();
	}

	@Override
	public List<Usuario> listUsuariosParaImpersonar(Usuario usuario) {
		usuario = (Usuario) getCurrentSession().load(this.getHandledClass(), usuario.getId());
		return new ArrayList<Usuario>(usuario.getImpersonables());
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getAllOnlyIdUsernameAndName() {
		Criteria c = this.getCurrentSession().createCriteria(this.getHandledClass());
		
		c.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("username"), "username")
				.add(Projections.property("nombre"), "nombre")
				.add(Projections.property("apellido"), "apellido"));
		
		c.addOrder(Order.asc("nombre")).addOrder(Order.asc("apellido"));
		
		c.setResultTransformer(Transformers.aliasToBean(this.getHandledClass()));
		
		return c.list();
	}

	@Override
	public Usuario findByUsername(String username) {
		Criteria c = this.getCurrentSession().createCriteria(this.getHandledClass());
		c.add(Restrictions.eq("username", username));
		return (Usuario) c.uniqueResult();
	}

	@Override
	public Usuario findByEmail(String email) {
		Criteria c = this.getCurrentSession().createCriteria(this.getHandledClass());
		c.add(Restrictions.eq("email", email));
		return (Usuario) c.uniqueResult();
	}
	
	@Override
	public boolean isUserExistWithEmail(String email) {
		return this.findByEmail(email) != null;
	}


}
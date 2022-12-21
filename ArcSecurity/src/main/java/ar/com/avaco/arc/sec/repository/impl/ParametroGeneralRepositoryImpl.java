package ar.com.avaco.arc.sec.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.sec.domain.ParametroGeneral;
import ar.com.avaco.arc.sec.repository.ParametroGeneralRepository;

@Repository
public class ParametroGeneralRepositoryImpl implements ParametroGeneralRepository {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public ParametroGeneral getParametroGeneral(String param) {
		Criteria createCriteria = sessionFactory.getCurrentSession().createCriteria(ParametroGeneral.class);
		createCriteria.add(Restrictions.eq("codigo", param));
		return (ParametroGeneral) createCriteria.uniqueResult();
	}

	public void save(String param) {
		ParametroGeneral pg = new ParametroGeneral();
		pg.setCodigo(param);
		sessionFactory.getCurrentSession().save(pg);
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
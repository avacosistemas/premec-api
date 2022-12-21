package ar.com.avaco.arc.core.component.bean.repository;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.arc.core.domain.filter.FilterData;

public class NJBaseRepository<ID extends Serializable, E extends ar.com.avaco.arc.core.domain.Entity<ID>>
		extends SimpleJpaRepository<E, ID> implements NJRepository<ID, E> {

	@Autowired
	private SessionFactory sessionFactory;
	private Class<E> javaType;

	private EntityManager entityManager;
	
	public NJBaseRepository(Class<E> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.javaType = domainClass;
		this.entityManager = entityManager;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<E> listFilter(AbstractFilter abstractFilter) {
		Criteria criteria = getCurrentSession().createCriteria(getHandledClass());
		applyFilters(criteria, abstractFilter);
		applyPagination(criteria, abstractFilter);
		applyOrdering(criteria, abstractFilter);
		if (abstractFilter.getDistinctRootEntity() != null && abstractFilter.getDistinctRootEntity().booleanValue()) {
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		return criteria.list();
	}

	protected Class<?> getHandledClass() {
		return javaType;
	}

	@Override
	public int listCount(AbstractFilter abstractFilter) {
		Criteria criteria = getCurrentSession().createCriteria(getHandledClass());
		applyFilters(criteria, abstractFilter);
		Long uniqueResult = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return uniqueResult.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> listPattern(String field, String pattern) {
		Criteria criteria = getCurrentSession().createCriteria(getHandledClass());
		criteria.add(Restrictions.like(field, pattern, MatchMode.ANYWHERE).ignoreCase());
		return criteria.list();
	}
	
	protected void applyPagination(Criteria criteria, AbstractFilter abstractFilter) {
		if (abstractFilter.getFirst() != null && abstractFilter.getRows() != null) {
			criteria.setFirstResult(abstractFilter.getFirst());
			criteria.setMaxResults(abstractFilter.getRows());
		}
	}
	
	protected void applyOrdering(Criteria criteria, AbstractFilter abstractFilter) {
		if (abstractFilter.getAsc() != null && !StringUtils.isEmpty(abstractFilter.getSidx())) {
			criteria = containsAlias(criteria, abstractFilter.getIdx());

			String sortField = abstractFilter.getIdx();
			if (!isPropertyEmbeddable(sortField)) {
				sortField = getAliasedProperty(sortField);
			}

			if (abstractFilter.isAsc()) {
				criteria.addOrder(Order.asc(sortField).nulls(NullPrecedence.NONE));
			} else {
				criteria.addOrder(Order.desc(sortField).nulls(NullPrecedence.NONE));
			}
		}
	}

	private String getAliasedProperty(String property) {
		String[] prop = property.split("\\.");

		StringBuilder field = new StringBuilder();

		for (int i = 0 ; i < prop.length; i++) {
			if (i != 0 && i == prop.length -1) {
				field.append(".");
			}
			field.append(prop[i]);
		}
		return field.toString();
	}
	
	protected void applyFilters(Criteria criteria, AbstractFilter abstractFilter) {

        for (List<FilterData> fdl : abstractFilter.getOrFilterDatas()) {
        	Disjunction or = Restrictions.disjunction();
			for (FilterData ofd : fdl) {
				criteria = containsAlias(criteria, ofd.getProperty());
				or.add(createCriterion(ofd));
			}
			criteria.add(or);
		}

		List<FilterData> filters = abstractFilter.getFilterDatas();
		
		for (FilterData data : filters) {
			criteria = containsAlias(criteria,data.getProperty());
			Criterion criterion = createCriterion(data);
			criteria.add(criterion);
		}
		
	}

	private Criterion createCriterion(FilterData data) {
		Criterion criterion = null; 
		
		String property = data.getProperty();
		if (!isPropertyEmbeddable(property)) {
			property = getAliasedProperty(data.getProperty());
		}
		
		switch (data.getFilterDataType()) {
			case LESS_THAN:
				criterion = Restrictions.lt(property, data.getObject());
				break;
			case MORE_THAN:
				criterion = Restrictions.gt(property, data.getObject());
				break;
			case EQUALS:
				criterion = Restrictions.eq(property, data.getObject());
				break;
			case LIKE:
				criterion = Restrictions.ilike(property, data.getObject().toString(), MatchMode.ANYWHERE);
				break;
			case EQUALS_LESS_THAN:
				criterion = Restrictions.le(property, data.getObject());
				break;
			case EQUALS_MORE_THAN:
				criterion = Restrictions.ge(property, data.getObject());
				break;
		default:
			break;
		}
		return criterion;
	}

	private boolean isPropertyEmbeddable(String property) {
		String theProperty = property;
		if (property.contains(".")) {
			theProperty = property.substring(0, property.indexOf("."));
		}
		
		Class<?> clazz = getHandledClass();
		
		Annotation[] lt = null; 
		while (clazz != null) {
			try {
				lt = clazz.getDeclaredField(theProperty).getType().getAnnotations();
				clazz = null;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
				if (clazz.equals(Object.class)) {
					throw new RuntimeException("Property " + property + " not present in class " + getHandledClass().getName());
				}
			}
		}
		if (lt != null) {
			for (Annotation ann : lt) {
				if (ann.annotationType().getCanonicalName().equals(Embeddable.class.getCanonicalName())) {
					return true;
				}
			}
		}
		return false;
	}

	private Criteria containsAlias(Criteria criteria, String property) {
		
		if (!isPropertyEmbeddable(property) && property.contains(".")) {
			String[] prop = property.split("\\.");

			CriteriaImpl ci = (CriteriaImpl) criteria;

			for (int i = 0; i < prop.length - 1; i++) {

				StringBuilder associationPath = new StringBuilder();
				StringBuilder alias = new StringBuilder();

				for (int j = 0; j <= i; j++) {

					associationPath.append(prop[j]);
					if (j != i) {
						associationPath.append(".");
					}
					alias.append(prop[j]);
				}

				Iterator<Subcriteria> it = ci.iterateSubcriteria();
				boolean found = false;
				while (it.hasNext() && !found) {
					Subcriteria next = it.next();
					found = next.getAlias().equals(alias.toString());
				}

				if (!found) {
					criteria.createAlias(associationPath.toString(), alias.toString(), JoinType.LEFT_OUTER_JOIN);
				}
			}
		}
		return criteria;
	}

	protected void initialize(E entity) {
		// Implementar si es necesario
	}
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * Gets the {@link SessionFactory} that will handle the Hibernate
	 * {@link org.hibernate.Session}s.
	 * 
	 * @return {@link SessionFactory} The factory.
	 */
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Sets the {@link SessionFactory} that will handle the Hibernate
	 * {@link org.hibernate.Session}s.
	 * 
	 * @param sessionFactory
	 *            The factory.
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
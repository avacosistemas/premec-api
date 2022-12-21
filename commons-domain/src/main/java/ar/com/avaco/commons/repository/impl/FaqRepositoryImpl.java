/**
 * 
 */
package ar.com.avaco.commons.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.commons.domain.Faq;
import ar.com.avaco.commons.repository.FaqRepositoryCustom;

/**
 * 
 *
 */
@Repository("faqRepository")
public class FaqRepositoryImpl extends NJBaseRepository<Long, Faq> implements FaqRepositoryCustom {

//	@SuppressWarnings("unchecked")
//	@Override
//	public <S extends Faq> S save(S entity) {
//		Criteria criteria = getCurrentSession().createCriteria(getDomainClass());
//		criteria.add(Restrictions.ge("orderFaq", entity.getOrderFaq()));
//		criteria.addOrder(Order.asc("orderFaq"));
//		List<Faq> result = criteria.list();
//		if (result != null && !result.isEmpty() && result.get(0).getOrderFaq().equals(entity.getOrderFaq())) {
//			result.forEach(el -> {
//				el.setOrderFaq(el.getOrderFaq() + 1);
//			});
//			super.save(result);
//		}
//		return super.save(entity);
//	}

	public FaqRepositoryImpl(EntityManager entityManager) {
		super(Faq.class, entityManager);
	}
}

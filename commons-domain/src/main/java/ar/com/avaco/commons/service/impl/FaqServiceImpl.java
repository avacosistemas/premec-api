/**
 * 
 */
package ar.com.avaco.commons.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.arc.core.component.bean.service.NJBaseService;
import ar.com.avaco.commons.domain.Faq;
import ar.com.avaco.commons.repository.FaqRepository;
import ar.com.avaco.commons.service.FaqService;

/**
 * @author avaco
 */

@Transactional
@Service("faqService")
public class FaqServiceImpl extends NJBaseService<Long, Faq, FaqRepository> implements FaqService {

	@Resource(name = "faqRepository")
	public void setFaqRepository(FaqRepository faqRepository) {
		this.repository = faqRepository;
	}

	@Override
	public List<Faq> listFaqs(String category, String subcategory) {
		return getRepository().findByCategoryEqualsAndSubcategoryEqualsOrderByOrderFaqAsc(category, subcategory);
	}

	@Override
	public List<Faq> listFavouriteFaqs(String category, String subcategory) {
		return getRepository().findByCategoryEqualsAndSubcategoryEqualsAndFavouriteEqualsOrderByOrderFaqAsc(category, subcategory, true);
	}

	@Override
	public List<Faq> listFaqs(String category) {
		return getRepository().findByCategoryEqualsOrderByOrderFaqAsc(category);
	}
	@Override
	public Faq save(Faq entity) {
		List<Faq> faqs = this.repository.findByCategoryEqualsAndSubcategoryEqualsOrderByOrderFaqAsc(entity.getCategory(), entity.getSubcategory());

		if (!faqs.isEmpty() && entity.getOrderFaq() < faqs.size()) {
			faqs.add(entity.getOrderFaq() - 1, entity);
		}	else {
			faqs.add(entity);
		}
		
		for (int i = 1 ; i <= faqs.size() ; i++ ) {
			faqs.get(i-1).setOrderFaq(i);
		}

		repository.save(faqs);

		return entity;
	}
	
	@Override
	public Faq update(Faq entity) {
		
		entity = this.getRepository().save(entity);
		
//		List<Faq> faqs = this.repository.findByIdNotAndCategoryEqualsAndSubcategoryEqualsOrderByOrderFaqAsc(entity.getId(), entity.getCategory(), entity.getSubcategory());
//		Faq toUpdate = repository.findOne(entity.getId());
//		
//		String category = toUpdate.getCategory();
//		String subcategory = toUpdate.getSubcategory();
//		
//		boolean mustUpdate = false; // !category.equals(entity.getCategory()) || !subcategory.equals(entity.getSubcategory()); 
//		
//		if (entity.getOrderFaq() < faqs.size()) {
//			faqs.add(entity.getOrderFaq() - 1, entity);
//		} else {
//			faqs.add(entity);
//		}
//		
//		for (int i = 1 ; i <= faqs.size() ; i++ ) {
//			faqs.get(i-1).setOrderFaq(i);
//		}
//		
//		repository.save(faqs);
//		
//		if (mustUpdate) {
//			faqs = this.repository.findByCategoryEqualsAndSubcategoryEqualsOrderByOrderFaqAsc(entity.getCategory(), entity.getSubcategory());
//			for (int i = 1 ; i <= faqs.size() ; i++ ) {
//				faqs.get(i-1).setOrderFaq(i);
//			}
//		}
		return entity;
	}
	
	@Override
	public void remove(Long id) {
		Faq one = repository.findOne(id);
		List<Faq> faqs = this.repository.findByIdNotAndCategoryEqualsAndSubcategoryEqualsOrderByOrderFaqAsc(id, one.getCategory(), one.getSubcategory());
		for (int i = 1 ; i <= faqs.size() ; i++) {
			faqs.get(i-1).setOrderFaq(i);
		}
		repository.save(faqs);
		super.remove(id);
	}
	
}

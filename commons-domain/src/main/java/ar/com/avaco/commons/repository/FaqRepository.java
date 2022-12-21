/**
 * 
 */
package ar.com.avaco.commons.repository;

import java.util.List;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.commons.domain.Faq;

/**
 * @author avaco
 *
 */
public interface FaqRepository extends NJRepository<Long,Faq>, FaqRepositoryCustom {

	List<Faq> findByCategoryEqualsAndSubcategoryEqualsAndFavouriteEqualsOrderByOrderFaqAsc(String category, String subcategory, boolean favourite);

	List<Faq> findByCategoryEqualsAndSubcategoryEqualsOrderByOrderFaqAsc(String category, String subcategory);

	List<Faq> findByCategoryEqualsOrderByOrderFaqAsc(String category);

	List<Faq> findByIdNotAndCategoryEqualsAndSubcategoryEqualsOrderByOrderFaqAsc(Long id, String category,	String subcategory);
	
}

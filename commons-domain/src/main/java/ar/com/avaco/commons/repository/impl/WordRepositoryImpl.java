/**
 * 
 */
package ar.com.avaco.commons.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.commons.domain.Word;
import ar.com.avaco.commons.repository.WordRepositoryCustom;

/**
 * 
 *
 */
@Repository("wordRepository")
public class WordRepositoryImpl extends NJBaseRepository<Long, Word> implements WordRepositoryCustom {

	public WordRepositoryImpl(EntityManager entityManager) {
		super(Word.class, entityManager);
	}
}


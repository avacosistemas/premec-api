/**
 * 
 */
package ar.com.avaco.commons.service;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.commons.domain.Word;


/**
 * 
 *
 */
public interface WordService extends NJService<Long, Word>{

	Word getByKey(String key);
	
}

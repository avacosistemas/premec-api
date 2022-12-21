/**
 * 
 */
package ar.com.avaco.commons.service;

import javax.persistence.EntityNotFoundException;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.commons.domain.I18n;

/**
 * 
 *
 */
public interface I18nService  extends NJService<Long, I18n> {
	
	public I18n getByNameAndLang(String name, String lang) throws EntityNotFoundException;

}

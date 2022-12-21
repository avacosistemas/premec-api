/**
 * 
 */
package ar.com.avaco.commons.service.impl;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.arc.core.component.bean.service.NJBaseService;
import ar.com.avaco.commons.domain.I18n;
import ar.com.avaco.commons.repository.I18nRepository;
import ar.com.avaco.commons.service.I18nService;

/**
 * @author avaco
 */

@Transactional
@Service("i18nService")
public class I18nServiceImpl extends NJBaseService<Long, I18n, I18nRepository> implements I18nService {

	@Resource(name = "i18nRepository")
	public void setI18nRepository(I18nRepository i18nRepository) {
		this.repository = i18nRepository;
	}

	@Override
	public I18n getByNameAndLang(String name, String lang) throws EntityNotFoundException {
		I18n i18n = this.getRepository().getByNameAndLang(name, lang);
		return i18n;
	}

}

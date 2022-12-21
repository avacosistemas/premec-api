package ar.com.avaco.commons.repository;

import ar.com.avaco.commons.domain.I18n;

public interface I18nRepositoryCustom {

	I18n getByNameAndLang(String name, String lang);
	
}

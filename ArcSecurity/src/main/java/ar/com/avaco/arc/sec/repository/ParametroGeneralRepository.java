package ar.com.avaco.arc.sec.repository;

import ar.com.avaco.arc.sec.domain.ParametroGeneral;

public interface ParametroGeneralRepository {
	ParametroGeneral getParametroGeneral(String param);
	
	void save(String param);	
}
/**
 * 
 */
package ar.com.avaco.commons.service;

import java.util.List;

import ar.com.avaco.commons.domain.FieldDef;
import ar.com.avaco.commons.domain.FormDef;

/**
 * @author avaco
 *
 */
public interface FormDefService {
	FormDef getFormDef(String formKey);
	List<FieldDef> getFieldsDef(String formKey);
	void saveFieldsDef(List<FormDef> formsDef);
}

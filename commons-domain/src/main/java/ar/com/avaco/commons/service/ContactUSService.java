/**
 * 
 */
package ar.com.avaco.commons.service;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.commons.domain.ContactUS;

/**
 * 
 *
 */
public interface ContactUSService  extends NJService<Long, ContactUS> {
	public ContactUS send(ContactUS entity);
}

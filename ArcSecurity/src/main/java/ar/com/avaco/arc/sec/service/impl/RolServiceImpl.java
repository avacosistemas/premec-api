/**
 * 
 */
package ar.com.avaco.arc.sec.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Rol;
import ar.com.avaco.arc.sec.repository.RolRepository;
import ar.com.avaco.arc.sec.service.RolService;
import ar.com.avaco.arc.core.component.bean.service.NJBaseService;

/**
 * @author avaco
 */
@Transactional
@Service("rolService")
public class RolServiceImpl extends NJBaseService<Long, Rol, RolRepository>
		implements RolService {

	@Resource(name = "rolRepository")
	public void setRolRepository(RolRepository rolRepository) {
		repository = rolRepository;
	}
}
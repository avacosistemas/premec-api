/**
 * 
 */
package ar.com.avaco.commons.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.arc.core.component.bean.service.NJBaseService;
import ar.com.avaco.commons.domain.ItemChecklistGrupo;
import ar.com.avaco.commons.repository.ItemChecklistGrupoRepository;
import ar.com.avaco.commons.service.ItemCheckListGrupoService;

/**
 * @author avaco
 */

@Transactional
@Service("itemChecklistGrupoService")
public class ItemChecklistGrupoServiceImpl extends NJBaseService<Long, ItemChecklistGrupo, ItemChecklistGrupoRepository>
		implements ItemCheckListGrupoService {

	@Resource(name = "itemChecklistGrupoRepository")
	public void setRepository(ItemChecklistGrupoRepository itemChecklistGrupoRepository) {
		repository = itemChecklistGrupoRepository;
	}

}

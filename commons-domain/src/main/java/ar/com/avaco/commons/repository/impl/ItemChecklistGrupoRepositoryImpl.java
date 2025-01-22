/**
 * 
 */
package ar.com.avaco.commons.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.commons.domain.ItemChecklistGrupo;
import ar.com.avaco.commons.repository.GrupoTipoActividadRepositoryCustom;

@Repository("itemChecklistGrupoRepository")
public class ItemChecklistGrupoRepositoryImpl extends NJBaseRepository<Long, ItemChecklistGrupo>
		implements GrupoTipoActividadRepositoryCustom {

	public ItemChecklistGrupoRepositoryImpl(EntityManager entityManager) {
		super(ItemChecklistGrupo.class, entityManager);
	}
}

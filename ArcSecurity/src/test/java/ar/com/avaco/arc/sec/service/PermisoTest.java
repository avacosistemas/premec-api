package ar.com.avaco.arc.sec.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.sec.service.PermisoService;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations={"classpath:arc/sec/spring/arcSecContext.xml"})
public class PermisoTest {

	@Autowired
	private PermisoService permisoService;
	
	@Test
	public void basicCRUDTest() {
		String codigo = "PERMISO_FAKE";
		String descripcion = "Descripcion Fake";

		Permiso permiso = new Permiso();
		permiso.setActivo(true);
		permiso.setCodigo(codigo);
		permiso.setDescripcion(descripcion);

		Assert.assertNull(permiso.getId());
		
		Permiso saved = permisoService.save(permiso);
		
		Assert.assertNotNull(saved.getId());
		
		Permiso recoveredById = permisoService.get(saved.getId());
		
		Assert.assertEquals(recoveredById, permiso);
		Assert.assertEquals(recoveredById.isActivo(), permiso.isActivo());
		Assert.assertEquals(recoveredById.getAuthority(), permiso.getAuthority());
		Assert.assertEquals(recoveredById.getDescripcion(), permiso.getDescripcion());
		Assert.assertEquals(recoveredById.getCodigo(), permiso.getCodigo());
		
		recoveredById.setActivo(false);
		Assert.assertNull(recoveredById.getAuthority());
		
		Permiso transientPermiso = new Permiso();
		Long id = 1L;
		transientPermiso.setId(id);
		Assert.assertEquals(transientPermiso.getId(), id);
		
	}
	
	public void setPermisoService(PermisoService permisoService) {
		this.permisoService = permisoService;
	}
	
}

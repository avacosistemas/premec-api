package ar.com.avaco.arc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.avaco.arc.utils.domain.Personaje;
import ar.com.avaco.arc.utils.domain.service.PersonajeService;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@ContextConfiguration(locations={"classpath:/arc/core/spring/applicationContext.xml"})
public class GenericMethodsTest {

	@Autowired
	private PersonajeService service;
	
	private Personaje entity;
	
	private List<Personaje> entityList;

	@Before
	public void setUp(){
		this.entity = new Personaje("Nombre3");
		
		this.entityList = new ArrayList<Personaje>();
		for (int i = 0; i < 5; i++) {
			this.entityList.add(new Personaje("Nombre" + i));
		}
	}
	
	@Test
	public void genericMethodsTest() {
		Assert.assertNotNull(this.service.save(this.entity).getId());
		
		for (Personaje elem : this.service.save(this.entityList)) {
			Assert.assertNotNull(elem.getId());
		} 
		
		Assert.assertEquals(6, this.service.list().size());
		
		this.entity = this.service.get(1l); 
		this.entity.setNombre("updateName");
		Assert.assertEquals("updateName", this.service.update(this.entity).getNombre());

		this.service.remove(1l);
		Assert.assertNull(this.service.get(1l));
	}
}
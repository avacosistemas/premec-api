package ar.com.avaco.arc.utils.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.arc.utils.domain.Personaje;
import ar.com.avaco.arc.utils.domain.filter.PersonajeFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@ContextConfiguration(locations={"classpath:/arc/core/spring/applicationContext.xml"})
public class PersonajeTest {

	@Autowired
	private PersonajeService personajeService;

	@Test
	public void personajeFilterTest() {
		AbstractFilter af = new PersonajeFilter();
		Assert.assertNotNull(af.getFilterDatas());
	}
	
	@Test
	public void saveRecoverByNameTest() {
		Personaje Personaje = new Personaje();
		String nombre = "Pinocho";
		
		Personaje.setNombre(nombre);
		personajeService.save(Personaje);
		
		List<Personaje> expected = personajeService.getByNombre(nombre);
		
		Assert.assertNotNull(expected);
		Assert.assertEquals(expected.size(), 1);
		Assert.assertEquals(Personaje.getNombre(), expected.get(0).getNombre());
		
	}

	@Test
	public void saveRecoverByPatternTest() {
		Personaje Personaje = new Personaje();
		String nombre = "Pepe Grillo";
		
		Personaje.setNombre(nombre);
		personajeService.save(Personaje);
		
		List<Personaje> expected = personajeService.listPattern("nombre", nombre);
		
		Assert.assertNotNull(expected);
		Assert.assertEquals(expected.size(), 1);
		Assert.assertEquals(Personaje.getNombre(), expected.get(0).getNombre());
		
	}

	@Test
	public void saveRecoverByFilterTest() {
		Personaje Personaje = new Personaje();
		String nombre = "Hada Azul";
		
		Personaje.setNombre(nombre);
		personajeService.save(Personaje);
		
		PersonajeFilter pf = new PersonajeFilter();
		pf.setNombreEquals(nombre);
		
		List<Personaje> expected = personajeService.listFilter(pf);
		
		Assert.assertNotNull(expected);
		Assert.assertEquals(expected.size(), 1);
		Assert.assertEquals(Personaje.getNombre(), expected.get(0).getNombre());
		
	}

	@Test
	public void saveAndListSortDescTest() {

		String nombreDesc = "Simba";
		
		for (int i = 6; i <= 9; i++) {
			Personaje Personaje = new Personaje();
			Personaje.setNombre(nombreDesc + i);
			personajeService.save(Personaje);
		}
		
		List<Personaje> Personajes = new ArrayList<Personaje>();
		for (int i = 5; i >= 1; i--) {
			Personaje Personaje = new Personaje();
			Personaje.setNombre(nombreDesc + i);
			Personajes.add(Personaje);
		}
		personajeService.save(Personajes);
		
		PersonajeFilter pf = new PersonajeFilter();
		pf.setAsc(false);
		pf.setIdx("nombre");
		pf.setNombreLike(nombreDesc);
		
		List<Personaje> list = personajeService.listFilter(pf);
		
		Assert.assertEquals(9, list.size());
		
		int i = 10;
		
		for (Personaje Personaje : list) {
			i--;
			Assert.assertEquals(nombreDesc + i, Personaje.getNombre());
		}
		
		int listCount = personajeService.listCount(pf);
		
		Assert.assertEquals(9, listCount);
		
	}

	@Test
	public void saveAndListSortAscTest() {
		
		String nombreAsc = "Mufasa";
		
		for (int i = 6; i <= 9; i++) {
			Personaje Personaje = new Personaje(nombreAsc + i);
			personajeService.save(Personaje);
		}
		
		List<Personaje> Personajes = new ArrayList<Personaje>();
		for (int i = 5; i >= 1; i--) {
			Personaje Personaje = new Personaje(nombreAsc + i);
			Personajes.add(Personaje);
		}
		personajeService.save(Personajes);
		
		PersonajeFilter pf = new PersonajeFilter();
		pf.setAsc(true);
		pf.setIdx("nombre");
		pf.setNombreLike(nombreAsc);
		
		List<Personaje> list = personajeService.listFilter(pf);
		
		Assert.assertEquals(9, list.size());
		
		int i = 0;
		
		for (Personaje Personaje : list) {
			i++;
			Assert.assertEquals(nombreAsc + i, Personaje.getNombre());
		}
		
		int listCount = personajeService.listCount(pf);
		
		Assert.assertEquals(9, listCount);
		
		
	}
	
	@Test
	public void paginationTest() {
		String nombre = "Enanito";
		for (int i = 1; i <= 7; i++) {
			Personaje personaje = new Personaje(nombre + i);
			personajeService.save(personaje);
		}
		
		PersonajeFilter pf = new PersonajeFilter();
		pf.setAsc(true);
		pf.setIdx("nombre");
		pf.setNombreLike(nombre);
		pf.setFirst(0);
		pf.setRows(3);
		
		List<Personaje> personajes = personajeService.listFilter(pf);
		
		Assert.assertEquals(personajes.size(), 3);
		
		int i = 1;
		for (Personaje p : personajes) {
			Assert.assertEquals(p.getNombre(), nombre + i);
			i++;
		}
		
		pf.setFirst(3);
		personajes = personajeService.listFilter(pf);

		Assert.assertEquals(personajes.size(), 3);
		
		int j = 4;
		for (Personaje p : personajes) {
			Assert.assertEquals(p.getNombre(), nombre + j);
			j++;
		}
		
	}
	
	public void setPersonajeService(PersonajeService PersonajeService) {
		this.personajeService = PersonajeService;
	}

}

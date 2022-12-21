package ar.com.avaco.arc.core.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.arc.utils.domain.Personaje;

/**
 * Test class for {@link Entity}.
 */
public class EntityTest {

	private Personaje entity;

	/**
	 * Method run before every test.
	 */
	@Before
	public void setUp() {
		this.entity = new Personaje();
	}

	@Test
	public void toStringTest() throws Exception {
		entity.setId(1l);
		Assert.assertEquals(entity.toString(), "Personaje: " + 1l);
	}

	/**
	 * Test method for {@link Entity#hashCode()}.
	 * 
	 * @param id
	 *            The ID that the entity will have.
	 * @param result
	 *            The expected result.
	 */
	@Test
	public void hashCodeTest() {
		entity.setId(1l);
		Assert.assertEquals(entity.hashCode(), 32);
	}

	/**
	 * Test method for {@link Entity#equals(Object)}
	 * 
	 * @param comparable
	 *            The object to compare the entity to.
	 * @param expected
	 *            The expected result of the method.
	 */
	@Test
	public void equalsTest() {
		entity.setId(1L);
		
		Personaje comparable = new Personaje();
		comparable.setId(1L);
		
		Assert.assertEquals(entity.equals(comparable), true);
	}

	/**
	 * Test method for {@link Entity#equals(Object)}.<br>
	 * Tests the case of matching an entity with itself.
	 */
	@Test
	public void equalsSameInstanceTest() {
		entity.setId(1L);
		Assert.assertTrue(entity.equals(entity));
	}
}
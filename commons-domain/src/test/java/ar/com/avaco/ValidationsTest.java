package ar.com.avaco;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import ar.com.avaco.validation.Validations;

public class ValidationsTest {

	@Test
	public void telefonoTest() {
		assertTrue(Validations.isCelularValido("333315889999"));
		assertTrue(Validations.isCelularValido("333158889999"));
		assertTrue(Validations.isCelularValido("331588889999"));
		assertTrue(Validations.isCelularValido("331588889999"));
		assertFalse(Validations.isCelularValido("33158888999"));
		assertFalse(Validations.isCelularValido("315588889999"));
		assertFalse(Validations.isCelularValido("155588889999"));
	}
	
	@Test
	public void mayorEdadTest() {
		Calendar mayor18 = Calendar.getInstance();
		mayor18.add(Calendar.YEAR, -18);
		assertTrue(Validations.isMayorEdad(mayor18.getTime()));
	}

	@Test
	public void menorEdadTest() {
		Calendar menor18 = Calendar.getInstance();
		menor18.add(Calendar.YEAR, -18);
		menor18.add(Calendar.DAY_OF_MONTH, 1);
		assertFalse(Validations.isMayorEdad(menor18.getTime()));
	}
	
	@Test
	public void cbuTest() {
		assertTrue(Validations.isCBUValido("0720000720000003079036"));
		assertTrue(Validations.isCBUValido("2590035610017865310022"));
		assertTrue(Validations.isCBUValido("0167777100002339456176"));
		assertTrue(Validations.isCBUValido("0140999801200008155211"));
		assertTrue(Validations.isCBUValido("0140305101622902739496"));
		assertFalse(Validations.isCBUValido("1140305101622902739496"));
		assertFalse(Validations.isCBUValido("3340305101622902739496"));
		assertFalse(Validations.isCBUValido("4140305101622902739496"));
		assertFalse(Validations.isCBUValido("0140305101622902739491"));
		assertFalse(Validations.isCBUValido("0140305101622902739492"));
		assertFalse(Validations.isCBUValido("0140305101622902739493"));
		assertFalse(Validations.isCBUValido("0140305101622902739416"));
		assertFalse(Validations.isCBUValido("014030510162290273941"));
		assertFalse(Validations.isCBUValido("014030510162290273941"));
		assertFalse(Validations.isCBUValido("014030510162290AA273941"));
		assertFalse(Validations.isCBUValido("014030510162290A273941"));
		assertFalse(Validations.isCBUValido("a111111111111111111111"));
	}
	
}

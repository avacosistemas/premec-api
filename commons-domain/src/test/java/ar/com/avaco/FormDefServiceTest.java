package ar.com.avaco;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ar.com.avaco.commons.domain.FieldDef;
import ar.com.avaco.commons.domain.FormDef;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.commons.service.FormDefService;
import ar.com.avaco.commons.service.impl.FormDefServiceImpl;
import ar.com.avaco.commons.service.impl.ValidationFormDefServiceImpl;

public class FormDefServiceTest {

	private FormDefService formDefService;
	
	@Before
	public void setUpFormDefService() {
		this.formDefService = new FormDefServiceImpl();
		File fdir = new File("src/test/resources/config/form-def/");
		List<FormDef> formsDef = new ArrayList<>();
		if(fdir.isDirectory()) {
			List<File> files = Arrays.asList(fdir.listFiles());
			files.stream().forEach(current -> {
				if(current.isFile()) {				
					FileReader fr;
					try {
						fr = new FileReader(current);
						Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
						FormDef formDef = gson.fromJson(fr, FormDef.class);
						formsDef.add(formDef);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			});
		}
		formDefService.saveFieldsDef(formsDef);			
	}
	
	@Test
	public void testSetUp() {
		List<FieldDef> fields = this.formDefService.getFieldsDef("testDto");
		assertTrue( fields != null);
		if(fields != null) {
			assertTrue(fields.size() > 1);
			if(fields.size() == 1) {
				assertTrue("lang".equals(fields.get(0).getKey()));	
			}
		}
	}
	
	@Test
	public void testValidDTO() {
		FormDef  formDef = this.formDefService.getFormDef("testDto");
		ValidationFormDefServiceImpl validationService = new ValidationFormDefServiceImpl();
		TestDTO testDto = new TestDTO();
		testDto.setNum(1);
		testDto.setDate("10/09/1989");
		testDto.setDate2("10/09/1989");
		testDto.setLang("ES");
		testDto.setStringNumber("10");
		validationService.validate(testDto, formDef);
	}
	
	@Test
	public void testInvalidDTO() {
		FormDef  formDef = this.formDefService.getFormDef("testDto");
		ValidationFormDefServiceImpl validationService = new ValidationFormDefServiceImpl();
		TestDTO testDto = new TestDTO();
		testDto.setNum(8);
		testDto.setDate("10/09/1989");
		testDto.setDate2("10/09/1989");
		testDto.setLang("ESP");
		testDto.setStringNumber("10N");
		try {
			validationService.validate(testDto, formDef);
			assertTrue("No se lanzo exception de error de campos", false);
		}catch(ErrorValidationException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testRequired() {
		FormDef  formDef = this.formDefService.getFormDef("testRequired");
		ValidationFormDefServiceImpl validationService = new ValidationFormDefServiceImpl();
		TestDTO testDto = new TestDTO();
		testDto.setNum(8);
		try {
			validationService.validate(testDto, formDef);
			assertTrue(true);
		}catch(ErrorValidationException e) {
			assertTrue("No paso el test de requerido", false);
		}
	}
	
	@Test
	public void testInvalidRequired() {
		FormDef  formDef = this.formDefService.getFormDef("testInvalidRequired");
		ValidationFormDefServiceImpl validationService = new ValidationFormDefServiceImpl();
		TestDTO testDto = new TestDTO();
		try {
			validationService.validate(testDto, formDef);
			assertTrue("No paso el test de InvalidRequired", false);
		}catch(ErrorValidationException e) {				
			assertTrue("No paso el test de InvalidRequired", e.getErrors().containsKey("num"));
		}
	}

	@Test
	public void testValidRegex() {
		FormDef  formDef = this.formDefService.getFormDef("testValidRegex");
		ValidationFormDefServiceImpl validationService = new ValidationFormDefServiceImpl();
		TestDTO testDto = new TestDTO();
		testDto.setSpecialString("10");
		try {
			validationService.validate(testDto, formDef);
			assertTrue(true);
		}catch(ErrorValidationException e) {				
			assertFalse("No paso el test de ValidRegex", e.getErrors().containsKey("specialString"));
		}
	}

	@Test
	public void testInvalidRegex() {
		FormDef  formDef = this.formDefService.getFormDef("testInvalidRegex");
		ValidationFormDefServiceImpl validationService = new ValidationFormDefServiceImpl();
		TestDTO testDto = new TestDTO();
		testDto.setSpecialString("10X");
		try {
			validationService.validate(testDto, formDef);
			assertTrue("No paso el test de InvalidRegex", false);
		}catch(ErrorValidationException e) {				
			assertTrue("No paso el test de InvalidRegex", e.getErrors().containsKey("specialString"));
		}
	}

}

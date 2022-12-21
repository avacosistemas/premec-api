package ar.com.avaco.commons.service.impl;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.commons.domain.FieldDef;
import ar.com.avaco.commons.domain.FormDef;
import ar.com.avaco.commons.domain.OptionField;
import ar.com.avaco.commons.domain.ValidationField;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.commons.service.ValidationFormDefService;

@Transactional
@Service("validationFormDefService")
public class ValidationFormDefServiceImpl implements ValidationFormDefService {

	@Override
	public void validate(Object dto, FormDef formDef) throws ErrorValidationException {
		Class<? extends Object> dtoClass = dto.getClass();
		List<FieldDef> fields = formDef.getFields();
		Map<String, String> errors = new HashMap<>();
		
		try {
			for(FieldDef f : fields){
				String fieldKey = f.getKey();
				Field field = dtoClass.getDeclaredField(fieldKey);
				field.setAccessible(true);
				if (!(FieldDef.TYPE_DATE.equals(f.getType().toUpperCase()) || 
						FieldDef.TYPE_EMAIL.equals(f.getType().toUpperCase())|| 
						FieldDef.TYPE_LIST.equals(f.getType().toUpperCase())) &&
						!f.getType().toLowerCase().equals(field.getType().getSimpleName().toLowerCase())){
					errors.put(f.getKey(), "El campo {0} debe ser de tipo " + f.getType());
				}else {
					Object value = field.get(dto);
					List<Supplier<Boolean>> validations = new ArrayList<>();
					validations.add(() -> this.validateRequired(value, f, errors));
					validations.add(() -> this.validateDate(value,field, f, errors));
					validations.add(() -> this.validateLength(value, f, errors));
					validations.add(() -> this.validateMinLength(value, f, errors));
					validations.add(() -> this.validateMaxLength(value, f, errors));
					validations.add(() -> this.validateMinValue(value, f, errors));
					validations.add(() -> this.validateMaxValue(value, f, errors));
					validations.add(() -> this.runOthersValidations(value, f, errors));
					Iterator<Supplier<Boolean>> it = validations.iterator();
					Supplier<Boolean> test = null;
					do { 
						test = it.next();
					}
					while(test.get() && it.hasNext());
					
				}
				field.setAccessible(false);
			};
			if(!errors.isEmpty()) {
				throw new ErrorValidationException("Surgio un error en la validacion de campos", errors);
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	
		
	}
	
	private boolean runOthersValidations(Object value,FieldDef f, Map<String, String> errors) {
		boolean ok = true;
		if(value != null) {
			List<ValidationField> validations = f.getValidations();
			if(validations != null) {
				for(ValidationField v : validations) {
					if(ValidationField.REGEX.equals(v.getKey().toUpperCase())) {
						Pattern pattern = Pattern.compile(v.getInput());
						Matcher matcher = pattern.matcher(String.valueOf(value));
						if(!matcher.matches()){
							String message = "El campo {0} no respeta la expression regular {1}";
							if(v.getMessage() != null) {
								message = v.getMessage();
							}
							errors.put(f.getKey(), MessageFormat.format(message, "{0}", v.getInput()));
							ok = false;
						}
					}
				}
			}
		}
		return ok;
	}

	private boolean validateDate(Object value,Field field, FieldDef f, Map<String, String> errors) {
		boolean ok = true;
		if(value != null && FieldDef.TYPE_DATE.equals(f.getType())) {
			if(!FieldDef.TYPE_STRING.toLowerCase().equals(field.getType().getSimpleName().toLowerCase())) {
				errors.put(f.getKey(), "El valor del campo {0} enviado debe ser de tipo cadena ");
				ok = false;
			}else {				
				OptionField options = f.getOptions();
				String format = options.getFormat();
				if(format != null){
					SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getInstance();
					dateFormat.applyPattern(format);
					try {
						dateFormat.parse((String) value);
					} catch (ParseException e) {
						errors.put(f.getKey(), "El campo {0} debe ser de un formato " + format);
						ok = false;
					}
				}
			}
		}
		return ok;
	}
	
	private boolean validateMinLength(Object value,FieldDef f, Map<String, String> errors) {
		boolean ok = true;
		if(value != null) {
			if(FieldDef.TYPE_ARRAY.equals(f.getType())){
				// Hoy esto no se usa en la app
				
			}else if ( (FieldDef.TYPE_NUMBER.equals(f.getType()) || FieldDef.TYPE_STRING.equals(f.getType())) && f.getMinLength() != null &&
					     String.valueOf(value).length() < f.getMinLength()){
				errors.put(f.getKey(), "El campo {0} debe tener una longitud minima de " + f.getMinLength());
				ok = false;
			}
		}
		return ok;
	}
	
	private boolean validateMaxLength(Object value,FieldDef f, Map<String, String> errors) {
		boolean ok = true;
		if(value != null) {
			if(FieldDef.TYPE_ARRAY.equals(f.getType())){
				// Hoy esto no se usa en la app
			}else if ( (FieldDef.TYPE_NUMBER.equals(f.getType()) || FieldDef.TYPE_STRING.equals(f.getType())) && f.getMaxLength() != null &&
					     String.valueOf(value).length() > f.getMaxLength()){
				errors.put(f.getKey(), "El campo {0} debe tener una longitud maxima de " + f.getMaxLength());
				ok = false;
			}
		}
		return ok;
	}
	
	private boolean validateMinValue(Object value,FieldDef f, Map<String, String> errors) {
		boolean ok = true;
		if(value != null) {
			if(FieldDef.TYPE_ARRAY.equals(f.getType())){
				// Hoy esto no se usa en la app
			}else if ( (FieldDef.TYPE_NUMBER.equals(f.getType()) && f.getMinValue() != null &&
					     ((Long) value) < f.getMinValue())){
				errors.put(f.getKey(), "El campo {0} debe tener un valor minimo de " + f.getMinValue());
				ok = false;
			}
		}
		return ok;
	}
	
	private boolean validateMaxValue(Object value,FieldDef f, Map<String, String> errors) {
		boolean ok = true;
		if(value != null) {
			if(FieldDef.TYPE_ARRAY.equals(f.getType())){
				// Hoy esto no se usa en la app
			}else if ( (FieldDef.TYPE_NUMBER.equals(f.getType()) && f.getMaxValue() != null &&
					     ((Long) value) > f.getMaxValue())){
				errors.put(f.getKey(), "El campo {0} debe tener un valor minimo de " + f.getMaxValue());
				ok = false;
			}
		}
		return ok;
	}

	private boolean validateLength(Object value,FieldDef f, Map<String, String> errors) {
		boolean ok = true;
		if(value != null) {
			if(FieldDef.TYPE_ARRAY.equals(f.getType())){
				// Hoy esto no se usa en la app
			}else if ( (FieldDef.TYPE_NUMBER.equals(f.getType()) || FieldDef.TYPE_STRING.equals(f.getType())) && f.getLength() != null &&
					     String.valueOf(value).length() != f.getLength()){
				errors.put(f.getKey(), "El campo {0} debe tener una longitud igual a " + f.getLength());
				ok = false;
			}
		}
		return ok;
	}
	
	private boolean validateRequired(Object value, FieldDef field, Map<String, String> errors) {
		boolean ok = true;
		if(field.getRequired() != null && field.getRequired()) {

			if( value == null) {
				errors.put(field.getKey(), "El campo {0} es requerido");
				ok = false;
			}else {
				if(FieldDef.TYPE_STRING.equals(field.getType().toUpperCase())){
					if( ((String) value).length() == 0) {
						errors.put(field.getKey(), "El campo {0} es requerido");
						ok = false;
					}
				}
			}
		}
		return ok;
	}
}

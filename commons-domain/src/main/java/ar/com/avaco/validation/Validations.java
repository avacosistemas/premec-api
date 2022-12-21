package ar.com.avaco.validation;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class Validations {

	public static final String ALIAS_BANCO_PATTERN = "^[a-zA-Z0-9\\-\\.]*$";
	public static final String PARAMETER_KEY_PATTERN = "^[a-z][a-z0-9_-]*$";
	public static final String CUIL_PATTERN = "^(20|23|24|27)[0-9]{9}$";
	public static final String CUIT_PATTERN = "^(20|23|24|27|30|33|34)[0-9]{9}$";
	public static final String DNI_PATTERN = "^(\\d{7}|\\d{8})$";
	public static final String NACIONALIDAD_PATTERN = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ ]*$";
	public static final String CARGO_POLITICO_PATTERN = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ ]*$";
	public static final String USERNAME_PATTERN = "^[a-z][a-z0-9_-]{4,40}$";

	public static final String DOMICILIO_PATTERN = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ \\-\\.]*$";
	public static final String CODIGO_POSTAL_PATTERN = "^(\\d{4})|([a-zA-Z]\\d{4}[a-zA-Z]{3})$";
	public static final String BARRIO_PATTERN = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ \\-\\.]*$";
	public static final String LOCALIDAD_PATTERN = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ \\-\\.]*$";

	public static final String TELEFONO_PATTERN = "^\\d{10}$";
	public static final String CELULAR_PATTERN = "^\\d{12}$";
	public static final String CBU_PATTERN = "^\\d{22}$";

	public static final Integer MAYORIA_EDAD = 18;

	public static Boolean isTelefonoValido(String telefono) {
		return telefono.length() == 10 && isRegexValid(TELEFONO_PATTERN, telefono);
	}

	public static Boolean isLocalidadValido(String localidad) {
		return localidad.length() >= 2 &&  localidad.length() <= 80 && isRegexValid(LOCALIDAD_PATTERN, localidad);
	}

	public static Boolean isCelularValido(String celular) {
		boolean longitud = celular.length() == 12;
		Boolean todosNumeros = isRegexValid(CELULAR_PATTERN, celular);
		if (longitud && todosNumeros) {
			return celular.substring(2, 6).contains("15");
		}
		return false;
	}

	public static Boolean isDomicilioValido(String domicilio) {
		return domicilio.length() >= 2 && domicilio.length() <= 80 && isRegexValid(DOMICILIO_PATTERN, domicilio);
	}
	
	public static Boolean isCodigoPostalValido(String cp) {
		return isRegexValid(CODIGO_POSTAL_PATTERN, cp);
	}
	
	public static Boolean isBarrioValido(String barrio) {
		return barrio.length() >= 2 &&  barrio.length() <= 80 && isRegexValid(BARRIO_PATTERN, barrio);
	}

	public static Boolean isAliasBancoFormatoValido(String alias) {
		return isRegexValid(ALIAS_BANCO_PATTERN, alias);
	}

	public static Boolean isUsernameValido(String username) {
		return isRegexValid(USERNAME_PATTERN, username);
	}

	public static Boolean isCuilValido(String cuil) {
		return isRegexValid(CUIL_PATTERN, cuil);
	}

	public static Boolean isNacionalidadValido(String nacionalidad) {
		return isRegexValid(NACIONALIDAD_PATTERN, nacionalidad);
	}

	public static Boolean isCargoPoliticoValido(String cargo) {
		return isRegexValid(CARGO_POLITICO_PATTERN, cargo);
	}

	public static Boolean isDNIValido(String dni) {
		return isRegexValid(DNI_PATTERN, dni);
	}

	public static Boolean isCuitValido(String cuit) {
		return isRegexValid(CUIT_PATTERN, cuit);
	}
	
	public static Boolean isParameterKeyValid(String parameterKey) {
		return isRegexValid(PARAMETER_KEY_PATTERN, parameterKey);
	}

	private static Boolean isRegexValid(String regex, String token) {
		Pattern matcher = Pattern.compile(regex);
		boolean matches = matcher.matcher(token).matches();
		return matches;
	}
	
	public static Boolean isDigitoVerificadorCuitCuilValido(String cuit) {
		// Convertimos la cadena que quedó en una matriz de caracteres
		char[] cuitArray = cuit.toCharArray();
		// Inicializamos una matriz por la cual se multiplicarán cada uno de los dígitos
		Integer[] serie = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
		// Creamos una variable auxiliar donde guardaremos los resultados del cálculo
		// del número validador
		Integer aux = 0;
		// Recorremos las matrices de forma simultanea, sumando los productos de la
		// serie por el número en la misma posición
		for (int i = 0; i < 10; i++) {
			aux += Character.getNumericValue(cuitArray[i]) * serie[i];
		}
		// Hacemos como se especifica: 11 menos el resto de de la división de la suma de
		// productos anterior por 11
		aux = 11 - (aux % 11);
		// Si el resultado anterior es 11 el código es 0
		if (aux == 11) {
			aux = 0;
		}
		// Si el resultado anterior es 10 el código no tiene que validar, cosa que de
		// todas formas pasa
		// en la siguiente comparación.
		// Devuelve verdadero si son iguales, falso si no lo son
		// (Esta forma esta dada para prevenir errores, en java 6 se puede usar: return
		// aux.equals(Character.getNumericValue(cuitArray[10]));)
		return Objects.equals(Character.getNumericValue(cuitArray[10]), aux);
	}
	
	/**
	 * Valida un CBU asumiendo que llegan 22 caracteres.
	 * @param cbu
	 * @return
	 */
	public static boolean isCBUValido(String cbu) {
		return isRegexValid(CBU_PATTERN, cbu) && isCodigoBancoValido(cbu) && isCuentaValida(cbu);
	}
	
	public static Boolean isCodigoBancoValido(String cbu) {
		String codigo = cbu.substring(0,8);
		Integer suma;
		Integer diferencia;
		if (codigo.length() != 8)
			 return false;
		char[] banco = codigo.substring(0,3).toCharArray();
		Integer digitoVerificador1 = charToInt(codigo.toCharArray()[3]);
		char[] sucursal = codigo.substring(4,7).toCharArray();
		Integer digitoVerificador2 = charToInt(codigo.toCharArray()[7]);
		suma = charToInt(banco[0]) * 7 + charToInt(banco[1]) * 1 + charToInt(banco[2]) * 3 + digitoVerificador1 * 9 + charToInt(sucursal[0]) * 7 + charToInt(sucursal[1]) * 1 + charToInt(sucursal[2]) * 3;
		diferencia = (10 - (suma % 10)) % 10;
		return diferencia == digitoVerificador2;
	}
 
	public static Boolean isCuentaValida(String cbu) {
		String cuenta = cbu.substring(8,22);
		if(cuenta.length()!=14)
			return false;
		Integer digitoVerificador = charToInt(cuenta.toCharArray()[13]);
		Integer suma;
		Integer diferencia;
		char[] cuentaArray = cuenta.toCharArray();
		suma = charToInt(cuentaArray[0]) * 3 + charToInt(cuentaArray[1]) * 9 + charToInt(cuentaArray[2]) * 7
				+ charToInt(cuentaArray[3]) * 1 + charToInt(cuentaArray[4]) * 3 + charToInt(cuentaArray[5]) * 9
				+ charToInt(cuentaArray[6]) * 7 + charToInt(cuentaArray[7]) * 1 + charToInt(cuentaArray[8]) * 3
				+ charToInt(cuentaArray[9]) * 9 + charToInt(cuentaArray[10]) * 7 + charToInt(cuentaArray[11]) * 1
				+ charToInt(cuentaArray[12]) * 3;
		diferencia = (10 - (suma % 10)) % 10;
		 return diferencia == digitoVerificador;
	}
 
	private static Integer charToInt(char ch){
		return Integer.parseInt(String.valueOf(ch));
	}

	public static boolean isMayorEdad(Date fechaNacimiento) {
		LocalDate fn = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Period periodo = Period.between(fn, LocalDate.now(ZoneId.systemDefault()));
		return periodo.getYears() >= MAYORIA_EDAD;
	}
	
	
}

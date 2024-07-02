package ar.com.avaco.ws.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.internal.LinkedTreeMap;

public class FieldUtils {

	public static final String U_U_HS_MAQ = "U_U_HsMaq";
	public static final String MANUFACTURER_SERIAL_NUM = "ManufacturerSerialNum";
	public static final String CUSTOMER_NAME = "CustomerName";
	public static final String INTERNAL_SERIAL_NUM = "InternalSerialNum";
	public static final String SUBJECT = "Subject";
	public static final String ITEM_CODE = "ItemCode";
	public static final String RESPONSE_ASSIGNEE = "ResponseAssignee";
	public static final String PARENT_OBJECT_ID = "ParentObjectId";
	public static final String LOCATION = "Location";
	public static final String LAST_NAME = "LastName";
	public static final String FIRST_NAME = "FirstName";
	public static final String HANDLED_BY_EMPLOYEE = "HandledByEmployee";
	public static final String U_CON_CARGO = "U_ConCargo";
	public static final String DETAILS = "Details";
	public static final String ACTIVITY_TIME = "ActivityTime";
	public static final String START_DATE = "StartDate";
	public static final String PRIORITY = "Priority";
	public static final String ACTIVITY_CODE = "ActivityCode";
	public static final String U_TALLER = "U_Taller";
	public static final String U_TAREASREAL = "U_Tareasreal";
	public static final String NOTES = "Notes";
	public static final String START_TIME = "StartTime";
	public static final String END_DUE_DATE = "EndDueDate";
	public static final String END_TIME = "EndTime";
	public static final String U_VALORACION = "U_Valoracion";
	public static final String U_NOMBRE_SUPERVISOR = "U_NomSupervisor";
	public static final String U_DNI_SUPERVISOR = "U_DniSupervisor";
	public static final String U_VALORACION_COMENT = "U_ValoracionComent";
	public static final String CUSTOMER_CODE = "CustomerCode";
	public static final String CONTACT_CODE = "ContactCode";
	public static final String TIPO_ACTIVIDAD = "U_T_Tarea";
	public static final String U_ESTADO = "U_Estado";

	public static void validarRequerido(Map<String, Object> fromJson, String key) throws Exception {
		Object object = fromJson.get(key);
		if (object == null || StringUtils.isBlank(object.toString())) {
			throw new Exception("El campo " + key + " no contiene valores");
		}
	}

	public static Boolean getBoolean(Map<String, Object> fromJson, String key, boolean required)
			throws Exception {

		if (required)
			validarRequerido(fromJson, key);

		if (fromJson.get(key) == null) {
			return false;
		}
		return fromJson.get(key).toString().toUpperCase().contains("Y");
	}

	public static Long getActivityCode(Map<String, Object> fromJson, String user) throws Exception {
		Object object = fromJson.get(FieldUtils.ACTIVITY_CODE);
		if (object == null) {
			throw new Exception(
					"No se pudo obtener el " + FieldUtils.ACTIVITY_CODE + " de una actividad del usuario " + user);
		}
		Long value = null;
		try {
			value = (long) Double.parseDouble(object.toString());
		} catch (Exception e) {
			String message = "No se pudo convertir a Long el valor " + object + " del campo " + FieldUtils.ACTIVITY_CODE
					+ " del usuario " + user;
			throw new Exception(message);
		}
		return value;
	}
	
	public static Integer getInteger(LinkedTreeMap<String, Object> fromJson, String key, boolean required) throws Exception {

		if (required)
			FieldUtils.validarRequerido(fromJson, key);

		Object object = fromJson.get(key);

		Integer value = null;

		if (object == null || StringUtils.isBlank(object.toString())) {
			return value;
		}

		try {
			value = (int) Double.parseDouble(object.toString());
		} catch (Exception e) {
			throw new Exception("No se pudo convertir a Long el valor " + object + " del campo " + key);
		}

		return value;
	}

	public static Long getLong(Map<String, Object> fromJson, String key, boolean required) throws Exception {

		if (required)
			validarRequerido(fromJson, key);

		Object object = fromJson.get(key);

		Long value = null;

		if (object == null || StringUtils.isBlank(object.toString())) {
			return value;
		}

		try {
			value = (long) Double.parseDouble(object.toString());
		} catch (Exception e) {
			throw new Exception("No se pudo convertir a Long el valor " + object + " del campo " + key);
		}

		return value;
	}

	public static String getString(Map<String, Object> fromJson, String key, boolean required)
			throws Exception {

		if (required)
			validarRequerido(fromJson, key);

		Object object = fromJson.get(key);

		String value = "";

		if (object == null) {
			return value;
		} else {
			value = object.toString();
		}
		return value;
	}

	public static String getString(Map<String, Object> fromJson, String key, boolean required, int from,
			int to) throws Exception {

		if (required)
			validarRequerido(fromJson, key);

		Object object = fromJson.get(key);

		String value = "";

		if (object == null) {
			return value;
		} else {
			try {
				value = object.toString().substring(from, to);
			} catch (Exception e) {
				throw new Exception(
						"No se pudo obtener el substring del valor " + object.toString() + " de " + from + " a " + to);
			}
		}
		return value;
	}

}

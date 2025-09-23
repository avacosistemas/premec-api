package ar.com.avaco.ws.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.ws.dto.employee.EmployeesInfoReponseSapDTO;
import ar.com.avaco.ws.service.AbstractSapService;

@Service("employeeService")
public class EmployeeServiceImpl extends AbstractSapService implements EmployeeService {

	@Override
	public EmployeesInfoReponseSapDTO getById(Long id) {

		String urlEmployee = urlSAP + "/EmployeesInfo(" + id + ")";

		ResponseEntity<EmployeesInfoReponseSapDTO> employee = null;

		try {
			employee = getRestTemplate().doExchange(urlEmployee, HttpMethod.GET, null,
					EmployeesInfoReponseSapDTO.class);
		} catch (SapBusinessException e) {
		    String fullStackTrace = SapBusinessException.generarStackTraceString(e);
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", urlEmployee);
			errors.put("error", fullStackTrace);
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}

		return employee.getBody();
	}

	

}

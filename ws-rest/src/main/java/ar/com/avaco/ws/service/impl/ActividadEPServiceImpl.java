package ar.com.avaco.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.ws.dto.ActividadTarjetaDTO;
import ar.com.avaco.ws.service.ActividadEPService;

@Service("actividadService")
public class ActividadEPServiceImpl implements ActividadEPService {

	private UsuarioService usuarioService;

	@Value("${urlSAP}")
	private String urlSAP;

	@Override
	public List<ActividadTarjetaDTO> getActividades(String fecha, String username) throws Exception {

		RestTemplate restTemplate = null;
		restTemplate = RestTemplateFactory.getInstance().getLoggedRestTemplate();

//		String usuarioSAP = usuarioService.getUsuarioSAP(username);
		String usuarioSAP = "22";
		String fechaActividad = "'2020-01-07'";

		String actividadUrl = urlSAP + "/Activities?$filter=HandledByEmployee eq " + usuarioSAP
				+ " and ActivityDate eq " + fechaActividad;

		ResponseEntity<String> responseActividades = restTemplate.exchange(actividadUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<String>() {
				});

		Gson gson = new Gson();
		JsonObject array = gson.fromJson(responseActividades.getBody(), JsonObject.class);

		List<ActividadTarjetaDTO> actividades = new ArrayList<>();
		
		String employeeUrl = urlSAP + "/EmployeesInfo({id})";
		String locationsUrl = urlSAP + "/ActivityLocations?$filter=Code eq {id}";
		String serviceCallUrl = urlSAP + "/ServiceCalls({id})";
		
		JsonArray asJsonArray = array.getAsJsonArray("value");
		for (JsonElement element : asJsonArray) {
			LinkedTreeMap fromJson = gson.fromJson(element.getAsJsonObject().toString(), LinkedTreeMap.class);
			
			ActividadTarjetaDTO atdto = new ActividadTarjetaDTO();
			atdto.setPrioridad(fromJson.get("Priority").toString());
			Long activityCode = Long.parseLong(fromJson.get("ActivityCode").toString());
			atdto.setIdActividad(activityCode);
			atdto.setNumero(fromJson.get("ActivityCode").toString());
			atdto.setFecha(fromJson.get("ActivityDate").toString());
			atdto.setHora(fromJson.get("ActivityTime").toString());
			atdto.setTareasARealizar(fromJson.get("Details").toString());
			atdto.setConCargo(fromJson.get("U_ConCargo") == null ? false : Boolean.parseBoolean(fromJson.get("U_ConCargo").toString()));

			Long handledByEmployeeId = Double.valueOf(fromJson.get("HandledByEmployee").toString()).longValue();
			ResponseEntity<String> responseEmployee = restTemplate.exchange(employeeUrl.replace("{id}", handledByEmployeeId.toString()), HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});
			JsonObject employeejson = gson.fromJson(responseEmployee.getBody(), JsonObject.class);
			
			atdto.setEmpleado(employeejson.get("FirstName").getAsString() + " " + employeejson.get("LastName").getAsString());

			Long locationId = Double.valueOf(fromJson.get("Location").toString()).longValue();
			ResponseEntity<String> responseLocation = restTemplate.exchange(locationsUrl.replace("{id}", locationId.toString()), HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
			});
			JsonObject locationjson = gson.fromJson(responseLocation.getBody(), JsonObject.class);
			atdto.setDireccion(locationjson.getAsJsonArray("value").size() == 1 ? locationjson.getAsJsonArray("value").get(0).getAsJsonObject().get("Name").getAsString() : "No encontrada " + locationId.toString());

			Long parentId = Double.valueOf(fromJson.get("ParentObjectId").toString()).longValue();
			ResponseEntity<String> responseServiceCall = restTemplate.exchange(serviceCallUrl.replace("{id}", parentId.toString()), HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
			});
			JsonObject servicejson = gson.fromJson(responseServiceCall.getBody(), JsonObject.class);
			atdto.setAsignadoPor(servicejson.get("ResponseAssignee").toString());
			atdto.setLlamadaID(servicejson.get("ServiceCallID").toString());
			String itemCode = servicejson.get("ItemCode") == JsonNull.INSTANCE ? "" : servicejson.get("ItemCode").toString();
			String descripcionArticulo = servicejson.get("ItemDescription") == JsonNull.INSTANCE ? "" : servicejson.get("ItemDescription").toString();
			atdto.setCodigoArticulo(itemCode + " " + descripcionArticulo);
			atdto.setDetalle(servicejson.get("Subject").getAsString());
			atdto.setNroSerie(servicejson.get("InternalSerialNum").getAsString());
			atdto.setCliente(servicejson.get("CustomerName").getAsString());
			atdto.setNroFabricante(servicejson.get("ManufacturerSerialNum").toString());
			atdto.setHorasMaquina(servicejson.get("U_HorasMaq") == JsonNull.INSTANCE ? 0 : Integer.parseInt(servicejson.get("U_HorasMaq").toString()));
			
			actividades.add(atdto);
		}
		
		return actividades;
	}

	
	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

}

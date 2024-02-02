package ar.com.avaco.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.ws.dto.RepuestoDepositoDTO;
import ar.com.avaco.ws.service.RepuestoEPService;

@Service("repuestoService")
public class RepuestoEPServiceImpl implements RepuestoEPService {

	private static final Logger LOGGER = Logger.getLogger(RepuestoEPServiceImpl.class);

	private UsuarioService usuarioService;

	@Value("${urlSAP}")
	private String urlSAP;
	@Value("${userSAP}")
	private String userSAP;
	@Value("${passSAP}")
	private String passSAP;
	@Value("${dbSAP}")
	private String dbSAP;

	@Override
	public List<RepuestoDepositoDTO> getRepuestos(String username) throws Exception {

		RestTemplate restTemplate = RestTemplateFactory.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP)
				.getLoggedRestTemplate();

		String deposito = usuarioService.getDeposito(username);

		String repuestosUrl = urlSAP
				+ "/$crossjoin(Items,Items/ItemWarehouseInfoCollection)?$expand=Items($select=ItemCode,ItemName),Items/ItemWarehouseInfoCollection($select=WarehouseCode,InStock)&$filter=Items/ItemCode eq Items/ItemWarehouseInfoCollection/ItemCode and Items/ItemWarehouseInfoCollection/WarehouseCode eq '"
				+ deposito + "' and Items/ItemWarehouseInfoCollection/InStock gt 0";

		ResponseEntity<String> responseRepuestos = null;
		try {
			responseRepuestos = restTemplate.exchange(repuestosUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});
		} catch (RestClientException rce) {
			LOGGER.error("Error al obtener los repuestos del usuario " + username);
			LOGGER.error(repuestosUrl);
			LOGGER.error(rce.getMessage());
			throw rce;
		}

		Gson gson = new Gson();
		JsonObject array = gson.fromJson(responseRepuestos.getBody(), JsonObject.class);

		List<RepuestoDepositoDTO> repuestos = new ArrayList<>();

		JsonArray asJsonArray = array.getAsJsonArray("value");

		for (JsonElement element : asJsonArray) {
			LinkedTreeMap fromJson = gson.fromJson(element.getAsJsonObject().toString(), LinkedTreeMap.class);

			RepuestoDepositoDTO ardto = generarRepuesto(fromJson);

			repuestos.add(ardto);
		}
		return repuestos;
	}

	private RepuestoDepositoDTO generarRepuesto(LinkedTreeMap fromJson) {

		Map<String, String> map = (Map<String, String>) fromJson.get("Items");
		String itemCode = map.get("ItemCode");
		String itemName = map.get("ItemName");

		Map<String, Object> map2 = (Map<String, Object>) fromJson.get("Items/ItemWarehouseInfoCollection");
		Double stock = new Double(map2.get("InStock").toString());

		RepuestoDepositoDTO repuesto = new RepuestoDepositoDTO();
		repuesto.setItemCode(itemCode);
		repuesto.setItemName(itemName);
		repuesto.setStock(stock);

		return repuesto;
	}

	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
}

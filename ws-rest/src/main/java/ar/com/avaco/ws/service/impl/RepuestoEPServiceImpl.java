package ar.com.avaco.ws.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.factory.RestTemplatePremec;
import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.ws.dto.RepuestoDepositoDTO;
import ar.com.avaco.ws.service.RepuestoEPService;

@Service("repuestoService")
public class RepuestoEPServiceImpl implements RepuestoEPService {

	private static final Logger LOGGER = Logger.getLogger(RepuestoEPServiceImpl.class);

	@Value("${urlSAP}")
	private String urlSAP;
	@Value("${userSAP}")
	private String userSAP;
	@Value("${passSAP}")
	private String passSAP;
	@Value("${dbSAP}")
	private String dbSAP;

	private UsuarioService usuarioService;

	private MailSenderSMTPService mailService;

	private RestTemplatePremec restTemplate;

	@Override
	public List<RepuestoDepositoDTO> getRepuestos(String username) throws Exception {

		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP, this.restTemplate).get();

		String deposito = usuarioService.getDeposito(username);

		String repuestosUrl = urlSAP + "/$crossjoin(Items,Items/ItemWarehouseInfoCollection)?$expand=Items"
				+ "($select=ItemCode,ItemName),Items/ItemWarehouseInfoCollection"
				+ "($select=WarehouseCode,InStock)&$filter=Items/ItemCode eq "
				+ "Items/ItemWarehouseInfoCollection/ItemCode and Items/ItemWarehouseInfoCollection/WarehouseCode eq '"
				+ deposito + "' and Items/ItemWarehouseInfoCollection/InStock gt 0";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Prefer", "odata.maxpagesize=0");

		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<String> responseRepuestos = null;
		try {
			responseRepuestos = restTemplate.doExchange(repuestosUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<String>() {
					});
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[REPUESTOS] Error al obtener repuestos del usuario " + username;
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("Usuario: " + username + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
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

		if (!repuestos.isEmpty()) {

			String seriadosUrl = generarUrlConsultaSeriado(repuestos);

			ResponseEntity<String> responseRepuestosSeriados = null;
			try {
				responseRepuestosSeriados = restTemplate.doExchange(seriadosUrl, HttpMethod.GET, null,
						new ParameterizedTypeReference<String>() {
						});
			} catch (SapBusinessException e) {
				e.printStackTrace();
				String error = "[REPUESTOS] Error al obtener los seriados de repuestos del usuario " + username;
				if (e.hasToSendMail()) {
					StringBuilder body = new StringBuilder();
					body.append("ErrorCode: " + e.getErrorCode() + "<br>");
					body.append("Usuario: " + username + "<br>");
					body.append("Error: " + e.getMessage() + "<br>");
					if (e.getCause() != null) {
						body.append("Causa: " + e.getCause().toString() + "<br>");
					}
					mailService.sendMail(error, body.toString(), null);
				}
				throw new Exception(error);
			}

			actualizarSeriadoRepuestos(repuestos, responseRepuestosSeriados);

		}
		return repuestos;
	}

	private void actualizarSeriadoRepuestos(List<RepuestoDepositoDTO> repuestos,
			ResponseEntity<String> responseRepuestosSeriados) {
		Gson gson = new Gson();

		JsonObject array = gson.fromJson(responseRepuestosSeriados.getBody(), JsonObject.class);
		JsonArray asJsonArray = array.getAsJsonArray("value");

		Map<String, Boolean> seriados = new HashMap<String, Boolean>();

		for (JsonElement element : asJsonArray) {
			String itemCode = element.getAsJsonObject().get("ItemCode").toString();
			String itemSerialNumber = element.getAsJsonObject().get("ManageSerialNumbers").toString();
			seriados.put(itemCode, itemSerialNumber.contains("tYES"));
		}

		for (int i = 0; i < repuestos.size(); i++) {
			RepuestoDepositoDTO dto = repuestos.get(i);
			Boolean seriado = seriados.get("\"" + dto.getItemCode() + "\"");
			boolean ser = seriado == null ? false : seriado.booleanValue();
			dto.setSeriado(ser);
			repuestos.set(i, dto);
		}
	}

	private String generarUrlConsultaSeriado(List<RepuestoDepositoDTO> repuestos) {
		List<String> codes = repuestos.stream().map(RepuestoDepositoDTO::getItemCode).collect(Collectors.toList());

		for (int i = 0; i < codes.size(); i++) {
			String string = "ItemCode eq '" + codes.get(i) + "'";
			codes.set(i, string);
		}

		String join = StringUtils.join(codes, " or ");

		String seriadosUrl = urlSAP + "/Items?$select=ItemCode,ManageSerialNumbers&$filter=" + join;
		return seriadosUrl;
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

	@Resource(name = "mailSenderSMTPService")
	public void setMailService(MailSenderSMTPService mailService) {
		this.mailService = mailService;
	}
}

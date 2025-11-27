package ar.com.avaco.ws.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetAttachDTO;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetAttachResponse;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetGetDTO;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetLinesResponse;
import ar.com.avaco.ws.service.AbstractSapService;

@Service("timeSheetService")
public class TimeSheetServiceImpl extends AbstractSapService implements TimeSheetService {

	@Override
	public Long generarTimeSheet(Long usuarioSap, String from, String to) {
		Map<String, Object> pmtsMap = new HashMap<>();
		pmtsMap.put("UserID", usuarioSap);
		pmtsMap.put("DateFrom", from);
		pmtsMap.put("DateTo", to);

		String pmtsUrlPost = urlSAP + "/ProjectManagementTimeSheet";
		HttpEntity<Map<String, Object>> httpEntityAttach = new HttpEntity<>(pmtsMap);

		ResponseEntity<String> pmtsResponse = null;
		try {
			pmtsResponse = getRestTemplate().doExchange(pmtsUrlPost, HttpMethod.POST, httpEntityAttach,
					new ParameterizedTypeReference<String>() {
					});
		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", pmtsUrlPost);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}

		Long absEntry = gson.fromJson(pmtsResponse.getBody(), JsonObject.class).get("AbsEntry").getAsLong();
		return absEntry;
	}

	@Override
	public TimeSheetEntryAttach getTimeSheetEntries(Long usuarioSap, String from, String to) {
		// Armo el parametro de la actividad
		String fechaDesde = "'" + from + "'";
		String fechaHasta = "'" + to + "'";

		String urlObtenerEntryPMTSGet = urlSAP
				+ "/ProjectManagementTimeSheet?$select=AbsEntry,AttachmentEntry&$filter=UserID eq " + usuarioSap
				+ " and DateFrom eq " + fechaDesde + " and DateTo eq " + fechaHasta;

		ResponseEntity<String> entrySetPMTSResponse;
		try {
			entrySetPMTSResponse = getRestTemplate().doExchange(urlObtenerEntryPMTSGet, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});

		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", urlObtenerEntryPMTSGet);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}

		JsonArray pmtsEntryArray = gson.fromJson(entrySetPMTSResponse.getBody(), JsonObject.class)
				.getAsJsonArray("value");

		TimeSheetEntryAttach ret = new TimeSheetEntryAttach();

		// Si obtengo un registros es porque el entry ya existe. Obtengo el entry y el
		// attachment
		if (pmtsEntryArray.size() == 1) {
			ret.setAbsEntry(pmtsEntryArray.get(0).getAsJsonObject().get("AbsEntry").getAsLong());
			JsonElement jsonElement = pmtsEntryArray.get(0).getAsJsonObject().get("AttachmentEntry");
			// El attachment puede o no existir
			if (!jsonElement.isJsonNull()) {
				ret.setAttachmentEntry(jsonElement.getAsLong());
			}
		}

		return ret;
	}

	@Override
	public void updateTimeSheetAttachmentEntry(Long absEntry, Long newAttachmentEntry) {
		String timeSheetPatchEntry = urlSAP + "/ProjectManagementTimeSheet(" + absEntry + ")";

		Map<String, Object> timesheetpatch = new HashMap<>();
		timesheetpatch.put("AttachmentEntry", newAttachmentEntry);

		HttpHeaders headers = getRestTemplate().getDefaultHeaders();
		HttpEntity<Map<String, Object>> httpEntityPatchServiceCall = new HttpEntity<>(timesheetpatch, headers);

		try {

			getRestTemplate().doExchange(timeSheetPatchEntry, HttpMethod.PATCH, httpEntityPatchServiceCall,
					Object.class);
		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", timeSheetPatchEntry);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}
	}

	@Override
	public void updateTimeSheetAttachmentEntry(Long absEntry, Map<String, Object> timesheetpatch) {
		String timeSheetPatchEntry = urlSAP + "/ProjectManagementTimeSheet(" + absEntry + ")";
		
		HttpHeaders headers = getRestTemplate().getDefaultHeaders();
		HttpEntity<Map<String, Object>> httpEntityPatchServiceCall = new HttpEntity<>(timesheetpatch, headers);
		
		try {
			getRestTemplate().doExchange(timeSheetPatchEntry, HttpMethod.PATCH, httpEntityPatchServiceCall,
					Object.class);
		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", timeSheetPatchEntry);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}
	}

	@Override
	public List<ProjectManagementTimeSheetAttachDTO> listTimeSheetByUsuarioSap(Long usuarioSAP) {
		// Preparo la url para enviar el attachment
		String attachmentUrl = urlSAP
				+ "/ProjectManagementTimeSheet?$filter=UserID eq {userId} and AttachmentEntry ne null&$expand=Attachments2&$orderby=DateFrom desc";
		attachmentUrl = attachmentUrl.replace("{userId}", usuarioSAP.toString());

		ResponseEntity<ProjectManagementTimeSheetAttachResponse> timeshteeRespose = null;

		try {
			timeshteeRespose = getRestTemplate().doExchange(attachmentUrl, HttpMethod.GET, null,
					ProjectManagementTimeSheetAttachResponse.class);
		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", attachmentUrl);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}

		List<ProjectManagementTimeSheetAttachDTO> registros = timeshteeRespose.getBody().getValue();

		return registros;
	}

	@Override
	public void updateTimeSheetLines(ProjectManagementTimeSheetGetDTO lineGroup) {

		String timeSheetPatchLines = urlSAP + "/ProjectManagementTimeSheet(" + lineGroup.getAbsEntry() + ")";

		HttpHeaders headers = getRestTemplate().getDefaultHeaders();
		HttpEntity<Map<String, Object>> httpEntityPatchServiceCall = new HttpEntity<>(
				lineGroup.getAsMapUpdateTimeSheetLines(), headers);

		try {
			getRestTemplate().doExchange(timeSheetPatchLines, HttpMethod.PATCH, httpEntityPatchServiceCall,
					Object.class);
		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", timeSheetPatchLines);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}

	}

	@Override
	public ProjectManagementTimeSheetGetDTO getTimeSheet(Long usuarioSap, String from, String to) {
		// Armo el parametro de la actividad
		String fechaDesde = "'" + from + "'";
		String fechaHasta = "'" + to + "'";

		String urlObtenerPMTSGet = urlSAP + "/ProjectManagementTimeSheet?$filter=UserID eq " + usuarioSap
				+ " and DateFrom eq " + fechaDesde + " and DateTo eq " + fechaHasta;

		ResponseEntity<ProjectManagementTimeSheetLinesResponse> timeshteeRespose = null;

		try {
			timeshteeRespose = getRestTemplate().doExchange(urlObtenerPMTSGet, HttpMethod.GET, null,
					ProjectManagementTimeSheetLinesResponse.class);
		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", urlObtenerPMTSGet);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}

		ProjectManagementTimeSheetGetDTO projectManagementTimeSheetGetDTO = null;
		List<ProjectManagementTimeSheetGetDTO> value = timeshteeRespose.getBody().getValue();
		if (value != null && !value.isEmpty())
			projectManagementTimeSheetGetDTO = value.get(0);

		return projectManagementTimeSheetGetDTO;

	}

	@Override
	public List<ProjectManagementTimeSheetGetDTO> getTimeSheets(String from, String to) {
		// Armo el parametro de la actividad
		String fechaDesde = "'" + from + "'";
		String fechaHasta = "'" + to + "'";
		
		String urlObtenerPMTSGet = urlSAP + "/ProjectManagementTimeSheet?$filter=DateFrom eq " + fechaDesde + " and DateTo eq " + fechaHasta;
		
		ResponseEntity<ProjectManagementTimeSheetLinesResponse> timeshteeRespose = null;

		HttpHeaders defaultHeaders = getRestTemplate().getDefaultHeaders();
		defaultHeaders.add("Prefer", "odata.maxpagesize=0");
		HttpEntity<Object> requestEntity = new HttpEntity<>(defaultHeaders);
		
		List<ProjectManagementTimeSheetGetDTO> list = new ArrayList<ProjectManagementTimeSheetGetDTO>();
		
		while (urlObtenerPMTSGet != null && !urlObtenerPMTSGet.isEmpty()) {
		
			try {
				timeshteeRespose = getRestTemplate().doExchange(urlObtenerPMTSGet, HttpMethod.GET, requestEntity,
						ProjectManagementTimeSheetLinesResponse.class);
								
				List<ProjectManagementTimeSheetGetDTO> res = timeshteeRespose.getBody().getValue();
				
				list.addAll(res);
				
				urlObtenerPMTSGet = timeshteeRespose.getBody().getNextLink();
				
				if (urlObtenerPMTSGet != null && !urlObtenerPMTSGet.isEmpty()) {
					urlObtenerPMTSGet = urlSAP + "/" + urlObtenerPMTSGet.replace("%20", " ");
				}
				
			} catch (SapBusinessException e) {
				Map<String, String> errors = new HashMap<String, String>();
				errors.put("url", urlObtenerPMTSGet);
				errors.put("error", e.getMessage());
				e.printStackTrace();
				throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
			}
		}
		
		return list;
		
	}

}

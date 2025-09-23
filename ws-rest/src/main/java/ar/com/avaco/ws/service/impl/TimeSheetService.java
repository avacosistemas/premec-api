package ar.com.avaco.ws.service.impl;

import java.util.List;
import java.util.Map;

import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetAttachDTO;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetGetDTO;

public interface TimeSheetService {

	Long generarTimeSheet(Long usuarioSap, String from, String to);

	TimeSheetEntryAttach getTimeSheetEntries(Long usuarioSap, String from, String to);

	void updateTimeSheetAttachmentEntry(Long absEntry, Long newAttachmentEntry);

	List<ProjectManagementTimeSheetAttachDTO> listTimeSheetByUsuarioSap(Long usuarioSAP);

	void updateTimeSheetLines(ProjectManagementTimeSheetGetDTO lineGroup);

	ProjectManagementTimeSheetGetDTO getTimeSheet(Long usuarioSap, String fechaDesde, String fechaHasta);

	List<ProjectManagementTimeSheetGetDTO> getTimeSheets(String from, String to);

	void updateTimeSheetAttachmentEntry(Long absEntry, Map<String, Object> values);

}

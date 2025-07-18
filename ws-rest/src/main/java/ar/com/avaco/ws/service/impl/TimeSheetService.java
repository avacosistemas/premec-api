package ar.com.avaco.ws.service.impl;

import java.util.List;

import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetAttachDTO;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetGetDTO;

public interface TimeSheetService {

	Long generarTimeSheet(String usuarioSap, String from, String to);

	TimeSheetEntryAttach getTimeSheetEntries(String usuarioSap, String from, String to);

	void updateTimeSheetAttachmentEntry(Long absEntry, Long newAttachmentEntry);

	List<ProjectManagementTimeSheetAttachDTO> listTimeSheetByUsuarioSap(String usuarioSAP);

	void updateTimeSheetLines(ProjectManagementTimeSheetGetDTO lineGroup);

	ProjectManagementTimeSheetGetDTO getTimeSheet(String usuarioSap, String fechaDesde, String fechaHasta);

}

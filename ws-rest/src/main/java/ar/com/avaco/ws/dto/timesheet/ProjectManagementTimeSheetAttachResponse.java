package ar.com.avaco.ws.dto.timesheet;

import java.util.List;

public class ProjectManagementTimeSheetAttachResponse {

	List<ProjectManagementTimeSheetAttachDTO> value;

	public List<ProjectManagementTimeSheetAttachDTO> getValue() {
		return value;
	}

	public void setValue(List<ProjectManagementTimeSheetAttachDTO> value) {
		this.value = value;
	}

}

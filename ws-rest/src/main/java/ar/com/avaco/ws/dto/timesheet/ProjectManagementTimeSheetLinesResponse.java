package ar.com.avaco.ws.dto.timesheet;

import java.util.List;

public class ProjectManagementTimeSheetLinesResponse {

	List<ProjectManagementTimeSheetGetDTO> value;

	public List<ProjectManagementTimeSheetGetDTO> getValue() {
		return value;
	}

	public void setValue(List<ProjectManagementTimeSheetGetDTO> value) {
		this.value = value;
	}

}

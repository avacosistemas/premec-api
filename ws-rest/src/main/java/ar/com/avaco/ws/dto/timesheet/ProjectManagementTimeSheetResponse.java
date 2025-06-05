package ar.com.avaco.ws.dto.timesheet;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ProjectManagementTimeSheetResponse {

	List<ProjectManagementTimeSheetDTO> value;

	public List<ProjectManagementTimeSheetDTO> getValue() {
		return value;
	}

	public void setValue(List<ProjectManagementTimeSheetDTO> value) {
		this.value = value;
	}

}

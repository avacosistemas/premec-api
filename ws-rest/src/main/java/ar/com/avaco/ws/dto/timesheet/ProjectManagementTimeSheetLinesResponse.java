package ar.com.avaco.ws.dto.timesheet;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectManagementTimeSheetLinesResponse {

	List<ProjectManagementTimeSheetGetDTO> value;

	@JsonProperty("@odata.nextLink")
	private String nextLink;

	public List<ProjectManagementTimeSheetGetDTO> getValue() {
		return value;
	}

	public void setValue(List<ProjectManagementTimeSheetGetDTO> value) {
		this.value = value;
	}

	public String getNextLink() {
		return nextLink;
	}

	public void setNextLink(String nextLink) {
		this.nextLink = nextLink;
	}

}

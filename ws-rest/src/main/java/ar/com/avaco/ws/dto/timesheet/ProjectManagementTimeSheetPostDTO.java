package ar.com.avaco.ws.dto.timesheet;

import java.util.HashMap;
import java.util.Map;

public class ProjectManagementTimeSheetPostDTO {

	private String userID;
	private String dateFrom;
	private String dateTo;

	public Map<String, Object> getAsMap() {

//    	"2025-04-01T00:00:00Z",

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("UserID", userID);
		map.put("DateFrom", dateFrom);
		map.put("DateTo", dateTo);
		return map;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

}

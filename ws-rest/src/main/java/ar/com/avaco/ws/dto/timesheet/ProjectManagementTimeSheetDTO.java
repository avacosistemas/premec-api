package ar.com.avaco.ws.dto.timesheet;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ar.com.avaco.ws.dto.attachment.Attachments2;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectManagementTimeSheetDTO {

	@JsonProperty(value = "AbsEntry")
	private Long absEntry;

	@JsonProperty(value = "UserID")
	private Long userID;

	@JsonProperty(value = "DateFrom")
	private Date dateFrom;

	@JsonProperty(value = "AttachmentEntry")
	private Long attachmentEntry;

	@JsonProperty(value = "Attachments2")
	private Attachments2 attachments2;

	public Long getAbsEntry() {
		return absEntry;
	}

	public void setAbsEntry(Long absEntry) {
		this.absEntry = absEntry;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Long getAttachmentEntry() {
		return attachmentEntry;
	}

	public void setAttachmentEntry(Long attachmentEntry) {
		this.attachmentEntry = attachmentEntry;
	}

	public Attachments2 getAttachments2() {
		return attachments2;
	}

	public void setAttachments2(Attachments2 attachments2) {
		this.attachments2 = attachments2;
	}

}

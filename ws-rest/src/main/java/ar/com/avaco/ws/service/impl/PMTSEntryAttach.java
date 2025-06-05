package ar.com.avaco.ws.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PMTSEntryAttach {

	@JsonProperty(value = "AbsEntry")
	private Long absEntry;

	@JsonProperty(value = "AttachmentEntry")
	private Long attachmentEntry;

	public Long getAbsEntry() {
		return absEntry;
	}

	public void setAbsEntry(Long absEntry) {
		this.absEntry = absEntry;
	}

	public Long getAttachmentEntry() {
		return attachmentEntry;
	}

	public void setAttachmentEntry(Long attachmentEntry) {
		this.attachmentEntry = attachmentEntry;
	}

}

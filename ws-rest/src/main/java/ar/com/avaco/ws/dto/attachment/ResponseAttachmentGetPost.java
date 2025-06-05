package ar.com.avaco.ws.dto.attachment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseAttachmentGetPost {

	@JsonProperty("AbsoluteEntry")
	private Long absoluteEntry;

	@JsonProperty("Attachments2_Lines")
	private List<AttachmentLine> attachments2Lines;

	public Long getAbsoluteEntry() {
		return absoluteEntry;
	}

	public void setAbsoluteEntry(Long absoluteEntry) {
		this.absoluteEntry = absoluteEntry;
	}

	public List<AttachmentLine> getAttachments2Lines() {
		return attachments2Lines;
	}

	public void setAttachments2Lines(List<AttachmentLine> attachments2Lines) {
		this.attachments2Lines = attachments2Lines;
	}

}

package ar.com.avaco.ws.dto.attachment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachments2 {

	@JsonProperty(value = "absoluteEntry")
	private Long absoluteEntry;

	@JsonProperty(value = "Attachments2_Lines")
	private List<AttachmentLine> lines;

	public Long getAbsoluteEntry() {
		return absoluteEntry;
	}

	public void setAbsoluteEntry(Long absoluteEntry) {
		this.absoluteEntry = absoluteEntry;
	}

	public List<AttachmentLine> getLines() {
		return lines;
	}

	public void setLines(List<AttachmentLine> lines) {
		this.lines = lines;
	}

}

package ar.com.avaco.ws.dto.attachment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentLine {

	@JsonProperty("AbsoluteEntry")
	private Long absoluteEntry;

	@JsonProperty("SourcePath")
	private String sourcePath;

	@JsonProperty("FileName")
	private String fileName;

	@JsonProperty("FileExtension")
	private String fileExtension;

	@JsonProperty("Override")
	private String override = "tYES";

	@JsonProperty("FreeText")
	private String freeText;

	@JsonProperty("TargetPath")
	private String targetPath;

	public Long getAbsoluteEntry() {
		return absoluteEntry;
	}

	public void setAbsoluteEntry(Long absoluteEntry) {
		this.absoluteEntry = absoluteEntry;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getOverride() {
		return override;
	}

	public void setOverride(String override) {
		this.override = override;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

}

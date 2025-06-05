package ar.com.avaco.ws.dto.timesheet;

public class RegistroReciboPorUsuarioDTO {

	private Long attachmentEntry;

	private Integer year;

	private Integer month;

	private String monthString;

	private String tipo;

	public Long getAttachmentEntry() {
		return attachmentEntry;
	}

	public void setAttachmentEntry(Long attachmentEntry) {
		this.attachmentEntry = attachmentEntry;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getMonthString() {
		return monthString;
	}

	public void setMonthString(String monthString) {
		this.monthString = monthString;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}

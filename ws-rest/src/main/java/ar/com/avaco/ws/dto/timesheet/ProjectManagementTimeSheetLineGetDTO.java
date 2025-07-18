package ar.com.avaco.ws.dto.timesheet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import ar.com.avaco.utils.DateUtils;

public class ProjectManagementTimeSheetLineGetDTO {

	@JsonProperty(value = "LineID")
	private Long lineId;

	@JsonProperty(value = "Date")
	private Date date;

	@JsonProperty(value = "StartTime")
	private String startTime;

	@JsonProperty(value = "EndTime")
	private String endTime;

	@JsonProperty(value = "BillableTime")
	private String billableTime;

	@JsonProperty(value = "NonBillableTime")
	private String nonBillableTime;

	@JsonProperty(value = "U_hsextras50")
	private String horasExtras50;

	@JsonProperty(value = "U_hsextras100")
	private String horasExtras100;

	@JsonProperty(value = "U_hsextrasferiados")
	private String horasExtrasFeriados;

	@JsonProperty(value = "U_comida")
	private String comida;

	@JsonProperty(value = "U_tipoausentismo")
	private String tipoAusentismo;

	@JsonProperty(value = "U_horasnormales")
	private String horasNormales;

	@JsonProperty(value = "U_tarde")
	private String tarde;

	public Map<String, Object> getAsMapNewLine() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LineID", this.lineId);
		map.put("Date", DateUtils.toString(this.date, "yyyyMMdd"));
		map.put("StartTime", this.startTime.trim());
		map.put("EndTime", this.endTime.trim());

		if (StringUtils.isNotEmpty(this.nonBillableTime))
			map.put("NonBillableTime", this.nonBillableTime.trim());

		if (StringUtils.isNotEmpty(this.horasExtras50))
			map.put("U_hsextras50", this.horasExtras50.trim());

		if (StringUtils.isNotEmpty(this.horasExtras100))
			map.put("U_hsextras100", this.horasExtras100.trim());

		if (StringUtils.isNotEmpty(this.horasExtrasFeriados))
			map.put("U_hsextrasferiados", this.horasExtrasFeriados.trim());

		if (StringUtils.isNotEmpty(this.comida))
			map.put("U_comida", this.comida.trim());
		
		map.put("U_tipoausentismo", this.tipoAusentismo.trim());

		if (StringUtils.isNotEmpty(this.horasNormales))
			map.put("U_horasnormales", this.horasNormales.trim());

		if (StringUtils.isNotEmpty(this.tarde))
			map.put("U_tarde", this.tarde.trim());

		return map;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBillableTime() {
		return billableTime;
	}

	public void setBillableTime(String billableTime) {
		this.billableTime = billableTime;
	}

	public String getHorasExtras50() {
		return horasExtras50;
	}

	public void setHorasExtras50(String horasExtras50) {
		this.horasExtras50 = horasExtras50;
	}

	public String getHorasExtras100() {
		return horasExtras100;
	}

	public void setHorasExtras100(String horasExtras100) {
		this.horasExtras100 = horasExtras100;
	}

	public String getHorasExtrasFeriados() {
		return horasExtrasFeriados;
	}

	public void setHorasExtrasFeriados(String horasExtrasFeriados) {
		this.horasExtrasFeriados = horasExtrasFeriados;
	}

	public String getTipoAusentismo() {
		return tipoAusentismo;
	}

	public void setTipoAusentismo(String tipoAusentismo) {
		this.tipoAusentismo = tipoAusentismo;
	}

	public String getHorasNormales() {
		return horasNormales;
	}

	public void setHorasNormales(String horasNormales) {
		this.horasNormales = horasNormales;
	}

	public String getTarde() {
		return tarde;
	}

	public void setTarde(String tarde) {
		this.tarde = tarde;
	}

	public String getComida() {
		return comida;
	}

	public void setComida(String comida) {
		this.comida = comida;
	}

	public String getNonBillableTime() {
		return nonBillableTime;
	}

	public void setNonBillableTime(String nonBillableTime) {
		this.nonBillableTime = nonBillableTime;
	}

}

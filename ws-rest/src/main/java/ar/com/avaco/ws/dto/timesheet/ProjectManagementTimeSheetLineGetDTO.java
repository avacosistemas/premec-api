package ar.com.avaco.ws.dto.timesheet;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectManagementTimeSheetLineGetDTO {

	@JsonProperty(value = "LineID")
	private Long lineId;

	@JsonProperty(value = "Date")
	private String date;

	@JsonProperty(value = "U_hsfichado")
	private String horaFichado;

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

	@JsonProperty(value = "U_hsextrasferiado")
	private String horasExtrasFeriados;

	@JsonProperty(value = "U_comidas")
	private String comida;

	@JsonProperty(value = "U_tipoausentismo")
	private String tipoAusentismo;

	@JsonProperty(value = "U_hsnormales")
	private String horasNormales;

	@JsonProperty(value = "U_tarde")
	private String tarde;

	@JsonProperty(value = "U_hsinjustificadas")
	private String horasInjustificadas;

	@JsonProperty(value = "U_hsferiado")
	private String horasFeriado;

	public Map<String, Object> getAsMapNewLine() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("LineID", this.lineId);
		map.put("Date", this.date);
		map.put("StartTime", this.startTime.trim());
		map.put("EndTime", this.endTime.trim());

		if (StringUtils.isNotEmpty(this.nonBillableTime))
			map.put("NonBillableTime", this.nonBillableTime.trim());

		if (StringUtils.isNotEmpty(this.horasExtras50))
			map.put("U_hsextras50", this.horasExtras50.trim());

		if (StringUtils.isNotEmpty(this.horasExtras100))
			map.put("U_hsextras100", this.horasExtras100.trim());

		if (StringUtils.isNotEmpty(this.horasExtrasFeriados))
			map.put("U_hsextrasferiado", this.horasExtrasFeriados.trim());

		if (StringUtils.isNotEmpty(this.comida))
			map.put("U_comidas", this.comida.trim());

		if (StringUtils.isNotEmpty(this.tipoAusentismo))
			map.put("U_tipoausentismo", this.tipoAusentismo.trim());

		if (StringUtils.isNotEmpty(this.horasNormales))
			map.put("U_hsnormales", this.horasNormales.trim());

		if (StringUtils.isNotEmpty(this.tarde))
			map.put("U_tarde", this.tarde.trim());

		if (StringUtils.isNotEmpty(this.horaFichado))
			map.put("U_hsfichado", this.horaFichado.trim());

		if (StringUtils.isNotEmpty(this.horasInjustificadas))
			map.put("U_hsinjustificadas", this.horasInjustificadas.trim());

		if (StringUtils.isNotEmpty(this.horasFeriado))
			map.put("U_hsferiado", this.horasFeriado.trim());

		return map;
	}

	public String getHorasFeriado() {
		return horasFeriado;
	}

	public void setHorasFeriado(String horasFeriado) {
		this.horasFeriado = horasFeriado;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHoraFichado() {
		return horaFichado;
	}

	public void setHoraFichado(String horaFichado) {
		this.horaFichado = horaFichado;
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

	public String getNonBillableTime() {
		return nonBillableTime;
	}

	public void setNonBillableTime(String nonBillableTime) {
		this.nonBillableTime = nonBillableTime;
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

	public String getComida() {
		return comida;
	}

	public void setComida(String comida) {
		this.comida = comida;
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

	public String getHorasInjustificadas() {
		return horasInjustificadas;
	}

	public void setHorasInjustificadas(String horasInjustificadas) {
		this.horasInjustificadas = horasInjustificadas;
	}

}

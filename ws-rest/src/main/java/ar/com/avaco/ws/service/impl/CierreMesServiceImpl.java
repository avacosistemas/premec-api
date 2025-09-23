package ar.com.avaco.ws.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.icu.util.Calendar;

import ar.com.avaco.utils.DateUtils;
import ar.com.avaco.ws.dto.actividad.RegistroPreviewEmpleadoMensualDTO;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetGetDTO;
import ar.com.avaco.ws.service.AbstractSapService;
import ar.com.avaco.ws.service.CierreMesService;

@Service("cierreMesService")
public class CierreMesServiceImpl extends AbstractSapService implements CierreMesService {

	@Value(value = "${exclusiones.actividades.calculo.horas.netas}")
	private String exclusionesActividadesCalculoHorasNetas;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private TimeSheetService timeSheetService;

	@Override
	public List<RegistroPreviewEmpleadoMensualDTO> getRegistrosCierre(String mes, String anio) {

		String fechaDesde = anio + StringUtils.leftPad(mes, 2, "0") + "01";

		Calendar fecha = Calendar.getInstance();
		fecha.setTime(DateUtils.toDate(fechaDesde, "yyyyMMdd"));
		fecha.add(Calendar.MONTH, 1);
		fecha.add(Calendar.DAY_OF_MONTH, -1);
		String fechaHasta = DateUtils.toString(fecha.getTime(), "yyyyMMdd");

		List<RegistroPreviewEmpleadoMensualDTO> resumenPreview = this.activityService
				.obtenerActividadesValoradas(fechaDesde, fechaHasta, exclusionesActividadesCalculoHorasNetas);

		return resumenPreview;
	}

	@Override
	public void cerrarMes(List<RegistroPreviewEmpleadoMensualDTO> cierre, String anio, String mes) {
		String fechaDesde = anio + StringUtils.leftPad(mes, 2, "0") + "01";

		Calendar fecha = Calendar.getInstance();
		fecha.setTime(DateUtils.toDate(fechaDesde, "yyyyMMdd"));
		fecha.add(Calendar.MONTH, 1);
		fecha.add(Calendar.DAY_OF_MONTH, -1);
		String fechaHasta = DateUtils.toString(fecha.getTime(), "yyyyMMdd");

		List<ProjectManagementTimeSheetGetDTO> timeSheets = timeSheetService.getTimeSheets(fechaDesde, fechaHasta);

		for (RegistroPreviewEmpleadoMensualDTO empleado : cierre) {
			Predicate<? super ProjectManagementTimeSheetGetDTO> filtro = x -> {
				String usuarioSap = empleado.getUsuarioSap().toString();
				return x.getUserId().equals(usuarioSap);
			};
			Optional<ProjectManagementTimeSheetGetDTO> resultado = timeSheets.stream().filter(filtro).findFirst();
			if (resultado.isPresent()) {
				ProjectManagementTimeSheetGetDTO cabecera = resultado.get();
				Map<String, Object> updateMap = empleado.generarMapUpdate();
				this.timeSheetService.updateTimeSheetAttachmentEntry(cabecera.getAbsEntry(), updateMap);
			}
			
		}
	}

}

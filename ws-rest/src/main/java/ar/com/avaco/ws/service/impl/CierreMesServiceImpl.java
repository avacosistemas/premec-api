package ar.com.avaco.ws.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ibm.icu.util.Calendar;

import ar.com.avaco.arc.sec.service.UsuarioService;
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

	private Logger logger = Logger.getLogger(CierreMesServiceImpl.class);

	@Autowired
	private UsuarioService usuarioService;

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
	public RegistroPreviewEmpleadoMensualDTO getRegistrosCierre(String mes, String anio, String usuario) {
		
		String usuarioSap = usuarioService.getUsuarioSAPByUsername(usuario);
		
		String fechaDesde = anio + StringUtils.leftPad(mes, 2, "0") + "01";
		
		Calendar fecha = Calendar.getInstance();
		fecha.setTime(DateUtils.toDate(fechaDesde, "yyyyMMdd"));
		fecha.add(Calendar.MONTH, 1);
		fecha.add(Calendar.DAY_OF_MONTH, -1);
		String fechaHasta = DateUtils.toString(fecha.getTime(), "yyyyMMdd");
		
		List<RegistroPreviewEmpleadoMensualDTO> resumenPreview = this.activityService
				.obtenerActividadesValoradas(fechaDesde, fechaHasta, exclusionesActividadesCalculoHorasNetas, usuarioSap, true);
		
		return resumenPreview.get(0);
	}

	@Override
	public void cerrarMes(List<RegistroPreviewEmpleadoMensualDTO> cierre, String anio, String mes) {

		logger.debug("Procesando periodo " + mes + "-" + anio + " con " + cierre.size() + " registros ");
		
		String fechaDesde = anio + StringUtils.leftPad(mes, 2, "0") + "01";

		Calendar fecha = Calendar.getInstance();
		fecha.setTime(DateUtils.toDate(fechaDesde, "yyyyMMdd"));
		fecha.add(Calendar.MONTH, 1);
		fecha.add(Calendar.DAY_OF_MONTH, -1);
		String fechaHasta = DateUtils.toString(fecha.getTime(), "yyyyMMdd");

		List<ProjectManagementTimeSheetGetDTO> timeSheets = timeSheetService.getTimeSheets(fechaDesde, fechaHasta);

		logger.debug("Se obtuvieron " + timeSheets.size() + " para procesar");
		logger.debug("Inicio iteracion de json");

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		for (RegistroPreviewEmpleadoMensualDTO empleado : cierre) {

			logger.debug("Procesando " + empleado.getNombre() + " con usuario sap " + empleado.getUsuarioSap());

			Predicate<? super ProjectManagementTimeSheetGetDTO> filtro = x -> {
				String usuarioSap = empleado.getUsuarioSap().toString();
				return x.getUserId().equals(usuarioSap);
			};
			Optional<ProjectManagementTimeSheetGetDTO> resultado = timeSheets.stream().filter(filtro).findFirst();

			if (resultado.isPresent()) {
				logger.debug("Se encontro correspondencia en timesheets de sap para " + empleado.getNombre()
						+ " con usuario sap " + empleado.getUsuarioSap());
				ProjectManagementTimeSheetGetDTO cabecera = resultado.get();
				Map<String, Object> updateMap = empleado.generarMapUpdate();
				
				
				String json = "No se pudo parsear";
				try {
					json = mapper.writeValueAsString(updateMap);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
				logger.debug("Se genera mapa de actualizacio para sap con los siguientes valores: " + json);
				this.timeSheetService.updateTimeSheetAttachmentEntry(cabecera.getAbsEntry(), updateMap);
				logger.debug("Procesado correctamente " + empleado.getNombre() + " con usuario sap " + empleado.getUsuarioSap());
			} else {
				logger.debug("NO Se encontro correspondencia en timesheets de sap para " + empleado.getNombre()
				+ " con usuario sap " + empleado.getUsuarioSap());
				logger.debug("NO Se encontro correspondencia en timesheets de sap para " + empleado.getNombre()
				+ " con usuario sap " + empleado.getUsuarioSap());
				logger.debug("NO Se encontro correspondencia en timesheets de sap para " + empleado.getNombre()
				+ " con usuario sap " + empleado.getUsuarioSap());
			}

		}
	}

	@Override
	public RegistroPreviewEmpleadoMensualDTO getRegistrosCierreSinAgrupar(String mes, String anio) {
		String fechaDesde = anio + StringUtils.leftPad(mes, 2, "0") + "01";

		Calendar fecha = Calendar.getInstance();
		fecha.setTime(DateUtils.toDate(fechaDesde, "yyyyMMdd"));
		fecha.add(Calendar.MONTH, 1);
		fecha.add(Calendar.DAY_OF_MONTH, -1);
		String fechaHasta = DateUtils.toString(fecha.getTime(), "yyyyMMdd");

		List<RegistroPreviewEmpleadoMensualDTO> resumenPreview = this.activityService
				.obtenerActividadesValoradasSinAgrupar(fechaDesde, fechaHasta, exclusionesActividadesCalculoHorasNetas);

		return resumenPreview.get(0);
	}

}

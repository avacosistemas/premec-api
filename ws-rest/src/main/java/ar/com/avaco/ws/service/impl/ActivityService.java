package ar.com.avaco.ws.service.impl;

import java.util.List;

import ar.com.avaco.ws.dto.actividad.RegistroPreviewEmpleadoMensualDTO;
import ar.com.avaco.ws.dto.employee.liquidacion.PlantillaData;
import ar.com.avaco.ws.dto.actividad.HorasPorEmpleadoDTO;

public interface ActivityService {

	List<HorasPorEmpleadoDTO> obtenerHorasAgrupadasPorFechaEmpleado(Long employeeId, String fechaDesde,
			String fechaHasta, String horaDesde, String horaHasta, String exclusionesActividadesCalculoHorasNetas);

	List<HorasPorEmpleadoDTO> obtenerHorasAgrupadasPorFechaEmpleado(List<Long> employeeIds, String fechaDesde,
			String fechaHasta, String exclusionesActividadesCalculoHorasNetas);

	List<RegistroPreviewEmpleadoMensualDTO> obtenerActividadesValoradas(String mes, String anio, String exclusiones);

	PlantillaData getPreviewNovedadesContador(String anio, String mes);

	List<RegistroPreviewEmpleadoMensualDTO> obtenerActividadesValoradas(String fechaDesde, String fechaHasta,
			String exclusiones, String usuarioSap, boolean agrupadas);

	List<RegistroPreviewEmpleadoMensualDTO> obtenerActividadesValoradasSinAgrupar(String fechaDesde, String fechaHasta,
			String exclusionesActividadesCalculoHorasNetas);

}

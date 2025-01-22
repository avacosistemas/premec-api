package ar.com.avaco.ws.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.task.TaskRejectedException;

public class Mock {

	public static List<ActividadTarjetaDTO> mockTarjetas() {
		List<ActividadTarjetaDTO> mocks = new ArrayList<ActividadTarjetaDTO>();
		
		ActividadTarjetaDTO actividad1 = new ActividadTarjetaDTO();
		actividad1.setActividadTaller(false);
		actividad1.setAsignadoPor("Walter Giardino");
		actividad1.setCliente("Pepe Argento");
		actividad1.setCodigoArticulo("JKQ104514");
		actividad1.setConCargo(false);
		actividad1.setDetalle("Detalle Detalle Detalle Detalle Detalle");
		actividad1.setDireccion("Mexico 2929");
		actividad1.setEmpleado("Roberto Gomez Bolaños");
		actividad1.setFecha("10/10/2025");
		actividad1.setHora("15:50");
		actividad1.setHorasMaquina(20);
		actividad1.setIdActividad(666L);
		actividad1.setLlamadaID("999");
		actividad1.setNroFabricante("FAB123");
		actividad1.setNroSerie("SER123");
		actividad1.setNumero("888");
		actividad1.setPrioridad("Alta");
		actividad1.setTareasARealizar("Cambiar aceite y ajustar tuerca del cerebro");
		actividad1.setTipoActividad("EE");
		
		for (int i = 1; i <= 3; i++) {
			GrupoDTO g1 = new GrupoDTO("Grupo A" + i);
			for (int j = 1; j <= 3; j++) {
				g1.agregarItem("Item " + j + " del grupo A" + i);
			}
			actividad1.agregarGrupo(g1);
		}
		
		mocks.add(actividad1);
		
		ActividadTarjetaDTO actividad2 = new ActividadTarjetaDTO();
		actividad2.setActividadTaller(false);
		actividad2.setAsignadoPor("Ramon Valdez");
		actividad2.setCliente("Bruce Wayne");
		actividad2.setCodigoArticulo("JKQ104514");
		actividad2.setConCargo(false);
		actividad2.setDetalle("Detalle Detalle Detalle Detalle Detalle");
		actividad2.setDireccion("Mexico 2929");
		actividad2.setEmpleado("Roberto Gomez Bolaños");
		actividad2.setFecha("10/10/2025");
		actividad2.setHora("15:50");
		actividad2.setHorasMaquina(20);
		actividad2.setIdActividad(666L);
		actividad2.setLlamadaID("999");
		actividad2.setNroFabricante("FAB123");
		actividad2.setNroSerie("SER123");
		actividad2.setNumero("888");
		actividad2.setPrioridad("Alta");
		actividad2.setTareasARealizar("Cambiar aceite y ajustar tuerca del cerebro");
		actividad2.setTipoActividad("EE");
		
		for (int i = 1; i <= 3; i++) {
			GrupoDTO g1 = new GrupoDTO("Grupo B" + i);
			for (int j = 1; j <= 3; j++) {
				g1.agregarItem("Item " + j + " del grupo B" + i);
			}
			actividad2.agregarGrupo(g1);
		}
		
		mocks.add(actividad2);
		
		return mocks;
	}
	
}

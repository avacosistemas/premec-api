package ar.com.avaco.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ar.com.avaco.ws.dto.ActividadTarjetaDTO;
import ar.com.avaco.ws.service.ActividadEPService;

@Service("actividadService")
public class ActividadEPServiceImpl implements ActividadEPService  {

	/*@Override
	@Resource(name = "actividadService")
	protected void setService(ActividadService service) {
		this.service = service;
	}*/

	@Override
	public List<ActividadTarjetaDTO> getActividades(String fecha,String usuarname) {
		
		ActividadTarjetaDTO actividad1 =new ActividadTarjetaDTO(); 
		ActividadTarjetaDTO actividad2 =new ActividadTarjetaDTO(); 
		List<ActividadTarjetaDTO> listaActividades = new ArrayList<ActividadTarjetaDTO>();
		//Test 
		actividad1.setIdActividad((long) 1);
		actividad1.setPrioridad("1");
		actividad1.setAsignadoPor("Pepe");
		actividad1.setEmpleado("Juan");
		actividad1.setNumero("112");
		actividad1.setLlamadaID("113");
		actividad1.setFecha(fecha);
		actividad1.setHora("15");
		actividad1.setCodigoArticulo("f4321");
		actividad1.setFechaInicio("2022");
		actividad1.setDetalle("No se");
		actividad1.setNroSerie("546");
		actividad1.setCliente("Jose");
		actividad1.setDireccion("San Martin");
		actividad1.setNroFabricante("872");
		actividad1.setHorasMaquina(2);
		actividad1.setTareasARealizar("Tarea 1");
		actividad1.setConCargo(true);
		
		actividad2.setIdActividad((long) 2);
		actividad2.setPrioridad("2");
		actividad2.setAsignadoPor("Carlos");
		actividad2.setEmpleado("Juan");
		actividad2.setNumero("164");
		actividad2.setLlamadaID("135");
		actividad2.setFecha(fecha);
		actividad2.setHora("13");
		actividad2.setCodigoArticulo("f1876");
		actividad2.setFechaInicio("2022");
		actividad2.setDetalle("No se Parte 2");
		actividad2.setNroSerie("5876");
		actividad2.setCliente("Jose");
		actividad2.setDireccion("San Martin");
		actividad2.setNroFabricante("2765");
		actividad2.setHorasMaquina(4);
		actividad2.setTareasARealizar("Tarea 3");
		actividad2.setConCargo(true);
		listaActividades.add(actividad1);
		listaActividades.add(actividad2);
		//List<ActividadTarjetaDTO> listaActividades = getListActividades();
		return listaActividades;
	}

}

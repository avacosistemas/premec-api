package ar.com.avaco.ws.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.dto.ItemCheckDTO;
import ar.com.avaco.ws.dto.RepuestoDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.reporte.InformeBuilder;
import ar.com.avaco.ws.rest.reporte.InformeBuilderChecklist;
import ar.com.avaco.ws.rest.reporte.InformeBuilderPiezasRepararMantenimientoMaquinaria;
import ar.com.avaco.ws.rest.reporte.InformeBuilderReparaciones;
import ar.com.avaco.ws.rest.security.service.ReporteService;
import ar.com.avaco.ws.service.ReporteEPService;

@Service("reporteEPService")
public class ReporteEPServiceImpl implements ReporteEPService {

	@Value("${informe.path}")
	private String informePath;

	private ReporteService reporteService;

	@Resource(name = "reporteService")
	public void setService(ReporteService reporteService) {
		this.reporteService = reporteService;
	}

	@Override
	public void generarReporteReparaciones(ActividadReporteDTO eldto) throws DocumentException, IOException {
		InformeBuilderReparaciones ib = new InformeBuilderReparaciones(eldto, informePath);
		ib.generarReporte();
	}

	@Override
	public void generarReporteChecklist(ActividadReporteDTO eldto) throws DocumentException, IOException {
		InformeBuilderChecklist ib = new InformeBuilderChecklist(eldto, informePath);
		ib.generarReporte();
	}

	@Override
	public void generarReporteMantenimientoPiezas(ActividadReporteDTO eldto) throws DocumentException, IOException {
		InformeBuilderPiezasRepararMantenimientoMaquinaria ib = new InformeBuilderPiezasRepararMantenimientoMaquinaria(
				eldto, informePath);
		ib.generarReporte();
	}

	@Override
	public void enviarReporte(ActividadReporteDTO eldto) throws MalformedURLException, DocumentException, IOException {
		generarReporte(eldto);
		reporteService.sendMail(eldto.getEmail(), eldto.getIdActividad().toString());
	}

	@Override
	public ResponseEntity<JSONResponse> generarReporte(ActividadReporteDTO eldto)
			throws FileNotFoundException, DocumentException, IOException {
		InformeBuilder ib = null;

		switch (eldto.getTipoActividad()) {
		case "R":
			ib = new InformeBuilderReparaciones(eldto, informePath);
			break;
		case "C":
			ib = new InformeBuilderChecklist(eldto, informePath);
			break;
		case "P":
			ib = new InformeBuilderPiezasRepararMantenimientoMaquinaria(eldto, informePath);
			break;
		case "M":
			ib = new InformeBuilderPiezasRepararMantenimientoMaquinaria(eldto, informePath);
			break;
		default:
			break;
		}

		return ib.generarReporte();
	}

	@Override
	public void generarReporteTestReparaciones() {
		ActividadReporteDTO dto = new ActividadReporteDTO();
		dto.setAsignadoPor("Asignado por");
		dto.setCliente("Cliente");
		dto.setCodigoArticulo("CodigoArt");
		dto.setConCargo(true);
		dto.setDetalle(
				"Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle  Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle");
		dto.setDireccion("Direccion Direccion Direccion Direccion Direccion ");
		dto.setEmail("email@gmail.com");
		dto.setEmpleado("Empleado");
		dto.setFecha("10/10/2023");
		dto.setFechaFinoOperario("11/10/2023");
		dto.setFechaInicioOperario("12/10/2023");
		dto.setHora("10:30");
		dto.setHoraFinOperario("11:55");
		dto.setHoraInicioOperario("15:20");
		dto.setHorasMaquina(20);
		dto.setIdActividad(666L);
		dto.setLlamadaID("LlamadaId666");
		dto.setNroFabricante("nrofabricante");
		dto.setNroSerie("nroserie");
		dto.setNumero("numero123");
		dto.setObservacionesGenerales(
				"Observaciones Generales Observaciones Generales Observaciones Generales Observaciones Generales Observaciones Generales Observaciones Generales ");
		dto.setPrioridad("prioridad");
		dto.setTareasARealizar(
				"Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizarTareas a realizarTareas a realizarTareas a realizarTareas a realizarTareas a realizar ");
		dto.setValoracionComentarios("comentarios comentarios comentarios comentarios ");
		dto.setValoracionDNISuperior("30218977");
		dto.setValoracionNombreSuperior("NombreSuperior");
		dto.setValoracionResultado("Bien");

		List<RepuestoDTO> repuestos = new ArrayList<RepuestoDTO>();
		repuestos.add(new RepuestoDTO("art1", "desc1", 1D, "nroserie1"));
		repuestos.add(new RepuestoDTO("art2", "desc2", 2D, "nroserie2"));
		repuestos.add(new RepuestoDTO("art3", "desc3", 3D, "nroserie3"));
		repuestos.add(new RepuestoDTO("art4", "desc4", 4D, "nroserie4"));
		dto.setRepuestos(repuestos);

		Map<String, List<ItemCheckDTO>> checks = new HashMap<String, List<ItemCheckDTO>>();
		List<ItemCheckDTO> hidra = new ArrayList<>();
		String sistemaHidraulico = "SISTEMA HIDRAULICO";
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Nivel de Aceite", "OK", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Bomba principal", "OK", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Mangueras en gral", "No OK", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Bomba principal", "OK", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Mangueras en gral", "No OK", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Bomba principal", "OK", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Mangueras en gral", "No OK", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Cilindros en gral", "No Ok", "Obs"));
		checks.put(sistemaHidraulico, hidra);

		List<ItemCheckDTO> elect = new ArrayList<>();
		String sistemaElectrico = "SISTEMA ELECTRICO";
		elect.add(new ItemCheckDTO(sistemaElectrico, "Módulos electrónicos", "OK", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Contactores, switch, sensores", "OK", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Tableros y comandos", "No OK", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Tableros y comandos", "No OK", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Contactores, switch, sensores", "OK", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Tableros y comandos", "No OK", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Cableado de mando y potencia", "No Ok", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Tableros y comandos", "No OK", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Cableado de mando y potencia", "No Ok", "Obs"));
		checks.put(sistemaElectrico, elect);

		List<ItemCheckDTO> elect4 = new ArrayList<>();
		String sistemaElectrico4 = "SISTEMA ELECTRICO 666";
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Módulos electrónicos", "OK", "Obs"));
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Contactores, switch, sensores", "OK", "Obs"));
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Tableros y comandos", "No OK", "Obs"));
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Tableros y comandos", "No OK", "Obs"));
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Contactores, switch, sensores", "OK", "Obs"));
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Tableros y comandos", "No OK", "Obs"));
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Cableado de mando y potencia", "No Ok", "Obs"));
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Tableros y comandos", "No OK", "Obs"));
		elect4.add(new ItemCheckDTO(sistemaElectrico4, "Cableado de mando y potencia", "No Ok", "Obs"));
		checks.put(sistemaElectrico4, elect4);

		List<ItemCheckDTO> elect2 = new ArrayList<>();
		String sistemaElectrico2 = "SISTEMA ELECTRICO 2";
		elect2.add(new ItemCheckDTO(sistemaElectrico2, "Módulos electrónicos", "OK", "Obs"));
		elect2.add(new ItemCheckDTO(sistemaElectrico2, "Contactores, switch, sensores", "OK", "Obs"));
		elect2.add(new ItemCheckDTO(sistemaElectrico2, "Contactores, switch, sensores", "OK", "Obs"));
		elect2.add(new ItemCheckDTO(sistemaElectrico2, "Tableros y comandos", "No OK", "Obs"));
		elect2.add(new ItemCheckDTO(sistemaElectrico2, "Cableado de mando y potencia", "No Ok", "Obs"));
		elect2.add(new ItemCheckDTO(sistemaElectrico2, "Tableros y comandos", "No OK", "Obs"));
		checks.put(sistemaElectrico2, elect2);

		List<ItemCheckDTO> elect3 = new ArrayList<>();
		String sistemaElectrico3 = "SISTEMA ELECTRICO 3";
		elect3.add(new ItemCheckDTO(sistemaElectrico3, "Módulos electrónicos", "OK", "Obs"));
		elect3.add(new ItemCheckDTO(sistemaElectrico3, "Contactores, switch, sensores", "OK", "Obs"));
		elect3.add(new ItemCheckDTO(sistemaElectrico3, "Contactores, switch, sensores", "OK", "Obs"));
		elect3.add(new ItemCheckDTO(sistemaElectrico3, "Tableros y comandos", "No OK", "Obs"));
		elect3.add(new ItemCheckDTO(sistemaElectrico3, "Cableado de mando y potencia", "No Ok", "Obs"));
		elect3.add(new ItemCheckDTO(sistemaElectrico3, "Tableros y comandos", "No OK", "Obs"));
		elect3.add(new ItemCheckDTO(sistemaElectrico3, "Cableado de mando y potencia", "No Ok", "Obs"));
		elect3.add(new ItemCheckDTO(sistemaElectrico3, "Módulos electrónicos", "OK", "Obs"));
		checks.put(sistemaElectrico3, elect3);

		dto.setChecks(checks);

		try {
			generarReporteReparaciones(dto);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void generarReporteTestChecklist() {
		ActividadReporteDTO dto = new ActividadReporteDTO();
		dto.setAsignadoPor("Asignado por");
		dto.setCliente("Cliente");
		dto.setCodigoArticulo("CodigoArt");
		dto.setConCargo(true);
		dto.setDetalle(
				"Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle  Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle");
		dto.setDireccion("Direccion Direccion Direccion Direccion Direccion ");
		dto.setEmail("email@gmail.com");
		dto.setEmpleado("Empleado");
		dto.setFecha("10/10/2023");
		dto.setFechaFinoOperario("11/10/2023");
		dto.setFechaInicioOperario("12/10/2023");
		dto.setHora("10:30");
		dto.setHoraFinOperario("11:55");
		dto.setHoraInicioOperario("15:20");
		dto.setHorasMaquina(20);
		dto.setIdActividad(666L);
		dto.setLlamadaID("LlamadaId666");
		dto.setNroFabricante("nrofabricante");
		dto.setNroSerie("nroserie");
		dto.setNumero("numero123");
		dto.setObservacionesGenerales(
				"Observaciones Generales Observaciones Generales Observaciones Generales Observaciones Generales Observaciones Generales Observaciones Generales ");
		dto.setPrioridad("prioridad");
		dto.setTareasARealizar(
				"Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizarTareas a realizarTareas a realizarTareas a realizarTareas a realizarTareas a realizar ");
		dto.setValoracionComentarios("comentarios comentarios comentarios comentarios ");
		dto.setValoracionDNISuperior("30218977");
		dto.setValoracionNombreSuperior("NombreSuperior");
		dto.setValoracionResultado("Bien");

		List<RepuestoDTO> repuestos = new ArrayList<RepuestoDTO>();
		repuestos.add(new RepuestoDTO("art1", "desc1", 1D, "nroserie1"));
		repuestos.add(new RepuestoDTO("art2", "desc2", 2D, "nroserie2"));
		repuestos.add(new RepuestoDTO("art3", "desc3", 3D, "nroserie3"));
		repuestos.add(new RepuestoDTO("art4", "desc4", 4D, "nroserie4"));
		dto.setRepuestos(repuestos);

		Map<String, List<ItemCheckDTO>> checks = new HashMap<String, List<ItemCheckDTO>>();
		List<ItemCheckDTO> hidra = new ArrayList<>();
		String sistemaHidraulico = "TRACCIÓN";
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Marcha Adelante", "Ok", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Marcha Atrás", "Ok", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Velocidad Regulada", "No Ok", "Obs"));
		hidra.add(new ItemCheckDTO(sistemaHidraulico, "Velocidades", "NA", "Obs"));
		checks.put(sistemaHidraulico, hidra);

		List<ItemCheckDTO> elect = new ArrayList<>();
		String sistemaElectrico = "ELEVACION";
		elect.add(new ItemCheckDTO(sistemaElectrico, "Subida", "Ok", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Bajada", "Ok", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Corte de elevacion max .alt", "No Ok", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Indicador de Altura", "NA", "Obs"));
		elect.add(new ItemCheckDTO(sistemaElectrico, "Sensor de nivel", "No Ok", "Obs"));
		checks.put(sistemaElectrico, elect);

		dto.setChecks(checks);

		try {
			generarReporteChecklist(dto);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generarReporteTestMantenimiento() {
		ActividadReporteDTO dto = new ActividadReporteDTO();
		dto.setAsignadoPor("Asignado por");
		dto.setTipoActividad("M");
		dto.setCliente("Cliente");
		dto.setCodigoArticulo("CodigoArt");
		dto.setConCargo(true);
		dto.setDetalle(
				"Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle  Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle");
		dto.setDireccion("Direccion Direccion Direccion Direccion Direccion ");
		dto.setEmail("email@gmail.com");
		dto.setEmpleado("Empleado");
		dto.setFecha("10/10/2023");
		dto.setFechaFinoOperario("11/10/2023");
		dto.setFechaInicioOperario("12/10/2023");
		dto.setHora("10:30");
		dto.setHoraFinOperario("11:55");
		dto.setHoraInicioOperario("15:20");
		dto.setHorasMaquina(20);
		dto.setIdActividad(666L);
		dto.setLlamadaID("LlamadaId666");
		dto.setNroFabricante("nrofabricante");
		dto.setNroSerie("nroserie");
		dto.setNumero("numero123");
		dto.setObservacionesGenerales(
				"Mantenimientos que se hicieron en maquinarias verdes a e i Mantenimientos que se hicieron a e en maquinarias Mantenimientos ojota que se hicieron en maquinarias Mantenimientos que se hicieron en maquinarias Mantenimientos que se hicieron en maquinarias Mantenimientos que se hicieron en maquinarias ");
		dto.setPrioridad("prioridad");
		dto.setTareasARealizar(
				"Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizarTareas a realizarTareas a realizarTareas a realizarTareas a realizarTareas a realizar ");
		dto.setValoracionComentarios("comentarios comentarios comentarios comentarios ");
		dto.setValoracionDNISuperior("30218977");
		dto.setValoracionNombreSuperior("NombreSuperior");
		dto.setValoracionResultado("Bien");

		try {
			generarReporteMantenimientoPiezas(dto);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generarReporteTestPiezas() {
		ActividadReporteDTO dto = new ActividadReporteDTO();
		dto.setAsignadoPor("Asignado por");
		dto.setTipoActividad("P");
		dto.setCliente("Cliente");
		dto.setCodigoArticulo("CodigoArt");
		dto.setConCargo(true);
		dto.setDetalle(
				"Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle  Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle Detalle");
		dto.setDireccion("Direccion Direccion Direccion Direccion Direccion ");
		dto.setEmail("email@gmail.com");
		dto.setEmpleado("Empleado");
		dto.setFecha("10/10/2023");
		dto.setFechaFinoOperario("11/10/2023");
		dto.setFechaInicioOperario("12/10/2023");
		dto.setHora("10:30");
		dto.setHoraFinOperario("11:55");
		dto.setHoraInicioOperario("15:20");
		dto.setHorasMaquina(20);
		dto.setIdActividad(666L);
		dto.setLlamadaID("LlamadaId666");
		dto.setNroFabricante("nrofabricante");
		dto.setNroSerie("nroserie");
		dto.setNumero("numero123");
		dto.setObservacionesGenerales(
				"Piezas a reparar Piezas a reparar Piezas a reparar Piezas a reparar Piezas a reparar Piezas a reparar Piezas a reparar ");
		dto.setPrioridad("prioridad");
		dto.setTareasARealizar(
				"Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizar Tareas a realizarTareas a realizarTareas a realizarTareas a realizarTareas a realizarTareas a realizar ");
		dto.setValoracionComentarios("comentarios comentarios comentarios comentarios ");
		dto.setValoracionDNISuperior("30218977");
		dto.setValoracionNombreSuperior("NombreSuperior");
		dto.setValoracionResultado("Bien");

		try {
			generarReporteMantenimientoPiezas(dto);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

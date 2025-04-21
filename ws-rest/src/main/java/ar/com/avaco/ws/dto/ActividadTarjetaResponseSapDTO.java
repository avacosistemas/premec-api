package ar.com.avaco.ws.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ar.com.avaco.commons.domain.TipoActividad;
import ar.com.avaco.ws.rest.security.dto.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActividadTarjetaResponseSapDTO {

	@JsonProperty("Activities")
	private ActivitiesResponseSapDTO actividad;

	@JsonProperty("EmployeesInfo")
	private EmployeesInfoReponseSapDTO empleado;

	@JsonProperty("ServiceCalls")
	private ServiceCallsResponseSapDTO serviceCall;

	@JsonProperty("Users")
	private UsersResponseSapDTO usuario;

	@JsonProperty("ActivityLocations")
	private LocationResponseSapDTO location;
	
	@JsonProperty("ServiceCalls/ServiceCallActivities")
	private ServiceCallActivitiesResponseSapDTO serviceCallActivity;

	public ActividadTarjetaDTO getActividadTarjetaDTO() {
		return getActividadTarjetaDTO(new ArrayList<>());		
	}
	
	public ActividadTarjetaDTO getActividadTarjetaDTO(List<GrupoDTO> grupos) {
		ActividadTarjetaDTO dto = new ActividadTarjetaDTO();
		dto.setActividadTaller(actividad.getEsTaller().equals("Y"));
		dto.setAsignadoPor(usuario.getUsername());
		dto.setCliente(serviceCall.getCustomerName());
		dto.setCodigoArticulo(serviceCall.getItemCode());
		dto.setConCargo(actividad.getConCargo().equals("Y"));
		dto.setDetalle(serviceCall.getSubject());
		dto.setDireccion(location.getName());
		dto.setEmpleado(empleado.getFirstName() + " " + empleado.getLastName());
		dto.setFecha(actividad.getStartDate());
		dto.setGrupos(grupos);
		dto.setHora(actividad.getActivityTime());
		dto.setHorasMaquina(serviceCallActivity.getHorasMaquina() == null ? 0 : new Integer(serviceCallActivity.getHorasMaquina()));
		dto.setIdActividad(actividad.getActivityCode());
		dto.setLlamadaID(serviceCall.getServiceCallID().toString());
		dto.setNroFabricante(serviceCall.getManufacturerSerialNum() != null ? serviceCall.getManufacturerSerialNum() : "");
		dto.setNroSerie(serviceCall.getInternalSerialNum() != null ? serviceCall.getInternalSerialNum() : "");
		dto.setNumero(actividad.getActivityCode().toString());
		dto.setPrioridad(actividad.getPriority());
		dto.setTareasARealizar(actividad.getDetails());
		dto.setTipoActividad(actividad.getTipoTarea());

		TipoActividad ta = TipoActividad.valueOf(dto.getTipoActividad());
		
		switch (ta) {
		case EC:
			dto.setTipoMaquina("Combustión");
			break;
		case EP:
			dto.setTipoMaquina("Plataforma");
			break;
		case EE:
			dto.setTipoMaquina("Eléctrica");
			break;
		default:
			break;
		}

		return dto;
		
	}
	
	public ActivitiesResponseSapDTO getActividad() {
		return actividad;
	}

	public void setActividad(ActivitiesResponseSapDTO actividad) {
		this.actividad = actividad;
	}

	public EmployeesInfoReponseSapDTO getEmpleado() {
		return empleado;
	}

	public void setEmpleado(EmployeesInfoReponseSapDTO empleado) {
		this.empleado = empleado;
	}

	public ServiceCallsResponseSapDTO getServiceCall() {
		return serviceCall;
	}

	public void setServiceCall(ServiceCallsResponseSapDTO serviceCall) {
		this.serviceCall = serviceCall;
	}

	public UsersResponseSapDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsersResponseSapDTO usuario) {
		this.usuario = usuario;
	}

	public ServiceCallActivitiesResponseSapDTO getServiceCallActivity() {
		return serviceCallActivity;
	}

	public void setServiceCallActivity(ServiceCallActivitiesResponseSapDTO serviceCallActivity) {
		this.serviceCallActivity = serviceCallActivity;
	}

}

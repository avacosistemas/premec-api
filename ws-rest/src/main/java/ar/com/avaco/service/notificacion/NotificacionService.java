package ar.com.avaco.service.notificacion;

import ar.com.avaco.entities.cliente.Cliente;

public interface NotificacionService {

	void notificarResetoPassword(Cliente cliente, String tmppass);

	void notificarRegistroClienteNuevoPassword(Cliente cliente, String tmpass);

	void notificarHabilitacionExitosa(Cliente cliente);
	
	void notificarInicioServer();

}

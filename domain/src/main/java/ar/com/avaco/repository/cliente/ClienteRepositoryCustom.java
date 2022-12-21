package ar.com.avaco.repository.cliente;

import java.util.List;

import ar.com.avaco.entities.cliente.Cliente;


public interface ClienteRepositoryCustom {

	Cliente getCliente(Long id);
	
	List<Cliente> listClientes();
	
}

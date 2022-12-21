package ar.com.avaco.service.cliente;

import java.util.List;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.entities.cliente.Cliente;
import ar.com.avaco.entities.cliente.Contacto;


public interface ClienteService extends NJService<Long, Cliente> {

	Cliente getCliente(Long id);
	
	List<Contacto> getContactosByCliente(Long idCliente);

	Contacto getContacto(Long idContactoCliente);

	Contacto addContactoCliente(Long idCliente, Contacto contactoToAdd) throws ErrorValidationException, BusinessException;

	Contacto updateContactoCliente(Contacto contactoToUpdate) throws ErrorValidationException, BusinessException;

	Cliente addCliente(Cliente clienteToAdd) throws ErrorValidationException, BusinessException;

	Cliente updateCliente(Cliente clienteToUpdate) throws ErrorValidationException, BusinessException;

	

	/*Cliente getClientePorUsername(String username);

	Cliente registrarClientePersona(Cliente cliente) throws ErrorValidationException, BusinessException;

	void validaContactoCliente(Contacto contacto) throws ErrorValidationException;

	Cliente getClienteCompleto(Long id);

	List<Cliente> listClientesListado();

	void resetPassword(Long id);
	
	void updatePassword(Cliente user, String password,String newPassword);

	Cliente getClientePorMail(String username);*/

}

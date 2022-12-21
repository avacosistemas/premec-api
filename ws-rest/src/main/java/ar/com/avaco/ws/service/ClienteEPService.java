package ar.com.avaco.ws.service;

import java.util.List;

import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.ws.dto.ClienteDTO;
import ar.com.avaco.ws.dto.ContactoDTO;
import ar.com.avaco.ws.rest.service.CRUDEPService;

public interface ClienteEPService extends CRUDEPService<Long, ClienteDTO> {

	ClienteDTO getCliente(Long idCliente);
	
	List<ContactoDTO> getContactosByCliente(Long idCliente);

	ContactoDTO getContacto(Long idContactoCliente);
	
	ClienteDTO addCliente(ClienteDTO clienteDTO) throws ErrorValidationException, BusinessException;
	
	ClienteDTO updateCliente(ClienteDTO clienteDTO) throws ErrorValidationException, BusinessException;

	ContactoDTO addContactoCliente(Long idCliente, ContactoDTO contactoDTO) throws ErrorValidationException, BusinessException;

	ContactoDTO updateContactoCliente(ContactoDTO contactoDTO) throws ErrorValidationException, BusinessException;

}

package ar.com.avaco.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.entities.cliente.Cliente;
import ar.com.avaco.entities.cliente.Contacto;
import ar.com.avaco.entities.cliente.Provincia;
import ar.com.avaco.service.cliente.ClienteService;
import ar.com.avaco.ws.dto.ClienteDTO;
import ar.com.avaco.ws.dto.ContactoDTO;
import ar.com.avaco.ws.rest.service.CRUDEPBaseService;
import ar.com.avaco.ws.service.ClienteEPService;

@Service("clienteEPService")
public class ClienteEPServiceImpl extends CRUDEPBaseService<Long, ClienteDTO, Cliente, ClienteService>
		implements ClienteEPService {
	
	
	@Override
	public ClienteDTO getCliente(Long idCliente) {
		Cliente cliente = service.getCliente(idCliente);
		return convertToDto(cliente);
	}
	
	@Override
	public List<ClienteDTO> listFilter(AbstractFilter abstractFilter) {
		List<Cliente> entities = service.listFilter(abstractFilter);
		return convertToDtos(entities);
	}
	
	@Override
	public List<ClienteDTO> list() {
		return convertToDtos(this.service.list());
	}
	
	@Override
	public ClienteDTO addCliente(ClienteDTO clienteDTO) throws ErrorValidationException, BusinessException {
		Cliente cliente = convertToEntity(clienteDTO);
		Cliente saved = service.addCliente(cliente);
		return convertToDto(saved);
	}
	
	@Override
	public ClienteDTO updateCliente(ClienteDTO clienteDTO) throws ErrorValidationException, BusinessException {
		Cliente cliente = convertToEntity(clienteDTO);
		Cliente saved = service.updateCliente(cliente);
		return convertToDto(saved);
	}
	
	@Override
	public List<ContactoDTO> getContactosByCliente(Long idCliente) {			
		List<Contacto> listado = service.getContactosByCliente(idCliente);
		if (listado != null) {
			List<ContactoDTO> returnedListado = new ArrayList<ContactoDTO>();	
			for (Contacto contacto: listado) {
				ContactoDTO dto = new ContactoDTO();
				
				dto.setId(contacto.getId());
				dto.setIdCliente(contacto.getCliente().getId());
				dto.setTipo(contacto.getTipo());
				dto.setNombre(contacto.getNombre());
				dto.setEmail(contacto.getEmail());
				dto.setTelefono(contacto.getTelefono());
			
				returnedListado.add(dto);								
			}
			return returnedListado;
		}		
		return null;
	}
	
	@Override
	public ContactoDTO getContacto(Long idContactoCliente) {			
		Contacto contacto = service.getContacto(idContactoCliente);
		return convertToContactoDto(contacto);
	}
	
	@Override
	public ContactoDTO addContactoCliente(Long idCliente, ContactoDTO contactoDTO) throws ErrorValidationException, BusinessException {
		Contacto contacto = convertToContactoEntity(contactoDTO);
		Contacto saved = service.addContactoCliente(idCliente, contacto);
		return convertToContactoDto(saved);
	}
	
	@Override
	public ContactoDTO updateContactoCliente(ContactoDTO contactoDTO) throws ErrorValidationException, BusinessException {
		Contacto contacto = convertToContactoEntity(contactoDTO);
		Contacto saved = service.updateContactoCliente(contacto);
		return convertToContactoDto(saved);
	}
	
	@Override
	protected ClienteDTO convertToDto(Cliente entity) {
		ClienteDTO clienteDTO = new ClienteDTO();
		if (entity != null) {
			clienteDTO.setId(entity.getId());
			clienteDTO.setRazonSocial(entity.getRazonSocial());
			clienteDTO.setDomicilio(entity.getDomicilio());
			clienteDTO.setCodigoPostal(entity.getCodigoPostal());
			clienteDTO.setLocalidad(entity.getLocalidad());
			clienteDTO.setProvincia(entity.getProvincia().name());
			clienteDTO.setTelefonoFijo(entity.getTelefonoFijo());
			clienteDTO.setTelefonoCelular(entity.getTelefonoCelular());
			clienteDTO.setEmail(entity.getEmail());
			clienteDTO.setWebSite(entity.getWebSite());
			clienteDTO.setCuit(entity.getCuit());
			clienteDTO.setIngresosBrutos(entity.getIngresosBrutos());	
		}
		
		return clienteDTO;
	}
	
	private ContactoDTO convertToContactoDto(Contacto entity) {
		ContactoDTO contactoDTO = new ContactoDTO();
		if (entity != null) {
			contactoDTO.setId(entity.getId());
			contactoDTO.setIdCliente(entity.getCliente().getId());
			contactoDTO.setTipo(entity.getTipo());
			contactoDTO.setNombre(entity.getNombre());
			contactoDTO.setEmail(entity.getEmail());
			contactoDTO.setTelefono(entity.getTelefono());
		}
		
		return contactoDTO;
	}

	@Override
	protected Cliente convertToEntity(ClienteDTO dto) {
		if (dto == null)
			return null;
		
		Cliente cliente = new Cliente();
		cliente.setId(dto.getId());
		cliente.setRazonSocial(dto.getRazonSocial());
		cliente.setDomicilio(dto.getDomicilio());
		cliente.setCodigoPostal(dto.getCodigoPostal());
		cliente.setLocalidad(dto.getLocalidad());
		cliente.setProvincia(Provincia.valueOf(dto.getProvincia()));
		cliente.setTelefonoFijo(dto.getTelefonoFijo());
		cliente.setTelefonoCelular(dto.getTelefonoCelular());
		cliente.setEmail(dto.getEmail());
		cliente.setWebSite(dto.getWebSite());
		cliente.setCUIT(dto.getCuit());
		cliente.setIngresosBrutos(dto.getIngresosBrutos());	

		return cliente;
	}
	
	private Contacto convertToContactoEntity(ContactoDTO dto) {
		if (dto == null)
			return null;
		
		Contacto contacto = new Contacto();
		contacto.setId(dto.getId());
		
		contacto.setTipo(dto.getTipo());
		contacto.setNombre(dto.getNombre());
		contacto.setEmail(dto.getEmail());
		contacto.setTelefono(dto.getTelefono());

		return contacto;
	}

	@Override
	@Resource(name = "clienteService")
	protected void setService(ClienteService service) {
		this.service = service;
	}

}

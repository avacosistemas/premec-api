package ar.com.avaco.service.cliente;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.arc.core.component.bean.service.NJBaseService;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.entities.cliente.Cliente;
import ar.com.avaco.entities.cliente.Contacto;
import ar.com.avaco.repository.cliente.ClienteRepository;
import ar.com.avaco.repository.cliente.ContactoRepository;


@Transactional
@Service("clienteService")
public class ClienteServiceImpl extends NJBaseService<Long, Cliente, ClienteRepository>
		implements ClienteService {


	private Logger logger = Logger.getLogger(getClass());
	
	private ContactoRepository contactoRepository;
	
	
	@Override
	public Cliente getCliente(Long id) {
		return this.repository.findOne(id);
	}
	
	@Override
	public List<Contacto> getContactosByCliente(Long idCliente) {
		return this.contactoRepository.findAllByClienteId(idCliente);
	}
	
	@Override
	public Contacto getContacto(Long idContactoCliente) {
		return this.contactoRepository.findOne(idContactoCliente);
	}
	
	@Override
	public Cliente addCliente(Cliente clienteToAdd) throws ErrorValidationException, BusinessException {
		validarCliente(clienteToAdd);
		clienteToAdd = this.repository.save(clienteToAdd);

		return clienteToAdd;
	}
	
	@Override
	public Cliente updateCliente(Cliente clienteToUpdate) throws ErrorValidationException, BusinessException {
		validarCliente(clienteToUpdate);
		clienteToUpdate = this.repository.save(clienteToUpdate);
		return clienteToUpdate;
	}
	
	@Override
	public Contacto addContactoCliente(Long idCliente, Contacto contactoToAdd) throws ErrorValidationException, BusinessException {
		
		Cliente cliente = repository.findOne(idCliente);

		if (cliente == null)
			throw new EntityNotFoundException("No se encontro el cliente con ID: " + idCliente);

		contactoToAdd.setCliente(cliente);
		validarContactoCliente(contactoToAdd, cliente.getContactos());

		contactoToAdd = this.contactoRepository.save(contactoToAdd);

		return contactoToAdd;
	}
	
	@Override
	public Contacto updateContactoCliente(Contacto contacto) throws ErrorValidationException, BusinessException {
		
		Contacto contactoToUpdate = contactoRepository.findOne(contacto.getId());
		
		validarContactoCliente(contactoToUpdate, contactoToUpdate.getCliente().getContactos());
		
		contactoToUpdate.setNombre(contacto.getNombre());
		contactoToUpdate.setEmail(contacto.getEmail());
		contactoToUpdate.setTelefono(contacto.getTelefono());
		contactoToUpdate.setTipo(contacto.getTipo());
		
		contactoToUpdate = this.contactoRepository.save(contactoToUpdate);
		return contactoToUpdate;
	}
	
	//Valida los campos para el cliente
	private void validarCliente(Cliente cliente) throws ErrorValidationException, BusinessException {
		Map<String, String> errores = new HashMap<>();
		
		if (cliente == null) {
			throw new BusinessException("Cliente vacío.");
		}
		
		if (StringUtils.isBlank(cliente.getRazonSocial())) {
			errores.put("razonSocial", "El campo Razon Social es requerido.");
		} else {
			
			Cliente cliByRazonSocial = getRepository().findByRazonSocialEqualsIgnoreCase(cliente.getRazonSocial());
			
			if(cliByRazonSocial!=null && cliente.getId()==null || 
					cliByRazonSocial!=null && cliente.getId()!=null && !cliente.getId().equals(cliByRazonSocial.getId())) {
				errores.put("razonSocial", "La Razon Social no esta disponible. Intente otra diferente.");
			}
		}
		
		if (!errores.isEmpty()) {
			logger.error("Se encontraron los siguientes errores");
			errores.values().forEach((x->logger.error(x)));
			throw new ErrorValidationException("Se encontraron los siguientes errores", errores);
		}
		
	}
	
	//Valida los campos para el contacto del cliente
	private void validarContactoCliente(Contacto contacto, Set<Contacto> contactos) throws ErrorValidationException, BusinessException {
		Map<String, String> errores = new HashMap<>();
		
		if (contacto == null) {
			throw new BusinessException("Contacto vacío.");
		}
		
		Contacto existContacto = contactos.stream().filter(c -> 
				(contacto.getId() == null  || (contacto.getId() != null && contacto.getId() != c.getId())) && 
					c.getCliente().getId().equals(contacto.getCliente().getId()) && c.getNombre().equals(contacto.getNombre()) && c.getTipo().equals(contacto.getTipo())
				).findFirst().orElse(null);

		if(existContacto != null) {
			throw new BusinessException("Ya existe un contacto con el mismo nombre y tipo para este cliente.");
		}
		
		if (StringUtils.isBlank(contacto.getNombre())) {
			errores.put("nombreContacto", "El campo Nombre es requerido.");
		} else if (contacto.getNombre().length() > 30) {
			errores.put("nombreContacto", "El campo Nombre no debe superar los 30 caracteres");
		}
		
		if (contacto.getTipo() == null) {
			errores.put("tipoContacto", "El campo Tipo es requerido.");
		}
		
		if (!errores.isEmpty()) {
			logger.error("Se encontraron los siguientes errores");
			errores.values().forEach((x->logger.error(x)));
			throw new ErrorValidationException("Se encontraron los siguientes errores", errores);
		}
		
	}
	
	@Resource(name = "contactoRepository")
	void setContactoRepository(ContactoRepository contactoRepository) {
		this.contactoRepository = contactoRepository;
	}
	
	@Resource(name = "clienteRepository")
	void setClienteRepository(ClienteRepository clienteRepository) {
		this.repository = clienteRepository;
	}
	
	/*
	@Override
	public Cliente registrarClientePersona(Cliente cliente) throws ErrorValidationException, BusinessException {
		validarClienteNoVacio(cliente);
		validarAltaModificacionCliente(cliente);
		validaContactoCliente(cliente.getContacto());
		cliente = registrarCliente(cliente);
		return cliente;
	}
	
	//Valida que los requerimientos de contraseña sean validos
	private void validarContraseña(Cliente cliente) throws BusinessException {
		if (cliente.getPassword()==null)
			throw new BusinessException("Debe ingresar una contraseña.");			
	}

	//Valida que la composición del cliente este completa, que no sea null y que la
	//cuenta bancaria, el contacto y el ingreso tampoco sea null.
	private void validarClienteNoVacio(Cliente cliente) throws BusinessException {
		if (cliente == null) {
			throw new BusinessException("Cliente vacío.");
		} else if (cliente.getContacto() == null) {
			throw new BusinessException("Contacto vacío.");
		}
	}

	//Ultimos detalles del registro del cliente. Lo pone como desbloqueado. Setea
	//la cantidad de inicios de reintento de login en cero. Setea que debe
	//cambiarse el password. Genera un password aleatorio y notifica al cliente la
	//bienvenida por mail.
	private Cliente registrarCliente(Cliente cliente) {
		// Por default el cliente se da de desbloqueado.
		cliente.setBloqueado(false);

		// La cantidad de intentos de login es cero.
		cliente.setIntentosFallidosLogin(INICIO_REINTENTOS_LOGIN);

		// Por mas que luego se reenviara el password, se requerira cambio de password.
		cliente.setRequiereCambioPassword(true);

		String tmppass = generarPasswordAleatorio();
		cliente.setPassword(passwordEncoder.encode(tmppass));

		cliente.setFechaRegistro(Calendar.getInstance().getTime());

		cliente = getRepository().save(cliente);

		notificarPasswordANuevoCliente(cliente, tmppass);

		return cliente;
	}
	
	@Override
	public void updatePassword(Cliente cliente, String password, String newPassword) {
		if (!passwordEncoder.matches(password, cliente.getPassword())) {
			throw new NuclearJSecurityException("Contraseña actual inválida.");
		} else if (password.equals(newPassword)) {
			throw new NuclearJSecurityException("La nueva contraseña es igual a la actual.");
		}
		cliente.setPassword(passwordEncoder.encode(newPassword));
		cliente.setFechaAltaPassword(Calendar.getInstance().getTime());
		cliente.setRequiereCambioPassword(Boolean.FALSE);
		getRepository().saveAndFlush(cliente);
	}

	public void resetPassword(Long idCliente) {
		Cliente cliente = repository.findOne(idCliente);
		// La cantidad de intentos de login es cero.
		cliente.setIntentosFallidosLogin(INICIO_REINTENTOS_LOGIN);
		// Se requerira cambio de password.
		cliente.setRequiereCambioPassword(true);
		// Genero una password temporal
		String tmppass = generarPasswordAleatorio();
		cliente.setPassword(passwordEncoder.encode(tmppass));
		getRepository().saveAndFlush(cliente);
		notificacionService.notificarResetoPassword(cliente, tmppass);
	}

	protected void notificarResetoPassword(Cliente cliente, String tmpass) {
		notificacionService.notificarResetoPassword(cliente, tmpass);
	}

	private void notificarPasswordANuevoCliente(Cliente cliente, String tmpass) {
		notificacionService.notificarRegistroClienteNuevoPassword(cliente, tmpass);
	}
	
	// Valida que el username, email y numero de identificacion no se encuentre registrado.
	public void validarAltaModificacionCliente(Cliente cliente) throws ErrorValidationException {

		Map<String, String> errores = new HashMap<>();

		// Validacion username
		if (StringUtils.isBlank(cliente.getUsername())) {
			errores.put("username", "El campo username es requerido.");
		} else if (cliente.getUsername().length() < 5) {
			errores.put("username", "El campo username debe tener al menos 5 caracteres.");
		} else if (!Validations.isUsernameValido(cliente.getUsername())) {
			errores.put("username",
					"El campo username debe empezar con una letra y tener al menos 5 caracteres alfanuméricos sin espacios.");
		} else {
	
			Cliente cliByUsername = getRepository().findByUsernameEqualsIgnoreCase(cliente.getUsername());
			
			if(cliByUsername!=null && cliente.getId()==null || 
					cliByUsername!=null && cliente.getId()!=null && !cliente.getId().equals(cliByUsername.getId())) {
			
				errores.put("username", "El username no esta disponible. Intente otro diferente.");
			}
			
			cliByUsername = null;
	
		}

		// Validacion email
		if (!EmailValidator.getInstance().isValid(cliente.getEmail())) {
			errores.put("email", "El campo Email no tiene un formato válido.");
		} else {
			
			Cliente cliByEmail = getRepository().findByEmailEqualsIgnoreCase(cliente.getEmail());
			
			if(cliByEmail!=null && cliente.getId()==null || 
					cliByEmail!=null && cliente.getId()!=null && !cliente.getId().equals(cliByEmail.getId())) {
		
				errores.put("email", "El Email ingresado ya se encuentra registrado.");
			
			}
			
			cliByEmail = null;
			
		}

		// Validacion identificacion
		validaIdentificacion(cliente.getId(), cliente.getIdentificacion());
	
		if (StringUtils.isBlank(cliente.getNombre()) || StringUtils.isBlank(cliente.getApellido())) {
			errores.put("nombreApellido", "El campo Nombre y Apellido es requerido.");
		} else if (cliente.getNombre().length() > 30) {
			errores.put("nombre", "El campo Nombre no debe superar los 30 caracteres");
		} else if (cliente.getApellido().length() > 30) {
			errores.put("apellido", "El campo Apellido no debe superar los 30 caracteres");
		}

		if (!errores.isEmpty()) {
			logger.error("Se encontraron los siguientes errores");
			errores.values().forEach((x->logger.error(x)));
			throw new ErrorValidationException("Se encontraron los siguientes errores", errores);
		}

	}
	
	@Override
	public void validaContactoCliente(Contacto contacto) throws ErrorValidationException {

		Map<String, String> errores = new HashMap<>();

		if (StringUtils.isBlank(contacto.getTelefonoMovil())) {
			errores.put("telefonoMovil", "Debe ingresar al menos un teléfono fijo o celular");
		} else {
			
			if (StringUtils.isBlank(contacto.getTelefonoMovil())
					&& !Validations.isCelularValido(contacto.getTelefonoMovil())) {
				errores.put("telefonoMovil",
						"El campo Telefono movil debe contener exactamente 12 dígitos inclyendo el 15 despues del código de area sin 0. (Ej: 111560001234)");
			}
		}
		
		if (!errores.isEmpty()) {
			logger.error("Se encontraron los siguientes errores");
			errores.values().forEach((x->logger.error(x)));
			throw new ErrorValidationException("Se encontraron los siguientes errores", errores);
		}

	}
	
	public void validaIdentificacion(Long idCliente, Identificacion identificacion) throws ErrorValidationException {

		Map<String, String> errores = new HashMap<>();
		
		String numeroIdentificacion = identificacion.getNumero();
		TipoIdentificacion tipoIdentificacion = identificacion.getTipo();

		switch (tipoIdentificacion) {
		case DNI:

			if (StringUtils.isBlank(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", "El campo DNI es requerido.");
			} else if (!Validations.isDNIValido(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", "El campo DNI Debe contener 7 u 8 caracteres numéricos");
			}
			break;
		case CUIT:
			if (StringUtils.isBlank(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", "El campo CUIT es requerido.");
			} else if (numeroIdentificacion.length() != 11) {
				errores.put("numeroIdentificacion", "El campo CUIT Debe tener exactamente 11 dígitos.");
			} else if (!Validations.isCuitValido(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", "El campo CUIT debe comenzar con 20, 23, 24, 27, 30, 33 o 34.");
			} else if (!Validations.isDigitoVerificadorCuitCuilValido(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", "El campo CUIT tiene el dígito verificador incorrecto.");
			}
			break;
		case CUIL:
			if (StringUtils.isBlank(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", "El campo CUIL es requerido.");
			} else if (numeroIdentificacion.length() != 11) {
				errores.put("numeroIdentificacion", "El campo CUIL debe tener exactamente 11 dígitos.");
			} else if (!Validations.isCuilValido(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", "El campo CUIL debe comenzar con 20, 23, 24 o 27.");
			} else if (!Validations.isDigitoVerificadorCuitCuilValido(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", "El campo CUIL tiene el dígito verificador incorrecto.");
			}

			break;
		default:
			break;
		}

		Cliente cli = getRepository().findByIdentificacionNumeroLikeIgnoreCase(numeroIdentificacion);
		
		if(cli != null && idCliente == null || cli != null && !idCliente.equals(cli.getId())) {
		
			Identificacion identificacionPorNumeroEncontrado = cli.getIdentificacion();
			if (identificacionPorNumeroEncontrado.getTipo().equals(tipoIdentificacion)
					|| identificacionPorNumeroEncontrado.getNumero().substring(2, 9).equals(numeroIdentificacion)) {
				errores.put("numeroIdentificacion", tipoIdentificacion + " ya se encuentra registrado");
			}
			identificacionPorNumeroEncontrado = null;
		}
		
		cli = null;


		if (!errores.isEmpty()) {
			logger.error("Se encontraron los siguientes errores");
			errores.values().forEach((x->logger.error(x)));
			throw new ErrorValidationException("Se encontraron los siguientes errores", errores);
		}
		
	}

	private String generarPasswordAleatorio() {
		String generateKey = KeyGenerators.string().generateKey();
		return generateKey;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return getRepository().findByUsernameEquals(username);
	}

	@Override
	public Cliente getClientePorUsername(String username) {
		return getRepository().findByUsernameEquals(username);
	}

	@Override
	public Cliente getClientePorMail(String username) {
		return getRepository().findByEmailEqualsIgnoreCase(username);
	}

	@Override
	public Cliente getClienteCompleto(Long id) {
		return getRepository().getClienteCompleto(id);
	}

	@Override
	public List<Cliente> listClientesListado() {
		return getRepository().listClientesListado();
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Resource(name = "clienteRepository")
	void setClienteRepository(ClienteRepository clienteRepository) {
		this.repository = clienteRepository;
	}

	@Resource(name = "notificacionService")
	public void setNotificacionService(NotificacionService notificacionService) {
		this.notificacionService = notificacionService;
	}
*/
}

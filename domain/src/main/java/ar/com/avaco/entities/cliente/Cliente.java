package ar.com.avaco.entities.cliente;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author el betazo
 *
 */
@Entity
@Table(name = "CLIENTE")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "CLIENTE_SEQ", sequenceName = "CLIENTE_SEQ", allocationSize = 1)
public class Cliente extends ar.com.avaco.arc.core.domain.Entity<Long> {

	private static final long serialVersionUID = 2843597480559139677L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTE_SEQ")
	@Column(name = "ID_CLIENTE")
	private Long id;

	/**
	 * La razon social.
	 */
	@Column(name = "RAZON_SOCIAL", nullable = false)
	private String razonSocial;
	
	/**
	 * Contacto con el cliente.
	
	@NotNull
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private Contacto contacto;*/ 
	
	@OneToMany(targetEntity= Contacto.class, mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Contacto> contactos = new HashSet<>();
	
	/**
	 * El domicilio completo.
	 */
	@Column(name = "DOMICILIO")
	private String domicilio;

	/**
	 * El codigo postal.
	 */
	@Column(name = "CODIGO_POSTAL", length = 10)
	private String codigoPostal;
	
	/**
	 * Localidad.
	 */
	@Column(name = "LOCALIDAD")
	private String localidad;

	/**
	 * Provincia.
	 */
	@Enumerated
	@Column(name = "PROVINCIA")
	private Provincia provincia;
	
	/**
	 * Telefono fijo.
	 */
	@Column(name = "TEL_FIJO")
	private String telefonoFijo;

	/**
	 * Telefono celular.
	 */
	@Column(name = "TEL_CELULAR")
	private String telefonoCelular;
	
	/**
	 * Email del cliente.
	 */
	@Column(name = "EMAIL")
	private String email;
	
	/**
	 * Sitio web.
	 */
	@Column(name = "WEB_SITE")
	private String webSite;
	
	/**
	 * CUIT
	 */
	@Column(name = "CUIT")
	private String cuit;
	
	/**
	 * Ingreso brutos
	 */
	@Column(name = "INGRESOS_BRUTOS")
	private Double ingresosBrutos;

	public Cliente() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	public Set<Contacto> getContactos() {
		return contactos;
	}

	public void setContactos(Set<Contacto> contactos) {
		this.contactos = contactos;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public String getTelefonoFijo() {
		return telefonoFijo;
	}

	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	public String getTelefonoCelular() {
		return telefonoCelular;
	}

	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getCUIT() {
		return cuit;
	}

	public void setCUIT(String cuit) {
		this.cuit = cuit;
	}

	public Double getIngresosBrutos() {
		return ingresosBrutos;
	}

	public void setIngresosBrutos(Double ingresosBrutos) {
		this.ingresosBrutos = ingresosBrutos;
	}
	
	

	
	
	
	
}

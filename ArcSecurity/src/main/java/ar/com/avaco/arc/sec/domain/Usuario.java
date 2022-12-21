package ar.com.avaco.arc.sec.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "SEG_USUARIO")
@SequenceGenerator(name = "SEG_USUARIO_SEQ", sequenceName = "SEG_USUARIO_SEQ", allocationSize = 1)
public class Usuario extends ar.com.avaco.arc.core.domain.Entity<Long> implements UserDetailsExtended {
	
	private static final long serialVersionUID = 2797698434873046327L;

	/**
	 * Cantidad maxima que el usuario puede intentar ingresar al sistema con
	 * contraseñas incorrectas.
	 */
	private static final int CANTIDAD_MAXIMA_INTENTOS_FALLIDOS = 10;
	
	/**
	 * Cantidad maxima de dias de uso del password.
	 */
	private static final int CANTIDAD_MAXIMA_DIAS_PASSWORD = 999999;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEG_USUARIO_SEQ")
	@Column(name = "ID_SEG_USUARIO")
	private Long id;

	/**
	 * El nombre de usuario de identificacion univoca.
	 */
	@Column(unique = true, length = 50, updatable = false, name = "NOMBRE_USUARIO")
	private String username;

	/**
	 * La contraseña de acceso.
	 */
	@Column
	private String password;

	/**
	 * El nombre.
	 */
	@Column(length = 100)
	private String nombre;

	/**
	 * El apellido.
	 */
	@Column(length = 100)
	private String apellido;

	/**
	 * Roles assigned to the user.
	 */
	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Acceso> accesos = new HashSet<Acceso>();

	/**
	 * Email unico para contacto directo con el usuario.
	 */
	@Column(length = 100, nullable = false)
	private String email;

	/**
	 * Listado de emails asociados al usuario para realizar la notificacion de
	 * operaciones como nominaciones, autorizaciones, etc.
	 */
	@ElementCollection
	private Set<String> mailsNotificacion;

	@Column
	private boolean interno;

	/**
	 * Cantidad de intentos fallidos en que el usuario intento loguearse con una
	 * contraseña incorrecta.
	 */
	@Column(name = "INTENTOS_FALLIDOS_LOGIN")
	private Integer intentosFallidosLogin;

	/**
	 * Determina si el usuario se encuentra bloqueado para ingresar al sistema.
	 */
	@Column
	private boolean bloqueado;

	/**
	 * Determina si el usuario luego de loguearse debe cambiar su contraseña.
	 */
	@Column(name = "REQUIERE_CAMBIO_PASSWORD")
	private boolean requiereCambioPassword;

	/**
	 *  Fecha de alta de nuevo password
	 */
	@Column(name = "FECHA_ALTA_PASSWORD")
	private Date fechaAltaPassword;
	
	/**
	 * Listado de contraseñas empleadas anteriormente.
	 */
	@ElementCollection
	@CollectionTable(name="HISTORICO_PASSWORDS")
	private Set<String> historicoPasswords;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SEG_USR_IMPER", 
	joinColumns = {@JoinColumn(name = "ID_SEG_USUARIO", referencedColumnName = "ID_SEG_USUARIO")}, 
	inverseJoinColumns = @JoinColumn(name = "ID_SEG_USUARIO_IMPER", referencedColumnName = "ID_SEG_USUARIO"))
	private Set<Usuario> impersonables = new HashSet<Usuario>();
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.intentosFallidosLogin < CANTIDAD_MAXIMA_INTENTOS_FALLIDOS;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !requiereCambioPassword && !passwordExpirado();
	}

	private boolean passwordExpirado() {
		
		boolean result = false;
		
		if(fechaAltaPassword != null){
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaAltaPassword);
			calendar.add(Calendar.DAY_OF_YEAR, CANTIDAD_MAXIMA_DIAS_PASSWORD);
			result = Calendar.getInstance().getTime().after(calendar.getTime());
		}

		return result;
	}

	@Override
	public boolean isEnabled() {
		return !this.bloqueado;
	}

	public boolean hasRol(String permiso, String rol){
		for(Acceso acceso : accesos){
			Perfil perfil = acceso.getPerfil();
			for(Permiso permisoDelPerfil : perfil.getPermisos()){
				if(permisoDelPerfil.getCodigo().equals(permiso)
						&& perfil.getRol().getCodigo().equals(rol)){
					return true;
				}
			}
		}
				
		return false;
	}
	
	public List<Rol> getRolesByPermiso(String permiso){
		List<Rol> roles = new ArrayList<Rol>();
		
		for(Acceso acceso : this.getAccesos()){
			Perfil perfil = acceso.getPerfil();
			for(Permiso permisoDelPerfil : perfil.getPermisos()){
					if(permisoDelPerfil.getCodigo().equals(permiso)){
						roles.add(perfil.getRol());
				}
			}
		}
		
		return roles;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getAuthorities
	 * ()
	 * 
	 * Verifica si existe un usuario seteado para impersonar. Si hay un usuario
	 * seteado, devuelve los granted authorities del mismo. Sino devuelve los
	 * del actual.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthorities(this);
	}

	/**
	 * Obtiene los granted authorities de un usuario.
	 * 
	 * @param usuario
	 *            El usuario.
	 * @return El listado de granted authorities.
	 */
	private Collection<? extends GrantedAuthority> getAuthorities(
			Usuario usuario) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (Acceso acceso : usuario.getAccesos()) {
			if (acceso.getPerfil().isActivo()) {
				for (Permiso permiso : acceso.getPerfil().getPermisos()) {
					if (permiso.isActivo()) {
						authorities.add(permiso);
					}
				}
			}
		}
		return authorities;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the apellido
	 */
	public String getApellido() {
		return apellido;
	}

	/**
	 * @param apellido
	 *            the apellido to set
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * @return the accesos
	 */
	public Set<Acceso> getAccesos() {
		return accesos;
	}

	/**
	 * @param accesos
	 *            the accesos to set
	 */
	public void setAccesos(Set<Acceso> accesos) {
		this.accesos = accesos;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public Set<String> getMailsNotificacion() {
		return mailsNotificacion;
	}

	public void setMailsNotificacion(Set<String> mailsNotificacion) {
		this.mailsNotificacion = mailsNotificacion;
	}

	/**
	 * @return the interno
	 */
	public boolean isInterno() {
		return interno;
	}

	/**
	 * @param interno
	 *            the interno to set
	 */
	public void setInterno(boolean interno) {
		this.interno = interno;
	}

	/**
	 * @return the intentosFallidosLogin
	 */
	public Integer getIntentosFallidosLogin() {
		return intentosFallidosLogin;
	}

	/**
	 * @param intentosFallidosLogin
	 *            the intentosFallidosLogin to set
	 */
	public void setIntentosFallidosLogin(Integer intentosFallidosLogin) {
		this.intentosFallidosLogin = intentosFallidosLogin;
	}

	/**
	 * @return the bloqueado
	 */
	public boolean isBloqueado() {
		return bloqueado;
	}

	/**
	 * @param bloqueado
	 *            the bloqueado to set
	 */
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	/**
	 * @return the historicoPasswords
	 */
	public Set<String> getHistoricoPasswords() {
		return historicoPasswords;
	}

	/**
	 * @param historicoPasswords
	 *            the historicoPasswords to set
	 */
	public void setHistoricoPasswords(Set<String> historicoPasswords) {
		this.historicoPasswords = historicoPasswords;
	}

	/**
	 * @return the requiereCambioPassword
	 */
	public boolean isRequiereCambioPassword() {
		return requiereCambioPassword;
	}

	/**
	 * @param requiereCambioPassword
	 *            the requiereCambioPassword to set
	 */
	public void setRequiereCambioPassword(boolean requiereCambioPassword) {
		this.requiereCambioPassword = requiereCambioPassword;
	}

	public String getNombreApellido() {
		return nombre + " " + apellido;
	}

	public String getNombreApellidoUsername() {
		return username + " (" + nombre + " " + apellido + " )";
	}
	
	/**
	 * update fail attempt 
	 */
	public void incrementarIntentoFallido(){
		this.intentosFallidosLogin++;
	}

	public Date getFechaAltaPassword() {
		return fechaAltaPassword;
	}

	public void setFechaAltaPassword(Date fechaAltaPassword) {
		this.fechaAltaPassword = fechaAltaPassword;
	}
	
	public void addPasswordToHistoric(String password){
	
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Usuario ");
		sb.append(this.username);
		sb.append(" (");
		sb.append(id);
		sb.append(")");
		return sb.toString();
	}

	public Set<Usuario> getImpersonables() {
		return impersonables;
	}

	public void setImpersonables(Set<Usuario> impersonables) {
		this.impersonables = impersonables;
	}
}
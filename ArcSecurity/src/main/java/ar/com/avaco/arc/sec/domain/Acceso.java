package ar.com.avaco.arc.sec.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * Un acceso representa la union de un perfil con un listado de rolCompania.
 * Un usuario tiene asigando un acceso y este determina que las
 * acciones que puede realizar en base a los permisos puede realizarse oeprando
 * con el listado de companias y gasoductos asignados.
 * 
 * @author aogonzalez
 * 
 */
@Entity
@Table(name = "SEG_ACCESO")
@SequenceGenerator(name = "SEG_ACCESO_SEQ", sequenceName = "SEG_ACCESO_SEQ", allocationSize = 1)
public class Acceso extends ar.com.avaco.arc.core.domain.Entity<Long> {

	/** */
	private static final long serialVersionUID = -445795213829738066L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEG_ACCESO_SEQ")
	@Column(name = "ID_SEG_ACCESO")
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "USUARIO_FK"))
	private Usuario usuario;
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * El perfil del Acceso
	 */
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "PERFIL_ID_FK"))
	private Perfil perfil;

	/**
	 * @return the perfil
	 */
	public Perfil getPerfil() {
		return perfil;
	}

	/**
	 * @param perfil
	 *            the perfil to set
	 */
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Acceso (");
		sb.append(id);
		sb.append(")");
		return sb.toString();
	}

	public String getDescripcion() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPerfil().getRol().getNombre());
		sb.append(" - ");
		sb.append(getPerfil().getNombre());
		return sb.toString();
	}
}
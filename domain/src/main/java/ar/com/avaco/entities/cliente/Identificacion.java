package ar.com.avaco.entities.cliente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * La identificación determina el tipo y numero con el que se identifica un
 * cliente. Por ejemplo DNI 30219433 o CUIT 2793859647.
 * 
 * @author beto
 *
 */
@Entity
@Table(name = "CLI_IDENTIFICACION")
public class Identificacion extends ar.com.avaco.arc.core.domain.Entity<Long> {

	private static final long serialVersionUID = 1091099862642276009L;

	/**
	 * Id de Identificacion igual al Id de Cliente.
	 * Al ser una relacion 1 a 1 es el mismo Id.
	 */
	@Id
	@GeneratedValue(generator = "foreigngeniden")
	@GenericGenerator(strategy = "foreign", name = "foreigngeniden", parameters = @Parameter(name = "property", value = "cliente"))
	@Column(name = "ID_IDENTIFICACION")
	private Long id;

	/**
	 * El cliente al cual pertenece la identificacion.
	 */
	//@OneToOne(mappedBy = "identificacion", optional = false)
	//@PrimaryKeyJoinColumn
	//private Cliente cliente;

	/**
	 * Tipo de identificacion.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ID", nullable = false)
	private TipoIdentificacion tipo;

	/**
	 * Numero de la identificacion.
	 */
	@Column(name = "NUMERO_ID", nullable = false)
	private String numero;

	public TipoIdentificacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoIdentificacion tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}*/

	@Override
	public String toString() {
		return tipo + " " + numero;
	}
	
}

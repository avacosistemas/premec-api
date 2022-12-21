package ar.com.avaco.repository.cliente;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.entities.cliente.Cliente;

/**
 * Repositorio de Clientes
 * @author betongo
 *
 */
@Repository("clienteRepository")
public class ClienteRepositoryImpl extends NJBaseRepository<Long, Cliente> implements ClienteRepositoryCustom {

	public ClienteRepositoryImpl(EntityManager entityManager) {
		super(Cliente.class, entityManager);
	}
	
	@Override
	public Cliente getCliente(Long id) {
		Criteria criteria = getCurrentSession().createCriteria(getHandledClass());
		criteria.add(Restrictions.eq("id", id));
		criteria.setFetchMode("contacto", FetchMode.JOIN); 
		return (Cliente) criteria.uniqueResult();
	}

	@Override
	public List<Cliente> listClientes() {
		Criteria criteria = getCurrentSession().createCriteria(getHandledClass());
		criteria.setFetchMode("contacto", FetchMode.JOIN); 
		criteria.addOrder(Order.asc("username"));
		return criteria.list();
	}


	
	
}

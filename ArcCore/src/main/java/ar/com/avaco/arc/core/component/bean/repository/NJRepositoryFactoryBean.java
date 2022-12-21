package ar.com.avaco.arc.core.component.bean.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

public class NJRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I> {

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new NJRepositoryFactory(entityManager);
	}

	private static class NJRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

		public NJRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
		}

		@Override
		protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(
				RepositoryInformation information, EntityManager entityManager) {
			NJBaseRepository njBaseRepository = new NJBaseRepository(information.getDomainType(), entityManager);
			SessionFactory sessionFactory = getSessionFactory(entityManager);
			njBaseRepository.setSessionFactory(sessionFactory);

			return njBaseRepository;
		}

		private SessionFactory getSessionFactory(EntityManager entityManager) {
			HibernateJpaSessionFactoryBean factory = new HibernateJpaSessionFactoryBean();
			factory.setEntityManagerFactory(entityManager.getEntityManagerFactory());
			SessionFactory sessionFactory = factory.getObject();
			return sessionFactory;
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return NJRepository.class;
		}
	}
}
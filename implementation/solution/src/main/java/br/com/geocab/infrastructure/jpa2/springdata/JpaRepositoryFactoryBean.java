package br.com.geocab.infrastructure.jpa2.springdata;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import br.com.geocab.domain.entity.IEntity;

public class JpaRepositoryFactoryBean<R extends JpaRepository<Entity, I>, Entity extends IEntity<I>, I extends Serializable> extends org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean<R, Entity, I>
{
	/**
	 * 
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected RepositoryFactorySupport createRepositoryFactory( EntityManager entityManager)
	{
		return new RepositoryFactory(entityManager);
	}

	/**
	 * 
	 * @author rodrigo
	 *
	 * @param <Entity>
	 * @param <I>
	 */
	private static class RepositoryFactory<Entity extends IEntity<I>, I extends Serializable> extends JpaRepositoryFactory
	{
		private EntityManager entityManager;

		public RepositoryFactory(EntityManager entityManager)
		{
			super(entityManager);

			this.entityManager = entityManager;
		}

		/**
		 * 
		 */
		@Override
		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata)
		{
			return new JpaRepository<Entity, I>((Class<Entity>) metadata.getDomainType(), entityManager);
		}

		/**
		 * 
		 */
		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata)
		{
			// The RepositoryMetadata can be safely ignored, it is used by the
			// JpaRepositoryFactory
			// to check for QueryDslJpaRepository's which is out of scope.
			return IDataRepository.class;
		}
	}
}
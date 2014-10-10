package br.com.geocab.infrastructure.jpa2.springdata;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import br.com.geocab.domain.entity.IEntity;

public class JpaRepository<Entity extends IEntity<ID>, ID extends Serializable> extends SimpleJpaRepository<Entity, ID> implements IDataRepository<Entity, ID>
{
	protected EntityManager em;
	
	/**
	 * Creates a new {@link SimpleJpaRepository} to manage objects of the given {@link JpaEntityInformation}.
	 * 
	 * @param entityInformation must not be {@literal null}.
	 * @param entityManager must not be {@literal null}.
	 */
	public JpaRepository(JpaEntityInformation<Entity, ?> entityInformation, EntityManager entityManager) 
	{
		super(entityInformation, entityManager);
		this.em = entityManager;
	}
	
	/**
	 * Creates a new {@link SimpleJpaRepository} to manage objects of the given domain type.
	 * 
	 * @param domainClass must not be {@literal null}.
	 * @param em must not be {@literal null}.
	 */
	public JpaRepository(Class<Entity> domainClass, EntityManager entityManager)
	{
		super(domainClass, entityManager);
		this.em = entityManager;
	}
	
	/**
	 * 
	 * @param entity
	 */
	@Override
	public void detach( Entity entity )
	{
		this.em.detach(entity);
	}
	
	/**
	 * 
	 * @param entity
	 */
	@Override
	public void replicate( Entity entity, ReplicationMode replicationMode)
	{
		final Session session = (Session) this.em.getDelegate();
		session.replicate(entity, replicationMode);
	}
}
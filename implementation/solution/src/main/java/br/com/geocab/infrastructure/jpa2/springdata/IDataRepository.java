package br.com.geocab.infrastructure.jpa2.springdata;

import java.io.Serializable;

import org.hibernate.ReplicationMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.geocab.domain.entity.IEntity;

/**
 * 
 * @author Rodrigo P. Fraga
 * @since 16/04/2014
 * @version 1.0
 * @category Repository
 */
@NoRepositoryBean
public interface IDataRepository<Entity extends IEntity<ID>, ID extends Serializable> extends JpaRepository<Entity, ID>
{
	/**
	 * 
	 * @param entity
	 */
	public void replicate(Entity object, ReplicationMode replicationMode);
	
	/**
	 * 
	 * @param entity
	 */
	public void detach( Entity entity );
}
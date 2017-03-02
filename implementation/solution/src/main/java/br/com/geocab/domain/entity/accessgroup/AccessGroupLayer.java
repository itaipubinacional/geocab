/**
 * 
 */
package br.com.geocab.domain.entity.accessgroup;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.layer.Layer;

/**
 * 
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/12/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject
@Table(schema=IEntity.SCHEMA)
public class AccessGroupLayer extends AbstractEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6496162028366468252L;
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	@ManyToOne
	@JoinColumn(name="access_group_id")
	private AccessGroup accessGroup;
	
	/*
	 * 
	 */
	@ManyToOne
	@JoinColumn(name="layer_id")
	private Layer layer;
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	public AccessGroupLayer()
	{
		
	}
	
	/**
	 * 
	 * @param id
	 */
	public AccessGroupLayer(Long id)
	{
		this.id = id;
	}
	
	/**
	 * 
	 * @param id
	 * @param accessGroup
	 * @param layer
	 */
	public AccessGroupLayer(Long id, AccessGroup accessGroup, Layer layer)
	{
		super(id);
		this.accessGroup = accessGroup;
		this.layer = layer;
	}
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
		result = prime * result
				+ ((accessGroup == null) ? 0 : accessGroup.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		AccessGroupLayer other = (AccessGroupLayer) obj;
		if (layer == null)
		{
			if (other.layer != null) return false;
		}
		else if (!layer.equals(other.layer)) return false;
		if (accessGroup == null)
		{
			if (other.accessGroup != null) return false;
		}
		else if (!accessGroup.equals(other.accessGroup)) return false;
		return true;
	}

	/**
	 * @return the accessGroup
	 */
	public AccessGroup getAccessGroup()
	{
		return accessGroup;
	}

	/**
	 * @param accessGroup the accessGroup to set
	 */
	public void setAccessGroup(AccessGroup accessGroup)
	{
		this.accessGroup = accessGroup;
	}

	/**
	 * @return the layer
	 */
	public Layer getLayer()
	{
		return layer;
	}

	/**
	 * @param layer the layer to set
	 */
	public void setLayer(Layer layer)
	{
		this.layer = layer;
	}

}

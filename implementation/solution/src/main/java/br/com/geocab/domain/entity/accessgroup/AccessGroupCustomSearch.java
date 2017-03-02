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
import br.com.geocab.domain.entity.layer.CustomSearch;

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
@DataTransferObject(javascript="AccessGroupCustomSearch")
@Table(schema=IEntity.SCHEMA)
public class AccessGroupCustomSearch extends AbstractEntity implements Serializable
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
	@JoinColumn(name="custom_search_id")
	private CustomSearch customSearch;
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	public AccessGroupCustomSearch()
	{
		
	}
	
	/**
	 * 
	 * @param id
	 */
	public AccessGroupCustomSearch(Long id)
	{
		this.id = id;
	}
	
	/**
	 * 
	 * @param id
	 * @param accessGroup
	 * @param customSearch
	 */
	public AccessGroupCustomSearch(Long id, AccessGroup accessGroup, CustomSearch customSearch)
	{
		super(id);
		this.accessGroup = accessGroup;
		this.customSearch = customSearch;
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
		result = prime * result + ((customSearch == null) ? 0 : customSearch.hashCode());
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
		AccessGroupCustomSearch other = (AccessGroupCustomSearch) obj;
		if (customSearch == null)
		{
			if (other.customSearch != null) return false;
		}
		else if (!customSearch.equals(other.customSearch)) return false;
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
	 * @return the customSearch
	 */
	public CustomSearch getCustomSearch()
	{
		return customSearch;
	}

	/**
	 * @param customSearch the customSearch to set
	 */
	public void setCustomSearch(CustomSearch customSearch)
	{
		this.customSearch = customSearch;
	}

}

package br.com.geocab.domain.entity.accessgroup;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.tool.Tool;

/**
 * Classe responsável por definir o comportamento de um {@link AccessGroup}
 * 
 * 
 * @author Vinicius R. Kawamoto
 * @since 05/06/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject
@Table(schema=IEntity.SCHEMA)
public class AccessGroup extends AbstractEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6380496199746377568L;
	
	public static final Long PUBLIC_GROUP_ID = 1L;
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * name of {@link AccessGroup}
	 */
	@NotEmpty
	@Column(nullable=false, length=144, unique=true)
	private String name;
	/**
	 * description of {@link AccessGroup}
	 */
	@NotEmpty
	@Column(nullable=false, length=255)
	private String description;
	
	/**
	 * 
	 */
	@OneToMany(mappedBy="accessGroup")
	private Set<AccessGroupLayer> accessGroupLayer= new HashSet<AccessGroupLayer>();
	
	/**
	 * 
	 */
	@OneToMany(mappedBy="accessGroup")
	private Set<AccessGroupCustomSearch> accessGroupCustomSearch = new HashSet<AccessGroupCustomSearch>();
	
	/**
	 * list of {@link Tool} tools of {@link AccessGroup}
	 */
	@ManyToMany(fetch=FetchType.EAGER, cascade={ CascadeType.MERGE })
	@JoinTable(name="ACCESS_GROUP_TOOL", schema=IEntity.SCHEMA,  
	joinColumns={ 
		@JoinColumn(name="access_group_id", referencedColumnName="id", nullable=true) 
	},
	inverseJoinColumns={
		@JoinColumn(name="tool_id", referencedColumnName="id", nullable=true)
	})
	private Set<Tool> tools = new HashSet<Tool>();
	
	/**
	 * list of {@link User} users of {@link AccessGroup}
	 */
	@ManyToMany(fetch=FetchType.EAGER, cascade={CascadeType.MERGE })
	@JoinTable(name="ACCESS_GROUP_USER", schema=IEntity.SCHEMA,  
	joinColumns={ 
		@JoinColumn(name="access_group_id", referencedColumnName="id", nullable=true) 
	},
	inverseJoinColumns={
		@JoinColumn(name="user_username", referencedColumnName="id", nullable=true)
	})
	private Set<User> users = new HashSet<User>();
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * @param id
	 */
	public AccessGroup()
	{
		
	}

	/**
	 * 
	 * @param id
	 */
	public AccessGroup( Long id )
	{
		this.setId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 */
	public AccessGroup( Long id, String name, String description )
	{
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
	}

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * @return
	 */
	@Override
	public Long getId()
	{
		return id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AccessGroup other = (AccessGroup) obj;
		if (description == null)
		{
			if (other.description != null) return false;
		}
		else if (!description.equals(other.description)) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		return true;
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the accessGroupLayer
	 */
	public Set<AccessGroupLayer> getAccessGroupLayer()
	{
		return accessGroupLayer;
	}

	/**
	 * @param accessGroupLayer the accessGroupLayer to set
	 */
	public void setAccessGroupLayer(Set<AccessGroupLayer> accessGroupLayer)
	{
		this.accessGroupLayer = accessGroupLayer;
	}

	/**
	 * @return the accessGroupCustomSearch
	 */
	public Set<AccessGroupCustomSearch> getAccessGroupCustomSearch()
	{
		return accessGroupCustomSearch;
	}

	/**
	 * @param accessGroupCustomSearch the accessGroupCustomSearch to set
	 */
	public void setAccessGroupCustomSearch(
			Set<AccessGroupCustomSearch> accessGroupCustomSearch)
	{
		this.accessGroupCustomSearch = accessGroupCustomSearch;
	}

	/**
	 * @return the tools
	 */
	public Set<Tool> getTools()
	{
		return tools;
	}

	/**
	 * @param tools the tools to set
	 */
	public void setTools(Set<Tool> tools)
	{
		this.tools = tools;
	}

	/**
	 * @return the users
	 */
	public Set<User> getUsers()
	{
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<User> users)
	{
		this.users = users;
	}

}
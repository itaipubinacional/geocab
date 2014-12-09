/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.accessgroup.AccessGroupCustomSearch;

/**
 * 
 * CLasse to define {@link CustomSearch}
 * 
 * @author Thiago 
 * @since 03/12/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject
@Table(schema=IEntity.SCHEMA)
public class CustomSearch extends AbstractEntity implements Serializable
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	private static final long serialVersionUID = -1358373183244765164L;
	/**
	 * name of {@link CustomSearch}
	 */
	@NotEmpty
	@Column(nullable=false, length=144, unique=true)
	private String name;
	/**
	 * {@link Layer} of {@link CustomSearch}
	 */
	@NotNull
	@ManyToOne(fetch=FetchType.EAGER)
	private Layer layer;
	
	/**
	 */
	@OneToMany(mappedBy="customSearch")
	private Set<AccessGroupCustomSearch> AccessGroupCustomSearch = new HashSet<AccessGroupCustomSearch>();
	
	/**
	 * 
	 */
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval=true)
	@JoinColumn(referencedColumnName="id", name="pesquisa_id")
	@OrderBy(value = "orderLayer") 
	private Set<LayerField> layerFields = new HashSet<LayerField>();
	
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public CustomSearch()
	{
		
	}
	/**
	 * 
	 *
	 * @param id
	 */
	public CustomSearch( Long id )
	{
		this.setId(id);
	}
	/**
	 * 
	 * @param id
	 * @param name
	 * @param layer
	 */
	public CustomSearch( Long id, String name, Layer layer)
	{
		this.setId(id);
		this.setName(name);
		this.setLayer(layer);
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
		CustomSearch other = (CustomSearch) obj;
		if (layer == null)
		{
			if (other.layer != null) return false;
		}
		else if (!layer.equals(other.layer)) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		return true;
	}
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
	/**
	 * @return the accessGroupCustomSearch
	 */
	public Set<AccessGroupCustomSearch> getAccessGroupCustomSearch()
	{
		return AccessGroupCustomSearch;
	}
	/**
	 * @param accessGroupCustomSearch the accessGroupCustomSearch to set
	 */
	public void setAccessGroupCustomSearch(
			Set<AccessGroupCustomSearch> accessGroupCustomSearch)
	{
		AccessGroupCustomSearch = accessGroupCustomSearch;
	}
	/**
	 * @return the layerFields
	 */
	public Set<LayerField> getLayerFields()
	{
		return layerFields;
	}
	/**
	 * @param layerFields the layerFields to set
	 */
	public void setLayerFields(Set<LayerField> layerFields)
	{
		this.layerFields = layerFields;
	}
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
}

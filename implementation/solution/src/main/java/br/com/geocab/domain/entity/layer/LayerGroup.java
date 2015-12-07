/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.geocab.domain.entity.AbstractEntity;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 19/09/2014
 * @version 1.0
 * @category Entity
 *
 */
@Entity
@Audited
@DataTransferObject(javascript="LayerGroup")
@Table(uniqueConstraints= @UniqueConstraint(columnNames={"name", "layer_group_upper_id"}))
public class LayerGroup extends AbstractEntity implements Serializable, ITreeNode
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8774128410822834963L;
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * nome do {@link LayerGroup}
	 */
	@NotEmpty
	@Column(nullable=false, length=144)
	private String name;
	/**
	 * 
	 */
	@Column
    private Integer orderLayerGroup;
	/**
	 * {@link LayerGroup} upper than the current {@link LayerGroup}
	 */
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	private LayerGroup layerGroupUpper;
	/**
	 * {@link LayerGroup} of {@link Layer}
	 */
	@OrderBy( value = "orderLayer" ) 
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(referencedColumnName="id", name="layer_group_id")
	private List<Layer> layers = new LinkedList<Layer>();
	
	/**
	 * {@link LayerGroup} of {@link LayerGroup}
	 */
	@OrderBy( value = "orderLayerGroup" ) 
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(referencedColumnName="id", name="layer_group_upper_id")
	private List<LayerGroup> layersGroup = new LinkedList<LayerGroup>();
	
	/**
	 * Draft {@link LayerGroup} that originated the published {@link LayerGroup}
	 */
	@OneToOne(fetch=FetchType.EAGER, optional=true)
	private LayerGroup draft;
	
	/**
	 * Field that informs if the {@link LayerGroup} is published
	 */
	@Column
	private Boolean published;
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public LayerGroup()
	{
		
	}
	
	/**
	 * 
	 *
	 * @param id
	 */
	public LayerGroup( Long id )
	{
		this.setId(id);
	}
	
	/**
	 * 
	 *
	 * @param id
	 */
	public LayerGroup( Long id, LayerGroup draft )
	{
		this.setId(id);
		this.setDraft(draft);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param layerGroupUpper
	 */
	public LayerGroup( Long id, String name, LayerGroup layerGroupUpper )
	{
		this.setId(id);
		this.setName(name);
		this.setLayerGroupUpper(layerGroupUpper);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 */
	public LayerGroup( Long id, String name )
	{
		this.setId(id);
		this.setName(name);
	}
	
	
	
	
	/**
	 * @param name
	 * @param orderLayerGroup
	 * @param layerGroupUpper
	 * @param layers
	 * @param layersGroup
	 * @param draft
	 * @param published
	 */
	public LayerGroup(Long id, String name, Integer orderLayerGroup, Boolean published, Long layerGroupUpperId)
	{
		this.setId(id);
		this.name = name;
		this.orderLayerGroup = orderLayerGroup;
		this.setLayerGroupUpper(new LayerGroup(layerGroupUpperId));
		this.published = published;
	}

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((draft == null) ? 0 : draft.hashCode());
		result = prime * result
				+ ((layerGroupUpper == null) ? 0 : layerGroupUpper.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + orderLayerGroup;
		result = prime * result
				+ ((published == null) ? 0 : published.hashCode());
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
		LayerGroup other = (LayerGroup) obj;
		if (draft == null)
		{
			if (other.draft != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		if (orderLayerGroup != other.orderLayerGroup) return false;
		if (published == null)
		{
			if (other.published != null) return false;
		}
		else if (!published.equals(other.published)) return false;
		return true;
	}
	
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	@Override
	public List<? extends ITreeNode> getNodes()
	{
		if ( layersGroup != null && !layersGroup.isEmpty() )
		{
			return this.layersGroup;
		}
		else
		{
			return this.layers;
		}
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
	 * @return the orderLayerGroup
	 */
	public Integer getOrderLayerGroup()
	{
		return orderLayerGroup;
	}

	/**
	 * @param orderLayerGroup the orderLayerGroup to set
	 */
	public void setOrderLayerGroup(Integer orderLayerGroup)
	{
		this.orderLayerGroup = orderLayerGroup;
	}

	/**
	 * @return the layerGroupUpper
	 */
	public LayerGroup getLayerGroupUpper()
	{
		return layerGroupUpper;
	}

	/**
	 * @param layerGroupUpper the layerGroupUpper to set
	 */
	public void setLayerGroupUpper(LayerGroup layerGroupUpper)
	{
		this.layerGroupUpper = layerGroupUpper;
	}

	/**
	 * @return the layers
	 */
	public List<Layer> getLayers()
	{
		return layers;
	}

	/**
	 * @param layers the layers to set
	 */
	public void setLayers(List<Layer> layers)
	{
		this.layers = layers;
	}

	/**
	 * @return the layersGroup
	 */
	public List<LayerGroup> getLayersGroup()
	{
		return layersGroup;
	}

	/**
	 * @param layersGroup the layersGroup to set
	 */
	public void setLayersGroup(List<LayerGroup> layersGroup)
	{
		this.layersGroup = layersGroup;
	}

	/**
	 * @return the draft
	 */
	public LayerGroup getDraft()
	{
		return draft;
	}

	/**
	 * @param draft the draft to set
	 */
	public void setDraft(LayerGroup draft)
	{
		this.draft = draft;
	}

	/**
	 * @return the published
	 */
	public Boolean getPublished()
	{
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(Boolean published)
	{
		this.published = published;
	}
	
	
	
}

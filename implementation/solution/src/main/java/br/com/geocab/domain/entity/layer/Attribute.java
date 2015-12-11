/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.marker.MarkerAttribute;

/**
 * @author Thiago Rossetto Afonso
 * @since 02/10/2014
 * @version 1.0
 */
@Entity
@Audited
@DataTransferObject(javascript = "Attribute")
public class Attribute extends AbstractEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 754889878712215160L;

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	@Transient
	private Long temporaryId;

	/**
	 * Name {@link Attribute}
	 */
	@NotNull
	private String name;

	/**
	 * Type {@link Attribute}
	 */
	@NotNull
	private AttributeType type;

	/**
	 * Required {@link Attribute}
	 */
	@NotNull
	private Boolean required;

	/**
	 * Visible {@link Attribute}
	 */
	@NotNull
	private Boolean visible;

	/**
	 * 
	 */
	private Boolean attributeDefault;

	/**
	 * Order {@link Attribute}
	 */
	@Column
	private Integer orderAttribute;
	/**
	 * Layer {@link Layer}
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = true, cascade =
	{ CascadeType.ALL })
	private Layer layer;

	/**
	 * 
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "attribute", fetch = FetchType.EAGER, cascade =
	{ CascadeType.REMOVE })
	private List<MarkerAttribute> markerAttribute = new ArrayList<MarkerAttribute>();

	/*-------------------------------------------------------------------
	 *								CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	public Attribute()
	{
		
	}

	/**
	 * 
	 * @param id
	 */
	public Attribute(Long id)
	{
		this.setId(id);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param required
	 * @param orderAttribute
	 * @param visible
	 */
	public Attribute(Long id, String name, AttributeType type, Boolean required, Integer orderAttribute, Boolean visible)
	{
		this.setId(id);
		this.setType(type);
		this.setName(name);
		this.setRequired(required);
		this.setOrderAttribute(orderAttribute);
		this.setVisible(visible);
	}
	
	// public Attribute(Long id, String name, AttributeType type, Boolean
	// required, Integer orderAttribute){
	//
	// }

	/**
	 * 
	 * @param name
	 */
	public Attribute(String name)
	{
		// this.setTemporaryId(id);
		this.setName(name);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param required
	 * @param type
	 * @param orderAttribute
	 * @param visible
	 */
	public Attribute(Long id, String name, Boolean required, AttributeType type, Integer orderAttribute, Boolean visible)
	{
		this.setId(id);
		this.setTemporaryId(id);
		this.setType(type);
		this.setName(name);
		this.setRequired(required);
		this.setOrderAttribute(orderAttribute);
		this.setVisible(visible);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param layer
	 */
	public Attribute(Long id, String name, AttributeType type, Layer layer){
		this.setId(id);
		this.setType(type);
		this.setName(name);
		this.setLayer(layer);
	}
	
	/*-------------------------------------------------------------------
	 *								BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	public String formmattedAttribute(){
		if (this.getType() == AttributeType.TEXT || this.getType() == AttributeType.DATE)
		{
			return "String";
		}
		else if (this.getType() == AttributeType.BOOLEAN)
		{
			return "Boolean";
		}
		else if (this.getType() != AttributeType.PHOTO_ALBUM)
		{
			return "Integer";
		}else // Se for do tipo PHOTO_ALBUM retorna null
		{
			return null;
		}
	}
	
	/*-------------------------------------------------------------------
	 *								SETTERS/GETTERS
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
	 * 
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public AttributeType getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(AttributeType type)
	{
		this.type = type;
	}

	/**
	 * @return the layer
	 */
	public Layer getLayer()
	{
		return layer;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setLayer(Layer layer)
	{
		this.layer = layer;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired()
	{
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(Boolean required)
	{
		this.required = required;
	}

	/**
	 * @return the attributeDefault
	 */
	public Boolean getAttributeDefault()
	{
		return attributeDefault;
	}

	/**
	 * @param attributeDefault
	 *            the attributeDefault to set
	 */
	public void setAttributeDefault(Boolean attributeDefault)
	{
		this.attributeDefault = attributeDefault;
	}

	/**
	 * @return the temporaryId
	 */
	public Long getTemporaryId()
	{
		return temporaryId;
	}

	/**
	 * @param temporaryId
	 *            the temporaryId to set
	 */
	public void setTemporaryId(Long temporaryId)
	{
		this.temporaryId = temporaryId;
	}

	/**
	 * @return the orderAttribute
	 */
	public Integer getOrderAttribute()
	{
		return orderAttribute;
	}

	/**
	 * @param orderAttribute
	 *            the orderAttribute to set
	 */
	public void setOrderAttribute(Integer orderAttribute)
	{
		this.orderAttribute = orderAttribute;
	}

	/**
	 * @return the visible
	 */
	public Boolean getVisible()
	{
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(Boolean visible)
	{
		this.visible = visible;
	}

	

}

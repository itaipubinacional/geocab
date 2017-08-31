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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.springframework.util.Assert;

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
	@NotNull(message = "admin.layer-config.The-all-fields-name-in-attributes-must-be-set")
	private String name;

	/**
	 * Type {@link Attribute}
	 */
	@NotNull(message = "admin.layer-config.The-all-fields-type-in-attributes-must-be-set")
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
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	private Layer layer;

	/**
	 * 
	 */
	@JsonIgnore
	@Transient
	private List<MarkerAttribute> markerAttribute = new ArrayList<MarkerAttribute>();
	
	/**
	 * 
	 */
	@OneToMany( mappedBy = "attribute", cascade = { CascadeType.ALL } )
	private List<AttributeOption> options = new ArrayList<AttributeOption>();

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
	public Attribute(Long id, String name, AttributeType type, Boolean required,
			Integer orderAttribute, Boolean visible)
	{
		this.setId(id);
		this.setType(type);
		this.setName(name);
		this.setRequired(required);
		this.setOrderAttribute(orderAttribute);
		this.setVisible(visible);
	}

	/**
	 * 
	 * @param name
	 */
	public Attribute(String name)
	{
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
	public Attribute(Long id, String name, AttributeType type, Layer layer)
	{
		this.setId(id);
		this.setType(type);
		this.setName(name);
		this.setLayer(layer);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param layer
	 */
	public Attribute(Long id, String name, String type, Layer layer)
	{
		this.setId(id);
		this.setType(formmatAttributes(type));
		this.setName(name);
		this.setLayer(layer);
	}

	/*-------------------------------------------------------------------
	 *								BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Formata os atributos para importação
	 * @return
	 */
	public void validate()
	{
		Assert.notNull(this.getType(), "admin.layer-config.The-all-fields-type-in-attributes-must-be-set");
	}
	
	/**
	 * Formata os atributos para importação
	 * @return
	 */
	public String formmattedTypeAttributes()
	{
		if (this.getType() == AttributeType.TEXT)
		{
			return "String";
		}
		else if (this.getType() == AttributeType.DATE)
		{
			return "Date";
		}
		else if (this.getType() == AttributeType.BOOLEAN)
		{
			return "Boolean";
		}
		else if (this.getType() != AttributeType.PHOTO_ALBUM)
		{
			return "Integer";
		}
		else // Se for do tipo PHOTO_ALBUM retorna null
		{
			return null;
		}
	}
	
	/**
	 * Formata o nome do atributo para exportação (A documentação informa que o nome não pode ser maior que 15 caracteres)
	 * @param type
	 * @return
	 */
	public void formmatNameAttribute()
	{
		if (this.getName().length() >= 10)
		{
			this.setName(this.getName().substring(0, 5) + "...");
		}
	}
	
	/**
	 * Formata os atributos para exportação
	 * @param type
	 * @return
	 */
	private static AttributeType formmatAttributes(String type)
	{
		AttributeType attributeType = null;
		if (type.contains("java.lang.String"))
		{
			attributeType = AttributeType.TEXT;
		}
		else if (type.contains("java.lang.Integer") || type.contains("java.lang.Long"))
		{
			attributeType = AttributeType.NUMBER;
		}
		else if (type.contains("java.util.Date"))
		{
			attributeType = AttributeType.DATE;
		}
		else if (type.contains("java.lang.Boolean"))
		{
			attributeType = AttributeType.BOOLEAN;
		}
		return attributeType;
	}
	
	/**
	 * @return the required
	 */
	public Boolean getRequired()
	{
		if (required == null)
		{
			required = false;
		}
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(Boolean required)
	{
		if (required == null)
		{
			required = false;
		}
		this.required = required;
	}
	
	/**
	 * @return the visible
	 */
	public Boolean getVisible()
	{
		if (visible == null)
		{
			visible = false;
		}
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(Boolean visible)
	{
		if (visible == null)
		{
			visible = false;
		}
		this.visible = visible;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attributeDefault == null) ? 0 : attributeDefault.hashCode());
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
		result = prime * result + ((markerAttribute == null) ? 0 : markerAttribute.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orderAttribute == null) ? 0 : orderAttribute.hashCode());
		result = prime * result + ((required == null) ? 0 : required.hashCode());
		result = prime * result + ((temporaryId == null) ? 0 : temporaryId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((visible == null) ? 0 : visible.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (attributeDefault == null) {
			if (other.attributeDefault != null)
				return false;
		} else if (!attributeDefault.equals(other.attributeDefault))
			return false;
		if (layer == null) {
			if (other.layer != null)
				return false;
		} else if (!layer.equals(other.layer))
			return false;
		if (markerAttribute == null) {
			if (other.markerAttribute != null)
				return false;
		} else if (!markerAttribute.equals(other.markerAttribute))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orderAttribute == null) {
			if (other.orderAttribute != null)
				return false;
		} else if (!orderAttribute.equals(other.orderAttribute))
			return false;
		if (required == null) {
			if (other.required != null)
				return false;
		} else if (!required.equals(other.required))
			return false;
		if (temporaryId == null) {
			if (other.temporaryId != null)
				return false;
		} else if (!temporaryId.equals(other.temporaryId))
			return false;
		if (type != other.type)
			return false;
		if (visible == null) {
			if (other.visible != null)
				return false;
		} else if (!visible.equals(other.visible))
			return false;
		return true;
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
	 * @param name
	 *            the name to set
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
	 * @return the options
	 */
	public List<AttributeOption> getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(List<AttributeOption> options) {
		this.options = options;
	}

}

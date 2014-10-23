/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.marker.MarkerAttribute;

/**
 * @author Thiago Rossetto Afonso
 * @since 02/10/2014
 * @version 1.0
 */
@Entity
@Audited
@DataTransferObject(javascript="Attribute")
@Table(schema=IEntity.SCHEMA)
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
	 * Name {@link Attribute}
	 * */
	@NotNull
	private String name;
	/**
	 * Type {@link Attribute}
	 * */
	@NotNull
	private AttributeType type;
	/**
	 * Required {@link Attribute}
	 * */
	@NotNull
	private Boolean required;
	
	private Boolean attributeDefault; 
	
	/**
	 * Layer {@link Layer}
	 * */
	@ManyToOne(fetch=FetchType.EAGER, optional=true, cascade={CascadeType.ALL})
	private Layer layer;
	
	@OneToMany(mappedBy="attribute", fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	private List<MarkerAttribute> markerAttribute = new ArrayList<MarkerAttribute>();

	public Attribute(){
		
	}
	
	public Attribute( Long id )
	{
		super(id);
	}
	
	public Attribute(Long id, String name, AttributeType type, Boolean required){
		super(id);
		this.setType(type);
		this.setName(name);
		this.setRequired(required);
	}
	
	public Attribute(Long id, String name, AttributeType type, Layer layer){
		super(id);
		this.setType(type);
		this.setName(name);
	    this.setLayer(layer);
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
	 * @return the type
	 */
	public AttributeType getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
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
	 * @param layer the layer to set
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
	 * @param required the required to set
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
	 * @param attributeDefault the attributeDefault to set
	 */
	public void setAttributeDefault(Boolean attributeDefault)
	{
		this.attributeDefault = attributeDefault;
	}
	
}

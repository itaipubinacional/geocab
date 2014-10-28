/**
 * 
 */
package br.com.geocab.entity;

import java.io.Serializable;

/**
 * @author Thiago Rossetto Afonso
 * @since 02/10/2014
 * @version 1.0
 */
public class Attribute implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 754889878712215160L;
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * Name {@link br.com.geocab.entity.Attribute}
	 * */
	private String name;
	/**
	 * Type {@link br.com.geocab.entity.Attribute}
	 * */
	private AttributeType type;
	/**
	 * Required {@link br.com.geocab.entity.Attribute}
	 * */
	private Boolean required;
	
	private Boolean attributeDefault;
	
	/**
	 * Layer {@link Layer}
	 * */
	private Layer layer;

	public Attribute(){
		
	}

    public Attribute(String name, AttributeType type, Boolean required, Boolean attributeDefault, Layer layer) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.attributeDefault = attributeDefault;
        this.layer = layer;
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

    public Boolean getAttributeDefault() {
        return attributeDefault;
    }

    public void setAttributeDefault(Boolean attributeDefault) {
        this.attributeDefault = attributeDefault;
    }
}

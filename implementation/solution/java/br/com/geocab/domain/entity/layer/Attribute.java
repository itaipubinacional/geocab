/**
 * 
 */
package br.com.geocab.domain.entity.layer;

/**
 * @author Thiago Rossetto Afonso
 * @since 02/10/2014
 * @version 1.0
 */
public class Attribute
{
	private String name;
	private AttributeType type;
	private Layer layer;
	
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
	
}

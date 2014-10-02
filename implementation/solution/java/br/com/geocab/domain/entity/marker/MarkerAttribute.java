/**
 * 
 */
package br.com.geocab.domain.entity.marker;

import br.com.geocab.domain.entity.layer.Attribute;;
/**
 * @author Thiago Rossetto Afonso
 * @since 02/10/2014
 * @version 1.0
 */
public class MarkerAttribute
{
	private String value;
	private Marker marker;
	private Attribute attribute;
	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
	/**
	 * @return the marker
	 */
	public Marker getMarker()
	{
		return marker;
	}
	/**
	 * @param marker the marker to set
	 */
	public void setMarker(Marker marker)
	{
		this.marker = marker;
	}
	/**
	 * @return the attribute
	 */
	public Attribute getAttribute()
	{
		return attribute;
	}
	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
	}
	
}

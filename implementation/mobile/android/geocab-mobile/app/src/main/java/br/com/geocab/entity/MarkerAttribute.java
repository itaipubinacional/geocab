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
public class MarkerAttribute implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7579901947534822117L;

    private Long id;

	private String value;

    private Layer layer;

	private Marker marker;

	private Attribute attribute;

	public MarkerAttribute(){
		
	}

    public MarkerAttribute(String value, Layer layer, Marker marker, Attribute attribute) {
        this.value = value;
        this.layer = layer;
        this.marker = marker;
        this.attribute = attribute;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
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

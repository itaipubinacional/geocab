/**
 * 
 */
package br.com.geocab.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 
 *  {@link br.com.geocab.entity.Marker}
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/10/2014
 * @version 1.0
 * @category Entity
 */
public class Marker implements Serializable
{


    /**
     * Id {@link Marker}
     */
    private Long id;

	private String latitude;

	private String longitude;

    private Bitmap image;
	
	private StatusMarker status;

    private User user;

    private Layer layer;

    private GregorianCalendar created;
	
	private List<MarkerAttribute> markerAttributes = new ArrayList<MarkerAttribute>();

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public Marker()
	{
		
	}

    public Marker(Long id, String latitude, String longitude, StatusMarker status, List<MarkerAttribute> markerAttributes) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.markerAttributes = markerAttributes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the latitude
	 */
	public String getLatitude()
	{
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude()
	{
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
	 * @return the status
	 */
	public StatusMarker getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusMarker status)
	{
		this.status = status;
	}

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    /**
	 * @return the markerAttributes
	 */
	public List<MarkerAttribute> getMarkerAttribute()
	{
		return markerAttributes;
	}

	/**
	 * @param markerAttributes the markerAttribute to set
	 */
	public void setMarkerAttribute(List<MarkerAttribute> markerAttributes)
	{
		this.markerAttributes = markerAttributes;
	}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GregorianCalendar getCreated() {
        return created;
    }

    public void setCreated(GregorianCalendar created) {
        this.created = created;
    }
}

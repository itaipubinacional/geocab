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

    private List<MarkerAttribute> markerAttributes = new ArrayList<MarkerAttribute>();

    private Calendar created;

    private String markerCreatedFormated;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public Marker(Long id, String markerCreatedFormated, User user)
	{
        this.id = id;
        this.markerCreatedFormated = markerCreatedFormated;
		this.user = user;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public StatusMarker getStatus() {
        return status;
    }

    public void setStatus(StatusMarker status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public List<MarkerAttribute> getMarkerAttributes() {
        return markerAttributes;
    }

    public void setMarkerAttributes(List<MarkerAttribute> markerAttributes) {
        this.markerAttributes = markerAttributes;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public String getMarkerCreatedFormated() {
        return markerCreatedFormated;
    }

    public void setMarkerCreatedFormated(String markerCreatedFormated) {
        this.markerCreatedFormated = markerCreatedFormated;
    }
}

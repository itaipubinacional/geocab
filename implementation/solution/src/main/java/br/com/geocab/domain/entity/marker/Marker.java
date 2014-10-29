/**
 * 
 */
package br.com.geocab.domain.entity.marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.io.FileTransfer;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.layer.Layer;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 
 *  {@link Marker}
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/10/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject(javascript="Marker")
@Table(schema=IEntity.SCHEMA)
public class Marker extends AbstractEntity implements Serializable
{
	
	/**
	 * 
	 */
	public static final String PICTURE_FOLDER = "/marker/%d";
	/**
	 * 
	 */
	public static final String PICTURE_PATH = PICTURE_FOLDER+"/%d";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1806026076674494131L;
	
	@Transient
	private FileTransfer image;
	
	@NotNull
	private String latitude;

	@NotNull
	private String longitude;
	
	@NotNull
	private StatusMarker status;
	
	private Boolean deleted;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	private Layer layer;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	private User user;
	
	@JsonManagedReference
	@OneToMany(mappedBy="marker", fetch=FetchType.EAGER, cascade={CascadeType.ALL})
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
	
	/**
	 * 
	 *
	 * @param id
	 */
	public Marker( Long id )
	{
		this.setId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param latitude
	 * @param longitude
	 * @param status
	 */
	public Marker( Long id, String latitude, String longitude, StatusMarker status)
	{
		this.setId(id);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setStatus(status);
	}

	/**
	 * 
	 * @param id
	 * @param latitude
	 * @param longitude
	 * @param status
	 * @param user
	 */
	public Marker( Long id, String latitude, String longitude, StatusMarker status, Calendar created, Layer layer, User user)
	{
		this.setId(id);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setStatus(status);
		user.setPassword("");
		user.setEmail("");
		this.setUser(user);
		this.setLayer(layer);
		this.setCreated(created);
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

	/**
	 * @return the image
	 */
	public FileTransfer getImage()
	{
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(FileTransfer image)
	{
		this.image = image;
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted()
	{
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}
	
}

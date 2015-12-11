/**
 * 
 */
package br.com.geocab.domain.entity.marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.envers.Audited;
import org.hibernate.spatial.GeometryType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;

/**
 * 
 * {@link Marker}
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/10/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject(javascript = "Marker")
@TypeDef(name = "geometry", typeClass = GeometryType.class)
public class Marker extends AbstractEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1806026076674494131L;
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public static final String PICTURE_FOLDER = "/marker/%d";
	/**
	 * 
	 */
	public static final String PICTURE_PATH = PICTURE_FOLDER + "/%d";
	/**
	 * 
	 */
	@Transient
	private Boolean imageToDelete;
	/**
	 * 
	 */
	@Transient
	private String wktCoordenate;
	/**
	 * 
	 */
	@Column(nullable = true)
	@Type(type = "org.hibernate.spatial.GeometryType")
	@JsonIgnore
	private Point location;
	/**
	 * 
	 */
	@NotNull
	private MarkerStatus status;
	/**
	 * 
	 */
	private Boolean deleted;
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	private Layer layer;
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	private User user;
	/**
	 * 
	 */
	@JsonManagedReference
	@OneToMany(mappedBy = "marker", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<MarkerAttribute> markerAttributes = new ArrayList<MarkerAttribute>();
	/**
	 * 
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "marker", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<MarkerModeration> markerModeration = new ArrayList<MarkerModeration>();

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public Marker()
	{
		super();
	}

	/**
	 * 
	 *
	 * @param id
	 */
	public Marker(Long id)
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
	public Marker(Long id, MarkerStatus status)
	{
		this.setId(id);
		this.setStatus(status);
	}

	/**
	 * 
	 * @param id
	 * @param latitude
	 * @param longitude
	 * @param status
	 */
	public Marker(Long id, MarkerStatus status, Geometry location, Layer layer)
	{
		this.setId(id);
		this.setStatus(status);
		this.setLocation((Point) location);
		this.setLayer(layer);
	}

	/**
	 * 
	 * @param id
	 * @param latitude
	 * @param longitude
	 * @param status
	 * @param user
	 */
	public Marker(Long id, MarkerStatus status, Calendar created, Layer layer,
			User user)
	{
		this.setId(id);
		this.setStatus(status);
		user.setPassword("");
		//user.setEmail("");
		this.setUser(user);
		this.setLayer(layer);
		this.setCreated(created);
	}
	
	/**
	 * 
	 * @param id
	 * @param status
	 * @param created
	 * @param layerId
	 * @param layerName
	 * @param layerTitle
	 * @param layerIcon
	 * @param layerPublished
	 * @param layerStartEnable
	 * @param layerStartVisible
	 * @param layerEnabled
	 * @param layerDataSource
	 * @param user
	 */
	public Marker( Long id, MarkerStatus status, Geometry location, Calendar created, Long layerId, String layerName, String layerTitle, String layerIcon, Boolean layerPublished, Boolean layerStartEnable, Boolean layerStartVisible, Boolean layerEnabled, User user)
	{
		this.setId(id);
		this.setStatus(status);
		user.setPassword("");
		this.setUser(user);
		this.setCreated(created);
		this.setLocation( (Point) location);
		
		Layer layer = new Layer(layerId, layerName, layerTitle, layerIcon, layerStartEnable, layerStartVisible, layerStartEnable, layerPublished, null, null, null, null);
		
		this.setLayer(layer);
	}
	
	/**
	 * 
	 * @param id
	 * @param status
	 * @param created
	 * @param location
	 */
	public Marker( Long id, MarkerStatus status, Calendar created, Geometry location)
	{
		this.setId(id);
		this.setStatus(status);
		user.setPassword("");
		user.setEmail("");
		layer.setMarkers(null);
		this.setLocation( (Point) location );
		this.setCreated(created);
	}

	/**
	 * 
	 * @param id
	 * @param latitudeGeometry
	 * @param longitude
	 * @param status
	 * @param created
	 * @param geom
	 * @param layer
	 * @param user
	 */
	public Marker(Long id, MarkerStatus status, Calendar created,
			Geometry location, Layer layer, User user)
	{
		this.setId(id);
		this.setStatus(status);
		user.setPassword("");
	//	user.setEmail("");
		this.setUser(user);
		layer.setMarkers(null);
		this.setLayer(layer);
		this.setLocation((Point) location);
		this.setCreated(created);
	}
	
	/*-------------------------------------------------------------------
	 *								BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/*-------------------------------------------------------------------
	 *						   SETTERS AND GETTERS
	 *-------------------------------------------------------------------*/
	
	
	/**
	 * @return the status
	 */
	public MarkerStatus getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(MarkerStatus status)
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
	 * @param layer
	 *            the layer to set
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
	 * @param markerAttributes
	 *            the markerAttribute to set
	 */
	public void setMarkerAttribute(List<MarkerAttribute> markerAttributes)
	{
		this.markerAttributes = markerAttributes;
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @param user
	 *            the user to set
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
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}

	/**
	 * @return the imageToDelete
	 */
	public Boolean getImageToDelete()
	{
		return imageToDelete;
	}

	/**
	 * @param imageToDelete
	 *            the imageToDelete to set
	 */
	public void setImageToDelete(Boolean imageToDelete)
	{
		this.imageToDelete = imageToDelete;
	}

	/**
	 * @return the wktCoordenate
	 */
	public String getWktCoordenate()
	{
		return wktCoordenate;
	}

	/**
	 * @param wktCoordenate
	 *            the wktCoordenate to set
	 */
	public void setWktCoordenate(String wktCoordenate)
	{
		this.wktCoordenate = wktCoordenate;
	}

	/**
	 * @return the location
	 */
	public Point getLocation()
	{
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Point location)
	{
		this.location = location;
	}

	/**
	 * @return the markerModeration
	 */
	public List<MarkerModeration> getMarkerModeration()
	{
		return markerModeration;
	}

	/**
	 * @param markerModeration
	 *            the markerModeration to set
	 */
	public void setMarkerModeration(List<MarkerModeration> markerModeration)
	{
		this.markerModeration = markerModeration;
	}

}

/**
 * 
 */
package br.com.geocab.domain.entity.marker;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vividsolutions.jts.geom.Geometry;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.AttributeType;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.layer.MapScale;
import br.com.geocab.domain.entity.marker.photo.PhotoAlbum;

/**
 * @author Thiago Rossetto Afonso
 * @since 02/10/2014
 * @version 1.0
 */
@Entity
@Audited
@DataTransferObject(javascript = "MarkerAttribute")
public class MarkerAttribute extends AbstractEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7579901947534822117L;
	/*-------------------------------------------------------------------
	 *							ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@NotNull
	private String value = "";
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "marker_id")
	@JsonBackReference
	private Marker marker;
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "attribute_id")
	private Attribute attribute;

	/**
	 * PhotoAlbum
	 */
	@OneToOne(optional = true, cascade = CascadeType.PERSIST )
	private PhotoAlbum photoAlbum;
	/*-------------------------------------------------------------------
	 *							CONSTRUCTOR
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public MarkerAttribute()
	{
		super();
	}
	
	/**
	 * 
	 */
	public MarkerAttribute(Long id)
	{
		super(id);
	}
	/**
	 * 
	 * @param id
	 * @param value
	 * @param marker
	 * @param attribute
	 */
	public MarkerAttribute(Long id, String value, Marker marker, Attribute attribute)
	{
		this.setId(id);
		this.setValue(value);
		this.setMarker(marker);
		this.setAttribute(attribute);
	}
	/**
	 * 
	 * @param id
	 * @param value
	 * @param marker
	 * @param attribute
	 */
	public MarkerAttribute(Long id, String value, Long attributeId, Long markerId, Geometry location, MarkerStatus markerStatus, Boolean markerDeleted, User user, 
			Long layerId, String layerName, String layerTitle, String layerIcon, Boolean startEnabled, Boolean startVisible, Integer orderLayer, MapScale minimumMapScale, MapScale maximumMapScale, Boolean enabled, DataSource dataSource)
	{
		
		this.setId(id);
		this.setValue(value);
		this.setMarker(new Marker(markerId, location, markerStatus, markerDeleted, user));
		this.getMarker().setLayer(new Layer(layerId, layerName, layerTitle, layerIcon, startEnabled, startVisible, orderLayer, minimumMapScale, maximumMapScale, enabled, dataSource));
		this.setAttribute(new Attribute(attributeId));
	}
	/**
	 * 
	 * @param id
	 * @param value
	 * @param markerId
	 * @param markerStatus
	 * @param markerCreated
	 * @param markerLayerId
	 * @param markerLayerName
	 * @param markerLayerTitle
	 * @param markerUserId
	 * @param markerUserName
	 * @param markerUserEmail
	 * @param markerUserStatus
	 * @param attributeId
	 * @param attributeName
	 * @param attributeType
	 * @param attributeRequired
	 * @param attributeVisible
	 * @param attributeOrder
	 */
	public MarkerAttribute(Long id, String value, 
			Long markerId, /*Geometry location,*/ MarkerStatus markerStatus, Calendar markerCreated,
			Long markerLayerId, String markerLayerName, String markerLayerTitle,  
			Long markerUserId, String markerUserName, String markerUserEmail, Boolean markerUserStatus,
			Long attributeId, String attributeName, AttributeType attributeType, Boolean attributeRequired, Boolean attributeVisible, Integer attributeOrder)
	{
		this.setId(id);
		this.setValue(value);
		
		Marker marker = new Marker();
		
		marker.setId(markerId);
		marker.setStatus(markerStatus);
		marker.setCreated(markerCreated);
		
		Layer layer = new Layer();
		layer.setId(markerLayerId);
		layer.setName(markerLayerName);
		layer.setTitle(markerLayerTitle);
		
		marker.setLayer(layer);
		
		User user = new User();
		user.setId(markerUserId);
		user.setName(markerUserName);
		user.setEmail(markerUserEmail);
		user.setEnabled(markerUserStatus);
		
		marker.setUser(user);
		
		this.setMarker(marker);
		
		Attribute attribute = new Attribute();
		attribute.setId(attributeId);
		attribute.setName(attributeName);
		attribute.setType(attributeType);
		attribute.setRequired(attributeRequired);
		attribute.setVisible(attributeVisible);
		attribute.setOrderAttribute(attributeOrder);
		
		this.setAttribute(attribute);
		
	}
	/*-------------------------------------------------------------------
	 *								BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * @return the value
	 */
	public String getValue()
	{
		if (this.value == null)
		{
			this.value = "";
		}
		return value;
	}
	
	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value)
	{	
		if (value == null)
		{
			value = "";
		}
		this.value = value;
	}
	
	/*-------------------------------------------------------------------
	 *							SETTERS AND GETTERS
	 *-------------------------------------------------------------------*/

	/**
	 * @return the marker
	 */
	public Marker getMarker()
	{
		return marker;
	}

	/**
	 * @param marker
	 *            the marker to set
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
	 * @param attribute
	 *            the attribute to set
	 */
	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
	}

	/**
	 * @return the photoAlbum
	 */
	public PhotoAlbum getPhotoAlbum()
	{
		return photoAlbum;
	}

	/**
	 * @param photoAlbum
	 *            the photoAlbum to set
	 */
	public void setPhotoAlbum(PhotoAlbum photoAlbum)
	{
		this.photoAlbum = photoAlbum;
	}

	

}

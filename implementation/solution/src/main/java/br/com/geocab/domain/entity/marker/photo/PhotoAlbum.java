/**
 * 
 */
package br.com.geocab.domain.entity.marker.photo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.loader.plan.build.internal.CascadeStyleLoadPlanBuildingAssociationVisitationStrategy;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.marker.MarkerAttribute;

/**
 * @author emanuelvictor
 *
 */
@Entity
@Audited
@DataTransferObject(javascript = "PhotoAlbum")
public class PhotoAlbum extends AbstractEntity implements Serializable
{
	/**
	 * 
	 */
	public static final String MARKER_FOLDER = "/markers/%d";

	/**
	 * 
	 */
	public static final String PHOTO_ALBUM_FOLDER =  MARKER_FOLDER + "/albuns/%d/photos";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7763138575022585976L;

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 * Um album de fotos deve estar vinculado á um atributo de uma camada
	 */
	@OneToOne(optional = false/*, cascade = CascadeType.ALL*/)
	private MarkerAttribute markerAttribute;

	/**
	 * Um album de fotos pode ter zero ou muitas fotos
	 */
//	@OneToMany(cascade = CascadeType.REMOVE)
	@Transient
	private List<Photo> photos;

	/**
	 * 
	 */
	@Column(length = 50, unique = true)
	private String identifier;

	/*-------------------------------------------------------------------
	 *								CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public PhotoAlbum()
	{
		super();
	}

	/**
	 * @param id
	 */
	public PhotoAlbum(Long id)
	{
		super(id);
	}

	/**
	 * @param markerAttribute
	 * @param photos
	 */
	public PhotoAlbum(MarkerAttribute markerAttribute, List<Photo> photos)
	{
		super();
		this.markerAttribute = new MarkerAttribute(markerAttribute.getId());
		this.photos = photos;
	}

	/**
	 * @param markerAttribute
	 * @param photos
	 * @param identifier
	 */
	public PhotoAlbum(MarkerAttribute markerAttribute, List<Photo> photos, String identifier)
	{
		super();
		this.markerAttribute = markerAttribute;
		this.photos = photos;
		this.identifier = identifier;
	}

	/*-------------------------------------------------------------------
	 *								BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * @return the identifier
	 */
	public String getIdentifier()
	{
		if (this.identifier == null)
		{
			generateIdentifier();
		}
		return this.identifier;
	}

	/**
	 * Gera o identificador da pasta do album de fotos
	 */
	private void generateIdentifier()
	{
		this.identifier = String.format( PhotoAlbum.PHOTO_ALBUM_FOLDER, this.getMarkerAttribute().getMarker().getId(), this.getId() );
	}

	/*-------------------------------------------------------------------
	 *								SETTERS/GETTERS
	 *-------------------------------------------------------------------*/
	/**
	 * @return the markerAttribute
	 */
	public MarkerAttribute getMarkerAttribute()
	{
		return markerAttribute;
	}

	/**
	 * @param markerAttribute
	 *            the markerAttribute to set
	 */
	public void setMarkerAttribute(MarkerAttribute markerAttribute)
	{
		this.markerAttribute = markerAttribute;
	}

	/**
	 * @return the photos
	 */
	public List<Photo> getPhotos()
	{
		return photos;
	}

	/**
	 * @param photos
	 *            the photos to set
	 */
	public void setPhotos(List<Photo> photos)
	{
		this.photos = photos;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

}

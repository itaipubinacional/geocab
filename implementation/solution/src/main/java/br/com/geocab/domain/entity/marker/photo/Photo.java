/**
 * 
 */
package br.com.geocab.domain.entity.marker.photo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.io.FileTransfer;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;

import br.com.geocab.domain.entity.AbstractEntity;

/**
 * @author emanuelvictor
 *
 */
@Entity
@Audited
@DataTransferObject(javascript = "Photo")
public class Photo extends AbstractEntity implements Serializable
{
	
	/**
	 * 
	 */
	public static final String PHOTO_PATH = "/%d";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8992340846549126943L;
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@ManyToOne(optional = false)
	private PhotoAlbum photoAlbum;
	
	/**
	 * 
	 */
	@Transient
	private FileTransfer image;
	
	/**
	 * 
	 */
	@Transient
	private String source;
	
	/**
	 * 
	 */
	@Transient
	private String mimeType;
	
	/**
	 * 
	 */
	@Transient
	private Integer contentLength;
	
	/**
	 * 
	 */
	@Transient
	private String name;
		
	/**
	 * 
	 */
	@Column(length = 60)
	@NotBlank
	private String description;
	
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
	public Photo()
	{
		super();
	}

	/**
	 * @param id
	 */
	public Photo(Long id)
	{
		super(id);
	}

	/**
	 * @param photoAlbum
	 * @param identifier
	 */
	public Photo(PhotoAlbum photoAlbum, String identifier)
	{
		super();
		this.photoAlbum = new PhotoAlbum(photoAlbum.getId());
		this.identifier = identifier;
	}

	/**
	 * @param photoAlbum
	 * @param source
	 * @param identifier
	 */
	public Photo(PhotoAlbum photoAlbum, String source, String identifier)
	{
		super();
		this.photoAlbum = photoAlbum;
		this.source = source;
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
	 * Gera o identificador do caminho da foto
	 */
	private void generateIdentifier()
	{
		this.getDescription();
		if (this.getId() != null)
		{
			this.identifier = this.getPhotoAlbum().getIdentifier() + String.format(PHOTO_PATH , this.getId());
		}
	}
	
	
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		if (this.description == null)
		{
			this.description = this.getName();
		}
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
		if (this.description == null)
		{
			this.description = this.getName();
		}
	}
	
	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier)
	{
		if (identifier != null)
		{
			this.identifier = identifier;
		}
		this.getIdentifier();
	}

	/*-------------------------------------------------------------------
	 *								SETTERS/GETTERS
	 *-------------------------------------------------------------------*/
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

	

	/**
	 * @return the source
	 */
	public String getSource()
	{
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source)
	{
		this.source = source;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType()
	{
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	/**
	 * @return the contentLength
	 */
	public Integer getContentLength()
	{
		return contentLength;
	}

	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(Integer contentLength)
	{
		this.contentLength = contentLength;
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

	

}

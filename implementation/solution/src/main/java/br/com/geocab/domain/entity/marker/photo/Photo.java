/**
 * 
 */
package br.com.geocab.domain.entity.marker.photo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.io.FileTransfer;
import org.hibernate.envers.Audited;

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
	private static final long serialVersionUID = 8992340846549126943L;
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@ManyToOne(optional = false)
	private PhotoAlbum photoAlbum;

	@Transient
	private FileTransfer image;
	/**
	 * 
	 */
	@Transient
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
	 * @param image
	 * @param identifier
	 */
	public Photo(PhotoAlbum photoAlbum, FileTransfer image, String identifier)
	{
		super();
		this.photoAlbum = photoAlbum;
		this.image = image;
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
	 * Gera o identificador do caminha da foto
	 */
	private void generateIdentifier()
	{
		if (this.getId() != null)
		{
			this.identifier = this.getPhotoAlbum().getIdentifier() + '/' + this.getId();
		}
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
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * @return the image
	 */
	public FileTransfer getImage()
	{
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(FileTransfer image)
	{
		this.image = image;
	}

}

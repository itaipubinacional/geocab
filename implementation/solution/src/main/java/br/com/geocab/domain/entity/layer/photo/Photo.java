/**
 * 
 */
package br.com.geocab.domain.entity.layer.photo;

import org.directwebremoting.annotations.DataTransferObject;

import br.com.geocab.domain.entity.MetaFile;

/**
 * @author emanuelvictor
 *
 */
// @Entity
// @Audited
// @Table(schema = IEntity.SCHEMA)
@DataTransferObject(javascript = "Photo")
public class Photo extends MetaFile// extends AbstractEntity implements
									// Serializable
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
	// @ManyToOne(optional = false)
	private PhotoAlbum photoAlbum;
	/**
	 * 
	 */
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
	public Photo(String id)
	{
		this.setId(id);
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
		this.identifier = this.getPhotoAlbum().getIdentifier() + '/'
				+ this.getId();
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

}

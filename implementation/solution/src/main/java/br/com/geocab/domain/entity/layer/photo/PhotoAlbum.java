/**
 * 
 */
package br.com.geocab.domain.entity.layer.photo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.layer.Attribute;

/**
 * @author emanuelvictor
 *
 */
@Entity
@Audited
@DataTransferObject(javascript = "PhotoAlbum")
@Table(schema = IEntity.SCHEMA)
public class PhotoAlbum extends AbstractEntity implements Serializable
{

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
	@OneToOne(optional = false)
	private Attribute attribute;

	/**
	 * Um album de fotos pode ter zero ou muitas fotos
	 */
	@Transient
	private Set<Photo> photos;

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
	 * @param attribute
	 * @param photos
	 */
	public PhotoAlbum(Attribute attribute, Set<Photo> photos)
	{
		super();
		this.attribute = new Attribute(attribute.getId());
		this.photos = photos;
	}

	/**
	 * @param attribute
	 * @param photos
	 * @param identifier
	 */
	public PhotoAlbum(Attribute attribute, Set<Photo> photos, String identifier)
	{
		super();
		this.attribute = attribute;
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
		this.identifier = this.getAttribute().getLayer().getId().toString();
		this.identifier = this.identifier + '/'
				+ this.getAttribute().getId().toString();
		this.identifier = this.identifier + '/' + this.getId().toString();
	}

	/*-------------------------------------------------------------------
	 *								SETTERS/GETTERS
	 *-------------------------------------------------------------------*/
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
	 * @return the photos
	 */
	public Set<Photo> getPhotos()
	{
		return photos;
	}

	/**
	 * @param photos
	 *            the photos to set
	 */
	public void setPhotos(Set<Photo> photos)
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

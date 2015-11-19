package br.com.geocab.tests.entity;

import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.layer.photo.Photo;
import br.com.geocab.domain.entity.layer.photo.PhotoAlbum;

/**
 *
 * @author Cristiano Correa @since 09/05/2013 @version 1.0 @category
 */
public class PhotoAlbumTest
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/

	@Test
	public void testGenerateIdentifierPhotoAlbum()
	{
		Layer layer = new Layer(100L);

		Attribute attribute = new Attribute(200L);
		attribute.setLayer(layer);

		PhotoAlbum photoAlbum = new PhotoAlbum(300L);
		photoAlbum.setAttribute(attribute);

		Assert.equals("100/200/300", photoAlbum.getIdentifier());
	}

	@Test
	public void testGetIdentifierPhoto()
	{
		Layer layer = new Layer(100L);

		Attribute attribute = new Attribute(200L);
		attribute.setLayer(layer);

		PhotoAlbum photoAlbum = new PhotoAlbum(300L);
		photoAlbum.setAttribute(attribute);

		Photo photo = new Photo("400");
		photo.setPhotoAlbum(photoAlbum);

		Assert.equals("100/200/300/400", photo.getIdentifier());
	}

}

package br.com.geocab.tests.entity;

import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.photo.Photo;
import br.com.geocab.domain.entity.marker.photo.PhotoAlbum;

/**
 * 
 * @author emanuelvictor
 *
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
	public void testGenerateIdentifierPhotoAlbumWithOutPhotoFolder()
	{
		Marker marker = new Marker(100L);

		MarkerAttribute markerAttribute = new MarkerAttribute(200L);
		markerAttribute.setMarker(marker);

		PhotoAlbum photoAlbum = new PhotoAlbum(300L);
		photoAlbum.setMarkerAttribute(markerAttribute);

		Assert.equals("/markers/100/albuns/300/photos", photoAlbum.getIdentifier());
	}

	@Test
	public void testGetIdentifierPhoto()
	{
		Marker marker = new Marker(100L);

		MarkerAttribute attribute = new MarkerAttribute(200L);
		attribute.setMarker(marker);

		PhotoAlbum photoAlbum = new PhotoAlbum(300L);
		photoAlbum.setMarkerAttribute(attribute);

		Photo photo = new Photo(400L);
		photo.setPhotoAlbum(photoAlbum);

		
		Assert.equals("/markers/100/albuns/300/photos/400", photo.getIdentifier());
	}

}

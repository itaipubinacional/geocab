/**
 * 
 */
package br.com.geocab.domain.service.marker;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.jcr.RepositoryException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.AttributeType;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.photo.Photo;
import br.com.geocab.domain.entity.marker.photo.PhotoAlbum;
import br.com.geocab.domain.repository.IMetaFileRepository;
import br.com.geocab.domain.repository.attribute.IAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;
import br.com.geocab.domain.repository.marker.photo.IPhotoAlbumRepository;
import br.com.geocab.domain.repository.marker.photo.IPhotoRepository;
import br.com.geocab.domain.repository.markermoderation.IMarkerModerationRepository;
import br.com.geocab.domain.service.DataSourceService;

/**
 * @author emanuelvictor
 *
 */
public abstract class AbstractMarkerService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Log
	 */
	protected static final Logger LOG = Logger.getLogger(DataSourceService.class.getName());
	/**
	 * Repository of {@link DataSource}
	 */
	@Autowired
	protected IMarkerRepository markerRepository;
	/**
	 * 
	 */
	@Autowired
	protected IMarkerAttributeRepository markerAttributeRepository;
	/**
	 * 
	 */
	@Autowired
	protected IMarkerModerationRepository markerModerationRepository;
	/**
	 * 
	 */
	@Autowired
	protected IPhotoAlbumRepository photoAlbumRepository;
	/**
	 * 
	 */
	@Autowired
	protected IPhotoRepository photoRepository;
	/**
	 * I18n
	*/
	@Autowired
	protected MessageSource messages;
	/**
	 * 
	 */
	@Autowired
	protected IAttributeRepository attributeRepository;
	/**
	 * 
	 */
	@Autowired
	protected IMetaFileRepository metaFileRepository;
	
	
	/**
	 * Salva todos os atributos de um ponto
	 * 
	 * @param marker
	 * @return
	 * @throws RepositoryException 
	 * @throws IOException 
	 */
	public List<MarkerAttribute> insertMarkersAttributes(List<MarkerAttribute> markersAttributes)
	{

		for (MarkerAttribute markerAttribute : markersAttributes)
		{
			if (markerAttribute.getValue() != null)
			{				
				if (markerAttribute.getAttribute().getType() == AttributeType.PHOTO_ALBUM && markerAttribute.getPhotoAlbum() != null)
				{
					List<Photo> photos = markerAttribute.getPhotoAlbum().getPhotos();
					markerAttribute = this.markerAttributeRepository.save(markerAttribute);
					
					markerAttribute.getPhotoAlbum().setPhotos(photos);
					markerAttribute.getPhotoAlbum().setMarkerAttribute(markerAttribute);
					
					markerAttribute.setPhotoAlbum(this.insertPhotoAlbum(markerAttribute.getPhotoAlbum()));
				} 
				else 
				{
					markerAttribute = this.markerAttributeRepository.save(markerAttribute);
				}
			}
		}
		
		for (MarkerAttribute markerAttribute : markersAttributes)
		{
			if (markerAttribute.getAttribute().getType() == AttributeType.PHOTO_ALBUM && markerAttribute.getPhotoAlbum() != null)
			{
				PhotoAlbum photoAlbum = markerAttribute.getPhotoAlbum();
				if (photoAlbum.getPhotos().size() == 0)
				{
					markerAttribute = markerAttributeRepository.findOne(photoAlbum.getMarkerAttribute().getId());
					markerAttribute.setPhotoAlbum(null);
					markerAttributeRepository.save(markerAttribute);
					
					photoAlbumRepository.delete(photoAlbum.getId());
					
					markerAttributeRepository.delete(markerAttribute.getId());
				}
			}
		}
		
		return markersAttributes;
	}
	
	/**
	 * Salva todos os albuns de fotos no banco de dados e todas as fotos nos
	 * sistemas de arquivos
	 * 
	 * @param marker
	 * @return
	 * @throws RepositoryException 
	 * @throws IOException 
	 */
	public PhotoAlbum insertPhotoAlbum(PhotoAlbum photoAlbum)
	{
		// Caso não haja o foto_album dentro da foto, seta lá então.
		// Caso seja uma inserção de um album de fotos ou uma atualização de um
		// album de fotos
		if (photoAlbum.getPhotos() != null)
		{
			List<Photo> photos = photoAlbum.getPhotos();
			
			photoAlbum = photoAlbumRepository.save(photoAlbum);	
			
			photoAlbum.setPhotos(photos);
			
			for (Photo photo : photoAlbum.getPhotos())
			{
				photo.setPhotoAlbum(photoAlbum);
			}
			photoAlbum.setPhotos(this.uploadPhoto(photoAlbum));
		}
		return photoAlbum;
		
	}
	
	/**
	 * Salva todas as fotos no sistema de arquivos
	 * @param photos
	 * @return
	 */
	public List<Photo> uploadPhoto(PhotoAlbum photoAlbum)
	{
		
	    List<Photo> photos = photoAlbum.getPhotos();
	    
    	List<Photo> photosDatabase = this.photoRepository.findByIdentifierContaining(photoAlbum.getIdentifier(), null).getContent();
    	//Handler para deletar fotos
		for (Photo photoDatabase : photosDatabase)
		{
			boolean photoToExclude = true;
			for (Photo photo : photos)
			{
				if (photoDatabase.getId().equals(photo.getId()) )
				{
					photoToExclude = false;
				}
			}
			if (photoToExclude)
			{
				MetaFile metaFile = null;	
				try
				{
					metaFile = this.metaFileRepository.findByPath( photoDatabase.getIdentifier(), true);
				}
				catch (RepositoryException e)
				{
					e.printStackTrace();
					LOG.info(e.getMessage());
				}
				this.removeImg(metaFile.getId());
				this.photoRepository.delete(photoDatabase.getId());
				
			}
		}
		
		// Album de fotos já existente
		if (photos.size() > 0)
		{
			for (Photo photo : photos)
			{
				if (photo.getId() != null)
				{
					// Se não é uma foto nova só atualiza a foto no banco de dados
					photo = this.photoRepository.save(photo);
				}
				else
				{
					// Se é uma foto nova, salva a foto no banco de dados e no sistema de arquivos
					photo = this.photoRepository.save(photo);
					photo = this.uploadImg(photo);
				}
			}
		}
		
		//Se o photoALbum não tem fotos deleta o mesmo
		return this.photoRepository.findByIdentifierContaining(photoAlbum.getIdentifier(), null).getContent();
	}
	
	/**
	 * Salva uma foto e devolve o objeto foto
	 * @param photo
	 * @return
	 */
	public Photo uploadImg(Photo photo)
	{

		try
		{
			if (photo.getSource() != null)
			{
				Base64 photoDecode = new Base64();
				
				byte[] data = photoDecode.decode(photo.getSource());
				InputStream decodedMap = new ByteArrayInputStream(data);	
				
				final String mimeType = photo.getMimeType();
		
				final List<String> validMimeTypes = new ArrayList<String>();
				validMimeTypes.add("image/gif");
				validMimeTypes.add("image/jpeg");
				validMimeTypes.add("image/bmp");
				validMimeTypes.add("image/png");
		
				if (mimeType == null || !validMimeTypes.contains(mimeType))
				{
					throw new IllegalArgumentException("Formato inválido!");
				}
		
				InputStream is = new BufferedInputStream(decodedMap);
				final BufferedImage bufferedImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
				Image image = ImageIO.read(is);
				Graphics2D g = bufferedImage.createGraphics();
				g.drawImage(image, 0, 0, 640, 480, null);
				g.dispose();
		
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "png", os);
				InputStream isteam = new ByteArrayInputStream(os.toByteArray());
		
				MetaFile metaFile = new MetaFile();
	
				// Gera o identificador
				photo.getIdentifier();
				
				metaFile.setId(String.valueOf(photo.getId()));
				metaFile.setContentType(photo.getMimeType());
				metaFile.setContentLength(photo.getContentLength());
				metaFile.setFolder(photo.getPhotoAlbum().getIdentifier());
				metaFile.setInputStream(isteam);
				metaFile.setName(photo.getName());
		
				this.metaFileRepository.insert(metaFile);
			
			}
		
		}
		catch (IOException | RepositoryException e)
		{
			e.printStackTrace();
			LOG.info(e.getMessage());
		}

		return photo;
	}
	
	/**
	 * 
	 * @param metaFileId
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public void removeImg(String metaFileId) 
	{
		try
		{
			this.metaFileRepository.remove(metaFileId);
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
			LOG.info(e.getMessage());
		}
	}
	
	/**
	 * Valida os atributos a serem inseridos, caso o atributo seja "required" e não estiver setado, estoura exceção
	 * @param markerAttributes
	 */
	protected void validateAttribute(List<MarkerAttribute> markerAttributes)
	{
		for (int i = 0; i < markerAttributes.size(); i++)
		{
			MarkerAttribute markerAttribute = markerAttributes.get(i);
			
			Attribute attribute = attributeRepository.findOne(markerAttribute.getAttribute().getId());
			
			if (attribute.getRequired() && attribute.getType() != AttributeType.PHOTO_ALBUM && markerAttribute.getValue() == null)
			{
				throw new RuntimeException(messages.getMessage("admin.shape.error.value-attribute-can-not-be-null", null, null));
			}
			else if (attribute.getRequired() && attribute.getType() == AttributeType.PHOTO_ALBUM && (markerAttribute.getPhotoAlbum() == null || markerAttribute.getPhotoAlbum().getPhotos() == null || markerAttribute.getPhotoAlbum().getPhotos().size() == 0))
			{
				throw new RuntimeException(messages.getMessage("photos.Insert-Photos-in-attribute", new Object [] {attribute.getName()}, null));
			}
		}
	}


}

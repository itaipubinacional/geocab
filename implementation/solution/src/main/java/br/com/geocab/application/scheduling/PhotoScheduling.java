/**
 * 
 */
package br.com.geocab.application.scheduling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.transaction.Transactional;

import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.AttributeType;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.photo.Photo;
import br.com.geocab.domain.entity.marker.photo.PhotoAlbum;
import br.com.geocab.domain.repository.attribute.IAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;
import br.com.geocab.domain.repository.marker.photo.IPhotoAlbumRepository;
import br.com.geocab.domain.repository.marker.photo.IPhotoRepository;
import br.com.geocab.infrastructure.jcr.MetaFileRepository;

/**
 * @author emanuelvictor
 *
 */
@Component
@Transactional
public class PhotoScheduling
{
	
	/**
	 * Log
	 */
//	private static final Logger LOG = Logger.getLogger(DataSourceService.class.getName());
	
	/**
	 * 
	 */
	@Autowired
	private IMarkerRepository markerRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IMarkerAttributeRepository markerAttributeRepository;
		
	/**
	 * 
	 */
	@Autowired
	private IPhotoAlbumRepository photoAlbumRepository;
	
	/**
	 * 
	 */
	@Autowired
	private MetaFileRepository metaFileRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IPhotoRepository photoRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IAttributeRepository attributeRepository;
	/**
	 * 
	 */
	@PostConstruct
	public void postConstruct()
	{
		for (Marker marker : markerRepository.listAll())
		{
			try
			{
				// Faz requisição de todos os marker_attributes do marker
				marker.setMarkerAttribute(markerAttributeRepository.listAttributeByMarker(marker.getId()));
				
				// Verifica se o marker tem fotos relacionadas diretamente a ele
				final FileTransfer fileTransfer = this.verifyMarker(marker);
				
				//Cria o atributo
				Attribute attribute= new Attribute(null, "Fotos", AttributeType.PHOTO_ALBUM, null);
				attribute.setVisible(false);
				attribute.setRequired(true);
				
				this.attributeRepository.save(attribute);
				
				// Cria o novo marker_attribute (que será o photo_album) a ser inserido
				MarkerAttribute markerAttribute = new MarkerAttribute();
				markerAttribute.setMarker(marker);
				markerAttribute.setAttribute(attribute);
				markerAttribute.setValue("");
				
				//Salva o marker_attribute
				markerAttribute = markerAttributeRepository.save(markerAttribute);
				
				// Cria o photo_album com o marker_attribute recém salvo
				PhotoAlbum photoAlbum = new PhotoAlbum();
				photoAlbum.setMarkerAttribute(markerAttribute);
				
				// Salva o photo_album 
				photoAlbum = photoAlbumRepository.save(photoAlbum);
				photoAlbum.getIdentifier(); // Já tem o id então pode criar o identificador (GJ)
				photoAlbum = photoAlbumRepository.save(photoAlbum);
				
				//Salva o marker_attribute
				markerAttribute.setPhotoAlbum(photoAlbum);
				markerAttribute = markerAttributeRepository.save(markerAttribute);
				
				// Cria o photo com o photo_album recém salvo
				Photo photo = new Photo();
				photo.setDescription(fileTransfer.getFilename());
				photo.setPhotoAlbum(photoAlbum);
				
				//Salva a foto
				photo = photoRepository.save(photo);
				photo.getIdentifier(); // Já tem o id então pode criar o identificador  (GJ)
				photo = photoRepository.save(photo);
				
				// Seta o metafile no objeto photo
				photo.setImage(fileTransfer);
				// Realiza o upload da foto
				photo = this.uploadImg(photo);
				
			}
			catch (RepositoryException | RuntimeException e)
			{
				continue;
			}
		}	
	}
	
	
	/**
	 * Salva uma foto e devolve o objeto foto
	 * @param photo
	 * @return
	 */
	private Photo uploadImg(Photo photo)
	{
		try
		{
			final String mimeType = photo.getImage().getMimeType();
	
			final List<String> validMimeTypes = new ArrayList<String>();
			validMimeTypes.add("image/gif");
			validMimeTypes.add("image/jpeg");
			validMimeTypes.add("image/bmp");
			validMimeTypes.add("image/png");
	
			if (mimeType == null || !validMimeTypes.contains(mimeType))
			{
				throw new IllegalArgumentException("Formato inválido!");
			}
	
			MetaFile metaFile = new MetaFile();
			
			metaFile.setId(String.valueOf(photo.getId()));
			metaFile.setContentType(photo.getImage().getMimeType());
			metaFile.setContentLength(photo.getImage().getSize());
			metaFile.setFolder(photo.getPhotoAlbum().getIdentifier());
			metaFile.setInputStream(photo.getImage().getInputStream());
			metaFile.setName(photo.getImage().getFilename());
	
			this.metaFileRepository.insert(metaFile);
		
		}
		catch (IOException | RepositoryException e)
		{
//			LOG.info(e.getMessage());
		}

		return photo;
	}
	
	/**
	 * Verifica se a postagem tem foto e se já tem o atributo album de fotos padrão
	 * @throws RepositoryException 
	 * @throws Exception 
	 */
	private FileTransfer verifyMarker(Marker marker) throws RuntimeException, RepositoryException
	{
		// Se já tiver photo_album não pode migrar		
		for (MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			if (markerAttribute.getAttribute().getType().equals(AttributeType.PHOTO_ALBUM) || markerAttribute.getAttribute().getType() == AttributeType.PHOTO_ALBUM)
			{
				throw new RuntimeException("Já tem album de photos");
			}
		}
		
		// Verifica se o marker tem foto, se não tiver estoura exceção
		final MetaFile metaFile = this.metaFileRepository.findByPath("/marker/" + marker.getId() + "/" + marker.getId(), true);
		
		Assert.isTrue(metaFile != null, "A postagem com id " +marker.getId()+ " não tem foto");
		
		return new FileTransfer(metaFile.getName(), metaFile.getContentType(), metaFile.getInputStream());
	}	
}

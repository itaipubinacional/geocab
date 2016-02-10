/**
 * 
 */
package br.com.geocab.application.scheduling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jcr.RepositoryException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.photo.Photo;
import br.com.geocab.domain.repository.marker.IMarkerAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;
import br.com.geocab.domain.repository.marker.photo.IPhotoAlbumRepository;
import br.com.geocab.domain.repository.marker.photo.IPhotoRepository;
import br.com.geocab.domain.service.DataSourceService;
import br.com.geocab.infrastructure.jcr.MetaFileRepository;

/**
 * @author emanuelvictor
 *
 */
@Component
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class PhotoScheduling
{
	
	/**
	 * Log
	 */
	private static final Logger LOG = Logger.getLogger(DataSourceService.class.getName());
	
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
	
//	/**
//	 * 
//	 */
//	@PostConstruct
//	public void postConstruct()
//	{
//		for (Marker marker : markerRepository.listAll())
//		{
//			
//			try
//			{
//				
//				this.verifyMarker(marker);
//											
//				marker.setMarkerAttribute(markerAttributeRepository.listAttributeByMarker(marker.getId()));
//				
//				MarkerAttribute markerAttribute = new MarkerAttribute();
//				markerAttribute.setMarker(marker);
//				markerAttribute.setValue("Default photo album");
//				
//				markerAttribute = markerAttributeRepository.save(markerAttribute);
//				
//				
//				PhotoAlbum photoAlbum = new PhotoAlbum();
//				photoAlbum.setMarkerAttribute(markerAttribute);
//				photoAlbum.getIdentifier();
//				
//				photoAlbum = photoAlbumRepository.save(photoAlbum);
//				
//				Photo photo = new Photo();
//				photo.setDescription("Default description");
//				photo.setPhotoAlbum(photoAlbum);
//				photo.getIdentifier();
//				final MetaFile metaFile = this.metaFileRepository.findByPath("/marker/" + marker.getId() + "/" + marker.getId(), true);
//				photo.setImage(new FileTransfer(metaFile.getName(),
//						metaFile.getContentType(), metaFile.getInputStream()));
//				
//				photo = photoRepository.save(photo);
//				
//				photo = this.uploadImg(photo);
//				
//			}
//			catch (RepositoryException | RuntimeException e)
//			{
//				e.printStackTrace();
//			}
//		}	
//	}
	
	
	/**
	 * Salva uma foto e devolve o objeto foto
	 * @param photo
	 * @return
	 */
	private Photo uploadImg(Photo photo)
	{

		try
		{
			
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
	
			MetaFile metaFile = new MetaFile();
			
			metaFile.setId(String.valueOf(photo.getId()));
			metaFile.setContentType(photo.getMimeType());
			metaFile.setContentLength(photo.getContentLength());
			metaFile.setFolder(photo.getPhotoAlbum().getIdentifier());
			metaFile.setInputStream(photo.getImage().getInputStream());
			metaFile.setName(photo.getName());
	
			this.metaFileRepository.insert(metaFile);
		
		}
		catch (IOException | RepositoryException e)
		{
			e.printStackTrace();
			LOG.info(e.getMessage());
		}

		return photo;
	}
	
	/**
	 * Verifica se a postagem tem foto e se já tem o atributo album de fotos padrão
	 * @throws RepositoryException 
	 * @throws Exception 
	 */
	private void verifyMarker(Marker marker) throws RuntimeException, RepositoryException
	{
		// Verifica se já tem o atributo album de fotos padrão e foto padrão
		for (MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			if (markerAttribute.getValue() == "Default photo album")
			{
				throw new RuntimeException("Já foi migrado");
			}
		}
		// Verifica se o marker tem foto, se não tiver estoura exceção
		this.metaFileRepository.findByPath("/marker/" + marker.getId() + "/" + marker.getId(), true);
	}	
}

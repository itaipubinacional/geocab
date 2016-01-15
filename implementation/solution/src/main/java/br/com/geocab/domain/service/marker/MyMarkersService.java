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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.layer.AttributeType;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.marker.photo.Photo;
import br.com.geocab.domain.entity.marker.photo.PhotoAlbum;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.domain.repository.IMetaFileRepository;
import br.com.geocab.domain.repository.marker.IMarkerAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;
import br.com.geocab.domain.repository.marker.photo.IPhotoAlbumRepository;
import br.com.geocab.domain.repository.marker.photo.IPhotoRepository;
import br.com.geocab.domain.repository.markermoderation.IMarkerModerationRepository;
import br.com.geocab.domain.service.DataSourceService;

@Service
@Transactional
@RemoteProxy(name = "myMarkersService")
public class MyMarkersService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Log
	 */
	private static final Logger LOG = Logger
			.getLogger(DataSourceService.class.getName());

	/**
	 * Repository of {@link DataSource}
	 */
	@Autowired
	private IMarkerRepository markerRepository;

	/**
	 * 
	 */
	@Autowired
	private IPhotoAlbumRepository photoAlbumRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IPhotoRepository photoRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IMarkerAttributeRepository markerAttributeRepository;

	/**
	 * 
	 */
	@Autowired
	private IMarkerModerationRepository markerModerationRepository;

	// @Autowired
	// private MessageSource messages;

	@Autowired
	private IMetaFileRepository metaFileRepository;

	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * 
	 * /** Method to update an {@link Marker}
	 * 
	 * @param Marker
	 * @return Marker
	 * @throws RepositoryException
	 * @throws IOException
	 */

	public Marker updateMarker(Marker marker) throws IOException, RepositoryException
	{
		try
		{
			Marker markerTemporary = this.markerRepository.findOne(marker.getId());

//			if (!markerTemporary.getLayer().getId().equals(marker.getLayer().getId()))
//			{
//				List<MarkerAttribute> markerAttributes = this.markerAttributeRepository.listAttributeByMarker(marker.getId());
//
//				if (markerAttributes != null)
//				{
//					this.markerAttributeRepository.deleteInBatch(markerAttributes);
//				}
//			}

			marker.setLocation(markerTemporary.getLocation());

			MarkerModeration markerModeration = new MarkerModeration();
			markerModeration.setMarker(marker);

			markerModeration.setStatus(marker.getStatus());
			
			marker.setMarkerAttribute(this.insertMarkersAttributes(marker.getMarkerAttribute()));

			this.markerModerationRepository.save(markerModeration);

			marker = this.markerRepository.save(marker);
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
		}
		return marker;
	}

	/**
	 * Method to update an {@link Marker}
	 * 
	 * @param Marker
	 * @return Marker
	 * @throws RepositoryException
	 * @throws IOException
	 */

	public Marker postMarker(Marker marker)
			throws IOException, RepositoryException
	{
		try
		{
			if (marker.getStatus() == MarkerStatus.SAVED
					|| marker.getStatus() == MarkerStatus.REFUSED)
			{

				Marker markerTemporary = this.markerRepository
						.findOne(marker.getId());

				if (markerTemporary.getLayer().getId() != marker.getLayer()
						.getId())
				{
					List<MarkerAttribute> markerAttributes = this.markerAttributeRepository
							.listAttributeByMarker(marker.getId());

					if (markerAttributes != null)
					{
						this.markerAttributeRepository
								.deleteInBatch(markerAttributes);
					}
				}

				marker.setLocation(markerTemporary.getLocation());

				marker.setStatus(MarkerStatus.PENDING);

				MarkerModeration markerModeration = new MarkerModeration();
				markerModeration.setMarker(marker);

				markerModeration.setStatus(marker.getStatus());

				this.markerModerationRepository.save(markerModeration);

				
				marker = this.markerRepository.save(marker);
			}
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
		}
		return marker;
	}
	
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
				/*markerAttribute = */this.markerAttributeRepository.save(markerAttribute);
				
				if (markerAttribute.getAttribute().getType() == AttributeType.PHOTO_ALBUM)
				{
					markerAttribute.getPhotoAlbum().setMarkerAttribute(markerAttribute);
					
					markerAttribute.setPhotoAlbum(this.insertPhotoAlbum(markerAttribute.getPhotoAlbum()));
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
	{try
	{
		/*photoAlbum =*/ photoAlbumRepository.save(photoAlbum);
	}
	catch (Exception e)
	{e.printStackTrace();
		// TODO: handle exception
	}
		
		// Caso não haja o foto_album dentro da foto, seta lá então.
		// Caso seja uma inserção de um album de fotos ou uma atualização de um
		// album de fotos
		for (Photo photo : photoAlbum.getPhotos())
		{
			if (photo.getPhotoAlbum() == null)
			{
				photo.setPhotoAlbum(photoAlbum);
			}
		}
		this.uploadPhoto(photoAlbum.getPhotos());
		return photoAlbum;
	}
	
	/**
	 * Salva todas as fotos no sistema de arquivos
	 * @param photos
	 * @return
	 */
	public List<Photo> uploadPhoto(List<Photo> photos)
	{
		for (Photo photo : photos)
		{
			photo = this.photoRepository.save(photo);

			photo = this.uploadImg(photo);
		}
		return photos;
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
	 * Method to remove markers
	 * 
	 */
	public void removeMarkers(List<Long> markersId) throws IOException, RepositoryException
	{
		try
		{
			for (Long markerId : markersId)
			{
				this.removeMarker(markerId);
			}
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
		}
	}

	/**
	 * Method to update markers
	 * 
	 */

	public void postMarkers(List<Long> markersId)
			throws IOException, RepositoryException
	{
		try
		{
			for (Long markerId : markersId)
			{
				Marker marker = new Marker(markerId);
				marker = this.markerRepository.findOne(markerId);
				this.postMarker(marker);
			}
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
		}
	}

	/**
	 * Method to remove an {@link Marker}
	 * 
	 * @param id
	 */
	public void removeMarker(Long id)
	{
		Marker marker = this.findMarkerById(id);
		marker.setDeleted(true);
		this.markerRepository.save(marker);
	}

	/**
	 * Method to find an {@link Marker} by id
	 * 
	 * @param id
	 * @return marker
	 * @throws JAXBException
	 */
	@Transactional(readOnly = true)
	public Marker findMarkerById(Long id)
	{
		return this.markerRepository.findOne(id);
	}

	public User getUserMe()
	{
		return ContextHolder.getAuthenticatedUser();
	}

	/**
	 * Method to find an {@link Marker} by layer
	 * 
	 * @param layerId
	 * @return marker List
	 * @throws JAXBException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listMarkerByLayerFilters(Long layerId)
	{
		final User user = ContextHolder.getAuthenticatedUser();

		List<Marker> listMarker = null;

		if (!user.equals(User.ANONYMOUS))
		{

			if (user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE)
					|| user.getRole().name().equals(UserRole.MODERATOR_VALUE))
			{
				listMarker = this.markerRepository
						.listMarkerByLayerAll(layerId);
			}
			else
			{
				listMarker = this.markerRepository.listMarkerByLayer(layerId,
						user.getId());
			}

		}
		else
		{
			listMarker = this.markerRepository.listMarkerByLayerPublic(layerId);
		}

		for (Marker marker : listMarker)
		{
			marker.setMarkerAttribute(listAttributeByMarker(marker.getId()));
		}

		return listMarker;
	}

	/**
	 * Method to find an {@link Marker} by layer
	 * 
	 * @param layerId
	 * @return marker List
	 * @throws JAXBException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listMarkerByLayer(Long layerId)
	{
		final User user = ContextHolder.getAuthenticatedUser();

		List<Marker> listMarker = null;

		if (!user.equals(User.ANONYMOUS))
		{

			if (user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE)
					|| user.getRole().name().equals(UserRole.MODERATOR_VALUE))
			{
				listMarker = this.markerRepository
						.listMarkerByLayerAll(layerId);
			}
			else
			{
				listMarker = this.markerRepository.listMarkerByLayer(layerId,
						user.getId());
			}

		}
		else
		{
			listMarker = this.markerRepository.listMarkerByLayerPublic(layerId);
		}

		return listMarker;
	}

	/**
	 * Method to list all {@link Marker}
	 * 
	 * @param id
	 * @return marker
	 * @throws JAXBException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listAll()
	{
		return this.markerRepository.listAll();
	}

	/**
	 * Method to find attribute by marker
	 * 
	 * @param id
	 */
	public List<MarkerAttribute> listAttributeByMarker(Long id)
	{
		return this.markerAttributeRepository.listAttributeByMarker(id);
	}

	@Transactional(readOnly = true)
	public Page<Marker> listMarkerByFiltersByUser(String layer,
			MarkerStatus status, String dateStart, String dateEnd,
			PageRequest pageable) throws java.text.ParseException
	{
		String user = ContextHolder.getAuthenticatedUser().getEmail();
		return this.listMarkerByFilters(layer, status, dateStart, dateEnd, user,
				pageable);
	}

	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 * @throws java.text.ParseException
	 */
	@Transactional(readOnly = true)
	public Page<Marker> listMarkerByFilters(String layer, MarkerStatus status,
			String dateStart, String dateEnd, String user, PageRequest pageable)
					throws java.text.ParseException
	{
		
//		if (!(pageable.getSort().getOrders().size() >= 0))
//			pageable.getSort().getOrders().add(new Order(Direction.ASC, "name", null));

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dEnd = null;
		Calendar dStart = null;

		if (dateStart != null)
		{
			dStart = Calendar.getInstance();
			dStart.setTime((Date) formatter.parse(dateStart));
		}

		if (dateEnd != null)
		{
			dEnd = Calendar.getInstance();
			dEnd.setTime((Date) formatter.parse(dateEnd));
			dEnd.add(Calendar.DAY_OF_YEAR, 1);
			System.out.println(dEnd);
		}

		return this.markerRepository.listByFiltersWithoutOrder(layer, status,
				dStart, dEnd, user, pageable);

	}

	@Transactional(readOnly = true)
	public List<Marker> listMarkerByFiltersMapByUser(String layer,
			MarkerStatus status, String dateStart, String dateEnd,
			PageRequest pageable) throws java.text.ParseException
	{
		String user = ContextHolder.getAuthenticatedUser().getEmail();
		return this.listMarkerByFiltersMap(layer, status, dateStart, dateEnd,
				user, pageable);
	}

	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 * @throws java.text.ParseException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listMarkerByFiltersMap(String layer,
			MarkerStatus status, String dateStart, String dateEnd, String user,
			PageRequest pageable) throws java.text.ParseException
	{

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dEnd = null;
		Calendar dStart = null;

		if (dateStart != null)
		{
			dStart = Calendar.getInstance();
			dStart.setTime((Date) formatter.parse(dateStart));
		}

		if (dateEnd != null)
		{
			dEnd = Calendar.getInstance();
			dEnd.setTime((Date) formatter.parse(dateEnd));
			dEnd.add(Calendar.DAY_OF_MONTH, 1);
			dEnd.setTime(dEnd.getTime());
		}

		return this.markerRepository.listByFiltersMap(layer, status, dStart,
				dEnd, user);
	}

	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Marker> listMarkerByMarkers(List<Long> ids,
			PageRequest pageable)
	{
		return this.markerRepository.listByMarkers(ids, pageable);
	}

	/**
	 * 
	 * @param metaFileId
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public void removeImg(String metaFileId)
			throws IOException, RepositoryException
	{

		this.metaFileRepository.remove(metaFileId);
	}

	/**
	 * 
	 * @param fileTransfer
	 * @param markerId
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public void uploadImg(FileTransfer fileTransfer, Long markerId)
			throws IOException, RepositoryException
	{

		final String mimeType = fileTransfer.getMimeType();

		final List<String> validMimeTypes = new ArrayList<String>();
		validMimeTypes.add("image/gif");
		validMimeTypes.add("image/jpeg");
		validMimeTypes.add("image/bmp");
		validMimeTypes.add("image/png");

		if (mimeType == null || !validMimeTypes.contains(mimeType))
		{
			throw new IllegalArgumentException("Formato inválido!");
		}

		InputStream is = new BufferedInputStream(fileTransfer.getInputStream());
		final BufferedImage bufferedImage = new BufferedImage(640, 480,
				BufferedImage.TYPE_INT_RGB);
		Image image = ImageIO.read(is);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, 640, 480, null);
		g.dispose();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", os);
		InputStream isteam = new ByteArrayInputStream(os.toByteArray());

		MetaFile metaFile = new MetaFile();
		metaFile.setId(String.valueOf(markerId));
		metaFile.setContentType(fileTransfer.getMimeType());
		metaFile.setContentLength(fileTransfer.getSize());
		metaFile.setFolder("/marker/" + markerId);
		metaFile.setInputStream(isteam);
		metaFile.setName(fileTransfer.getFilename());

		this.metaFileRepository.insert(metaFile);
	}

	/**
	 * 
	 * @param markerId
	 * @return
	 * @throws RepositoryException
	 */
	public FileTransfer findImgByMarker(Long markerId)
			throws RepositoryException
	{
		try
		{
			final MetaFile metaFile = this.metaFileRepository
					.findByPath("/marker/" + markerId + "/" + markerId, true);
			return new FileTransfer(metaFile.getName(),
					metaFile.getContentType(), metaFile.getInputStream());
		}
		catch (PathNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retorna os status possíveis das postagens para o front-end
	 * 
	 * @return
	 */
	public MarkerStatus[] getMarkerStatus()
	{
		return MarkerStatus.values();
	}

}

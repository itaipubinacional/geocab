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

import javax.imageio.ImageIO;
import javax.jcr.RepositoryException;
import javax.xml.bind.JAXBException;

import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.configuration.account.User;
import br.com.geocab.domain.entity.configuration.account.UserRole;
import br.com.geocab.domain.entity.layer.AttributeType;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.marker.photo.Photo;
import br.com.geocab.domain.entity.marker.photo.PhotoAlbum;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;

/**
 * @author Thiago Rossetto Afonso
 * @since 30/09/2014
 * @version 1.0
 */
@Service
@Transactional
@RemoteProxy(name = "markerService")
public class MarkerService extends AbstractMarkerService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 * @param markers
	 * @return
	 */
	public List<Marker> insertMarker(List<Marker> markers) 
	{
		for (Marker marker : markers)
		{
			marker = this.insertMarker(marker);
		}
		return markers;
	}
	
	/**
	 * Method to insert an {@link Marker}
	 * 
	 * @param Marker
	 * @return Marker
	 * @throws RepositoryException
	 * @throws IOException
	 */
	public Marker insertMarker(Marker marker) 
	{
		try
        {
			User user = ContextHolder.getAuthenticatedUser();
	
			marker.setLocation((Point) this.wktToGeometry(marker.getWktCoordenate()));
	
			marker.setUser(user);
			
			validateAttribute(marker.getMarkerAttribute());
			
			marker = this.markerRepository.save(marker);	
	
			marker.setMarkerAttribute(this.insertMarkersAttributes(marker.getMarkerAttribute()));
			
			MarkerModeration markerModeration = new MarkerModeration();
			markerModeration.setMarker(marker);
			markerModeration.setStatus(marker.getStatus());
			this.markerModerationRepository.save(markerModeration);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.info(e.getMessage());
        }
		return marker;
	}
	
	/**
	 * 
	 * @param marker
	 * @return
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public Marker updateMarker(Marker marker)
	{
		try
	    {
			Assert.isTrue( ContextHolder.getAuthenticatedUser().getId().equals(marker.getUser().getId()), messages.getMessage("Access-is-denied", null, null));
				
			if(marker.getLocation() == null)
			{
				marker.setLocation(this.markerRepository.findOne(marker.getId()).getLocation());
			}
			else
			{
				marker.setLocation((Point) this.wktToGeometry(marker.getWktCoordenate()));
			}
	
			validateAttribute(marker.getMarkerAttribute());
			
			// N�o deixa repetir os atributos, previne erros do cascade
			List<MarkerAttribute> markersAttributes = marker.getMarkerAttribute();
			marker.setMarkerAttribute(null);
			this.markerRepository.save(marker);
			marker.setMarkerAttribute(markersAttributes);
			
			marker.setMarkerAttribute(this.insertMarkersAttributes(marker.getMarkerAttribute()));
			
			MarkerModeration markerModeration = new MarkerModeration();
			markerModeration.setMarker(marker);
			markerModeration.setStatus(marker.getStatus());
			this.markerModerationRepository.save(markerModeration);
	    }
        catch (DataIntegrityViolationException e)
        {
            LOG.info(e.getMessage());
        }
		return marker;
	}
	
	
	
	/**
	 * Pega a ultima foto que foi adicionada na marker
	 * 
	 * @param markerId
	 * @return
	 */
	public Page<Photo> lastPhotoByMarkerId(Long markerId, PageRequest pageRequest)
	{
		
		Page<Photo> photo = this.photoRepository.findPhotoByMarkerId(markerId, pageRequest);
		
//		photo.getContent().add ( this.getPhotoFileTransfer( photo.getContent().get(0)) );
		
		try
		{
			MetaFile metaFile = this.metaFileRepository.findByPath( photo.getContent().get(0).getIdentifier(), true);
			FileTransfer fileTransfer = new FileTransfer(metaFile.getName(),metaFile.getContentType(), metaFile.getInputStream());
			photo.getContent().get(0).setImage(fileTransfer);
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		
		Assert.isTrue( photo.getNumberOfElements() > 0 );
		
		
		return photo;
		
		
	}
	
	public Photo lastPhotoByMarkerId(Long markerId)
	{
	        
		Order order = new Order(Sort.Direction.DESC, "id");
	    Sort sort = new Sort(order);
		Pageable pageRequest = new PageRequest( 0, 1 , sort);
		
		Page<Photo> photo = this.photoRepository.findPhotoByMarkerId(markerId, pageRequest);
		
		return this.getPhotoFileTransfer( photo.getContent().get(0));
		
	}
	
	/**
	 * 
	 * @param photo
	 * @return
	 */
	private Photo getPhotoFileTransfer(Photo photo){

		try
		{
			MetaFile metaFile = this.metaFileRepository.findByPath( photo.getIdentifier(), true);
			FileTransfer fileTransfer = new FileTransfer(metaFile.getName(),metaFile.getContentType(), metaFile.getInputStream());
			photo.setImage(fileTransfer);
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		
		return photo;
	}
	
	
	/**
	 * Remove todas as fotos no sistema de arquivos
	 * @param idPhotos
	 */
	public void removePhotos(List<Long> idPhotos)
	{
		for (Long idPhoto : idPhotos)
		{
			Photo photo = this.photoRepository.findOne(idPhoto);

			try
			{
				this.metaFileRepository.removeByPath(photo.getIdentifier());
				this.photoRepository.delete(idPhoto);
			}
			catch (RepositoryException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public Page<Photo> listPhotosByPhotoAlbumIdentifier(final String identifier, final PageRequest pageRequest)
	{
		
		Page<Photo> photos = this.photoRepository.findByIdentifierContaining(identifier, pageRequest);
		
		for (Photo photo : photos.getContent())
		{
			try
			{
				MetaFile metaFile = this.metaFileRepository.findByPath( photo.getIdentifier(), true);
				FileTransfer fileTransfer = new FileTransfer(metaFile.getName(),metaFile.getContentType(), metaFile.getInputStream());
				photo.setImage(fileTransfer);
			}
			catch (RepositoryException e)
			{
				e.printStackTrace();
			}
		}
		return photos;
	}
	
	/**
	 * 
	 * @param markerAttributeId
	 * @param pageRequest
	 * @return
	 */
	public Page<Photo> findPhotoAlbumByAttributeMarkerId(Long markerAttributeId, final PageRequest pageRequest)
	{
		PhotoAlbum photoAlbum = this.photoAlbumRepository.findByMarkerAttributeId(markerAttributeId);
		return this.listPhotosByPhotoAlbumIdentifier(photoAlbum.getIdentifier(), pageRequest);
	}
	
	
	
	/**
	 * 
	 * @param photoId
	 * @return
	 */
	public Photo findPhotoById(String identifier)
	{
		Photo photo = this.photoRepository.findByIdentifier(identifier);
		
		return this.getPhotoFileTransfer(photo);
		
	}
	
	/**
	 * 
	 * @param photoId
	 * @return
	 */
	public Photo findPhotoById(Long photoId)
	{
		Photo photo = this.photoRepository.findOne(photoId);
		
		return this.getPhotoFileTransfer(photo);
	}

	/**
	 * Method to remove an {@link Marker}
	 * 
	 * @param id
	 */
	// @PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','"+
	// UserRole.MODERATOR_VALUE + "')")
	public void removeMarker(Long id)
	{
		try
        {
			Marker marker = this.markerRepository.findOne(id);
			
			Assert.isTrue( ContextHolder.getAuthenticatedUser().getId().equals(marker.getUser().getId()), messages.getMessage("Access-is-denied", null, null));
			
			marker.setDeleted(true);
			this.markerRepository.save(marker);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.info(e.getMessage());
        }
		
	}

	/**
	 * Method to block an {@link Marker}
	 * 
	 * @param Marker
	 *            marker
	 */
	@PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','" + UserRole.MODERATOR_VALUE + "')")
	public void enableMarker(Long id)
	{
		try
		{
			Marker marker = this.markerRepository.findOne(id);
			marker.setStatus(MarkerStatus.ACCEPTED);
			marker = this.markerRepository.save(marker);
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
		}
	}

	/**
	 * Method to unblock an {@link Marker}
	 * 
	 * @param Marker
	 *            marker
	 */
	@PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','" + UserRole.MODERATOR_VALUE + "')")
	public void disableMarker(Long id)
	{
		try
		{
			Marker marker = this.markerRepository.findOne(id);
			marker.setStatus(MarkerStatus.REFUSED);
			marker = this.markerRepository.save(marker);
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
		}
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
		Marker marker = this.markerRepository.findOne(id);	
		
		marker.setMarkerAttribute(this.markerAttributeRepository.listAttributeByMarker(marker.getId()));
		for (MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			markerAttribute.setPhotoAlbum(this.photoAlbumRepository.findByMarkerAttributeId(markerAttribute.getId()));
		}
		
		return marker;
	}
	
	/**
	 * 
	 * @return
	 */
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
	public List<Marker> listMarkerByLayer(Long layerId)
	{
		final User user = ContextHolder.getAuthenticatedUser();

		List<Marker> listMarker = null;

		if (!user.equals(User.ANONYMOUS))
		{
			if (user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE) || user.getRole().name().equals(UserRole.MODERATOR_VALUE))
			{
				listMarker = this.markerRepository.listMarkerByLayerAll(layerId);
			}
			else
			{
				listMarker = this.markerRepository.listMarkerByLayer(layerId, user.getId());
			}
		}
		else
		{
			listMarker = this.markerRepository.listMarkerByLayerPublic(layerId);
		}

		return listMarker;
	}
	
	@Transactional(readOnly = true)
	public List<Marker> listMarkersToExport(  List<Long> layersId, MarkerStatus status, String dateStart, String dateEnd, String user ){
		
		List<Marker> markers = new ArrayList<>();
		
		try
		{
			
			for (Long layerId : layersId)
			{
				
				
				Page<Marker> markersPage =  this.listMarkerByFilters( layerId, status, dateStart, dateEnd, user, null);
				
				markers.addAll( markersPage.getContent() );
				
			}
			
			return markers;
			
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
			
			return null;
		}

		
	}
	
	

	/**
	 * 
	 * @param wktPoint
	 * @return
	 */
	private Geometry wktToGeometry(String wktPoint)
	{
		WKTReader fromText = new WKTReader();
		Geometry geom = null;
		try
		{
			geom = fromText.read(wktPoint);
		}
		catch (ParseException e)
		{
			throw new RuntimeException(messages.getMessage("admin.marker.Invalid-coordinates", null, null));
		}
		
		Assert.isTrue(geom.isValid(), messages.getMessage("admin.marker.Invalid-coordinates", null, null));
		return geom;
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
		final List<MarkerAttribute> markerAttributes = this.markerAttributeRepository.listAttributeByMarker(id);
		
		for (MarkerAttribute markerAttribute : markerAttributes)
		{
			if ( markerAttribute.getAttribute().getType().equals(AttributeType.MULTIPLE_CHOICE))
			{
				markerAttribute.setSelectedAttribute( markerAttributeRepository.findOne(markerAttribute.getId()).getSelectedAttribute());
				markerAttribute.getAttribute().setOptions( attributeOptionRepository.listByAttributeId(markerAttribute.getAttribute().getId()) );
			}
		}
		
		return markerAttributes;
	}
	
	/**
	 * 
	 * @param layer
	 * @param status
	 * @param dateStart
	 * @param dateEnd
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Marker> listMarkerByFiltersByUser(Long layer, MarkerStatus status, String dateStart, String dateEnd, PageRequest pageable)
	{
		String user = ContextHolder.getAuthenticatedUser().getEmail();
		return this.listMarkerByFilters(layer, status, dateStart, dateEnd, user, pageable);
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
	public Page<Marker> listMarkerByFilters(Long layer, MarkerStatus status, String dateStart, String dateEnd, String user, PageRequest pageable)
	{
		if(this.getUserMe().getRole() != UserRole.ADMINISTRATOR)
		{
			user = this.getUserMe().getEmail();	
		}
		return this.markerRepository.listByFilters(layer, status, this.formattDates(dateStart, dateEnd)[0], this.formattDates(dateStart, dateEnd)[1], user, pageable);
	}
	
	/**
	 * 
	 * @param layer
	 * @param status
	 * @param dateStart
	 * @param dateEnd
	 * @param pageable
	 * @return
	 * @throws java.text.ParseException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listMarkerByFiltersMapByUser(Long layer, MarkerStatus status, String dateStart, String dateEnd, PageRequest pageable) throws java.text.ParseException
	{
		String user = ContextHolder.getAuthenticatedUser().getEmail();
		return this.listMarkerByFiltersMap(layer, status, dateStart, dateEnd, user, pageable);
	}
	
	/**
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	private Calendar[] formattDates(String dateStart, String dateEnd){
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dEnd = null;
		Calendar dStart = null;
		
		try
		{
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
		}
		catch (java.text.ParseException e )
		{
			e.printStackTrace();
			LOG.info(e.getMessage());
		}
		return new Calendar[] {dStart, dEnd};
	}
	
	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 * @param layer
	 * @param status
	 * @param dateStart
	 * @param dateEnd
	 * @param user
	 * @param pageable
	 * @return
	 * @throws java.text.ParseException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listMarkerByFiltersMap(Long layer, MarkerStatus status, String dateStart, String dateEnd, String user, PageRequest pageable) throws java.text.ParseException
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

		if(this.getUserMe().getRole() != UserRole.ADMINISTRATOR)
		{
			user = this.getUserMe().getEmail();	
		}
		
		
		return this.markerRepository.listByFiltersMap(layer, status, this.formattDates(dateStart, dateEnd)[0], this.formattDates(dateStart, dateEnd)[1], user);
	}

	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Marker> listMarkerByMarkers(List<Long> ids, PageRequest pageable)
	{
		return this.markerRepository.listByMarkers(ids, pageable);
	}
	

	/**
	 * 
	 * @param fileTransfer
	 * @param markerId
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public void uploadImg(FileTransfer fileTransfer, Long markerId)
	{
		try
		{
			final String mimeType = fileTransfer.getMimeType();
	
			final List<String> validMimeTypes = new ArrayList<String>();
			validMimeTypes.add("image/gif");
			validMimeTypes.add("image/jpeg");
			validMimeTypes.add("image/bmp");
			validMimeTypes.add("image/png");
	
			if (mimeType == null || !validMimeTypes.contains(mimeType))
			{
				throw new IllegalArgumentException("Formato inv�lido!");
			}
	
			InputStream is = new BufferedInputStream(fileTransfer.getInputStream());
			
			final BufferedImage bufferedImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
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
		catch (IOException | RepositoryException e)
		{
			LOG.info(e.getMessage());
		}
	}

	/**
	 * 
	 * @param markerId
	 * @return
	 * @throws RepositoryException
	 */
	public FileTransfer findImgByMarker(Long markerId)
	{
		try
		{
			final MetaFile metaFile = this.metaFileRepository.findByPath(markerId.toString(), true);
			return new FileTransfer(metaFile.getName(),metaFile.getContentType(), metaFile.getInputStream());
		}
		catch (RepositoryException e)
		{
			return null;
		}
	}

	/**
	 * Retorna os status poss�veis das postagens para o front-end
	 * 
	 * @return
	 */
	public MarkerStatus[] getMarkerStatus()
	{
		return MarkerStatus.values();
	}

}

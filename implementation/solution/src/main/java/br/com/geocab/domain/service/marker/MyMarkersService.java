/**
 * 
 */
package br.com.geocab.domain.service.marker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.xml.bind.JAXBException;

import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;

@Service
@Transactional
@RemoteProxy(name = "myMarkersService")
public class MyMarkersService extends AbstractMarkerService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	

	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * @param marker
	 * @return
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public Marker updateMarker(Marker marker) throws IOException, RepositoryException
	{
		try
        {
			Assert.isTrue( ContextHolder.getAuthenticatedUser().getId().equals(marker.getUser().getId()), messages.getMessage("Access-is-denied", null, null));
			
			marker.setLocation(this.markerRepository.findOne(marker.getId()).getLocation());
			
			validateAttribute(marker.getMarkerAttribute());
			
			// Não deixa repetir os atributos, previne erros do cascade
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
	 * Method to update an {@link Marker}
	 * 
	 * @param Marker
	 * @return Marker
	 * @throws RepositoryException
	 * @throws IOException
	 */

	public Marker postMarker(Marker marker) throws IOException, RepositoryException
	{
		marker.setStatus(MarkerStatus.PENDING);
		return this.updateMarker(marker);
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

	public void postMarkers(List<Long> markersId) throws IOException, RepositoryException
	{
		try
		{
			for (Long markerId : markersId)
			{
				Marker marker = this.markerRepository.findOne(markerId);
				
				Assert.isTrue( ContextHolder.getAuthenticatedUser().getId().equals(marker.getUser().getId()), messages.getMessage("Access-is-denied", null, null));
			
				marker.setStatus(MarkerStatus.PENDING);
				this.markerRepository.save(marker);
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
		try
        {
			Marker marker = this.findMarkerById(id);
	
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
	public List<Marker> listMarkerByLayerFilters(Long layerId)
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
				listMarker = this.markerRepository.listMarkerByLayer(layerId,user.getId());
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
	
	/**
	 * 
	 * @param layerId
	 * @param status
	 * @param dateStart
	 * @param dateEnd
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Marker> listMarkerByFiltersByUser(Long layer, MarkerStatus status, String dateStart, String dateEnd, PageRequest pageable)
	{
		return this.listMarkerByFilters(layer, status, dateStart, dateEnd, ContextHolder.getAuthenticatedUser().getEmail(), pageable);
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
		return this.markerRepository.listByFilters(layer, status, this.formattDates(dateStart, dateEnd)[0], this.formattDates(dateStart, dateEnd)[1], this.getUserMe().getEmail(), pageable);
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
		
		return this.markerRepository.listByFiltersMap(layer, status, this.formattDates(dateStart, dateEnd)[0], this.formattDates(dateStart, dateEnd)[1], this.getUserMe().getEmail());
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
		return this.listMarkerByFiltersMap(layer, status, dateStart, dateEnd, ContextHolder.getAuthenticatedUser().getEmail(), pageable);
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
	 * @param markerId
	 * @return
	 * @throws RepositoryException
	 */
	public FileTransfer findImgByMarker(Long markerId) throws RepositoryException
	{
		try
		{
			final MetaFile metaFile = this.metaFileRepository.findByPath("/marker/" + markerId + "/" + markerId, true);
			return new FileTransfer(metaFile.getName(), metaFile.getContentType(), metaFile.getInputStream());
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

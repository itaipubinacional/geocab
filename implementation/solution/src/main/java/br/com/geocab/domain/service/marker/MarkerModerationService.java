/**
 * 
 */
package br.com.geocab.domain.service.marker;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jcr.RepositoryException;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.domain.entity.markermoderation.Motive;
import br.com.geocab.domain.entity.markermoderation.MotiveMarkerModeration;
import br.com.geocab.domain.repository.IAccountMailRepository;
import br.com.geocab.domain.repository.account.IUserRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;
import br.com.geocab.domain.repository.markermoderation.IMarkerModerationRepository;
import br.com.geocab.domain.repository.motive.IMotiveMarkerModerationRepository;

/**
 * 
 * @author Vinicius Ramos Kawamoto
 * @since 12/01/2015
 * @version 1.0
 * @category Service
 *
 */

@Service
@Transactional
@RemoteProxy(name="markerModerationService")
@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
public class MarkerModerationService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * Log
	 */
	private static final Logger LOG = Logger.getLogger( MarkerModeration.class.getName() );
	
	/**
	 * 
	 */
	@Autowired
	private IMarkerModerationRepository markerModerationRepository;
	
	/**
	 * User Repository
	 */
	@Autowired
	private IUserRepository userRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IMarkerRepository markerRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IMotiveMarkerModerationRepository motiveMarkerModerationRepository;
	
	/**
	 * AccountMail Repository
	 */
	@Autowired
	private IAccountMailRepository accountMailRepository;
	
	
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 *
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public Page<MarkerModeration> listMarkerModerationByFilters( String filter, PageRequest pageable )
	{
		return this.markerModerationRepository.listByFilters(filter, pageable);
	}
	
	
	/**
	 * 
	 * Method to accept a {@link Marker}
	 * 
	 * @param markerModeration
	 * @return
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public MarkerModeration acceptMarker( Long id)
	{
		try
		{
			final MarkerModeration lastMarkerModeration = this.listMarkerModerationByMarker(id).get(0);
			
			MarkerModeration markerModeration = new MarkerModeration();
			
			if( lastMarkerModeration.getStatus().equals(MarkerStatus.ACCEPTED) )
			{
				throw new IllegalArgumentException("The marker moderation already accepted");
			}
			else
			{
				Marker marker = markerRepository.findOne(id);
				User user = this.userRepository.findOne(marker.getUser().getId());
				
				marker.setStatus(MarkerStatus.ACCEPTED);
				
				markerModeration.setMarker(marker);
				markerModeration.setStatus(MarkerStatus.ACCEPTED);
				
				markerModeration = this.markerModerationRepository.save(markerModeration);
				
				this.accountMailRepository.sendMarkerAccepted( user, marker );
			}
			
			return markerModeration;
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param layers
	 * @param accessGroupId
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public void associateMotive( List<Motive> motives, Long markerModerationId )
	{
		MarkerModeration markerModeration = new MarkerModeration(markerModerationId);
		
		for (Motive motive : motives)
		{
			MotiveMarkerModeration motiveMarkerModeration = new MotiveMarkerModeration();
			motiveMarkerModeration.setMarkerModeration(markerModeration);
			motiveMarkerModeration.setMotive(motive);
			
			this.motiveMarkerModerationRepository.save(motiveMarkerModeration);
		}
	}
	
	
	   /**
     * Method to update an {@link Marker}
     * 
     * @param Marker
     * @return Marker
     * @throws RepositoryException
     * @throws IOException
     */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
    public Marker cancelMarkerModeration (Long id) throws IOException,
            RepositoryException
    {
    	try
		{
			final MarkerModeration lastMarkerModeration = this.listMarkerModerationByMarker(id).get(0);
			
			MarkerModeration markerModeration = new MarkerModeration();
			
			if( lastMarkerModeration.getStatus().equals(MarkerStatus.CANCELED) )
			{
				throw new IllegalArgumentException("The marker moderation already canceled");
				
			}
			else
			{
				Marker marker = markerRepository.findOne(id);
				User user = this.userRepository.findOne(marker.getUser().getId());
				
				marker.setStatus(MarkerStatus.CANCELED);
				
				markerModeration.setMarker(marker);
				markerModeration.setStatus(MarkerStatus.CANCELED);
				
				markerModeration = this.markerModerationRepository.save(markerModeration);
				
				this.accountMailRepository.sendMarkerCanceled( user, marker );
				
				return marker;
			}
			
	
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
		}
		
		return null;
    }
 
	
	/**
	 * 
	 * Method to refuse a {@link Marker}
	 * 
	 * @param markerModeration
	 * @return
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public MarkerModeration refuseMarker( Long markerId, Motive motive, String description )
	{			
		
		try
		{			
			PageRequest pageable = new PageRequest();
			pageable.setSort(new Sort(Direction.ASC, "created"));
			
			final MarkerModeration lastMarkerModeration = this.markerModerationRepository.listMarkerModerationByMarker(markerId, pageable).getContent().get(0);
			
			MarkerModeration markerModeration = new MarkerModeration();
			
			if( lastMarkerModeration.getStatus().equals(MarkerStatus.REFUSED) )
			{
				throw new IllegalArgumentException("The marker moderation already refused");
			}
			else
			{
				Marker marker = markerRepository.findOne(markerId);
				User user = this.userRepository.findOne(marker.getUser().getId());
				
				marker.setStatus(MarkerStatus.REFUSED);
				
				markerModeration.setMarker(marker);
				markerModeration.setStatus(MarkerStatus.REFUSED);
				
				markerModeration = this.markerModerationRepository.save(markerModeration);
				
				MotiveMarkerModeration motiveMarkerModeration = new MotiveMarkerModeration();
				motiveMarkerModeration.setMarkerModeration(markerModeration);
				motiveMarkerModeration.setMotive(motive);
				motiveMarkerModeration.setDescription(description);
				
				this.motiveMarkerModerationRepository.save(motiveMarkerModeration);
				
				this.accountMailRepository.sendMarkerRefused( user, marker, motiveMarkerModeration );
				
			}
			
			return markerModeration;
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param markerModerationId
	 * @return
	 */
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public List<MotiveMarkerModeration> listMotivesByMarkerModerationId(Long markerModerationId)
	{
		List<MotiveMarkerModeration> motivesMarkerModeration = this.motiveMarkerModerationRepository.listByMarkerModerationId(markerModerationId);
		
		return motivesMarkerModeration;
	}
	
	/**
	 * 
	 * @param markerId
	 * @return
	 */
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public List<MarkerModeration> listMarkerModerationByMarker( Long markerId  )
	{
		return this.markerModerationRepository.listMarkerModerationByMarker(markerId);
	}
	
	
	/**
	 *
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public Page<MarkerModeration> listMarkerModerationByMarker( Long markerId, PageRequest pageable  )
	{
		pageable.setSort(new Sort(Direction.ASC, "id"));

        return this.markerModerationRepository.listMarkerModerationByMarker(markerId, pageable);
	}
	
}

/**
 * 
 */
package br.com.geocab.domain.service;

import java.util.List;
import java.util.logging.Logger;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.account.IAccountMailRepository;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.domain.repository.account.IUserRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;
import br.com.geocab.domain.repository.markermoderation.IMarkerModerationRepository;

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
	public MarkerModeration acceptMarker( Long id )
	{			
		try
		{
			User user = this.userRepository.findOne(ContextHolder.getAuthenticatedUser().getId());
			
			final MarkerModeration lastMarkerModeration = this.listMarkerModerationByMarker(id).get(0);
			
			MarkerModeration markerModeration = new MarkerModeration();
			
			if( lastMarkerModeration.getStatus().equals(MarkerStatus.ACCEPTED) )
			{
				throw new IllegalArgumentException("The marker moderation already accepted");
			}
			else
			{
				Marker marker = markerRepository.findOne(id);
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
	 * Method to refuse a {@link Marker}
	 * 
	 * @param markerModeration
	 * @return
	 */
	public MarkerModeration refuseMarker( Long id )
	{			
		try
		{
			User user = this.userRepository.findOne(ContextHolder.getAuthenticatedUser().getId());
			
			final MarkerModeration lastMarkerModeration = this.listMarkerModerationByMarker(id).get(0);
			
			MarkerModeration markerModeration = new MarkerModeration();
			
			if( lastMarkerModeration.getStatus().equals(MarkerStatus.REFUSED) )
			{
				throw new IllegalArgumentException("The marker moderation already refused");
			}
			else
			{
				Marker marker = markerRepository.findOne(id);
				marker.setStatus(MarkerStatus.REFUSED);
				
				markerModeration.setMarker(marker);
				markerModeration.setStatus(MarkerStatus.REFUSED);
				
				markerModeration = this.markerModerationRepository.save(markerModeration);

				this.accountMailRepository.sendMarkerRefused( user, marker );
				
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
	 * @param markerId
	 * @return
	 */
	@Transactional(readOnly=true)
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
	public Page<MarkerModeration> listMarkerModerationByMarker( Long markerId, PageRequest pageable  )
	{
		pageable.setSort(new Sort(Direction.ASC, "id"));

		return this.markerModerationRepository.listByMarker(markerId, pageable);
	}
	
}

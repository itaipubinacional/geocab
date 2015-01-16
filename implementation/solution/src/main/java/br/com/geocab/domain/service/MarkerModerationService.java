/**
 * 
 */
package br.com.geocab.domain.service;

import java.util.logging.Logger;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
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
	 * 
	 */
	@Autowired
	private IMarkerRepository markerRepository;
	
	
	
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
			
			Marker marker = markerRepository.findOne(id);
			marker.setStatus(MarkerStatus.ACCEPTED);
			
			MarkerModeration markerModeration = new MarkerModeration();
			markerModeration.setMarker(marker);
			markerModeration.setStatus(MarkerStatus.ACCEPTED);
			
			markerModeration = this.markerModerationRepository.save(markerModeration);
			
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
			Marker marker = markerRepository.findOne(id);
			marker.setStatus(MarkerStatus.REFUSED);
			
			MarkerModeration markerModeration = new MarkerModeration();
			markerModeration.setMarker(marker);
			markerModeration.setStatus(MarkerStatus.REFUSED);
			
			markerModeration = this.markerModerationRepository.save(markerModeration);
			
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

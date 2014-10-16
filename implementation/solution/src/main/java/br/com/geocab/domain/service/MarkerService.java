/**
 * 
 */
package br.com.geocab.domain.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jcr.RepositoryException;
import javax.xml.bind.JAXBException;

import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.StatusMarker;
import br.com.geocab.domain.repository.IMetaFileRepository;
import br.com.geocab.domain.repository.marker.IMarkerAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;


/**
 * @author Thiago Rossetto Afonso
 * @since 30/09/2014
 * @version 1.0
 */
@Service
@Transactional
//@PreAuthorize("hasRole('"+UserRole.ADMINISTRADOR_VALUE+"')")
@RemoteProxy(name="markerService")
public class MarkerService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Log
	 */
	private static final Logger LOG = Logger.getLogger( DataSourceService.class.getName() );
	
	/**
	 * Repository of {@link DataSource}
	 */
	@Autowired
	private IMarkerRepository markerRepository;
	
	@Autowired
	private IMarkerAttributeRepository markerAttributeRepository;
	
	/**
	 * I18n 
	 */
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private IMetaFileRepository metaFileRepository;
	
	
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Method to insert an {@link Marker}
	 * 
	 * @param Marker
	 * @return Marker
	 */
	public Marker insertMarker( Marker marker )
	{
		try{
			marker.setStatus(StatusMarker.PENDING);
			marker = this.markerRepository.save( marker );
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			final String error = e.getCause().getCause().getMessage();
			
			this.dataIntegrityViolationException(error);			
		}
		return marker; 
	}
	
	/**
	 * Method to update an {@link Marker}
	 * 
	 * @param Marker
	 * @return Marker
	 */
	public Marker updateMarker( Marker marker )
	{			
		try{
			marker = this.markerRepository.save( marker );
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			final String error = e.getCause().getCause().getMessage();
			
			this.dataIntegrityViolationException(error);
		}
		return marker;
	}
	
	/**
	 * Method to remove an {@link Marker}
	 * 
	 * @param id
	 */
	public void removeMarker( Long id )
	{
		this.markerRepository.delete( id );
	}
	
	/**
	 * Method to block an {@link Marker}
	 * 
	 * @param Marker marker
	 */
	public void enableMarker( Long id )
	{
		try{
			Marker marker = this.findMarkerById(id);
			marker.setStatus(StatusMarker.ACCEPTED);
			marker = this.markerRepository.save( marker );
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			final String error = e.getCause().getCause().getMessage();
			
			this.dataIntegrityViolationException(error);
		}
	}
	
	/**
	 * Method to unblock an {@link Marker}
	 * 
	 * @param Marker marker
	 */
	public void disableMarker( Long id )
	{
		try{
			Marker marker = this.findMarkerById(id);
			marker.setStatus(StatusMarker.REFUSED);
			marker = this.markerRepository.save( marker );
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			final String error = e.getCause().getCause().getMessage();
			
			this.dataIntegrityViolationException(error);
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
	public Marker findMarkerById( Long id )
	{
		return this.markerRepository.findOne( id );
	}
	
	/**
	 * Method to list all {@link Marker}
	 * 
	 * @param id
	 * @return marker
	 * @throws JAXBException 
	 */
	@Transactional(readOnly=true)
	public List<Marker> listAll()
	{
		return this.markerRepository.listAll();
	}
	
	/**
	 * Method to find attribute by marker 
	 * 
	 * @param id 
	 */
	public List<MarkerAttribute> findAttributeByMarker(Long id){
		return this.markerAttributeRepository.findAttributeByMarker(id);
	}
	
	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 *
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<Marker> listMarkerByFilters( String filter, PageRequest pageable )
	{
		return this.markerRepository.listByFilters(pageable);
	}
	
	/**
	 * Method to verify DataIntegrityViolations and throw IllegalArgumentException with the field name
	 *
	 * @param error
	 * @throws IllegalArgumentException 
	 * @return void
	 */
	private void dataIntegrityViolationException( String error )
	{	
		/*String fieldError = "";
		
		if(error.contains("uk_data_source_name"))
		{
			fieldError = this.messages.getMessage("Name", new Object [] {}, null );
		}
		else if(error.contains("uk_data_source_url"))
		{
			fieldError = this.messages.getMessage("Address", new Object [] {}, null );
		}
		
		if(!fieldError.isEmpty()){
			throw new IllegalArgumentException( this.messages.getMessage("The-field-entered-already-exists,-change-and-try-again", new Object [] {fieldError}, null) );
		}*/
	}
	
	public MetaFile uploadImg( FileTransfer fileTransfer ) throws IOException, RepositoryException {
		
		MetaFile metaFile = new MetaFile();
		metaFile.setContentType( fileTransfer.getMimeType() );
		metaFile.setFolder("/test/files");
		metaFile.setInputStream(fileTransfer.getInputStream());
		metaFile.setName( fileTransfer.getFilename() );
		
		return this.metaFileRepository.insert( metaFile );
	}

}

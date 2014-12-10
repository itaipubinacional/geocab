/**
 * 
 */
package br.com.geocab.domain.service;

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
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.xml.bind.JAXBException;

import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
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
	 * @throws RepositoryException 
	 * @throws IOException 
	 */
	public Marker insertMarker( Marker marker ) throws IOException, RepositoryException
	{
		try{
			User user = ContextHolder.getAuthenticatedUser();
			
			marker.setStatus(StatusMarker.PENDING);
			marker.setUser(user);
			marker = this.markerRepository.save( marker );
			if( marker.getImage() != null ) {
				this.uploadImg(marker.getImage(), marker.getId());
			}
			
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
	 * @throws RepositoryException 
	 * @throws IOException 
	 */
	//@PreAuthorize("hasAnyRole('"+UserRole.ADMINISTRATOR_VALUE+"','"+UserRole.MODERATOR_VALUE+"')")
	public Marker updateMarker( Marker marker ) throws IOException, RepositoryException
	{			
		try{
			Marker markerTemporary = this.markerRepository.findOne(marker.getId());
			
			if(markerTemporary.getLayer().getId() != marker.getLayer().getId()) {
				List<MarkerAttribute> markerAttributes = this.markerAttributeRepository.listAttributeByMarker(marker.getId());
				
				if( markerAttributes != null ) {
					this.markerAttributeRepository.deleteInBatch(markerAttributes);	
				}
			}
			
			FileTransfer file = this.findImgByMarker(marker.getId());
			
			if( file != null && marker.getImage() != null ){
				this.removeImg(String.valueOf(marker.getId()));
			}
			
			if( marker.getImage() != null) {
				this.uploadImg(marker.getImage(), marker.getId());	
			}
			
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
	@PreAuthorize("hasAnyRole('"+UserRole.ADMINISTRATOR_VALUE+"','"+UserRole.MODERATOR_VALUE+"')")
	public void removeMarker( Long id )
	{
		Marker marker = this.findMarkerById(id);
		marker.setDeleted(true);
		this.markerRepository.save(marker);
	}
	
	/**
	 * Method to block an {@link Marker}
	 * 
	 * @param Marker marker
	 */
	@PreAuthorize("hasAnyRole('"+UserRole.ADMINISTRATOR_VALUE+"','"+UserRole.MODERATOR_VALUE+"')")
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
	@PreAuthorize("hasAnyRole('"+UserRole.ADMINISTRATOR_VALUE+"','"+UserRole.MODERATOR_VALUE+"')")
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
	
	public User getUserMe(){
		User u = ContextHolder.getAuthenticatedUser();
		return u;
	}
	
	/**
	 * Method to find an {@link Marker} by layer
	 * 
	 * @param layerId
	 * @return marker List
	 * @throws JAXBException 
	 */
	@Transactional(readOnly = true)
	public List<Marker> listMarkerByLayerFilters( Long layerId )
	{
		User user = ContextHolder.getAuthenticatedUser();
	
		List<Marker> listMarker = null;
		
		if(user != null) {
			 
			if( user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE) || user.getRole().name().equals(UserRole.MODERATOR_VALUE) ) {
				listMarker = this.markerRepository.listMarkerByLayerAll( layerId );
			} else {
				listMarker = this.markerRepository.listMarkerByLayer( layerId, user.getId() );
			}
			
		}
		
		for(Marker marker : listMarker) {
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
	public List<Marker> listMarkerByLayer( Long layerId )
	{
		User user = ContextHolder.getAuthenticatedUser();
	
		List<Marker> listMarker = null;
		
		if(user != null) {
			 
			if( user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE) || user.getRole().name().equals(UserRole.MODERATOR_VALUE) ) {
				listMarker = this.markerRepository.listMarkerByLayerAll( layerId );
			} else {
				listMarker = this.markerRepository.listMarkerByLayer( layerId, user.getId() );
			}
			
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
	public List<MarkerAttribute> listAttributeByMarker(Long id){
		return this.markerAttributeRepository.listAttributeByMarker(id);
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
	/**
	 * 
	 * @param metaFileId
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public void removeImg( String metaFileId ) throws IOException, RepositoryException {
		
	
		this.metaFileRepository.remove(metaFileId);
	}
	
	/**
	 * 
	 * @param fileTransfer
	 * @param markerId
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public void uploadImg( FileTransfer fileTransfer, Long markerId ) throws IOException, RepositoryException {
		
		final String  mimeType = fileTransfer.getMimeType();
		
		final List<String> validMimeTypes = new ArrayList<String>();
		validMimeTypes.add("image/gif");
		validMimeTypes.add("image/jpeg");
		validMimeTypes.add("image/bmp");
		validMimeTypes.add("image/png");
		
		if ( mimeType == null || !validMimeTypes.contains(mimeType))
		{
			throw new IllegalArgumentException( "Formato inválido!" );
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
		metaFile.setContentType( fileTransfer.getMimeType() );
		metaFile.setContentLength( fileTransfer.getSize() );
		metaFile.setFolder("/marker/"+markerId);
		metaFile.setInputStream(isteam);
		metaFile.setName( fileTransfer.getFilename() );
		
		this.metaFileRepository.insert( metaFile );
	}
	
	/**
	 * 
	 * @param markerId
	 * @return
	 * @throws RepositoryException
	 */
	public FileTransfer findImgByMarker( Long markerId ) throws RepositoryException
	{
		try
		{
			final MetaFile metaFile = this.metaFileRepository.findByPath("/marker/"+markerId+"/"+markerId, true);
			return new FileTransfer(metaFile.getName(), metaFile.getContentType(), metaFile.getInputStream());
		}
		catch ( PathNotFoundException e )
		{
			return null;
		}	
	}
	
	

}

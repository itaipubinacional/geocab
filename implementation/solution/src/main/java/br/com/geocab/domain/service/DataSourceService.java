/**
 * 
 */
package br.com.geocab.domain.service;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.repository.datasource.IDataSourceRepository;
import br.com.geocab.infrastructure.geoserver.GeoserverConnection;

/**
 * Class to manage of entities {@link DataSource}
 * 
 * @author Cristiano Correa
 * @since 27/05/2014
 * @version 1.0
 * @category Service
 *
 */

@Service
@Transactional
@RemoteProxy(name="dataSourceService")
@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
public class DataSourceService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Log
	 */
	private static final Logger LOG = Logger.getLogger( DataSourceService.class.getName() );
	
	/**
	 * I18n 
	 */
	@Autowired
	private MessageSource messages;
	
	/**
	 * Repository of {@link DataSource}
	 */
	@Autowired
	private IDataSourceRepository dataSourceRepository;
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Method to insert an {@link DataSource}
	 * 
	 * @param DataSource
	 * @return DataSource
	 */
	public DataSource insertDataSource( DataSource dataSource )
	{
		try{
			dataSource = this.dataSourceRepository.save( dataSource );
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			final String error = e.getCause().getCause().getMessage();
			
			this.dataIntegrityViolationException(error);			
		}
		return dataSource; 
	}
	
	/**
	 * Method to update an {@link DataSource}
	 * 
	 * @param dataSource
	 * @return dataSource
	 */
	public DataSource updateDataSource( DataSource dataSource )
	{			
		try{
			dataSource = this.dataSourceRepository.save( dataSource );
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			final String error = e.getCause().getCause().getMessage();
			
			this.dataIntegrityViolationException(error);
		}
		return dataSource;
	}
	
	/**
	 * Method to remove an {@link FonteDados}
	 * 
	 * @param id
	 */
	public void removeDataSource( Long id )
	{
		this.dataSourceRepository.delete( id );
	}
	
	/**
	 * Method to find an {@link FonteDados} by id
	 * 
	 * @param id
	 * @return dataSource
	 * @throws JAXBException 
	 */
	@Transactional(readOnly = true)
	public DataSource findDataSourceById( Long id )
	{
		return this.dataSourceRepository.findOne( id );
	}
	
	/**
	 * Method to list all {@link FonteDados}
	 * 
	 * @param id
	 * @return dataSource
	 * @throws JAXBException 
	 */
	@Transactional(readOnly=true)
	public List<DataSource> listAllDataSource()
	{
		return this.dataSourceRepository.listAll();
	}
	
	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 *
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<DataSource> listDataSourceByFilters( String filter, PageRequest pageable )
	{
		return this.dataSourceRepository.listByFilters(filter, pageable);
	}
	
	/**
	 * Method to test Data source connection
	 * 
	 * @param url 
	 * @return boolean
	 */
	@Transactional(readOnly=true)
	public boolean testConnection(String url)
	{
		GeoserverConnection geoserverConnection = new GeoserverConnection();		
		return geoserverConnection.testConnection(url);
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
		String fieldError = "";
		
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
		}
	}
	
}
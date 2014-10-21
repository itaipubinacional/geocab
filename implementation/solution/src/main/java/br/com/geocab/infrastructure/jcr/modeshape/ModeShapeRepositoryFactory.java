package br.com.geocab.infrastructure.jcr.modeshape;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.LoginException;
import javax.jcr.PropertyType;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

import org.infinispan.schematic.document.ParsingException;
import org.modeshape.jcr.ConfigurationException;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;


/**
 * {@link FactoryBean} which uses ModeShape to provide {@link Repository} instances
 *
 * @author rodrigofraga
 * @since Sep 11, 2013
 * @version 1.0
 * @category
 */
public class ModeShapeRepositoryFactory implements FactoryBean<Repository>
{
	/**
	 * 
	 */
	private static final Logger	LOG	= Logger.getLogger(ModeShapeRepositoryFactory.class.getName());
	/**
	 * Tempo maximo para o timeout
	 */
	private static final long TEMPO_MAXIMO_PARA_TIMEOUT = 10;
	/**
	 * 
	 */
	private static final ModeShapeEngine ENGINE	= new ModeShapeEngine();

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	private Resource configuration;
	/**
	 * 
	 */
	private Repository repository;

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 *
	 * @throws ParsingException
	 * @throws IOException
	 * @throws ConfigurationException
	 * @throws RepositoryException
	 */
	@PostConstruct
	public void start() throws ParsingException, IOException, ConfigurationException, RepositoryException
	{
		Assert.notNull(configuration, "The repository configuration file is required");
		
		ENGINE.start();
		
		final RepositoryConfiguration repositoryConfiguration = RepositoryConfiguration.read( configuration.getURL() );
		
		repository = ENGINE.deploy(repositoryConfiguration);
		
		ENGINE.startRepository(repositoryConfiguration.getName());
		
		this.createCustomTypes();
	}

	/**
	 * 
	 *
	 * @throws LoginException
	 * @throws RepositoryException
	 */
	@SuppressWarnings("unchecked")
	private void createCustomTypes() throws LoginException, RepositoryException
	{
		final NodeTypeManager nodeTypeManager = this.repository.login().getWorkspace().getNodeTypeManager();
		
		if ( nodeTypeManager.hasNodeType(MetadataNodeType.NAME) ) return;
		
		final NodeTypeTemplate metadataNodeType = nodeTypeManager.createNodeTypeTemplate();
		metadataNodeType.setName( MetadataNodeType.NAME );
		metadataNodeType.setMixin(true);
		
		final PropertyDefinitionTemplate idProperty = nodeTypeManager.createPropertyDefinitionTemplate();
		idProperty.setName( MetadataNodeType.PROPERTY_ID );
		idProperty.setMandatory(true);
		idProperty.setRequiredType(PropertyType.STRING); 
		metadataNodeType.getPropertyDefinitionTemplates().add(idProperty);
		
		final PropertyDefinitionTemplate filenameProperty = nodeTypeManager.createPropertyDefinitionTemplate();
		filenameProperty.setName( MetadataNodeType.PROPERTY_FILENAME );
		filenameProperty.setMandatory(true);
		filenameProperty.setRequiredType(PropertyType.STRING); 
		metadataNodeType.getPropertyDefinitionTemplates().add(filenameProperty);
		
		final PropertyDefinitionTemplate createdByProperty = nodeTypeManager.createPropertyDefinitionTemplate();
		createdByProperty.setName( MetadataNodeType.PROPERTY_CREATED_BY );
		createdByProperty.setRequiredType(PropertyType.STRING); 
		metadataNodeType.getPropertyDefinitionTemplates().add(createdByProperty);
		
		final PropertyDefinitionTemplate descriptionProperty = nodeTypeManager.createPropertyDefinitionTemplate();
		descriptionProperty.setName( MetadataNodeType.PROPERTY_DESCRIPTION );
		descriptionProperty.setRequiredType(PropertyType.STRING); 
		metadataNodeType.getPropertyDefinitionTemplates().add(descriptionProperty);
		
		nodeTypeManager.registerNodeType(metadataNodeType, false);
	}

	/**
	 * 
	 */
	@PreDestroy
	public void stop()
	{
		try
		{
			ENGINE.shutdown().get(TEMPO_MAXIMO_PARA_TIMEOUT, TimeUnit.SECONDS);
		}
		catch ( Exception e )
		{
			LOG.severe("Error while waiting for the ModeShape engine to shutdown");
		}
	}

	/*-------------------------------------------------------------------
	 *				 		    GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Override
	public Repository getObject()
	{
		return repository;
	}

	/**
	 * 
	 */
	@Override
	public Class<?> getObjectType()
	{
		return javax.jcr.Repository.class;
	}

	/**
	 * 
	 */
	@Override
	public boolean isSingleton()
	{
		return true;
	}

	/**
	 *
	 * @param configuration
	 */
	public void setConfiguration( Resource configuration )
	{
		this.configuration = configuration;
	}
}
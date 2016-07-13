package br.com.geocab.domain.service;

import java.util.List;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.geocab.domain.entity.configuration.Configuration;
import br.com.geocab.domain.entity.configuration.account.UserRole;
import br.com.geocab.domain.repository.configuration.IConfigurationRepository;

/**
 * 
 * @author Emanuel Victor de Oliveira Fonseca
 * @since 29/06/2016
 * @version 1.0
 * @category Service
 */
@Service
@Transactional
@PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
@RemoteProxy(name = "configurationService")
public class ConfigurationService
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 * User Repository
	 */
	@Autowired
	private IConfigurationRepository configurationRepository;

//	/**
//	 * Logger
//	 */
//	private static final Logger LOG = Logger
//			.getLogger(ConfigurationService.class.getName());


	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * Update a {@link Configuration}
	 * 
	 * @param user
	 * @return
	 */
	public Configuration updateConfiguration(Configuration configuration)
	{
		Assert.notNull(configuration);

		return configurationRepository.save(configuration);
	}
	
	/**
	 * Get a {@link Configuration}
	 * 
	 * @param user
	 * @return
	 */
	public Configuration getConfiguration()
	{
		List<Configuration> configurations = configurationRepository.findAll();
		
		if (configurations.size()>0)
		{
			return configurations.get(0);
		}
		
		// Retorna o primeiro (e único) item da lista
		return new Configuration();
	}
	
}

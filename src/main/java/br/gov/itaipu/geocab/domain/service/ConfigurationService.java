package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.domain.entity.configuration.Configuration;
import br.gov.itaipu.geocab.domain.entity.configuration.account.UserRole;
import br.gov.itaipu.geocab.domain.repository.configuration.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Emanuel Victor de Oliveira Fonseca
 * @version 1.0
 * @category Service
 * @since 29/06/2016
 */
@Service
@Transactional
@PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
public class ConfigurationService {
    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * User Repository
     */
    @Autowired
    private ConfigurationRepository configurationRepository;

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
     * @param configuration
     * @return
     */
    public Configuration updateConfiguration(Configuration configuration) {
        Assert.notNull(configuration);

        return configurationRepository.save(configuration);
    }

    /**
     * Get a {@link Configuration}
     *
     * @return
     */
    public Configuration getConfiguration() {
        List<Configuration> configurations = configurationRepository.findAll();

        if (configurations.size() > 0) {
            return configurations.get(0);
        }

        // Retorna o primeiro (e único) item da lista
        return new Configuration();
    }

}

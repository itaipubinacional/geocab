/**
 * 
 */
package br.com.geocab.domain.repository.configuration;

import br.com.geocab.domain.entity.configuration.Configuration;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author Emanuel Victor
 *
 */
public interface IConfigurationRepository  extends IDataRepository<Configuration, Long>
{
}

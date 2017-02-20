/**
 *
 */
package br.gov.itaipu.geocab.domain.repository.configuration;

import br.gov.itaipu.geocab.domain.entity.configuration.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Emanuel Victor
 */
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
}

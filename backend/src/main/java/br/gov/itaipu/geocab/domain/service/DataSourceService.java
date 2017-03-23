/**
 *
 */
package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.repository.datasource.DataSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.logging.Logger;


/**
 * Class to manage of entities {@link DataSource}
 *
 * @author Cristiano Correa
 * @version 1.0
 * @category Service
 * @since 27/05/2014
 */

@Service
@Transactional
public class DataSourceService {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     * Log
     */
    private static final Logger LOG = Logger.getLogger(DataSourceService.class.getName());

    /**
     * I18n
     */
    @Autowired
    private MessageSource messages;

    /**
     * Repository of {@link DataSource}
     */
    @Autowired
    private DataSourceRepository dataSourceRepository;

	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * Method to insert an {@link DataSource}
     *
     * @param dataSource
     * @return DataSource
     */
    public DataSource insertDataSource(DataSource dataSource) {
        try {
            // limpa o ID para evitar que haja duplicação
            dataSource.setId(0L);
            dataSource = this.dataSourceRepository.save(dataSource);
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
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
    public DataSource updateDataSource(DataSource dataSource) {

        try {
            DataSource oldDataSource = this.dataSourceRepository.findOne(dataSource.getId());

            /*
             * Checa se a fonte de dados mudou de interna para externa e vice-versa. Caso
             * isto ocorra, impede a alteração caso existam camadas associadas a esta fonte.
             */
            if (oldDataSource.isInternal() != dataSource.isInternal() &&
                    oldDataSource.getLayers().size() > 0)
                throw new DataIntegrityViolationException("Não é possível alterar a fonte de dados");

            dataSource = this.dataSourceRepository.save(dataSource);
            return dataSource;
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
            final String error = e.getCause().getCause().getMessage();

            this.dataIntegrityViolationException(error);
        }
        return null;
    }

    /**
     * Method to remove an {@link DataSource}
     *
     * @param id
     */
    public void removeDataSource(Long id) {
        this.dataSourceRepository.delete(id);
    }

    /**
     * Method to find an {@link DataSource} by id
     *
     * @param id
     * @return dataSource
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public DataSource findDataSourceById(Long id) {
        return this.dataSourceRepository.findOne(id);
    }

    /**
     * Method to list all {@link DataSource}
     *
     * @return dataSource
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public List<DataSource> listAllDataSource() {
        return this.dataSourceRepository.listAll();
    }

    /**
     * Method to list {@link DataSource} pageable with filter options
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<DataSource> listDataSourceByFilters(String filter, PageRequest pageable) {
        return this.dataSourceRepository.listByFilters(filter, pageable);
    }

    /**
     * @param filter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<DataSource> listInternalDataSourceByFilters(String filter, PageRequest pageable) {
        return this.dataSourceRepository.listInternalDatasourceByFilters(filter, pageable);
    }

    /**
     * Method to verify DataIntegrityViolations and throw IllegalArgumentException with the field name
     *
     * @param error
     * @return void
     * @throws IllegalArgumentException
     */
    private void dataIntegrityViolationException(String error) {
        String fieldError = "";

        if (error.contains("uk_data_source_name")) {
            fieldError = this.messages.getMessage("Name", new Object[]{}, null);
        } else if (error.contains("uk_data_source_url")) {
            fieldError = this.messages.getMessage("Address", new Object[]{}, null);
        }

        if (!fieldError.isEmpty()) {
            throw new IllegalArgumentException(this.messages.getMessage("The-field-entered-already-exists,-change-and-try-again", new Object[]{fieldError}, null));
        }
    }

}
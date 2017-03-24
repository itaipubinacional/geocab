package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.repository.datasource.DataSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;


/**
 * Classe de serviços para gerenciamento das fontes de dados.
 */
@Service
@Transactional
public class DataSourceService {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

    private static final Logger LOG = Logger.getLogger(DataSourceService.class.getName());

    @Autowired
    private MessageSource messages;

    @Autowired
    private DataSourceRepository dataSourceRepository;

	/*-------------------------------------------------------------------
     *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * Insere uma nova fonte de dados.
     *
     * @param dataSource O objeto da fonte de dados.
     * @return A fonte de dados adicionada com o ID ajustado.
     */
    public DataSource insertDataSource(DataSource dataSource) {
        // limpa o ID para evitar que haja duplicação
        dataSource.setId(0L);
        dataSource = this.dataSourceRepository.save(dataSource);
        return dataSource;
    }

    /**
     * Atualiza uma fonte de dados existente ou cria uma nova caso ela não
     * estiver cadastrada.
     *
     * @param dataSource O objeto da fonte de dados.
     * @return A fonte de dados atualizada ou criada.
     */
    public DataSource updateDataSource(DataSource dataSource) {

        DataSource oldDataSource = this.dataSourceRepository.findOne(dataSource.getId());
        // se existir
        if (oldDataSource != null) {
            /*
             * Checa se a fonte de dados mudou de interna para externa e vice-versa. Caso
             * isto ocorra, impede a alteração caso existam camadas associadas a esta fonte.
             */
            if (oldDataSource.isInternal() != dataSource.isInternal() &&
                    oldDataSource.getLayers().size() > 0)
                throw new RuntimeException("Não é possível alterar a fonte de dados");
            // ajusta o ID para evitar a criação de um novo
            dataSource.setId(oldDataSource.getId());
        } else
            // adiciona a fonte em vez disso
            dataSource.setId(0L);

        dataSource = this.dataSourceRepository.save(dataSource);
        return dataSource;
    }

    /**
     * Remove a fonte de dados passada.
     *
     * @param dataSource O objeto da fonte de dados a ser removida.
     * @return Retorna <code>true</code> se a fonte for removida. Caso contrário,
     * retorna <code>false</code>.
     */
    public boolean removeDataSource(DataSource dataSource) {
        // busca antes de apagar
        dataSource = this.dataSourceRepository.findOne(dataSource.getId());
        if (dataSource != null)
            this.dataSourceRepository.delete(dataSource);
        return dataSource != null;
    }

    /**
     * Busca uma fonte de dados pelo ID passado.
     *
     * @param id O ID da fonte de dados a ser buscada.
     * @return Retorna o objeto da fonte de dados caso ela existir. Caso contrário,
     * retorna <code>null</code>.
     */
    @Transactional(readOnly = true)
    public DataSource getDataSource(long id) {
        return this.dataSourceRepository.findOne(id);
    }

    /**
     * Retorna a lista de todas as fontes de dados cadastradas.
     *
     * @return A lista de fontes de dados.
     */
    @Transactional(readOnly = true)
    public List<DataSource> getDataSources() {
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
}
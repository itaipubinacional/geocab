package br.gov.itaipu.geocab.domain.repository.datasource;

import java.util.List;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Rodrigo P. Fraga
 * @version 1.0
 * @category Repository
 * @since 22/04/2014
 */
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {

	/*-------------------------------------------------------------------
     *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param filter
     * @param pageable
     * @return
     */
    @Query(value = "SELECT new DataSource( dataSource.id, dataSource.name, dataSource.url, dataSource.login, dataSource.password, dataSource.serviceType ) " +
            "FROM DataSource dataSource " +
            "WHERE  ( ( LOWER(dataSource.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
            "OR ( LOWER(dataSource.url) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ))")
    Page<DataSource> listByFilters(@Param("filter") String filter, Pageable pageable);

    /**
     * @param filter
     * @param pageable
     * @return
     */
    @Query(value = "SELECT new DataSource( dataSource.id, dataSource.name, dataSource.url, dataSource.login, dataSource.password, dataSource.serviceType ) " +
            "FROM DataSource dataSource " +
            "WHERE  ( (( LOWER(dataSource.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
            "OR ( LOWER(dataSource.url) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL )) AND (dataSource.url IS NULL ))")
    Page<DataSource> listInternalDatasourceByFilters(@Param("filter") String filter, Pageable pageable);


    /**
     * @return
     */
    @Query(value = "SELECT new DataSource( dataSource.id, dataSource.name, dataSource.url, dataSource.serviceType ) " +
            "FROM DataSource dataSource ")
    List<DataSource> listAll();
}

package br.com.geocab.domain.repository.datasource;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * 
 * @author Rodrigo P. Fraga 
 * @since 22/04/2014
 * @version 1.0
 * @category Repository
 */
public interface IDataSourceRepository extends IDataRepository<DataSource, Long>
{

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/	
	/**
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT new DataSource( dataSource.id, dataSource.name, dataSource.url, dataSource.login, dataSource.password ) " +
				"FROM DataSource dataSource " +
				"WHERE  ( ( LOWER(dataSource.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
				"OR ( LOWER(dataSource.url) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ))" )
	public Page<DataSource> listByFilters( @Param("filter") String filter, Pageable pageable );
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new DataSource( dataSource.id, dataSource.name, dataSource.url ) " +
				"FROM DataSource dataSource " )
	public List<DataSource> listAll();
}

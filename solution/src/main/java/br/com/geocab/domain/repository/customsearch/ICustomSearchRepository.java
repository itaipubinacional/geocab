/**
 * 
 */
package br.com.geocab.domain.repository.customsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.layer.CustomSearch;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;


/**
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/12/2014
 * @version 1.0
 * @category Repository
 */
public interface ICustomSearchRepository extends IDataRepository<CustomSearch, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/	
	/**
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT new CustomSearch( customSearch.id, customSearch.name, layer.id, layer.name, layer.title, layer.icon, dataSource ) " +
				"FROM CustomSearch customSearch " +
				"LEFT OUTER JOIN customSearch.layer layer " + 
				"LEFT OUTER JOIN layer.dataSource dataSource " +
				"WHERE  ( ( LOWER(customSearch.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
				"OR ( LOWER(layer.title) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " + 
				"OR ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
				"OR ( LOWER(dataSource.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) )" )
	public Page<CustomSearch> listByFilters( @Param("filter") String filter, Pageable pageable );
	
	
	

	/**
	 * @param id
	 * @return
	 */
	@Query(value="SELECT new CustomSearch( customSearch.id, customSearch.name, customSearch.layer.id, customSearch.layer.name, customSearch.layer.title, customSearch.layer.icon , customSearch.layer.dataSource ) " +
			"FROM CustomSearch customSearch " +
			"WHERE  ( customSearch.id = :id )" )
	public CustomSearch findById( @Param("id") Long id);
	
	/**
	 * @param id
	 * @return
	 */
	@Query(value="SELECT new Layer( customSearch.layer.id ) " +
			"FROM CustomSearch customSearch " +
			"WHERE  ( customSearch.layer.id = :id )" )
	public Layer getFindLayerById( @Param("id") Long id);
	
}

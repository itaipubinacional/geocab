/**
 * 
 */
package br.com.geocab.domain.repository.accessgroup;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.accessgroup.AccessGroupCustomSearch;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * 
 * @author Thiago Rossetto Afonso
 * @since 01/12/2014
 * @version 1.0
 * @category Repository
 */
public interface IAccessGroupCustomSearchRepository extends IDataRepository<AccessGroupCustomSearch, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 * @param searchId
	 * @return
	 */
	@Query(value="SELECT new AccessGroupCustomSearch( accessGroupCustomSearch.id, accessGroupCustomSearch.accessGroup, accessGroupCustomSearch.customSearch ) " +
				"FROM AccessGroupCustomSearch accessGroupCustomSearch " +
				"WHERE  accessGroupCustomSearch.customSearch.id = :searchId " )
	public List<AccessGroupCustomSearch> listByCustomSearchId( @Param("searchId") Long searchId );
	
	/**
	 * 
	 * @param groupId
	 * @return
	 */
	@Query(value="SELECT new AccessGroupCustomSearch( accessGroupCustomSearch.id, accessGroupCustomSearch.accessGroup, accessGroupCustomSearch.customSearch ) " +
			"FROM AccessGroupCustomSearch accessGroupCustomSearch " +
			"WHERE  accessGroupCustomSearch.accessGroup.id = :groupId " )
	public List<AccessGroupCustomSearch> listByAccessGroupId( @Param("groupId") Long groupId );
	
	/**
	 * 
	 * @param groupId
	 * @param customSearchId
	 * @return
	 */
	@Query(value="SELECT new AccessGroupCustomSearch( accessGroupCustomSearch.id, accessGroupCustomSearch.accessGroup, accessGroupCustomSearch.customSearch ) " +
			"FROM AccessGroupCustomSearch accessGroupCustomSearch " +
			"WHERE  ((accessGroupCustomSearch.accessGroup.id = :groupId) "
			+ "AND (accessGroupCustomSearch.customSearch.id = :customSearchId))" )
	public List<AccessGroupCustomSearch> listByAccessGroupCustomSearchId( @Param("groupId") Long groupId, @Param("customSearchId") Long customSearchId );
}

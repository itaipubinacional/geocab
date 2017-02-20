/**
 *
 */
package br.gov.itaipu.geocab.domain.repository.accessgroup;

import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroupCustomSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @category Repository
 * @since 01/12/2014
 */
public interface AccessGroupCustomSearchRepository extends JpaRepository<AccessGroupCustomSearch, Long> {
    /*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param searchId
     * @return
     */
    @Query(value = "SELECT new AccessGroupCustomSearch( accessGroupCustomSearch.id, accessGroupCustomSearch.accessGroup.id, accessGroupCustomSearch.accessGroup.name, accessGroupCustomSearch.accessGroup.description, accessGroupCustomSearch.customSearch.id ) " +
            "FROM AccessGroupCustomSearch accessGroupCustomSearch " +
            "WHERE  accessGroupCustomSearch.customSearch.id = :searchId ")
    List<AccessGroupCustomSearch> listByCustomSearchId(@Param("searchId") Long searchId);

    /**
     * @param groupId
     * @return
     */
    @Query(value = "SELECT new AccessGroupCustomSearch( accessGroupCustomSearch.id, accessGroupCustomSearch.accessGroup, accessGroupCustomSearch.customSearch ) " +
            "FROM AccessGroupCustomSearch accessGroupCustomSearch " +
            "WHERE  accessGroupCustomSearch.accessGroup.id = :groupId ")
    List<AccessGroupCustomSearch> listByAccessGroupId(@Param("groupId") Long groupId);

    /**
     * @param groupId
     * @return
     */
    @Query(value = "SELECT new AccessGroupCustomSearch( accessGroupCustomSearch.id, accessGroupCustomSearch.id, accessGroupCustomSearch.customSearch.id ) " +
            "FROM AccessGroupCustomSearch accessGroupCustomSearch " +
            "WHERE  accessGroupCustomSearch.accessGroup.id = :groupId ")
    List<AccessGroupCustomSearch> listByAccessGroupId(@Param("groupId") Long groupId, Pageable pageable);

    /**
     * @param groupId
     * @param customSearchId
     * @return
     */
    @Query(value = "SELECT new AccessGroupCustomSearch( accessGroupCustomSearch.id, accessGroupCustomSearch.accessGroup, accessGroupCustomSearch.customSearch ) " +
            "FROM AccessGroupCustomSearch accessGroupCustomSearch " +
            "WHERE  ((accessGroupCustomSearch.accessGroup.id = :groupId) "
            + "AND (accessGroupCustomSearch.customSearch.id = :customSearchId))")
    List<AccessGroupCustomSearch> listByAccessGroupCustomSearchId(@Param("groupId") Long groupId, @Param("customSearchId") Long customSearchId);
}

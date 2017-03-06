/**
 *
 */
package br.gov.itaipu.geocab.domain.repository.accessgroup;

import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * @author Thiago
 * @version 1.0
 * @category Repository
 * @since 03/12/2014
 */
public interface AccessGroupRepository extends JpaRepository<AccessGroup, Long> {
    /*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/


    /**
     * @param filter
     * @param pageable
     * @return
     * @TODO Adicionar demais filtros que faltam: Grupo, usu�rio, camada, pesquisa personalizada e ferramenta quando desenvolver esta funcionalidade
     */
    @Query(value = "SELECT DISTINCT new AccessGroup ( accessGroup.id, accessGroup.name, accessGroup.description ) " +
            "FROM AccessGroup accessGroup " +
            "LEFT JOIN accessGroup.users user " +
            "LEFT JOIN accessGroup.tools tool " +
            "LEFT JOIN accessGroup.accessGroupLayer accessGroupLayer " +
            "LEFT JOIN accessGroupLayer.layer layer " +
            "LEFT JOIN accessGroup.accessGroupCustomSearch accessGroupSearch " +
            "LEFT JOIN accessGroupSearch.customSearch search " +
            "WHERE  ( ( LOWER(accessGroup.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
            "OR ( LOWER(user.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL )" +
            "OR ( LOWER(tool.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL )" +
            "OR ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL)" +
            "OR ( LOWER(search.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL)" +
            "OR ( LOWER(accessGroup.description) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) )")
    Page<AccessGroup> listByFilters(@Param("filter") String filter, Pageable pageable);

    /**
     */
    @Query(value = "SELECT new AccessGroup ( accessGroup.id, accessGroup.name, accessGroup.description ) " +
            "FROM AccessGroup accessGroup " +
            "INNER JOIN accessGroup.users user " +
            "WHERE user.email = :email")
    List<AccessGroup> listByUser(@Param("email") String email);

    /**
     * @return
     */
    @Query(value = "SELECT new AccessGroup ( accessGroup.id, accessGroup.name, accessGroup.description ) " +
            "FROM AccessGroup accessGroup " +
            "WHERE accessGroup.id = 1")
    List<AccessGroup> listPublicGroups();

    /**
     * @return
     */
    @Query(value = "SELECT new AccessGroup ( accessGroup.id, accessGroup.name, accessGroup.description ) " +
            "FROM AccessGroup accessGroup " +
            "WHERE accessGroup.id = :id")
    AccessGroup findById(@Param("id") Long id);

}

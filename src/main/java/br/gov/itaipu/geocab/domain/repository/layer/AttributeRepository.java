package br.gov.itaipu.geocab.domain.repository.layer;

import br.gov.itaipu.geocab.domain.entity.layer.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @category Repository
 * @since 03/10/2014
 */
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    /*-------------------------------------------------------------------
     *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param layerId
     * @return
     */
    @Query(value = "SELECT new Attribute( attribute.id, attribute.name, attribute.type, attribute.required, attribute.orderAttribute, attribute.visible ) " +
            "FROM Attribute attribute " +
            "LEFT OUTER JOIN attribute.layer layer " +
            "WHERE ( layer.id = :layerId ) " +
            "ORDER BY attribute.orderAttribute ASC")
    List<Attribute> listAttributeByLayer(@Param("layerId") Long layerId);

    /**
     * @param layerId
     * @return
     */
    @Query(value = "SELECT new Attribute( attribute.id, attribute.name, attribute.required, attribute.type, attribute.orderAttribute, attribute.visible ) " +
            "FROM Attribute attribute " +
            "LEFT OUTER JOIN attribute.layer layer " +
            "WHERE ( layer.id = :layerId ) " +
            "ORDER BY attribute.orderAttribute ASC")
    List<Attribute> listAttributeByLayerMarker(@Param("layerId") Long layerId);

    /**
     * @return
     */
    @Query(value = "SELECT new Attribute( attribute.id, attribute.name, attribute.type, attribute.required, attribute.orderAttribute, attribute.visible ) " +
            "FROM Attribute attribute " +
            "WHERE ( attribute.id = :id ) " +
            "ORDER BY attribute.orderAttribute ASC")
    Attribute findById(@Param("id") Long id);

}

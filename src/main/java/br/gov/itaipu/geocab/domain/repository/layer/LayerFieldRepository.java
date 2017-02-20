/**
 *
 */
package br.gov.itaipu.geocab.domain.repository.layer;

import br.gov.itaipu.geocab.domain.entity.layer.LayerField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author emanuelvictor
 */
public interface LayerFieldRepository extends JpaRepository<LayerField, Long> {
    /**
     * @return
     */
    @Query(value = "SELECT new LayerField( layerField.id, layerField.name, layerField.label, layerField.orderLayer, layerField.type, layerField.attributeId ) " +
            "FROM LayerField layerField " +
            "WHERE  layerField.customSearchId = :customSearchId ")
    List<LayerField> findByCustomSearchId(@Param("customSearchId") Long customSearchId);
}

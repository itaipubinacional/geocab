/**
 * 
 */
package br.com.geocab.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.layer.LayerField;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author emanuelvictor
 *
 */
public interface ILayerFieldRepository extends IDataRepository<LayerField, Long>
{
	
	
	

	/**
	 * 
	 * @param searchId
	 * @return
	 */
	@Query(value="SELECT new LayerField( layerField.id, layerField.name, layerField.label, layerField.orderLayer, layerField.type, layerField.attributeId ) " +
				"FROM LayerField layerField " + 
				"WHERE  layerField.customSearchId = :customSearchId " )
	public List<LayerField> findByCustomSearchId( @Param("customSearchId") Long customSearchId );
}

package br.com.geocab.domain.repository.attribute;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author Thiago Rossetto Afonso
 * @since 03/10/2014
 * @version 1.0
 * @category Repository
 *
 */
public interface IAttributeRepository extends IDataRepository<Attribute, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/	
	
	/**
	 * @param layerId
	 * @return
	 */
	@Query(value="SELECT new Attribute( attribute.id, attribute.name, attribute.type ) " +
				"FROM Attribute attribute " +
				"LEFT OUTER JOIN attribute.layer layer " + 
				"WHERE ( :layerId = layer.id ) ")
	public List<Attribute> listAttributeByLayer( @Param("layerId") Long layerId );

}

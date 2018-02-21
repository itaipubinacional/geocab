package br.com.geocab.domain.repository.layergroup;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.layer.AttributeOption;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

public interface IAttributeOptionRepository extends IDataRepository<AttributeOption, Long>
{

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/	
	
	/**
	 * 
	 * @param attributeId
	 * @return
	 */
	@Query( value = "SELECT new AttributeOption( attributeOption.id, attributeOption.description, attributeOption.attribute.id )  "
			+ "FROM AttributeOption attributeOption "
			+ "WHERE attributeOption.attribute.id = :attributeId" )
	public List<AttributeOption> listByAttributeId( @Param("attributeId") long attributeId );
	
}

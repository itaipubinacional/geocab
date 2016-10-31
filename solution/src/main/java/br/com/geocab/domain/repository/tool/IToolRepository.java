package br.com.geocab.domain.repository.tool;

import br.com.geocab.domain.entity.tool.Tool;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;


/**
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/12/2014
 * @version 1.0
 * @category Repository
 */
public interface IToolRepository extends IDataRepository<Tool, Long>
{

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/	
}

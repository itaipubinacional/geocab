package br.gov.itaipu.geocab.domain.repository.tool;


import br.gov.itaipu.geocab.domain.entity.tool.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @category Repository
 * @since 03/12/2014
 */
public interface ToolRepository extends JpaRepository<Tool, Long> {

	/*-------------------------------------------------------------------
     *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
}

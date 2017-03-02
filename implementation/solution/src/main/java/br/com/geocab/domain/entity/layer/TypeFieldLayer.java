/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * @author Vinicius Ramos Kawamoto
 * @since 22/09/2014
 * @version 1.0
 * @category
 */
@DataTransferObject(type="enum")
public enum TypeFieldLayer
{
	
	/*-------------------------------------------------------------------
	 *				 		     ENUMS
	 *-------------------------------------------------------------------*/
	//Não mudar este ordem pois está sendo usado de forma hierarquica
	//A ordem tambem é utilizada para definir o nivel 
	STRING, NUMBER, SHORT, INT, DECIMAL, DOUBLE, DATETIME;
	
	/**
	 *
	 * @return o valor ordinal do enum
	 */
	public int getOrdinal()
	{
		return this.ordinal();
	}
}
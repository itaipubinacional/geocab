/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * Classe responsável por definir o comportamento de uma {@link LayerFieldType}
 * 
 * @author Vagner B.C
 * @since 06/08/2014
 * @version 1.0
 * @category
 */
@DataTransferObject(type="enum")
public enum LayerFieldType
{
	
	/*-------------------------------------------------------------------
	 *				 		     ENUMS
	 *-------------------------------------------------------------------*/
	//Não mudar este ordem pois está sendo usado de forma hierarquica
	//A ordem tambem é utilizada para definir o nivel 
	STRING,
	NUMBER,
	SHORT,
	INT,
	DECIMAL,
	DOUBLE,
	DATETIME,
	DATE,
	BOOLEAN;
	
	/**
	 *
	 * @return o valor ordinal do enum
	 */
	public int getOrdinal()
	{
		return this.ordinal();
	}
}
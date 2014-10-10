/**
 * 
 */
package br.com.geocab.domain.entity.marker;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * @author Thiago Rossetto Afonso
 * @since 30/09/2014
 * @version 1.0
 * 
 */
@DataTransferObject(type="enum")
public enum StatusMarker
{
	
	/*-------------------------------------------------------------------
	 *				 		     ENUMS
	 *-------------------------------------------------------------------*/
	//Do not change this order
	ACCEPTED, REFUSED, PENDING;
	
	/**
	 *
	 * @return value of enum
	 */
	public int getOrdinal()
	{
		return this.ordinal();
	}
}
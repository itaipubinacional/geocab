/**
 * 
 */
package br.com.geocab.domain.entity.markermoderation;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * @author Vinicius Ramos Kawamoto
 * @since 13/01/2015
 * @version 1.0
 * 
 */
@DataTransferObject(type="enum")
public enum MarkerModerationStatus
{
	
	/*-------------------------------------------------------------------
	 *				 		     ENUMS
	 *-------------------------------------------------------------------*/
	//Do not change this order
	PENDING, ACCEPTED, REFUSED;
	
	/**
	 *
	 * @return value of enum
	 */
	public int getOrdinal()
	{
		return this.ordinal();
	}
}
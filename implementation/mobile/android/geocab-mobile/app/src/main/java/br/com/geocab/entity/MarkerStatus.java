/**
 * 
 */
package br.com.geocab.entity;

/**
 * 
 * @author Thiago Rossetto Afonso
 * @since 30/09/2014
 * @version 1.0
 * 
 */
public enum MarkerStatus
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
/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * @author Vinicius Ramos Kawamoto
 * @since 19/09/2014
 * @version 1.0
 * 
 */
@DataTransferObject(type="enum")
public enum MapScale
{
	
	/*-------------------------------------------------------------------
	 *				 		     ENUMS
	 *-------------------------------------------------------------------*/
	//Do not change this order
	//UM70M, UM35M, UM18M, UM9M, UM4M, UM2M, UM1M, UM528K, UM274K, UM137K, UM68K, UM34K, UM17K, UM8561, UM4280, UM2140;
	UM500km, UM200km, UM100km, UM50km, UM20km, UM10km, UM5km, UM2km, UM1km, UM500m, UM200m, UM100m, UM50m, UM20m;
	/**
	 *
	 * @return value of enum
	 */
	public int getOrdinal()
	{
		return this.ordinal();
	}
}
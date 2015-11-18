/**
 * 
 */
package br.com.geocab.domain.entity.account.preferences;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * @author emanuelvictor
 *
 */
@DataTransferObject
public enum Coordinates
{

	/*-------------------------------------------------------------------
	 *								ENUMS
	 *-------------------------------------------------------------------*/
	DEGRESS, // 0
	DEGRESS_DECIMAL, // 1
	DEGRESS_MINUTES_SECONDS; // 2
}

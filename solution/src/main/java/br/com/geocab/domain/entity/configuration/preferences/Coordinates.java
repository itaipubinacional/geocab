/**
 * 
 */
package br.com.geocab.domain.entity.configuration.preferences;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * @author emanuelvictor
 *
 */
@DataTransferObject(type="enum", javascript="Coordinates")
public enum Coordinates
{

	/*-------------------------------------------------------------------
	 *								ENUMS
	 *-------------------------------------------------------------------*/
	DEGREES_DECIMAL, // 0
	DEGREES_MINUTES_SECONDS; // 1
}

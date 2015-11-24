/**
 * 
 */
package br.com.geocab.domain.entity.account.preferences;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * @author emanuelvictor
 *
 */
@DataTransferObject(type = "enum", javascript = "BackgroundMap")
public enum BackgroundMap
{

	/*-------------------------------------------------------------------
	 *								ENUMS
	 *-------------------------------------------------------------------*/

	// GOOGLE MAPS CONFIGURATIONS
	GOOGLE_MAP, // 0
	GOOGLE_MAP_TERRAIN, // 1
	GOOGLE_SATELLITE, // 2
	GOOGLE_SATELLITE_LABELS, // 3

	// MAP QUEST
	MAP_QUEST_OSM, // 4
	MAP_QUEST_SAT, // 5

	// OPEN STREET MAP
	OPEN_STREET_MAP; // 6

}

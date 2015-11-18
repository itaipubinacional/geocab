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
public enum BackgroundMap
{

	/*-------------------------------------------------------------------
	 *								ENUMS
	 *-------------------------------------------------------------------*/

	// GOOGLE MAPS CONFIGURATIONS
	GOOGLE, // 0
	GOOGLE_MAP, // 1
	GOOGLE_MAP_TERRAIN, // 2
	GOOGLE_SATELITE, // 3
	GOOGLE_SATELITE_LABELS, // 4

	// MAP QUEST
	MAP_QUEST, // 5
	MAP_QUEST_OSM, // 6
	MAP_QUEST_SAT, // 7

	// OPEN STREET MAP
	OPEN_STREET_MAP; // 8

}

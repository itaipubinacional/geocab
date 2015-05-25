package br.com.geocab.entity;

import java.io.Serializable;

import br.com.geocab.entity.Marker;
import br.com.geocab.entity.MarkerStatus;

/**
 * 
 * @author Joaz Vieira Soares
 * @since 09/01/2015
 * @version 1.0
 * @category Entity
 */
public class MarkerModeration implements Serializable
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1358373183244765164L;

	private Marker marker;
	
	private MarkerStatus status;
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	public MarkerModeration()
	{
		
	}

	public MarkerModeration( Long id, MarkerStatus status, Marker marker)
	{
		this.setStatus(status);
		this.setMarker(marker);
	}
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/


	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @return the marker
	 */
	public Marker getMarker()
	{
		return marker;
	}
	/**
	 * @param marker the marker to set
	 */
	public void setMarker(Marker marker)
	{
		this.marker = marker;
	}
	/**
	 * @return the status
	 */
	public MarkerStatus getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(MarkerStatus status)
	{
		this.status = status;
	}
	
}

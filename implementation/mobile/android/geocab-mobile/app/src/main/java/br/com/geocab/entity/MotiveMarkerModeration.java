package br.com.geocab.entity;

import java.io.Serializable;

/**
 * 
 * @author Joaz Vieira Soares
 * @since 09/01/2015
 * @version 1.0
 * @category Entity
 */
public class MotiveMarkerModeration implements Serializable
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 *
	 */
	private static final long serialVersionUID = -1358373183244765164L;

	private String description;

	private Motive motive;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	/**
	 *
	 */
	public MotiveMarkerModeration()
	{

	}


	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/


	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Motive getMotive() {
        return motive;
    }

    public void setMotivo(Motive motivo) {
        this.motive = motivo;
    }
}

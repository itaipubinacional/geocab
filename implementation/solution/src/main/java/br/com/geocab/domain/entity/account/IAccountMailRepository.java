package br.com.geocab.domain.entity.account;

import java.util.concurrent.Future;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.markermoderation.MotiveMarkerModeration;

/**
 * Interface para o envio de e-mails
 *
 * @author Rodrigo P. Fraga
 * @since 02/10/2014
 * @version 1.0
 * @category Mail
 */
public interface IAccountMailRepository
{
	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * @param user
	 */
	public Future<Void> sendRecoveryPassword( User user );
	
	/**
	 * @param user
	 * @param marker
	 */
	public Future<Void> sendMarkerAccepted( User user, Marker marker );
	
	/**
	 * @param user
	 * @param marker
	 */
	public Future<Void> sendMarkerRefused( User user, Marker marker, MotiveMarkerModeration motiveMarkerModeration );
}
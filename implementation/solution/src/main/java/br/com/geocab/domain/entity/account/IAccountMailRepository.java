package br.com.geocab.domain.entity.account;

import java.util.concurrent.Future;

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
}
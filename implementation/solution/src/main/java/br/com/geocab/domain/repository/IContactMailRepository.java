package br.com.geocab.domain.repository;

import java.util.concurrent.Future;

import br.com.geocab.domain.entity.configuration.account.Email;

/**
 * Interface para o envio de e-mails de contato
 * 
 * @author emanuelvictor
 *
 */
public interface IContactMailRepository
{
	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * @param email
	 * @return
	 */
	public Future<Void> sendContactUs(final Email email);
}
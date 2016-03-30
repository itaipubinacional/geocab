/**
 * 
 */
package br.com.geocab.infrastructure.social.mobile;

import javax.servlet.http.HttpServletRequest;

import br.com.geocab.domain.entity.account.User;

/**
 * @author emanuelvictor
 *
 */
public interface Authenticate
{
	/**
	 * Realiza o login do usuário, e devolve uma sessão para o navegador
	 */
	User login(User user, HttpServletRequest request);
	/**
	 * Utilizado para validar o nome do usuário (login) do usuário
	 */
	User validateUsername(User user);
	
	/**
	 * Utilizado para validar o token de acesso do usuário
	 */
	User validateToken(User user);
}

/**
 * 
 */
package br.com.geocab.infrastructure.social.mobile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author emanuelvictor
 *
 */
public interface Authenticate
{
	void validateToken();
	
	String generateToken(String userName);
	
	String login(HttpServletRequest request);
}

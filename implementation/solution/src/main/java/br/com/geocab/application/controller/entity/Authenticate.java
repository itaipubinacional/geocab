/**
 * 
 */
package br.com.geocab.application.controller.entity;

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

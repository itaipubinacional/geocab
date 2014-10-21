package br.com.geocab.application.restful;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.service.LoginService;


/**
 * @author Vinicius Ramos Kawamoto
 * @since 25/09/2014
 * @version 1.0
 * @category Controller
 */

@Controller
@RequestMapping("authentication")
public class AuthenticationRESTful
{

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private LoginService loginService;
	
	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	
	/**
	 * 
	 * @param credentials using base64 form.
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "check", method = RequestMethod.POST)
	public @ResponseBody User checkCredentials( @RequestParam String credentials,
								  HttpServletResponse response ) throws IOException
	{
		try 
		{
			return this.loginService.checkCredentials(credentials);
			
		} 
		catch (Exception e) 
		{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			return null;
		}
	}
	
	
}
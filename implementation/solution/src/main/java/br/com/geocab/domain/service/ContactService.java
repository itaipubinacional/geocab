/**
 * 
 */
package br.com.geocab.domain.service;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.account.Email;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.repository.IContactMailRepository;
import nl.captcha.Captcha;

/**
 * @author emanuelvictor
 *
 */
@Service
@RemoteProxy(name = "contactService")
public class ContactService
{
	
	/**
	 * Log
	 */
	private static final Logger LOG = Logger.getLogger( DataSourceService.class.getName() );
	
	/**
	 * I18n 
	 */
	@Autowired
	private MessageSource messages;
	
	/**
	 * AccountMail Repository
	 */
	@Autowired
	private IContactMailRepository contactMailRepository;

	/**
	 * Insert a new User
	 * 
	 * @param user
	 * @return
	 */
	public Email contactUs(Email email)
	{
		try
		{			
			if (this.getLoggedUser() != null)
			{
				email.setEmail(ContextHolder.getAuthenticatedUser().getUsername());
				email.setName(ContextHolder.getAuthenticatedUser().getName());
			}
			else
			{
				Assert.notNull(getCaptcha());
				email.validate(getCaptcha());
			}
			email.validate();
	
			contactMailRepository.sendContactUs(email);
		}
		catch (Exception e)
		{
			LOG.info(e.getMessage());
			throw new RuntimeException(this.messages.getMessage(e.getMessage(), new Object [] {}, null ));
		}

		return email;
	}

	/**
	 * Insert a new User
	 * 
	 * @param user
	 * @return
	 */
	public User getLoggedUser()
	{
		User user = ContextHolder.getAuthenticatedUser();
		if (user == null || user.getName().equals("Anonymous"))
		{
			return null;
		}
		user.setPassword(null);
		return user;
	}

	/**
	 * Getting the captcha in session
	 * 
	 * @return
	 */
	private Captcha getCaptcha()
	{
		HttpSession session = WebContextFactory.get().getSession();

		return (Captcha) session.getAttribute(Captcha.NAME);
	}
}

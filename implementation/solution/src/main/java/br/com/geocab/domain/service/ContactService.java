/**
 * 
 */
package br.com.geocab.domain.service;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.account.Email;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.repository.IContactMailRepository;

/**
 * @author emanuelvictor
 *
 */
@Service
@RemoteProxy(name = "contactService")
public class ContactService
{

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
		Assert.notNull(email);

		if (this.getLoggedUser().getUsername() != null || !this.getLoggedUser().getRole().equals(User.ANONYMOUS))
		{
			email.setEmail(ContextHolder.getAuthenticatedUser().getUsername());
			email.setName(ContextHolder.getAuthenticatedUser().getName());
		}

		email.validate();

		contactMailRepository.sendContactUs(email);

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
		return ContextHolder.getAuthenticatedUser();
	}
}

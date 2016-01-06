/**
 * 
 */
package br.com.geocab.domain.entity.account;

import javax.mail.internet.InternetAddress;

import org.directwebremoting.annotations.DataTransferObject;
import org.springframework.util.Assert;

import nl.captcha.Captcha;

/**
 * @author emanuelvictor
 * 
 */
@DataTransferObject(javascript = "Email")
public class Email
{
	/**
	 * 
	 */
	private String name;

	/**
	 * 
	 */
	private String email;
	
	/**
	 * 
	 */
	private String subject;

	/**
	 * 
	 */
	private String message;

	/**
	 * 
	 */
	private String answer;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	public Email()
	{
	}

	/**
	 * @param name
	 * @param email
	 * @param subject
	 * @param message
	 * @param answer
	 */
	public Email(String name, String email, String subject, String message, String answer)
	{
		super();
		this.name = name;
		this.email = email;
		this.subject = subject;
		this.message = message;
		this.answer = answer;
	}

	/*-------------------------------------------------------------------
	 *							BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 * @param captcha
	 * @throws Exception
	 */
	public void validate(Captcha captcha) throws Exception
	{
		Assert.isTrue(captcha.isCorrect(this.getAnswer()), "contact.SecurityVerification.Incorrect");

		this.validate();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception
	{
		this.validate(this.getEmail());

		Assert.isTrue(this.getName() != null, "contact.Insert.Name");

		Assert.isTrue(this.getSubject() != null, "contact.Insert.Subject");

		Assert.isTrue(this.getMessage() != null, "contact.Insert.Message");
	}
	
	/**
	 * Valida o email
	 * @param email
	 * @throws Exception
	 */
	public void validate(String email) throws Exception
	{
		Assert.isTrue(this.getEmail() != null, "contact.Insert.Email");
		InternetAddress emailAddr = new InternetAddress(email);
        try
		{
        	emailAddr.validate();	
		}
		catch (Exception e)
		{
			throw new Exception("contact.Email.Invalid");
		}
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer()
	{
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}

}

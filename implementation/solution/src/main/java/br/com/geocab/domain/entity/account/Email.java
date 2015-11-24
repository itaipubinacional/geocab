/**
 * 
 */
package br.com.geocab.domain.entity.account;

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
	@org.hibernate.validator.constraints.Email
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
	 * FIXME ALTERAR PARA MENSAGENS INTERNACIONALIZADAS Valida o email
	 */
	public void validate(Captcha captcha)
	{
		Assert.isTrue(captcha.isCorrect(this.getAnswer()), "You are a robot?");

		this.validate();
	}
	
	/**
	 * FIXME ALTERAR PARA MENSAGENS INTERNACIONALIZADAS Valida o email
	 */
	public void validate()
	{
		// Verificar se o TODO @Email vai funcionar, se não funcionar fazer função
		// validadora
		Assert.isTrue(this.getEmail() != null, "Insira o email");

		Assert.isTrue(this.getName() != null, "Insira o nome");

		Assert.isTrue(this.getSubject() != null, "Insira o assunto");

		Assert.isTrue(this.getMessage() != null, "Insira a mensagem");
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

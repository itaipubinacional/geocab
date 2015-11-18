package br.com.geocab.infrastructure.mail;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import br.com.geocab.domain.entity.account.Email;
import br.com.geocab.domain.repository.IContactMailRepository;

/**
 * 
 * @author emanuelvictor
 *
 */
@Component
public class ContactMailRepository implements IContactMailRepository
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 *
	 */
	@Autowired
	private JavaMailSender mailSender;
	/**
	 *
	 */
	@Autowired
	private VelocityEngine velocityEngine;
	/**
	 *
	 */
	@Value("${mail.from}")
	private String mailFrom;

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 *
	 * @param user
	 */
	@Async
	public Future<Void> sendContactUs(final Email email)
	{
		final MimeMessagePreparator preparator = new MimeMessagePreparator()
		{
			public void prepare(MimeMessage mimeMessage) throws Exception
			{

				final MimeMessageHelper message = new MimeMessageHelper(
						mimeMessage);
				message.setSubject(email.getSubject());
				message.setTo(email.getEmail());
				message.setFrom(mailFrom);

				final Map<String, Object> model = new HashMap<String, Object>();
				model.put("email", email);

				final String content = VelocityEngineUtils
						.mergeTemplateIntoString(velocityEngine,
								"mail-templates/contact-us.html",
								StandardCharsets.ISO_8859_1.toString(), model);
				message.setText(content, true);
			}
		};

		this.mailSender.send(preparator);

		return new AsyncResult<Void>(null);
	}

}
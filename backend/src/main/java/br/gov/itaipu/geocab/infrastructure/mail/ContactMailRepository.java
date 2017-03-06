package br.gov.itaipu.geocab.infrastructure.mail;

import br.gov.itaipu.geocab.domain.entity.configuration.account.Email;
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

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * @author emanuelvictor
 */
@Component
public class ContactMailRepository {
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
    @Value("${mail.support}")
    private String mailSupport;

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param email
     */
    @Async
    public Future<Void> sendContactUs(final Email email) {
        final MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {

                final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setSubject(email.getSubject());
                message.setTo(mailSupport);
                message.setFrom(email.getEmail());

                final Map<String, Object> model = new HashMap<String, Object>();
                model.put("email", email);

                final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail-templates/contact-us.html", StandardCharsets.ISO_8859_1.toString(), model);
                message.setText(content, true);
            }
        };

        this.mailSender.send(preparator);

        return new AsyncResult<Void>(null);
    }

}
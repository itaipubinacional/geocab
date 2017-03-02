package br.gov.itaipu.geocab.infrastructure.mail;

import br.gov.itaipu.geocab.application.configuration.MailConfig;
import br.gov.itaipu.geocab.domain.entity.configuration.Configuration;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.marker.Marker;
import br.gov.itaipu.geocab.domain.entity.markermoderation.MotiveMarkerModeration;
import br.gov.itaipu.geocab.domain.repository.configuration.ConfigurationRepository;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Rodrigo P. Fraga
 * @version 1.0
 * @category Mail
 * @since 03/08/2012
 */
@Component
public class AccountMailRepository {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    @Autowired
    private ConfigurationRepository configurationRepository;
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
    @Autowired
    private MailConfig.Server mailServerConfig;

    /**
     *
     */
    @Value("bla")
    private String geocabUrl;
	
	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param user
     */
    @Async
    public Future<Void> sendRecoveryPassword(final User user) {
        if (!verifySenderEmail()) {
            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {

                    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setSubject("Redefinição de senha"); //FIXME Localize
                    message.setTo(user.getEmail());
                    message.setFrom(mailServerConfig.from);

                    final Map<String, Object> model = new HashMap<String, Object>();
                    model.put("userName", user.getName());
                    model.put("message", "redefinição."); //TODO message
                    model.put("newPassword", user.getNewPassword());

                    final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail-templates/recovery-password.html", StandardCharsets.ISO_8859_1.toString(), model);
                    message.setText(content, true);
                }
            };

            this.mailSender.send(preparator);
        }
        return new AsyncResult<Void>(null);
    }

    private boolean verifySenderEmail() {
        List<Configuration> configurations = configurationRepository.findAll();
        if (configurations == null || configurations.size() > 0) {
            return configurationRepository.findAll().get(0).getStopSendEmail();
        }
        return new Configuration().getStopSendEmail();
    }

    /**
     * @param user
     * @param marker
     */
    @Async
    public Future<Void> sendMarkerAccepted(final User user, final Marker marker) {
        if (!verifySenderEmail()) {
            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {

                    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setSubject("Postagem aprovada!"); //FIXME Localize
                    message.setTo(user.getEmail());
                    message.setFrom(mailServerConfig.from);

                    final Map<String, Object> model = new HashMap<String, Object>();
                    model.put("userName", user.getName());
                    model.put("marker", marker.getLayer().getName());

                    model.put("url", geocabUrl + "/admin/markers?id=" + marker.getId());

                    final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail-templates/accept-marker.html", StandardCharsets.ISO_8859_1.toString(), model);
                    message.setText(content, true);
                }
            };

            this.mailSender.send(preparator);
        }
        return new AsyncResult<Void>(null);
    }

    /**
     * @param user
     * @param marker
     */
    @Async
    public Future<Void> sendMarkerRefused(final User user, final Marker marker, final MotiveMarkerModeration motiveMarkerModeration) {
        if (!verifySenderEmail()) {
            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {

                    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setSubject("Postagem recusada!"); //FIXME Localize
                    message.setTo(user.getEmail());
                    message.setFrom(mailServerConfig.from);

                    final Map<String, Object> model = new HashMap<String, Object>();
                    model.put("userName", user.getName());
                    model.put("marker", marker.getLayer().getName());
                    model.put("motive", motiveMarkerModeration.getMotive().getName() + " - " + motiveMarkerModeration.getDescription());

                    model.put("url", geocabUrl + "/admin/markers?id=" + marker.getId());

                    final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail-templates/refuse-marker.html", StandardCharsets.ISO_8859_1.toString(), model);
                    message.setText(content, true);
                }
            };

            this.mailSender.send(preparator);
        }
        return new AsyncResult<Void>(null);
    }


    /**
     * @param user
     * @param marker
     */
    @Async
    public Future<Void> sendMarkerCanceled(final User user, final Marker marker) {
        if (!verifySenderEmail()) {
            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {

                    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setSubject("Postagem cancelada!"); //FIXME Localize
                    message.setTo(user.getEmail());
                    message.setFrom(mailServerConfig.from);

                    final Map<String, Object> model = new HashMap<String, Object>();
                    model.put("userName", user.getName());
                    model.put("marker", marker.getLayer().getName());

                    model.put("url", geocabUrl + "/admin/markers?id=" + marker.getId());

                    final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail-templates/cancel-marker.html", StandardCharsets.ISO_8859_1.toString(), model);
                    message.setText(content, true);
                }
            };

            this.mailSender.send(preparator);
        }
        return new AsyncResult<Void>(null);
    }

}
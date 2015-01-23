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

import br.com.geocab.domain.entity.account.IAccountMailRepository;
import br.com.geocab.domain.entity.markermoderation.MotiveMarkerModeration;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.marker.Marker;

/**
 * @author Rodrigo P. Fraga
 * @since 03/08/2012
 * @version 1.0
 * @category Mail
 */
@Component
public class AccountMailRepository implements IAccountMailRepository
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 *
	 */
	@Autowired
	private JavaMailSender  mailSender;
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
	public Future<Void> sendRecoveryPassword( final User user )
	{
		final MimeMessagePreparator preparator = new MimeMessagePreparator() 
		{
            public void prepare( MimeMessage mimeMessage ) throws Exception 
            {

                final MimeMessageHelper message = new MimeMessageHelper( mimeMessage );
                message.setSubject("Redefinição de senha"); //FIXME Localize
                message.setTo( user.getEmail() );
                message.setFrom( mailFrom );

                final Map<String, Object> model = new HashMap<String, Object>();
    	        model.put("name",  user.getName() );
    	        model.put("message", "redefinição." ); //TODO message
    	        model.put("newPassword", user.getNewPassword());

                final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail-templates/recovery-password.html", StandardCharsets.UTF_8.toString(), model);
                message.setText(content, true);
            }
        };

        this.mailSender.send(preparator);

        return new AsyncResult<Void>(null);
	}
	
	/**
	 *
	 * @param user
	 * @param marker
	 */
	@Async
	public Future<Void> sendMarkerAccepted( final User user, final Marker marker )
	{
		final MimeMessagePreparator preparator = new MimeMessagePreparator() 
		{
           public void prepare( MimeMessage mimeMessage ) throws Exception 
           {
        	   
               final MimeMessageHelper message = new MimeMessageHelper( mimeMessage );
               message.setSubject("Postagem aprovada!"); //FIXME Localize
               message.setTo( user.getEmail() );
               message.setFrom( mailFrom );

               final Map<String, Object> model = new HashMap<String, Object>();
   	        	model.put("user.name",  user.getName() );
   	        	model.put("marker", marker.getLayer().getName()); //TODO message

               final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail-templates/accept-marker.html", StandardCharsets.UTF_8.toString(), model);
               message.setText(content, true);
           }
       };

       this.mailSender.send(preparator);

       return new AsyncResult<Void>(null);
	}
	
	/**
	 *
	 * @param user
	 * @param marker
	 */
	@Async
	public Future<Void> sendMarkerRefused( final User user,  final Marker marker, final MotiveMarkerModeration motiveMarkerModeration )
	{
		final MimeMessagePreparator preparator = new MimeMessagePreparator() 
		{
          public void prepare( MimeMessage mimeMessage ) throws Exception 
          {

              final MimeMessageHelper message = new MimeMessageHelper( mimeMessage );
              message.setSubject("Postagem recusada!"); //FIXME Localize
              message.setTo( user.getEmail() );
              message.setFrom( mailFrom );

              final Map<String, Object> model = new HashMap<String, Object>();
  	          model.put("user.name",  user.getName() );
  	          model.put("marker", marker.getLayer().getName());
  	          model.put("motive", motiveMarkerModeration.getMotive().getName() + " - " + motiveMarkerModeration.getDescription());

              final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail-templates/refuse-marker.html", StandardCharsets.UTF_8.toString(), model);
              message.setText(content, true);
          }
      };

      this.mailSender.send(preparator);

      return new AsyncResult<Void>(null);
	}

}
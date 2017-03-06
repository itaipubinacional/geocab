/**
 *
 */
package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.application.security.ContextHolder;
import br.gov.itaipu.geocab.domain.entity.configuration.account.Email;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.infrastructure.mail.ContactMailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @author emanuelvictor
 */
@Service
public class ContactService {

    /**
     * Log
     */
    private static final Logger LOG = Logger.getLogger(DataSourceService.class.getName());

    /**
     * I18n
     */
    @Autowired
    private MessageSource messages;

    /**
     * AccountMail Repository
     */
    @Autowired
    private ContactMailRepository contactMailRepository;

    /**
     * Insert a new User
     *
     * @param email
     * @return
     */
    public Email contactUs(Email email) {
        try {
            if (this.getLoggedUser() != null) {
                email.setEmail(ContextHolder.getAuthenticatedUser().getUsername());
                email.setName(ContextHolder.getAuthenticatedUser().getName());
            }
            email.validate();

            contactMailRepository.sendContactUs(email);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            throw new RuntimeException(this.messages.getMessage(e.getMessage(), new Object[]{}, null));
        }

        return email;
    }

    /**
     * Insert a new User
     *
     * @return
     */
    public User getLoggedUser() {
        User user = ContextHolder.getAuthenticatedUser();
        if (user == null || user.getName().equals("Anonymous")) {
            return null;
        }
        user.setPassword(null);
        return user;
    }
}

/**
 *
 */
package br.gov.itaipu.geocab.domain.service;


import br.gov.itaipu.geocab.application.security.ContextHolder;
import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroup;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.configuration.account.UserRole;
import br.gov.itaipu.geocab.domain.repository.accessgroup.AccessGroupRepository;
import br.gov.itaipu.geocab.domain.repository.configuration.ConfigurationRepository;
import br.gov.itaipu.geocab.domain.repository.configuration.account.UserRepository;
import br.gov.itaipu.geocab.infrastructure.mail.AccountMailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;

/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @since 29/09/2014
 */

@Service
@Transactional
public class LoginService {
    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     * User Repository
     */
    @Autowired
    private ConfigurationRepository configurationRepository;

    /**
     * User Repository
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * AccountMail Repository
     */
    @Autowired
    private AccountMailRepository accountMailRepository;

    /**
     *
     */
    @Autowired
    private AccessGroupRepository accessGroupRepository;

    /**
     * Password encoder
     */
    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    /**
     * Hash generator for encryption
     */
    @Autowired
    private SaltSource saltSource;
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * Insert a new User
     *
     * @param user
     * @return
     */
    public User insertUser(User user) {
        Assert.notNull(user);

        user.setRole(UserRole.USER);
        user.setEnabled(true);

        //encrypt password
        final String encodedPassword = this.passwordEncoder.encodePassword(user.getPassword(), saltSource.getSalt(user));
        user.setPassword(encodedPassword);
        user.verifyBackgroundMap(configurationRepository.findAll());
        user = this.userRepository.save(user);

        AccessGroup publicAccessGroup = this.accessGroupRepository.findOne(AccessGroup.PUBLIC_GROUP_ID);

        publicAccessGroup.getUsers().add(user);

        this.accessGroupRepository.save(publicAccessGroup);

        return user;
    }

    /**
     * @param user
     * @return
     */
    public User insertSocialUser(User user) {
        Assert.notNull(user);

        user.setRole(UserRole.USER);
        user.setEnabled(true);
		
		/*//encrypt password
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); */
        user.setPassword("no password");

        user.verifyBackgroundMap(configurationRepository.findAll());
        user = this.userRepository.save(user);

        AccessGroup publicAccessGroup = this.accessGroupRepository.findOne(AccessGroup.PUBLIC_GROUP_ID);

        publicAccessGroup.getUsers().add(user);

        this.accessGroupRepository.save(publicAccessGroup);

        return user;

    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String userName) {
        return this.userRepository.findUser(userName);
    }

    public User authenticatedUser() {
        User user = ContextHolder.getAuthenticatedUser();
        user = this.userRepository.save(ContextHolder.getAuthenticatedUser());
        return user;
    }

    public void recoverPassword(User user) throws Exception {
        User userValid = this.userRepository.findByEmail(user.getEmail());

        if (userValid == null) {
            throw new Exception();
        }

        String s = userValid.getEmail() + Calendar.getInstance().getTime();
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(s.getBytes(), 0, s.length());
        userValid.setNewPassword(new BigInteger(1, m.digest()).toString(16).substring(0, 6));

        final String encodedPassword = this.passwordEncoder.encodePassword(userValid.getNewPassword(), saltSource.getSalt(userValid));
        userValid.setPassword(encodedPassword);
        userValid.verifyBackgroundMap(configurationRepository.findAll());
        this.userRepository.save(userValid);
        userValid.getBackgroundMap();
        userValid.getCoordinates();
        this.accountMailRepository.sendRecoveryPassword(userValid);
    }

    /**
     * Realiza o logout do sistema
     */
    public void logout() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.createEmptyContext();
    }

    /**
     * @param credentials This value must be base64 encoded, following the pattern: "email:password"
     * @return
     */
    @Transactional(readOnly = true)
    public User checkCredentials(String credentials) {
        try {
            credentials = new String(Base64.decode(credentials.getBytes()));
            final String email = credentials.substring(0, credentials.indexOf(':'));
            final String password = credentials.substring(credentials.indexOf(':') + 1);

            final User user = (User) this.userRepository.findByEmail(email);
            final String encodedPassword = this.passwordEncoder.encodePassword(password, saltSource.getSalt(user));

            if (!user.getPassword().equals(encodedPassword)) {
                throw new SecurityException("Email and/or password is invalid.");
            }
            return user;
        } catch (Exception e) {
            throw new SecurityException("Email and/or password is invalid.");
        }
    }

    /**
     * @param username
     */
    public User loadOrSaveNewUserByUsername(String username) {
        User user = this.userRepository.findByEmail(username);
        if (user == null) {
            //Inserting configurations default
            user = new User(username, username);
            user.getBackgroundMap();
            user.getCoordinates();
            return this.insertSocialUser(user);
        }
        return user;
    }

}

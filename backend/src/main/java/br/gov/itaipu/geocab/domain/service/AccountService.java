package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.application.security.ContextHolder;
import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroup;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.configuration.account.UserRole;
import br.gov.itaipu.geocab.domain.entity.configuration.preferences.Coordinates;
import br.gov.itaipu.geocab.domain.repository.accessgroup.AccessGroupRepository;
import br.gov.itaipu.geocab.domain.repository.configuration.ConfigurationRepository;
import br.gov.itaipu.geocab.domain.repository.configuration.account.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author Cristiano Correa
 * @version 1.0
 * @category Service
 * @since 22/04/2014
 */
@Service
@Transactional
@PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
public class AccountService {
    /*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(AccountService.class.getName());

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

        user.setEnabled(true);
        // encrypt password
        final String encodedPassword = this.passwordEncoder
                .encodePassword(user.getPassword(), saltSource.getSalt(user));
        user.setPassword(encodedPassword);

        if (user.getCoordinates() == null) {
            user.setCoordinates(Coordinates.DEGREES_MINUTES_SECONDS);
        }

        user.verifyBackgroundMap(configurationRepository.findAll());
        user = this.userRepository.save(user);

        AccessGroup publicAccessGroup = this.accessGroupRepository
                .findOne(AccessGroup.PUBLIC_GROUP_ID);

        publicAccessGroup.getUsers().add(user);

        this.accessGroupRepository.save(publicAccessGroup);

        return user;
    }

    /**
     * List Users with pagination and filters
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<User> listUsersByFilters(String filter, PageRequest pageable) {
        return this.userRepository.listByFilters(filter, pageable);
    }

    /**
     * @return
     */
    @Transactional(readOnly = true)
    public List<User> listAllUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Find User by id
     *
     * @param id
     * @return User
     */
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return this.userRepository.findOne(id);
    }

    /**
     * Find User by userName
     *
     * @param userName
     * @return User
     */
    @Transactional(readOnly = true)
    public User findUserByEmail(String userName) {
        return this.userRepository.findByEmail(userName);
    }

    /**
     * Disable User
     *
     * @param id
     * @return boolean
     */
    public Boolean disableUser(Long id) {
        User user = this.userRepository.findOne(id); // Load user
        user.setEnabled(false); // Disable user
        user.verifyBackgroundMap(configurationRepository.findAll());
        this.userRepository.save(user); // Save user

        return true;
    }

    /**
     * Enable User
     *
     * @param id
     * @return boolean
     */
    public Boolean enableUser(Long id) {
        User user = this.userRepository.findOne(id); // Load user
        user.setEnabled(true); // Enable user
        user.verifyBackgroundMap(configurationRepository.findAll());
        this.userRepository.save(user); // Save user

        return true;
    }

    /**
     * Update User
     *
     * @param user
     * @return User
     */
    public User updateUser(User user) {
        try {
            User dbUser = this.userRepository.findOne(user.getId());

            // Update database user
            dbUser.setEmail(user.getEmail());
            dbUser.setName(user.getName());
            dbUser.setRole(user.getRole());

            if (!user.getPassword().isEmpty()) { // if set new password
                final String encodedPassword = this.passwordEncoder
                        .encodePassword(user.getPassword(),
                                saltSource.getSalt(dbUser));
                dbUser.setPassword(encodedPassword);
            }

            user.verifyBackgroundMap(configurationRepository.findAll());
            user = this.userRepository.save(dbUser);// save data in database

        } catch (DataIntegrityViolationException e) {
            final String error = e.getCause().getCause().getMessage();
            LOG.info(error);
        }

        return user;
    }

    /**
     * Update User
     *
     * @return
     * @throws Exception
     */
    @PreAuthorize("permitAll")
    public User getUserAuthenticated() throws Exception {
        if (ContextHolder.getAuthenticatedUser() == null || ContextHolder.getAuthenticatedUser().getRole().equals(UserRole.ANONYMOUS)) {
            User user = new User();
            user.verifyBackgroundMap(configurationRepository.findAll());
            return user;
        } else {
            User user = this.userRepository.findOne(ContextHolder.getAuthenticatedUser().getId());
            User u = new User();
            u.setCreated(user.getCreated());
            u.setEmail(user.getEmail());
            u.setEnabled(user.getEnabled());
            u.setId(user.getId());
            u.setName(user.getName());
            u.setRole(user.getRole());
            u.setUpdated(user.getUpdated());
            u.setBackgroundMap(user.getBackgroundMap());
            u.setCoordinates(user.getCoordinates());
            return u;
        }
    }

    /**
     * @param userId
     * @return
     * @throws Exception
     */
    @PreAuthorize("permitAll")
    public Boolean getSocialUser(Long userId) {
        return this.userRepository.findOne(userId).getPassword().equals(User.NO_PASSWORD);
    }

    /**
     * Update User
     *
     * @param user
     * @return
     * @throws Exception
     */
    @PreAuthorize("permitAll")
    public User updateUserAuthenticated(User user) throws Exception {
        User dbUser = this.userRepository.findOne(ContextHolder.getAuthenticatedUser().getId());

        // Update database user
        dbUser.setName(user.getName());
        dbUser.setCoordinates(user.getCoordinates());
        dbUser.setBackgroundMap(user.getBackgroundMap());

        // Se o usuário alterou a senha de um usuário que era acessado via rede social
        if (dbUser.getPassword().equals(User.NO_PASSWORD)) {
            String encodedPassword = this.passwordEncoder.encodePassword(user.getNewPassword(), saltSource.getSalt(user));
            dbUser.setPassword(encodedPassword);
            dbUser.verifyBackgroundMap(configurationRepository.findAll());
            return this.userRepository.save(dbUser);
        }


        if (!(user.getPassword() == null) && !user.getPassword().isEmpty()) { // if set new password
            if (!this.passwordEncoder
                    .encodePassword(user.getPassword(),
                            saltSource.getSalt(user))
                    .equals(dbUser.getPassword())) {
                throw new Exception("A senha informada não é correspondente!");
            } else if (!(user.getNewPassword() == null)
                    && !user.getNewPassword().isEmpty()) {
                String encodedPassword = this.passwordEncoder.encodePassword(
                        user.getNewPassword(), saltSource.getSalt(user));
                dbUser.setPassword(encodedPassword);
            } else {
                throw new Exception("Nova senha inválida!");
            }
        } else if (!(user.getNewPassword() == null) && !user.getNewPassword().isEmpty() && !(user.getPassword() == null) && !user.getPassword().isEmpty()) {
            throw new Exception("A senha informada não é correspondente!");
        }
        user.verifyBackgroundMap(configurationRepository.findAll());
        return this.userRepository.save(dbUser);
    }

}

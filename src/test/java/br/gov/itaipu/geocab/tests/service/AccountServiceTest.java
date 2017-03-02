package br.gov.itaipu.geocab.tests.service;

import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.configuration.account.UserRole;
import br.gov.itaipu.geocab.domain.service.AccountService;
import br.gov.itaipu.geocab.tests.AbstractIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * @author Cristiano Correa
 * @version 1.0
 * @category
 * @since 09/05/2013
 */
public class AccountServiceTest extends AbstractIntegrationTest {
    /*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * User Repository
     */
    @Autowired
    public AccountService accountService;

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
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/


    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml"
    })
    public void updateUser() {
        this.authenticate(100L);

        User user = this.accountService.findUserById(100L);

        user.setEmail("user2@geocab.com.br");
        user.setName("Testing User2");
        user.setPassword("1234");
        user.setRole(UserRole.USER);

        user = this.accountService.updateUser(user);

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals("user2@geocab.com.br", user.getEmail());
        Assert.assertEquals("Testing User2", user.getName());
        Assert.assertEquals(UserRole.USER, user.getRole());

        final String encodedPassword = this.passwordEncoder.encodePassword("1234", saltSource.getSalt(user));
        Assert.assertEquals(encodedPassword, user.getPassword());
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml"
    })
    public void findUserById() {
        this.authenticate(100L);

        User user = this.accountService.findUserById(100L);

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals("test_prognus@mailinator.com", user.getEmail());
        Assert.assertEquals("Testing User", user.getName());
        Assert.assertEquals(UserRole.ADMINISTRATOR, user.getRole());

        Assert.assertTrue(user.isEnabled());

        final String encodedPassword = this.passwordEncoder.encodePassword("123", saltSource.getSalt(user));
        Assert.assertEquals(encodedPassword, user.getPassword());
    }


    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml"
    })
    public void findUserByEmail() {
        this.authenticate(100L);

        User user = this.accountService.findUserByEmail("test_prognus@mailinator.com");

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals("test_prognus@mailinator.com", user.getEmail());
        Assert.assertEquals("Testing User", user.getName());
        Assert.assertEquals(UserRole.ADMINISTRATOR, user.getRole());

        Assert.assertTrue(user.isEnabled());

        final String encodedPassword = this.passwordEncoder.encodePassword("123", saltSource.getSalt(user));
        Assert.assertEquals(encodedPassword, user.getPassword());
    }


    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml"
    })
    public void disableEnableUser() {
        this.authenticate(100L);

        this.accountService.disableUser(100L);
        User user = this.accountService.findUserById(100L);
        Assert.assertFalse(user.isEnabled());

        this.accountService.enableUser(100L);
        User user2 = this.accountService.findUserById(100L);
        Assert.assertTrue(user2.isEnabled());
    }

}

package br.gov.itaipu.geocab.infrastructure.jcr.modeshape;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * {@link org.springframework.beans.factory.FactoryBean} which uses
 * {@link ModeShapeRepositoryFactory} to provide different {@link Session}
 * instances each time this bean is used.
 *
 * @author rodrigofraga
 * @version 1.0
 * @category
 * @since Sep 11, 2013
 */
@Component
public class ModeShapeSessionFactory implements FactoryBean<Session> {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    @Autowired
    private Repository repository;
    /**
     *
     */
    private Session session;

    /*-------------------------------------------------------------------
     *				 		     BEHAVIORS
     *-------------------------------------------------------------------*/

    /**
     *
     */
    @PreDestroy
    public void logout() {
        if (session != null)
            session.logout();
        session = null;
    }

	/*-------------------------------------------------------------------
	 *				 		  GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    /**
     * @throws RepositoryException
     * @throws LoginException
     */
    @Override
    public Session getObject() throws LoginException, RepositoryException {
        if (session == null)
            session = repository.login();
        return session;
    }

    /**
     *
     */
    @Override
    public Class<?> getObjectType() {
        return Session.class;
    }

    /**
     *
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
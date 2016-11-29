package br.com.geocab.infrastructure.jcr.modeshape;

import javax.annotation.PreDestroy;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link org.springframework.beans.factory.FactoryBean} which uses
 * {@link ModeShapeRepositoryFactory} to provide different {@link Session}
 * instances each time this bean is used.
 * 
 * @author rodrigofraga
 * @since Sep 11, 2013
 * @version 1.0
 * @category
 */
public class ModeShapeSessionFactory implements FactoryBean<Session>
{
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
	public void logout()
	{
		session.logout();
		session = null;
	}

	/*-------------------------------------------------------------------
	 *				 		  GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	/**
	 * @throws RepositoryException 
	 * @throws LoginException 
	 * 
	 */
	@Override
	public Session getObject() throws LoginException, RepositoryException
	{
		if ( session == null )
		{
			session = repository.login();
		}
		
		return session;
	}

	/**
	 * 
	 */
	@Override
	public Class<?> getObjectType()
	{
		return Session.class;
	}

	/**
	 * 
	 */
	@Override
	public boolean isSingleton()
	{
		return true;
	}
}
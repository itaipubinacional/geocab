package br.com.geocab.domain.entity;

import java.io.Serializable;

import org.hibernate.envers.RevisionType;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.account.User;

/**
 * @author Rodrigo P. Fraga
 * @since 06/12/2012
 * @version 1.0
 * @category Entity
 */
public class EntityTrackingRevisionListener implements org.hibernate.envers.EntityTrackingRevisionListener
{	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void newRevision( Object revisionEntity ) 
	{
		try
		{
			final User user = ContextHolder.getAuthenticatedUser();
			((Revision)revisionEntity).setUserId( user.getId() );
		}
		catch (InsufficientAuthenticationException e)
		{
			//Não há usuário logado.
		}
    }
	
	/* 
	 * (non-Javadoc)
	 * @see org.hibernate.envers.EntityTrackingRevisionListener#entityChanged(java.lang.Class, java.lang.String, java.io.Serializable, org.hibernate.envers.RevisionType, java.lang.Object)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void entityChanged( Class entityClass, String entityName, Serializable entityId, RevisionType revisionType, Object revisionEntity )
	{
		
	}
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
}
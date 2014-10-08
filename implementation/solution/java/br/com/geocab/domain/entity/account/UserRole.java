package br.com.geocab.domain.entity.account;

import org.directwebremoting.annotations.DataTransferObject;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Vagner BC
 * @since 02/06/2014
 * @version 1.0
 */
@DataTransferObject(type="enum")
public enum UserRole implements GrantedAuthority 
{
	/*-------------------------------------------------------------------
	 *				 		     ENUMS
	 *-------------------------------------------------------------------*/
	ADMINISTRATOR, 	//0
	MODERATOR, 		//1
	USER;			//2

	/* (non-Javadoc)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority()
	{
		return this.name();
	}
}
package br.gov.itaipu.geocab.domain.entity.configuration.account;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Vagner BC
 * @version 1.0
 * @since 02/06/2014
 */
public enum UserRole implements GrantedAuthority {
    /*-------------------------------------------------------------------
     *				 		     ENUMS
     *-------------------------------------------------------------------*/
    ADMINISTRATOR,    //0
    MODERATOR,        //1
    USER,            //2
    ANONYMOUS;        //3


    public static final String ADMINISTRATOR_VALUE = "ADMINISTRATOR";
    public static final String MODERATOR_VALUE = "MODERATOR";
    public static final String USER_VALUE = "USER";
    public static final String ANONYMOUS_VALUE = "ANONYMOUS";

    /* (non-Javadoc)
     * @see org.springframework.security.core.GrantedAuthority#getAuthority()
     */
    @Override
    public String getAuthority() {
        return this.name();
    }
}
package br.gov.itaipu.geocab.application.security;

import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * @author Rodrigo P. Fraga
 * @version 1.0
 * @category Repository
 * @since 22/04/2014
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {
    /*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    @Autowired
    private EntityManager entityManager;

    /*-------------------------------------------------------------------
     *				 		     BEHAVIORS
     *-------------------------------------------------------------------*/
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            final String hql = "FROM User user "
                    + "WHERE user.email = :email";

            final TypedQuery<User> query = this.entityManager.createQuery(hql, User.class);
            query.setParameter("email", email);

            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("O usuário com o email " + email + " não foi encontrado.");//FIXME Localize
        }
    }
}

package br.com.geocab.domain.repository.account;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * 
 * @author Rodrigo P. Fraga 
 * @since 22/04/2014
 * @version 1.0
 * @category Repository
 */
public interface IUserRepository extends IDataRepository<User, Long>, UserDetailsService
{
	/**
	 * @param username
	 * @return
	 */
	public User findByEmail(String username);
}

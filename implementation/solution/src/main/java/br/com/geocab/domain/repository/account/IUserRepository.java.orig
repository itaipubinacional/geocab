package br.com.geocab.domain.repository.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	
	/**
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT new User( user.id, user.name, user.email , user.enabled , user.role ) " +
				"FROM User user " +
				"WHERE  ( ( LOWER(user.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
				"OR ( LOWER(user.email) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ))" )
	public Page<User> listByFilters( @Param("filter") String filter, Pageable pageable );
	
}

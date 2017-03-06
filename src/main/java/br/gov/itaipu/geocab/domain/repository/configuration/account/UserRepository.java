package br.gov.itaipu.geocab.domain.repository.configuration.account;

import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * @author Rodrigo P. Fraga
 * @version 1.0
 * @category Repository
 * @since 22/04/2014
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * @param username
     * @return
     */
    User findByEmail(String username);

    /**
     * @param email
     * @return
     */
    @Query(value = "SELECT new User( user.id, user.name, user.email , user.enabled , user.role, user.password ) " +
            "FROM User user WHERE  (:email = user.email)")
    User findUser(@Param("email") String email);

    /**
     * @param filter
     * @param pageable
     * @return
     */
    @Query(value = "SELECT new User( user.id, user.name, user.email , user.enabled , user.role ) " +
            "FROM User user " +
            "WHERE  ( ( LOWER(user.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
            "OR ( LOWER(user.email) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ))")
    Page<User> listByFilters(@Param("filter") String filter, Pageable pageable);

}

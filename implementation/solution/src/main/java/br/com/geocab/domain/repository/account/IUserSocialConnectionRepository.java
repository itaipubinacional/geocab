package br.com.geocab.domain.repository.account;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.configuration.account.UserSocialConnection;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * 
 * @author Rodrigo P. Fraga 
 * @since 22/04/2014
 * @version 1.0
 * @category Repository
 */
public interface IUserSocialConnectionRepository extends IDataRepository<UserSocialConnection, Long>
{
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT user.email "
			 	 + "FROM UserSocialConnection  "
			 	+ "WHERE providerId = :providerId "
			 		+ "AND providerUserId = :providerUserId")
	public List<String> listUserEmailsByProviderIdAndProviderUserId( @Param(value="providerId") String providerId, @Param(value="providerUserId") String providerUserId );
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT user.email "
			  	 + "FROM UserSocialConnection "
			  	+ "WHERE providerId = :providerId "
			  		+ "AND providerUserId IN (:providerUserIds) ")
	public Set<String> listUserEmailsByProviderIdAndProviderUserIds( @Param(value="providerId") String providerId, @Param(value="providerUserIds") Collection<String> providerUserIds );
	/**
	 * 
	 * @return
	 */
	public List<UserSocialConnection> findByUserIdOrderByRankDesc( Long userId );
	/**
	 * @param userId
	 * @param providerId
	 * @return
	 */
	public List<UserSocialConnection> findByUserIdAndProviderIdOrderByRankDesc( Long userId, String providerId );
	/**
	 * @param userId
	 * @param providerId
	 * @param providerUserId
	 * @return
	 */
	public UserSocialConnection findByUserIdAndProviderIdAndProviderUserId( Long userId, String providerId, String providerUserId );
	/**
	 * @param userId
	 * @param providerId
	 * @return
	 */
	@Query(value="SELECT COALESCE(MAX(rank) + 1, 1) AS rank "
				 + "FROM UserSocialConnection "
				+ "WHERE user.id = :userId "
					+ "AND providerId = :providerId")
	public int findNewRankByUserIdAndProviderId( @Param(value="userId") Long userId, @Param(value="providerId") String providerId );
}

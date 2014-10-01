package br.com.geocab.domain.repository.account;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.account.UserSocialConnection;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * 
 * @author Rodrigo P. Fraga 
 * @since 22/04/2014
 * @version 1.0
 * @category Repository
 */
public interface IUserSocialConnectionRepository extends IDataRepository<UserSocialConnection, String>
{
	/**
	 * 
	 * @return
	 */
	public List<String> findUserIdByProviderIdAndProviderUserId( String providerId, String providerUserId );
	/**
	 * 
	 * @return
	 */
	public Set<String> findUserIdByProviderIdAndProviderUserIdIn( String providerId, Collection<String> providerUserIds );
	/**
	 * 
	 * @return
	 */
	public List<UserSocialConnection> findByUserIdOrderByRankDesc( String userId );
	/**
	 * @param userId
	 * @param providerId
	 * @return
	 */
	public List<UserSocialConnection> findByUserIdAndProviderIdOrderByRankDesc( String userId, String providerId );
	/**
	 * @param userId
	 * @param providerId
	 * @param providerUserId
	 * @return
	 */
	public UserSocialConnection findByUserIdAndProviderIdAndProviderUserId( String userId, String providerId, String providerUserId );
	/**
	 * @param userId
	 * @param providerId
	 * @return
	 */
	@Query(value="SELECT COALESCE(MAX(rank) + 1, 1) AS rank "
				 + "FROM UserSocialConnection "
				+ "WHERE userId = :userId "
					+ "AND providerId = :providerId")
	public int findNewRankByUserIdAndProviderId( @Param(value="userId") String userId, @Param(value="providerId") String providerId );
}

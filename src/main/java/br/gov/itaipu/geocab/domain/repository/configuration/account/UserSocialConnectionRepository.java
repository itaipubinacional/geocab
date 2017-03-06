package br.gov.itaipu.geocab.domain.repository.configuration.account;

import br.gov.itaipu.geocab.domain.entity.configuration.account.UserSocialConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Rodrigo P. Fraga
 * @version 1.0
 * @category Repository
 * @since 22/04/2014
 */
public interface UserSocialConnectionRepository extends JpaRepository<UserSocialConnection, Long> {
    /**
     * @return
     */
    @Query(value = "SELECT user.email "
            + "FROM UserSocialConnection  "
            + "WHERE providerId = :providerId "
            + "AND providerUserId = :providerUserId")
    List<String> listUserEmailsByProviderIdAndProviderUserId(@Param(value = "providerId") String providerId, @Param(value = "providerUserId") String providerUserId);

    /**
     * @return
     */
    @Query(value = "SELECT user.email "
            + "FROM UserSocialConnection "
            + "WHERE providerId = :providerId "
            + "AND providerUserId IN (:providerUserIds) ")
    Set<String> listUserEmailsByProviderIdAndProviderUserIds(@Param(value = "providerId") String providerId, @Param(value = "providerUserIds") Collection<String> providerUserIds);

    /**
     * @return
     */
    List<UserSocialConnection> findByUserIdOrderByRankDesc(Long userId);

    /**
     * @param userId
     * @param providerId
     * @return
     */
    List<UserSocialConnection> findByUserIdAndProviderIdOrderByRankDesc(Long userId, String providerId);

    /**
     * @param userId
     * @param providerId
     * @param providerUserId
     * @return
     */
    UserSocialConnection findByUserIdAndProviderIdAndProviderUserId(Long userId, String providerId, String providerUserId);

    /**
     * @param userId
     * @param providerId
     * @return
     */
    @Query(value = "SELECT COALESCE(MAX(rank) + 1, 1) AS rank "
            + "FROM UserSocialConnection "
            + "WHERE user.id = :userId "
            + "AND providerId = :providerId")
    int findNewRankByUserIdAndProviderId(@Param(value = "userId") Long userId, @Param(value = "providerId") String providerId);

    UserSocialConnection findByUserId(@Param(value = "userId") Long userId);
}

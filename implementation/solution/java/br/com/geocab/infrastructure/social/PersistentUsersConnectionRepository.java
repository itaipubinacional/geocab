package br.com.geocab.infrastructure.social;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

import br.com.geocab.domain.repository.account.IUserSocialConnectionRepository;

/**
 * 
 * @author rodrigo
 */
public class PersistentUsersConnectionRepository implements UsersConnectionRepository
{
	private TextEncryptor textEncryptor;
	private ConnectionSignUp connectionSignUp;
	
	/**
	 * 
	 */
	@Autowired
	private IUserSocialConnectionRepository socialConnectionRepository;
	/**
	 * 
	 */
	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;
	
	/**
	 * 
	 * @param connectionFactoryLocator
	 * @param textEncryptor
	 */
	public PersistentUsersConnectionRepository()
	{
		//this.textEncryptor = textEncryptor;
	}

	/**
	 * The command to execute to create a new local user profile in the event no
	 * user id could be mapped to a connection. Allows for implicitly creating a
	 * user profile from connection data during a provider sign-in attempt.
	 * Defaults to null, indicating explicit sign-up will be required to
	 * complete the provider sign-in attempt.
	 * 
	 * @see #findUserIdsWithConnection(Connection)
	 */
	public void setConnectionSignUp(ConnectionSignUp connectionSignUp)
	{
		this.connectionSignUp = connectionSignUp;
	}

	/**
	 * 
	 */
	public List<String> findUserIdsWithConnection(Connection<?> connection)
	{
		ConnectionKey key = connection.getKey();
		
		final List<String> localUserIds = this.socialConnectionRepository.findUserIdByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
		
		if ( localUserIds.size() == 0 && connectionSignUp != null )
		{
			final String newUserId = connectionSignUp.execute(connection);
			if ( newUserId != null )
			{
				this.createConnectionRepository(newUserId).addConnection(connection);
				return Arrays.asList(newUserId);
			}
		}
		return localUserIds;
	}

	/**
	 * 
	 */
	public Set<String> findUserIdsConnectedTo( String providerId, Set<String> providerUserIds )
	{
		return this.socialConnectionRepository.findUserIdByProviderIdAndProviderUserIdIn( providerId, providerUserIds);
	}

	/**
	 * 
	 */
	public ConnectionRepository createConnectionRepository( String userId )
	{
		if ( userId == null )
		{
			throw new IllegalArgumentException("userId cannot be null");
		}
		
		return new PersistentConnectionRepository( userId, socialConnectionRepository, connectionFactoryLocator, textEncryptor );
	}
}

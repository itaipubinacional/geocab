package br.com.geocab.infrastructure.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import br.com.geocab.domain.entity.configuration.account.User;
import br.com.geocab.domain.entity.configuration.account.UserSocialConnection;
import br.com.geocab.domain.repository.account.IUserSocialConnectionRepository;

/**
 * Data access interface for saving and restoring Connection objects from a persistent store.
 * The view is relative to a specific local user--it's not possible using this interface to access or update connections for multiple local users.
 * If you need that capability, see {@link UsersConnectionRepository}.
 * @author rodrigo
 * @see UsersConnectionRepository
 */
class PersistentConnectionRepository implements ConnectionRepository
{
	/**
	 * 
	 */
	private final User user;
	/**
	 * 
	 */
	private final ConnectionFactoryLocator connectionFactoryLocator;
	/**
	 * 
	 */
	private IUserSocialConnectionRepository userSocialConnectionRepository;
	/**
	 * 
	 * @param userId
	 * @param connectionFactoryLocator
	 * @param textEncryptor
	 */
	public PersistentConnectionRepository( User user, IUserSocialConnectionRepository userSocialConnectionRepository, ConnectionFactoryLocator connectionFactoryLocator )
	{
		this.user = user;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.userSocialConnectionRepository = userSocialConnectionRepository;
	}

	/**
	 * 
	 */
	public MultiValueMap<String, Connection<?>> findAllConnections()
	{
		final MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		final Set<String> registeredProviderIds = this.connectionFactoryLocator.registeredProviderIds();
		for ( String registeredProviderId : registeredProviderIds )
		{
			connections.put( registeredProviderId, Collections.<Connection<?>> emptyList() );
		}
		
		final List<UserSocialConnection> userSocialConnections = this.userSocialConnectionRepository.findByUserIdOrderByRankDesc( user.getId() );
		
		for ( UserSocialConnection userSocialConnection : userSocialConnections )
		{
			final ConnectionFactory<?> connectionFactory = this.connectionFactoryLocator.getConnectionFactory( userSocialConnection.getProviderId() );
			final Connection<?> connection = connectionFactory.createConnection(userSocialConnection);
			
			final String providerId = connection.getKey().getProviderId();
			if ( connections.get(providerId).size() == 0 )
			{
				connections.put(providerId, new LinkedList<Connection<?>>());
			}
			connections.add(providerId, connection);
		}
		
		return connections;
	}

	/**
	 * 
	 */
	public List<Connection<?>> findConnections( String providerId )
	{
		final List<UserSocialConnection> userSocialConnections = this.userSocialConnectionRepository.findByUserIdAndProviderIdOrderByRankDesc(user.getId(), providerId);
		
		final List<Connection<?>> connections = new ArrayList<Connection<?>>();
		for ( UserSocialConnection userSocialConnection : userSocialConnections )
		{
			final ConnectionFactory<?> connectionFactory = this.connectionFactoryLocator.getConnectionFactory( userSocialConnection.getProviderId() );
			final Connection<?> connection = connectionFactory.createConnection(userSocialConnection);
			connections.add(connection);
		}
		return connections;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <A> List<Connection<A>> findConnections(Class<A> apiType)
	{
		final List<?> connections = this.findConnections(getProviderId(apiType));
		return (List<Connection<A>>) connections;
	}

	/**
	 * 
	 */
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers( MultiValueMap<String, String> providerUsers )
	{
		throw new Error("Not implemented");
		
//		if ( providerUsers == null || providerUsers.isEmpty() )
//		{
//			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
//		}
//		StringBuilder providerUsersCriteriaSql = new StringBuilder();
//		MapSqlParameterSource parameters = new MapSqlParameterSource();
//		parameters.addValue("userId", userId);
//		for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();)
//		{
//			Entry<String, List<String>> entry = it.next();
//			String providerId = entry.getKey();
//			providerUsersCriteriaSql.append("providerId = :providerId_")
//					.append(providerId)
//					.append(" and providerUserId in (:providerUserIds_")
//					.append(providerId).append(")");
//			parameters.addValue("providerId_" + providerId, providerId);
//			parameters.addValue("providerUserIds_" + providerId,
//					entry.getValue());
//			if (it.hasNext())
//			{
//				providerUsersCriteriaSql.append(" or ");
//			}
//		}
//		
//		List<Connection<?>> resultList = new NamedParameterJdbcTemplate(
//				jdbcTemplate).query(selectFromUserConnection()
//				+ " where userId = :userId and " + providerUsersCriteriaSql
//				+ " order by providerId, rank", parameters, connectionMapper);
//		MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
//		for (Connection<?> connection : resultList)
//		{
//			String providerId = connection.getKey().getProviderId();
//			List<String> userIds = providerUsers.get(providerId);
//			List<Connection<?>> connections = connectionsForUsers
//					.get(providerId);
//			if (connections == null)
//			{
//				connections = new ArrayList<Connection<?>>(userIds.size());
//				for (int i = 0; i < userIds.size(); i++)
//				{
//					connections.add(null);
//				}
//				connectionsForUsers.put(providerId, connections);
//			}
//			String providerUserId = connection.getKey().getProviderUserId();
//			int connectionIndex = userIds.indexOf(providerUserId);
//			connections.set(connectionIndex, connection);
//		}
//		return connectionsForUsers;
	}

	/**
	 * 
	 */
	public Connection<?> getConnection(ConnectionKey connectionKey)
	{
		final UserSocialConnection userSocialConnection = this.userSocialConnectionRepository.findByUserIdAndProviderIdAndProviderUserId( user.getId(), connectionKey.getProviderId(), connectionKey.getProviderUserId() );
		if ( userSocialConnection == null ) 
		{
			throw new NoSuchConnectionException(connectionKey);	
		}
		
		final ConnectionFactory<?> connectionFactory = this.connectionFactoryLocator.getConnectionFactory( userSocialConnection.getProviderId() );
		return connectionFactory.createConnection(userSocialConnection);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId)
	{
		final String providerId = this.getProviderId(apiType);
		return (Connection<A>) this.getConnection(new ConnectionKey(providerId, providerUserId));
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType)
	{
		final String providerId = this.getProviderId(apiType);
		final Connection<A> connection = (Connection<A>) this.findPrimaryConnection(providerId);
		if (connection == null)
		{
			throw new NotConnectedException(providerId);
		}
		return connection;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType)
	{
		final String providerId = this.getProviderId(apiType);
		return (Connection<A>) findPrimaryConnection(providerId);
	}

	/**
	 * 
	 */
	@Transactional
	public void addConnection( Connection<?> connection )
	{
		final ConnectionData data = connection.createData();
		
		int rank = this.userSocialConnectionRepository.findNewRankByUserIdAndProviderId( user.getId(), data.getProviderId() ); 
		
		try
		{
			final UserSocialConnection userSocialConnection = new UserSocialConnection();
			userSocialConnection.setUser( new User(user.getId()) );
			userSocialConnection.setProviderId( data.getProviderId() );
			userSocialConnection.setProviderUserId( data.getProviderUserId() );
			userSocialConnection.setRank( rank );
			userSocialConnection.setDisplayName( data.getDisplayName() );
			userSocialConnection.setProfileUrl( data.getProfileUrl() );
			userSocialConnection.setImageUrl( data.getImageUrl() );
			userSocialConnection.setExpireTime( data.getExpireTime() );
			
			//encrypted
			userSocialConnection.setAccessToken( data.getAccessToken() );
			userSocialConnection.setSecret( data.getSecret() );
			userSocialConnection.setRefreshToken( data.getRefreshToken() );
		
			this.userSocialConnectionRepository.save( userSocialConnection );
		}
		catch (DuplicateKeyException e)
		{
			throw new DuplicateConnectionException(connection.getKey());
		}
	}

	/**
	 * 
	 */
	@Transactional
	public void updateConnection( Connection<?> connection )
	{
		final ConnectionData data = connection.createData();
		
		final UserSocialConnection userSocialConnection = this.userSocialConnectionRepository.findByUserIdAndProviderIdAndProviderUserId(user.getId(), data.getProviderId(), data.getProviderUserId());
		userSocialConnection.setDisplayName( data.getDisplayName() );
		userSocialConnection.setProfileUrl( data.getProfileUrl() );
		userSocialConnection.setImageUrl( data.getImageUrl() );
		userSocialConnection.setExpireTime( data.getExpireTime() );
		
		//encrypted
		userSocialConnection.setAccessToken( data.getAccessToken() );
		userSocialConnection.setSecret( data.getSecret() );
		userSocialConnection.setRefreshToken( data.getRefreshToken() );

		this.userSocialConnectionRepository.save( userSocialConnection );
	}

	/**
	 * 
	 */
	@Transactional
	public void removeConnections( String providerId )
	{
		final List<UserSocialConnection> userSocialConnections = this.userSocialConnectionRepository.findByUserIdAndProviderIdOrderByRankDesc(user.getId(), providerId);
		this.userSocialConnectionRepository.delete(userSocialConnections);
	}

	/**
	 * 
	 */
	@Transactional
	public void removeConnection( ConnectionKey connectionKey )
	{
		final UserSocialConnection userSocialConnection = this.userSocialConnectionRepository.findByUserIdAndProviderIdAndProviderUserId(user.getId(), connectionKey.getProviderId(), connectionKey.getProviderUserId());
		this.userSocialConnectionRepository.delete( userSocialConnection );
	}

	/**
	 * 
	 * @param providerId
	 * @return
	 */
	private Connection<?> findPrimaryConnection(String providerId)
	{
		final List<UserSocialConnection> userSocialConnections = this.userSocialConnectionRepository.findByUserIdAndProviderIdOrderByRankDesc(user.getId(), providerId);
		
		if ( userSocialConnections.size() > 0 )
		{
			final ConnectionFactory<?> connectionFactory = this.connectionFactoryLocator.getConnectionFactory( userSocialConnections.get(0).getProviderId() );
			return connectionFactory.createConnection( userSocialConnections.get(0) );
		}
	
		return null;
	}

	/**
	 * 
	 * @param apiType
	 * @return
	 */
	private <A> String getProviderId(Class<A> apiType)
	{
		return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}
}

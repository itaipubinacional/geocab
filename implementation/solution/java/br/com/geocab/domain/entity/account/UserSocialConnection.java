package br.com.geocab.domain.entity.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.social.connect.ConnectionData;

import br.com.geocab.domain.entity.IEntity;

/**
 * @author rodrigo
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"userId", "providerId", "rank"}))
public class UserSocialConnection extends ConnectionData implements IEntity<String>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2308415825457233373L;
	
	/**
	 * @param providerId
	 * @param providerUserId
	 * @param displayName
	 * @param profileUrl
	 * @param imageUrl
	 * @param accessToken
	 * @param secret
	 * @param refreshToken
	 * @param expireTime
	 */
	public UserSocialConnection()
	{
		super(null, null, null, null, null, null, null, null, null);
	}
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Id
	private String userId;
	/**
	 * 
	 */
	@NotNull
	@Column(nullable=false)
	private String providerId;
	/**
	 * 
	 */
	@Column(length=255)
	private String providerUserId;
	/**
	 * 
	 */
	@NotNull
	@Column(nullable=false)
	private int rank;
	/**
	 * 
	 */
	@Column(length=255)
	private String displayName;
	/**
	 * 
	 */
	@Column(length=512)
	private String profileUrl;
	/**
	 * 
	 */
	@Column(length=512)
	private String imageUrl;
	/**
	 * 
	 */
	@NotNull
	@Column(length=255, nullable=false)
	private String accessToken;
	/**
	 * 
	 */
	@Column(length=255)
	private String secret;
	/**
	 * 
	 */
	@Column(length=255)
	private String refreshToken;
	/**
	 * 
	 */
	private Long expireTime;
	
	/*-------------------------------------------------------------------
	 *				 		   GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see br.com.eits.prize.global.domain.entity.IEntity#getId()
	 */
	@Override
	public String getId()
	{
		return this.getUserId();
	}
	/* (non-Javadoc)
	 * @see br.com.eits.prize.global.domain.entity.IEntity#setId(java.io.Serializable)
	 */
	@Override
	public void setId(String id)
	{
		this.setUserId(id);
	}
	/**
	 * @return the providerId
	 */
	public String getProviderId()
	{
		return providerId;
	}
	/**
	 * @param providerId the providerId to set
	 */
	public void setProviderId(String providerId)
	{
		this.providerId = providerId;
	}
	/**
	 * @return the providerUserId
	 */
	public String getProviderUserId()
	{
		return providerUserId;
	}
	/**
	 * @param providerUserId the providerUserId to set
	 */
	public void setProviderUserId(String providerUserId)
	{
		this.providerUserId = providerUserId;
	}
	/**
	 * @return the rank
	 */
	public int getRank()
	{
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank)
	{
		this.rank = rank;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName()
	{
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	/**
	 * @return the profileUrl
	 */
	public String getProfileUrl()
	{
		return profileUrl;
	}
	/**
	 * @param profileUrl the profileUrl to set
	 */
	public void setProfileUrl(String profileUrl)
	{
		this.profileUrl = profileUrl;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl()
	{
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the accessToken
	 */
	public String getAccessToken()
	{
		return accessToken;
	}
	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}
	/**
	 * @return the secret
	 */
	public String getSecret()
	{
		return secret;
	}
	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret)
	{
		this.secret = secret;
	}
	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken()
	{
		return refreshToken;
	}
	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken)
	{
		this.refreshToken = refreshToken;
	}
	/**
	 * @return the expireTime
	 */
	public Long getExpireTime()
	{
		return expireTime;
	}
	/**
	 * @param expireTime the expireTime to set
	 */
	public void setExpireTime(Long expireTime)
	{
		this.expireTime = expireTime;
	}
	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
}

package br.com.geocab.domain.entity.account;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.geocab.domain.entity.AbstractEntity;

/**
 * 
 * @author Vagner BC
 * @since 02/06/2014
 * @version 1.0
 * @category
 */
@Entity
@Audited
@DataTransferObject(javascript="User")
public class User extends AbstractEntity implements Serializable, UserDetails
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4052986759552589018L;
	
	//----
	// Default user
	//----
	public static final User ADMINISTRATOR = new User(1L, "Administrator", "admin@geocab.com.br"  , true , UserRole.ADMINISTRATOR , "admin");
	public static final User ANONYMOUS = new User(0L, "Anonymous", null, true , UserRole.ANONYMOUS , "anonymous");
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	//Basic
	/**
	 * 
	 */
	@NotEmpty
	@Column(nullable=false, length=50)
	private String name;
	/**
	 * 
	 */
	@Email
	@NotNull
	@Column(nullable=false, length=144, unique=true)
	private String email;
	/**
	 * 
	 */
	@NotNull
	@Column(nullable=false)
	private Boolean enabled;
	/**
	 * 
	 */
	@NotBlank
	@Length(min=8)
	@Column(nullable=false, length=100)
	private String password;
	
	@Transient
	private String newPassword;
	/**
	 * 
	 */
	@NotNull
	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private UserRole role;
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	public User()
	{
	}
	
	/**
	 * 
	 * @param id
	 */
	public User( Long id )
	{
		super( id );
	}
	
	/**
	 * 
	 * @param id
	 */
	public User( Long id, String name, String email , boolean enabled )
	{
		super( id );
		this.email = email;
		this.name = name;
		this.enabled = enabled;
	}
	
	/**
	 * 
	 * @param id
	 */
	public User( Long id, String name, String email , boolean enabled , UserRole role )
	{
		super( id );
		this.email = email;
		this.name = name;
		this.enabled = enabled;
		this.role = role;
	}


	
	/**
	 * 
	 * @param id
	 */
	public User( Long id, String name, String email,  boolean enabled ,  UserRole role , String password  )
	{
		super( id );
		this.email = email;
		this.name = name;
		this.enabled = enabled;
		this.password = password;
		this.role = role;
	}
	
	

	/*-------------------------------------------------------------------
	 *							BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	@Override
	@Transient
	public Set<GrantedAuthority> getAuthorities()
	{
		final Set<GrantedAuthority> authorities = new HashSet<>();
		
		if ( role == null )
		{
			return null;
		}
		
		if ( role.equals( UserRole.ADMINISTRATOR ) ) 
		{
			authorities.add( UserRole.ADMINISTRATOR );
		}
		
		if ( role.equals( UserRole.MODERATOR ) ) 
		{
			authorities.add( UserRole.MODERATOR );
		}
		
		if ( role.equals( UserRole.USER ) ) 
		{
			authorities.add( UserRole.USER );
		}

		return authorities;
	}
	
	/**
	 * 
	 */
	@Override
	@Transient
	public boolean isAccountNonExpired()
	{
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	@Transient
	public boolean isAccountNonLocked()
	{
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	@Transient
	public boolean isCredentialsNonExpired()
	{
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	@Transient
	public boolean isEnabled()
	{
		return this.enabled;
	}

	/* 
	 *
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	@Transient
	public String getPassword()
	{
		return this.password;
	}

	/* 
	 *
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	@Transient
	public String getUsername()
	{
		return this.email;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @return the enabled
	 */
	public Boolean getEnabled()
	{
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * @return the role
	 */
	public UserRole getRole()
	{
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(UserRole userRole)
	{
		this.role = userRole;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword()
	{
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}
	
	
}
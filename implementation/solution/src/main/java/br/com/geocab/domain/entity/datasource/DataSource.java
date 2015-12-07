/**
 * 
 */
package br.com.geocab.domain.entity.datasource;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.directwebremoting.annotations.DataTransferObject;
import org.geoserver.security.PublicKeyGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import br.com.geocab.domain.entity.AbstractEntity;

/**
 * 
 *  {@link DataSource}
 * 
 * @author Cristiano Correa
 * @since 22/05/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject(javascript="DataSource")
public class DataSource extends AbstractEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6937800700125744070L;
	
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Name of {@link DataSource}
	 */
	@NotEmpty
	@Column(nullable=false, length=144, unique=true)
	private String name;
	
	/**
	 * URL of {@link DataSource}
	 */
	@URL
	@Column(nullable=true, length=255  )
	private String url;
	
	/**
	 * Login to access the url {@link DataSource}
	 */
	@Column(nullable=true, length=144)
	private String login;
	
	/**
	 * Password to access the url {@link DataSource}
	 */
	@Column(nullable=true, length=144)
	private String password;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public DataSource()
	{
		
	}
	
	/**
	 * 
	 *
	 * @param id
	 */
	public DataSource( Long id )
	{
		this.setId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param url
	 * @param login
	 * @param password
	 */
	public DataSource( Long id, String name, String url, String login, String password)
	{
		this.setId(id);
		this.setName(name);
		this.setUrl(url);
		this.setLogin(login);
		this.setPassword(password);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param url
	 */
	public DataSource( Long id, String name, String url)
	{
		this.setId(id);
		this.setName(name);
		this.setUrl(url);

	}

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((login == null) ? 0 : login.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		DataSource other = (DataSource) obj;
		if (url == null)
		{
			if (other.url != null) return false;
		}
		else if (!url.equals(other.url)) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		if (password == null)
		{
			if (other.password != null) return false;
		}
		else if (!password.equals(other.password)) return false;
		if (login == null)
		{
			if (other.login != null) return false;
		}
		else if (!login.equals(other.login)) return false;
		return true;
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	/**
	 * @return the token
	 */
	public String getToken()
	{
		if (!(this.getLogin() == null)
				&& !this.getLogin().equals(null)
				&& this.getLogin().length() > 0)
		{
			return PublicKeyGenerator.generateKey(this.getLogin());
		}
		return null;
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
	 * @return the url
	 */
	public String getUrl()
	{
		//if there's a token at the url, remove it to add again
		if (this.url != null && this.url.contains("&authkey="))
		{
			this.url = this.url.replace(
					this.url.substring(this.url.indexOf("&authkey="),
							this.url.length()), "");
		}
		
		//Concat the authentication token case the data source needs authentication
		if (this.getToken() != null)
		{
			this.url = this.url.concat("&authkey=" + this.getToken());
		}
		
		return this.url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url)
	{
		//if there's a token at the url, remove it to add again
		if (this.url != null && this.url.contains("&authkey="))
		{
			this.url = this.url.replace(
					this.url.substring(this.url.indexOf("&authkey="),
							this.url.length()), "");
		}
		if (url != null && url.contains("&authkey="))
		{
			url = url.replace(
					url.substring(url.indexOf("&authkey="),
							url.length()), "");
		}
		
		//Concat the authentication token case the data source needs authentication
		if (this.getToken() != null)
		{
			url = url.concat("&authkey=" + this.getToken());
		}
		
		this.url = url;
	}

	/**
	 * @return the login
	 */
	public String getLogin()
	{
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login)
	{
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
}

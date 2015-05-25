package br.com.geocab.entity;

import java.io.Serializable;

/**
 * Created by Vinicius on 25/09/2014.
 */
public class DataSource implements Serializable
{

    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * Name of {@link DataSource}
     */
    private String name;
    /**
     * URL of {@link DataSource}
     */
    private String url;
    /**
     * Login to access the url {@link DataSource}
     */
    private String login;
    /**
     * Password to access the url {@link DataSource}
     */
    private String password;

    /**
     * Flag to see if it is internal or external {@link DataSource}
     */
    private Boolean internal;

    /*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    public DataSource(String name, String url, String login, String password, Boolean internal) {
        this.name = name;
        this.url = url;
        this.login = login;
        this.password = password;
        this.internal = internal;
    }

    /*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }
}

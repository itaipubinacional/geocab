package br.com.geocab.controller.delegate;

import br.com.geocab.entity.User;

/**
 *
 */
public abstract class AbstractDelegate
{


    /**
     *
     */
    public static User loggedUser;

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    /**
     *
     */
    protected final String urlPath;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public AbstractDelegate(String urlPath)
    {
        this.urlPath = urlPath;
    }
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/


    protected String getUrl()
    {
        //return "http://192.168.20.122:8080/geocab/" + urlPath;
        return "http://geocab.sbox.me/" + urlPath;
    }

}
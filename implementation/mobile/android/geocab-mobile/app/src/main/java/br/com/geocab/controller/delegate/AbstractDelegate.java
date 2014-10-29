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
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/


    /**
     *
     */
//    protected HttpHeaders commonHttpHeaders()
//    {
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType( MediaType.APPLICATION_JSON );
//
//        if ( loggedUser != null )
//        {
//            final String credentials = loggedUser.getEmail() + ":" + loggedUser.getPassword();
//            headers.set( "Authorization", "Basic " + Base64.encodeToString( credentials.getBytes(), Base64.NO_WRAP) );
//        }
//
//        return headers;
//    }
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/


    protected String getUrl()
    {
         return "http://192.168.20.122:8080/geocab/" + urlPath;
    }

}
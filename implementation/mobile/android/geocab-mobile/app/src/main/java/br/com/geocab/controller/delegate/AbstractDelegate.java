package br.com.geocab.controller.delegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

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
    protected final String urlPath;
    protected final Context context;
    protected ProgressDialog progressDialog;


	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public AbstractDelegate(String urlPath, Context context)
    {
        this.urlPath = urlPath;
        this.context = context;
    }
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/


    protected String getUrl()
    {
        //return "http://192.168.0.45:8080/geocab/" + urlPath;
        return "http://geocab.sbox.me/" + urlPath;
    }

    public void showLoadingDialog(){

        this.progressDialog = new ProgressDialog(this.context);
        this.progressDialog.setTitle("Carregando");
        this.progressDialog.setMessage("Por favor aguarde...");
        this.progressDialog.show();

    }

    public void hideLoadingDialog(){

        if ( this.progressDialog != null ){
            this.progressDialog.dismiss();
        }

    }

}
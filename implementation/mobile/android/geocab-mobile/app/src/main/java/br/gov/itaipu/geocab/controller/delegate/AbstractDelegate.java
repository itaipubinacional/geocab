package br.gov.itaipu.geocab.controller.delegate;

import android.app.ProgressDialog;
import android.content.Context;

import br.gov.itaipu.geocab.R;
import br.gov.itaipu.geocab.entity.User;

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
        return "https://geocab.itaipu.gov.br/api/" + urlPath;
        //return "http://geocab.sbox.me/api/" + urlPath;
    }

    public void showLoadingDialog(String message){

        this.progressDialog = new ProgressDialog(this.context);
        this.progressDialog.setTitle(R.string.wait);
        this.progressDialog.setMessage(message);
        this.progressDialog.show();

    }

    public void hideLoadingDialog(){

        if ( this.progressDialog != null ){
            this.progressDialog.dismiss();
        }

    }

}
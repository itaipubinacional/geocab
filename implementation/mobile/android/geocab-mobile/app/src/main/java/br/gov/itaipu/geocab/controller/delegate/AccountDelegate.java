package br.gov.itaipu.geocab.controller.delegate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import br.gov.itaipu.geocab.R;
import br.gov.itaipu.geocab.controller.activity.AuthenticationActivity;
import br.gov.itaipu.geocab.controller.activity.MapActivity;
import br.gov.itaipu.geocab.controller.activity.SplashScreenActivity;
import br.gov.itaipu.geocab.entity.User;
import br.gov.itaipu.geocab.util.DelegateHandler;

/**
 *
 */
public class AccountDelegate extends AbstractDelegate
{

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public AccountDelegate(Context context)
    {
        super("authentication", context);
    }

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/



    public void authenticateUser(final User user, final DelegateHandler delegateHandler) {

        progressDialog = new ProgressDialog(AccountDelegate.this.context);
        progressDialog.setTitle(R.string.authenticating);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(AccountDelegate.this.context.getString(R.string.please_wait));
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = this.getUrl()+"/user";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);
                        Gson json = new Gson();
                        User userResponse = json.fromJson(response, User.class);
                        userResponse.setCredentials(user.getCredentials());
                        AbstractDelegate.loggedUser = userResponse;

                        Intent mapIntent = new Intent(AccountDelegate.this.context, MapActivity.class);
                        AccountDelegate.this.context.startActivity(mapIntent);
                        progressDialog.dismiss();
                        ((Activity)AccountDelegate.this.context).finish();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        SplashScreenActivity.prefEditor = SplashScreenActivity.settings.edit();
                        SplashScreenActivity.prefEditor.putString("email", null);
                        SplashScreenActivity.prefEditor.putString("password", null);
                        SplashScreenActivity.prefEditor.commit();
                        progressDialog.dismiss();

                        if( error instanceof TimeoutError )
                        {
                            Toast.makeText(AccountDelegate.this.context, R.string.email_password_invalid, Toast.LENGTH_LONG).show();
                        }
                        else if( error instanceof AuthFailureError )
                        {
                            Toast.makeText(AccountDelegate.this.context, R.string.email_password_invalid, Toast.LENGTH_LONG).show();
                        }

                        if ( delegateHandler != null )
                            delegateHandler.responseHandler(error);

                    }
                }
        ) {
            // this is the relevant method
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", user.getEmail());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", user.getCredentials());
                params.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                return params;
            }
        };
        queue.add(postRequest);
    }

}
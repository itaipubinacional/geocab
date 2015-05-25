package br.com.geocab.controller.delegate;

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

import br.com.geocab.R;
import br.com.geocab.controller.activity.MapActivity;
import br.com.geocab.controller.activity.SplashScreenActivity;
import br.com.geocab.entity.User;

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



    public void checkLogin(final String credentials, boolean needAuthentication) {

        progressDialog = new ProgressDialog(AccountDelegate.this.context);

        if( needAuthentication )
        {
            progressDialog.setTitle(R.string.loading);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(AccountDelegate.this.context.getString(R.string.authenticating_application));
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = this.getUrl()+"/check";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);
                        Gson json = new Gson();
                        User user = json.fromJson(response, User.class);

                        AbstractDelegate.loggedUser = user;

                        SplashScreenActivity.prefEditor = SplashScreenActivity.settings.edit();
                        SplashScreenActivity.prefEditor.putString("email", user.getEmail());
                        SplashScreenActivity.prefEditor.commit();

                        if(SplashScreenActivity.settings.getAll().get("email") != null && SplashScreenActivity.settings.getAll().get("password") != null )
                        {
                            AbstractDelegate.loggedUser.setPassword(SplashScreenActivity.settings.getAll().get("password").toString());
                        }

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
                        if( error instanceof TimeoutError )
                        {
                            Toast.makeText(AccountDelegate.this.context, R.string.email_password_invalid, Toast.LENGTH_LONG).show();
                        }
                        else if( error instanceof AuthFailureError )
                        {
                            Toast.makeText(AccountDelegate.this.context, R.string.email_password_invalid, Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();

                    }
                }
        ) {
            // this is the relevant method
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("credentials", credentials);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void insertUserSocial(final User user){

        progressDialog = new ProgressDialog(AccountDelegate.this.context);
        progressDialog.setTitle(R.string.loading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(AccountDelegate.this.context.getString(R.string.authenticating_application));
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = this.getUrl()+"/create";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson json = new Gson();

                SplashScreenActivity.prefEditor = SplashScreenActivity.settings.edit();
                SplashScreenActivity.prefEditor.putString("email", user.getEmail());
                SplashScreenActivity.prefEditor.putString("password", "none");
                SplashScreenActivity.prefEditor.commit();

                User user = json.fromJson(response, User.class);

                AbstractDelegate.loggedUser = user;

                if(SplashScreenActivity.settings.getAll().get("email") != null && SplashScreenActivity.settings.getAll().get("password") != null )
                {
                    AbstractDelegate.loggedUser.setPassword(SplashScreenActivity.settings.getAll().get("password").toString());
                }

                Intent mapIntent = new Intent(AccountDelegate.this.context, MapActivity.class);
                AccountDelegate.this.context.startActivity(mapIntent);
                progressDialog.dismiss();
                ((Activity)AccountDelegate.this.context).finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AE",error.getMessage());
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",user.getEmail());
                params.put("name",user.getName());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

}
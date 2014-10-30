package br.com.geocab.controller.delegate;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import br.com.geocab.R;
import br.com.geocab.controller.activity.AuthenticationActivity;
import br.com.geocab.controller.activity.MapActivity;
import br.com.geocab.controller.activity.SplashScreenActivity;
import br.com.geocab.entity.User;

/**
 *
 */
public class AccountDelegate extends AbstractDelegate
{

    private final Context context;

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public AccountDelegate(Context context)
    {
        super("authentication");
        this.context = context;
    }



	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/



    public void checkLogin(final String credentials) {


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

                        if(SplashScreenActivity.settings.getAll().get("email") != null && SplashScreenActivity.settings.getAll().get("password") != null )
                        {
                            AbstractDelegate.loggedUser.setPassword(SplashScreenActivity.settings.getAll().get("password").toString());
                        }

                        Intent mapIntent = new Intent(AccountDelegate.this.context, MapActivity.class);
                        AccountDelegate.this.context.startActivity(mapIntent);
                        ((Activity)AccountDelegate.this.context).finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountDelegate.this.context, R.string.error_authentication, Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+error.toString());
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

    public void postNewComment(final User user){
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = this.getUrl()+"/create";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent mapIntent = new Intent(AccountDelegate.this.context, MapActivity.class);
                AccountDelegate.this.context.startActivity(mapIntent);
                ((Activity)AccountDelegate.this.context).finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AE",error.getMessage());
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
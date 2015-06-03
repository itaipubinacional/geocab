package br.com.geocab.controller.delegate;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.geocab.R;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.Marker;
import br.com.geocab.entity.MarkerAttribute;
import br.com.geocab.entity.MotiveMarkerModeration;
import br.com.geocab.util.DelegateHandler;
import br.com.geocab.util.MultiPartRequest;

/**
 *
 */
public class FileDelegate extends AbstractDelegate
{

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public FileDelegate(Context context)
    {
        super("files", context);
    }

    /*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     *
     * @param marker
     */
    public void downloadMarkerPicture(final Marker marker, final DelegateHandler delegateHandler)
    {
        String url = getUrl() + "/marker/"+marker.getId()+"/download";

        ImageRequest jReq = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response)
            {
                delegateHandler.responseHandler(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                delegateHandler.responseHandler(null);
            }
        } )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                final String credentials = loggedUser.getEmail() + ":" + loggedUser.getPassword();
                params.put("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                params.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                return params;
            }
        };

        jReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jReq);
    }

    public void uploadMarkerPicture( long markerId, File file, final DelegateHandler delegateHandler){

        String url = getUrl() + "/marker/"+markerId+"/upload";

        MultiPartRequest request = new MultiPartRequest(url, file, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                if ( delegateHandler != null )
                    delegateHandler.responseHandler(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                final String credentials = loggedUser.getEmail() + ":" + loggedUser.getPassword();
                params.put("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP) );
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);

    }

}
package br.com.geocab.controller.delegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.geocab.R;
import br.com.geocab.controller.activity.MapActivity;
import br.com.geocab.controller.adapter.NavDrawerListAdapter;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.GroupEntity;
import br.com.geocab.entity.Layer;
import br.com.geocab.entity.Marker;
import br.com.geocab.entity.MarkerAttribute;
import br.com.geocab.util.DelegateHandler;
import br.com.geocab.util.MultiPartRequest;

/**
 *
 */
public class MarkerDelegate extends AbstractDelegate
{

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public MarkerDelegate(Context context)
    {
        super("marker", context);
    }

    /*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @return
     */
    public void listMarkersByLayer( long layerId, final DelegateHandler delegateHandler )
    {
        String url = getUrl() + "/"+layerId+"/markers";

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                if ( delegateHandler != null )
                    delegateHandler.responseHandler(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "ERROR");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                final String credentials = loggedUser.getEmail() + ":" + loggedUser.getPassword();
                params.put("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP) );
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jReq);

    }

    /**
     *
     * @param marker
     */
    public void listMarkerAttributesByMarker( final Marker marker, final DelegateHandler delegateHandler )
    {
        String url = getUrl() +  "/"+marker.getId()+"/markerattributes";

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            Gson json = new Gson();

            @Override
            public void onResponse(JSONArray response)
            {
                marker.setMarkerAttributes(new ArrayList<MarkerAttribute>());
                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        MarkerAttribute markerAttribute = json.fromJson(response.getString(i), MarkerAttribute.class);
                        marker.getMarkerAttributes().add(markerAttribute);
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(MarkerDelegate.this.context, R.string.error_connection, Toast.LENGTH_SHORT).show();
                    }
                }

                if ( delegateHandler != null )
                    delegateHandler.responseHandler( json.toJson(marker) );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MarkerDelegate.this.context, R.string.error_connection, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                final String credentials = loggedUser.getEmail() + ":" + loggedUser.getPassword();
                params.put("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP) );
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jReq);

    }

    /**
     *
     * @param marker
     */
    public void downloadMarkerPicture(final Marker marker, final DelegateHandler delegateHandler)
    {
        String url =  "http://geocab.sbox.me/files/markers/"+marker.getId()+"/download";

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
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jReq);
    }

    public void uploadMarkerFile( long markerId, File file, final DelegateHandler delegateHandler){

        String url = getUrl() + "/"+markerId+"/uploadphoto";

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

    /**
     * Método para persistir o marker
     * @param marker
     * @param delegateHandler
     */
    public void insertMarker( final Marker marker, final DelegateHandler delegateHandler )
    {
        String url = getUrl() + "/";

        final Gson gson = new GsonBuilder().create();

        try {

            JSONObject markerJson = new JSONObject(gson.toJson(marker));

            JsonObjectRequest jReq = new JsonObjectRequest(Request.Method.POST, url, markerJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    if ( delegateHandler != null )
                        delegateHandler.responseHandler(gson.fromJson(response.toString(), Marker.class));
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
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(jReq);

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }

    /**
     * Método para atualizar o marker
     * @param marker
     * @param delegateHandler
     */
    public void updateMarker( final Marker marker, final DelegateHandler delegateHandler )
    {
        String url = getUrl() + "/";

        final Gson gson = new GsonBuilder().create();

        try {

            JSONObject markerJson = new JSONObject(gson.toJson(marker));

            JsonObjectRequest jReq = new JsonObjectRequest(Request.Method.PUT, url, markerJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    if ( delegateHandler != null )
                        delegateHandler.responseHandler(gson.fromJson(response.toString(), Marker.class));
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
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(jReq);

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }

    /**
     * Aprova o marker
     * @param markerId
     * @param delegateHandler
     */
    public void approveMarker( final long markerId, final DelegateHandler delegateHandler )
    {
        String url = getUrl() + "/"+markerId+"/approve";

        final Gson gson = new GsonBuilder().create();

        try {

            StringRequest jReq = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
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
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(jReq);

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }

    /**
     * Recusa o marker
     * @param markerId
     * @param delegateHandler
     */
    public void refuseMarker( final long markerId, final DelegateHandler delegateHandler )
    {
        String url = getUrl() + "/"+markerId+"/refuse";
        final Gson gson = new GsonBuilder().create();

        try {

            StringRequest jReq = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
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
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(jReq);

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }

    /**
     * Remove o marker
     * @param markerId
     * @param delegateHandler
     */
    public void removeMarker( final long markerId, final DelegateHandler delegateHandler )
    {
        String url = this.getUrl() +"/"+markerId;
        final Gson gson = new GsonBuilder().create();

        try {

            StringRequest jReq = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
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
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(jReq);

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }

}
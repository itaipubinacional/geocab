package br.com.geocab.controller.delegate;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.geocab.R;
import br.com.geocab.controller.adapter.NavDrawerListAdapter;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.Attribute;
import br.com.geocab.entity.Layer;
import br.com.geocab.util.DelegateHandler;
import br.com.geocab.util.JsonArrayPostRequest;

/**
 *
 */
public class LayerDelegate extends AbstractDelegate
{

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public LayerDelegate(Context context)
    {
        super("layergroup", context);
    }

    /*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/


    /**
     * @return
     */
    public void listLayersPublished(final DelegateHandler delegateHandler)
    {
        String url = getUrl()+ "/layers";

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            Gson json = new Gson();

            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Layer> result = new ArrayList<Layer>();

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        Layer layer = json.fromJson(response.getString(i), Layer.class);
                        if( layer.getIsChecked() == null )
                        {
                            layer.setIsChecked(false);
                        }
                        result.add(layer);

                    }
                    catch (JSONException e)
                    {
                        Log.d("ERRO", e.getMessage());
                    }
                }

                if ( delegateHandler != null )
                    delegateHandler.responseHandler(result);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LayerDelegate.this.context, R.string.problem_loading_layer, Toast.LENGTH_SHORT).show();
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
     * @return
     */
    public void listInternalLayers(final DelegateHandler delegateHandler)
    {
        String url = this.getUrl()+"/internal/layers";

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            Gson json = new Gson();

            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Layer> result = new ArrayList<Layer>();

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        Layer layer = json.fromJson(response.getString(i), Layer.class);
                        if( layer.getIsChecked() == null )
                        {
                            layer.setIsChecked(false);
                        }
                        result.add(layer);

                    }
                    catch (JSONException e)
                    {
                        Log.d("ERRO", e.getMessage());
                    }
                }

                if ( delegateHandler != null )
                    delegateHandler.responseHandler(result);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LayerDelegate.this.context, R.string.problem_loading_layer, Toast.LENGTH_SHORT).show();
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
     * @return
     */
    public void listAttributesByLayer(long layerId, final DelegateHandler delegateHandler)
    {
        String url = this.getUrl()+"/"+layerId+"/layerattributes";

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            Gson json = new Gson();

            @Override
            public void onResponse(JSONArray response) {

                ArrayList<Attribute> result = new ArrayList<Attribute>();

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        Attribute attribute = json.fromJson(response.getString(i), Attribute.class);
                        result.add(attribute);
                    }
                    catch (JSONException e) {
                        Log.d("ERRO", e.getMessage());
                    }
                }

                if ( delegateHandler != null )
                    delegateHandler.responseHandler(result);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LayerDelegate.this.context, R.string.problem_loading_layer, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jReq);

    }

    /**
     * @param layerUrls
     * @param delegateHandler
     */
    public void listLayerProperties(final String[] layerUrls, final DelegateHandler<String> delegateHandler) {

        String urlRequest = "http://192.168.0.45:8080/geocab/layergroup/layerproperties";
        JSONArray jsonArray = new JSONArray();

        try
        {
            for ( String url : layerUrls ){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", url);
                jsonArray.put(jsonObject);
            }
        }
        catch (JSONException error)
        {
            Log.d("Error", "Error: " + error.getMessage());
        }

        JsonArrayPostRequest request = new JsonArrayPostRequest(urlRequest, jsonArray.toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                delegateHandler.responseHandler(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                final String credentials = loggedUser.getEmail() + ":" + loggedUser.getPassword();
                params.put("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP) );
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }

}
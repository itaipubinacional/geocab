package br.gov.itaipu.geocab.controller.delegate;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.gov.itaipu.geocab.R;
import br.gov.itaipu.geocab.controller.app.AppController;
import br.gov.itaipu.geocab.entity.Attribute;
import br.gov.itaipu.geocab.entity.Layer;
import br.gov.itaipu.geocab.util.DelegateHandler;
import br.gov.itaipu.geocab.util.JsonArrayPostRequest;

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
                params.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
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
                params.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
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
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                final String credentials = loggedUser.getEmail() + ":" + loggedUser.getPassword();
                params.put("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP) );
                params.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jReq);

    }

    /**
     * @param layerUrls
     * @param delegateHandler
     */
    public void listLayerProperties(final String[] layerUrls, final DelegateHandler<String> delegateHandler) {

        String urlRequest = this.getUrl()+"/layerproperties";
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

        JsonArrayPostRequest request = new JsonArrayPostRequest(urlRequest, jsonArray.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("\"{","{");
                response = response.replace("}\"","}");
                delegateHandler.responseHandler(response);
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
package br.com.geocab.controller.delegate;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Base64;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.geocab.controller.adapter.ListLayerAdapter;
import br.com.geocab.controller.adapter.NavDrawerListAdapter;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.Layer;

/**
 *
 */
public class LayerMarkerDelegate extends AbstractDelegate
{

    private final Context context;

    private ListLayerAdapter listAdapter;

    private AnimationDrawable animationLoadLayer;

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public LayerMarkerDelegate(Context context, BaseAdapter adapter)
    {
        super("layergroup");

        this.context = context;

        this.listAdapter = (ListLayerAdapter) adapter;

    }

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/


    /**
     * @return
     */
    public void listLayersMarker()
    {
        String url = getUrl()+ "/layers";

        JsonArrayRequest jReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    Gson json = new Gson();

                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Layer> result = new ArrayList<Layer>();

                        for (int i = 0; i < response.length(); i++)
                        {
                            try
                            {
                                Layer layer = json.fromJson(response.getString(i), Layer.class);
                                result.add(layer);

                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(LayerMarkerDelegate.this.context, "Unable to call service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        LayerMarkerDelegate.this.listAdapter.setItemList(result);
                        LayerMarkerDelegate.this.listAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("ERROR", "ERROR");
            }
        })
        {
            // this is the relevant method
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                return params;
            }

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


}
package br.com.geocab.controller.delegate;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import br.com.geocab.controller.adapter.NavDrawerListAdapter;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.Layer;

/**
 *
 */
public class LayerDelegate
{

    private final Context context;

    private NavDrawerListAdapter listAdapter;

    private AnimationDrawable animationLoadLayer;

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public LayerDelegate(Context context, BaseAdapter adapter)
    {
        this.context = context;

        this.listAdapter = (NavDrawerListAdapter) adapter;

    }

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/


    /**
     * @return
     */
    public void listLayersPublished( AnimationDrawable d )
    {
        this.animationLoadLayer = d;

        String url = "http://192.168.20.122:8080/geocab/layergroup/layers";

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
                                if( layer.getIsChecked() == null )
                                {
                                    layer.setIsChecked(false);
                                }
                                result.add(layer);

                                LayerDelegate.this.animationLoadLayer.stop();

                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(LayerDelegate.this.context, "Unable to call service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        LayerDelegate.this.listAdapter.setItemList(result);
                        LayerDelegate.this.listAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error

            }
        });

        AppController.getInstance().addToRequestQueue(jReq);

    }


}
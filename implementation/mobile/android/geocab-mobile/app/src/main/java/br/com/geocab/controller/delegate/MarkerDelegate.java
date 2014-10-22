package br.com.geocab.controller.delegate;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
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

import br.com.geocab.controller.adapter.NavDrawerListAdapter;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.Layer;
import br.com.geocab.entity.Marker;

/**
 *
 */
public class MarkerDelegate extends AbstractDelegate
{

    private final Context context;

    private static String layerName;

    private static WebView webViewMap;

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public MarkerDelegate(Context context, WebView webViewMap, String name)
    {
        super("marker");

        this.context = context;

        this.layerName = name;

        this.webViewMap = webViewMap;

    }



    /*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/


    /**
     * @return
     */
    public void listMarkersByLayer( final long layerId )
    {
        String url = getUrl()+ "/"+layerId+"/markers";

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

                    Gson json = new Gson();

                    @Override
                    public void onResponse(JSONArray response)
                    {
                        ArrayList<Marker> result = new ArrayList<Marker>();

                        for (int i = 0; i < response.length(); i++)
                        {
                            try
                            {
                                Marker marker = json.fromJson(response.getString(i), Marker.class);

                                //result.add(marker);

                                webViewMap.loadUrl("javascript:showMarker(\"" + marker.getLatitude() + "\",\"" + marker.getLongitude() + "\", \""+layerName+"\", \""+marker.getId()+"\")");


                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(MarkerDelegate.this.context, "Unable to call service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

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
                //final String credentials = loggedUser.getEmail() + ":" + loggedUser.getPassword();
                final String credentials = "admin@admin.com:admin";
                params.put("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP) );
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jReq);

    }


}
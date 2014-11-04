package br.com.geocab.controller.delegate;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import br.com.geocab.controller.activity.dialog.DialogInformation;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.GroupEntity;
import br.com.geocab.entity.Marker;
import br.com.geocab.entity.MarkerAttribute;

/**
 *
 */
public class MarkerDelegate extends AbstractDelegate
{

    private final Context context;

    private String layerName;

    private WebView webViewMap;

    private DialogInformation dialogInformation;


	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    public MarkerDelegate(Context context, DialogInformation dialogInformation)
    {
        super("marker");

        this.context = context;

        this.dialogInformation = dialogInformation;

    }

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
    public void listMarkersByLayer( long layerId, final String layerIcon )
    {
        String url = getUrl()+ "/"+layerId+"/markers";

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            GsonBuilder gsonBuilder = new GsonBuilder();

            @Override
            public void onResponse(JSONArray response)
            {
                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {

                        gsonBuilder.registerTypeAdapter(Calendar.class,
                                new JsonDeserializer<Calendar>() {
                                    @Override
                                    public Calendar deserialize(JsonElement jsonElement, Type type,
                                                            JsonDeserializationContext context)
                                            throws JsonParseException {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(jsonElement.getAsLong());
                                        return calendar;
                                    }
                                });

                        Gson json = gsonBuilder.create();
                        Marker marker = json.fromJson(response.getString(i), Marker.class);

                        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
                        String dateFormatted = df1.format(marker.getCreated().getTime());

                        webViewMap.loadUrl("javascript:showMarker(\"" + marker.getLatitude() + "\",\"" + marker.getLongitude() + "\", \""+marker.getId()+"\", \""+marker.getUser().getName()+"\", \""+dateFormatted+"\", \""+layerName+"\", \""+layerIcon+"\")");


                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(MarkerDelegate.this.context, R.string.error_connection, Toast.LENGTH_SHORT).show();
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
    public void listMarkerAttributesByMarker( final Marker marker )
    {
        String url = getUrl()+ "/"+marker.getId()+"/markerattributes";

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            Gson json = new Gson();

            @Override
            public void onResponse(JSONArray response)
            {
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

                MarkerDelegate.this.dialogInformation.populateMarkerAttributes(marker);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(MarkerDelegate.this.context, R.string.error_connection, Toast.LENGTH_SHORT).show();

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

    /**
     *
     * @param marker
     */
    public void downloadMarkerPicture(final Marker marker)
    {
        String url =  "http://geocab.sbox.me/files/markers/"+marker.getId()+"/download";

        ImageRequest jReq = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response)
            {

                marker.setImage(response);

                listMarkerAttributesByMarker(marker);

            }
        }, 0, 0, null, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
               marker.setImage(null);
               listMarkerAttributesByMarker(marker);

            }
        } )

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
                params.put("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jReq);
    }

    /**
     *
     * @param url
     * @param title
     */
    public void listLayerProperties(String url, final String title) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());

                GroupEntity groupEntity = new GroupEntity();

                try {

                    if( ((JSONArray)response.get("features")).length() > 0 )
                    {

                        JSONObject responseFeatures = ((JSONArray)response.get("features")).getJSONObject(0).getJSONObject("properties");
                        Iterator<String> iter = responseFeatures.keys();

                        while (iter.hasNext()) {
                            String key = iter.next();
                            Object value = responseFeatures.get(key);

                            try
                            {
                                String encodedKey = URLEncoder.encode(key, "ISO-8859-1");
                                String decodedKey = URLDecoder.decode(encodedKey, "UTF-8");

                                String encodedValue = URLEncoder.encode(value.toString(), "ISO-8859-1");
                                String decodedValue = URLDecoder.decode(encodedValue, "UTF-8");

                                GroupEntity.GroupItemEntity groupItemEntity = groupEntity.new GroupItemEntity();
                                groupEntity.title = title;

                                groupItemEntity.title = decodedKey;
                                groupItemEntity.value = decodedValue;
                                groupEntity.groupItemCollection.add(groupItemEntity);

                            }
                            catch (UnsupportedEncodingException e)
                            {
                                Toast.makeText(MarkerDelegate.this.context, R.string.error_connection, Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                    dialogInformation.populateLayerProperties(groupEntity);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }

        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
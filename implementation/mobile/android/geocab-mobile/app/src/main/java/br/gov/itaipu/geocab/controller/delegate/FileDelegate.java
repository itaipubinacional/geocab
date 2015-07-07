package br.gov.itaipu.geocab.controller.delegate;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import br.gov.itaipu.geocab.R;
import br.gov.itaipu.geocab.controller.app.AppController;
import br.gov.itaipu.geocab.entity.Marker;
import br.gov.itaipu.geocab.util.DelegateHandler;
import br.gov.itaipu.geocab.util.MultiPartRequest;

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
                params.put("Authorization", loggedUser.getCredentials());
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
                params.put("Authorization", loggedUser.getCredentials() );
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);

    }

}
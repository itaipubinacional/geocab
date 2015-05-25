package br.com.geocab.util;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import br.com.geocab.controller.activity.MapActivity;

public class JavaScriptHandler {

    MapActivity mapActivity;

    public JavaScriptHandler(MapActivity activity)  {
        mapActivity = activity;
    }

    public void vibrateOnSelect()
    {
        Vibrator vb = (Vibrator) this.mapActivity.getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(100);
    }

    public void showInformation(long markerId, String[] obj)
    {
        this.mapActivity.showLayerMarker(markerId, obj);
    }

    public void showOpenMenuButton()
    {
        this.mapActivity.showOpenMenuButton();
    }

}

package br.com.geocab.util;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import br.com.geocab.controller.activity.MapActivity;
import br.com.geocab.controller.activity.dialog.DialogInformation;

public class JavaScriptHandler {
    MapActivity mapActivity;
    public JavaScriptHandler(MapActivity activity)  {
        mapActivity = activity;
    }

    public void showOtherMarker(double[] s){
        this.mapActivity.showOtherMarker( s[0], s[1]);
    }

    public void vibrateOnSelect()
    {
        Vibrator vb = (Vibrator) this.mapActivity.getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(100);
    }

    public void showInformation()
    {
        this.mapActivity.showInformation();
    }

    public void showLayerId(int layerId)
    {
        Toast.makeText(this.mapActivity, "LayerId: " + layerId, Toast.LENGTH_SHORT).show();
    }



}

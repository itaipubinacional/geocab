package br.com.geocab.util;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import br.com.geocab.controller.activity.MapActivity;

public class JavaScriptHandler {
    MapActivity mapActivity;
    public JavaScriptHandler(MapActivity activity)  {
        mapActivity = activity;
    }

    public void showOtherMarker(double[] s){
        //Toast.makeText(mapActivity, "lat: " + s[0] + "lon: " + s[1], Toast.LENGTH_SHORT).show();
        this.mapActivity.showOtherMarker( s[0], s[1]);
    }

    public void calcSomething(int x, int y){
        this.mapActivity.changeText("Result is : " + (x * y));
    }
}

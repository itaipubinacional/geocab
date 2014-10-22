package br.com.geocab.controller.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import br.com.geocab.R;
import br.com.geocab.controller.activity.dialog.DialogInformation;
import br.com.geocab.controller.adapter.NavDrawerListAdapter;
import br.com.geocab.controller.delegate.LayerDelegate;
import br.com.geocab.controller.delegate.MarkerDelegate;
import br.com.geocab.util.GPSTracker;
import br.com.geocab.util.JavaScriptHandler;

public class MapActivity extends Activity
{

    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * side menu
     */
    private DrawerLayout mDrawerLayout;
    /**
     * listview side menu
     */
    private ListView mDrawerList;
    /**
     * button to open side menu
     */
    private ActionBarDrawerToggle mDrawerToggle;
    /**
     * nav drawer adapter listview nav drawer
     */
    private NavDrawerListAdapter adapter;
    /**
     * web view map
     */
    private WebView webViewMap;
    /**
     * location to get position user
     */
    private GPSTracker gpsTracker;
    /**
     * search layer edit text
     */
    private EditText searchLayerEditText;
    /**
     *
     */
    private Button buttonClearSearchLayerEditText;
    /**
     *
     */
    private Button buttonRefreshLayers;
    /**
     *
     */
    private Button buttonOpenMenu;
    /**
     * the layout side menu list
     */
    private LinearLayout linearLayoutLayer;
    /**
     *
     */
    private LayerDelegate layerDelegate;
    /**
     *
     */
    private AnimationDrawable animationLoadLayer;

    /**
     *
     */
    private TextView textViewSelectedCountItems;
    /**
     *
     */
    private TextView textViewTotalItems;

    private DialogInformation dialogInformation;

    private Button buttonTestInformation;

    /*-------------------------------------------------------------------
	 *				 		     HANDLERS
	 *-------------------------------------------------------------------*/

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_map);

        dialogInformation = new DialogInformation(this);
        buttonTestInformation = (Button) findViewById(R.id.btn_add_marker);
        buttonTestInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInformation.childSectionView();
            }
        });

        textViewSelectedCountItems = (TextView) findViewById(R.id.text_view_count_selected_items);
        textViewTotalItems = (TextView) findViewById(R.id.text_view_total_items);

        //===========================================


        webViewMap = (WebView) findViewById(R.id.web_view_map);
        webViewMap.getSettings().setJavaScriptEnabled(true);
        webViewMap.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webViewMap.getSettings().setRenderPriority(RenderPriority.HIGH);
        webViewMap.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webViewMap.loadUrl("file:///android_asset/map.html");

        //String html = "<!DOCTYPE html><html lang=en><head><link rel=stylesheet href=ol.css type=text/css><style>div.map{height:100%;width:100%}</style><script src=ol.js type=text/javascript></script><script src=jquery.min.js type=text/javascript></script><script src=jquery.mobile-1.4.4.min.js></script><body style=margin:0><div id=map class=map></div><script type=text/javascript>function showLayer(e,o,a){if(a){var n=new ol.source.TileWMS({url:e,params:{LAYERS:o}}),r=new ol.layer.Tile({source:n});map.addLayer(r)}}function showUserMarker(e,o){var a=new ol.Feature({geometry:new ol.geom.Point([o,e]),name:\"Localizacao\"}),n=new ol.style.Style({image:new ol.style.Icon({size:[48,48],src:\"http://iconshow.me/media/images/Mixed/small-n-flat-icon/png2/48/-map-marker.png\"})}),r=new ol.source.Vector({features:[a]}),l=new ol.layer.Vector({source:r,style:n});map.addLayer(l)}function showToast(e){return app.makeToast(e),!1}function showOther(e){app.makeToast2();var o=new ol.Feature({geometry:new ol.geom.Point([e[0],e[1]]),name:\"Localizacao\"}),a=new ol.style.Style({image:new ol.style.Icon({size:[48,48],src:\"http://iconshow.me/media/images/Mixed/small-n-flat-icon/png2/48/-map-marker.png\"})}),n=new ol.source.Vector({features:[o]}),r=new ol.layer.Vector({source:n,style:a});map.addLayer(r)}map=new ol.Map({target:\"map\",layers:[new ol.layer.Tile({source:new ol.source.OSM})],view:new ol.View({center:ol.proj.transform([-54.1394,-24.7568],\"EPSG:4326\",\"EPSG:3857\"),zoom:7})}),$(function(){function e(){var e=!1;map.on(\"click\",function(o){e||(showOther(o.coordinate),e=!0)})}$(\"div.map\").bind(\"taphold\",e)});</script>";
        //webViewMap.loadDataWithBaseURL("file:///android_asset/blank.html",  html, "text/html", "utf-8", null);

        webViewMap.addJavascriptInterface(new JavaScriptHandler(this), "app");

        searchLayerEditText = (EditText) findViewById(R.id.edit_text_search_layer);
        buttonRefreshLayers = (Button) findViewById(R.id.button_refresh_layers);

        this.animationLoadLayer =  (AnimationDrawable) buttonRefreshLayers.getCompoundDrawables()[0];

        buttonRefreshLayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                MapActivity.this.animationLoadLayer.start();

                MapActivity.this.layerDelegate.listLayersPublished(MapActivity.this.animationLoadLayer);
            }
        });

        buttonClearSearchLayerEditText = (Button) findViewById(R.id.button_clear_edit_text);
        buttonClearSearchLayerEditText.setVisibility(RelativeLayout.INVISIBLE);
        buttonClearSearchLayerEditText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchLayerEditText.setText("");
            }
        });

        searchLayerEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if( cs.length() > 0 )
                {
                    buttonClearSearchLayerEditText.setVisibility(RelativeLayout.VISIBLE);
                }
                else
                {
                    buttonClearSearchLayerEditText.setVisibility(RelativeLayout.INVISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {

            }

            @Override
            public void afterTextChanged(Editable arg0)
            {
                String text = searchLayerEditText.getText().toString().toLowerCase(Locale.getDefault());
                MapActivity.this.adapter.filter(text);
            }
        });

        linearLayoutLayer = (LinearLayout) findViewById(R.id.left_drawer);

        buttonOpenMenu = (Button) findViewById(R.id.btn_open_menu);
        buttonOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(linearLayoutLayer);
            }
        });

        //NAV DRAWER

        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mDrawerList=(ListView) findViewById(R.id.list_slidermenu);

        mDrawerList.setTextFilterEnabled(true);

        // setting the nav drawer list adapter
        Object[] objects = new Object[3];
        objects[0]= this.webViewMap;
        objects[1] = this.textViewSelectedCountItems;
        objects[2] = this.textViewTotalItems;

        adapter=new NavDrawerListAdapter(getApplicationContext(), 0,objects);
        mDrawerList.setAdapter(adapter);

        this.layerDelegate=new LayerDelegate(MapActivity.this, adapter);

        this.layerDelegate.listLayersPublished(MapActivity.this.animationLoadLayer);

        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,
                        R.drawable.ic_drawer, //nav menu toggle icon
                        R.string.app_name, // nav drawer open - description for accessibility
                        R.string.app_name // nav drawer close - description for accessibility
                )
        {
        public void onDrawerClosed (View view)
        {
            hideSoftKeyboard(MapActivity.this);

            invalidateOptionsMenu();

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        public void onDrawerOpened(View drawerView)
        {
            hideSoftKeyboard(MapActivity.this);

            invalidateOptionsMenu();

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null)
        {
            // on first time display view for first nav item
            //selectItem(0);
        }
    }

    public void showOtherMarker(double lat, double lon)
    {
        webViewMap.loadUrl("javascript:showOther(\"" + lat + "\",\"" + lon + "\")");
    }

    public void showInformation()
    {
            DialogInformation dialogInformation = new DialogInformation(this);
            dialogInformation.childSectionView();
    }

    /**
     *
     * Close keyboard
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}

package br.com.geocab.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import br.com.geocab.R;
import br.com.geocab.controller.adapter.NavDrawerListAdapter;
import br.com.geocab.controller.delegate.LayerDelegate;
import br.com.geocab.entity.Layer;
import br.com.geocab.util.GPSTracker;

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
     * nav drawer title
     */
    private CharSequence mDrawerTitle;
    /**
     * used to store app title
     */
    private CharSequence mTitle;
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

        setContentView(R.layout.activity_map);

        webViewMap = (WebView) findViewById(R.id.web_view_map);
        webViewMap.getSettings().setJavaScriptEnabled(true);
        webViewMap.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webViewMap.setWebViewClient(new WebViewClient());
        webViewMap.loadUrl("file:///android_asset/map.html");

        //String html = "<!DOCTYPE html><html lang=en><head><link rel=stylesheet href=ol.css type=text/css><style>.map{height:100%;width:100%}</style><body style=margin:0><script src=ol.js type=text/javascript></script><script src=jquery.min.js type=text/javascript></script><div id=map class=map></div><script type=text/javascript>function showLayer(e,o,r){if(r){var a=new ol.source.TileWMS({url:e,params:{LAYERS:o}}),l=new ol.layer.Tile({source:a});map.addLayer(l)}}map=new ol.Map({target:\"map\",layers:[new ol.layer.Tile({source:new ol.source.OSM})],view:new ol.View({center:ol.proj.transform([-54.1394,-24.7568],\"EPSG:4326\",\"EPSG:3857\"),zoom:7})});</script>";
        //webViewMap.loadDataWithBaseURL("file:///android_asset/blank.html",  html, "text/html", "utf-8", null);

        try{
            InputStream is = getAssets().open("aaa.html");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String str = new String(buffer);

            webViewMap.loadData(str, "text/html", null);
        }
        catch (Exception e)
        {

        }


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

        //NAV DRAWER

        // nav drawer title
        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        mDrawerList.setTextFilterEnabled(true);

        // setting the nav drawer list adapter
        Object[] objects = new Object[1];
        objects[0] = this.webViewMap;
        adapter = new NavDrawerListAdapter(getApplicationContext(), 0, objects);
        mDrawerList.setAdapter(adapter);

        this.layerDelegate = new LayerDelegate(MapActivity.this, adapter);
        this.layerDelegate.listLayersPublished(MapActivity.this.animationLoadLayer);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view)
            {
                getActionBar().setTitle(mTitle);

                hideSoftKeyboard(MapActivity.this);

//                for(Layer layer : MapActivity.this.adapter.getListLayers() )
//                {
//                    int index = layer.getName().indexOf(":");
//                    int position = layer.getDataSource().getUrl().lastIndexOf("geoserver/");
//                    String typeLayer = layer.getName().substring(0,index);
//                    String nameLayer = layer.getName().substring(index+1,layer.getName().length());
//                    String urlFormated = layer.getDataSource().getUrl().substring(0, position+10)+typeLayer+"wms";
//
//                    webViewMap.loadUrl("javascript:showLayer(\""+urlFormated+"\",\""+nameLayer+"\",\""+layer.getIsChecked()+"\")");
//                }

                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons

                hideSoftKeyboard(MapActivity.this);

                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null)
        {
            // on first time display view for first nav item
            //selectItem(0);
        }
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
     *
     * Initialize the contents of the Activity's standard options menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * Prepare the Screen's standard options menu to be displayed.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(this.linearLayoutLayer);
        menu.findItem(R.id.action_localization).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_localization:
                gpsTracker = new GPSTracker(MapActivity.this);

                // check if GPS enabled
                if(gpsTracker.canGetLocation()){

                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();

                    webViewMap.loadUrl("javascript:userLocation(\""+latitude+"\",\""+longitude+"\")");

                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gpsTracker.showSettingsAlert();
                }
                return true;
            case R.id.action_other_localization:
                Intent intent = new Intent(MapActivity.this, MarkActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     * set title from nav drawer
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title)
    {
        mTitle = title;
        getActionBar().setTitle(mTitle);
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

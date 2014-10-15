package br.com.geocab.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import br.com.geocab.R;
import br.com.geocab.controller.adapter.ListLayerAdapter;
import br.com.geocab.controller.delegate.LayerMarkerDelegate;

public class LayerActivity extends Activity {

    /**
     * listview layer marker
     */
    private ListLayerAdapter listLayerAdapter;

    /**
     *
     */
    private LayerMarkerDelegate layerMarkerDelegate;
    /**
     * listview layers
     */
    private ListView listViewLayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer);

        listViewLayers = (ListView) findViewById(R.id.list_view_layer);

        listLayerAdapter = new ListLayerAdapter(getApplicationContext(), 0);
        listViewLayers.setAdapter(listLayerAdapter);

        this.layerMarkerDelegate = new LayerMarkerDelegate(LayerActivity.this, listLayerAdapter);

        this.layerMarkerDelegate.listLayersMarker();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.layer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

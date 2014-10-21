package br.com.geocab.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.geocab.R;
import br.com.geocab.controller.adapter.ListLayerAdapter;
import br.com.geocab.controller.delegate.LayerMarkerDelegate;
import br.com.geocab.entity.Layer;

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

        this.listViewLayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();
                Layer layer = (Layer) parent.getAdapter().getItem(position);
                returnIntent.putExtra("name", layer.getName());
                returnIntent.putExtra("title", layer.getTitle());
                returnIntent.putExtra("id", layer.getId());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
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

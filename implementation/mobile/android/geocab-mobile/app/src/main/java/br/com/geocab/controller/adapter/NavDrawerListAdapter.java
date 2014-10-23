package br.com.geocab.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.geocab.R;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.controller.delegate.MarkerDelegate;
import br.com.geocab.entity.Layer;
import br.com.geocab.entity.Marker;

public class NavDrawerListAdapter extends ArrayAdapter {

	private Context context;
	private ArrayList<Layer> navDrawerItems;
	private ArrayList<Layer> navDrawerItemsSearch;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private WebView webViewMap;

    private int selectedItemCount = 0;
    private final int limitCheckList = 3;

    private TextView textViewSelectedCountItems;
    private TextView textViewTotalItems;

    private MarkerDelegate markerDelegate;

    private List<Marker> markers;

    public NavDrawerListAdapter(Context context, int resource, Object[] objects) {
        super(context, resource);

        this.context = context;
        this.navDrawerItems = new ArrayList<Layer>();
        this.navDrawerItemsSearch = new ArrayList<Layer>();
        this.webViewMap = (WebView) objects[0];
        this.textViewSelectedCountItems = (TextView) objects[1];
        this.textViewTotalItems = (TextView) objects[2];
    }

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

    /**
     *
     */
    public List<Layer> getListLayers()
    {
        return this.navDrawerItems;
    }

    /**
     *
     * @param layers
     */
    public void setItemList(ArrayList<Layer> layers)
    {
        if( this.navDrawerItems.size() == 0 || this.navDrawerItems.size() < layers.size())
        {
            this.navDrawerItems.clear();
            this.navDrawerItemsSearch.clear();
            this.navDrawerItems.addAll(layers);
            this.navDrawerItemsSearch.addAll(layers);
        }
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        
        textViewTotalItems.setText(Integer.toString(limitCheckList));

        if (convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.networkImageViewLegend = (NetworkImageView) convertView.findViewById(R.id.network_image_view_legend);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.text_view_title_layer);
            viewHolder.checkBoxLayer = (CheckBox) convertView.findViewById(R.id.checkbox_layer);
            viewHolder.txtSelectedCountItems = (TextView) convertView.findViewById(R.id.text_view_count_selected_items);

            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (imageLoader == null)
        {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        Layer layer = (Layer) getItem(position);
        viewHolder.checkBoxLayer.setTag(layer);
        viewHolder.txtTitle.setText(layer.getTitle());
        viewHolder.networkImageViewLegend.setImageUrl(layer.getLegend(), imageLoader);

        viewHolder.checkBoxLayer.setChecked(layer.getIsChecked());

        viewHolder.checkBoxLayer.setOnCheckedChangeListener(null);
        viewHolder.checkBoxLayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CheckBox checkBoxLayer = (CheckBox) buttonView;
                Layer layer = (Layer) checkBoxLayer.getTag();

                if( (isChecked && layer.getIsChecked()) || (!isChecked && !layer.getIsChecked()) )
                {
                    return;
                }

                if( isChecked && !layer.getIsChecked() )
                {
                    if( selectedItemCount >= limitCheckList )
                    {
                        Toast.makeText(NavDrawerListAdapter.this.context, "Limite maximo de seleções", Toast.LENGTH_LONG).show();
                        checkBoxLayer.setChecked(false);
                        return;
                    }

                    layer.setIsChecked(true);
                    selectedItemCount++;
                } else if( !isChecked && layer.getIsChecked() )
                {
                    layer.setIsChecked(false);
                    selectedItemCount--;
                }

                textViewSelectedCountItems.setText(Integer.toString(selectedItemCount));

                //layer.setIsChecked(isChecked);

                if( layer.getDataSource().getUrl() != null )
                {
                    int index = layer.getName().indexOf(":");
                    int position = layer.getDataSource().getUrl().lastIndexOf("geoserver/");
                    String typeLayer = layer.getName().substring(0,index);
                    String nameLayer = layer.getName().substring(index+1,layer.getName().length());
                    String urlFormated = layer.getDataSource().getUrl().substring(0, position+10)+typeLayer+"/wms";

                    webViewMap.loadUrl("javascript:showLayer(\""+urlFormated+"\",\""+nameLayer+"\",\""+layer.getIsChecked()+"\")");
                }
                else
                {
                    if( layer.getIsChecked() )
                    {
                        NavDrawerListAdapter.this.markerDelegate = new MarkerDelegate(NavDrawerListAdapter.this.context, webViewMap, layer.getName() );
                        NavDrawerListAdapter.this.markerDelegate.listMarkersByLayer(layer.getId());
                    }
                    else
                    {
                        webViewMap.loadUrl("javascript:closeMarker(\""+ layer.getName() + "\")");
                    }


                }


            }
        });

        viewHolder.checkBoxLayer.setChecked(layer.getIsChecked());

        return convertView;
	}

    /**
     *
     * @param charText
     */
    public void filter(String charText)
    {
        this.navDrawerItems.clear();
        if (charText.length() == 0)
        {
            this.navDrawerItems.addAll(this.navDrawerItemsSearch);
        }
        else
        {
            for (Layer layer : this.navDrawerItemsSearch)
            {
                if ( layer.getTitle().toLowerCase(Locale.getDefault()).contains(charText.toLowerCase(Locale.getDefault())))
                {
                    this.navDrawerItems.add(layer);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView txtTitle;
        NetworkImageView networkImageViewLegend;
        CheckBox checkBoxLayer;
        TextView txtSelectedCountItems;
        TextView txtTotalItems;
    }

}

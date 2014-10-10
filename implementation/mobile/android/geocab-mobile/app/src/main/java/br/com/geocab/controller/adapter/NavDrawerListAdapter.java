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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.geocab.R;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.Layer;

public class NavDrawerListAdapter extends ArrayAdapter {

	private Context context;
	private ArrayList<Layer> navDrawerItems;
	private ArrayList<Layer> navDrawerItemsSearch;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private WebView webViewMap;

    public NavDrawerListAdapter(Context context, int resource, Object[] objects) {
        super(context, resource);

        this.context = context;
        this.navDrawerItems = new ArrayList<Layer>();
        this.navDrawerItemsSearch = new ArrayList<Layer>();
        this.webViewMap = (WebView) objects[0];
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
        if (convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        if (imageLoader == null)
        {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        NetworkImageView networkImageViewLegend = (NetworkImageView) convertView
                .findViewById(R.id.network_image_view_legend);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.text_view_title_layer);
        CheckBox checkBoxLayer = (CheckBox) convertView.findViewById(R.id.checkbox_layer);

        checkBoxLayer.setTag(getItem(position));

        txtTitle.setText(navDrawerItems.get(position).getTitle());

        // thumbnail image
        networkImageViewLegend.setImageUrl(navDrawerItems.get(position).getLegend(), imageLoader);


        checkBoxLayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CheckBox checkBoxLayer = (CheckBox) buttonView;

                if( isChecked )
                {
                    Layer layer = (Layer) checkBoxLayer.getTag();

                    layer.setIsChecked(isChecked);

                    int index = layer.getName().indexOf(":");
                    int position = layer.getDataSource().getUrl().lastIndexOf("geoserver/");
                    String typeLayer = layer.getName().substring(0,index);
                    String nameLayer = layer.getName().substring(index+1,layer.getName().length());
                    String urlFormated = layer.getDataSource().getUrl().substring(0, position+10)+typeLayer+"/wms";

                    webViewMap.loadUrl("javascript:showLayer(\""+urlFormated+"\",\""+nameLayer+"\",\""+layer.getIsChecked()+"\")");

                }
            }
        });

//        checkBoxLayer.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                CheckBox checkBoxLayer = (CheckBox) v;
//
//                if( checkBoxLayer.isChecked() )
//                {
//                    Layer layer = (Layer) checkBoxLayer.getTag();
//
//                    layer.setIsChecked(checkBoxLayer.isChecked());
//
//                    int index = layer.getName().indexOf(":");
//                    int position = layer.getDataSource().getUrl().lastIndexOf("geoserver/");
//                    String typeLayer = layer.getName().substring(0,index);
//                    String nameLayer = layer.getName().substring(index+1,layer.getName().length());
//                    String urlFormated = layer.getDataSource().getUrl().substring(0, position+10)+typeLayer+"/wms";
//
//                    webViewMap.loadUrl("javascript:showLayer(\""+urlFormated+"\",\""+nameLayer+"\",\""+layer.getIsChecked()+"\")");
//
//                }
//
//            }
//        });
        
        return convertView;
	}

    /**
     *
     * @param charText
     */
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        this.navDrawerItems.clear();
        if (charText.length() == 0)
        {
            this.navDrawerItems.addAll(this.navDrawerItemsSearch);
        }
        else
        {
            for (Layer layer : this.navDrawerItemsSearch)
            {
                if ( layer.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    this.navDrawerItems.add(layer);
                }
            }
        }
        notifyDataSetChanged();
    }

}

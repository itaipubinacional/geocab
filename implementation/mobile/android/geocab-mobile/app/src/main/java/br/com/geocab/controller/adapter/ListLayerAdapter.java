package br.com.geocab.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.geocab.R;
import br.com.geocab.controller.app.AppController;
import br.com.geocab.entity.Layer;

public class ListLayerAdapter extends ArrayAdapter {

	private Context context;
	private ArrayList<Layer> layersItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ListLayerAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.layersItems = new ArrayList<Layer>();
    }

    @Override
	public int getCount() {
		return layersItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return layersItems.get(position);
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
        return this.layersItems;
    }

    /**
     *
     * @param layers
     */
    public void setItemList(ArrayList<Layer> layers)
    {
        if( this.layersItems.size() == 0 )
        {
            this.layersItems.clear();
            this.layersItems.addAll(layers);
        }
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layer_list_item, null);
        }

        if (imageLoader == null)
        {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        NetworkImageView networkImageViewLegend = (NetworkImageView) convertView
                .findViewById(R.id.network_image_view_select_layer_legend);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.text_view_title_select_layer);

        txtTitle.setText(layersItems.get(position).getTitle());

        // thumbnail image
        networkImageViewLegend.setImageUrl(layersItems.get(position).getLegend(), imageLoader);

        return convertView;
	}

}

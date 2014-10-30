package br.com.geocab.controller.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import br.com.geocab.R;
import br.com.geocab.controller.activity.MapActivity;
import br.com.geocab.controller.adapter.ExpandableListAdapter;
import br.com.geocab.controller.delegate.MarkerDelegate;
import br.com.geocab.entity.GroupEntity;
import br.com.geocab.entity.Marker;
import br.com.geocab.entity.MarkerAttribute;

/**
 * Created by Vinicius on 21/10/2014.
 */
public class DialogInformation{

    private ExpandableListView mExpandableListView;
    private List<GroupEntity> mGroupCollection;
    private Context context;
    private Marker marker;
    private ExpandableListAdapter adapter;
    private String[] listUrls;
    private String[] listTitles;

    public DialogInformation(Context context, Marker marker, String[] listUrls, String[] listTitles) {
        this.context = context;
        this.marker = marker;
        this.mGroupCollection = new ArrayList<GroupEntity>();
        this.listUrls = listUrls;
        this.listTitles = listTitles;

        ReceiverThread r = new ReceiverThread();
        r.run();


    }

    public void childSectionView() {

        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_information);
        // Grab the window of the dialog, and change the width
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        Display display = ((Activity)this.context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        lp.width = size.x;
        lp.height = size.y;
        // This makes the dialog take up the full width
        window.setAttributes(lp);

        window.getAttributes().windowAnimations = R.style.dialog_animation;

        ImageView icon_close = (ImageView) dialog.findViewById(R.id.icon_close);

        //populateMarkerAttributes(marker);
        //prepareResource(markerAttributes);
        initPage(dialog);

        icon_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            ((MapActivity)context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    childSectionView();
                }
            });
        }
    }


    public void populateLayerProperties(final GroupEntity groupEntity)
    {
        if( groupEntity.groupItemCollection.size() > 0)
        {
            adapter.setItemList(groupEntity);

            ((MapActivity)context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }



    public void populateMarkerAttributes(Marker marker) {

        if( marker.getMarkerAttribute().size() == 0 )
        {
            GroupEntity groupEntity = new GroupEntity();

            GroupEntity.GroupItemEntity groupItemEntity = groupEntity.new GroupItemEntity();
            groupEntity.title = marker.getLayer().getName();

            groupItemEntity.title = null;
            groupItemEntity.value = null;
            groupItemEntity.image = marker.getImage();
            groupEntity.groupItemCollection.add(groupItemEntity);

            adapter.setItemList(groupEntity);
        }
        else
        {
            GroupEntity groupEntity = new GroupEntity();
            GroupEntity.GroupItemEntity groupItemEntity;

            for(MarkerAttribute markerAttribute : marker.getMarkerAttribute())
            {

                groupEntity.title = markerAttribute.getAttribute().getLayer().getName();
                groupItemEntity = groupEntity.new GroupItemEntity();
                groupItemEntity.title = markerAttribute.getAttribute().getName();
                groupItemEntity.value = markerAttribute.getValue();
                groupItemEntity.image = null;

                groupEntity.groupItemCollection.add(groupItemEntity);

            }

            groupItemEntity = groupEntity.new GroupItemEntity();
            groupItemEntity.title = null;
            groupItemEntity.value = null;
            groupItemEntity.image = marker.getImage();
            groupEntity.groupItemCollection.add(groupItemEntity);

            adapter.setItemList(groupEntity);
        }



        ((MapActivity)context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initPage(Dialog dialog) {
        mExpandableListView = (ExpandableListView) dialog.findViewById(R.id.expandableListView);
        adapter = new ExpandableListAdapter(this.context,
                mExpandableListView, mGroupCollection);

        mExpandableListView.setAdapter(adapter);
    }

    public ExpandableListAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ExpandableListAdapter adapter) {
        this.adapter = adapter;
    }
}

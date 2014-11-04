package br.com.geocab.controller.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import br.com.geocab.entity.AttributeType;
import br.com.geocab.entity.GroupEntity;
import br.com.geocab.entity.LayerType;
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
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private boolean isLayerPropertiesEmpty;
    private boolean isMarkerAttributesEmpty;

    public DialogInformation(Context context) {
        this.context = context;
        this.mGroupCollection = new ArrayList<GroupEntity>();

        ReceiverThread r = new ReceiverThread();
        r.run();
    }

    public void childSectionView() {

        dialog = new Dialog(this.context);
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

        initPage(dialog);

        icon_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adapter.setmGroupCollection(new ArrayList<GroupEntity>());
                dialog.dismiss();
            }
        });

        isLayerPropertiesEmpty = true;
        isMarkerAttributesEmpty = true;

    }

    public void showLoadMarkers( final ProgressDialog progressDialog )
    {
        ((MapActivity)context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if( !progressDialog.isShowing() )
                {
                    DialogInformation.this.progressDialog = progressDialog;
                    progressDialog.setTitle("Carregando");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Carregando atributos da camada");
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                }
            }
        });

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
            isLayerPropertiesEmpty = false;

            groupEntity.layerType = LayerType.EXTERNAL;

            if( !dialog.isShowing() )
            {
                dialog.show();
                adapter.setItemList(groupEntity);
            }
            else
            {
                adapter.setItemList(groupEntity);
            }

        }
        else
        {
            if( isMarkerAttributesEmpty )
            {
                dialog.dismiss();
            }
        }

        ((MapActivity)context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
    }



    public void populateMarkerAttributes(Marker marker) {

        if( marker.getMarkerAttributes().size() == 0 )
        {
            GroupEntity groupEntity = new GroupEntity();

            GroupEntity.GroupItemEntity groupItemEntity = groupEntity.new GroupItemEntity();
            groupEntity.title = marker.getLayer().getName();
            groupEntity.id = marker.getId();
            groupEntity.layerType = LayerType.INTERNAL;

            groupItemEntity.title = null;
            groupItemEntity.value = null;
            groupItemEntity.user = marker.getUser().getName();
            groupItemEntity.date = marker.getMarkerCreatedFormated();
            groupItemEntity.image = marker.getImage();
            groupEntity.groupItemCollection.add(groupItemEntity);

            adapter.setItemList(groupEntity);
        }
        else
        {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.id = marker.getId();
            groupEntity.layerType = LayerType.INTERNAL;
            GroupEntity.GroupItemEntity groupItemEntity;
            boolean isHaveInformation = false;

            for(MarkerAttribute markerAttribute : marker.getMarkerAttributes())
            {

                groupEntity.title = markerAttribute.getAttribute().getLayer().getName();
                groupItemEntity = groupEntity.new GroupItemEntity();
                groupItemEntity.title = markerAttribute.getAttribute().getName();
                if(markerAttribute.getAttribute().getType() == AttributeType.BOOLEAN)
                {
                    if(markerAttribute.getValue().equals("Yes"))
                    {
                        groupItemEntity.value = "Sim";
                    }
                    else
                    {
                        groupItemEntity.value = "Não";
                    }

                }
                else
                {
                    groupItemEntity.value = markerAttribute.getValue();
                }

                if (!isHaveInformation)
                {
                    groupItemEntity.user = marker.getUser().getName();
                    groupItemEntity.date = marker.getMarkerCreatedFormated();
                    marker.setUser(null);
                    marker.setMarkerCreatedFormated(null);
                    isHaveInformation = true;
                }
                groupItemEntity.image = null;

                groupEntity.groupItemCollection.add(groupItemEntity);

            }

            groupItemEntity = groupEntity.new GroupItemEntity();
            groupItemEntity.title = null;
            groupItemEntity.value = null;
            groupItemEntity.image = marker.getImage();
            groupEntity.groupItemCollection.add(groupItemEntity);

            if( !dialog.isShowing() )
            {
                dialog.show();
                adapter.setItemList(groupEntity);
            }
            else
            {
                adapter.setItemList(groupEntity);
            }

        }


        ((MapActivity)context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public List<GroupEntity> getmGroupCollection() {
        return mGroupCollection;
    }

    public void setmGroupCollection(List<GroupEntity> mGroupCollection) {
        this.mGroupCollection = mGroupCollection;
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

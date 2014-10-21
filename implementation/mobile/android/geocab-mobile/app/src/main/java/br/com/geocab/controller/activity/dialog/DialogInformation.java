package br.com.geocab.controller.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
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
import br.com.geocab.controller.adapter.ExpandableListAdapter;
import br.com.geocab.entity.GroupEntity;

/**
 * Created by Vinicius on 21/10/2014.
 */
public class DialogInformation {

    private ExpandableListView mExpandableListView;
    private List<GroupEntity> mGroupCollection;
    private Activity context;

    public DialogInformation(Activity context) {
        this.context = context;
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

        Display display = this.context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        lp.width = size.x;
        lp.height = size.y;
        // This makes the dialog take up the full width
        window.setAttributes(lp);

        window.getAttributes().windowAnimations = R.style.dialog_animation;

        ImageView icon_close = (ImageView) dialog.findViewById(R.id.icon_close);

        prepareResource();
        initPage(dialog);

        icon_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void prepareResource() {

        mGroupCollection = new ArrayList<GroupEntity>();

        for (int i = 1; i < 6; i++) {
            GroupEntity ge = new GroupEntity();
            ge.Name = "Group" + i;

//            for (int j = 1; j < 5; j++) {
//                GroupEntity.GroupItemEntity gi = ge.new GroupItemEntity();
//                gi.Name = "Child" + j;
//                ge.GroupItemCollection.add(gi);
//            }

            GroupEntity.GroupItemEntity gi = ge.new GroupItemEntity();
            gi.Name = "Child";
            ge.GroupItemCollection.add(gi);

            mGroupCollection.add(ge);
        }

    }

    private void initPage(Dialog dialog) {
        mExpandableListView = (ExpandableListView) dialog.findViewById(R.id.expandableListView);
        ExpandableListAdapter adapter = new ExpandableListAdapter(this.context,
                mExpandableListView, mGroupCollection);

        mExpandableListView.setAdapter(adapter);
    }
}

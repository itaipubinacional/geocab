package br.com.geocab.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.geocab.R;
import br.com.geocab.entity.GroupEntity;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private ExpandableListView mExpandableListView;
	private List<GroupEntity> mGroupCollection;
	private int[] groupStatus;

	public ExpandableListAdapter(Context pContext,
                                 ExpandableListView pExpandableListView,
                                 List<GroupEntity> pGroupCollection) {
		mContext = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];

		setListEvent();
	}


	private void setListEvent() {

		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

                    int previousGroup = -1;
					@Override
					public void onGroupExpand(int groupPosition) {
						// TODO Auto-generated method stub

//                        if(groupPosition != previousGroup && previousGroup != -1)
//                        {
//                            mExpandableListView.collapseGroup(previousGroup);
//                        }
//
//                        previousGroup = groupPosition;

                        groupStatus[groupPosition] = 1;

					}
				});

		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						groupStatus[arg0] = 0;
					}
				});
	}

	@Override
	public String getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).GroupItemCollection.get(arg1).Name;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub

		ChildHolder childHolder;
		if (arg3 == null) {
			arg3 = LayoutInflater.from(mContext).inflate(
					R.layout.list_information_group_item, null);

			childHolder = new ChildHolder();

			childHolder.title = (TextView) arg3.findViewById(R.id.item_title);
			arg3.setTag(childHolder);
		}else {
			childHolder = (ChildHolder) arg3.getTag();
		}

		childHolder.title.setText(mGroupCollection.get(arg0).GroupItemCollection.get(arg1).Name);
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).GroupItemCollection.size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroupCollection.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		GroupHolder groupHolder;
		if (arg2 == null) {
			arg2 = LayoutInflater.from(mContext).inflate(R.layout.list_information_group,
					null);
			groupHolder = new GroupHolder();
			groupHolder.img = (ImageView) arg2.findViewById(R.id.tag_img);
			groupHolder.title = (TextView) arg2.findViewById(R.id.group_title);
			arg2.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) arg2.getTag();
		}
		if (groupStatus[arg0] == 0) {
			groupHolder.img.setImageResource(R.drawable.icon_arrow_close);
		} else {
			groupHolder.img.setImageResource(R.drawable.icon_arrow_open);
		}
		groupHolder.title.setText(mGroupCollection.get(arg0).Name);

		return arg2;
	}

	class GroupHolder {
		ImageView img;
		TextView title;
	}

	class ChildHolder {
		TextView title;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}

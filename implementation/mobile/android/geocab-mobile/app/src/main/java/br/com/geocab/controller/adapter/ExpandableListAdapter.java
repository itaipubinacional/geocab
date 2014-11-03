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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


		setListEvent();
	}

    public void setItemList(GroupEntity groupEntity)
    {
        if( groupEntity != null )
        {
            this.mGroupCollection.add(groupEntity);
            groupStatus = new int[mGroupCollection.size()];
        }
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
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).title;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return 0;
    }

	@Override
	public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

		ChildHolder childHolder;
		if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_information_group_item, null);

			childHolder = new ChildHolder();

			childHolder.title = (TextView) convertView.findViewById(R.id.text_view_marker_attribute_title);
			childHolder.value = (TextView) convertView.findViewById(R.id.text_view_marker_attribute_value);
            childHolder.image = (ImageView) convertView.findViewById(R.id.image_view_marker_attribute);
            childHolder.user = (TextView) convertView.findViewById(R.id.text_view_user_marker);
            childHolder.date = (TextView) convertView.findViewById(R.id.text_view_date_marker);
            childHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_layout_creation_information);

            convertView.setTag(childHolder);
		}else {
			childHolder = (ChildHolder) convertView.getTag();
		}

        if( mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).user != null && mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).date != null)
        {
            childHolder.relativeLayout.setVisibility(View.VISIBLE);
            childHolder.user.setText(mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).user);
            childHolder.date.setText(mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).date);
        }
        else
        {
            childHolder.relativeLayout.setVisibility(View.GONE);
        }

        if( mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).value == null )
        {
            childHolder.title.setVisibility(View.GONE);
            childHolder.value.setVisibility(View.GONE);
        }
        else
        {
            childHolder.title.setVisibility(View.VISIBLE);
            childHolder.value.setVisibility(View.VISIBLE);
            childHolder.title.setText(mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).title);
            childHolder.value.setText(mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).value);
        }

        if( mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).image == null )
        {
            childHolder.image.setVisibility(View.GONE);
        }
        else
        {
            childHolder.image.setVisibility(View.VISIBLE);
            childHolder.image.setImageBitmap(mGroupCollection.get(groupPosition).groupItemCollection.get(childPosition).image);

        }

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(groupPosition).groupItemCollection.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroupCollection.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupHolder groupHolder;
		if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_information_group,
					null);
			groupHolder = new GroupHolder();
			groupHolder.img = (ImageView) convertView.findViewById(R.id.tag_img);
			groupHolder.title = (TextView) convertView.findViewById(R.id.group_title);
            convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

            if (groupStatus[groupPosition] == 0) {
                groupHolder.img.setImageResource(R.drawable.icon_arrow_close);
            } else {
                groupHolder.img.setImageResource(R.drawable.icon_arrow_open);
            }

		groupHolder.title.setText(mGroupCollection.get(groupPosition).title);

		return convertView;
	}

	class GroupHolder {
		ImageView img;
		TextView title;
	}

	class ChildHolder {
		TextView title;
        TextView value;
        TextView user;
        TextView date;
        ImageView image;
        RelativeLayout relativeLayout;
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

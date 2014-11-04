package br.com.geocab.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class GroupEntity {
    public long id;
	public String title;
    public LayerType layerType;
	public List<GroupItemEntity> groupItemCollection;

	public GroupEntity()
	{
		groupItemCollection = new ArrayList<GroupItemEntity>();
	}

	public class GroupItemEntity
	{
		public String title;
		public String value;
        public Bitmap image;
        public String user;
        public String date;
	}
}

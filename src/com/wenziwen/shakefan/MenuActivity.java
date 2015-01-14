package com.wenziwen.shakefan;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends Activity{
	private static final String TAG = "MenuActivity";
	private LayoutInflater mInflater;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		MenuAdapter adapter = new MenuAdapter(DBManager.getInstance().getRecordList());
		((ListView) findViewById(R.id.listView)).setAdapter(adapter);
	}
	
	public class MenuAdapter extends BaseAdapter {
		private List<Food> mListFood = new ArrayList<Food>();
		private String[] str = {"早餐", "午餐", "晚餐"};
				
		public MenuAdapter(List<Food> list) {
			mListFood.addAll(list);
		}
		@Override
		public int getCount() {
			return mListFood.size();
		}

		@Override
		public Object getItem(int position) {
			return mListFood.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null ) {
				convertView = mInflater.inflate(R.layout.menu_item_view, null); 
			} 
			
			Food food =  mListFood.get(position);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_name);
			tv.setText(food.name);
			tv = (TextView) convertView.findViewById(R.id.tv_info);
			tv.setText(str[food.type] + "  " + food.phone + "  " + food.restaurant);
			return convertView;
		}
		
	}
}
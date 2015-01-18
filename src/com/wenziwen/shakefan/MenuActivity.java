package com.wenziwen.shakefan;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity{
	private static final String TAG = "MenuActivity";
	public static final int REQUEST_EDIT = 16;
	private LayoutInflater mInflater;
	private ListView mListView;
	private MenuAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
		mListView = ((ListView) findViewById(R.id.listView));
		
		updateList();
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MenuActivity.this, EditMenuActivity.class);
				Food food = (Food) mAdapter.getItem(arg2);
				
				intent.putExtra(EditMenuActivity.FOOD_ID, food.id);
				intent.putExtra(EditMenuActivity.FOOD_NAME, food.name);
				intent.putExtra(EditMenuActivity.FOOD_TYPE, food.type);
				intent.putExtra(EditMenuActivity.FOOD_PHONE, food.phone);
				intent.putExtra(EditMenuActivity.FOOD_RESTAURANT, food.restaurant);
				// 不关心request code
				startActivityForResult(intent, REQUEST_EDIT);
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder builder = new Builder(MenuActivity.this);
				builder.setTitle("提示");
				builder.setMessage("确定删除该项?");
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (DBManager.getInstance().deleteRecord((Food) mAdapter.getItem(position))){
							toast("删除数据成功");
						} else {
							toast("删除数据失败");
						}
					}
				});
				builder.create().show();
				return true;
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 操作成功时，更新列表
		if (resultCode == RESULT_OK) {
			updateList();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, EditMenuActivity.class);
		// 不关心request code
		startActivityForResult(intent, REQUEST_EDIT);
		return true;
	}
	
	private void updateList() {
		// TODO 数据过多时，需要使用子线程从数据库获取数据
		if (mAdapter == null) {
			mAdapter = new MenuAdapter(DBManager.getInstance().getRecordList());
		} else {
			Log.d(TAG, "update list");
			mAdapter.updateList(DBManager.getInstance().getRecordList());
		}
		mListView.setAdapter(mAdapter);		
	}

	private void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	public class MenuAdapter extends BaseAdapter {
		private List<Food> mListFood = new ArrayList<Food>();
		private String[] str = {"早餐", "午餐", "晚餐"};
				
		public MenuAdapter(List<Food> list) {
			mListFood.addAll(list);
		}
		
		public void updateList(List<Food> list) {
			mListFood.clear();
			mListFood.addAll(list);
			notifyDataSetChanged();
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

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null ) {
				convertView = mInflater.inflate(R.layout.menu_item_view, null); 
			} 
			
			Food food =  mListFood.get(position);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_name);
			tv.setText(food.name);
			tv = (TextView) convertView.findViewById(R.id.tv_info);
			tv.setText(str[food.type] + "  " + food.restaurant + "  " + food.phone);
			return convertView;
		}
		
	}
}
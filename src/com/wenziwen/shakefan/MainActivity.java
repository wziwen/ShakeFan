package com.wenziwen.shakefan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "MainActivity";
	private static final String SHARED_PREF = "ShakeFun";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 设置click事件
		findViewById(R.id.btn_breakfast).setOnClickListener(this);
		findViewById(R.id.btn_lunch).setOnClickListener(this);
		findViewById(R.id.btn_dinner).setOnClickListener(this);
		
		initDataBase();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ShakeActivity.class);
		// 根据选择摇一摇
		switch (v.getId()) {
		case R.id.btn_breakfast:
			intent.putExtra(Bean.TYPE, Bean.BREAKFAST);
			break;

		case R.id.btn_lunch:
			intent.putExtra(Bean.TYPE, Bean.LUNCH);
			break;

		case R.id.btn_dinner:
			intent.putExtra(Bean.TYPE, Bean.DINER);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("菜单");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
		
		return super.onOptionsItemSelected(item);
	}
	
	private void initDataBase() {
		DBManager.createInstance(this);
		SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
		boolean isDefaultDataAdded = pref.getBoolean("is_default_data_added", false);
		if (!isDefaultDataAdded) {
			pref.edit().putBoolean("is_default_data_added", true);
			
			Food food = new Food(0, "卤肉饭", "", "");
			addData(food);
			food = new Food(0, "快餐", "", "");
			addData(food);
			food = new Food(0, "鸡腿饭", "", "");
			addData(food);
			food = new Food(0, "烧鸭饭", "", "");
			addData(food);
		}
	}
	
	private void addData(Food food) {
		DBManager.getInstance().insertFtpRecord(food);
	}
}
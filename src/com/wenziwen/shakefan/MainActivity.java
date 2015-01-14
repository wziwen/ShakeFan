package com.wenziwen.shakefan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	private static final String SHARED_PREF = "ShakeFun";
	private static final String IS_INITIALIZED = "is_initialized";

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
			intent.putExtra(Bean.TYPE, Food.BREAKFAST);
			break;

		case R.id.btn_lunch:
			intent.putExtra(Bean.TYPE, Food.LUNCH);
			break;

		case R.id.btn_dinner:
			intent.putExtra(Bean.TYPE, Food.DINER);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_menu:
			intent = new Intent(this, MenuActivity.class);
			break;

			// TODO 添加设置
		/*case R.id.action_setting:
			intent = new Intent(this, SettingActivity.class);
			break;*/
			
		case R.id.action_usage:
			intent = new Intent(this, UsageActivity.class);
			break;
			
		case R.id.action_about:
			intent = new Intent(this, AboutActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
		return true;
	}
		
	private void initDataBase() {
		DBManager.createInstance(this);
		SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
		boolean isDefaultDataAdded = pref.getBoolean(IS_INITIALIZED, false);
		if (!isDefaultDataAdded) {
			pref.edit().putBoolean(IS_INITIALIZED, true).commit();
			
			// 添加默认早餐
			for (int i = 0; i < BREAKFAST.length; i++) {
				Food food = new Food(Food.BREAKFAST, BREAKFAST[i], "", "");
				addData(food);
			}
			
			// 添加默认午餐
			for (int i = 0; i < LUNCH_AND_DINER.length; i++) {
				Food food = new Food(Food.LUNCH, LUNCH_AND_DINER[i], "", "");
				addData(food);
			}
			
			// 添加默认晚餐
			for (int i = 0; i < LUNCH_AND_DINER.length; i++) {
				Food food = new Food(Food.DINER, LUNCH_AND_DINER[i], "", "");
				addData(food);
			}
		}
	}
	
	private void addData(Food food) {
		DBManager.getInstance().insertRecord(food);
	}
	
	private String[] BREAKFAST = {"鸡蛋", "牛奶", "豆浆", "面包", "小笼包", "纸包鸡", "糯米鸡"};
	/**
	 * 早餐和晚餐菜单一样
	 */
	private String[] LUNCH_AND_DINER = {"叉烧饭", "白切鸡", "烧鸭", "双拼", "手撕鸡", "烧鸭腿", "凉瓜炒蛋", 
			"日本豆腐", "凉瓜排骨", "五花肉", "青瓜肉片", "支竹焖花肉", "蛋炒饭", "炖汤"};
}
package com.wenziwen.shakefan;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBManager extends LxDBManager{
	
	private static DBManager mInstance= null;	
	private static final String TB_FOOD = "tb_food";
	
	private DBManager(Context context) {
		super(context); 		
	}
	
	public static void createInstance(Context context) {
		mInstance = new DBManager(context);		
	}
	
	public static DBManager getInstance() {
		return mInstance;
	}
	
	/**
	 * 插入数据
	 * @param info  
	 * @return 成功返回id，失败返回-1
	 */
	public long insertRecord(Food info) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("type", info.type);
		contentValues.put("name", info.name);
		contentValues.put("phone", info.phone);
		contentValues.put("restaurant", info.restaurant);
		
		return insert(TB_FOOD, contentValues);
	}
		
	public boolean updateRecord(Food info) {
		ContentValues contentValues = new ContentValues();		
		contentValues.put("id", info.id);
		contentValues.put("type", info.type);
		contentValues.put("name", info.name);
		contentValues.put("phone", info.phone);
		contentValues.put("restaurant", info.restaurant);
		
		String args[] = {Long.toString(info.id)};
		return update(TB_FOOD, contentValues, "id = ?", args);
	}
	
	public List<Food> getRecordList() {
		List<Food> ftpRecordInfoList = new ArrayList<Food>();
		
		String sql = String.format("SELECT * from " + TB_FOOD);
		Cursor cursor = queryAllBySql(sql);
		Food food = null;
		try {			
			if(cursor.getCount() > 0){
				while(cursor.moveToNext()){
					food = new Food();
					food.id = cursor.getInt(0);
					food.type = cursor.getInt(1);
					food.name = cursor.getString(2);
					food.phone = cursor.getString(3);
					food.restaurant = cursor.getString(4);
					
					ftpRecordInfoList.add(0, food);
				}				
			}
		} finally {
			cursor.close();			
		}
		return ftpRecordInfoList;
	}
	
	public List<Food> getRecordList(int type) {
		List<Food> ftpRecordInfoList = new ArrayList<Food>();
		
		String sql = String.format("SELECT * from " + TB_FOOD + " where type=" + Integer.toString(type));
		Cursor cursor = queryAllBySql(sql);
		Food food = null;
		try {			
			if(cursor.getCount() > 0){
				while(cursor.moveToNext()){
					food = new Food();
					food.id = cursor.getInt(0);
					food.type = cursor.getInt(1);
					food.name = cursor.getString(2);
					food.phone = cursor.getString(3);
					food.restaurant = cursor.getString(4);
					
					ftpRecordInfoList.add(0, food);
				}				
			}
		} finally {
			cursor.close();			
		}
		return ftpRecordInfoList;
	}
	
	/**
	 * 删除所有记录
	 * @return 成功返回true  失败返回false
	 */
	public boolean deleteRecordAll() {
		return deleteAll(TB_FOOD);
	}
	
	public boolean deleteRecord(Food info) {
		String[] args = new String[1];
		args[0] = Long.toString(info.id);
		return delete(TB_FOOD, "id = ?", args);
	}

	@Override
	public void onCreateTable() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TB_FOOD + " " +
				"(id integer primary key autoincrement, " +	               
                "type integer, " + 
                "name varchar, " +
                "phone varchar, " +
                "restaurant varchar)";
		createTable(sql);
	}
	
	private class FoodBean {
		// TODO 数据包的列类型统一使用FoodBean
		static final String id = "id";
		static final String type = "type";
		static final String name = "name";
		static final String phone = "phone";
		static final String restaurant = "restaurant";
		
	}
}

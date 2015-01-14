package com.wenziwen.shakefan;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;

/**
 * 数据库相关操作类
 */
public abstract class LxDBManager extends SQLiteOpenHelper {

	/** 数据库的名字 */
	private static final String DATABASE_NAME = "lx.db";

	/** 数据库的版本 */
	private static final int DBTABASE_VERSION = 1;

	/** 上下文 */
	private Context mContext;
	private SQLiteDatabase db = null;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            context对象
	 */
	public LxDBManager(Context context) {
		super(context, DATABASE_NAME, null, DBTABASE_VERSION);
		mContext = context;		
		db = getWritableDatabase();
	}	
	
	/**
	 * 重写onCreate方法，建立数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {	
		this.db = db;
		onCreateTable();
	}

	/**
	 * 重写onUpgrade方法，根据版本号不同（DBTABASE_VERSION）更新数据库
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 数据库创建时并没有创建表，子类需要实现本函数，在创建的数据库的同时创建对应的数据表。
	 */
	public abstract void onCreateTable();

	/**
	 * 关闭数据库连接 数据库不再使用时应该调用close函数
	 */
	@Override
	public void close() {
		super.close();
	}

	/**
	 * 查询所有记录
	 * 
	 * @param table
	 *            ：表名称
	 * @return cursor 没有则返回 null
	 */
	public Cursor queryAll(String table) {
		if (db.isOpen()) {
			return db.query(table, null, null, null, null, null, null);
		}
		return null;
	}

	/**
	 * 查询所有记录
	 * 
	 * @param sql
	 *            传入sql语句直接查询
	 * @return 数据指针
	 */
	public Cursor queryAllBySql(String sql) {
		if (db.isOpen()) {
			return db.rawQuery(sql, null);
		}
		return null;
	}

	/**
	 * 按条件查询记录
	 * 
	 * @param table
	 *            ：表名称
	 * @param colums
	 *            ：列名称数组
	 * @param selection
	 *            ：条件子句，相当于where
	 * @param selectionArgs
	 *            ：条件语句的参数数组
	 * @param groupBy
	 *            ：分组
	 * @param having
	 *            ：分组条件
	 * @param orderBy
	 *            ：排序类
	 * @param limit
	 *            ：分页查询的限制
	 * @return Cursor：返回值，相当于结果集ResultSet
	 */
	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		if (db.isOpen()) {
			Cursor mCursor = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
			if (mCursor != null)
				mCursor.moveToPrevious(); // 如果指针存在，就把指针移到第一个条目上
			return mCursor;
		}
		return null;
	}

	/**
	 * 按条件查询记录
	 * 
	 * @param sql
	 *            传入sql语句直接查询
	 * @return 单个数据指针
	 */
	public Cursor queryBySql(String sql) {
		if (db.isOpen()) {
			Cursor mCursor = db.rawQuery(sql, null);
			// 如果指针存在，就把指针移到第一个条目上
			if (mCursor != null)
				mCursor.moveToPrevious();
			return mCursor;
		}
		return null;
	}

	/**
	 * 更新数据
	 * 
	 * @param table
	 *            表名
	 * @param contentValues
	 *            要更新的数据
	 * @param whereClause
	 *            条件
	 * @param whereArgs
	 *            条件参数
	 * @return 是否更新成功
	 */
	public boolean update(String table, ContentValues contentValues,
			String whereClause, String[] whereArgs) {
		if (db.isOpen()) {
			db.beginTransaction();
			boolean ret = db.update(table, contentValues, whereClause,
					whereArgs) > 0;
			db.setTransactionSuccessful();
			db.endTransaction();
			return ret;
		}
		return false;
	}

	/**
	 * 插入新数据
	 * 
	 * @param tableName
	 *            表名
	 * @param contentValues
	 *            数据
	 * @return 成功返回id，失败返回-1
	 */
	public long insert(String tableName, ContentValues contentValues) {
		if (db.isOpen()) {
			db.beginTransaction();
			long ret = db.insert(tableName, null, contentValues);
			db.setTransactionSuccessful();
			db.endTransaction();
			return ret;
		}
		return -1;
	}

	/**
	 * 按条件删除数据
	 * 
	 * @param table
	 *            表名
	 * @param whereClause
	 *            条件
	 * @param whereArgs
	 *            条件参数
	 * @return 是否删除成功
	 */
	public boolean delete(String table, String whereClause, String[] whereArgs) {
		if (db.isOpen()) {
			db.beginTransaction();
			boolean ret = db.delete(table, whereClause, whereArgs) > 0;
			db.setTransactionSuccessful();
			db.endTransaction();
			return ret;
		}
		return false;
	}

	/**
	 * 删除所有数据
	 * 
	 * @param table
	 *            表名
	 * @return 是否删除成功
	 */
	public boolean deleteAll(String table) {
		if (db.isOpen()) {
			return db.delete(table, null, null) > 0;
		}
		return false;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 *            sql语句
	 */
	public void executeSql(String sql) {
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建表
	 * 
	 * @param tableName
	 * @param sql
	 *            建表语句
	 * @return
	 */
	public void createTable(String sql) {
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

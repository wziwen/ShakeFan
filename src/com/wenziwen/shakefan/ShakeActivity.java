package com.wenziwen.shakefan;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

public class ShakeActivity extends Activity {
	private SensorManager mSensorManager;
	private Vibrator mVibrator;
	private static final String TAG = "ShakeActivity";
	private static final int SENSOR_SHAKE = 10;
	private TextView mTextView;
	private int mType = 0;
	private String[] TYPES = {"早餐", "午餐", "晚餐"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		
		mType = getIntent().getExtras().getInt(Bean.TYPE, 0);
		
		mTextView = (TextView) findViewById(R.id.tv_shakeResult);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		reisterListener();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterListener();
	}

	private void reisterListener() {
		// 注册监听器
		if (mSensorManager != null) {
			mSensorManager.registerListener(sensorEventListener,
					mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					mSensorManager.SENSOR_DELAY_NORMAL);
		}
	}
	
	private void unregisterListener() {
		// 取消监听器
		if (mSensorManager != null) {
			mSensorManager.unregisterListener(sensorEventListener);
		}
	}
	
	/**
	 * 重力感应监听
	 */
	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			// 传感器信息改变时执行该方法
			float[] values = event.values;
			float x = values[0]; 
			float y = values[1]; 
			float z = values[2]; 
			// 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
			int medumValue = 19; // 三星 i9250怎么晃都不会超过20，没办法，只设置19了
			if (Math.abs(x) > medumValue || Math.abs(y) > medumValue
					|| Math.abs(z) > medumValue) {
				// 只能摇一次
				unregisterListener();
				// TODO 添加震动和声音
				mVibrator.vibrate(200);
				
				Message msg = new Message();
				msg.what = SENSOR_SHAKE;
				mHandler.sendMessage(msg);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENSOR_SHAKE:
				Random random = new Random(System.currentTimeMillis());
				List<Food> list = DBManager.getInstance().getRecordList(mType);
				int id = random.nextInt(list.size());
				Food food = list.get(id); 
				
				// eg: 你挑到的  早餐 是：楼下 的 纸包鸡， 电话是 1590000000.
				String info = "您挑到的\t" + TYPES[mType] + "\t是:\t";
				if (!food.restaurant.equals("")) {
					info += food.restaurant + "\t的";
				}
				
				info += food.name;
				
				if (!food.phone.equals("")) {
					info += ",电话是\t" + food.phone;
				}
				
				info += ".";
				mTextView.setText(info);
				Log.d(TAG, "random value: " + id);
				break;
			}
		}
	};
}
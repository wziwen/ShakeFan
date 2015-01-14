package com.wenziwen.shakefan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditMenuActivity extends Activity{
	private static final String TAG = "EditMenuActivity";
	public static final String FOOD_ID = "food_id";
	public static final String FOOD_NAME = "food_name";
	public static final String FOOD_TYPE = "food_type";
	public static final String FOOD_PHONE = "food_phone";
	public static final String FOOD_RESTAURANT = "food_restaurant";
	
	private TextView mTvName;
	private TextView mTvPhone;
	private TextView mTvRestaurant;
	private RadioButton[] mRadioButtons = new RadioButton[3];
	
	private boolean mIsEdit = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_menu);
		
		mTvName = (TextView) findViewById(R.id.et_name);
		mTvPhone = (TextView) findViewById(R.id.et_phone);
		mTvRestaurant = (TextView) findViewById(R.id.et_restaurant);
		
		mRadioButtons[0] = (RadioButton) findViewById(R.id.radioButton1);
		mRadioButtons[1] = (RadioButton) findViewById(R.id.radioButton2);
		mRadioButtons[2] = (RadioButton) findViewById(R.id.radioButton3);
		
		findViewById(R.id.btn_confirm).setOnClickListener(mButtonClick);
		
		Bundle data = getIntent().getExtras();
		if (data != null) {
			mTvName.setText(data.getString(FOOD_NAME));
			mTvPhone.setText(data.getString(FOOD_PHONE));
			mTvRestaurant.setText(data.getString(FOOD_RESTAURANT));
			mRadioButtons[data.getInt(FOOD_TYPE)].setChecked(true);
			
			mIsEdit = true;
		}
	}
	
	private OnClickListener mButtonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			boolean result = false;
			Food food = new Food();		
			food.name = mTvName.getText().toString();
			if(food.name.trim().equals("")) {
				toast("名字不能为空");
				return;
			}
			food.phone = mTvPhone.getText().toString();
			food.restaurant = mTvRestaurant.getText().toString();
			for(int i = 0; i < mRadioButtons.length; i++) {
				RadioButton rb = mRadioButtons[i];
				if (rb.isChecked()) {
					food.type = i;
					break;
				}
			}
			
			if (mIsEdit) {			
				food.id = getIntent().getExtras().getLong(FOOD_ID);
				if (DBManager.getInstance().updateRecord(food)){
					toast("更新数据成功");
					result = true;
				} else {
					toast("更新数据失败");
				}
			} else {				
				if (DBManager.getInstance().insertRecord(food) != -1){
					toast("插入数据成功");
					result = true;
				} else {
					toast("插入数据失败");
				}
			}
			// 成功返回
			if (result) {
				setResult(RESULT_OK);				
			} else {
				setResult(RESULT_CANCELED);
			}
			finish();
		}
	};
	
	private void toast(String msg) {
		Toast.makeText(EditMenuActivity.this, msg, Toast.LENGTH_SHORT).show();
	}
}
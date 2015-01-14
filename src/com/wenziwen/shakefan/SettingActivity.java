package com.wenziwen.shakefan;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingActivity extends PreferenceActivity{	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}	
	
	@Override
	public void onBuildHeaders(List<Header> target) {
		// TODO Auto-generated method stub
		super.onBuildHeaders(target);
	}
}
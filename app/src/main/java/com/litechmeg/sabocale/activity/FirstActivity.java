package com.litechmeg.sabocale.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager.OnCancelListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.litechmeg.sabocale.R;
import com.litechmeg.sabocale.model.Attendance;
import com.litechmeg.sabocale.model.Kamoku;
import com.litechmeg.sabocale.model.Subject;
import com.litechmeg.sabocale.model.Term;

public class FirstActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		SharedPreferences preference = getSharedPreferences("しぇあぷり", MODE_PRIVATE);
		Editor firstBoot = preference.edit();
		 if (preference.getBoolean("あ", false) == false) {
			long startTime;
			startTime=System.currentTimeMillis();
		 } else {
			 Intent intent = new Intent(FirstActivity.this,
					 StartActivity.class);
					 startActivity(intent);

		
		 }

	}


	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first, menu);
		return true;
	}

}

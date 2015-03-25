package com.litechmeg.sabocale.util;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

public class SaboCaleApplication extends Application {

	@Override
	public void onCreate() {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate();
		ActiveAndroid.initialize(this);
	}

	@Override
	public void onTerminate() {
		// TODO 自動生成されたメソッド・スタブ
		super.onTerminate();
		ActiveAndroid.dispose();
	}

}

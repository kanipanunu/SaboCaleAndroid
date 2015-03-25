package com.litechmeg.sabocale.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Twitterに投稿するための機能を持ったクラス
 */
public class Twitter {
	static String getHashTag() {
		return " %23sabocale";
	}
	
	public static void tweet(Activity act, String x) {
		String url = "http://twitter.com/share?text=" + x + getHashTag();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		act.startActivity(intent);
	}
}

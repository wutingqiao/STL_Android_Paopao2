package com.example.common.util;

import org.apache.http.message.BasicNameValuePair;




import com.example.stl_android_paopao.R;

import android.app.Activity;
import android.content.Intent;

public class IntentUtil {
	public static void start_activity(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		for (int i = 0; i < name.length; i++) {
			intent.putExtra(name[i].getName(), name[i].getValue());
		}
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}
}

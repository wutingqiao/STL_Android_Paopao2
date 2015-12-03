package com.example.ui.users;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import com.example.common.util.IntentUtil;
import com.example.common.util.PreferencesUtils;
import com.example.config.URLs;
import com.example.entity.Message;
import com.example.entity.Users;
import com.example.stl_android_paopao.MainActivity;
import com.example.stl_android_paopao.R;
import com.example.ui.BaseActivity;
import com.example.ui.LoadingDialog;
import com.example.ui.job.ParttimeList;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {
	@ViewInject(R.id.register_username)
	EditText register_username;
	@ViewInject(R.id.register_password)
	EditText register_password;
	@ViewInject(R.id.register_confirm_password)
	EditText register_confirm_password;
	@ViewInject(R.id.register_invite_code)
	EditText register_invite_code;
	private LoadingDialog dialog;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userregist_layout);
		ViewUtils.inject(this);
		 dialog = new LoadingDialog(this);
	}
	@OnClick(R.id.register_submit)
	public void register_submit(View v){
		String username=register_username.getText().toString().trim();
		String userpwd=register_password.getText().toString().trim();
		String confirm_password=register_confirm_password.getText().toString().trim();
		String invite_code=register_invite_code.getText().toString().trim();
		
		if (TextUtils.isEmpty(username)) {
			showShortToast(3, "用户名必须填");
			return;
		}
		if (TextUtils.isEmpty(userpwd)) {
			showShortToast(3, "密码必须填写");
			return;
		}
		if (TextUtils.isEmpty(confirm_password)) {
			showShortToast(3, "请再次输入密码");
			return;
		}
		if (!confirm_password.equals(userpwd)) {
			showShortToast(3, "两次密码输入不一致");
			return;
		}
		//组装服务器需要的参数
		RequestParams params = new RequestParams();
		params.addBodyParameter("usersName", username);
		params.addBodyParameter("usersPwd",userpwd);
		params.addBodyParameter("usersInvalitCode",invite_code);
		params.addBodyParameter("usersIsForgot","0");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sd.format(new Date());
		params.addBodyParameter("usersRegDate",date);
		//异步提交至服务器端
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, URLs.REGISTER, params, new RequestCallBack<String>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				dialog.show();
			}
			
			
			@Override
			public void onFailure(HttpException error, String msg) {
				if (dialog!=null&&dialog.isShowing()) {
					dialog.dismiss();
				}
				showLongToast(3, msg);
				Log.e("---test--", error.getExceptionCode() + msg);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (dialog!=null&&dialog.isShowing()) {
					dialog.dismiss();
				}
				ObjectMapper mapper=new ObjectMapper();
				Message message = null;
				try {
					message=mapper.readValue(responseInfo.result, Message.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.e("---tt----",responseInfo.result);
				int resultID=message.res;
				if (resultID>0) {
					showLongToast(2,message.msg);
					try {
						Users users = mapper.readValue(message.data,
								Users.class);
						PreferencesUtils.putInt(RegisterActivity.this,
								"usersID", users.usersID);
						PreferencesUtils.putString(RegisterActivity.this,
								"usersName", users.usersName);
						IntentUtil.start_activity(RegisterActivity.this,
								ParttimeList.class);
					} catch (IOException e) {
						e.printStackTrace();
					}				
				}
				else {
					showLongToast(3, message.msg);
				}
			}
		});
	}
	
	@OnClick(R.id.back)
	private void Back(View v){
		IntentUtil.start_activity(this, MainActivity.class);
	}
}

package com.example.ui.users;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class LoginActivity extends BaseActivity {

	//2-注入UI组件
	@ViewInject(R.id.login_username)
	EditText login_username;
	
	@ViewInject(R.id.login_password)
	EditText login_userpwd;
	
	
	private LoadingDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);//1-注入View和相关事件
		dialog = new LoadingDialog(this);
	}
	
	@OnClick(R.id.login_submit)
	public void Submit(View v){
		
		String username = login_username.getText().toString().trim();
		String userpwd = login_userpwd.getText().toString().trim();
		
		if (TextUtils.isEmpty(username)) {
			showShortToast(1, "用户名必须填写");
			return;
		}

		if (TextUtils.isEmpty(userpwd)) {
			showShortToast(1, "密码必须填写");
			return;
		}
	RequestParams params = new RequestParams();
	params.addBodyParameter("usersName",username);
	params.addBodyParameter("usersPwd",userpwd);
	
	HttpUtils http = new HttpUtils();	
		//send参数
		//1-GET、pOST方法
		//2-url:服务器的处理网址
		//3-上传的数据参数
		//4-回调
		http.send(HttpRequest.HttpMethod.POST, URLs.LOGIN, params, new RequestCallBack<String>() {

			//开始传输数据时候做什么
			@Override
			public void onStart() {
				super.onStart();
			}
			
			//数据传输过程中做什么
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				dialog.show();
			}
			
			//服务器端出错(404,500)，一般引起该方法
			@Override
			public void onFailure(HttpException exception, String msg) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				showLongToast(3, msg);
			}

			//页面成功200
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				LogUtils.e("服务器传回来的结果"+responseInfo.result);
				ObjectMapper mObjectMapper = new ObjectMapper();
				Message message =null;

				try {
					 message = mObjectMapper.readValue(responseInfo.result,Message.class);
					
				} catch (IOException e) {
					e.printStackTrace();
					
				}
				if(message!= null){
					int resultID=message.res;
					if (resultID>0) {
						showLongToast(2,"登录成功");
						
						Users users;
						try {
							users = mObjectMapper.readValue(message.data, Users.class);
							PreferencesUtils.putInt(LoginActivity.this,"usersID", users.usersID);
							PreferencesUtils.putString(LoginActivity.this,"usersName",users.usersName);
							
							//跳转
							Intent intent =new Intent(LoginActivity.this,ParttimeList.class);
							startActivity(intent);
							LoginActivity.this.finish();
						} 
						 catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//保存用户登录成功的信息保存到会话
						
					}else {
						showLongToast(3, "登录失败");
					}
					
				}else {
					showLongToast(3, "未解析到对象");
				}
				
			}
		});
	}
	@OnClick(R.id.login_forget_password)
	private void login_forget_password(View v){
		IntentUtil.start_activity(LoginActivity.this, ResetPwdActivity.class);
		LoginActivity.this.finish();
	}
	
	@OnClick(R.id.login_register)
	private void login_register(View v){
		IntentUtil.start_activity(LoginActivity.this, RegisterActivity.class);
		LoginActivity.this.finish();
	}
	
	@OnClick(R.id.back)
	private void back(View v){
		IntentUtil.start_activity(LoginActivity.this, MainActivity.class);
		LoginActivity.this.finish();
	}
	
	
}

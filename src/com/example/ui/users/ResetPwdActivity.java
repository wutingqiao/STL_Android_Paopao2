package com.example.ui.users;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.common.util.IntentUtil;
import com.example.stl_android_paopao.MainActivity;
import com.example.stl_android_paopao.R;
import com.example.ui.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ResetPwdActivity extends BaseActivity {
  @ViewInject(R.id.register_username)
     EditText register_username;
  @ViewInject(R.id.register_password)
  EditText register_password;
  @ViewInject(R.id.register_confirm_password)
  EditText register_confirm_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.register_submit)
	private void Submit(View v){
		
	}
	
	@OnClick(R.id.back)
	private void Back(View v){
		IntentUtil.start_activity(ResetPwdActivity.this, MainActivity.class);
		 ResetPwdActivity.this.finish();
	}
}

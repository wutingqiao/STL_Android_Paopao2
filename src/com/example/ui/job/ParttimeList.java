package com.example.ui.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.R.integer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.common.util.IntentUtil;
import com.example.config.URLs;
import com.example.entity.Joblist;
import com.example.entity.Message;
import com.example.stl_android_paopao.MainActivity;
import com.example.stl_android_paopao.R;
import com.example.ui.BaseActivity;
import com.example.ui.LoadingDialog;
import com.leelistview.view.LeeListView;
import com.leelistview.view.LeeListView.IXListViewListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ParttimeList extends BaseActivity implements IXListViewListener {
   @ViewInject(R.id.my_listview)
   LeeListView mListView ;
   private LoadingDialog dialog;
   //数据源
   private List<Joblist> mDatas=new ArrayList<Joblist>();
   //适配器
   private PartTimeJobAdapter mAdapter;
   //设置ListView属性
   private int start=0;
   private int currentPage=0;
   private static String refreshTime="";
   private static String refreshCount="0";
   private Handler mHandler;
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobinvitation_layout);
		ViewUtils.inject(this);
		dialog=new LoadingDialog(this);
		LoadData();
		//实例化适配器
		mAdapter = new PartTimeJobAdapter(this, mDatas);
		
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(true, "为您推荐了"+refreshCount+"份工作");
		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Joblist item=(Joblist)mAdapter.getItem(position-1);
				BasicNameValuePair pair = new BasicNameValuePair("jobid", item.jobID+"");
				
				IntentUtil.start_activity(ParttimeList.this,PartTimeJobView.class , pair);
				
			}
		});
		mHandler = new Handler();
	}
   /**
    * 获取数据源，从服务器端查找
    */
   private void LoadData(){
	   HttpUtils http = new HttpUtils();
	   http.send(HttpMethod.GET,URLs.JOBLIST, new RequestCallBack<String>() {

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			// TODO Auto-generated method stub
			super.onLoading(total, current, isUploading);
			dialog.show();
	
		}
		   @Override
		public void onFailure(HttpException exception, String msg) {
			if (dialog!=null&&dialog.isShowing()) {
				dialog.dismiss();
			}
			showLongToast(3, "出错："+msg);
		}

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			if (dialog!=null&&dialog.isShowing()) {
				dialog.dismiss();
			}
			ObjectMapper mapper = new ObjectMapper();
			Message message =null;
			try {
				message=mapper.readValue(responseInfo.result, Message.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (message!=null) {
				try {
					List<Joblist> lists=mapper.readValue(message.data, new TypeReference<List<Joblist>>(){});
					
					mDatas=lists;
					refreshTime = new Date().toString();
					refreshCount = lists.size()+"";
					mAdapter.AddDatas(mDatas);
					mAdapter.notifyDataSetChanged();
					mListView.stopRefresh();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}else {
				showLongToast(3, "未解析到对象");
			}
		} 
			
		
	});
   }
   //上拉刷新
  @Override
  public void onRefresh(){
	  mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				LoadData();
				mAdapter.AddDatas(mDatas);
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
  }
  @Override
  public void onLoadMore(){
	  mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				LoadData();
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
  }
  @OnClick(R.id.back)
  private void Back(View v){
	  IntentUtil.start_activity(ParttimeList.this, MainActivity.class);
	  this.finish();
  }
  private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(refreshTime);
	}
}

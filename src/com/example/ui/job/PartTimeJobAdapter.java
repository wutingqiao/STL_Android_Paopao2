/**
 * 
 */
package com.example.ui.job;

import java.util.List;

import com.example.entity.Joblist;
import com.example.stl_android_paopao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class PartTimeJobAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Joblist> mDatas;
  public PartTimeJobAdapter(Context context,List<Joblist> datas ){
	  if (datas==null) {
		return;
	}
	  this.mDatas=datas;
	  mInflater =LayoutInflater.from(context);
  }
  public void AddDatas(List<Joblist> datas ){
	  if (datas ==null) {
		return;
	}
	  this.mDatas.addAll(datas);
  }
	
	@Override
	public int getCount() {

		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.adapter_job_item2, null);
			holder=new ViewHolder();
			holder.theme_content=(TextView)convertView.findViewById(R.id.theme_content);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			holder.distance=(TextView)convertView.findViewById(R.id.distance);
			holder. wage=(TextView)convertView.findViewById(R.id. wage);
			holder.companyName=(TextView)convertView.findViewById(R.id.companyName);
			
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		Joblist job = mDatas.get(position);
		holder.theme_content.setText(job.jobTitle);
		holder.time.setText(job.getJobPostDate());
		holder.distance.setText(job.getJobPostAddress());
		holder.wage.setText(job.jobPayFee);
		holder.companyName.setText(job.getJobPostCompany());
		return convertView;
	}
	private class ViewHolder{
		TextView theme_content;
		TextView time;
		TextView distance;
		TextView wage;
		TextView companyName;
	}

}

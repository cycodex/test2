package com.sunix.game.paperplane;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PassTimeAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<PassTimeBean> mPassTimeData;
	
	public PassTimeAdapter(Context context, ArrayList<PassTimeBean> data) {
		mContext = context;
		mPassTimeData = data;
		Log.d("zxw","size:"+mPassTimeData.size());
		mInflater =	LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPassTimeData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Log.d("zxw", "getView1:"+pos+"/"+(mPassTimeData != null));
		if (mPassTimeData != null && pos < mPassTimeData.size()) {
			PassTimeBean bean = mPassTimeData.get(pos);
			Log.d("zxw", "getView:"+pos+"/"+bean.passLevel);
			RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.pass_item, null);
			TextView pass = (TextView) view.findViewById(R.id.pass);
			pass.setText("PASS "+bean.passLevel);
			TextView yourtime = (TextView) view.findViewById(R.id.your_text);
			String yourtimeString = "--";
			if (bean.yourTime > 0) {
				yourtimeString = GameControler.getInstance(mContext).format(bean.yourTime);
			}
			yourtime.setText(yourtimeString);
			TextView besttime = (TextView) view.findViewById(R.id.best_text);
			String besttimeString = "--";
			if (bean.bestTime > 0) {
				besttimeString = GameControler.getInstance(mContext).format(bean.bestTime);
			}
			besttime.setText(besttimeString);
			convertView = view;
		}
		return convertView;
	}

}

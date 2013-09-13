package com.werpt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView=inflater.inflate(R.layout.view1,container, false);
		TextView tv=(TextView) convertView.findViewById(R.id.tv);
		tv.setText("myfragment");
		return convertView;
	}
	
}

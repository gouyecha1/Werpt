package com.werpt.fragmentchild;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.werpt.R;

public class Fragment1 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView=inflater.inflate(R.layout.view1,container, false);
		TextView tv=(TextView) convertView.findViewById(R.id.tv);
		tv.setText("fragment1");
		return convertView;
	}
	
}

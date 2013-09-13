package com.werpt.fragmentparent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.werpt.MyFragment;
import com.werpt.R;

public  class FragmentParent extends Fragment {
	public static final FragmentParent newInstance(int position) {
		FragmentParent f = new FragmentParent();
		Bundle args = new Bundle();
		args.putInt("position", position);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.viewpager_fragments,
				container, false);
		ViewPager pager = (ViewPager) convertView.findViewById(R.id.pager);
		PagerTabStrip tabStrip=(PagerTabStrip) convertView.findViewById(R.id.tabstrip);
		tabStrip.setTabIndicatorColor(Color.MAGENTA);
//		tabStrip.setBackgroundColor(0xffbb00bb);
		
		final int parent_position = getArguments().getInt("position");
		pager.setAdapter(new FragmentStatePagerAdapter(
				getChildFragmentManager()) {

			@Override
			public Fragment getItem(final int position) {
				Fragment myFragment=new MyFragment();
				return myFragment;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				 return "Page " + parent_position + " - " + position;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 3;
			}

		});
		return convertView;
	}

}

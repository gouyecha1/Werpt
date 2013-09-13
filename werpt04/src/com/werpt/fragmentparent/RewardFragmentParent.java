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

public  class RewardFragmentParent extends Fragment {
	String[] title=new String[]{
		"最新悬赏","悬赏圈子"	,"更多"
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.viewpager_fragments,
				container, false);
		ViewPager pager = (ViewPager) convertView.findViewById(R.id.pager);
		PagerTabStrip tabStrip=(PagerTabStrip) convertView.findViewById(R.id.tabstrip);
		tabStrip.setTabIndicatorColor(Color.MAGENTA);
//		tabStrip.setBackgroundColor(0xffbb00bb);
		
		pager.setAdapter(new FragmentStatePagerAdapter(
				getChildFragmentManager()) {

			@Override
			public Fragment getItem(final int position) {
//				Fragment myFragment=new MyFragment();
				Fragment fragment=null;
				switch (position) {
				case 0:
					fragment=new MyFragment();
					break;
				case 1:
					fragment=new MyFragment();
				case 2:
					fragment=new MyFragment();
					break;
				default:
					break;
				}
				return fragment;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				 return title[position];
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return title.length;
			}

		});
		return convertView;
	}

}

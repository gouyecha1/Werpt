package com.werpt.fragmentparent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.werpt.MyFragment;
import com.werpt.R;
import com.werpt.fragmentchild.Fragment1;
import com.werpt.fragmentchild.NewReportFragment;

public  class ReportFragmentParent extends Fragment {
	String[] title=new String[]{
		"编辑推荐","最新微记","更多"
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.viewpager_fragments,
				container, false);
		ViewPager pager = (ViewPager) convertView.findViewById(R.id.pager);
		PagerTabStrip tabStrip=(PagerTabStrip) convertView.findViewById(R.id.tabstrip);
		tabStrip.setTabIndicatorColor(Color.RED);
		
//		tabStrip.setBackgroundColor(0xffbb00bb);
		
		pager.setAdapter(new FragmentPagerAdapter(
				getChildFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				Fragment fragment=null;
				switch (position) {
				case 0:
					fragment=new Fragment1();
					break;
				case 1:
					fragment=new NewReportFragment();
					break;
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
		pager.setCurrentItem(1);
		return convertView;
	}
	

}

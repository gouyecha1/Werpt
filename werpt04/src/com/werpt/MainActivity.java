package com.werpt;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.ActionBarSherlock.OnCreateOptionsMenuListener;
import com.actionbarsherlock.ActionBarSherlock.OnOptionsItemSelectedListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.werpt.fragmentparent.ReportFragmentParent;
import com.werpt.fragmentparent.RewardFragmentParent;
import com.werpt.fragmentparent.WelfareFragmentParent;

public class MainActivity extends SlidingFragmentActivity implements
		OnCreateOptionsMenuListener, OnOptionsItemSelectedListener {
	private long exitTime = 0;
	private ActionBar mActionBar;
	private SlidingMenu mSlidingMenu;
	private ActionBarSherlock mSherlock = ActionBarSherlock.wrap(this);
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initActionBar();
		initSlidingMenu();
	}

	public void initActionBar() {
		// forceShowActionBarOverflowMenu();
		mActionBar = mSherlock.getActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		SpinnerAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.items, android.R.layout.simple_dropdown_item_1line);
		mActionBar.setListNavigationCallbacks(adapter, new DropDownListenser());
	}

	public void initSlidingMenu() {
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		// mSlidingMenu.setMode(SlidingMenu.LEFT);
		setBehindContentView(R.layout.left_view);
		// mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setSecondaryMenu(R.layout.right_view);
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
	}

	public void forceShowActionBarOverflowMenu() {
		ViewConfiguration config = ViewConfiguration.get(this);
		try {
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		return mSherlock.dispatchCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		return mSherlock.dispatchOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SearchView searchView = new SearchView(mSherlock.getActionBar()
				.getThemedContext());
		searchView.setQueryHint("search...");

		menu.add("search")
				.setActionView(searchView)
				.setIcon(R.drawable.action_search)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add("login")
		.setTitle("注销|登录")
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("check_refresh")
		.setTitle("检查更新")
		.setIcon(R.drawable.refresh)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("settings")
		.setTitle("设置")
		.setIcon(R.drawable.action_settings)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("feedback")
		.setTitle("用户反馈")
		.setIcon(R.drawable.feedback)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("help")
		.setTitle("帮助")
		.setIcon(R.drawable.help)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("about")
		.setTitle("关于")
		.setIcon(R.drawable.about)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mSlidingMenu.toggle();
			break;

		default:
			break;
		}
		return true;
	}

	class DropDownListenser implements OnNavigationListener {
		// 得到和SpinnerAdapter里一致的字符数组
		String[] listNames = getResources().getStringArray(R.array.items);

		/* 当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment */
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			// FragmentParent parent = FragmentParent.newInstance(itemPosition);
			Fragment parent = null;
			switch (itemPosition) {
			case 0:
				parent = new ReportFragmentParent();
				break;
			case 1:
				parent = new RewardFragmentParent();
				break;
			case 2:
				parent = new WelfareFragmentParent();
			default:
				break;
			}
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			// 将Activity中的内容替换成对应选择的Fragment
			transaction.replace(R.id.context, parent, listNames[itemPosition]);
			transaction.commit();
			return true;
		}
	}

	/**
	 * 双击退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(mSlidingMenu.isMenuShowing()){
			toggle();
		}else{
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(MainActivity.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
		}
		}
		return false;
	}
}

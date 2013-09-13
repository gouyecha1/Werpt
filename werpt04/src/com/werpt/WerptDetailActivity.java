package com.werpt;

import android.app.Activity;
import android.os.Bundle;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;

public class WerptDetailActivity extends Activity {
	ActionBarSherlock mSherlock=ActionBarSherlock.wrap(this);
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.werpt_detail);
		mActionBar=mSherlock.getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
//		mActionBar.setDisplayUseLogoEnabled(true);
		mActionBar.setTitle("微记正文");
		
	}
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return true;
	}
	
}

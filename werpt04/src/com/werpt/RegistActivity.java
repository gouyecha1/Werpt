package com.werpt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;

public class RegistActivity extends Activity {

	private ActionBar mActionBar;
	private ActionBarSherlock mSherLock;
	private Button regist;
	private Button clear;
	private TextView userName;
	private TextView passWord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);
		mSherLock = ActionBarSherlock.wrap(this);
		mActionBar = mSherLock.getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
//		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setTitle("注册");
		regist = (Button) findViewById(R.id.regist);
		clear = (Button) findViewById(R.id.clear);
		userName = (TextView) findViewById(R.id.username);
		passWord = (TextView) findViewById(R.id.password);
		
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

package com.werpt;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.ActionBarSherlock.OnCreateOptionsMenuListener;
import com.actionbarsherlock.ActionBarSherlock.OnOptionsItemSelectedListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class SendWerptActivity extends Activity implements
		OnCreateOptionsMenuListener, OnOptionsItemSelectedListener {
	private ActionBarSherlock mSherlock = ActionBarSherlock.wrap(this);
	private ActionBar mActionBar;
	private EditText task, title, content, tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_werpt);
		mActionBar = mSherlock.getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setTitle("发微记");
		task = (EditText) findViewById(R.id.task);
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);
		tag = (EditText) findViewById(R.id.tag);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("submit").setIcon(R.drawable.navigation_forward)
				.setTitle("submit")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

}

package com.werpt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;

public class LoginActivity extends Activity {
	private ActionBar mActionBar;
	private ActionBarSherlock mSherLock;
	private Button login;
	private Button regist;
	private Button clear;
	private TextView userName;
	private TextView passWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mSherLock = ActionBarSherlock.wrap(this);
		mActionBar = mSherLock.getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
//		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false); 
		mActionBar.setTitle("登录");
		login = (Button) findViewById(R.id.login_submit);
		regist = (Button) findViewById(R.id.regist);
		clear = (Button) findViewById(R.id.clear);
		userName = (TextView) findViewById(R.id.username);
		passWord = (TextView) findViewById(R.id.password);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(100);
				finish();
			}
		});
		regist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
				startActivity(intent);
			}
		});
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userName.setText("");
				passWord.setText("");
			}
		});
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

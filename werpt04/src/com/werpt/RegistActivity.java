package com.werpt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.werpt.costant.Address;

public class RegistActivity extends Activity {

	private ActionBar mActionBar;
	private ActionBarSherlock mSherLock;
	private Button regist;
	private Button clear;
	private TextView userName;
	private TextView passWord;
	private TextView repeat;
	private String uName;
	private String uPass;
	private String re;
	private String url, result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);
		mSherLock = ActionBarSherlock.wrap(this);
		mActionBar = mSherLock.getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		// mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setTitle("注册");
		regist = (Button) findViewById(R.id.regist);
		clear = (Button) findViewById(R.id.clear);
		userName = (TextView) findViewById(R.id.username);
		passWord = (TextView) findViewById(R.id.password);
		repeat = (TextView) findViewById(R.id.repeat);
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userName.setText("");
				passWord.setText("");
				repeat.setText("");
			}
		});
		regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uName = userName.getEditableText().toString();
				uPass = passWord.getEditableText().toString();
				re = repeat.getEditableText().toString();
				if (uName.equals("") || uPass.equals("") || (!uPass.equals(re))) {
					Toast.makeText(RegistActivity.this, "请正确输入",
							Toast.LENGTH_SHORT).show();
				} else {
					url = Address.REGIST;
					new GetDataTask().execute();
				}
			}
		});

	}

	public String getData(String userName, String passWord) {
		String result = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", uName));
			params.add(new BasicNameValuePair("password", uPass));
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response;
			response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				System.out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {

			result = getData(uName, uPass);

			Message msg = handler.obtainMessage();
			if (result != null) {
				msg.what = 1;

			} else {
				msg.what = 0;
			}

			handler.sendMessage(msg);
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			super.onPostExecute(result);
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				try {
					JSONObject obj = new JSONObject(result);
					String flag = obj.getString("flag");
					if (!flag.equals("-1")) {
						Toast.makeText(RegistActivity.this, "注册成功" + result,
								Toast.LENGTH_SHORT).show();
						SharedPreferences sp=getSharedPreferences("user", MODE_PRIVATE);
						Editor edit=sp.edit();
						edit.putString("uname", flag);
						edit.putString("upass", uPass);
						edit.commit();
						setResult(100);
						finish();
					} else {
						Toast.makeText(RegistActivity.this, "注册失败" + result,
								Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {

			}
		}

	};

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

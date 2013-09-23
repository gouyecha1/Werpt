package com.werpt;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.ActionBarSherlock.OnCreateOptionsMenuListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.werpt.costant.MyAddress;
import com.werpt.util.ImageLazyLoad;
import com.werpt.util.ServiceData;
import com.werpt.util.Task;

public class TaskDetailActivity extends Activity implements
		OnCreateOptionsMenuListener {
	private ActionBarSherlock mSherlock = ActionBarSherlock.wrap(this);
	private ActionBar mActionBar;
	private int taskId;
	private LinearLayout loading;
	private String result;
	private ImageLazyLoad load = new ImageLazyLoad();
	private PullToRefreshScrollView mPullToRefreshScrollView;
	private Boolean flag = false;
	private TextView title;
	private TextView deadline;
	private ImageView img;
	private TextView num;
	private TextView content;
	private SharedPreferences userInfo;
	private String uname;
	private String url;
	private Bitmap bit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_detail);
		userInfo = getSharedPreferences("user", MODE_PRIVATE);
		taskId = getIntent().getIntExtra("taskId", 0);
		uname = userInfo.getString("uname", "");
		initActionBar();
		initView();
		 new GetDataTask().execute();
	}

	private void initView() {
		mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		loading = (LinearLayout) findViewById(R.id.loading);
		title = (TextView) findViewById(R.id.title);
		deadline = (TextView) findViewById(R.id.deadline);
		num = (TextView) findViewById(R.id.num);
		img = (ImageView) findViewById(R.id.img);
		content=(TextView) findViewById(R.id.content);
		mPullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						flag = true;
						new GetDataTask().execute();
					}
				});

	}

	
	private void initActionBar() {
		mActionBar = mSherlock.getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("悬赏正文");
		mActionBar.setDisplayShowHomeEnabled(false);
	}
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (flag) {
					mPullToRefreshScrollView.onRefreshComplete();
					Toast.makeText(TaskDetailActivity.this, "刷新成功",
							Toast.LENGTH_SHORT).show();

				}
				Task t = getTaskAll(result);
				
				url = MyAddress.WEIJIIMAGE + t.getPic();
				
				load.getBitmap(img, url, 3,
						TaskDetailActivity.this);
				title.setText(t.getTitle());
				deadline.setText(t.getEndTime());
				content.setText(Html.fromHtml(t.getDescription()));
				num.setText(String.valueOf(t.getWerptNum()));

			} else {
				Toast.makeText(TaskDetailActivity.this, "刷新失败",
						Toast.LENGTH_SHORT).show();
			}
			loading.setVisibility(View.GONE);

		}

	};
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			String[] key = {"taskId","uname"};
			String[] value = {String.valueOf(taskId),uname};
			
			HashMap<String, String> map = getMap(key, value);
			result = ServiceData.getServiceData(map, MyAddress.GETTASKALLINFO);
			Message msg = handler.obtainMessage();
			if (!result.equals("0")) {
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
	
	public Task getTaskAll(String str){
		Task t = null;
		try {
			JSONObject obj = new JSONObject(str);
			int id = obj.getInt("id");
			String title = obj.getString("title");
			String description = obj.getString("description");
			String pic = obj.getString("pic");
			String endTime = obj.getString("endtime");
			int werptNum = obj.getInt("werptNum");
			t = new Task(id, title, description, pic, werptNum, endTime);
			System.out.println("输出的悬赏的详细信息是"+t.getId()+"-->"+t.getTitle()+"-->"+t.getPic()+"-->"+t.getEndTime()+"-->"+t.getDescription()+"-->"+t.getWerptNum());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return t;
	}
	public HashMap<String, String> getMap(String[] key,String[] value){
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < key.length; i++) {
			map.put(key[i], value[i]);
		}
		return map;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
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

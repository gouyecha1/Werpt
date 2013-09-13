package com.werpt;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.ActionBarSherlock.OnCreateOptionsMenuListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.werpt.bean.Werpt;
import com.werpt.util.ImageLazyLoad;
import com.werpt.util.ServiceData;

public class WerptDetailActivity extends Activity implements
		OnCreateOptionsMenuListener {
	private ActionBarSherlock mSherlock = ActionBarSherlock.wrap(this);
	private ActionBar mActionBar;
	private TextView title, nickname, addtime, content;
	private ImageView img;
	private String result;
	private ImageLazyLoad load = new ImageLazyLoad();
	private ArrayList<String> thumbList;
	private int wid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.werpt_detail);
		wid = getIntent().getIntExtra("wid", 0);
		initActionBar();
		initView();
		Thread t = new Thread(getWerptDetail);
		t.start();
		

	}

	private void initActionBar() {
		mActionBar = mSherlock.getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		// mActionBar.setDisplayUseLogoEnabled(true);
		mActionBar.setTitle("微记正文");
		mActionBar.setDisplayShowHomeEnabled(false);

	}

	private void initView() {
		title = (TextView) findViewById(R.id.title);
		nickname = (TextView) findViewById(R.id.nickname);
		addtime = (TextView) findViewById(R.id.addtime);
		content = (TextView) findViewById(R.id.content);
		img =  (ImageView) findViewById(R.id.img);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		return mSherlock.dispatchCreateOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("refresh").setIcon(R.drawable.refresh).setTitle("刷新")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add("collection").setIcon(R.drawable.collection).setTitle("收藏")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add("share").setIcon(R.drawable.share).setTitle("分享")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add("comment").setIcon(R.drawable.feedback).setTitle("评论")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {

				Werpt werpt = getWerptAll(result);
				title.setText(werpt.getTitle());
			}

		}

	};
	Runnable getWerptDetail = new Runnable() {
		Message msg = handler.obtainMessage();

		@Override
		public void run() {
			result = ServiceData.getWeiJiJSONData(wid);

			if (!result.equals("0")) {

				msg.what = 1;
			} else {
				msg.what = 0;
			}
			handler.sendMessage(msg);

		}
	};

	public Werpt getWerptAll(String str) {
		Werpt w = null;
		try {
			JSONObject obj = new JSONObject(str);
			int id = obj.getInt("id");
			String uname = obj.getString("username");
			int uid = obj.getInt("uid");
			String title = obj.getString("title");
			String content = obj.getString("content");
			String thumb = obj.getString("thumb");
			String addtime = obj.getString("addtime");
			int comment = obj.getInt("comments");
			int collection = obj.getInt("favs");
			int shares = obj.getInt("shares");
			w = new Werpt(id, uid, title, content, thumb, addtime, comment,
					collection, shares, 0, uname);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return w;
	}

	public ArrayList<String> getThumbList(String thumb) {
		ArrayList<String> data = new ArrayList<String>();
		try {
			JSONArray arr = new JSONArray(thumb);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				String url = obj.getString("thumb");
				data.add(url);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
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

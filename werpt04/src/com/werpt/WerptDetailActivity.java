package com.werpt;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.ActionBarSherlock.OnCreateOptionsMenuListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.werpt.bean.Werpt;
import com.werpt.costant.MyAddress;
import com.werpt.util.ImageLazyLoad;
import com.werpt.util.ServiceData;

public class WerptDetailActivity extends Activity implements
		OnCreateOptionsMenuListener {
	private ActionBarSherlock mSherlock = ActionBarSherlock.wrap(this);
	private ActionBar mActionBar;
	private TextView title, nickname, addtime, content;
	private ImageView img;
	private LinearLayout loading, img_lay;
	private String result;
	private ImageLazyLoad load = new ImageLazyLoad();
	private ArrayList<String> thumbList;
	private int wid;
	private String uname = "ming";
	private PullToRefreshScrollView mPullToRefreshScrollView;
	private Boolean flag = false;

	// private List<Bitmap> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.werpt_detail);
		
		wid = getIntent().getIntExtra("wid", 0);
		initActionBar();
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			BitmapDrawable bg=(BitmapDrawable) getResources().getDrawable(R.drawable.bg_striped);
			bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			mActionBar.setBackgroundDrawable(bg);
			 BitmapDrawable bgSplit = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
			 bgSplit.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			 mActionBar.setBackgroundDrawable(bgSplit);
		}
		initView();
		new GetDataTask().execute();

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
		loading = (LinearLayout) findViewById(R.id.loading);
		img_lay = (LinearLayout) findViewById(R.id.img_lay);
		mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
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

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			String[] key = { "id", "uname" };
			String[] value = { String.valueOf(wid), uname };
			HashMap<String, String> map = getMap(key, value);
			result = ServiceData.getServiceData(map, MyAddress.WEIJIALLINFO);

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
				if (flag) {
					mPullToRefreshScrollView.onRefreshComplete();
					Toast.makeText(WerptDetailActivity.this, "刷新成功",
							Toast.LENGTH_SHORT).show();

				}
				Werpt werpt = getWerptAll(result);
				title.setText(werpt.getTitle());
				// TODO change to getNickname()
				nickname.setText(werpt.getUsername());
				addtime.setText(werpt.getAddtime());
				content.setText(Html.fromHtml(werpt.getContent()));

				thumbList = getThumbList(werpt.getThumb());
				
				img_lay.removeAllViews();
				// TODO 获取媒体文件
				for (int i = 0; i < thumbList.size()-1; i++) {
					System.out.println("thumb"+thumbList.get(i));
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
					param.setMargins(0, 0, 0, 4);
					ImageView iv = new ImageView(WerptDetailActivity.this);
					iv.setImageResource(R.drawable.werpt_pic_thumb_default);
					img_lay.addView(iv, param);
					

						String url = thumbList.get(i);
						Bitmap bit = null;
						if (url.endsWith(".3GP") || url.endsWith(".3gp")
								|| url.endsWith(".mp4") || url.endsWith(".MP4")) {
							bit = ThumbnailUtils.createVideoThumbnail(
									MyAddress.WEIJIIMAGE + url,
									Video.Thumbnails.MINI_KIND);
						} else {
							load.getBitmap(iv, MyAddress.WEIJIIMAGE
									+ thumbList.get(i), 3,
									WerptDetailActivity.this);
						}
						
						
				}

			} else {
				Toast.makeText(WerptDetailActivity.this, "刷新失败",
						Toast.LENGTH_SHORT).show();
			}
			loading.setVisibility(View.GONE);

		}

	};

	public HashMap<String, String> getMap(String[] key, String[] value) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < key.length; i++) {
			map.put(key[i], value[i]);
		}
		return map;
	}

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

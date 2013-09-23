package com.werpt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.werpt.util.ServiceData;


public class MediaStoreActivity extends Activity {

	private GridView werptMediaStoreContent;
	private ArrayList<String> imagePathList = new ArrayList<String>();
	private ArrayList<String> videoPathList = new ArrayList<String>();
	private int flags = 0;
	private SharedPreferences mediaSelect;
	private SharedPreferences mediaUrl;
	private SharedPreferences articleUrl;
	private int pageCode = 1,pageSize = 20,allCounts,allPages;
	private List<String> data = null;
//	private MediaStoreGridAdapter adapter;
	private int imageSize;
	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		super.setContentView(R.layout.werpt_mediastore_layout);
//
//		if(ServiceData.hasSDCard()){
//			ServiceData.scanSdCard(this);
//		}
//
//		dialog = new ProgressDialog(this);
//		dialog.setMessage("正在加载数据,请稍后...");
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
//		
//		flags = getIntent().getFlags();
//		if(flags == 3 || flags == 6 || flags == 9){
//			ActivityManager.exitApp(0);
//		}
//		ActivityManager.addActivity(this);
//
//		mediaSelect = getSharedPreferences("mediaSelect", MODE_PRIVATE);
//		mediaUrl = getSharedPreferences("mediaUrl", MODE_PRIVATE);
//		articleUrl = getSharedPreferences("articleUrl", MODE_PRIVATE);
//
//		werptMediaStoreContent = (GridView)findViewById(R.id.werpt_mediastore_content_gridview);
//		new Thread(initAdapter).start();
//		
//	}
//	
//	private Handler handler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			werptMediaStoreContent.setAdapter(adapter);
//			dialog.cancel();
//			werptMediaStoreContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
//					String url = "";
//					if(position < imagePathList.size()){
//						url = imagePathList.get(position);
//					}else{
//						url = videoPathList.get(position - imagePathList.size());
//					}
//					if (flags == 3 || flags == 6 || flags == 9) {
//						finish();
//						int size = getCurrentMedia(mediaSelect);
//						size ++;
//						Editor edit = mediaSelect.edit();
//						if(position < imagePathList.size()){
//							edit.putString("image"+size, url);
//						}else{
//							edit.putString("video"+size, url);
//						}
//						edit.commit();
//						Intent intent = new Intent(MediaStoreActivity.this, WriteContentActivity.class);
//						intent.setFlags(flags);
//						startActivity(intent);
//					}else if(flags == 99){
//						if(position < imagePathList.size()){
//							finish();
//							Editor edit = articleUrl.edit();
//							edit.putString("selectedUrl", url);
//							edit.commit();
//							Intent intent = new Intent(MediaStoreActivity.this, WriteArticleActivity.class);
//							startActivity(intent);
//						}else{
//							Toast.makeText(MediaStoreActivity.this, "不能选择视频文件", Toast.LENGTH_SHORT).show();
//						}
//					}
//				}
//			});
//
//			werptMediaStoreContent.setOnScrollListener(new AbsListView.OnScrollListener() {
//				@Override
//				public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//				}
//
//				@Override
//				public void onScroll(AbsListView view, final int firstVisibleItem,final int visibleItemCount, final int totalItemCount) {
//					handler.postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							if(firstVisibleItem + visibleItemCount >= totalItemCount){
//								List<String> list = null;
//								System.out.println("pageCode-->"+pageCode+"-----"+"allPages"+"-->"+allPages);
//								if(pageCode <= allPages){
//									list = getData(pageCode, pageSize, allCounts);
//								}
//								if(list == null){
//									list = new ArrayList<String>();
//								}
//								if(adapter.count + list.size() <= allCounts){
//									int size = list.size();
//									adapter.count =  adapter.count + size;
//									for (int i = 0; i < list.size(); i++) {
//										data.add(list.get(i));
//									}
//									adapter.notifyDataSetChanged();
//								}
//								pageCode ++;
//							}
//						}
//					}, 1000);
//				}
//			});
//		}
//	};
//	
//	Runnable initAdapter = new Runnable() {
//		@Override
//		public void run() {
//			imagePathList = ServiceData.getData(MediaStoreActivity.this,Adress.imageUri, Adress.imagePath, Adress.imageOrder);
//			videoPathList = ServiceData.getData(MediaStoreActivity.this,Adress.videoUri, Adress.videoPath, Adress.videoOrder);
//			allCounts = imagePathList.size() + videoPathList.size();
//			if(allCounts <= 50){
//				allPages = 1;
//			}else{
//				allPages = (allCounts - 50 - 1)/pageSize + 1;
//			}
//			imageSize = imagePathList.size();
//			data = getMediaData(allCounts, 1);
//			adapter = new MediaStoreGridAdapter();
//			Message msg = handler.obtainMessage();
//			msg.obj = adapter;
//			handler.sendMessage(msg);
//		}
//	};
//	
//	public List<String> getData(int pagecode,int pagesize,int allCounts){
//		List<String> list = getMediaData(allCounts, 2);
//		int start = (pagecode - 1) * pagesize;
//		int end = start + pagesize ;
//		System.out.println(start +"-->"+end+"-->"+allCounts);
//		allCounts = allCounts - 50;
//		if(start < end){
//			if(end <= allCounts){
//				return list.subList(start, end);
//			}else{
//				if(start <= allCounts){
//					return list.subList(start, allCounts);
//				}
//				else{
//					return data;
//				}
//			}
//		}else{
//			return data;
//		}
//	}
//	public ArrayList<String> getAllData(int allCounts){
//		ArrayList<String> list = new ArrayList<String>();
//		int imageSize = imagePathList.size();
//		for (int i = 0; i < allCounts; i++) {
//			if(i <= imageSize - 1){
//				list.add(imagePathList.get(i));
//			}else{
//				list.add(videoPathList.get(i - imageSize));
//			}
//		}
//		return list;
//	}
//	public List<String> getMediaData(int allCounts,int flag){
//		List<String> list = getAllData(allCounts);
//		if(flag == 1){
//			list = list.subList(0, 50);
//		}else if(flag == 2){
//			list = list.subList(50, list.size());
//		}
//		return list;
//	}
//
//	public void clear(SharedPreferences share){
//		Editor edit = share.edit();
//		edit.clear();
//		edit.commit();
//	}
//
//	public class MediaStoreGridAdapter extends BaseAdapter {
//
//		private Bitmap map;
//		private BitmapFactory.Options options;
//		public int count = data.size();//9092
//
//		@Override
//		public int getCount() {
//			return count;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View view = null;
//			if(convertView != null){
//				view = (View)convertView;
//			}else{
//				view = LayoutInflater.from(MediaStoreActivity.this).inflate(R.layout.werpt_mediastore_item, null);
//			}
//
//			RelativeLayout bg = (RelativeLayout)view.findViewById(R.id.werpt_grid_item_layout);
//			ImageView play = (ImageView)view.findViewById(R.id.werpt_grid_item_play);
//
//			String url = "";
//			options = new BitmapFactory.Options();
//			options.inSampleSize = 3;
//			if(position <= imageSize - 1){
//				url = data.get(position);
//				map = BitmapFactory.decodeFile(url, options);
//				if(map == null){
//					map = BitmapFactory.decodeResource(getResources(), R.drawable.default_image, options);
//				}
//			}else{
//				url = data.get(position);
//				map = ThumbnailUtils.createVideoThumbnail(url, Video.Thumbnails.MINI_KIND);
//				if(map == null){
//					map = BitmapFactory.decodeResource(getResources(), R.drawable.default_image, options);
//				}
//			}
//			int width = ServiceData.getScreenWidthOrHeight("width", MediaStoreActivity.this);
//			map = ThumbnailUtils.extractThumbnail(map, (int)(0.4*width), (int)(0.3*width));
//			BitmapDrawable d = new BitmapDrawable(map);
//			bg.setBackgroundDrawable(d);
//
//			if(position > imageSize - 1){
//				play.setVisibility(View.VISIBLE);
//			}else{
//				play.setVisibility(View.GONE);
//			}
//			return view;
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		ActivityManager.removeActivity(this);
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		finish();
//		if(flags == 36){
//			Intent intent = new Intent(MediaStoreActivity.this, HomeActivity.class);
//			startActivity(intent);
//		}else if(flags == 3 || flags == 6 || flags == 9){
//			Intent intent = new Intent(MediaStoreActivity.this, WriteContentActivity.class);
//			intent.setFlags(flags);
//			startActivity(intent);
//		}else if(flags == 99){
//			Intent intent = new Intent(MediaStoreActivity.this, WriteArticleActivity.class);
//			startActivity(intent);
//		}
//		return false;
//	}
//	public int getCurrentMedia(SharedPreferences share){
//		Map<String, String> map = (Map<String, String>)share.getAll();
//		return map.size();
	}
}

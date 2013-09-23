package com.werpt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.werpt.costant.MyAddress;

public class VideoStoreActivity extends Activity {
	private List<Bitmap> list = new ArrayList<Bitmap>();
	private GridView gridView;
	private MyAdapter adapter = new MyAdapter();
	private File file;
	private File[] f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_store);
		file = new File(MyAddress.VIDEOPATH);
		f = file.listFiles();
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(adapter);
		new GetDataTask().execute();

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				adapter.notifyDataSetChanged();
			} else {

			}
		}

	};

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			for (int i = 0; i < f.length; i++) {
				String path = f[i].toString();
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path,
						Thumbnails.MINI_KIND);
				if(bitmap==null){
					bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
				}
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				bitmap = ThumbnailUtils.extractThumbnail(bitmap,
						(int) (100 * w / h), 100);
				list.add(bitmap);
			}
			Message msg = handler.obtainMessage();
			if (list != null) {
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

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (list.size() == 0) {
				return 0;
			}
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.pic_store_item, null);
			ImageView img = (ImageView) view.findViewById(R.id.img);
			img.setImageBitmap(list.get(position));
			return view;
		}

	}

}

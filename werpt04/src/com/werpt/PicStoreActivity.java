package com.werpt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.werpt.costant.MyAddress;

public class PicStoreActivity extends Activity {
	private List<Bitmap> list = new ArrayList<Bitmap>();
	private GridView gridView;
	private MyAdapter adapter = new MyAdapter();
	private File file;
	private File[] f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_store);
		file = new File(MyAddress.PICPATH);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(adapter);
		new GetDataTask(this).execute();

	}

	

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		AlertDialog dialog;
		public GetDataTask(Context context){
//			dialog=new ProgressDialog(context);
//			dialog.setMessage("正在加载...");
//			dialog.show();
		}
		@Override
		protected String[] doInBackground(Void... params) {
			f = file.listFiles();
			if (f != null) {
				for (int i = 0; i < f.length; i++) {
					String path = f[i].toString();
					BitmapFactory.Options options = new BitmapFactory.Options(); 
					options.inJustDecodeBounds=true;
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					int w = bitmap.getWidth();
					int h = bitmap.getHeight();
					int s=w/100;
					options.inJustDecodeBounds=false;
					options.inSampleSize=s;
					bitmap=BitmapFactory.decodeFile(path, options);
//					bitmap = ThumbnailUtils.extractThumbnail(bitmap,
//							(int) (100 * w / h), 100);
					list.add(bitmap);
					publishProgress();
				}
				
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			adapter.notifyDataSetChanged();
		}
		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			
			adapter.notifyDataSetChanged();
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

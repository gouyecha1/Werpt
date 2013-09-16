package com.werpt.util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.view.View;
import android.widget.ImageView;

public class ImageLazyLoad {
	
	public Map<String, SoftReference<Bitmap>> map = null ;
	
	public ImageLazyLoad(){
		map = new HashMap<String, SoftReference<Bitmap>>();
	}
	
	public Bitmap getBitmap(final ImageView imageView,final String url){
		Bitmap bit = null;
		if(map.containsKey(url)){
			SoftReference<Bitmap> soft = map.get(url);
			bit = soft.get();
			if(bit != null){
				return bit;
			}
		}
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				imageView.setImageBitmap((Bitmap)msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap b = getBitmap(url);
				if(b != null){
					SoftReference<Bitmap> soft = new SoftReference<Bitmap>(b);
					map.put(url, soft);
					Message msg = handler.obtainMessage();
					msg.obj = b;
					handler.sendMessage(msg);
				}
			}
		}).start();
		return null;
	}
	public Bitmap getBitmap(final View view,final String url,final int flag){
		Bitmap bit = null;
		if(map.containsKey(url)){
			SoftReference<Bitmap> soft = map.get(url);
			bit = soft.get();
			if(bit != null){
				return bit;
			}
		}
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				BitmapDrawable d = new BitmapDrawable((Bitmap)msg.obj);
				view.setBackgroundDrawable(d);
			}
		};//9092
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap b = null;
				if(flag == 1){
					b = getImageBit(url);
				}else if(flag == 2){
					b = getVideoBit(url);
				}else if(flag == 3){
					b = getBitmap(url);
				}
				if(b != null){
					SoftReference<Bitmap> soft = new SoftReference<Bitmap>(b);
					map.put(url, soft);
					Message msg = handler.obtainMessage();
					msg.obj = b;
					handler.sendMessage(msg);
				}
			}
		}).start();
		return null;
	}
	public Bitmap getBitmap(String u){
		Bitmap map = null;
		try {
			URL url = new URL(u);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			InputStream in = conn.getInputStream();
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = getInSampleSize(u);
			map = BitmapFactory.decodeStream(in, null, option);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	public Bitmap getImageBit(String url){
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = 2;
		Bitmap bit = BitmapFactory.decodeFile(url, option);
		return bit;
	}
	public Bitmap getVideoBit(String url){
		Bitmap bit = ThumbnailUtils.createVideoThumbnail(url, Video.Thumbnails.MINI_KIND);
		return bit;
	}
	public int getInSampleSize(String u){
		int percent = 1;
		try {
			URL url = new URL(u);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			InputStream in = conn.getInputStream();
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inJustDecodeBounds = true;
			Bitmap m = BitmapFactory.decodeStream(in, null, op);
			int imageWidth = op.outWidth;
			int imageHeight = op.outHeight;
			if(imageWidth >300 && imageHeight > 200){
				int w_num = imageWidth / 300;
				int h_num = imageHeight / 200;
				percent = Math.max(w_num, h_num);
			}
			if(percent == 0){
				percent = 1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percent;
	}
}

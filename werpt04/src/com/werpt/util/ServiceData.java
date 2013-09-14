package com.werpt.util;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.werpt.bean.Comment;
import com.werpt.costant.Address;

public class ServiceData {

	//根据不同的参数返回屏幕的宽高
	public static int getScreenWidthOrHeight(String flag,Context context){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();
		if(flag.equals("width")){
			return d.getWidth();
		}else{
			return d.getHeight();
		}
	}

	public static boolean hasSDCard(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	
	//控件的触摸事件
	public static void onCustomTouchListener(View view,final int downXmlFile1,final int nomalXmlFile2){
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					v.setBackgroundResource(downXmlFile1);
				}else if(event.getAction() == MotionEvent.ACTION_MOVE){
					v.setBackgroundResource(nomalXmlFile2);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					v.setBackgroundResource(nomalXmlFile2);
				}
				return false;
			}
		});
	}

	//将一个整形的正整数转化为分秒的表示
	public static int getTime(int flag ,long time){
		int minute = (int)(((float)time/1000)/60);
		int second = (int)((((float)time/1000)/60 - minute)*60);
		if(flag == 1){
			return minute;
		}else{
			return second;
		}

	}
	//如果：9-->09
	public static String getStr(int num){
		if(num <= 9){
			return "0"+num;
		}else{
			return ""+num;
		}
	}

	//不同的手机使用的照片的照片的帧数
	public static Integer getPictureFormats(Camera.Parameters params){
		List<Integer> pictureFormatList = params.getSupportedPictureFormats();
		return pictureFormatList.get(0);
	}

	//不同手机使用的每秒的帧数（预览的）
	public static Integer getPreviewFrameRates(Camera.Parameters params){
		List<Integer> previewFrameRatesList = params.getSupportedPreviewFrameRates();
		return previewFrameRatesList.get(previewFrameRatesList.size()-1);
	}

	//不同手机使用的照片的尺寸
	public static Size getPictureSizes(Camera.Parameters params){
		List<Size> pictureSizesList = params.getSupportedPictureSizes();
		return pictureSizesList.get(0);
	}

	//不同手机使用的预览的尺寸
	public static Size getPreviewSizes(Camera.Parameters params){
		List<Size> previewSizesList = params.getSupportedPreviewSizes();
		return previewSizesList.get(0);
	}

	//根据指定的参数获得手机中所有的视频和图片的信息
	public static ArrayList<String> getData(Context context,Uri uri, String[] projection, String sortOrder ){
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = context.getContentResolver().query(uri, projection, null,null, sortOrder);
		System.out.println("查询的图片的数据"+cursor.getCount());
		cursor.moveToFirst();
		for (int count = 0; count < cursor.getCount(); count++) {
			list.add(cursor.getString(cursor.getColumnIndex(projection[0])));
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	//刷新媒体库
	public static void scanSdCard(Context context){ 
		Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory().getAbsolutePath())); 
		context.sendBroadcast(intent);
	}
	public static void initAlpha(View view,float from,float to,int duration){
		AlphaAnimation alpha = new AlphaAnimation(from, to);
		alpha.setFillAfter(true);
		alpha.setDuration(duration);
		view.startAnimation(alpha);
	}
	
	public static void customOnClick(View view,final Activity activity,final Class cls){
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				activity.finish();
				Intent intent = new Intent(activity,cls);
				activity.startActivity(intent);
			}
		});
	}
	
	public static String getTaskData(int pageCode,int pageSize,String url){
		String result = "0";
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("pageCode", pageCode+""));
			params.add(new BasicNameValuePair("pageSize", pageSize+""));
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response;
			response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				System.out.println(result);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static String getUserSimpleWeiJi(int pageCode,int pageSize,String uname){
		String result = "0";
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("pageCode", pageCode+""));
			params.add(new BasicNameValuePair("pageSize", pageSize+""));
			params.add(new BasicNameValuePair("uname", uname));
			HttpPost request = new HttpPost(Address.WEIJIUSERALL);
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response;
			response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				System.out.println(result);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static String getUserCount(String uname){
		String result = "0";
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uname", uname));
			HttpPost request = new HttpPost(Address.WEIJIUSERCOUNT);
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response;
			response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				System.out.println(result);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static String getJSONData(String url){
		String result = "0";
		try {
			HttpPost request = new HttpPost(url);
			HttpResponse response;
			response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				System.out.println(result);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static String getTaskJSONData(int taskId){
		String result = "0";
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("taskId", taskId+""));
			HttpPost request = new HttpPost(Address.GETTASKALLINFO);
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				System.out.println(result);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static String getWeiJiJSONData(int wid){
		String result = "0";
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", wid+""));
			HttpPost request = new HttpPost(Address.WEIJIALLINFO);
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				System.out.println(result);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static String getWeiJiCommment(int wid,int pageCode,int pageSize){
		String result = "0";
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", wid+""));
			params.add(new BasicNameValuePair("pageCode", pageCode+""));
			params.add(new BasicNameValuePair("pageSize", pageSize+""));
			HttpPost request = new HttpPost(Address.GETWEIJICOMMENT);
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				System.out.println(result);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static String insertCommment(Comment c){
		String result = "0";
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", c.getWid()+""));
			params.add(new BasicNameValuePair("uname", c.getUname()));
			params.add(new BasicNameValuePair("content", c.getContent()));
			HttpPost request = new HttpPost(Address.INSERTCOMMENT);
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				System.out.println(result);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static String getServiceData(Map<String, String> map,String url){
		String result = "0";
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if(map != null){
				Set<Entry<String, String>> enty = map.entrySet();
				Iterator<Entry<String, String>> iter = enty.iterator();
				while(iter.hasNext()){
					Entry<String, String> en = iter.next();
					BasicNameValuePair basc = new BasicNameValuePair(en.getKey(), en.getValue());
					params.add(basc);
				}
			}
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			int code = response.getStatusLine().getStatusCode();
			if(code == 200){
				result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	public static void clear(SharedPreferences share){
		Editor edit = share.edit();
		edit.clear();
		edit.commit();
	}
	public static boolean getNetWork(final Context context){
		ProgressDialog dialog1 = new ProgressDialog(context);
    	dialog1.setMessage("正在检查网络...");
 		dialog1.show();
    	ConnectivityManager conn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if(conn.getActiveNetworkInfo() == null){
    		Dialog dialog2 = new AlertDialog.Builder(context)
											.setTitle("是否进入网络设置界面")
											.setPositiveButton("是", new DialogInterface.OnClickListener(){
												@Override
												public void onClick(DialogInterface dialog,int which) {
													Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
													context.startActivity(intent);
												}})
											.setNegativeButton("否", new DialogInterface.OnClickListener(){
												@Override
												public void onClick(DialogInterface dialog,int which) {
												}})
											.create();
    		dialog2.show();
    	}else if(conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED){
    		Toast.makeText(context, "您所使用的网络是WIFI", Toast.LENGTH_SHORT).show();
    		dialog1.cancel();
    		return true;
    	}else if(conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()==NetworkInfo.State.CONNECTED){
    		String proxyHost = android.net.Proxy.getDefaultHost();
    		if(proxyHost!=null&&!proxyHost.equals("")){
    			Toast.makeText(context, "您所使用的网络是WAP", Toast.LENGTH_SHORT).show();
    			dialog1.cancel();
    			return true;
    		}
    		Toast.makeText(context, "您所使用的网络是GPRS", Toast.LENGTH_SHORT).show();
    		dialog1.cancel();
    		return true;
    	}
    	return false;
    }
}

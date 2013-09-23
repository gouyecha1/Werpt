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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.werpt.costant.MyAddress;
import com.werpt.util.ServiceData;

public class LeftMenuFragment extends Fragment {
	private Button login;
	private ImageView userPhoto;
	private TextView nickName;
	private ImageView camera, video, write;
	private String uName, uPass;
	private SharedPreferences sp;
	private String result;
	private String url;
	private LinearLayout user;
	private Button picStore, videoStore;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sp = getActivity().getSharedPreferences("user", 0);
		uName = sp.getString("uname", "");
		uPass = sp.getString("upass", "");
		url = MyAddress.LOGIN;
		new GetDataTask().execute();

		View convertView = inflater.inflate(R.layout.left_menu_content, null);
		login = (Button) convertView.findViewById(R.id.login);
		userPhoto = (ImageView) convertView.findViewById(R.id.userphoto);
		nickName = (TextView) convertView.findViewById(R.id.nickname);
		camera = (ImageView) convertView.findViewById(R.id.camera);
		video = (ImageView) convertView.findViewById(R.id.video);
		write = (ImageView) convertView.findViewById(R.id.write);
		user = (LinearLayout) convertView.findViewById(R.id.user);
		picStore = (Button) convertView.findViewById(R.id.pic_store);
		videoStore = (Button) convertView.findViewById(R.id.video_store);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				// startActivity(intent);
				login.setClickable(false);

				startActivityForResult(intent, 0);
			}
		});

		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ServiceData.hasSDCard()) {
					Intent intent = new Intent(getActivity(),
							CameraActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "未插入内存卡", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ServiceData.hasSDCard()) {
					Intent intent = new Intent(getActivity(),
							VideoActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "未插入内存卡", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						SendWerptActivity.class);
				startActivity(intent);
			}
		});
		picStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ServiceData.hasSDCard()) {
					Intent intent = new Intent(getActivity(),
							PicStoreActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "未插入内存卡", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		videoStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ServiceData.hasSDCard()) {
					Intent intent = new Intent(getActivity(),
							VideoStoreActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "未插入内存卡", Toast.LENGTH_SHORT)
							.show();
				}
			}

		});
		return convertView;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				try {
					JSONObject obj = new JSONObject(result);
					String flag = obj.getString("flag");
					if (flag.equals("true")) {
						System.out.println("11111111111111");
						login.setVisibility(View.GONE);
						user.setVisibility(View.VISIBLE);
						nickName.setText(uName);
					} else {

						login.setVisibility(View.VISIBLE);
						user.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_SHORT)
						.show();
			}
		};
	};

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 200) {
			sp = getActivity().getSharedPreferences("user", 0);
			uName = sp.getString("uname", "");
			uPass = sp.getString("upass", "");
			url = MyAddress.LOGIN;
			new GetDataTask().execute();

		} else {
			login.setClickable(true);
		}
	}

}

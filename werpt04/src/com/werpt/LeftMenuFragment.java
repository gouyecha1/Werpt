package com.werpt;

import com.werpt.util.ServiceData;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
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

public class LeftMenuFragment extends Fragment {
	Button login;
	ImageView userPhoto;
	TextView nickName;
	ImageView camera, video, write;
	LinearLayout refresh, setting, feedback, help, about,logout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.left_menu_content, null);
		login = (Button) convertView.findViewById(R.id.login);
		userPhoto = (ImageView) convertView.findViewById(R.id.userphoto);
		nickName = (TextView) convertView.findViewById(R.id.nickname);
		camera = (ImageView) convertView.findViewById(R.id.camera);
		video = (ImageView) convertView.findViewById(R.id.video);
		write = (ImageView) convertView.findViewById(R.id.write);
		refresh = (LinearLayout) convertView.findViewById(R.id.refresh);
		setting = (LinearLayout) convertView.findViewById(R.id.setting);
		feedback = (LinearLayout) convertView.findViewById(R.id.feedback);
		help = (LinearLayout) convertView.findViewById(R.id.help);
		about = (LinearLayout) convertView.findViewById(R.id.about);
		logout=(LinearLayout) convertView.findViewById(R.id.logout);
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
				// TODO Auto-generated method stub

			}
		});
		write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		return convertView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			login.setVisibility(View.GONE);
			userPhoto.setVisibility(View.VISIBLE);
			nickName.setVisibility(View.VISIBLE);

		}else {
			login.setClickable(true);
		}
	}

}

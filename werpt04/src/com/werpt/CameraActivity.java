package com.werpt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraActivity extends Activity {
	private LinearLayout picLay,takePicLay,showPicBg,showPicLay;
	private FrameLayout picAllLay,showPicFrame;
	private ImageView takePicImg,showPicImg;
	private boolean isPreView = false,isShow = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		initView();
	}
	private void initView(){
		picLay=(LinearLayout) findViewById(R.id.pic_lay);
		takePicLay=(LinearLayout) findViewById(R.id.take_pic_lay);
		showPicBg=(LinearLayout) findViewById(R.id.show_pic_bg);
		showPicLay=(LinearLayout) findViewById(R.id.show_pic_lay);
		
		picAllLay=(FrameLayout) findViewById(R.id.pic_all_lay);
		showPicFrame=(FrameLayout) findViewById(R.id.show_pic_framelay);
		
		takePicImg=(ImageView) findViewById(R.id.take_pic_img);
		showPicImg=(ImageView) findViewById(R.id.show_pic_img);
		
		showPicImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isShow){
					showPicLay.setVisibility(View.VISIBLE);
					isShow=true;
				}else{
					showPicLay.setVisibility(View.GONE);
					isShow=false;
				}
			}
		});
	}
}

package com.werpt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.werpt.costant.Address;
import com.werpt.util.ServiceData;

public class CameraActivity extends Activity {
	private FrameLayout takePictureAllLayout,showPictureLayout;
	private LinearLayout werptPicture,takePictureLayout,werptShowPictureBg,werptShowPictureLayout; 
	private CustomSurfaceView mySurfaceView;
	private SurfaceHolder surfaceHolder;
	private boolean isPreView = false,isShow = false;
	private Camera camera;
	private ImageView werptTakePicture,showPicture;
	private ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
	private byte[] imageSource;
	private int picWidth,picHeight;
	private int flags = 0;
	private String selectUrl;
	private SharedPreferences mediaSelect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.camera);
		mediaSelect = getSharedPreferences("mediaSelect", MODE_PRIVATE);
		
		initWidget();
	}
	public void initWidget(){
		AlphaAnimation alpha = new AlphaAnimation(0.5f, 0.5f);
		alpha.setFillAfter(true);
		showPictureLayout = (FrameLayout)findViewById(R.id.show_picture_framelayout);
		showPictureLayout.getLayoutParams().width = (int)(0.3*ServiceData.getScreenWidthOrHeight("width", this));
		showPictureLayout.setVisibility(View.GONE);
		werptShowPictureBg = (LinearLayout)findViewById(R.id.werpt_show_picture_bg);
		werptShowPictureBg.setAnimation(alpha);
		werptShowPictureLayout = (LinearLayout)findViewById(R.id.werpt_show_picture_linearlayout);
		takePictureAllLayout = (FrameLayout)findViewById(R.id.take_picture_all_layout);
		takePictureAllLayout.getLayoutParams().width = (int)(0.2*ServiceData.getScreenWidthOrHeight("width", this));
		takePictureLayout = (LinearLayout)findViewById(R.id.werpt_take_picture_layout);
		takePictureLayout.setAnimation(alpha);
		showPicture = (ImageView)findViewById(R.id.werpt_show_picture_imageview);

		showPicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isShow){
					showPictureLayout.setVisibility(View.VISIBLE);
					isShow = true;
				}else{
					showPictureLayout.setVisibility(View.GONE);
					isShow = false;
				}

			}
		});

		werptPicture = (LinearLayout)findViewById(R.id.werpt_picture_linearlayout);
		mySurfaceView = new CustomSurfaceView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		werptPicture.addView(mySurfaceView, params);
		surfaceHolder = mySurfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//无缓冲
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				System.out.println("-----------surfaceDestroyed()--------------");
				if(camera != null){
					if(isPreView){
						camera.stopPreview();//取消预览
					}
					camera.release();
					camera = null;
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				System.out.println("-----------surfaceCreated()--------------");
				initCamera();

			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
				System.out.println("-----------surfaceChanged()--------------");
			}
		});

		werptTakePicture = (ImageView)findViewById(R.id.werpt_take_picture_imageview);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 10;
		Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.werpt_pic_start, options);
		werptTakePicture.setImageBitmap(bit);
		werptTakePicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(camera != null){
					camera.takePicture(null, null, pictrue);//拍照
				}
			}
		});
	}

	public void initCamera(){
		if(!isPreView){
			camera = Camera.open();//实例化摄像头
			if(camera != null){
				Camera.Parameters params = camera.getParameters();
				params.setPreviewFrameRate(ServiceData.getPreviewFrameRates(params));// 每秒显示帧数
				params.setPreviewSize(ServiceData.getPreviewSizes(params).width, ServiceData.getPreviewSizes(params).height);
				picWidth = ServiceData.getPictureSizes(params).width;
				picHeight = ServiceData.getPictureSizes(params).height;
				params.setPictureSize(ServiceData.getPictureSizes(params).width, ServiceData.getPictureSizes(params).height);
				params.setPictureFormat(ServiceData.getPictureFormats(params));
				camera.setParameters(params);// 参数填充至Camera
				try {
					camera.setPreviewDisplay(surfaceHolder);// 通过SurfaceView取得画面
				} catch (IOException e) {
					e.printStackTrace();
				}
				camera.startPreview();// 开始预览
				camera.autoFocus(null);
				isPreView = true;
			}
		}
	}


	PictureCallback pictrue = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			int screenWidth = ServiceData.getScreenWidthOrHeight("width", CameraActivity.this);
			int screenHeight = ServiceData.getScreenWidthOrHeight("height", CameraActivity.this);
			
			Bitmap map = null;
			if(picWidth > screenWidth || picHeight > screenHeight){
				BitmapFactory.Options options = new BitmapFactory.Options();
				int num = Math.max(picWidth/screenWidth+1, picHeight/screenHeight+1);
				options.inSampleSize = num*2; //width，hight设为原来的十分一
				map = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			}else if(picWidth <= screenWidth || picHeight <= screenHeight){
				map = BitmapFactory.decodeByteArray(data, 0, data.length);
			}
			Bitmap bit = Bitmap.createBitmap(map);//必须使用这种方法来构造Bitmap  否则画面是模糊的
			showPicture.setImageBitmap(bit);
			bitmapList.add(bit);

			ImageView image = new ImageView(CameraActivity.this);
			image.setImageBitmap(bit);
			int width = (int)(0.25*ServiceData.getScreenWidthOrHeight("width", CameraActivity.this));
			int height = (int)(((float)width/bit.getWidth())*bit.getHeight());
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width, height);
			p.topMargin = 6;
			werptShowPictureLayout.addView(image, p);
			
			imageSource = data;
			new Thread(r).start();
			
			/**
			 * 重新预览
			 */
			 camera.stopPreview();//停止预览
			 camera.startPreview();
		}
	};
	
	Runnable r = new Runnable() {
		@Override
		public void run() {
			String savePath = Address.PICPATH ;
			if(!new File(savePath).exists()){
				new File(savePath).mkdirs();
			}
			try {
				File picPath = File.createTempFile("pic_", ".jpg", new File(savePath));
				selectUrl = picPath.toString();
				FileOutputStream out = new FileOutputStream(picPath);
				out.write(imageSource, 0, imageSource.length);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(flags == 3 || flags == 6 || flags == 9){
				int size = getCurrentMedia(mediaSelect);
				size ++;
				Editor edit = mediaSelect.edit();
				edit.putString("image"+size, selectUrl);
				edit.commit();
			}
		}
	};
	
	
	
	
	public int getCurrentMedia(SharedPreferences share){
		Map<String, String> map = (Map<String, String>)share.getAll();
		return map.size();
	}
}

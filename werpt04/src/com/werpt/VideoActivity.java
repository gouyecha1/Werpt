package com.werpt;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.werpt.costant.Address;
import com.werpt.util.ServiceData;

public class VideoActivity extends Activity {
	private LinearLayout werptCameraVideoLayout, weprtVideoRecordeLayout,
			werptVideoLayoutBg;
	private CustomSurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private boolean isRecording = false, isStop = false, isPreView = false;
	private MediaRecorder mediaRecorder;// 多媒体录制器
	private ImageView werptVideoOperate;
	private File videoFile;
	private TextView recordeTime;
	private long currentTime;
	private Handler handler = new Handler();
	private Camera camera;
	private BitmapFactory.Options options;
	private SharedPreferences mediaSelect;
	private String selectUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video);

		mediaSelect = getSharedPreferences("mediaSelect", MODE_PRIVATE);

		initWidget();
	}

	public void initWidget() {
		werptVideoLayoutBg = (LinearLayout) findViewById(R.id.werpt_video_layout_bg);
		AlphaAnimation alpha = new AlphaAnimation(0.5f, 0.5f);
		alpha.setFillAfter(true);
		werptVideoLayoutBg.setAnimation(alpha);
		recordeTime = (TextView) findViewById(R.id.weprt_video_recorde_time);
		weprtVideoRecordeLayout = (LinearLayout) findViewById(R.id.weprt_video_recorde_layout);
		werptCameraVideoLayout = (LinearLayout) findViewById(R.id.werpt_camera_video_layout);
		surfaceView = new CustomSurfaceView(this);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		werptCameraVideoLayout.addView(surfaceView, p);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if (camera != null) {
					if (isPreView) {
						camera.stopPreview();// 取消预览
					}
					camera.release();
					camera = null;
				}
				System.out.println("----------surfaceDestroyed()-----------"
						+ "isRecording-->" + isRecording);
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				initCamera();
				System.out.println("----------surfaceCreated()-----------"
						+ "isRecording-->" + isRecording);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				System.out.println("----------surfaceChanged()-----------"
						+ "isRecording-->" + isRecording);
			}
		});
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		werptVideoOperate = (ImageView) findViewById(R.id.werpt_video_start_or_stop);
		options = new BitmapFactory.Options();
		options.inSampleSize = 10;// 原图的1/10
		Bitmap map = BitmapFactory.decodeResource(getResources(),
				R.drawable.werpt_video_start, options);
		werptVideoOperate.setImageBitmap(map);
		werptVideoOperate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("开始进行摄像");
				if (!isRecording) {
					if (isPreView) {
						camera.stopPreview();// 取消预览
						System.out.println("取消拍照的预览界面");
					}
					isPreView = false;
					camera.release();
					camera = null;
					startMediaRecorder();
					Bitmap map = BitmapFactory.decodeResource(getResources(),
							R.drawable.werpt_video_stop, options);
					werptVideoOperate.setImageBitmap(map);
				} else {
					stopMediaRecorder();
					initCamera();
					Bitmap map = BitmapFactory.decodeResource(getResources(),
							R.drawable.werpt_video_start, options);
					werptVideoOperate.setImageBitmap(map);
				}
			}
		});
	}

	public void startMediaRecorder() {
		System.out.println("进入了摄像的初始化方法");
		String path = Address.VIDEOPATH;
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
		try {
			videoFile = File.createTempFile("vedio_", ".3GP", new File(path));// 以指定的前缀和后缀创建一个临时的视频输出路径
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());// 设置预览
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 设置视频来源
																			// (一般为照相机)
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置音频来源
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 设置视频的输出格式(设置为3GP格式)
			mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// 设置视频编码方式（H263编码方式）
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置音频的编码方式
			mediaRecorder.setVideoSize(176, 144);// 设置视频大小,必须放在视频的编码和格式之后否则报错
			mediaRecorder.setVideoFrameRate(15);// 视频帧大小，必须放在视频的编码和格式之后否则报错
			mediaRecorder.setOutputFile(videoFile.getAbsolutePath());// 设置录制器的文件保留路径
																		// /mnt/sdcard/customVideo
			selectUrl = videoFile.getAbsolutePath();
			mediaRecorder.prepare();
			mediaRecorder.start();
			weprtVideoRecordeLayout.setVisibility(View.VISIBLE);
			currentTime = System.currentTimeMillis();
			new Thread(r).start();
			isRecording = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopMediaRecorder() {
		if (mediaRecorder == null) {
			return;
		}
		mediaRecorder.stop();// 停止
		isRecording = false;
		isStop = true;
		mediaRecorder.release();// 释放资源
		mediaRecorder = null;

		int size = getCurrentMedia(mediaSelect);
		size++;
		Editor edit = mediaSelect.edit();
		edit.putString("video" + size, selectUrl);
		edit.commit();
	}

	Runnable r = new Runnable() {
		@Override
		public void run() {
			if (!isStop) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						long num = System.currentTimeMillis() - currentTime;
						recordeTime.setText(ServiceData.getStr(ServiceData
								.getTime(1, num))
								+ ":"
								+ ServiceData.getStr(ServiceData
										.getTime(2, num)));
					}
				});
			}
			new Thread(r).start();
		}
	};

	public void initCamera() {
		if (!isPreView) {
			camera = Camera.open();// 实例化摄像头
			if (camera != null) {
				Camera.Parameters params = camera.getParameters();
				params.setPreviewFrameRate(ServiceData
						.getPreviewFrameRates(params));// 每秒显示帧数
				params.setPreviewSize(
						ServiceData.getPreviewSizes(params).width,
						ServiceData.getPreviewSizes(params).height);
				params.setPictureSize(
						ServiceData.getPictureSizes(params).width,
						ServiceData.getPictureSizes(params).height);
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

	public int getCurrentMedia(SharedPreferences share) {
		Map<String, String> map = (Map<String, String>) share.getAll();
		return map.size();
	}
}

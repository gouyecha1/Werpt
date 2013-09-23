package com.werpt;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

public class PicStoreActivity extends Activity implements OnScrollListener,
		OnGestureListener, OnTouchListener {
	/** 当前屏幕上下文 **/
	private Context context;
	/** 手势监听器 **/
	private GestureDetector gestureDetector;
	/** 素材适配器 **/
	private CustomPictureAdaper pictureAdapter;
	/** 显示素材的GridView **/
	private GridView gridView;

	/** GridView 第一项ID **/
	private int firstItem;
	/** GridView 当前显示的项目总数 **/
	private int lastItem;
	/** GridView 划动方向，true表示向下划动，false表示向上划动 **/
	private boolean down = false;
	private ContentResolver cr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_store);
		context = this;
		gridView = (GridView) findViewById(R.id.gridview);
		pictureAdapter = new CustomPictureAdaper();
		gridView.setAdapter(pictureAdapter);
		gridView.setOnScrollListener(this);
		gridView.setOnTouchListener(this);
		gestureDetector = new GestureDetector(context, this);
		// 初始化GridView
		new InitAdapterTask().execute();

		new CustomLoadTask().execute();

	}

	/** 素材在媒体库中的最大ID **/
	private int maxID;
	/** 素材在媒体库中的最小ID **/
	private int minID;
	/** 每次最大加载 媒体文件数目 **/
	private final int MaxLoadNumber = 22;
	/** 屏幕允许存在媒体数目,当适配器中的数据集合大于此值时，将会清除多余的 **/
	private final int MaxNumber = 30;
	/** 素材当前最小ID **/
	private int currentMinID = 0;
	/** 素材当前最大ID **/
	private int currentMaxID = 0;
	/** 是否正在加载 **/
	private boolean loading = false;
	/** 存储适配器中数据集合ID和素材ID的 Map **/
	private Map<Integer, Integer> dataList = new HashMap<Integer, Integer>();

	/**
	 * 初始化适配器数据集合的任务类
	 * 
	 * @author YuanLiangFeng 袁良锋
	 *         <p>
	 *         at 2011-08-05
	 *         </p>
	 */
	class InitAdapterTask extends AsyncTask<Object, CustomBitmap, Object> {
		@Override
		protected Object doInBackground(Object... params) {
			String[] projection = { Images.Media._ID };
			cr=getContentResolver();
			Cursor cursor = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
					projection, null, null, Images.Media._ID + " DESC");
			int idIndex = cursor.getColumnIndexOrThrow(Images.Media._ID);
			while (cursor.moveToNext()) {
				int imageID = cursor.getInt(idIndex);
				publishProgress(new CustomBitmap(null, imageID));
			}
			cursor.moveToFirst();
			maxID = cursor.getInt(idIndex);
			cursor.moveToLast();
			minID = cursor.getInt(idIndex);
			return null;
		}

		@Override
		protected void onProgressUpdate(CustomBitmap... values) {
			for (CustomBitmap customBitmap : values) {
				pictureAdapter.itemList.add(customBitmap);
				dataList.put(customBitmap.getImageID(),
						pictureAdapter.itemList.size() - 1);
				pictureAdapter.notifyDataSetChanged();
			}
			super.onProgressUpdate(values);
		}

	}

	/**
	 * 自定义 素材加载任务类
	 * 
	 * @author YuanLiangFeng 袁良锋
	 *         <p>
	 *         at 2011-08-05
	 *         </p>
	 */
	class CustomLoadTask extends AsyncTask<Integer, CustomBitmap, Object> {

		/** true表示向前查询，否则表示向前查询 **/
		private boolean before;

		@Override
		protected Object doInBackground(Integer... params) {
			Bitmap image = null;
			String selection;

			if (params.length == 0) {
				selection = Images.Media._ID + ">" + 0;
			} else {
				selection = Images.Media._ID + ">=" + params[0] + " AND "
						+ Images.Media._ID + "<=" + params[1];
			}
			System.out.println("查询条件为:" + selection);
			String[] projection = { Images.Media._ID, Images.Media.DATA,
					Images.Media.DISPLAY_NAME };
			
			Cursor cursor = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
					projection, selection, null, Images.Media._ID + " DESC");
			int count = cursor.getCount();
			if (count > MaxLoadNumber && params.length == 0) {
				count = MaxLoadNumber;
			}
			for (int i = 0; i < count; i++) {
				cursor.moveToPosition(i);
				int imageID = cursor.getInt(cursor
						.getColumnIndexOrThrow(Images.Media._ID));
				if (i == 0 && before || i == 0 && currentMaxID == 0) {
					currentMaxID = imageID;
				}
				if (i == count - 1 && !before || i == count - 1
						&& currentMinID == 0) {
					currentMinID = imageID;
				}
				Cursor thumbnailsCursor = managedQuery(
						Images.Thumbnails.EXTERNAL_CONTENT_URI, null,
						Images.Thumbnails.IMAGE_ID + "=?",
						new String[] { String.valueOf(imageID) }, null);
				if (thumbnailsCursor.moveToFirst()) {
					int thumbnailsID = thumbnailsCursor.getInt(thumbnailsCursor
							.getColumnIndexOrThrow(Images.Thumbnails._ID));
					Uri uri = Uri.withAppendedPath(
							Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
									+ thumbnailsID);
					try {
						image = BitmapFactory.decodeStream(getContentResolver()
								.openInputStream(uri));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						thumbnailsCursor.close();
					}
				}
				if (image != null) {
					publishProgress(new CustomBitmap(image, imageID));
				}
			}
			cursor.close();
			loading = false;
			return null;
		}

		@Override
		protected void onProgressUpdate(CustomBitmap... values) {
			for (CustomBitmap bitmap : values) {
				int size = dataList.get(bitmap.getImageID());
				pictureAdapter.itemList.set(size, bitmap);
				pictureAdapter.notifyDataSetChanged();
			}
			super.onProgressUpdate(values);
		}

	}

	class CustomPictureAdaper extends BaseAdapter {
		private List<CustomBitmap> itemList = new ArrayList<CustomBitmap>();
		private final int resourceID = R.layout.choose_resource_item_with_checkbox;
		private final int ImageID = R.id.choose_resource_item_imageView;
		private final int checkBoxID = R.id.choose_resource_item_checkBox;

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(resourceID,
						null);
			}
			CheckBox checkBox = (CheckBox) convertView.findViewById(checkBoxID);
			CustomBitmap customBitmap = itemList.get(position);
			if (customBitmap.isExistBitmap()) {
				((ImageView) convertView.findViewById(ImageID))
						.setImageBitmap(customBitmap.getBitmap());
				checkBox.setVisibility(View.VISIBLE);
				checkBox.setTag(position);
				checkBox.setOnCheckedChangeListener(customOnCheckChangeListener);
				// 是否将选择框选中
				if (customBitmap.isChoose()) {
					checkBox.setChecked(true);
				} else {
					checkBox.setChecked(false);
				}
			} else {
				((ImageView) convertView.findViewById(ImageID))
						.setImageBitmap(null);
				checkBox.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

		OnCheckedChangeListener customOnCheckChangeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int position = (Integer) buttonView.getTag();
				if (isChecked) {
					itemList.get(position).setChoose(true);
				} else {
					itemList.get(position).setChoose(false);
				}
			}
		};

	}

	class CustomBitmap extends SimpleOnGestureListener {

		/** 素材缩略图 **/
		private Bitmap bitmap;
		/** 是否被选中 **/
		private boolean choose;
		/** 素材路径 **/
		private String path;
		/** 素材在素材库中的ID **/
		private int imageID;

		private CustomBitmap(Bitmap bitmap, int imageID) {
			this.bitmap = bitmap;
			this.imageID = imageID;
		}

		/**
		 * 是否包含缩略图
		 * 
		 * @return true表示包含，否则表示不包含
		 */
		public boolean isExistBitmap() {
			if (bitmap == null) {
				return false;
			} else {
				return true;
			}
		}

		public Bitmap getBitmap() {
			return bitmap;
		}

		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public boolean isChoose() {
			return choose;
		}

		public void setChoose(boolean choose) {
			this.choose = choose;
		}

		public int getImageID() {
			return imageID;
		}

		public void setImageID(int imageID) {
			this.imageID = imageID;
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			// 当前任务类处于未加载状态 并且适配器数据集合有值(初始时适配器数据集合为空)
			if (pictureAdapter.itemList.size() > 0 && !loading) {
				// 向后查询
				if (firstItem >= currentMaxID && down && firstItem != maxID) {
					new CustomLoadTask().execute(new Integer[] { firstItem,
							currentMaxID + MaxLoadNumber });
					System.out.println(firstItem + "==" + currentMaxID
							+ MaxLoadNumber + "==查询ID更大的素材,最大ID为:" + maxID);
					loading = true;
				} else if (lastItem <= currentMinID && !down
						&& currentMinID != minID) {
					new CustomLoadTask().execute(new Integer[] { lastItem,
							currentMinID });
					System.out.println(lastItem + "==" + currentMinID
							+ "==查询ID更小的素材,最小ID为" + minID);
					loading = true;
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (pictureAdapter.itemList.size() > 0) {
			firstItem = pictureAdapter.itemList.get(firstVisibleItem)
					.getImageID();
			lastItem = pictureAdapter.itemList.get(
					firstVisibleItem + visibleItemCount - 1).getImageID();
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// 判断是否向下划动
		if (distanceY > 0) {
			down = false;
		} else {
			down = true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

}

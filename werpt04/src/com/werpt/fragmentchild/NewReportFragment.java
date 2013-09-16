package com.werpt.fragmentchild;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.werpt.R;
import com.werpt.WerptDetailActivity;
import com.werpt.bean.Werpt;
import com.werpt.costant.Address;
import com.werpt.util.DateFormat;
import com.werpt.util.ImageLazyLoad;
import com.werpt.util.ServiceData;

public class NewReportFragment extends Fragment implements IXListViewListener,
		OnItemClickListener {
	private XListView xListView;
	private LinearLayout loading;
	private List<Werpt> list = null;
	private int pageCode = 1, pageSize = 5;
	private String result;
	private ReportAdapter adapter;
	private ImageLazyLoad load = new ImageLazyLoad();
	private LayoutInflater inflater;
	private Thread getSimpleWerptThread;
	private int flag = 0;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (flag == 0) {
				if (msg.what == 1) {
					list = getWeiJiSimpleList();
					adapter.notifyDataSetChanged(); 
					loading.setVisibility(View.GONE);
				}
			} else if (flag == 1) {
				list = getWeiJiSimpleList();
				adapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT)
						.show();
			} else if (flag == 2) {
				List<Werpt> moreList = getWeiJiSimpleList();
				for (int i = 0; i < moreList.size(); i++) {
					list.add(moreList.get(i));
				}
				adapter.notifyDataSetChanged();
			}
			
		}

	};;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View convertView = inflater.inflate(R.layout.new_report, null);
		xListView = (XListView) convertView.findViewById(R.id.xListView);
		loading = (LinearLayout) convertView.findViewById(R.id.loading);
		xListView.setXListViewListener(this);
		xListView.setPullLoadEnable(true);
		adapter = new ReportAdapter();
		xListView.setAdapter(adapter);
		xListView.setOnItemClickListener(this);
		new GetDataTask().execute();
		return convertView;

	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			result = ServiceData.getTaskData(pageCode, pageSize,
					Address.WEIJISIMPLEINFO);
			Message msg = handler.obtainMessage();
			if (!result.equals("0")) {
				msg.what = 1;
			} else {
				msg.what = 0;
			}

			handler.sendMessage(msg);
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			onLoad();
			
			super.onPostExecute(result);
		}
	}

	private void onLoad() {
		// TODO Auto-generated method stub
		xListView.stopLoadMore();
		xListView.stopRefresh();
		xListView.setRefreshTime(DateFormat.getCurTime());
	}

	@Override
	public void onLoadMore() {
		flag = 2;
		pageCode++;
		new GetDataTask().execute();

	}

	@Override
	public void onRefresh() {
		flag = 1;
		pageCode = 1;
		new GetDataTask().execute();

	}

	public List<Werpt> getWeiJiSimpleList() {
		List<Werpt> list = new ArrayList<Werpt>();
		try {
			JSONArray arr = new JSONArray(result);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				int id = obj.getInt("id");
				String uname = obj.getString("username");
				String nickname = obj.getString("nickname");
				int uid = obj.getInt("uid");
				String title = obj.getString("title");
				String content = obj.getString("content");
				String thumb = obj.getString("thumb");
				String addtime = obj.getString("addtime");
				int comment = obj.getInt("comments");
				Werpt w = new Werpt(id, uid, title, content, thumb, addtime,
						comment, 0, uname, nickname);
				list.add(w);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

	static class ViewHolder {
		TextView nickname;
		TextView addtime;
		ImageView pic;
		TextView title;
		TextView content;
		TextView comment;
	}

	class ReportAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (list == null) {
				return 0;
			}
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup container) {
			ViewHolder holder=null;

			Werpt werpt = list.get(position);

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.new_report_item, null);
				holder.nickname = (TextView) convertView
						.findViewById(R.id.nickname);
				holder.addtime = (TextView) convertView
						.findViewById(R.id.addtime);
				holder.pic = (ImageView) convertView.findViewById(R.id.pic);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.comment = (TextView) convertView
						.findViewById(R.id.comment);
				
				holder.nickname.setText(werpt.getNickname());
				holder.addtime.setText(werpt.getAddtime());
				Bitmap bit = load.getBitmap(holder.pic,
						Address.WEIJIIMAGE + werpt.getThumb());
				if (bit != null) {
					holder.pic.setImageBitmap(bit);
				}
				holder.title.setText(werpt.getTitle());
				holder.content.setText(Html.fromHtml(werpt.getContent()));
				holder.comment.setText(werpt.getComments() + "");
				
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

				holder.nickname.setText(werpt.getNickname());
				holder.addtime.setText(werpt.getAddtime());
				Bitmap bit = load.getBitmap(holder.pic,
						Address.WEIJIIMAGE + werpt.getThumb());
				if (bit != null) {
					holder.pic.setImageBitmap(bit);
				}
				holder.title.setText(werpt.getTitle());
				holder.content.setText(Html.fromHtml(werpt.getContent()));
				holder.comment.setText(werpt.getComments() + "");
			}
			
			
			return convertView;
		}
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent(getActivity(), WerptDetailActivity.class);
		intent.putExtra("wid", list.get(position - 1).getId());
		startActivity(intent);
	}

}

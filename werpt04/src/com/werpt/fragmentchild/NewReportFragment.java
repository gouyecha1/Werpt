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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.werpt.R;
import com.werpt.WerptDetailActivity;
import com.werpt.bean.Werpt;
import com.werpt.costant.Adress;
import com.werpt.util.DateFormat;
import com.werpt.util.ImageLazyLoad;
import com.werpt.util.ServiceData;

public class NewReportFragment extends Fragment implements IXListViewListener,
		OnItemClickListener {
	private XListView xListView;
	private List<Werpt> list;
	private int pageCode = 1, pageSize = 5;
	private String result;
	private ReportAdapter adapter;
	private ImageLazyLoad load = new ImageLazyLoad();
	private LayoutInflater inflater;
	private Thread getSimpleWerptThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSimpleWerptThread = new Thread(getSimpleWeiJi);
		getSimpleWerptThread.start();
		try {
			getSimpleWerptThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		list = getWeiJiSimpleList();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View convertView = inflater.inflate(R.layout.new_report, null);
		xListView = (XListView) convertView.findViewById(R.id.xListView);
		xListView.setXListViewListener(this);
		xListView.setPullLoadEnable(true);
		adapter = new ReportAdapter();
		xListView.setAdapter(adapter);
		xListView.setOnItemClickListener(this);
		return convertView;

	}

	@Override
	public void onLoadMore() {
		List<Werpt> moreList = new ArrayList<Werpt>();
		pageCode++;
		getSimpleWerptThread = new Thread(getSimpleWeiJi);
		getSimpleWerptThread.start();
		try {
			getSimpleWerptThread.join();
			moreList = getWeiJiSimpleList();
			for (int i = 0; i < moreList.size(); i++) {
				list.add(moreList.get(i));
			}
			adapter.notifyDataSetChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		xListView.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		getSimpleWerptThread = new Thread(getSimpleWeiJi);
		getSimpleWerptThread.start();
		try {
			getSimpleWerptThread.join();
			list = getWeiJiSimpleList();
			adapter.notifyDataSetChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		xListView.stopRefresh();
		Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
		xListView.setRefreshTime(DateFormat.getCurTime());
	}

	Runnable getSimpleWeiJi = new Runnable() {
		@Override
		public void run() {
			result = ServiceData.getTaskData(pageCode, pageSize,
					Adress.WEIJISIMPLEINFO);
		}
	};

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

	class ReportAdapter extends BaseAdapter {

		@Override
		public int getCount() {
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
			View view = null;
			if (convertView == null) {
				view = inflater.inflate(R.layout.new_report_item, null);
			} else {
				view = convertView;
			}
			// ImageView userphoto = (ImageView)
			// view.findViewById(R.id.userphoto);
			TextView nickname = (TextView) view.findViewById(R.id.nickname);
			TextView addtime = (TextView) view.findViewById(R.id.addtime);
			ImageView pic = (ImageView) view.findViewById(R.id.pic);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView content = (TextView) view.findViewById(R.id.content);
			TextView comment = (TextView) view.findViewById(R.id.comment);
			Werpt werpt=list.get(position);
			nickname.setText(werpt.getNickname());
			addtime.setText(werpt.getAddtime());
			Bitmap bit = load.getBitmap(pic,
					Adress.WEIJIIMAGE + werpt.getThumb());
			if (bit != null) {
				pic.setImageBitmap(bit);
			}
			title.setText(werpt.getTitle());
			content.setText(Html.fromHtml(werpt.getContent()));
			comment.setText(werpt.getComments() + "");
			return view;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent(getActivity(), WerptDetailActivity.class);
		intent.putExtra("wid", list.get(position).getId());
		startActivity(intent);
	}

}

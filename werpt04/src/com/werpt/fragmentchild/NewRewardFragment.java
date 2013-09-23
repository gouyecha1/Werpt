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
import com.werpt.TaskDetailActivity;
import com.werpt.costant.MyAddress;
import com.werpt.util.DateFormat;
import com.werpt.util.ImageLazyLoad;
import com.werpt.util.ServiceData;
import com.werpt.util.Task;

public class NewRewardFragment extends Fragment implements IXListViewListener,
		OnItemClickListener {
	private XListView xListView;
	private LinearLayout loading;
	private int pageCode = 1, pageSize = 5;
	private String result;
	private ImageLazyLoad load = new ImageLazyLoad();
	private LayoutInflater inflater;
	private RewardAdapter adapter;
	private List<Task> list;
	private int flag = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater=inflater;
		View convertView = inflater.inflate(R.layout.new_reward, null);
		xListView = (XListView) convertView.findViewById(R.id.xListView);
		loading = (LinearLayout) convertView.findViewById(R.id.loading);
		xListView.setXListViewListener(this);
		xListView.setPullLoadEnable(true);
		adapter = new RewardAdapter();
		xListView.setAdapter(adapter);
		xListView.setOnItemClickListener(this);
		new GetDataTask().execute();
		return convertView;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (flag == 0) {
				if (msg.what == 1) {
					list = getTaskSimpleList();
					adapter.notifyDataSetChanged();
					loading.setVisibility(View.GONE);
				}
			} else if (flag == 1) {
				list = getTaskSimpleList();
				adapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT)
						.show();
			} else if (flag == 2) {
				List<Task> moreList = getTaskSimpleList();
				for (int i = 0; i < moreList.size(); i++) {
					list.add(moreList.get(i));
				}
				adapter.notifyDataSetChanged();
			}

		}

	};

	public List<Task> getTaskSimpleList() {
		List<Task> list = new ArrayList<Task>();
		try {
			JSONArray arr = new JSONArray(result);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				int id = obj.getInt("id");
				String title = obj.getString("title");
				String pic = obj.getString("pic");
				String endTime = obj.getString("endtime");
				String teams = obj.getString("teams");
				// System.out.println("输出的悬赏的信息是"+id+"-->"+title+"-->"+pic+"-->"+endTime+"-->"+teams);
				Task t = new Task(id, title, pic, endTime, teams);
				// System.out.println("输出的悬赏的信息是"+t.getId()+"-->"+t.getTitle()+"-->"+t.getPic()+"-->"+t.getEndTime()+"-->"+t.getTeams());
				list.add(t);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			result = ServiceData.getTaskData(pageCode, pageSize,
					MyAddress.GETTASKSIMPLEINFO);
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
		xListView.stopLoadMore();
		xListView.stopRefresh();
		xListView.setRefreshTime(DateFormat.getCurTime());
	}

	public static final class ViewHolder {
		public ImageView pic;
		public TextView title;
		public TextView deadline;
		public TextView group;
	}

	class RewardAdapter extends BaseAdapter {

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
			ViewHolder holder = null;
			Task task = list.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.new_reward_item, null);
				holder.pic = (ImageView) convertView.findViewById(R.id.pic);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.deadline = (TextView) convertView
						.findViewById(R.id.deadline);
				holder.group = (TextView) convertView.findViewById(R.id.group);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Bitmap bit = load.getBitmap(holder.pic, MyAddress.WEIJIIMAGE
					+ task.getPic());

			holder.pic.setImageBitmap(bit);
			holder.title.setText(task.getTitle());
			holder.deadline.setText(task.getEndTime());
			holder.group.setText(task.getTeams());
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
		intent.putExtra("taskId", list.get(position-1).getId());
		startActivity(intent);
	}

	@Override
	public void onRefresh() {
		flag = 1;
		pageCode = 1;
		new GetDataTask().execute();

	}

	@Override
	public void onLoadMore() {
		flag = 2;
		pageCode++;
		new GetDataTask().execute();
	}

}

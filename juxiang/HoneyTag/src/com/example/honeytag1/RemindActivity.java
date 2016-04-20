package com.example.honeytag1;

import info.wangchen.simplehud.SimpleHUD;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.FinalBitmap;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.config.MySharedPreference;
import com.example.dialog.SpotsDialog;
import com.example.dto.RemindDTO;
import com.example.dto.User;
import com.example.fragment.FragmentShouYe;
import com.example.fragment.FragmentLanMu;
import com.example.honeytag1.R;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.MyApplication.PushListener;
import com.example.utils.PublicStaticURL;
import com.example.utils.Utils;
import com.example.view.CircleImageView2;
import com.example.view.RefreshLayout;
import com.example.view.RefreshLayout.OnLoadListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 通知页面
 * 
 * @author Administrator
 * 
 */
public class RemindActivity extends BaseActivity implements OnClickListener, OnRefreshListener, OnItemClickListener {

	private LinearLayout remind_fanhui; // 返回按钮
	private ImageButton remind_btn; // 返回按钮
	private ListView remind_list;
	RefreshLayout rswipe_refresh_widget; // 下拉刷新
	List<RemindDTO> remindlist = new ArrayList<RemindDTO>();
	List<RemindDTO> unread_list = new ArrayList<RemindDTO>();
	List<RemindDTO> list = null;
	RemindMyadapter myadapter;
	SpotsDialog spotsDialog; // dialog
	TextView gone;
	TextView text_quanbu;
	LinearLayout rem_wuwife;
	ImageButton rem_wuwife_btn;
	private String uid;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		User mUser = MySharedPreference.readUser(RemindActivity.this);
		uid = mUser.getUid();
		MyApplication.getInstance().addActivity(this);
		myadapter = new RemindMyadapter(RemindActivity.this);
		remind_list.setAdapter(myadapter);

		onRefresh();

	}

	public void loadXml() {
		setContentView(R.layout.activity_remind);
		rswipe_refresh_widget = (RefreshLayout) findViewById(R.id.rswipe_refresh_widget);
		rswipe_refresh_widget.setColorScheme(R.color.lanse, R.color.huise, R.color.lanse, R.color.huise);
		remind_list = (ListView) findViewById(R.id.remind_listview);
		remind_fanhui = (LinearLayout) findViewById(R.id.remind_fanhui);
		remind_btn = (ImageButton) findViewById(R.id.remind_btn);
		gone = (TextView) findViewById(R.id.gone);
		text_quanbu = (TextView) findViewById(R.id.text_quanbu);
		rem_wuwife = (LinearLayout) findViewById(R.id.rem_wuwife);
		rem_wuwife_btn = (ImageButton) findViewById(R.id.rem_wuwife_btn);

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		rswipe_refresh_widget.setOnRefreshListener(this);
		rswipe_refresh_widget.setSize(SwipeRefreshLayout.DEFAULT);
		rswipe_refresh_widget.setProgressViewEndTarget(true, 100);
		remind_fanhui.setOnClickListener(this);
		remind_btn.setOnClickListener(this);
		remind_list.setOnItemClickListener(this);
		text_quanbu.setOnClickListener(this);
		rem_wuwife_btn.setOnClickListener(this);

		spotsDialog = new SpotsDialog(RemindActivity.this);
		// 获取数据
		rswipe_refresh_widget.setRefreshing(true);
		// 这句话是为了，第一次进入页面的时候显示加载进度条
		rswipe_refresh_widget.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub想·
		switch (v.getId()) {
		case R.id.remind_fanhui:
			// MyApplication.getInstance().popActivity(RemindActivity.this);

			finish();

			break;
		case R.id.remind_btn:
			finish();
			break;
		case R.id.rem_wuwife_btn:
			rem_wuwife.setVisibility(View.GONE);
			rem_wuwife_btn.setVisibility(View.GONE);
			spotsDialog.show();
			onRefresh();
			break;
		case R.id.text_quanbu:
			ZHUANGTAI(PublicStaticURL.TIXINGZHUANGTAIQB + "&uid=" + PublicStaticURL.userid);
			break;
		}
	}

	// 刷新监听
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		SHUAXINGETTONGZHI(PublicStaticURL.TIXING + "&uid=" + uid);
		Log.i("======", PublicStaticURL.userid);
	}

	// // 加载更多
	// @Override
	// public void onLoad() {
	// // TODO Auto-generated method stub
	// Remind_details_page++;
	// RemindLoad(PublicStaticURL.TIXING + "&uid=" + uid + "&p=" +
	// Remind_details_page);
	//
	// }

	// 刷新提醒列表数据

	private void SHUAXINGETTONGZHI(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				rswipe_refresh_widget.setRefreshing(false);
				spotsDialog.dismiss();
				rem_wuwife.setVisibility(View.VISIBLE);
				rem_wuwife_btn.setVisibility(View.VISIBLE);
				SimpleHUD.showInfoMessage(RemindActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("消息列表", str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						spotsDialog.dismiss();
						rswipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						if (result.equals("")) {
							rswipe_refresh_widget.setRefreshing(false);
							remind_list.setVisibility(View.GONE);
							gone.setVisibility(View.VISIBLE);
						} else {
							Gson gson = new Gson();
							Type type = new TypeToken<List<RemindDTO>>() {
							}.getType();
							remindlist = gson.fromJson(result, type);
							unread_list.clear();
							for (int i = 0; i < remindlist.size(); i++) {
								if (remindlist.get(i).getIs_read().equals("0")) {// 0代表未读
									unread_list.add(remindlist.get(i));
								}

							}

							if (unread_list.size() == 0) {
								text_quanbu.setTextColor(getResources().getColor(R.color.tijiao_hei));
								text_quanbu.setClickable(false);
								FragmentShouYe.main_tab_unread_tv.setBackgroundResource(R.drawable.circle_gray);// 更改主页通知状态
								FragmentShouYe.main_tab_unread_tv.setText("0");
								FragmentShouYe.main_tab_unread_tv.setTextColor(Color.GRAY);
							} else {
								text_quanbu.setTextColor(Color.BLACK);
								text_quanbu.setClickable(true);
								FragmentShouYe.main_tab_unread_tv.setBackgroundResource(R.drawable.circle_red);// 更改主页通知状态
								FragmentShouYe.main_tab_unread_tv.setText(unread_list.size() + "");
								FragmentShouYe.main_tab_unread_tv.setTextColor(Color.RED);
							}

							myadapter.notifyDataSetChanged();
							rswipe_refresh_widget.setRefreshing(false);

						}
					}

				} catch (Exception e) {
					rswipe_refresh_widget.setRefreshing(false);
					SimpleHUD.showInfoMessage(RemindActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// // 加载更多
	//
	// private void RemindLoad(String str) {
	// HttpUtils httpUtils = new HttpUtils();
	// httpUtils.configCurrentHttpCacheExpiry(0);
	// httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
	// @Override
	// public void onStart() {
	// super.onStart();
	//
	// }
	//
	// @Override
	// public void onFailure(com.lidroid.xutils.exception.HttpException arg0,
	// String arg1) {
	// // TODO Auto-generated method stub
	//
	// rswipe_refresh_widget.setLoading(false);
	// SimpleHUD.showInfoMessage(RemindActivity.this,
	// getString(R.string.server_connect_failed));
	//
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	//
	// String str1 = responseInfo.result;// 接口返回的数据
	// Logger.i(str1);
	// JSONObject json;
	// try {
	// json = new JSONObject(str1);
	// String code = json.getString("code");
	// if ("2".equals(code)) {
	//
	// String result = json.getString("result");
	// if ("".equals(result)) {
	// Toast.makeText(RemindActivity.this, getString(R.string.not_more_data),
	// Toast.LENGTH_SHORT)
	// .show();
	// rswipe_refresh_widget.setLoading(false);
	// } else {
	// Gson gson = new Gson();
	// Type type = new TypeToken<List<RemindDTO>>() {
	// }.getType();
	// list = gson.fromJson(result, type);
	// // repeat_list.clear();
	// for (int i = 0; i < remindlist.size(); i++) {
	// if (remindlist.get(i).getIs_read().equals("0")) {// 0代表未读
	// unread_list.add(remindlist.get(i));
	// }
	// }
	// remindlist.addAll(list);
	// myadapter.notifyDataSetChanged();
	// rswipe_refresh_widget.setLoading(false);
	// }
	//
	// }
	//
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// }
	//
	// });
	//
	// }
	// 将未读状态设为已读

	private void ZHUANGTAI(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				rswipe_refresh_widget.setRefreshing(false);
				spotsDialog.dismiss();
				rem_wuwife.setVisibility(View.VISIBLE);
				rem_wuwife_btn.setVisibility(View.VISIBLE);
				SimpleHUD.showInfoMessage(RemindActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("提醒数据：" + str1);
				onRefresh();
			}

		});

	}

	/*
	 * 将已读和未读进行分组并排序
	 */
	// public List<RemindDTO> sort(List<RemindDTO> list){
	// List<RemindDTO> mList=new ArrayList<RemindDTO>();
	// if (list.get(arg0).getIs_read().equals("1")) {// 已读
	// convertView.setAlpha(0.3f);
	// }
	//
	//
	// }

	public class RemindMyadapter extends BaseAdapter {

		FinalBitmap finalBitmap;

		public RemindMyadapter(Context context) {
			super();
			finalBitmap = FinalBitmap.create(context);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return remindlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return remindlist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			HolderView holderView;
			if (convertView == null) {
				convertView = LayoutInflater.from(RemindActivity.this).inflate(R.layout.remind_item, null);
				holderView = new HolderView();

				holderView.redmind_image = (CircleImageView2) convertView.findViewById(R.id.redmind_image); // 头像
				holderView.remind_title = (TextView) convertView.findViewById(R.id.remind_title); // 昵称
				holderView.remind_content = (TextView) convertView.findViewById(R.id.remind_content); // 内容
				holderView.remind_time = (TextView) convertView.findViewById(R.id.remind_time);// time
				holderView.view_list_divide = (View) convertView.findViewById(R.id.view_list_divide);// time

				convertView.setTag(holderView);
			} else {
				holderView = (HolderView) convertView.getTag();
			}
			if (remindlist.size() <= arg0 + 1) {
				holderView.view_list_divide.setVisibility(View.GONE);
			} else {
				if (remindlist.get(arg0).getIs_read().equals("0")
						&& remindlist.get(arg0 + 1).getIs_read().equals("1")) {
					holderView.view_list_divide.setVisibility(View.VISIBLE);
				} else {
					holderView.view_list_divide.setVisibility(View.GONE);
				}
			}

			if (remindlist.get(arg0).getIs_read().equals("1")) {// 已读
				convertView.setAlpha(0.3f);
			} else {
				convertView.setAlpha(1f);
			}
			if (!remindlist.get(arg0).getAvatar().equals("no_pic")) {
				finalBitmap.display(holderView.redmind_image, remindlist.get(arg0).getAvatar());
			}
			holderView.remind_title.setText(remindlist.get(arg0).getPost_title());
			holderView.remind_content.setText(remindlist.get(arg0).getContent());
			holderView.remind_time.setText(Utils.timeSpan(remindlist.get(arg0).getPost_date()));

			return convertView;
		}

	}

	// 定义view缓存对象
	static class HolderView {
		TextView remind_title, remind_content, remind_time;
		CircleImageView2 redmind_image;
		View view_list_divide;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (remindlist.size() == 0) {
			return;
		}
		if (arg2 > remindlist.size()) {// 防止点到加载更多导致崩溃
			if (arg2 - 1 == remindlist.size()) {
				return;
			}
		}

		if (arg2 >= remindlist.size()) {
			return;
		}
		RemindDTO rDto = remindlist.get(arg2);
		PublicStaticURL.pid = rDto.getPid(); // 将文章id存上 用调去详情
		PublicStaticURL.remind_cid = rDto.getCid(); // 将文章id存上 用调去详情
		PublicStaticURL.remindto_cid = rDto.getTo_cid(); // 将文章id存上 用调去详情
		PublicStaticURL.ColumnTitle = rDto.getPost_title();
		if (rDto.getPost_title().contains("栏目：")) {
			PublicStaticURL.ColumnTitle = rDto.getPost_title().substring(4);
		}
		int type = Integer.parseInt(rDto.getType());
		ZHUANGTAI(PublicStaticURL.TIXINGZHUANGTAI + "&coid=" + rDto.getCoid());
		LogUtils.e("+++++++++++" + PublicStaticURL.pid);
		LogUtils.e("+++++++++++" + PublicStaticURL.remind_cid);
		LogUtils.e("+++++++++++" + type);
		switch (type) {
		case 1:// 话题被评论或评论被回复
			if (PublicStaticURL.remindto_cid == null || PublicStaticURL.remindto_cid.equals("")) {
				intent = new Intent(RemindActivity.this, TopicDetailsActivity.class);
				intent.putExtra("flag", "TX");
			} else {
				intent = new Intent(RemindActivity.this, TopicDetailsActivity.class);
				intent.putExtra("flag", "TX_WD");
			}
			break;
		case 3:// 话题被栏目收录
		case 5:// 关注的栏目有更新
			intent = new Intent(RemindActivity.this, ColumnDetailsActivity.class);
			intent.putExtra("id", rDto.getTid());
			break;

		}

		startActivity(intent);
		// sendBroadcast(new Intent("com.example.set.referesh"));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MyApplication.getInstance().setPushListener(this);
		umengResume(this, getClass().toString());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		umengPause(this, getClass().toString());
	}

	@Override
	public void notice(String str) {

		onRefresh();
		// if (TextUtils.isEmpty(str)) {
		// return;
		// }
		//
		// try {
		// JSONObject object = new JSONObject(str);
		// String type = object.getString("type");
		// if (type.equals("1")) {
		// String message = object.getString("message");
		// RemindDTO mRemindDTO = RemindDTO.fromJson(message);
		// String aa = mRemindDTO.toString();
		// if (mRemindDTO != null) {
		// remindlist = toHeavy(mRemindDTO, remindlist);
		// // remindlist.add(0, mRemindDTO);
		// myadapter.notifyDataSetChanged();
		// unread_list.add(mRemindDTO);
		// if (unread_list.size() == 0) {
		// text_quanbu.setTextColor(getResources().getColor(R.color.tijiao_hei));
		// text_quanbu.setClickable(false);
		// FragmentOne.main_tab_unread_tv.setBackgroundResource(R.drawable.circle_gray);//
		// 更改主页通知状态
		// FragmentOne.main_tab_unread_tv.setText("0");
		// FragmentOne.main_tab_unread_tv.setTextColor(Color.GRAY);
		// } else {
		// text_quanbu.setTextColor(Color.BLACK);
		// text_quanbu.setClickable(true);
		// FragmentOne.main_tab_unread_tv.setBackgroundResource(R.drawable.circle_red);//
		// 更改主页通知状态
		// FragmentOne.main_tab_unread_tv.setText(unread_list.size() + "");
		// FragmentOne.main_tab_unread_tv.setTextColor(Color.RED);
		// }
		// }
		// }
		//
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/*
	 * 去重
	 */

	// public void toHeavy(){
	//
	// }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public synchronized List<RemindDTO> toHeavy(RemindDTO mRemindDTO, List<RemindDTO> remindlist) {
		//
		// for (int i = 0; i < remindlist.size(); i++) {
		// if (remindlist.get(i).getMid().equals(mRemindDTO.getMid())) {
		// remindlist.remove(i);
		// remindlist.add(i, mRemindDTO);
		// break;
		// } else {
		// remindlist.add(0, mRemindDTO);
		// break;
		// }
		//
		// }
		if (remindlist.get(0).getCoid().equals(mRemindDTO.getCoid())) {
			remindlist.remove(0);
			remindlist.add(0, mRemindDTO);

		} else {
			remindlist.add(0, mRemindDTO);
		}

		return remindlist;

	}

}

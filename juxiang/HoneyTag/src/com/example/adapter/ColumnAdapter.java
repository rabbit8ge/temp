package com.example.adapter;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.example.config.MySharedPreference;
import com.example.dialog.SpotsDialog;
import com.example.dto.ColumnDTO;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.my.LoginActivity;
import com.example.utils.Logger;
import com.example.utils.PublicStaticURL;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import info.wangchen.simplehud.SimpleHUD;
import net.tsz.afinal.FinalBitmap;

public class ColumnAdapter extends BaseAdapter {
	private Context context;
	private List<ColumnDTO> list = new ArrayList<ColumnDTO>();
	private FinalBitmap finalBitmap;
	private SpotsDialog spotsDialog;
	private int p;

	private int[] colors = { R.color.column_gray, R.color.column_red, R.color.column_green, R.color.column_lightgreen };

	public ColumnAdapter(Context context) {

		this.context = context;
		spotsDialog = new SpotsDialog(context);
		finalBitmap = FinalBitmap.create(context);
	}

	public void reFresh(List<ColumnDTO> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if (convertView == null) {

			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.fragment3_item, null);
			holderView.mLinearAttention = (LinearLayout) convertView.findViewById(R.id.linear_attention);
			holderView.mTvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holderView.mTvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			holderView.mIvColumn = (ImageView) convertView.findViewById(R.id.iv_column);
			holderView.mIvNew = (ImageView) convertView.findViewById(R.id.iv_new);
			holderView.fragment3_item_image = (ImageView) convertView.findViewById(R.id.fragment3_item_image);
			//convertView.getBackground().setAlpha(0);
			//convertView.setAlpha(0.1f);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.mTvTitle.setText("「" + list.get(position).getTitle() + "」 ");
		holderView.mTvNumber.setText(list.get(position).getTitle_num());
		holderView.mTvNumber.setBackgroundColor(context.getResources().getColor(colors[position % 4]));
		if (list.get(position).getHaslike().equals("0")) {
			holderView.mIvColumn.setVisibility(View.VISIBLE);
			holderView.mIvColumn.setBackgroundResource(R.drawable.collect);

		} else if (list.get(position).getHaslike().equals("1")) {
			holderView.mIvColumn.setVisibility(View.VISIBLE);
			holderView.mIvColumn.setBackgroundResource(R.drawable.alreadycollect);
		} else {
			holderView.mIvColumn.setVisibility(View.INVISIBLE);
		}
		if (list.get(position).getHasnew().equals("0")) {
			holderView.mIvNew.setVisibility(View.INVISIBLE);

		} else if (list.get(position).getHasnew().equals("1")) {
			holderView.mIvNew.setVisibility(View.VISIBLE);
		} else {
			holderView.mIvNew.setVisibility(View.INVISIBLE);
		}
		finalBitmap.display(holderView.fragment3_item_image, list.get(position).getAvatar());

		holderView.mLinearAttention.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User mUser = MySharedPreference.readUser(context);
				if (mUser == null) {// 需要先登录才能取消栏目关注
					context.startActivity(new Intent(context, LoginActivity.class));
					return;
				}
				p = position;

				if (list.get(position).getHaslike().equals("0")) {
					RequestParams params = new RequestParams();
					params.addBodyParameter("uid", mUser.getUid());
					params.addBodyParameter("tid", list.get(position).getId());
					payAttentionColumn(PublicStaticURL.PAYATTENTIONCOLUMN, params);// 关注栏目
				} else {
					RequestParams params = new RequestParams();
					params.addBodyParameter("uid", mUser.getUid());
					params.addBodyParameter("tid", list.get(position).getId());

					cancelAttention(PublicStaticURL.CANCELATTENTION, params);// 取消栏目关注
				}

			}
		});
		return convertView;
	}

	// 定义view缓存对象
	static class HolderView {
		LinearLayout mLinearAttention;
		TextView mTvTitle, mTvNumber;
		ImageView fragment3_item_image, mIvColumn, mIvNew;
	}

	/*
	 * 关注栏目
	 */
	private void payAttentionColumn(String str, RequestParams params) {

		if (spotsDialog != null) {
			spotsDialog.show();
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);

		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {

				if (spotsDialog != null) {
					spotsDialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {// 栏目关注成功

						list.get(p).setHaslike("1");
						notifyDataSetChanged();
						SimpleHUD.showInfoMessage(context, context.getString(R.string.focusOnSuccess));
					}

					if ("3".equals(code)) {// 栏目关注失败
						SimpleHUD.showInfoMessage(context, context.getString(R.string.focusOnFail));
					}
					if ("4".equals(code)) {// 栏目已被关注
						list.get(p).setHaslike("1");
						notifyDataSetChanged();
						SimpleHUD.showInfoMessage(context, context.getString(R.string.columnHasBeenConcerned));
					}

				} catch (Exception e) {

					SimpleHUD.showInfoMessage(context, context.getString(R.string.focusOnFail));
					Log.e("message", "走catch了");
					e.printStackTrace();
				} finally {
					if (spotsDialog != null) {
						spotsDialog.dismiss();
					}

				}
			}

		});

	}

	/*
	 * 取消栏目关注
	 */
	private void cancelAttention(String str, RequestParams params) {

		if (spotsDialog != null) {
			spotsDialog.show();
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);

		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {

				if (spotsDialog != null) {
					spotsDialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {// 取消栏目关注成功
						list.get(p).setHaslike("0");
						notifyDataSetChanged();
						SimpleHUD.showInfoMessage(context, context.getString(R.string.cancelAttentionSuccess));
					}

					if ("3".equals(code)) {// 取消栏目关注失败
						SimpleHUD.showInfoMessage(context, context.getString(R.string.focusOnFail));
					}
					if ("4".equals(code)) {// 栏目没有关注过
						list.get(p).setHaslike("0");
						notifyDataSetChanged();
						SimpleHUD.showInfoMessage(context, context.getString(R.string.cancelAttentionFail));
					}

				} catch (Exception e) {

					SimpleHUD.showInfoMessage(context, context.getString(R.string.noAttention));
					Log.e("message", "走catch了");
					e.printStackTrace();
				} finally {
					if (spotsDialog != null) {
						spotsDialog.dismiss();
					}

				}
			}

		});

	}

}

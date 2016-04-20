package com.example.adapter;

import java.util.List;
import org.json.JSONObject;
import com.daimajia.swipe.SwipeAdapter;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.SwipeListener;
import com.example.config.MySharedPreference;
import com.example.dialog.SpotsDialog;
import com.example.dto.User;
import com.example.entity.CardInfo;
import com.example.honeytag1.R;
import com.example.utils.PublicStaticURL;
import com.example.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import info.wangchen.simplehud.SimpleHUD;

public class CardListAdapter extends SwipeAdapter {

	private Context context;
	private List<CardInfo> mList;
	Holder mHolder = null;
	Holder[] arr;
	SpotsDialog spotsDialog; // dialog
	public int position1 = -1;// 侧滑打开时的位置

	public CardListAdapter(Context context, List<CardInfo> mList) {
		this.context = context;
		this.mList = mList;
		arr = new Holder[mList.size()];
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public void fillValues(final int position, View convertView) {

		Holder mHolder = (Holder) convertView.getTag();

		CardInfo mCardInfo = mList.get(position);
		final String cardId = mCardInfo.getId();
		String cardname = mCardInfo.getCard_name();
		String cardzhiwei = mCardInfo.getCard_title();
		String cardphone = mCardInfo.getMobile();
		String cardemail = mCardInfo.getEmail();
		String cardfax = mCardInfo.getFax();
		String cardtel = mCardInfo.getPhone();
		String cardgongsi = mCardInfo.getComp();
		String cardadress = mCardInfo.getAddress();
		String cardindustry = mCardInfo.getCard_industry();

		String cardtime = TimeUtils.getTime(mCardInfo.getAdd_time());

		if (TextUtils.isEmpty(cardindustry)) {
			if (TextUtils.isEmpty(cardzhiwei)) {
				mHolder.identity_service_name.setText("|");
			} else {
				mHolder.identity_service_name.setText(" | " + Transformation(cardzhiwei));
			}

		} else {
			if (TextUtils.isEmpty(cardzhiwei)) {
				mHolder.identity_service_name.setText(Transformation(cardindustry) + " | ");
			} else {
				mHolder.identity_service_name
						.setText(Transformation(cardindustry) + " | " + Transformation(cardzhiwei));
			}
		}

		mHolder.identity_service_name.setText(Transformation(cardindustry) + " | " + Transformation(cardzhiwei));

		// if(cardname.equals(""))
		// {
		// identity_lin1.setVisibility(View.GONE);
		// }else
		// {
		// identity_name.setText(Transformation(cardname));
		// }

		mHolder.identity_username.setText(Transformation(cardname));
		mHolder.identity_position.setText(Transformation(cardzhiwei));

		// if (!TextUtils.isEmpty(cardtime)) {
		mHolder.identity_service_time.setText(cardtime);

		//
		// if (TextUtils.isEmpty(cardphone)) {
		// mHolder.identity_lin3.setVisibility(View.GONE);
		// } else {
		mHolder.identity_tel.setText(Transformation(cardphone));
		// }

		// if (TextUtils.isEmpty(cardemail)) {
		//
		// mHolder.identity_lin6.setVisibility(View.GONE);
		// } else {
		mHolder.identity_email.setText(Transformation(cardemail));
		// }
		// if (TextUtils.isEmpty(cardfax)) {
		//
		// mHolder.identity_lin7.setVisibility(View.GONE);
		// } else {
		mHolder.identity_fax.setText(Transformation(cardfax));
		// }

		// if (TextUtils.isEmpty(cardtel)) {
		//
		// mHolder.identity_lin8.setVisibility(View.GONE);
		// } else {
		mHolder.identity_zuoji.setText(Transformation(cardtel));
		// }

		// if (TextUtils.isEmpty(cardgongsi)) {
		//
		// mHolder.identity_lin9.setVisibility(View.GONE);
		// } else {
		String cmp = Transformation(cardgongsi);
		String[] c = cmp.split(",");
		String cmp1 = "";
		for (int i = 0; i < c.length; i++) {
			if (i < c.length - 1) {
				cmp1 += c[i] + "\n";
			}
			if (i == c.length - 1) {
				cmp1 += c[i];
			}

		}

		mHolder.identity_gongsi.setText(cmp1);
		// }
		// if (TextUtils.isEmpty(cardadress)) {
		// mHolder.identity_lin10.setVisibility(View.GONE);
		// } else {
		mHolder.identity_dizhi.setText(Transformation(cardadress));
		// }

		mHolder.img_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout mLinearLayout = arr[position].linear_content;
				ImageView iv = arr[position].img_show;

				if (mLinearLayout.getVisibility() == View.GONE) {
					mLinearLayout.setVisibility(View.VISIBLE);
					iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state));
				} else {
					mLinearLayout.setVisibility(View.GONE);
					iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state_1));
				}

			}

		});
		mHolder.mSwipe.addSwipeListener(new SwipeListener() {

			@Override
			public void onUpdate(SwipeLayout arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onOpen(SwipeLayout arg0) {
				position1 = position;

				LinearLayout mLinearLayout = arr[position].linear_content;
				ImageView iv = arr[position].img_show;
				// iv.setEnabled(false);
				// iv.setClickable(false);
				iv.setVisibility(View.INVISIBLE);

				// if (mLinearLayout.getVisibility() == View.GONE) {
				// mLinearLayout.setVisibility(View.VISIBLE);
				// iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state));
				// } else {
				mLinearLayout.setVisibility(View.GONE);
				iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state_1));
				// }

			}

			@Override
			public void onHandRelease(SwipeLayout arg0, float arg1, float arg2) {

			}

			@Override
			public void onClose(SwipeLayout arg0) {
				if (position1 == -1) {
					return;
				}
				LinearLayout mLinearLayout = arr[position].linear_content;
				ImageView iv = arr[position].img_show;
				// iv.setEnabled(true);
				// iv.setClickable(true);
				iv.setVisibility(View.VISIBLE);

				// mLinearLayout.setVisibility(View.VISIBLE);
				// iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state));

			}
		});

		/*
		 * 报错
		 */
		mHolder.mTvError.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				RequestParams params = new RequestParams();
				params.addBodyParameter("uid", PublicStaticURL.userid);
				params.addBodyParameter("type", "1");// 1代表名片
				params.addBodyParameter("params", "[{id:" + cardId + "}]");
				error(PublicStaticURL.BUSINESSCARDERROR, params, position);
			}
		});

		/*
		 * 删除名片
		 * 
		 */
		mHolder.mTvDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mList.size() <= 1) {
					SimpleHUD.showInfoMessage(context, context.getString(R.string.tishi));
					return;
				}
				User mUser = MySharedPreference.readUser(context);
				if (mUser == null) {
					return;
				}
				closeItem(position);
				RequestParams params = new RequestParams();
				params.addBodyParameter("uid", mUser.getUid());
				params.addBodyParameter("id", cardId);// 1代表名片
				delete(PublicStaticURL.BUSINESSCARDDELETE, params, position);

			}
		});

	}

	@Override
	public View generateView(int position, ViewGroup arg1) {
		View convertView = LayoutInflater.from(context).inflate(R.layout.item_identity, null);

		mHolder = new Holder();

		mHolder.mSwipe = (SwipeLayout) convertView.findViewById(R.id.sample2);
		mHolder.identity_lin3 = (LinearLayout) convertView.findViewById(R.id.identity_lin3);
		mHolder.identity_lin4 = (LinearLayout) convertView.findViewById(R.id.identity_lin4);
		mHolder.identity_lin6 = (LinearLayout) convertView.findViewById(R.id.identity_lin6);
		mHolder.identity_lin7 = (LinearLayout) convertView.findViewById(R.id.identity_lin7);
		mHolder.identity_lin8 = (LinearLayout) convertView.findViewById(R.id.identity_lin8);

		mHolder.mTvError = (TextView) convertView.findViewById(R.id.tv_error);
		mHolder.mTvDelete = (TextView) convertView.findViewById(R.id.tv_delete);

		mHolder.linear_content = (LinearLayout) convertView.findViewById(R.id.linear_content);
		mHolder.identity_service_time = (TextView) convertView.findViewById(R.id.identity_service_time);
		mHolder.identity_service_name = (TextView) convertView.findViewById(R.id.identity_service_name);
		mHolder.identity_gongsi = (TextView) convertView.findViewById(R.id.identity_gongsi);
		mHolder.identity_dizhi = (TextView) convertView.findViewById(R.id.identity_dizhi);
		mHolder.identity_username = (TextView) convertView.findViewById(R.id.identity_username);
		mHolder.identity_position = (TextView) convertView.findViewById(R.id.identity_position);
		mHolder.identity_tel = (TextView) convertView.findViewById(R.id.identity_tel);
		mHolder.identity_email = (TextView) convertView.findViewById(R.id.identity_email);
		mHolder.identity_zuoji = (TextView) convertView.findViewById(R.id.identity_zuoji);
		mHolder.identity_fax = (TextView) convertView.findViewById(R.id.identity_fax);

		mHolder.img_show = (ImageView) convertView.findViewById(R.id.img_show);

		mHolder.linear_content.setTag(position);
		convertView.setTag(mHolder);

		arr[position] = mHolder;
		return convertView;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {

		return R.id.sample2;
	}

	@Override
	public void openItem(int position) {
		LinearLayout mLinearLayout = arr[position].linear_content;
		ImageView iv = arr[position].img_show;
		iv.setEnabled(false);
		iv.setClickable(false);

		// if (mLinearLayout.getVisibility() == View.GONE) {
		// mLinearLayout.setVisibility(View.VISIBLE);
		// iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state));
		// } else {
		mLinearLayout.setVisibility(View.GONE);
		iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state_1));
		// }

		super.openItem(position);
	}

	@Override
	public void closeItem(int position) {
		super.closeItem(position);
	}

	/*
	 * 报错
	 */
	private void error(String str, RequestParams params, final int position) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(context);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(context, context.getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据

				JSONObject json;

				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						closeItem(position);

						SimpleHUD.showInfoMessage(context, context.getString(R.string.commitSuccess));

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(context, context.getString(R.string.getinfo_failed));
					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(context, context.getString(R.string.getinfo_failed));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	/*
	 * 删除名片
	 */
	private void delete(String str, RequestParams params, final int position) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(context);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(context, context.getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据

				JSONObject json;

				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {

						mList.remove(position);
						notifyDataSetInvalidated();

						SimpleHUD.showInfoMessage(context, context.getString(R.string.deleteSuccess));

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(context, context.getString(R.string.deleteFailure));
					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(context, context.getString(R.string.deleteFailure));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// 字符串截取
	private String Transformation(String s) {
		if (TextUtils.isEmpty(s)) {
			return "";
		}
		String str = s.replace("[", "");
		String str1 = str.replace("]", "");

		if (str1.indexOf("\"") == -1) {
			return str1;
		}
		String str2 = str1.replace("\"", "");
		return str2;
	}

	static class Holder {
		private SwipeLayout mSwipe;
		private LinearLayout identity_lin1, identity_lin2, identity_lin3, identity_lin4, identity_lin5, identity_lin6,
				identity_lin7, identity_lin8, identity_lin9, identity_lin10;
		LinearLayout linear_content;
		ImageView img_show;
		TextView mTvError, mTvDelete;
		private TextView identity_username, identity_position, identity_name, identity_zhiwei, identity_tel,
				identity_hangye, identity_email, identity_fax, identity_zuoji, identity_gongsi, identity_dizhi,
				identity_tel2, identity_service_name, identity_service_time; // 获取名片信息
	}

	// private Context context;
	// private List<CardInfo> mList;
	// Holder mHolder = null;
	// Holder[] arr;
	// SpotsDialog spotsDialog; // dialog
	//
	// public CardListAdapter(Context context, List<CardInfo> mList) {
	// this.context = context;
	// this.mList = mList;
	// arr = new Holder[mList.size()];
	// }
	//
	// @Override
	// public int getCount() {
	//
	// return mList.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// @Override
	// public View getView(final int position, View convertView, ViewGroup
	// parent) {
	//
	// if (convertView == null) {
	// mHolder = new Holder();
	// convertView =
	// LayoutInflater.from(context).inflate(R.layout.item_identity, null);
	//
	// mHolder.identity_lin3 = (LinearLayout)
	// convertView.findViewById(R.id.identity_lin3);
	// mHolder.identity_lin4 = (LinearLayout)
	// convertView.findViewById(R.id.identity_lin4);
	// mHolder.identity_lin6 = (LinearLayout)
	// convertView.findViewById(R.id.identity_lin6);
	// mHolder.identity_lin7 = (LinearLayout)
	// convertView.findViewById(R.id.identity_lin7);
	// mHolder.identity_lin8 = (LinearLayout)
	// convertView.findViewById(R.id.identity_lin8);
	//
	//
	// mHolder.mBtnError = (Button) convertView.findViewById(R.id.btn_error);
	// mHolder.mBtnDelete = (Button) convertView.findViewById(R.id.btn_delete);
	//
	// mHolder.linear_content = (LinearLayout)
	// convertView.findViewById(R.id.linear_content);
	// mHolder.identity_service_time = (TextView)
	// convertView.findViewById(R.id.identity_service_time);
	// mHolder.identity_service_name = (TextView)
	// convertView.findViewById(R.id.identity_service_name);
	// mHolder.identity_gongsi = (TextView)
	// convertView.findViewById(R.id.identity_gongsi);
	// mHolder.identity_dizhi = (TextView)
	// convertView.findViewById(R.id.identity_dizhi);
	// mHolder.identity_username = (TextView)
	// convertView.findViewById(R.id.identity_username);
	// mHolder.identity_position = (TextView)
	// convertView.findViewById(R.id.identity_position);
	// mHolder.identity_tel = (TextView)
	// convertView.findViewById(R.id.identity_tel);
	// mHolder.identity_email = (TextView)
	// convertView.findViewById(R.id.identity_email);
	// mHolder.identity_zuoji = (TextView)
	// convertView.findViewById(R.id.identity_zuoji);
	// mHolder.identity_fax = (TextView)
	// convertView.findViewById(R.id.identity_fax);
	//
	// mHolder.img_show = (ImageView) convertView.findViewById(R.id.img_show);
	//
	// mHolder.linear_content.setTag(position);
	// convertView.setTag(mHolder);
	// } else {
	// mHolder = (Holder) convertView.getTag();
	//
	// }
	// arr[position] = mHolder;
	//
	// CardInfo mCardInfo = mList.get(position);
	// final String cardId = mCardInfo.getId();
	// String cardname = mCardInfo.getCard_name();
	// String cardzhiwei = mCardInfo.getCard_title();
	// String cardphone = mCardInfo.getMobile();
	// String cardemail = mCardInfo.getEmail();
	// String cardfax = mCardInfo.getFax();
	// String cardtel = mCardInfo.getPhone();
	// String cardgongsi = mCardInfo.getComp();
	// String cardadress = mCardInfo.getAddress();
	// String cardindustry = mCardInfo.getCard_industry();
	//
	// String cardtime = mCardInfo.getAdd_time();
	//
	// if (TextUtils.isEmpty(cardindustry)) {
	// if (TextUtils.isEmpty(cardzhiwei)) {
	// mHolder.identity_service_name.setText("|");
	// } else {
	// mHolder.identity_service_name.setText(" | " +
	// Transformation(cardzhiwei));
	// }
	//
	// } else {
	// if (TextUtils.isEmpty(cardzhiwei)) {
	// mHolder.identity_service_name.setText(Transformation(cardindustry) + " |
	// ");
	// } else {
	// mHolder.identity_service_name
	// .setText(Transformation(cardindustry) + " | " +
	// Transformation(cardzhiwei));
	// }
	// }
	//
	// mHolder.identity_service_name.setText(Transformation(cardindustry) + " |
	// " + Transformation(cardzhiwei));
	//
	// // if(cardname.equals(""))
	// // {
	// // identity_lin1.setVisibility(View.GONE);
	// // }else
	// // {
	// // identity_name.setText(Transformation(cardname));
	// // }
	//
	// mHolder.identity_username.setText(Transformation(cardname));
	// mHolder.identity_position.setText(Transformation(cardzhiwei));
	//
	// // if (!TextUtils.isEmpty(cardtime)) {
	// mHolder.identity_service_time.setText(cardtime);
	//
	// //
	// // if (TextUtils.isEmpty(cardphone)) {
	// // mHolder.identity_lin3.setVisibility(View.GONE);
	// // } else {
	// mHolder.identity_tel.setText(Transformation(cardphone));
	// // }
	//
	// // if (TextUtils.isEmpty(cardemail)) {
	// //
	// // mHolder.identity_lin6.setVisibility(View.GONE);
	// // } else {
	// mHolder.identity_email.setText(Transformation(cardemail));
	// // }
	// // if (TextUtils.isEmpty(cardfax)) {
	// //
	// // mHolder.identity_lin7.setVisibility(View.GONE);
	// // } else {
	// mHolder.identity_fax.setText(Transformation(cardfax));
	// // }
	//
	// // if (TextUtils.isEmpty(cardtel)) {
	// //
	// // mHolder.identity_lin8.setVisibility(View.GONE);
	// // } else {
	// mHolder.identity_zuoji.setText(Transformation(cardtel));
	// // }
	//
	// // if (TextUtils.isEmpty(cardgongsi)) {
	// //
	// // mHolder.identity_lin9.setVisibility(View.GONE);
	// // } else {
	// String cmp = Transformation(cardgongsi);
	// String[] c = cmp.split(",");
	// String cmp1 = "";
	// for (int i = 0; i < c.length; i++) {
	// if (i < c.length - 1) {
	// cmp1 += c[i] + "\n";
	// }
	// if (i == c.length - 1) {
	// cmp1 += c[i];
	// }
	//
	// }
	//
	// mHolder.identity_gongsi.setText(cmp1);
	// // }
	// // if (TextUtils.isEmpty(cardadress)) {
	// // mHolder.identity_lin10.setVisibility(View.GONE);
	// // } else {
	// mHolder.identity_dizhi.setText(Transformation(cardadress));
	// // }
	//
	// mHolder.img_show.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// LinearLayout mLinearLayout = arr[position].linear_content;
	// ImageView iv = arr[position].img_show;
	//
	// if (mLinearLayout.getVisibility() == View.GONE) {
	// mLinearLayout.setVisibility(View.VISIBLE);
	// iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state));
	// } else {
	// mLinearLayout.setVisibility(View.GONE);
	// iv.setImageDrawable(context.getResources().getDrawable(R.drawable.state_1));
	// }
	//
	// }
	//
	// });
	//
	// /*
	// * 报错
	// */
	// mHolder.mBtnError.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// RequestParams params = new RequestParams();
	// params.addBodyParameter("uid", PublicStaticURL.userid);
	// params.addBodyParameter("type", "1");// 1代表名片
	// params.addBodyParameter("params", "[{id" + cardId + "}]");
	// error(PublicStaticURL.BUSINESSCARDERROR, params);
	// }
	// });
	//
	// /*
	// * 删除名片
	// *
	// */
	// mHolder.mBtnDelete.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// User mUser = MySharedPreference.readUser(context);
	// if (mUser == null) {
	// return;
	// }
	// RequestParams params = new RequestParams();
	// params.addBodyParameter("uid", PublicStaticURL.userid);
	// params.addBodyParameter("id", cardId);// 1代表名片
	// delete(PublicStaticURL.BUSINESSCARDDELETE, params, position);
	//
	// }
	// });
	//
	// return convertView;
	// }
	//
	// /*
	// * 报错
	// */
	// private void error(String str, RequestParams params) {
	// HttpUtils httpUtils = new HttpUtils();
	// httpUtils.configCurrentHttpCacheExpiry(0);
	// httpUtils.send(HttpMethod.POST, str, params, new
	// RequestCallBack<String>() {
	// @Override
	// public void onStart() {
	// super.onStart();
	//
	// spotsDialog = new SpotsDialog(context);
	// spotsDialog.show();
	// }
	//
	// @Override
	// public void onFailure(com.lidroid.xutils.exception.HttpException arg0,
	// String arg1) {
	// // TODO Auto-generated method stub
	// spotsDialog.dismiss();
	// SimpleHUD.showInfoMessage(context,
	// context.getString(R.string.server_connect_failed));
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	// spotsDialog.dismiss();
	// String str1 = responseInfo.result;// 接口返回的数据
	//
	// JSONObject json;
	//
	// try {
	// json = new JSONObject(str1);
	// String code = json.getString("code");
	// if ("2".equals(code)) {
	//
	// SimpleHUD.showInfoMessage(context,
	// context.getString(R.string.commitSuccess));
	//
	// }
	// if ("3".equals(code)) {
	// SimpleHUD.showInfoMessage(context,
	// context.getString(R.string.getinfo_failed));
	// }
	//
	// } catch (Exception e) {
	// SimpleHUD.showInfoMessage(context,
	// context.getString(R.string.getinfo_failed));
	// Log.e("message", "走catch了");
	// e.printStackTrace();
	// }
	// }
	//
	// });
	//
	// }
	//
	// /*
	// * 删除名片
	// */
	// private void delete(String str, RequestParams params, final int position)
	// {
	// HttpUtils httpUtils = new HttpUtils();
	// httpUtils.configCurrentHttpCacheExpiry(0);
	// httpUtils.send(HttpMethod.POST, str, params, new
	// RequestCallBack<String>() {
	// @Override
	// public void onStart() {
	// super.onStart();
	//
	// spotsDialog = new SpotsDialog(context);
	// spotsDialog.show();
	// }
	//
	// @Override
	// public void onFailure(com.lidroid.xutils.exception.HttpException arg0,
	// String arg1) {
	// // TODO Auto-generated method stub
	// spotsDialog.dismiss();
	// SimpleHUD.showInfoMessage(context,
	// context.getString(R.string.server_connect_failed));
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	// spotsDialog.dismiss();
	// String str1 = responseInfo.result;// 接口返回的数据
	//
	// JSONObject json;
	//
	// try {
	// json = new JSONObject(str1);
	// String code = json.getString("code");
	// if ("2".equals(code)) {
	//
	// mList.remove(position);
	// notifyDataSetChanged();
	// SimpleHUD.showInfoMessage(context,
	// context.getString(R.string.commitSuccess));
	//
	// }
	// if ("3".equals(code)) {
	// SimpleHUD.showInfoMessage(context,
	// context.getString(R.string.commitFailure));
	// }
	//
	// } catch (Exception e) {
	// SimpleHUD.showInfoMessage(context,
	// context.getString(R.string.commitFailure));
	// Log.e("message", "走catch了");
	// e.printStackTrace();
	// }
	// }
	//
	// });
	//
	// }
	//
	// // 字符串截取
	// private String Transformation(String s) {
	// if (TextUtils.isEmpty(s)) {
	// return "";
	// }
	// String str = s.replace("[", "");
	// String str1 = str.replace("]", "");
	//
	// if (str1.indexOf("\"") == -1) {
	// return str1;
	// }
	// String str2 = str1.replace("\"", "");
	// return str2;
	// }
	//
	// static class Holder {
	// private LinearLayout identity_lin1, identity_lin2, identity_lin3,
	// identity_lin4, identity_lin5, identity_lin6,
	// identity_lin7, identity_lin8, identity_lin9, identity_lin10;
	// LinearLayout linear_content;
	// ImageView img_show;
	// Button mBtnError, mBtnDelete;
	// private TextView identity_username, identity_position, identity_name,
	// identity_zhiwei, identity_tel,
	// identity_hangye, identity_email, identity_fax, identity_zuoji,
	// identity_gongsi, identity_dizhi,
	// identity_tel2, identity_service_name, identity_service_time; // 获取名片信息
	// }

}

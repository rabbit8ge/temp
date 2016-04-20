package com.example.dialog;

import com.example.dialog.CustomDialog.Builder;
import com.example.honeytag1.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context; // 娑撳﹣绗呴弬鍥ь嚠鐠烇拷
		private String title; // 鐎电鐦藉鍡樼垼妫帮拷
		private String message; // 鐎电鐦藉鍡楀敶鐎癸拷
		private String confirm_btnText; // 閹稿鎸抽崥宥囆為垾婊呪�樼�规埃锟斤拷
		private String cancel_btnText; // 閹稿鎸抽崥宥囆為垾婊冨絿濞戝牃锟斤拷
		private String neutral_btnText; // 閹稿鎸抽崥宥囆為垾婊堟閽樺繆锟斤拷
		private View contentView; // 鐎电鐦藉鍡曡厬闂傛潙濮炴潪鐣屾畱閸忔湹绮敮鍐ㄧ湰閻ｅ矂娼�
		/* 閹稿鎸抽崸姘隘娴滃娆� */
		private DialogInterface.OnClickListener confirm_btnClickListener;
		private DialogInterface.OnClickListener cancel_btnClickListener;
		private DialogInterface.OnClickListener neutral_btnClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/* 鐠佸墽鐤嗙�电鐦藉鍡曚繆閹拷 */
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * 鐠佸墽鐤嗙�电鐦藉鍡欐櫕闂堬拷
		 * 
		 * @param v
		 *            View
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setPositiveButton(int confirm_btnText, DialogInterface.OnClickListener listener) {
			this.confirm_btnText = (String) context.getText(confirm_btnText);
			this.confirm_btnClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setPositiveButton(String confirm_btnText, DialogInterface.OnClickListener listener) {
			this.confirm_btnText = confirm_btnText;
			this.confirm_btnClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button resource and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setNegativeButton(int cancel_btnText, DialogInterface.OnClickListener listener) {
			this.cancel_btnText = (String) context.getText(cancel_btnText);
			this.cancel_btnClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setNegativeButton(String cancel_btnText, DialogInterface.OnClickListener listener) {
			this.cancel_btnText = cancel_btnText;
			this.cancel_btnClickListener = listener;
			return this;
		}

		/**
		 * Set the netural button resource and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setNeutralButton(int neutral_btnText, DialogInterface.OnClickListener listener) {
			this.neutral_btnText = (String) context.getText(neutral_btnText);
			this.neutral_btnClickListener = listener;
			return this;
		}

		/**
		 * Set the netural button and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setNeutralButton(String neutral_btnText, DialogInterface.OnClickListener listener) {
			this.neutral_btnText = neutral_btnText;
			this.neutral_btnClickListener = listener;
			return this;
		}

		@SuppressLint("WrongViewCast")
		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context, R.style.mystyle);
			View layout = inflater.inflate(R.layout.cusdialog, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			((TextView) layout.findViewById(R.id.title)).getPaint().setFakeBoldText(true);

			if (title == null || title.trim().length() == 0) {
				((TextView) layout.findViewById(R.id.message)).setGravity(Gravity.CENTER);
			}

			if (neutral_btnText != null && confirm_btnText != null && cancel_btnText != null) {
				((Button) layout.findViewById(R.id.confirm_btn)).setText(confirm_btnText);
				if (neutral_btnClickListener != null) {
					((Button) layout.findViewById(R.id.neutral_btn)).setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							neutral_btnClickListener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
						}
					});
				} else {
					((Button) layout.findViewById(R.id.neutral_btn)).setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			} else {
				// if no confirm button or cancle button or neutral just set the
				// visibility to GONE
				layout.findViewById(R.id.neutral_btn).setVisibility(View.GONE);
				layout.findViewById(R.id.single_line).setVisibility(View.GONE);
			}
			// set the confirm button
			if (confirm_btnText != null) {
				((Button) layout.findViewById(R.id.confirm_btn)).setText(confirm_btnText);
				if (confirm_btnClickListener != null) {
					((Button) layout.findViewById(R.id.confirm_btn)).setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							confirm_btnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				} else {
					((Button) layout.findViewById(R.id.confirm_btn)).setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.confirm_btn).setVisibility(View.GONE);
				layout.findViewById(R.id.second_line).setVisibility(View.GONE);
				layout.findViewById(R.id.cancel_btn).setBackgroundResource(R.drawable.single_btn_select);
			}
			// set the cancel button
			if (cancel_btnText != null) {
				((Button) layout.findViewById(R.id.cancel_btn)).setText(cancel_btnText);
				if (cancel_btnClickListener != null) {
					((Button) layout.findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							cancel_btnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						}
					});
				} else {
					((Button) layout.findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			} else {
				// if no cancel button just set the visibility to GONE
				layout.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
				layout.findViewById(R.id.second_line).setVisibility(View.GONE);
				layout.findViewById(R.id.confirm_btn).setBackgroundResource(R.drawable.single_btn_select);
			}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.message)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.message)).addView(contentView,
						new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}

			dialog.setContentView(layout);
			return dialog;
		}

		public CustomDialog create(boolean flag) {

			CustomDialog dialog = create();
			dialog.setCancelable(flag);
			return dialog;
		}

		public Builder setCancelable(boolean b) {
			// TODO Auto-generated method stub
			this.setCancelable(b);
			return this;
		}

	}
}

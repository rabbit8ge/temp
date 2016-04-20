package com.example.view;

import com.example.utils.ExpressionUtil;
import com.example.utils.Logger;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

public class ClipboarEditText extends EditText {
	private Context mContext;
	/**
	 * 粘贴id
	 */
	private static final int ID_PASTE = android.R.id.paste;

	public ClipboarListener mClipboarListener;

	public ClipboarEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public ClipboarEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public ClipboarEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	@Override
	public boolean onTextContextMenuItem(int id) {
		// 粘帖板
		ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

		Logger.i("id:" + id);
		// 粘贴板内容
		CharSequence paste = clip.getText();
		Logger.i("paste = " + paste);
		if (id == ID_PASTE) {// 如果选择黏贴
			Logger.i("=====在粘贴======");
			// Toast toast = Toast.makeText(getContext(), "不能输入粘贴内容，请手输！",
			// Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
			// toast.show();
			// if(mClipboarListener!=null){
			// mClipboarListener.OnClipboarListener();
			// }
			// int iCursorStart = Selection.getSelectionStart((this.getText()));
			// int iCursorEnd = Selection.getSelectionEnd((this.getText()));
			// if (iCursorStart != iCursorEnd) {
			// this.getText().replace(iCursorStart, iCursorEnd, "");
			// }
			int iCursor = Selection.getSelectionEnd((this.getText()));

			SpannableStringBuilder sb = ExpressionUtil.praseToEditText(mContext, this, paste.toString());
			this.getText().insert(iCursor, sb);
			return false;
		}
		return super.onTextContextMenuItem(id);
	}

	public interface ClipboarListener {
		public void OnClipboarListener();
	}

	public void setOnClipboarListener(ClipboarListener mClipboarListener) {
		this.mClipboarListener = mClipboarListener;
	}

}

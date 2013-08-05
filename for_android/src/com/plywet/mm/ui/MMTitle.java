package com.plywet.mm.ui;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plywet.mm.R;
import com.plywet.mm.ui.chatting.ChattingUtils;

public class MMTitle {
	private MMImageButton btn1 = null;
	private MMImageButton btn4 = null;
	private TextView title = null;
	private ImageView logo = null;
	private Button leftBtn = null;
	private Button rightBtn = null;
	private ImageView phone = null;
	private ImageView mute = null;
	private LinearLayout navTitle = null;
	private LinearLayout titleToTop = null;
	private final MMActivity act;

	public MMTitle(Activity act) {
		this.act = (MMActivity) act;
		this.btn1 = (MMImageButton) act.findViewById(R.id.title_btn1);
		this.btn4 = (MMImageButton) act.findViewById(R.id.title_btn4);
		this.navTitle = (LinearLayout) act.findViewById(R.id.nav_title);
		this.titleToTop = (LinearLayout) act.findViewById(R.id.title_to_top);
		this.phone = (ImageView) act.findViewById(R.id.title_phone);
		this.mute = (ImageView) act.findViewById(R.id.title_mute);
		this.logo = (ImageView) act.findViewById(R.id.title_logo);
		this.title = (TextView) act.findViewById(R.id.title);

	}

	/**
	 * 获得Title的根对象
	 * 
	 * @return
	 */
	public View getRoot() {
		return this.navTitle;
	}

	public void setLeftBtn(int text, View.OnClickListener listener) {
		// 如果左按钮不存在，设置layout中的btn4
		if (this.leftBtn == null) {
			setBtn4(this.act.getString(text), listener);
		}
		// 设置左按钮
		else {
			this.leftBtn.setVisibility(View.VISIBLE);
			if (text != 0) {
				this.leftBtn.setText(text);
			}
			this.leftBtn.setOnClickListener(listener);
		}
	}

	public void setLeftBtnEnabled(boolean enabled) {
		if (this.leftBtn == null) {
			if (this.btn4 != null) {
				this.btn4.setEnabled(enabled);
			}
		} else {
			this.leftBtn.setEnabled(enabled);
		}
	}

	public void disableLeftBtn() {
		if (this.leftBtn != null) {
			this.leftBtn.setEnabled(false);
		}
	}

	public MMImageButton setBtn4(Object obj, View.OnClickListener listener) {
		if (this.btn4 != null) {
			// btn4显示
			this.btn4.setVisibility(View.VISIBLE);

			// 设置btn4文字
			if (obj instanceof String) {
				this.btn4.setText((String) obj);
			} else {
				this.btn4.setImage(((Integer) obj).intValue());
			}

			// 设置btn4监听器
			this.btn4.setOnClickListener(listener);

			// 判断btn1是否显示
			if (this.btn1 != null && this.btn1.getVisibility() != View.VISIBLE)
				this.btn1.setVisibility(View.INVISIBLE);
		}
		return this.btn4;
	}

	public void setRightBtn(int text, View.OnClickListener listener) {
		// 如果左按钮不存在，设置layout中的btn1
		if (this.rightBtn == null) {
			setBtn1(this.act.getString(text), listener);
		}
		// 设置左按钮
		else {
			this.rightBtn.setVisibility(View.VISIBLE);
			if (text != 0) {
				this.rightBtn.setText(text);
			}
			this.rightBtn.setOnClickListener(listener);
		}
	}

	public void setRightBtnEnabled(boolean enabled) {
		if (this.rightBtn == null) {
			if (this.btn1 != null) {
				this.btn1.setEnabled(enabled);
			}
		} else {
			this.rightBtn.setEnabled(enabled);
		}
	}

	public final void visibleRightBtn(int v) {
		if (this.rightBtn == null) {
			if (this.btn1 != null) {
				this.btn1.setVisibility(v);
			}
		} else {
			this.rightBtn.setVisibility(v);
		}
	}

	public final MMImageButton setBtn1(Object obj, View.OnClickListener listener) {
		if (this.btn1 != null) {
			// btn1显示
			this.btn1.setVisibility(View.VISIBLE);

			// 设置btn1文字
			if (obj instanceof String) {
				this.btn1.setText((String) obj);
			} else {
				this.btn1.setImage(((Integer) obj).intValue());
			}

			// 设置btn1监听器
			this.btn1.setOnClickListener(listener);

			// 判断btn4是否显示
			if (this.btn4 != null && this.btn4.getVisibility() != View.VISIBLE)
				this.btn4.setVisibility(View.INVISIBLE);
		}
		return this.btn1;
	}

	public final void setTitleToTop(View.OnClickListener listener) {
		this.titleToTop.setOnClickListener(listener);
	}

	public final void setTitle(String text) {
		if (this.title != null) {
			this.title.setText(ChattingUtils.createSpannableString(this.act,
					text, (int) this.title.getTextSize()));
		}
	}

	public final void setTitle(int id) {
		if (this.title != null) {
			setTitle(this.act.getString(id));
		}
	}

	public final void enableLogo() {
		if (this.logo != null) {
			this.logo.setVisibility(View.VISIBLE);
			this.logo.setImageDrawable(this.act
					.getDrawableObject(R.drawable.title_logo));
		}
	}

	public final void visibleMute(int v) {
		this.mute.setVisibility(v);
	}

	public final void visibleNavTitle(int v) {
		if (this.navTitle != null) {
			this.navTitle.setVisibility(v);
		}
	}

	public final void visiblePhone(int v) {
		this.phone.setVisibility(v);
	}
}

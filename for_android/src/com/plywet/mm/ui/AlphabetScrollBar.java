package com.plywet.mm.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.plywet.mm.R;
import com.plywet.mm.platformtools.android.BitmapFactory;

/**
 * 字母表滚动条
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 下午05:43:08
 */
public class AlphabetScrollBar extends View {

	private String[] alphabets;

	private Bitmap searchIcon;

	private MMActivity activity;

	private int block;

	private PopupWindow popWin;

	private TextView headToastText;

	private boolean move;

	private MMAlphabetActionInterface action;

	/**
	 * 索引
	 */
	private int index;

	/**
	 * 缩放率
	 */
	private float perSize;

	/**
	 * y轴坐标
	 */
	private float y;

	public AlphabetScrollBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.alphabets = new String[] { "!", "+", "A", "B", "C", "D", "E", "F",
				"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z", "#" };

		this.activity = (MMActivity) context;

		setFocusable(true);
		setFocusableInTouchMode(true);

		this.move = false;

		this.block = BitmapFactory.transDpi(context, 3);

		View headToast = inflate(context, R.layout.show_head_toast, null);

		int winSizeDpi = BitmapFactory.transDpi(context, 79);

		this.popWin = new PopupWindow(headToast, winSizeDpi, winSizeDpi);

		this.headToastText = (TextView) headToast
				.findViewById(R.id.show_head_toast_text);

		this.searchIcon = android.graphics.BitmapFactory
				.decodeStream(getResources().openRawResource(
						R.drawable.scroll_bar_search_icon));
	}

	public void clearAction() {
		this.action = null;
	}

	public void setAction(MMAlphabetActionInterface action) {
		this.action = action;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int h = getMeasuredHeight();
		int w = getMeasuredWidth();

		this.perSize = (h - this.searchIcon.getHeight() - this.block)
				/ (1.2F * this.alphabets.length);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(0xFF858C94);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(this.perSize);
		canvas.drawBitmap(this.searchIcon,
				(w - this.searchIcon.getWidth()) / 2.0F, this.block, paint);
		for (int i = 0; i < this.alphabets.length; i++) {
			canvas.drawText(this.alphabets[i], w / 2.0F, this.perSize + 1.2F
					* (i * this.perSize) + this.searchIcon.getHeight()
					+ this.block, paint);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float f1;
		float f2;

		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			this.move = true;

			this.y = event.getY();

			if (this.y < 0.0F) {
				this.y = 0.0F;
			}

			if (this.y > getMeasuredHeight()) {
				this.y = getMeasuredHeight();
			}

			setBackgroundDrawable(this.activity
					.getDrawableObject(R.drawable.scrollbar_bg));

			f1 = this.y;
			f2 = 1.2F * this.perSize;

			this.index = -1;

			// 如果是索引，设置索引位置
			if (f1 >= this.searchIcon.getHeight() + this.block) {
				this.index = (int) ((f1 - this.searchIcon.getHeight() + this.block) / f2);
				if (this.index < 0) {
					this.index = 0;
				} else if (this.index >= this.alphabets.length) {
					this.index = this.alphabets.length - 1;
				}
			}

			if (this.index == -1) {
				this.headToastText.setText(R.string.scroll_bar_search);
			} else {
				this.headToastText.setText(this.alphabets[this.index]);
			}

			// 显示提示
			this.popWin.showAtLocation(this, Gravity.CENTER, 0, 0);

			// 触发行动
			if (this.action != null)
				if (this.index == -1) {
					this.action.action(this.activity
							.getString(R.string.scroll_bar_search));
				} else {
					this.action.action(this.alphabets[this.index]);
				}
		}

		invalidate();

		if (event.getAction() == MotionEvent.ACTION_UP) {
			setBackgroundResource(0);
			this.popWin.dismiss();
			this.move = false;
		}

		return true;
	}

}

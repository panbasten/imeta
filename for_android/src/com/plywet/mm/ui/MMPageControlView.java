package com.plywet.mm.ui;

import java.util.HashMap;
import java.util.Map;

import com.plywet.mm.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MMPageControlView extends LinearLayout {

	private Context context;

	private int maxPageIndex;

	private Map pages = new HashMap();

	private ImageView iv;

	public MMPageControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	/**
	 * 设置位指定页面
	 * 
	 * @param i
	 */
	private void setPage(int i) {

		if (i > this.maxPageIndex) {
			return;
		}

		if (this.maxPageIndex > 0) {
			if (i == 0) {
				removeAllViews();
				for (int index = 0; index < this.maxPageIndex; index++) {
					this.iv = (ImageView) View.inflate(this.context,
							R.layout.mmpage_control_image, null);
					this.pages.put(Integer.valueOf(index), this.iv);

					addView(this.iv);
				}
			}

			for (int index = 0; index < this.maxPageIndex; index++) {
				this.iv = (ImageView) this.pages.get(Integer.valueOf(index));

				if (i == index) {
					this.iv.setImageResource(R.drawable.page_indicator_focused);
				} else {
					this.iv.setImageResource(R.drawable.page_indicator_unfocused);
				}
			}
		}

	}

	/**
	 * 初始化页面
	 * 
	 * @param maxPageIndex
	 */
	public void init(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
		setPage(0);
	}

	/**
	 * 跳转到指定页面
	 * 
	 * @param index
	 */
	public void toPage(int index) {
		setPage(index);
	}

}

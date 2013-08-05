package com.plywet.mm.ui;

import com.plywet.mm.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 带图片和文字的自定义按钮Layout
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-10 上午01:42:59
 */
public class MMImageButton extends LinearLayout {

	private TextView text;
	private ImageView img;
	private MMActivity parent;

	public MMImageButton(Context context, AttributeSet attrSet) {
		super(context, attrSet);

		this.parent = (MMActivity) context;
		View imgBtnView = LayoutInflater.from(this.parent).inflate(
				R.layout.image_button, this, true);
		this.img = (ImageView) imgBtnView.findViewById(R.id.title_btn_iv);
		this.text = (TextView) imgBtnView.findViewById(R.id.title_btn_tv);

	}
	
	public final void setImage(int id){
		this.img.setImageDrawable(this.parent.getDrawableObject(id));
		this.img.setVisibility(View.VISIBLE);
		this.text.setVisibility(View.GONE);
	}
	
	public final void setText(String text){
		this.text.setText(text);
		this.text.setVisibility(View.VISIBLE);
		this.img.setVisibility(View.GONE);
	}

}

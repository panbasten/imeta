package com.plywet.mm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MMGallery extends Gallery {

	public MMGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		setStaticTransformationsEnabled(true);
	}

	public MMGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setStaticTransformationsEnabled(true);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if(e1.getX() - e2.getX() > 10.0F){
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
		return true;
	}
	
	

}

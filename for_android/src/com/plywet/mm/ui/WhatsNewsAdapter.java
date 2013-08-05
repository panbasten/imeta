package com.plywet.mm.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.plywet.mm.R;

public class WhatsNewsAdapter extends BaseAdapter {

	private Context context;

	private List page = new ArrayList();

	public WhatsNewsAdapter(Context context) {
		this.context = context;

		ImageView iv = new ImageView(this.context);

		iv.setBackgroundResource(R.drawable.whatsnew_00);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		this.page.add(iv);

		View view1 = View.inflate(this.context, R.layout.whatsnews_gallery_one,
				null);
		((ImageView) view1.findViewById(R.id.mm_image))
				.setImageResource(R.drawable.whatsnew_01);
		((ImageView) view1.findViewById(R.id.mm_image))
				.setScaleType(ImageView.ScaleType.FIT_XY);
		((TextView) view1.findViewById(R.id.mm_text_tv)).setText(this.context
				.getString(R.string.whats_news_item1));
		this.page.add(view1);

		View view2 = View.inflate(this.context, R.layout.whatsnews_gallery_one,
				null);
		((ImageView) view2.findViewById(R.id.mm_image))
				.setImageResource(R.drawable.whatsnew_01);
		((ImageView) view2.findViewById(R.id.mm_image))
				.setScaleType(ImageView.ScaleType.FIT_XY);
		((TextView) view2.findViewById(R.id.mm_text_tv)).setText(this.context
				.getString(R.string.whats_news_item2));
		this.page.add(view2);

		View view3 = View.inflate(this.context, R.layout.whatsnews_gallery_two,
				null);
		this.page.add(view3);

		View view4 = View.inflate(this.context, R.layout.whatsnews_gallery_one,
				null);
		this.page.add(view4);
	}

	@Override
	public int getCount() {
		return this.page.size();
	}

	@Override
	public Object getItem(int position) {
		return Integer.valueOf(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = (View) this.page.get(position);
		v.setLayoutParams(new Gallery.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
		return v;
	}

}

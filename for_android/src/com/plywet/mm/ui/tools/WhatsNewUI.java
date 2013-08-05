package com.plywet.mm.ui.tools;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.plywet.mm.R;
import com.plywet.mm.ui.MMGallery;
import com.plywet.mm.ui.MMPageControlView;
import com.plywet.mm.ui.WhatsNewsAdapter;

/**
 * whats_news的layout的对应活动类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-16 上午07:31:22
 */
public class WhatsNewUI extends Activity {

	private WhatsNewsAdapter adapter;

	private MMGallery gallery;

	private MMPageControlView pageControl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.whats_news);

		setRequestedOrientation(ActivityInfo.CONFIG_MCC);

		this.gallery = (MMGallery) findViewById(R.id.what_news_gallery);

		this.pageControl = (MMPageControlView) findViewById(R.id.what_news_page_control);

		this.adapter = new WhatsNewsAdapter(this);

		this.gallery.setAdapter(this.adapter);
		this.gallery.setFadingEdgeLength(0);

		this.pageControl.init(this.adapter.getCount() - 1);

		this.gallery.setOnItemClickListener(new WhatsNewOnItemClickListener(
				this));
		this.gallery
				.setOnItemSelectedListener(new WhatsNewOnItemSelectedListener(
						this));
	}

	public WhatsNewsAdapter getAdapter() {
		return adapter;
	}

	public MMGallery getGallery() {
		return gallery;
	}

	public MMPageControlView getPageControl() {
		return pageControl;
	}

}

class WhatsNewOnItemClickListener implements AdapterView.OnItemClickListener {
	private WhatsNewUI ui;

	WhatsNewOnItemClickListener(WhatsNewUI ui) {
		this.ui = ui;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position < this.ui.getAdapter().getCount() - 1) {
			this.ui.getGallery().setSelection(position + 1);
		}
	}
}

class WhatsNewOnItemSelectedListener implements
		AdapterView.OnItemSelectedListener {
	private WhatsNewUI ui;

	WhatsNewOnItemSelectedListener(WhatsNewUI ui) {
		this.ui = ui;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == this.ui.getAdapter().getCount() - 1) {
			this.ui.finish();
		} else {
			this.ui.getPageControl().toPage(position);
		}

	}

	@Override
	public final void onNothingSelected(AdapterView<?> parent) {

	}
}

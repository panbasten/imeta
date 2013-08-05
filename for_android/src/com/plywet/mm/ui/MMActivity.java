package com.plywet.mm.ui;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.plywet.mm.R;
import com.plywet.mm.booter.MMService;
import com.plywet.mm.platformtools.utils.MMLocaleUtils;
import com.plywet.mm.ui.skin.MMSkinFactory;

public abstract class MMActivity extends Activity {

	private AudioManager am;

	private MMTitle title;

	private int maxVolumn;

	private LayoutInflater li;

	private MMSkinFactory skinFact;

	private View activityView;

	private LinearLayout activityLinearLayout;

	/**
	 * 活动默认提供按钮，默认不显示
	 */
	private View activityBtn;

	private View activityContent;

	/**
	 * 触发跳转
	 * 
	 * @param cls
	 */
	public void doStartActivity(Class cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startActivity(intent);
	}

	public void doStartActivity(Class cls, Intent intent) {
		intent.setClass(this, cls);
		startActivity(intent);
	}

	public MMImageButton setBtn1(String text, View.OnClickListener listener) {
		return this.title.setBtn1(text, listener);
	}

	public MMImageButton setBtn1(int text, View.OnClickListener listener) {
		return this.title.setBtn1(Integer.valueOf(text), listener);
	}

	public MMImageButton setBtn4(View.OnClickListener listener) {
		String text = getResources().getString(R.string.app_back);
		MMImageButton btn4 = this.title.setBtn4(text, listener);
		btn4.setBackgroundDrawable(getDrawableObject(R.drawable.mm_title_btn_back));
		return btn4;
	}

	public void setLeftBtn(int text, View.OnClickListener listener) {
		this.title.setLeftBtn(text, listener);
	}

	public void setRightBtnEnabled(boolean enabled) {
		this.title.setRightBtnEnabled(enabled);
	}

	public void setRightBtn(int text, View.OnClickListener listener) {
		this.title.setRightBtn(text, listener);
	}

	public void setTitleToTop(View.OnClickListener listener) {
		this.title.setTitleToTop(listener);
	}
	
	public void visibleNavTitle(int flag){
		this.title.visibleNavTitle(flag);
	}

	public void enableLogo() {
		this.title.enableLogo();
	}

	public void setTitle(int title) {
		this.title.setTitle(title);
	}

	public void setTitle(String title) {
		this.title.setTitle(title);
	}

	public void visibleRightBtn(int flag) {
		this.title.visibleRightBtn(flag);
	}
	
	public void visiblePhone(int flag){
		this.title.visiblePhone(flag);
	}
	
	public void visibleMute(int flag){
		this.title.visibleMute(flag);
	}
	
	public void disableLeftBtn(){
		this.title.disableLeftBtn();
	}
	
	public View getTitleView(){
		return this.title.getRoot();
	}

	protected int getTitleLayoutId() {
		return R.layout.mm_title;
	}
	
	public void onBodyTouch() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		if (imm != null) {
			View view = getCurrentFocus();
			if (view != null) {
				IBinder binder = view.getWindowToken();
				if (binder != null) {
					imm.hideSoftInputFromWindow(binder, 0);
				}
			}
		}
	}

	/**
	 * 通过ID获得皮肤的绘图对象
	 * 
	 * @param id
	 * @return
	 */
	public final Drawable getDrawableObject(int id) {
		if (this.skinFact != null) {
			return this.skinFact.getDrawableObject(id);
		}
		return getResources().getDrawable(id);
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		// 确保服务启动
		MMService.operate(this, "noop");

		// 设置locale
		String str = MMLocaleUtils.getLanguage(getSharedPreferences(
				"com.plywet.mm_preferences", 0));
		if (str.equals("zh_TW") || str.equals("zh_HK"))
			MMLocaleUtils.setLocale(this, Locale.TAIWAN);
		else if (str.equals("en"))
			MMLocaleUtils.setLocale(this, Locale.ENGLISH);
		else if (str.equals("zh_CN"))
			MMLocaleUtils.setLocale(this, Locale.CHINA);

		// 设置AudioMananger
		this.am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		this.maxVolumn = this.am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// 设置显示布局
		this.li = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

		if (this.skinFact == null) {
			this.skinFact = new MMSkinFactory(this);

			String pkgName = this.skinFact.getSkinPackageName();
			if (pkgName != null && !"".equals(pkgName)) {
				this.li.setFactory(this.skinFact);
			}
		}

		// 根布局使用mm_activity
		this.activityView = this.li.inflate(R.layout.mm_activity, null);
		this.activityLinearLayout = (LinearLayout) this.activityView
				.findViewById(R.id.mm_root_view);
		this.activityBtn = this.activityView.findViewById(R.id.mm_trans_layer);

		// 添加title，布局使用mm_title
		int titleLayoutId = this.getTitleLayoutId();
		if (titleLayoutId != -1) {
			this.activityLinearLayout.addView(
					this.li.inflate(titleLayoutId, null),
					WindowManager.LayoutParams.FILL_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
		}

		// 添加centent,布局id来自getCententLayoutId抽象方法
		int cententLayoutId = getCententLayoutId();
		if (cententLayoutId != -1) {
			this.activityContent = this.li.inflate(cententLayoutId, null);
			this.activityLinearLayout.addView(this.activityContent,
					WindowManager.LayoutParams.FILL_PARENT,
					WindowManager.LayoutParams.FILL_PARENT);
			setContentView(this.activityView);
		}
		
		// 初始化title
		this.title = new MMTitle(this);

		// 判断是否有主体滚动视图,
		ScrollView bodySv = (ScrollView) findViewById(R.id.mm_body_sv);
		if (bodySv != null) {
			bodySv.setOnTouchListener(new MMBodyScrollViewTouchListener(this));
		}

	}

	@Override
	protected void onDestroy() {
		if (this.skinFact != null) {
			this.skinFact.destroy();
			this.skinFact = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int key, KeyEvent event) {
		if (key == KeyEvent.KEYCODE_VOLUME_DOWN) {
			int vol = this.am.getStreamVolume(AudioManager.STREAM_MUSIC);
			int vol_per = this.maxVolumn / 7;
			if (vol_per == 0) {
				vol_per = 1;
			}
			this.am.setStreamVolume(AudioManager.STREAM_MUSIC, vol - vol_per, 5);
		} else if (key == KeyEvent.KEYCODE_VOLUME_UP) {
			int vol = this.am.getStreamVolume(AudioManager.STREAM_MUSIC);
			int vol_per = this.maxVolumn / 7;
			if (vol_per == 0) {
				vol_per = 1;
			}
			this.am.setStreamVolume(AudioManager.STREAM_MUSIC, vol + vol_per, 5);
		}

		return super.onKeyDown(key, event);
	}

	protected void onPause() {
		super.onPause();

	}

	/**
	 * 获得内容的布局ID
	 * 
	 * @return
	 */
	protected abstract int getCententLayoutId();

}

class MMBodyScrollViewTouchListener implements View.OnTouchListener {

	private MMActivity act;

	MMBodyScrollViewTouchListener(MMActivity act) {
		this.act = act;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		this.act.onBodyTouch();
		return false;
	}
}

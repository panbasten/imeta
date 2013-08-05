package com.plywet.mm.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.plywet.mm.R;
import com.plywet.mm.ui.login.MMActionInterface;

/**
 * 对话框帮助类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-12 下午02:08:17
 */
public class MMDialogUtils {

	/**
	 * 显示一个Alert对话框，带有默认取消按钮
	 * 
	 * @param context
	 * @param msg
	 * @param title
	 * @return
	 */
	public static AlertDialog showAlertDialog(Context context, int msg,
			int title) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.app_ok, new MMCancelListener());
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static AlertDialog showAlertDialog(Context context, String msg,
			String title) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.app_ok, new MMCancelListener());
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	/**
	 * 显示一个Alert对话框，带有自定义事件的确定按钮
	 * 
	 * @param context
	 * @param msg
	 * @param title
	 * @param listener
	 * @return
	 */
	public static AlertDialog showAlertDialog(Context context, int msg,
			int title, DialogInterface.OnClickListener listener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.app_ok, listener);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static AlertDialog showAlertDialog(Context context, String msg,
			String title, DialogInterface.OnClickListener listener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.app_ok, listener);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	/**
	 * 显示Alert对话框，自定义显示内容和确认事件
	 * 
	 * @param context
	 * @param msg
	 * @param title
	 * @param ok
	 * @param cancel
	 * @param listener
	 * @return
	 */
	public static AlertDialog showAlertDialog(Context context, String msg,
			String title, String ok, String cancel,
			DialogInterface.OnClickListener listener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(ok, listener);
		builder.setNegativeButton(cancel, null);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	/**
	 * 显示一个Alert对话框，带有自定义的确定和取消按钮
	 * 
	 * @param context
	 * @param msg
	 * @param title
	 * @param posListener
	 * @param negListener
	 * @return
	 */
	public static AlertDialog showAlertDialog(Context context, int msg,
			int title, DialogInterface.OnClickListener posListener,
			DialogInterface.OnClickListener negListener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.app_ok, posListener);
		builder.setNegativeButton(R.string.app_cancel, negListener);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static AlertDialog showAlertDialog(Context context, String msg,
			String title, DialogInterface.OnClickListener posListener,
			DialogInterface.OnClickListener negListener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.app_ok, posListener);
		builder.setNegativeButton(R.string.app_cancel, negListener);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	/**
	 * 显示一个Alert对话框，自定义显示
	 * 
	 * @param context
	 * @param title
	 * @param view
	 * @param listener
	 * @return
	 */
	public static AlertDialog showAlertDialog(Context context, String title,
			View view, DialogInterface.OnClickListener listener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setView(view);
		builder.setPositiveButton(R.string.app_ok, listener);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static AlertDialog showAlertDialog(Context context, String title,
			View view, DialogInterface.OnClickListener posListener,
			DialogInterface.OnClickListener negListener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(null);
		builder.setView(view);
		builder.setPositiveButton(R.string.app_ok, posListener);
		builder.setNegativeButton(R.string.app_cancel, negListener);
		builder.setCancelable(true);
		builder.setOnCancelListener(new MMCustomCancelListener(negListener));
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	/**
	 * 显示推出对话框
	 * 
	 * @param context
	 * @param listener
	 * @return
	 */
	public static AlertDialog showExitDialog(Context context,
			DialogInterface.OnClickListener listener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(R.string.main_exit_title);
		builder.setMessage(R.string.main_exit_warning);
		builder.setPositiveButton(R.string.app_yes, listener);
		builder.setNegativeButton(R.string.app_no, null);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static Dialog showDialog(Context context, String title,
			String[] menus, String special, MMActionInterface action) {
		return showDialog(context, title, menus, special, action, null);
	}

	public static Dialog showDialog(Context context, String title,
			String[] menus, String special, MMActionInterface action,
			DialogInterface.OnCancelListener cancelListener) {
		String cancelStr = context.getString(R.string.app_cancel);
		Dialog dialog = new Dialog(context, R.style.mmdialog);

		LinearLayout ll = (LinearLayout) ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.alert_dialog_menu_layout, null);
		ll.setMinimumWidth(10000);

		ListView listView = (ListView) ll.findViewById(R.id.content_list);
		listView.setAdapter(new MMDialogAdapter(context, title, menus, special,
				cancelStr));

		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new MMDialogItemListener(title, action,
				dialog, listView));

		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

		params.x = 0;
		params.y = -1000;
		params.gravity = Gravity.BOTTOM;
		params.height = WindowManager.LayoutParams.FILL_PARENT;

		dialog.onWindowAttributesChanged(params);
		dialog.setCanceledOnTouchOutside(true);
		if (cancelListener != null) {
			dialog.setOnCancelListener(cancelListener);
		}
		dialog.setContentView(ll);
		dialog.show();
		return dialog;
	}

	public static ProgressDialog showProgressDialog(Context context,
			String title, String message,
			DialogInterface.OnCancelListener cancelListener) {
		MMAppMgr.setAliveState(true);
		return ProgressDialog.show(context, title, message, true, true,
				cancelListener);
	}
}

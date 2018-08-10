/**
 *
 */
package com.shouyubang.android.sybang.utils;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Toast;

import com.shouyubang.android.sybang.MainActivity;

public class DialogUtil {

    // 最大进度
    private static final int MAX_PROGRESS = 100;

    // 定义一个显示消息的对话框
	public static void showDialog(final Context content, String msg , boolean goHome)
	{
		if(content == null)
            return;
		
		// 创建一个AlertDialog.Builder对象
		AlertDialog.Builder builder = new AlertDialog.Builder(content).setMessage(msg);
		builder.setNegativeButton("取消", null);
		if(goHome)
		{
			builder.setPositiveButton("确定", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent i = new Intent(content , MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					content.startActivity(i);
				}
			});
		}
		else
		{
			builder.setPositiveButton("确定", null);
		}
		builder.create().show();
	}
	// 定义一个显示指定组件的对话框
	public static void showDialog(Context content , View view)
	{
		new AlertDialog.Builder(content)
			.setView(view).setCancelable(false)
			.setPositiveButton("确定", null)
			.create()
			.show();
	}

    public static ProgressDialog getProgressDialog(Context context, String message, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(message);
        progressDialog.setMessage("请稍候...");
        // 设置进度对话框的风格
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置进度对话框的进度最大值
        progressDialog.setMax(MAX_PROGRESS);
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }

	/**
	 * @function: 对屏幕中间显示一个Toast，其内容为msg
	 * */
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static BottomSheetDialog getBottomDialog(Context context, int viewId, boolean canceledOnTouchOutside, boolean cancelable) {
		final BottomSheetDialog d = new BottomSheetDialog(context);
		d.setContentView(viewId);
		return d;
	}

	public static BottomSheetDialog getBottomDialog(Context context, int viewId) {
		return getBottomDialog(context, viewId, true, true);
	}


}

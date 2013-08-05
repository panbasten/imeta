package com.plywet.mm.dao;

import android.os.Environment;

public class PathConst {
	public static final String absolutePath = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public static final String msgPath = absolutePath + "/tencent/MicroMsg/";
	public static final String msgCameraPath = absolutePath
			+ "/tencent/MicroMsg/Camera/";
	public static final String msgVideaPath = absolutePath
			+ "/tencent/MicroMsg/Video/";
	public static final String msgRootPath = msgPath;
}

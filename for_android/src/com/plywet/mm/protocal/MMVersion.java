package com.plywet.mm.protocal;

import android.os.Build;

public class MMVersion {

	public static final String SYSTEM_SDK_VERSION = "android-"
			+ Build.VERSION.SDK;

	public static int TEST_PROTOCAL_VERSION = 587268181;

	public static void setTestProtocalVersion(int ver) {
		TEST_PROTOCAL_VERSION = ver;
	}
}

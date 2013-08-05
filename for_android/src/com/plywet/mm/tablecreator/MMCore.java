package com.plywet.mm.tablecreator;

import java.io.File;

import android.content.Context;

import com.plywet.mm.core.AccountStorage;
import com.plywet.mm.dao.PathConst;
import com.plywet.mm.dao.TConfigDao;
import com.plywet.mm.platformtools.Utils;

public class MMCore {

	//private final AccountStorage as;

	private final String path = checkPath();

	private static MMCore mmCore = null;
	
	private Context context;

	private final TConfigDao configDao = new TConfigDao(
			"/data/data/com.plywet.mm/MicroMsg/systemInfo.cfg");

	private MMCore() {
		if (Utils.defaultInteger((Integer) configDao.get(17)) != 0) {
			//this.as = new AccountStorage(this.path,null);
		}

	}

	public static String checkPath() {
		String path = "/data/data/com.tencent.mm/MicroMsg/";

		if (new File(PathConst.absolutePath).exists()) {
			File msg = new File(PathConst.msgPath);
			if (msg.exists() || msg.mkdirs()) {
				path = PathConst.msgPath;
			}

			File camera = new File(PathConst.msgCameraPath);
			if (!camera.exists()) {
				camera.mkdirs();
			}

			File videa = new File(PathConst.msgVideaPath);
			if (!videa.exists()) {
				videa.mkdirs();
			}
		}

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		return path;
	}
	
	public static void setContext(Context context){
		getInstance().context = context;
	}
	
	private static MMCore getInstance(){
		if(mmCore == null){
			mmCore = new MMCore();
		}
		return mmCore;
	}
	
	public static void m(){
		//getInstance()
	}
}

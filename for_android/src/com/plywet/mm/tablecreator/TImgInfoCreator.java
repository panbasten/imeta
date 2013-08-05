package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TImgInfoDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TImgInfoCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TImgInfoDao.sqls;
	}

}

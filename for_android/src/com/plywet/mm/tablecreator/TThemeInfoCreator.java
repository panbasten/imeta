package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TThemeInfoDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TThemeInfoCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TThemeInfoDao.sqls;
	}

}

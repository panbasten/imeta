package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TVUserInfoDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TVUserInfoCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TVUserInfoDao.sqls;
	}

}

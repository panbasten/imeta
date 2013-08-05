package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TRoleInfoDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TRoleInfoCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TRoleInfoDao.sqls;
	}

}

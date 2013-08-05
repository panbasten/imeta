package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TNetstatDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TNetstatCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TNetstatDao.sqls;
	}

}

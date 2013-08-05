package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TConfigDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TConfigCreator implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TConfigDao.sqls;
	}

}

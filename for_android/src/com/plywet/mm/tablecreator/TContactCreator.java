package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TContactDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TContactCreator implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TContactDao.sqls;
	}

}
package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TVerifyContactDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TVerifyContactCreator implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TVerifyContactDao.sqls;
	}

}

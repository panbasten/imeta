package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TTContactDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TTContactCreator implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TTContactDao.sqls;
	}

}

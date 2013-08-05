package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TMessageDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TMessageCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TMessageDao.sqls;
	}

}
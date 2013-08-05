package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TQqGroupDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TQqGroupCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TQqGroupDao.sqls;
	}

}

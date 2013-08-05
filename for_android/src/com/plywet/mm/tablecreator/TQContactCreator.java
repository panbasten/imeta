package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TQContactDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TQContactCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TQContactDao.sqls;
	}

}

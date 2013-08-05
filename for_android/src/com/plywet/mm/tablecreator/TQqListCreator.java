package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TQqListDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TQqListCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TQqListDao.sqls;
	}

}

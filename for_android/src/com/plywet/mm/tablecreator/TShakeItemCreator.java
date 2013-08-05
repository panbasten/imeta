package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TShakeItemDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TShakeItemCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TShakeItemDao.sqls;
	}

}

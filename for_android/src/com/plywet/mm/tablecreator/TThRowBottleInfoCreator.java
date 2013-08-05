package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TThRowBottleInfoDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TThRowBottleInfoCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TThRowBottleInfoDao.sqls;
	}

}

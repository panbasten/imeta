package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TVideoInfoDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TVideoInfoCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TVideoInfoDao.sqls;
	}

}

package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TImgFlagDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TImgFlagCreator implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TImgFlagDao.sqls;
	}

}

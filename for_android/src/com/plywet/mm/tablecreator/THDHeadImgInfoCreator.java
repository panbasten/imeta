package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.THDHeadImgInfoDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class THDHeadImgInfoCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return THDHeadImgInfoDao.sqls;
	}

}

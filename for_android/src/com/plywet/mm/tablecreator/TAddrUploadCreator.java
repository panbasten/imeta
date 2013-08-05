package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TAddrUploadDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TAddrUploadCreator implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TAddrUploadDao.sqls;
	}

}

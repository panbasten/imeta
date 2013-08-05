package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TFriendExtDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TFriendExtCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TFriendExtDao.sqls;
	}

}

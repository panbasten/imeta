package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TVoiceDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TVoiceCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TVoiceDao.sqls;
	}

}

package com.plywet.mm.tablecreator;

import com.plywet.mm.dao.TInviteFriendOpenDao;
import com.plywet.mm.database.MMTableCreatorInterface;

public class TInviteFriendOpenCreator  implements MMTableCreatorInterface {

	@Override
	public String[] getCreateTableSqls() {
		return TInviteFriendOpenDao.sqls;
	}

}

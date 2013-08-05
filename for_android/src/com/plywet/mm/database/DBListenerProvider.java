package com.plywet.mm.database;

public class DBListenerProvider extends MMListenerProvider {

	DBListenerProvider(DBListenerProxy proxy) {

	}

	@Override
	protected void dealListener(Object lis, Object obj) {
		if (lis instanceof DBListenerInterface && obj instanceof String)
			((DBListenerInterface) lis).deal((String) obj);
	}

}

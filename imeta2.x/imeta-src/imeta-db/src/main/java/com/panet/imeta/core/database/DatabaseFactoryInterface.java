package com.panet.imeta.core.database;

import com.panet.imeta.core.exception.KettleDatabaseException;

public interface DatabaseFactoryInterface {
	
	public String getConnectionTestReport(DatabaseMeta databaseMeta) throws KettleDatabaseException;

}

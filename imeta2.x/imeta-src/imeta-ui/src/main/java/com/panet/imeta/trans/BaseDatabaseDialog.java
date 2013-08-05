package com.panet.imeta.trans;

import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.repository.Repository;

public class BaseDatabaseDialog implements Cloneable {
	private DatabaseMeta databaseMeta;

	protected Repository rep;

	public DatabaseMeta getDatabaseMeta() {
		return databaseMeta;
	}

	public void setDatabaseMeta(DatabaseMeta databaseMeta) {
		this.databaseMeta = databaseMeta;
	}

	public void setRepository(Repository repository) {
		this.rep = repository;
	}

	public BaseDatabaseDialog(DatabaseMeta databaseMeta, Repository rep) {
		this.databaseMeta = databaseMeta;
		this.rep = rep;
	}

	public String getId() {
		return "database_" + this.databaseMeta.getID();
	}

}

package com.panet.imeta.core.database;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;

// import plugin.palo.core.PaloHelper;

public class PaloDatabaseFactory implements DatabaseFactoryInterface {

	public String getConnectionTestReport(DatabaseMeta databaseMeta) throws KettleDatabaseException {

		StringBuffer report = new StringBuffer();
		
		PaloHelper helper = new PaloHelper(databaseMeta);
		try {
			helper.connect();
			
			// If the connection was successful
			//
			report.append("Connecting to PALO server [").append(databaseMeta.getName()).append("] went without a problem.").append(Const.CR);
			
		} catch (KettleException e) {
			report.append("Unable to connect to the PALO server: ").append(e.getMessage()).append(Const.CR);
			report.append(Const.getStackTracker(e));
		}
		finally
		{
			helper.disconnect();	
		}
		
		return report.toString();
	}

}

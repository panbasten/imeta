/* Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.*/

package com.panet.imeta.job.entries.tableexists;

import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;

/**
 * This defines a table exists job entry.
 * 
 * @author Matt
 * @since 05-11-2003
 * 
 */
public class JobEntryTableExists extends JobEntryBase implements Cloneable,
		JobEntryInterface {
	private String tablename;
	private String schemaname;
	private DatabaseMeta connection;

	public JobEntryTableExists(String n) {
		super(n, "");
		schemaname = null;
		tablename = null;
		connection = null;
		setID(-1L);
		setJobEntryType(JobEntryType.TABLE_EXISTS);
	}

	public JobEntryTableExists() {
		this("");
	}

	public JobEntryTableExists(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntryTableExists je = (JobEntryTableExists) super.clone();
		return je;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(200);

		retval.append(super.getXML());

		retval.append("      ").append(
				XMLHandler.addTagValue("tablename", tablename));
		retval.append("      ").append(
				XMLHandler.addTagValue("schemaname", schemaname));
		retval.append("      ").append(
				XMLHandler.addTagValue("connection", connection == null ? null
						: connection.getName()));

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers, Repository rep)
			throws KettleXMLException {
		try {
			super.loadXML(entrynode, databases, slaveServers);

			tablename = XMLHandler.getTagValue(entrynode, "tablename");
			schemaname = XMLHandler.getTagValue(entrynode, "schemaname");
			String dbname = XMLHandler.getTagValue(entrynode, "connection");
			connection = DatabaseMeta.findDatabase(databases, dbname);
		} catch (KettleException e) {
			throw new KettleXMLException(Messages
					.getString("TableExists.Meta.UnableLoadXml"), e);
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		tablename = JobEntryBase.parameterToString(p.get(id + ".tablename"));
		schemaname = JobEntryBase.parameterToString(p.get(id + ".schemaname"));
		connection  = DatabaseMeta.findDatabase(
				(List<DatabaseMeta>) databases, JobEntryBase.parameterToLong(p
						.get(id + ".connection")));
	}

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			super.loadRep(rep, id_jobentry, databases, slaveServers);

			tablename = rep
					.getJobEntryAttributeString(id_jobentry, "tablename");
			schemaname = rep.getJobEntryAttributeString(id_jobentry,
					"schemaname");

			long id_db = rep.getJobEntryAttributeInteger(id_jobentry,
					"id_database");
			if (id_db > 0) {
				connection = DatabaseMeta.findDatabase(databases, id_db);
			} else {
				// This is were we end up in normally, the previous lines are
				// for backward compatibility.
				connection = DatabaseMeta.findDatabase(databases, rep
						.getJobEntryAttributeString(id_jobentry, "connection"));
			}

		} catch (KettleDatabaseException dbe) {
			throw new KettleException(Messages.getString(
					"TableExists.Meta.UnableLoadRep", "" + id_jobentry), dbe);
		}
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			super.saveRep(rep, id_job);

			rep.saveJobEntryAttribute(id_job, getID(), "tablename", tablename);
			rep
					.saveJobEntryAttribute(id_job, getID(), "schemaname",
							schemaname);

			if (connection != null)
				rep.saveJobEntryAttribute(id_job, getID(), "connection",
						connection.getName());
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(Messages.getString(
					"TableExists.Meta.UnableSaveRep", "" + id_job), dbe);
		}
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getTablename() {
		return tablename;
	}

	public String getSchemaname() {
		return schemaname;
	}

	public void setSchemaname(String schemaname) {
		this.schemaname = schemaname;
	}

	public void setDatabase(DatabaseMeta database) {
		this.connection = database;
	}

	public DatabaseMeta getDatabase() {
		return connection;
	}

	public boolean evaluates() {
		return true;
	}

	public boolean isUnconditional() {
		return false;
	}

	public Result execute(Result previousResult, int nr, Repository rep,
			Job parentJob) {
		LogWriter log = LogWriter.getInstance();

		Result result = previousResult;
		result.setResult(false);

		if (connection != null) {
			Database db = new Database(connection);
			db.shareVariablesWith(this);
			try {
				db.connect();
				String realTablename = environmentSubstitute(tablename);
				String realSchemaname = environmentSubstitute(schemaname);
				if (!Const.isEmpty(realSchemaname)) {
					realTablename = db.getDatabaseMeta()
							.getQuotedSchemaTableCombination(realSchemaname,
									realTablename);
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"TableExists.Log.SchemaTable", realTablename));
				} else
					realTablename = db.getDatabaseMeta().quoteField(
							realTablename);

				if (db.checkTableExists(realTablename)) {
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"TableExists.Log.TableExists", realTablename));
					result.setResult(true);
				} else {
					if (log.isDetailed())
						log.logDetailed(toString(), Messages
								.getString("TableExists.Log.TableNotExists",
										realTablename));
				}
			} catch (KettleDatabaseException dbe) {
				result.setNrErrors(1);
				log.logError(toString(), Messages.getString(
						"TableExists.Error.RunningJobEntry", dbe.getMessage()));
			} finally {
				if (db != null)
					try {
						db.disconnect();
					} catch (Exception e) {
					}
				;
			}
		} else {
			result.setNrErrors(1);
			log.logError(toString(), Messages
					.getString("TableExists.Error.NoConnectionDefined"));
		}

		return result;
	}

	public DatabaseMeta[] getUsedDatabaseConnections() {
		return new DatabaseMeta[] { connection, };
	}

	public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
		List<ResourceReference> references = super
				.getResourceDependencies(jobMeta);
		if (connection != null) {
			ResourceReference reference = new ResourceReference(this);
			reference.getEntries().add(
					new ResourceEntry(connection.getHostname(),
							ResourceType.SERVER));
			reference.getEntries().add(
					new ResourceEntry(connection.getDatabaseName(),
							ResourceType.DATABASENAME));
			references.add(reference);
		}
		return references;
	}

	@Override
	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {
		andValidator().validate(this,
				"tablename", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
	}

}

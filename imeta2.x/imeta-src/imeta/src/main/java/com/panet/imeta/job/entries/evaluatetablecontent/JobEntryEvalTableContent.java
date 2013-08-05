/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Samatar HASSAN.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 */

package com.panet.imeta.job.entries.evaluatetablecontent;

import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.row.RowMetaInterface;
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
 * This defines a Table content evaluation job entry
 * 
 * @author Samatar
 * @since 22-07-2008
 * 
 */
public class JobEntryEvalTableContent extends JobEntryBase implements
		Cloneable, JobEntryInterface {
	public static final String ENTRY_ATTRIBUTE_CONNECTION = "connection";
	public static final String ENTRY_ATTRIBUTE_SCHEMANAME = "schemaname";
	public static final String ENTRY_ATTRIBUTE_TABLENAME = "tablename";
	public static final String ENTRY_ATTRIBUTE_SUCCESS_CONDITION = "success_condition";
	public static final String ENTRY_ATTRIBUTE_LIMIT = "limit";
	public static final String ENTRY_ATTRIBUTE_IS_CUSTOM_SQL = "is_custom_sql";
	public static final String ENTRY_ATTRIBUTE_IS_USEVARS = "is_usevars";
	public static final String ENTRY_ATTRIBUTE_CUSTOM_SQL = "custom_sql";
	public static final String ENTRY_ATTRIBUTE_ADD_ROWS_RESULT = "add_rows_result";
	public static final String ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS = "clear_result_rows";

	public boolean isAddRowsResult;

	public boolean isClearResultList;

	public boolean isUseVars;

	public boolean iscustomSQL;

	public String customSQL;

	private DatabaseMeta connection;

	public String tablename;

	public String schemaname;

	private static final String selectCount = "SELECT count(*) FROM ";

	public static final String[] successConditionsDesc = new String[] {
			Messages
					.getString("JobEntryEvalTableContent.SuccessWhenRowCountEqual.Label"),
			Messages
					.getString("JobEntryEvalTableContent.SuccessWhenRowCountDifferent.Label"),
			Messages
					.getString("JobEntryEvalTableContent.SuccessWhenRowCountSmallerThan.Label"),
			Messages
					.getString("JobEntryEvalTableContent.SuccessWhenRowCountSmallerOrEqualThan.Label"),
			Messages
					.getString("JobEntryEvalTableContent.SuccessWhenRowCountGreaterThan.Label"),
			Messages
					.getString("JobEntryEvalTableContent.SuccessWhenRowCountGreaterOrEqual.Label")

	};
	public static final String[] successConditionsCode = new String[] {
			"rows_count_equal", "rows_count_different", "rows_count_smaller",
			"rows_count_smaller_equal", "rows_count_greater",
			"rows_count_greater_equal" };

	public static final int SUCCESS_CONDITION_ROWS_COUNT_EQUAL = 0;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_DIFFERENT = 1;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_SMALLER = 2;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_SMALLER_EQUAL = 3;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_GREATER = 4;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_GREATER_EQUAL = 5;

	public String limit;
	public int successCondition;

	public JobEntryEvalTableContent(String n) {
		super(n, "");
		limit = "0";
		successCondition = SUCCESS_CONDITION_ROWS_COUNT_GREATER;
		iscustomSQL = false;
		isUseVars = false;
		isAddRowsResult = false;
		isClearResultList = true;
		customSQL = null;
		schemaname = null;
		tablename = null;
		connection = null;
		setID(-1L);
		setJobEntryType(JobEntryType.EVAL_TABLE_CONTENT);
	}

	public JobEntryEvalTableContent() {
		this("");
	}

	public JobEntryEvalTableContent(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntryEvalTableContent je = (JobEntryEvalTableContent) super.clone();
		return je;
	}

	public int getSuccessCobdition() {
		return successCondition;
	}

	public static int getSuccessConditionByDesc(String tt) {
		if (tt == null)
			return 0;

		for (int i = 0; i < successConditionsDesc.length; i++) {
			if (successConditionsDesc[i].equalsIgnoreCase(tt))
				return i;
		}

		// If this fails, try to match using the code.
		return getSuccessConditionByCode(tt);
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(200);

		retval.append(super.getXML());
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_CONNECTION,
						connection == null ? null : connection.getName()));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_SCHEMANAME, schemaname));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_TABLENAME, tablename));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_SUCCESS_CONDITION,
						getSuccessConditionCode(successCondition)));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_LIMIT, limit));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_IS_CUSTOM_SQL,
						iscustomSQL));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_IS_USEVARS, isUseVars));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_CUSTOM_SQL, customSQL));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_ADD_ROWS_RESULT,
						isAddRowsResult));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS,
						isClearResultList));

		return retval.toString();
	}

	private static String getSuccessConditionCode(int i) {
		if (i < 0 || i >= successConditionsCode.length)
			return successConditionsCode[0];
		return successConditionsCode[i];
	}

	private static int getSucessConditionByCode(String tt) {
		if (tt == null)
			return 0;

		for (int i = 0; i < successConditionsCode.length; i++) {
			if (successConditionsCode[i].equalsIgnoreCase(tt))
				return i;
		}
		return 0;
	}

	public static String getSuccessConditionDesc(int i) {
		if (i < 0 || i >= successConditionsDesc.length)
			return successConditionsDesc[0];
		return successConditionsDesc[i];
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers, Repository rep)
			throws KettleXMLException {
		try {
			super.loadXML(entrynode, databases, slaveServers);
			String dbname = XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_CONNECTION);
			connection = DatabaseMeta.findDatabase(databases, dbname);
			schemaname = XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_SCHEMANAME);
			tablename = XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_TABLENAME);
			successCondition = getSucessConditionByCode(Const.NVL(XMLHandler
					.getTagValue(entrynode, ENTRY_ATTRIBUTE_SUCCESS_CONDITION),
					""));
			limit = Const.NVL(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_LIMIT), "0");
			iscustomSQL = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, ENTRY_ATTRIBUTE_IS_CUSTOM_SQL));
			isUseVars = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_IS_USEVARS));
			customSQL = XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_CUSTOM_SQL);
			isAddRowsResult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, ENTRY_ATTRIBUTE_ADD_ROWS_RESULT));
			isClearResultList = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS));

		} catch (KettleException e) {
			throw new KettleXMLException(Messages
					.getString("JobEntryEvalTableContent.UnableLoadXML"), e);
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		connection = DatabaseMeta.findDatabase((List<DatabaseMeta>) databases, JobEntryBase
				.parameterToLong(p.get(id + ".connection")));
		schemaname = JobEntryBase.parameterToString(p.get(id + ".schemaname"));
		tablename = JobEntryBase.parameterToString(p.get(id + ".tablename"));
		successCondition = JobEntryBase.parameterToInt(p.get(id
				+ ".successCondition"));
		limit = JobEntryBase.parameterToString(p.get(id + ".limit"));
		iscustomSQL = JobEntryBase.parameterToBoolean(p
				.get(id + ".iscustomSQL"));
		isUseVars = JobEntryBase.parameterToBoolean(p.get(id + ".isUseVars"));
		isAddRowsResult = JobEntryBase.parameterToBoolean(p.get(id
				+ ".isAddRowsResult"));
		isClearResultList = JobEntryBase.parameterToBoolean(p.get(id
				+ ".isClearResultList"));
		customSQL = JobEntryBase.parameterToString(p.get(id + ".customSQL"));
	}

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			super.loadRep(rep, id_jobentry, databases, slaveServers);

			long id_db = rep.getJobEntryAttributeInteger(id_jobentry,
					"id_database");
			if (id_db > 0) {
				connection = DatabaseMeta.findDatabase(databases, id_db);
			} else {
				// This is were we end up in normally, the previous lines are
				// for backward compatibility.
				connection = DatabaseMeta.findDatabase(databases, rep
						.getJobEntryAttributeString(id_jobentry,
								ENTRY_ATTRIBUTE_CONNECTION));
			}

			schemaname = rep.getJobEntryAttributeString(id_jobentry,
					ENTRY_ATTRIBUTE_SCHEMANAME);
			tablename = rep.getJobEntryAttributeString(id_jobentry,
					ENTRY_ATTRIBUTE_TABLENAME);
			successCondition = getSuccessConditionByCode(Const.NVL(rep
					.getStepAttributeString(id_jobentry,
							ENTRY_ATTRIBUTE_SUCCESS_CONDITION), ""));
			limit = rep.getJobEntryAttributeString(id_jobentry,
					ENTRY_ATTRIBUTE_LIMIT);
			iscustomSQL = rep.getJobEntryAttributeBoolean(id_jobentry,
					ENTRY_ATTRIBUTE_IS_CUSTOM_SQL);
			isUseVars = rep.getJobEntryAttributeBoolean(id_jobentry,
					ENTRY_ATTRIBUTE_IS_USEVARS);
			isAddRowsResult = rep.getJobEntryAttributeBoolean(id_jobentry,
					ENTRY_ATTRIBUTE_ADD_ROWS_RESULT);
			isClearResultList = rep.getJobEntryAttributeBoolean(id_jobentry,
					ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS);

			customSQL = rep.getJobEntryAttributeString(id_jobentry,
					ENTRY_ATTRIBUTE_CUSTOM_SQL);
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					Messages.getString(
							"JobEntryEvalTableContent.UnableLoadRep", ""
									+ id_jobentry), dbe);
		}
	}

	private static int getSuccessConditionByCode(String tt) {
		if (tt == null)
			return 0;

		for (int i = 0; i < successConditionsCode.length; i++) {
			if (successConditionsCode[i].equalsIgnoreCase(tt))
				return i;
		}
		return 0;
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			super.saveRep(rep, id_job);

			if (connection != null)
				rep.saveJobEntryAttribute(id_job, getID(),
						ENTRY_ATTRIBUTE_CONNECTION, connection.getName());

			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_SCHEMANAME, schemaname);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_TABLENAME, tablename);
			rep.saveStepAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_SUCCESS_CONDITION,
					getSuccessConditionCode(successCondition));
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_LIMIT,
					limit);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_CUSTOM_SQL, customSQL);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_IS_CUSTOM_SQL, iscustomSQL);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_IS_USEVARS, isUseVars);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_ADD_ROWS_RESULT, isAddRowsResult);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS, isClearResultList);

		} catch (KettleDatabaseException dbe) {
			throw new KettleException(Messages.getString(
					"JobEntryEvalTableContent.UnableSaveRep", "" + id_job), dbe);
		}
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
		result.setNrErrors(1);
		String countSQLStatement = null;
		long rowsCount = 0;

		boolean successOK = false;

		int nrRowsLimit = Const.toInt(environmentSubstitute(limit), 0);
		if (log.isDetailed())
			log.logDetailed(toString(), Messages.getString(
					"JobEntryEvalTableContent.Log.nrRowsLimit", ""
							+ nrRowsLimit));

		if (connection != null) {
			Database db = new Database(connection);
			try {
				db.connect();

				if (iscustomSQL) {
					String realCustomSQL = customSQL;
					if (isUseVars)
						realCustomSQL = environmentSubstitute(realCustomSQL);
					if (log.isDebug())
						log
								.logDebug(
										toString(),
										Messages
												.getString(
														"JobEntryEvalTableContent.Log.EnteredCustomSQL",
														realCustomSQL));

					if (!Const.isEmpty(realCustomSQL)) {
						countSQLStatement = realCustomSQL;
					} else
						log
								.logError(
										toString(),
										Messages
												.getString("JobEntryEvalTableContent.Error.NoCustomSQL"));

				} else {
					String realTablename = environmentSubstitute(tablename);
					String realSchemaname = environmentSubstitute(schemaname);

					if (!Const.isEmpty(realTablename)) {
						if (!Const.isEmpty(realSchemaname)) {
							countSQLStatement = selectCount
									+ db.getDatabaseMeta()
											.getQuotedSchemaTableCombination(
													realSchemaname,
													realTablename);
						} else {
							countSQLStatement = selectCount
									+ db.getDatabaseMeta().quoteField(
											realTablename);
						}
					} else
						log
								.logError(
										toString(),
										Messages
												.getString("JobEntryEvalTableContent.Error.NoTableName"));
				}

				if (countSQLStatement != null) {
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"JobEntryEvalTableContent.Log.RunSQLStatement",
								countSQLStatement));

					if (iscustomSQL) {
						if (isClearResultList)
							result.getRows().clear();

						List<Object[]> ar = db.getRows(countSQLStatement, 0);
						if (ar != null) {
							rowsCount = ar.size();

							// ad rows to result
							RowMetaInterface rowMeta = db.getQueryFields(
									countSQLStatement, false);

							List<RowMetaAndData> rows = new ArrayList<RowMetaAndData>();
							;
							for (int i = 0; i < ar.size(); i++) {
								rows
										.add(new RowMetaAndData(rowMeta, ar
												.get(i)));
							}
							if (isAddRowsResult && iscustomSQL)
								if (rows != null)
									result.getRows().addAll(rows);
						} else {
							if (log.isDebug())
								log
										.logDebug(
												toString(),
												Messages
														.getString(
																"JobEntryEvalTableContent.Log.customSQLreturnedNothing",
																countSQLStatement));
						}

					} else {
						RowMetaAndData row = db.getOneRow(countSQLStatement);
						if (row != null) {
							rowsCount = row.getInteger(0);
						}
					}
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"JobEntryEvalTableContent.Log.NrRowsReturned",
								"" + rowsCount));
					switch (successCondition) {
					case JobEntryEvalTableContent.SUCCESS_CONDITION_ROWS_COUNT_EQUAL:
						successOK = (rowsCount == nrRowsLimit);
						break;
					case JobEntryEvalTableContent.SUCCESS_CONDITION_ROWS_COUNT_DIFFERENT:
						successOK = (rowsCount != nrRowsLimit);
						break;
					case JobEntryEvalTableContent.SUCCESS_CONDITION_ROWS_COUNT_SMALLER:
						successOK = (rowsCount < nrRowsLimit);
						break;
					case JobEntryEvalTableContent.SUCCESS_CONDITION_ROWS_COUNT_SMALLER_EQUAL:
						successOK = (rowsCount <= nrRowsLimit);
						break;
					case JobEntryEvalTableContent.SUCCESS_CONDITION_ROWS_COUNT_GREATER:
						successOK = (rowsCount > nrRowsLimit);
						break;
					case JobEntryEvalTableContent.SUCCESS_CONDITION_ROWS_COUNT_GREATER_EQUAL:
						successOK = (rowsCount >= nrRowsLimit);
						break;
					default:
						break;
					}
				} // endif countSQLStatement!=null
			} catch (KettleException dbe) {
				log.logError(toString(), Messages.getString(
						"JobEntryEvalTableContent.Error.RunningEntry", dbe
								.getMessage()));
			} finally {
				if (db != null)
					db.disconnect();
			}
		} else {
			log.logError(toString(), Messages
					.getString("JobEntryEvalTableContent.NoDbConnection"));
		}

		if (successOK) {
			result.setResult(true);
			result.setNrErrors(0);
		}
		result.setNrLinesRead(rowsCount);
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
				"WaitForSQL", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
	}

}

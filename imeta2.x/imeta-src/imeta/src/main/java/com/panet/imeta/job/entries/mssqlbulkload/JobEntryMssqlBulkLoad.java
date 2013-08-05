/*************************************************************************************** 
 * Copyright (C) 2007 Samatar  All rights reserved. 
 * This software was developed by Samatar and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. A copy of the license, 
 * is included with the binaries and source code. The Original Code is Samatar.  
 * The Initial Developer is Samatar.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an 
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. 
 * Please refer to the license for the specific language governing your rights 
 * and limitations.
 ***************************************************************************************/

package com.panet.imeta.job.entries.mssqlbulkload;

import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.provider.local.LocalFile;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.ResultFile;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.vfs.KettleVFS;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.job.entry.validator.ValidatorContext;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;

/**
 * This defines a MSSQL Bulk job entry.
 * 
 * @author Samatar Hassan
 * @since Jan-2007
 */
public class JobEntryMssqlBulkLoad extends JobEntryBase implements Cloneable,
		JobEntryInterface {
	
	
	public static final String STEP_ATTRIBUTE_SCHEMANAME="schemaname ";
	public static final String STEP_ATTRIBUTE_TABLENAME ="tablename";
	public static final String STEP_ATTRIBUTE_FILENAME="filename ";
	public static final String STEP_ATTRIBUTE_DATAFILETYPE="datafiletype ";
	public static final String STEP_ATTRIBUTE_FIELDTERMINATOR ="fieldterminator";
	public static final String STEP_ATTRIBUTE_LINETERMINATED="lineterminated ";
	public static final String STEP_ATTRIBUTE_CODEPAGE="codepage ";
	public static final String STEP_ATTRIBUTE_SPECIFICCODEPAGE="specificcodepage ";
	public static final String STEP_ATTRIBUTE_FORMATFILENAME="formatfilename ";
	public static final String STEP_ATTRIBUTE_FIRETRIGGERS="firetriggers ";
	public static final String STEP_ATTRIBUTE_CHECKCONSTRAINTS="checkconstraints ";
	public static final String STEP_ATTRIBUTE_KEEPNULLS ="keepnulls";
	public static final String STEP_ATTRIBUTE_KEEPIDENTITY="keepidentity ";
	public static final String STEP_ATTRIBUTE_TABLOCK ="tablock";
	public static final String STEP_ATTRIBUTE_STARTFILE ="startfile";
	public static final String STEP_ATTRIBUTE_ENDFILE ="endfile";
	public static final String STEP_ATTRIBUTE_ORDERBY ="orderby";
	public static final String STEP_ATTRIBUTE_ORDERDIRECTION="orderdirection ";
	public static final String STEP_ATTRIBUTE_ERRORFILENAME ="errorfilename";
	public static final String STEP_ATTRIBUTE_MAXERRORS ="maxerrors";
	public static final String STEP_ATTRIBUTE_BATCHSIZE ="batchsize";
	public static final String STEP_ATTRIBUTE_ROWSPERBATCH="rowsperbatch ";
	public static final String STEP_ATTRIBUTE_ADDDATETIME ="adddatetime";
	public static final String STEP_ATTRIBUTE_ADDFILETORESULT ="addfiletoresult";
	public static final String STEP_ATTRIBUTE_TRUNCATE="truncate ";
	
	private String schemaname;
	private String tablename;
	private String filename;
	private String datafiletype;
	private String fieldterminator;
	private String lineterminated;
	private String codepage;
	private String specificcodepage;
	private int startfile;
	private int endfile;
	private String orderby;
	private boolean addfiletoresult;
	private String formatfilename;
	private boolean firetriggers;
	private boolean checkconstraints;
	private boolean keepnulls;
	private boolean tablock;
	private String errorfilename;
	private boolean adddatetime;
	private String orderdirection;
	private int maxerrors;
	private int batchsize;
	private int rowsperbatch;
	private boolean keepidentity;
	private boolean truncate;

	private DatabaseMeta connection;

	public JobEntryMssqlBulkLoad(String n) {
		super(n, "");
		tablename = null;
		schemaname = null;
		filename = null;
		datafiletype = "char";
		fieldterminator = null;
		lineterminated = null;
		codepage = "OEM";
		specificcodepage = null;
		checkconstraints = false;
		keepnulls = false;
		tablock = false;
		startfile = 0;
		endfile = 0;
		orderby = null;

		errorfilename = null;
		adddatetime = false;
		orderdirection = "Asc";
		maxerrors = 0;
		batchsize = 0;
		rowsperbatch = 0;

		connection = null;
		addfiletoresult = false;
		formatfilename = null;
		firetriggers = false;
		keepidentity = false;
		truncate = false;
		setID(-1L);
		setJobEntryType(JobEntryType.MSSQL_BULK_LOAD);
	}

	public JobEntryMssqlBulkLoad() {
		this("");
	}

	public JobEntryMssqlBulkLoad(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntryMssqlBulkLoad je = (JobEntryMssqlBulkLoad) super.clone();
		return je;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(200);

		retval.append(super.getXML());
		retval.append("      ").append(
				XMLHandler.addTagValue("schemaname", schemaname));
		retval.append("      ").append(
				XMLHandler.addTagValue("tablename", tablename));
		retval.append("      ").append(
				XMLHandler.addTagValue("filename", filename));

		retval.append("      ").append(
				XMLHandler.addTagValue("datafiletype", datafiletype));
		retval.append("      ").append(
				XMLHandler.addTagValue("fieldterminator", fieldterminator));
		retval.append("      ").append(
				XMLHandler.addTagValue("lineterminated", lineterminated));
		retval.append("      ").append(
				XMLHandler.addTagValue("codepage", codepage));
		retval.append("      ").append(
				XMLHandler.addTagValue("specificcodepage", specificcodepage));
		retval.append("      ").append(
				XMLHandler.addTagValue("formatfilename", formatfilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("firetriggers", firetriggers));
		retval.append("      ").append(
				XMLHandler.addTagValue("checkconstraints", checkconstraints));
		retval.append("      ").append(
				XMLHandler.addTagValue("keepnulls", keepnulls));
		retval.append("      ").append(
				XMLHandler.addTagValue("keepidentity", keepidentity));
		retval.append("      ").append(
				XMLHandler.addTagValue("tablock", tablock));
		retval.append("      ").append(
				XMLHandler.addTagValue("startfile", startfile));
		retval.append("      ").append(
				XMLHandler.addTagValue("endfile", endfile));
		retval.append("      ").append(
				XMLHandler.addTagValue("orderby", orderby));
		retval.append("      ").append(
				XMLHandler.addTagValue("orderdirection", orderdirection));
		retval.append("      ").append(
				XMLHandler.addTagValue("maxerrors", maxerrors));
		retval.append("      ").append(
				XMLHandler.addTagValue("batchsize", batchsize));
		retval.append("      ").append(
				XMLHandler.addTagValue("rowsperbatch", rowsperbatch));
		retval.append("      ").append(
				XMLHandler.addTagValue("errorfilename", errorfilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("adddatetime", adddatetime));
		retval.append("      ").append(
				XMLHandler.addTagValue("addfiletoresult", addfiletoresult));
		retval.append("      ").append(
				XMLHandler.addTagValue("truncate", truncate));

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
			schemaname = XMLHandler.getTagValue(entrynode, "schemaname");
			tablename = XMLHandler.getTagValue(entrynode, "tablename");
			filename = XMLHandler.getTagValue(entrynode, "filename");
			datafiletype = XMLHandler.getTagValue(entrynode, "datafiletype");
			fieldterminator = XMLHandler.getTagValue(entrynode,
					"fieldterminator");

			lineterminated = XMLHandler
					.getTagValue(entrynode, "lineterminated");
			codepage = XMLHandler.getTagValue(entrynode, "codepage");
			specificcodepage = XMLHandler.getTagValue(entrynode,
					"specificcodepage");
			formatfilename = XMLHandler
					.getTagValue(entrynode, "formatfilename");

			firetriggers = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "firetriggers"));
			checkconstraints = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "checkconstraints"));
			keepnulls = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					"keepnulls"));
			keepidentity = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "keepidentity"));

			tablock = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					"tablock"));
			startfile = Const.toInt(XMLHandler.getTagValue(entrynode,
					"startfile"), 0);
			endfile = Const.toInt(XMLHandler.getTagValue(entrynode, "endfile"),
					0);

			orderby = XMLHandler.getTagValue(entrynode, "orderby");
			orderdirection = XMLHandler
					.getTagValue(entrynode, "orderdirection");

			errorfilename = XMLHandler.getTagValue(entrynode, "errorfilename");

			maxerrors = Const.toInt(XMLHandler.getTagValue(entrynode,
					"maxerrors"), 0);
			batchsize = Const.toInt(XMLHandler.getTagValue(entrynode,
					"batchsize"), 0);
			rowsperbatch = Const.toInt(XMLHandler.getTagValue(entrynode,
					"rowsperbatch"), 0);
			adddatetime = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "adddatetime"));
			String dbname = XMLHandler.getTagValue(entrynode, "connection");
			addfiletoresult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "addfiletoresult"));
			truncate = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					"truncate"));

			connection = DatabaseMeta.findDatabase(databases, dbname);

		} catch (KettleException e) {
			throw new KettleXMLException(
					"Unable to load job entry of type 'MSsql bulk load' from XML node",
					e);
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		
		    connection = DatabaseMeta.findDatabase((List<DatabaseMeta>) databases, JobEntryBase
				.parameterToLong(p.get(id + ".connection")));
		
		    schemaname = JobEntryBase.parameterToString(p.get(id + ".schemaname"));
			tablename = JobEntryBase.parameterToString(p.get(id + ".tablename"));
			filename = JobEntryBase.parameterToString(p.get(id + ".filename"));
			datafiletype = JobEntryBase.parameterToString(p.get(id+ ".datafiletype"));
			fieldterminator = JobEntryBase.parameterToString(p.get(id+ ".fieldterminator"));
			lineterminated = JobEntryBase.parameterToString(p.get(id+ ".lineterminated"));
			codepage = JobEntryBase.parameterToString(p.get(id + ".codepage"));
			specificcodepage = JobEntryBase.parameterToString(p.get(id+ ".specificcodepage"));
			formatfilename = JobEntryBase.parameterToString(p.get(id+ ".formatfilename"));
			firetriggers = JobEntryBase.parameterToBoolean(p.get(id+ ".firetriggers"));
			checkconstraints = JobEntryBase.parameterToBoolean(p.get(id+ ".checkconstraints"));
			keepnulls = JobEntryBase.parameterToBoolean(p.get(id + ".keepnulls"));
			keepidentity = JobEntryBase.parameterToBoolean(p.get(id+ ".keepidentity"));
			tablock = JobEntryBase.parameterToBoolean(p.get(id + ".tablock"));
		    startfile = JobEntryBase.parameterToInt(p.get(id + ".startfile"));
			endfile = JobEntryBase.parameterToInt(p.get(id + ".endfile"));
			orderby = JobEntryBase.parameterToString(p.get(id + ".orderby"));
			orderdirection = JobEntryBase.parameterToString(p.get(id+ ".orderdirection"));
			errorfilename = JobEntryBase.parameterToString(p.get(id+ ".errorfilename"));
			maxerrors = JobEntryBase.parameterToInt(p.get(id + ".maxerrors"));
			batchsize = JobEntryBase.parameterToInt(p.get(id + ".batchsize"));
			rowsperbatch = JobEntryBase.parameterToInt(p.get(id + ".rowsperbatch"));
			adddatetime = JobEntryBase.parameterToBoolean(p.get(id + ".adddatetime"));
			addfiletoresult = JobEntryBase.parameterToBoolean(p.get(id+ ".addfiletoresult"));
			truncate = JobEntryBase.parameterToBoolean(p.get(id + ".truncate"));
	}

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			super.loadRep(rep, id_jobentry, databases, slaveServers);

			schemaname = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_SCHEMANAME);
			tablename = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_TABLENAME);
			filename = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_FILENAME);
			datafiletype = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_DATAFILETYPE);
			fieldterminator = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_FIELDTERMINATOR);
			lineterminated = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_LINETERMINATED);
			codepage = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_CODEPAGE);
			specificcodepage = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_SPECIFICCODEPAGE);
			formatfilename = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_FORMATFILENAME);
			firetriggers = rep.getJobEntryAttributeBoolean(id_jobentry, STEP_ATTRIBUTE_FIRETRIGGERS);
			checkconstraints = rep.getJobEntryAttributeBoolean(id_jobentry, STEP_ATTRIBUTE_CHECKCONSTRAINTS);
			keepnulls = rep.getJobEntryAttributeBoolean(id_jobentry, STEP_ATTRIBUTE_KEEPNULLS);
			keepidentity = rep.getJobEntryAttributeBoolean(id_jobentry, STEP_ATTRIBUTE_KEEPIDENTITY);
			tablock = rep.getJobEntryAttributeBoolean(id_jobentry, STEP_ATTRIBUTE_TABLOCK);
			startfile = (int)rep.getJobEntryAttributeInteger(id_jobentry, STEP_ATTRIBUTE_STARTFILE);
			endfile = (int)rep.getJobEntryAttributeInteger(id_jobentry, STEP_ATTRIBUTE_ENDFILE);
			orderby = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_ORDERBY);
			orderdirection = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_ORDERDIRECTION);
			errorfilename = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_ERRORFILENAME);
			maxerrors = (int)rep.getJobEntryAttributeInteger(id_jobentry, STEP_ATTRIBUTE_MAXERRORS);
			batchsize = (int)rep.getJobEntryAttributeInteger(id_jobentry, STEP_ATTRIBUTE_BATCHSIZE);
			rowsperbatch = (int)rep.getJobEntryAttributeInteger(id_jobentry, STEP_ATTRIBUTE_ROWSPERBATCH);
			adddatetime = rep.getJobEntryAttributeBoolean(id_jobentry, STEP_ATTRIBUTE_ADDDATETIME);
			addfiletoresult = rep.getJobEntryAttributeBoolean(id_jobentry, STEP_ATTRIBUTE_ADDFILETORESULT);
			truncate = rep.getJobEntryAttributeBoolean(id_jobentry, STEP_ATTRIBUTE_TRUNCATE);

			long id_db = rep.getJobEntryAttributeInteger(id_jobentry,"id_database");
			
			if (id_db > 0) {
				connection = DatabaseMeta.findDatabase(databases, id_db);
			} else {
				// This is were we end up in normally, the previous lines are
				// for backward compatibility.
				connection = DatabaseMeta.findDatabase(databases, rep
						.getJobEntryAttributeString(id_jobentry, "connection"));
			}

		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					"Unable to load job entry of type 'MSsql bulk load' from the repository for id_jobentry="
							+ id_jobentry, dbe);
		}
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			super.saveRep(rep, id_job);
			
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_SCHEMANAME,schemaname);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_TABLENAME, tablename);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_FILENAME, filename);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_DATAFILETYPE,datafiletype);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_FIELDTERMINATOR,fieldterminator);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_LINETERMINATED,lineterminated);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_CODEPAGE, codepage);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_SPECIFICCODEPAGE,specificcodepage);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_FORMATFILENAME,formatfilename);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_FIRETRIGGERS,firetriggers);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_CHECKCONSTRAINTS,checkconstraints);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_KEEPNULLS, keepnulls);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_KEEPIDENTITY,keepidentity);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_TABLOCK, tablock);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_STARTFILE, startfile);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ENDFILE, endfile);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ORDERBY, orderby);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ORDERDIRECTION,orderdirection);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ERRORFILENAME,errorfilename);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_MAXERRORS, maxerrors);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_BATCHSIZE, batchsize);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ROWSPERBATCH,rowsperbatch);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ADDDATETIME,adddatetime);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ADDFILETORESULT,addfiletoresult);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_TRUNCATE, truncate);

			if (connection != null)
				rep.saveJobEntryAttribute(id_job, getID(), "connection",
						connection.getName());

		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					"Unable to load job entry of type 'MSsql Bulk Load' to the repository for id_job="
							+ id_job, dbe);
		}
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public void setSchemaname(String schemaname) {
		this.schemaname = schemaname;
	}

	public String getSchemaname() {
		return schemaname;
	}

	public String getTablename() {
		return tablename;
	}

	public void setMaxErrors(int maxerrors) {
		this.maxerrors = maxerrors;
	}

	public int getMaxErrors() {
		return maxerrors;
	}

	public int getBatchSize() {
		return batchsize;
	}

	public void setBatchSize(int batchsize) {
		this.batchsize = batchsize;
	}

	public int getRowsPerBatch() {
		return rowsperbatch;
	}

	public void setRowsPerBatch(int rowsperbatch) {
		this.rowsperbatch = rowsperbatch;
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
		String TakeFirstNbrLines = "";
		String LineTerminatedby = "";
		String FieldTerminatedby = "";
		boolean useFieldSeparator = false;
		String UseCodepage = "";
		String ErrorfileName = "";

		LogWriter log = LogWriter.getInstance();

		Result result = previousResult;
		result.setResult(false);

		String vfsFilename = environmentSubstitute(filename);
		FileObject fileObject = null;
		// Let's check the filename ...
		if (!Const.isEmpty(vfsFilename)) {
			try {
				// User has specified a file, We can continue ...
				//
				// This is running over VFS but we need a normal file.
				// As such, we're going to verify that it's a local file...
				// We're also going to convert VFS FileObject to File
				//
				fileObject = KettleVFS.getFileObject(vfsFilename);
				if (!(fileObject instanceof LocalFile)) {
					// MSSQL BUKL INSERT can only use local files, so that's
					// what we limit ourselves to.
					//
					throw new KettleException(Messages.getString(
							"JobMssqlBulkLoad.Error.OnlyLocalFileSupported",
							vfsFilename));
				}

				// Convert it to a regular platform specific file name
				//
				String realFilename = KettleVFS.getFilename(fileObject);

				// Here we go... back to the regular scheduled program...
				//
				File file = new File(realFilename);
				if (file.exists() && file.canRead()) {
					// User has specified an existing file, We can continue ...
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"JobMssqlBulkLoad.FileExists.Label",
								realFilename));

					if (connection != null) {
						// User has specified a connection, We can continue ...
						Database db = new Database(connection);

						if (db.getDatabaseMeta().getDatabaseType() != DatabaseMeta.TYPE_DATABASE_MSSQL) {
							log.logError(toString(), Messages.getString(
									"JobMssqlBulkLoad.Error.DbNotMSSQL",
									connection.getDatabaseName()));
							return result;
						}
						db.shareVariablesWith(this);
						try {
							db.connect();
							// Get schemaname
							String realSchemaname = environmentSubstitute(schemaname);
							// Get tablename
							String realTablename = environmentSubstitute(tablename);

							if (db.checkTableExists(realTablename)) {
								// The table existe, We can continue ...
								if (log.isDetailed())
									log
											.logDetailed(
													toString(),
													Messages
															.getString(
																	"JobMssqlBulkLoad.TableExists.Label",
																	realTablename));

								// Add schemaname (Most the time
								// Schemaname.Tablename)
								if (schemaname != null)
									realTablename = realSchemaname + "."
											+ realTablename;

								// FIELDTERMINATOR
								String Fieldterminator = getRealFieldTerminator();
								if (Const.isEmpty(Fieldterminator)
										&& (datafiletype.equals("char") || datafiletype
												.equals("widechar"))) {
									log
											.logError(
													toString(),
													Messages
															.getString("JobMssqlBulkLoad.Error.FieldTerminatorMissing"));
									return result;
								} else {
									if (datafiletype.equals("char")
											|| datafiletype.equals("widechar")) {
										useFieldSeparator = true;
										FieldTerminatedby = "FIELDTERMINATOR='"
												+ Fieldterminator + "'";
									}
								}
								// Check Specific Code page
								if (codepage.equals("Specific")) {
									if (specificcodepage.length() < 0) {
										log
												.logError(
														toString(),
														Messages
																.getString("JobMssqlBulkLoad.Error.SpecificCodePageMissing"));
										return result;

									} else
										UseCodepage = "CODEPAGE = '"
												+ specificcodepage + "'";
								} else {
									UseCodepage = "CODEPAGE = '" + codepage
											+ "'";
								}

								// Check Error file
								if (errorfilename != null) {
									File errorfile = new File(errorfilename);
									if (errorfile.exists() && !adddatetime) {
										// The error file is created when the
										// command is executed. An error occurs
										// if the file already exists.
										log
												.logError(
														toString(),
														Messages
																.getString("JobMssqlBulkLoad.Error.ErrorFileExists"));
										return result;
									}
									if (adddatetime) {
										// Add date time to filename...
										SimpleDateFormat daf = new SimpleDateFormat();
										Date now = new Date();
										daf.applyPattern("yyyMMdd_HHmmss");
										String d = daf.format(now);

										ErrorfileName = "ERRORFILE ='"
												+ errorfilename + "_" + d + "'";
									} else
										ErrorfileName = "ERRORFILE ='"
												+ errorfilename + "'";
								}

								// ROWTERMINATOR
								String Rowterminator = getRealLineterminated();
								if (!Const.isEmpty(Rowterminator))
									LineTerminatedby = "ROWTERMINATOR='"
											+ Rowterminator + "'";

								// Start file at
								if (startfile > 0)
									TakeFirstNbrLines = "FIRSTROW=" + startfile;

								// End file at
								if (endfile > 0)
									TakeFirstNbrLines = "LASTROW=" + endfile;

								// Truncate table?
								String SQLBULKLOAD = "";
								if (truncate)
									SQLBULKLOAD = "TRUNCATE " + realTablename
											+ ";";

								// Build BULK Command
								SQLBULKLOAD = SQLBULKLOAD + "BULK INSERT "
										+ realTablename + " FROM " + "'"
										+ realFilename.replace('\\', '/') + "'";
								SQLBULKLOAD = SQLBULKLOAD + " WITH (";
								if (useFieldSeparator)
									SQLBULKLOAD = SQLBULKLOAD
											+ FieldTerminatedby;
								else
									SQLBULKLOAD = SQLBULKLOAD
											+ "DATAFILETYPE ='" + datafiletype
											+ "'";

								if (LineTerminatedby.length() > 0)
									SQLBULKLOAD = SQLBULKLOAD + ","
											+ LineTerminatedby;
								if (TakeFirstNbrLines.length() > 0)
									SQLBULKLOAD = SQLBULKLOAD + ","
											+ TakeFirstNbrLines;
								if (UseCodepage.length() > 0)
									SQLBULKLOAD = SQLBULKLOAD + ","
											+ UseCodepage;
								if (formatfilename != null)
									SQLBULKLOAD = SQLBULKLOAD
											+ ", FORMATFILE='" + formatfilename
											+ "'";
								if (firetriggers)
									SQLBULKLOAD = SQLBULKLOAD
											+ ",FIRE_TRIGGERS";
								if (keepnulls)
									SQLBULKLOAD = SQLBULKLOAD + ",KEEPNULLS";
								if (keepidentity)
									SQLBULKLOAD = SQLBULKLOAD + ",KEEPIDENTITY";
								if (checkconstraints)
									SQLBULKLOAD = SQLBULKLOAD
											+ ",CHECK_CONSTRAINTS";
								if (tablock)
									SQLBULKLOAD = SQLBULKLOAD + ",TABLOCK";
								if (orderby != null)
									SQLBULKLOAD = SQLBULKLOAD + ",ORDER ( "
											+ orderby + " " + orderdirection
											+ ")";
								if (ErrorfileName.length() > 0)
									SQLBULKLOAD = SQLBULKLOAD + ", "
											+ ErrorfileName;
								if (maxerrors > 0)
									SQLBULKLOAD = SQLBULKLOAD + ", MAXERRORS="
											+ maxerrors;
								if (batchsize > 0)
									SQLBULKLOAD = SQLBULKLOAD + ", BATCHSIZE="
											+ batchsize;
								if (rowsperbatch > 0)
									SQLBULKLOAD = SQLBULKLOAD
											+ ", ROWS_PER_BATCH="
											+ rowsperbatch;
								// End of Bulk command
								SQLBULKLOAD = SQLBULKLOAD + ")";

								try {
									// Run the SQL
									db.execStatements(SQLBULKLOAD);

									// Everything is OK...we can disconnect now
									db.disconnect();

									if (isAddFileToResult()) {
										// Add filename to output files
										ResultFile resultFile = new ResultFile(
												ResultFile.FILE_TYPE_GENERAL,
												KettleVFS
														.getFileObject(realFilename),
												parentJob.getJobname(),
												toString());
										result.getResultFiles()
												.put(
														resultFile.getFile()
																.toString(),
														resultFile);
									}

									result.setResult(true);
								} catch (KettleDatabaseException je) {
									result.setNrErrors(1);
									log.logError(toString(),
											"An error occurred executing this job entry : "
													+ je.getMessage());
								} catch (IOException e) {
									log.logError(toString(),
											"An error occurred executing this job entry : "
													+ e.getMessage());
									result.setNrErrors(1);
								} finally {
									if (db != null) {
										db.disconnect();
										db = null;
									}
								}
							} else {
								// Of course, the table should have been created
								// already before the bulk load operation
								db.disconnect();
								result.setNrErrors(1);
								if (log.isDetailed())
									log
											.logDetailed(
													toString(),
													Messages
															.getString(
																	"JobMssqlBulkLoad.Error.TableNotExists",
																	realTablename));
							}
						} catch (KettleDatabaseException dbe) {
							db.disconnect();
							result.setNrErrors(1);
							log.logError(toString(),
									"An error occurred executing this entry: "
											+ dbe.getMessage());
						}
					} else {
						// No database connection is defined
						result.setNrErrors(1);
						log
								.logError(
										toString(),
										Messages
												.getString("JobMssqlBulkLoad.Nodatabase.Label"));
					}
				} else {
					// the file doesn't exist
					result.setNrErrors(1);
					log.logError(toString(), Messages.getString(
							"JobMssqlBulkLoad.Error.FileNotExists",
							realFilename));
				}
			} catch (Exception e) {
				// An unexpected error occurred
				result.setNrErrors(1);
				log
						.logError(
								toString(),
								Messages
										.getString("JobMssqlBulkLoad.UnexpectedError.Label"),
								e);
			} finally {
				try {
					if (fileObject != null)
						fileObject.close();
				} catch (Exception e) {
				}
			}
		} else {
			// No file was specified
			result.setNrErrors(1);
			log.logError(toString(), Messages
					.getString("JobMssqlBulkLoad.Nofilename.Label"));
		}
		return result;
	}

	public DatabaseMeta[] getUsedDatabaseConnections() {
		return new DatabaseMeta[] { connection, };
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFieldTerminator(String fieldterminator) {
		this.fieldterminator = fieldterminator;
	}

	public void setLineterminated(String lineterminated) {
		this.lineterminated = lineterminated;
	}

	public void setCodePage(String codepage) {
		this.codepage = codepage;
	}

	public String getCodePage() {
		return codepage;
	}

	public void setSpecificCodePage(String specificcodepage) {
		this.specificcodepage = specificcodepage;
	}

	public String getSpecificCodePage() {
		return specificcodepage;
	}

	public void setFormatFilename(String formatfilename) {
		this.formatfilename = formatfilename;
	}

	public String getFormatFilename() {
		return formatfilename;
	}

	public String getFieldTerminator() {
		return fieldterminator;
	}

	public String getLineterminated() {
		return lineterminated;
	}

	public String getDataFileType() {
		return datafiletype;
	}

	public void setDataFileType(String datafiletype) {
		this.datafiletype = datafiletype;
	}

	public String getRealLineterminated() {
		return environmentSubstitute(getLineterminated());
	}

	public String getRealFieldTerminator() {
		return environmentSubstitute(getFieldTerminator());
	}

	public void setStartFile(int startfile) {
		this.startfile = startfile;
	}

	public int getStartFile() {
		return startfile;
	}

	public void setEndFile(int endfile) {
		this.endfile = endfile;
	}

	public int getEndFile() {
		return endfile;
	}

	public void setOrderBy(String orderby) {
		this.orderby = orderby;
	}

	public String getOrderBy() {
		return orderby;
	}

	public String getOrderDirection() {
		return orderdirection;
	}

	public void setOrderDirection(String orderdirection) {
		this.orderdirection = orderdirection;
	}

	public void setErrorFilename(String errorfilename) {
		this.errorfilename = errorfilename;
	}

	public String getErrorFilename() {
		return errorfilename;
	}

	public String getRealOrderBy() {
		return environmentSubstitute(getOrderBy());
	}

	public void setAddFileToResult(boolean addfiletoresultin) {
		this.addfiletoresult = addfiletoresultin;
	}

	public boolean isAddFileToResult() {
		return addfiletoresult;
	}

	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

	public boolean isTruncate() {
		return truncate;
	}

	public void setAddDatetime(boolean adddatetime) {
		this.adddatetime = adddatetime;
	}

	public boolean isAddDatetime() {
		return adddatetime;
	}

	public void setFireTriggers(boolean firetriggers) {
		this.firetriggers = firetriggers;
	}

	public boolean isFireTriggers() {
		return firetriggers;
	}

	public void setCheckConstraints(boolean checkconstraints) {
		this.checkconstraints = checkconstraints;
	}

	public boolean isCheckConstraints() {
		return checkconstraints;
	}

	public void setKeepNulls(boolean keepnulls) {
		this.keepnulls = keepnulls;
	}

	public boolean isKeepNulls() {
		return keepnulls;
	}

	public void setKeepIdentity(boolean keepidentity) {
		this.keepidentity = keepidentity;
	}

	public boolean isKeepIdentity() {
		return keepidentity;
	}

	public void setTablock(boolean tablock) {
		this.tablock = tablock;
	}

	public boolean isTablock() {
		return tablock;
	}

	public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
		List<ResourceReference> references = super
				.getResourceDependencies(jobMeta);
		ResourceReference reference = null;
		if (connection != null) {
			reference = new ResourceReference(this);
			references.add(reference);
			reference.getEntries().add(
					new ResourceEntry(connection.getHostname(),
							ResourceType.SERVER));
			reference.getEntries().add(
					new ResourceEntry(connection.getDatabaseName(),
							ResourceType.DATABASENAME));
		}
		if (filename != null) {
			String realFilename = getRealFilename();
			if (reference == null) {
				reference = new ResourceReference(this);
				references.add(reference);
			}
			reference.getEntries().add(
					new ResourceEntry(realFilename, ResourceType.FILE));
		}
		return references;
	}

	@Override
	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {
		ValidatorContext ctx = new ValidatorContext();
		putVariableSpace(ctx, getVariables());
		putValidators(ctx, notBlankValidator(), fileExistsValidator());
		andValidator().validate(this, "filename", remarks, ctx);//$NON-NLS-1$

		andValidator().validate(this,
				"tablename", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
	}

}

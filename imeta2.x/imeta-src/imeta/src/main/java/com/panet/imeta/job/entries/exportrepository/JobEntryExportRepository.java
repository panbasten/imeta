/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 */

package com.panet.imeta.job.entries.exportrepository;

import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notNullValidator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.ResultFile;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.util.StringUtil;
import com.panet.imeta.core.vfs.KettleVFS;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entries.sftp.JobEntrySFTP;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.job.entry.validator.ValidatorContext;
import com.panet.imeta.repository.RepositoriesMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryMeta;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.shared.SharedObjectInterface;

/**
 * This defines a 'Export repository' job entry. Its main use would be export
 * repository objects to a XML file that can be used to control the flow in ETL
 * cycles.
 * 
 * @author Samatar
 * @since 04-06-2008
 * 
 */
public class JobEntryExportRepository extends JobEntryBase implements
		Cloneable, JobEntryInterface {
	
	public static final String ENTRY_ATTRIBUTE_REPOSITORYNAME = "repositoryname";
	public static final String ENTRY_ATTRIBUTE_USERNAME = "username";
	public static final String ENTRY_ATTRIBUTE_PASSWORD = "password";
	public static final String ENTRY_ATTRIBUTE_TARGETFILENAME = "targetfilename";
	public static final String ENTRY_ATTRIBUTE_IFFILEEXISTS = "iffileexists";
	public static final String ENTRY_ATTRIBUTE_EXPORT_TYPE = "export_type";
	public static final String ENTRY_ATTRIBUTE_DIRECTORYPATH = "directoryPath";
	public static final String ENTRY_ATTRIBUTE_ADD_DATE = "add_date";
	public static final String ENTRY_ATTRIBUTE_ADD_TIME = "add_time";
	public static final String ENTRY_ATTRIBUTE_SPECIFYFORMAT = "SpecifyFormat";
	public static final String ENTRY_ATTRIBUTE_DATE_TIME_FORMAT = "date_time_format";
	public static final String ENTRY_ATTRIBUTE_CREATEFOLDER = "createfolder";
	public static final String ENTRY_ATTRIBUTE_NEWFOLDER = "newfolder";
	public static final String ENTRY_ATTRIBUTE_ADD_RESULT_FILESNAME = "add_result_filesname";
	public static final String ENTRY_ATTRIBUTE_NR_ERRORS_LESS_THAN = "nr_errors_less_than";
	public static final String ENTRY_ATTRIBUTE_SUCCESS_CONDITION = "success_condition";
	
	private String repositoryname;
	private String username;
	private String password;
	private String targetfilename;
	private String iffileexists;
	private String export_type;
	private String directoryPath;

	public String If_FileExists_Skip = "if_file_exists_skip";
	public String If_FileExists_Fail = "if_file_exists_fail";
	public String If_FileExists_Overwrite = "if_file_exists_overwrite";
	public String If_FileExists_Uniquename = "if_file_exists_uniquename";

	public String Export_All = "export_all";
	public String Export_Jobs = "export_jobs";
	public String Export_Trans = "export_trans";
	public String Export_By_Folder = "export_by_folder";
	public String Export_One_Folder = "export_one_folder";

	private boolean add_date;
	private boolean add_time;
	private boolean SpecifyFormat;
	private String date_time_format;
	private boolean createfolder;
	private boolean newfolder;
	private boolean add_result_filesname;
	private String nr_errors_less_than;

	private String success_condition;
	public String SUCCESS_IF_ERRORS_LESS = "success_if_errors_less";
	public String SUCCESS_IF_NO_ERRORS = "success_if_no_errors";

	FileObject file = null;
	RepositoriesMeta repsinfo = null;
	Repository repo = null;
	RepositoryMeta repinfo = null;
	UserInfo userinfo = null;

	int NrErrors = 0;
	boolean successConditionBroken = false;
	int limitErr = 0;

	public JobEntryExportRepository(String n) {
		super(n, "");
		repositoryname = null;
		targetfilename = null;
		username = null;
		iffileexists = If_FileExists_Skip;
		export_type = Export_All;
		add_date = false;
		add_time = false;
		SpecifyFormat = false;
		date_time_format = null;
		createfolder = false;
		newfolder = false;
		add_result_filesname = false;
		nr_errors_less_than = "10";
		success_condition = SUCCESS_IF_NO_ERRORS;
		setID(-1L);
		setJobEntryType(JobEntryType.EXPORT_REPOSITORY);
	}

	public JobEntryExportRepository() {
		this("");
	}

	public JobEntryExportRepository(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntryExportRepository je = (JobEntryExportRepository) super.clone();
		return je;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(50);

		retval.append(super.getXML());
		retval.append("      ").append(
				XMLHandler.addTagValue("repositoryname", repositoryname));
		retval.append("      ").append(
				XMLHandler.addTagValue("username", username));
		retval.append("      ").append(
				XMLHandler.addTagValue("password", Encr
						.encryptPasswordIfNotUsingVariables(getPassword())));
		retval.append("      ").append(
				XMLHandler.addTagValue("targetfilename", targetfilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("iffileexists", iffileexists));
		retval.append("      ").append(
				XMLHandler.addTagValue("export_type", export_type));
		retval.append("      ").append(
				XMLHandler.addTagValue("directoryPath", directoryPath)); // don't
		// loose
		// this
		// info
		// (backup/recovery)
		retval.append("      ").append(
				XMLHandler.addTagValue("add_date", add_date));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_time", add_time));
		retval.append("      ").append(
				XMLHandler.addTagValue("SpecifyFormat", SpecifyFormat));
		retval.append("      ").append(
				XMLHandler.addTagValue("date_time_format", date_time_format));
		retval.append("      ").append(
				XMLHandler.addTagValue("createfolder", createfolder));
		retval.append("      ").append(
				XMLHandler.addTagValue("newfolder", newfolder));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_result_filesname",
						add_result_filesname));
		retval.append("      ").append(
				XMLHandler.addTagValue("nr_errors_less_than",
						nr_errors_less_than));
		retval.append("      ").append(
				XMLHandler.addTagValue("success_condition", success_condition));

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers, Repository rep)
			throws KettleXMLException {
		try {
			super.loadXML(entrynode, databases, slaveServers);
			repositoryname = XMLHandler
					.getTagValue(entrynode, "repositoryname");
			username = XMLHandler.getTagValue(entrynode, "username");
			password = Encr.decryptPasswordOptionallyEncrypted(XMLHandler
					.getTagValue(entrynode, "password"));
			targetfilename = XMLHandler
					.getTagValue(entrynode, "targetfilename");
			iffileexists = XMLHandler.getTagValue(entrynode, "iffileexists");
			export_type = XMLHandler.getTagValue(entrynode, "export_type");
			directoryPath = XMLHandler.getTagValue(entrynode, "directoryPath");
			add_date = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					"add_date"));
			add_time = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					"add_time"));
			SpecifyFormat = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "SpecifyFormat"));
			date_time_format = XMLHandler.getTagValue(entrynode,
					"date_time_format");
			createfolder = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "createfolder"));
			newfolder = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					"newfolder"));
			add_result_filesname = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "add_result_filesname"));
			nr_errors_less_than = XMLHandler.getTagValue(entrynode,
					"nr_errors_less_than");
			success_condition = XMLHandler.getTagValue(entrynode,
					"success_condition");

		} catch (KettleXMLException xe) {
			throw new KettleXMLException(Messages
					.getString("JobExportRepository.Meta.UnableLoadXML"), xe);
		}
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		this.repositoryname = JobEntryBase.parameterToString(p.get(id+ ".repositoryname"));
		this.username = JobEntryBase.parameterToString(p.get(id+ ".username"));
		this.password = Encr.decryptPasswordOptionallyEncrypted(JobEntryBase.parameterToString(p.get(id+ ".password")));
		this.targetfilename = JobEntryBase.parameterToString(p.get(id+ ".targetfilename"));
		this.iffileexists = JobEntryBase.parameterToString(p.get(id+ ".iffileexists"));
		this.export_type = JobEntryBase.parameterToString(p.get(id+ ".export_type"));
		this.directoryPath = JobEntryBase.parameterToString(p.get(id+ ".directoryPath"));
		this.add_date = JobEntryBase.parameterToBoolean(p.get(id + ".add_date"));
		this.add_time = JobEntryBase.parameterToBoolean(p.get(id + ".add_time"));
		this.SpecifyFormat = JobEntryBase.parameterToBoolean(p.get(id+ ".SpecifyFormat"));
		this.date_time_format = JobEntryBase.parameterToString(p.get(id+ ".date_time_format"));
		this.createfolder = JobEntryBase.parameterToBoolean(p.get(id+ ".createfolder"));
		this.newfolder = JobEntryBase.parameterToBoolean(p.get(id+ ".newfolder"));
		this.add_result_filesname = JobEntryBase.parameterToBoolean(p.get(id+ ".add_result_filesname"));
		this.nr_errors_less_than = JobEntryBase.parameterToString(p.get(id+ ".nr_errors_less_than"));
		this.success_condition = JobEntryBase.parameterToString(p.get(id+ ".success_condition"));
	}
	

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			
			repositoryname = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_REPOSITORYNAME);
			username = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_USERNAME);
			password = Encr.decryptPasswordOptionallyEncrypted(rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_PASSWORD));
			targetfilename = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_TARGETFILENAME);
			iffileexists = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_IFFILEEXISTS);
			export_type = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_EXPORT_TYPE);
			directoryPath = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_DIRECTORYPATH);
			add_date = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_ADD_DATE);
			add_time = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_ADD_TIME);
			SpecifyFormat = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_SPECIFYFORMAT);
			date_time_format = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_DATE_TIME_FORMAT);
			createfolder = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_CREATEFOLDER);
			newfolder = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_NEWFOLDER);
			add_result_filesname = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_ADD_RESULT_FILESNAME);
			nr_errors_less_than = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_NR_ERRORS_LESS_THAN);
			success_condition = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_SUCCESS_CONDITION);

		} catch (KettleException dbe) {
			throw new KettleException(
					Messages.getString(
							"JobExportRepository.Meta.UnableLoadRep", ""
									+ id_jobentry), dbe);
		}
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			super.saveRep(rep, id_job);

			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_REPOSITORYNAME, repositoryname);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_USERNAME, username);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_PASSWORD, Encr.encryptPasswordIfNotUsingVariables(password));
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_TARGETFILENAME, targetfilename);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_IFFILEEXISTS, iffileexists);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_EXPORT_TYPE, export_type);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_DIRECTORYPATH, directoryPath);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ADD_DATE, add_date);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ADD_TIME, add_time);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_SPECIFYFORMAT, SpecifyFormat);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_DATE_TIME_FORMAT, date_time_format);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_CREATEFOLDER, createfolder);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_NEWFOLDER, newfolder);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ADD_RESULT_FILESNAME, add_result_filesname);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_NR_ERRORS_LESS_THAN, nr_errors_less_than);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_SUCCESS_CONDITION, success_condition);

		} catch (KettleDatabaseException dbe) {
			throw new KettleException(Messages.getString(
					"JobExportRepository.Meta.UnableSaveRep", "" + id_job), dbe);
		}
	}

	public void setSuccessCondition(String success_condition) {
		this.success_condition = success_condition;
	}

	public String getSuccessCondition() {
		return success_condition;
	}

	public void setRepositoryname(String repositoryname) {
		this.repositoryname = repositoryname;
	}

	public String getRepositoryname() {
		return repositoryname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setExportType(String export_type) {
		this.export_type = export_type;
	}

	public String getExportType() {
		return export_type;
	}

	public void setIfFileExists(String iffileexists) {
		this.iffileexists = iffileexists;
	}

	public String getIfFileExists() {
		return iffileexists;
	}

	public void setTargetfilename(String targetfilename) {
		this.targetfilename = targetfilename;
	}

	public String getTargetfilename() {
		return targetfilename;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDirectory() {
		return directoryPath;
	}

	public String getDateTimeFormat() {
		return date_time_format;
	}

	public void setDateTimeFormat(String date_time_format) {
		this.date_time_format = date_time_format;
	}

	public boolean isSpecifyFormat() {
		return SpecifyFormat;
	}

	public void setSpecifyFormat(boolean SpecifyFormat) {
		this.SpecifyFormat = SpecifyFormat;
	}

	public void setAddTime(boolean addtime) {
		this.add_time = addtime;
	}

	public boolean isAddTime() {
		return add_time;
	}

	public boolean isCreateFolder() {
		return createfolder;
	}

	public void setCreateFolder(boolean createfolder) {
		this.createfolder = createfolder;
	}

	public void setNewFolder(boolean newfolder) {
		this.newfolder = newfolder;
	}

	public boolean isNewFolder() {
		return newfolder;
	}

	public void setDirectory(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public void setAddDate(boolean adddate) {
		this.add_date = adddate;
	}

	public boolean isAddDate() {
		return add_date;
	}

	public void setAddresultfilesname(boolean add_result_filesnamein) {
		this.add_result_filesname = add_result_filesnamein;
	}

	public boolean isAddresultfilesname() {
		return add_result_filesname;
	}

	public void setNrLimit(String nr_errors_less_than) {
		this.nr_errors_less_than = nr_errors_less_than;
	}

	public String getNrLimit() {
		return nr_errors_less_than;
	}

	public String buildFilename(String filename) {
		String retval = "";
		if (Const.isEmpty(filename))
			return null;

		// Replace possible environment variables...
		String realfilename = filename;
		int lenstring = realfilename.length();
		int lastindexOfDot = realfilename.lastIndexOf('.');
		if (lastindexOfDot == -1)
			lastindexOfDot = lenstring;

		retval = realfilename.substring(0, lastindexOfDot);

		SimpleDateFormat daf = new SimpleDateFormat();
		Date now = new Date();

		if (SpecifyFormat && !Const.isEmpty(date_time_format)) {
			daf.applyPattern(date_time_format);
			String dt = daf.format(now);
			retval += dt;
		} else {
			if (add_date) {
				daf.applyPattern("yyyyMMdd");
				String d = daf.format(now);
				retval += "_" + d;
			}
			if (add_time) {
				daf.applyPattern("HHmmssSSS");
				String t = daf.format(now);
				retval += "_" + t;
			}
		}
		retval += realfilename.substring(lastindexOfDot, lenstring);
		return retval;

	}

	public String buildUniqueFilename(String filename) {
		String retval = "";
		if (Const.isEmpty(filename))
			return null;

		// Replace possible environment variables...
		String realfilename = filename;
		int lenstring = realfilename.length();
		int lastindexOfDot = realfilename.lastIndexOf('.');
		if (lastindexOfDot == -1)
			lastindexOfDot = lenstring;
		retval = realfilename.substring(0, lastindexOfDot);
		retval += StringUtil.getFormattedDateTimeNow();
		retval += realfilename.substring(lastindexOfDot, lenstring);

		return retval;

	}

	public Result execute(Result previousResult, int nr, Repository rep,
			Job parentJob) {
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
		result.setNrErrors(1);
		result.setResult(false);

		String realrepName = environmentSubstitute(repositoryname);
		String realoutfilename = environmentSubstitute(targetfilename);
		String realusername = environmentSubstitute(username);
		String realpassword = environmentSubstitute(password);
		String realfoldername = environmentSubstitute(directoryPath);

		NrErrors = 0;
		successConditionBroken = false;
		limitErr = Const.toInt(environmentSubstitute(getNrLimit()), 10);

		try {
			file = KettleVFS.getFileObject(realoutfilename);
			if (file.exists()) {
				if (export_type.equals(Export_All)
						|| export_type.equals(Export_Jobs)
						|| export_type.equals(Export_Trans)
						|| export_type.equals(Export_One_Folder)) {
					if (iffileexists.equals(If_FileExists_Fail)) {
						log.logError(toString(), Messages.getString(
								"JobExportRepository.Log.Failing",
								realoutfilename));
						return result;
					} else if (iffileexists.equals(If_FileExists_Skip)) {
						if (log.isDetailed())
							log.logDetailed(toString(), Messages.getString(
									"JobExportRepository.Log.Exit",
									realoutfilename));
						result.setResult(true);
						result.setNrErrors(0);
						return result;
					} else if (iffileexists.equals(If_FileExists_Uniquename)) {
						String parentFolder = KettleVFS.getFilename(file
								.getParent());
						String shortFilename = file.getName().getBaseName();
						shortFilename = buildUniqueFilename(shortFilename);
						file = KettleVFS.getFileObject(parentFolder
								+ Const.FILE_SEPARATOR + shortFilename);
						if (log.isDetailed())
							log.logDetailed(toString(), Messages.getString(
									"JobExportRepository.Log.NewFilename", file
											.toString()));
					}
				} else if (export_type.equals(Export_By_Folder)) {
					if (file.getType() != FileType.FOLDER) {
						log.logError(toString(), Messages.getString(
								"JobExportRepository.Log.NotFolder", ""
										+ file.getName()));
						return result;
					}
				}
			} else {
				if (export_type.equals(Export_By_Folder)) {
					// create folder?
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"JobExportRepository.Log.FolderNotExists", ""
										+ file.getName()));
					if (!createfolder) {
						return result;
					}
					file.createFolder();
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"JobExportRepository.Log.FolderCreated", file
										.toString()));
				} else if (export_type.equals(Export_All)
						|| export_type.equals(Export_Jobs)
						|| export_type.equals(Export_Trans)
						|| export_type.equals(Export_One_Folder)) {
					// create parent folder?
					if (!file.getParent().exists()) {
						if (log.isDetailed())
							log.logDetailed(toString(), Messages.getString(
									"JobExportRepository.Log.FolderNotExists",
									"" + file.getParent().toString()));
						if (createfolder) {
							file.getParent().createFolder();
							if (log.isDetailed())
								log
										.logDetailed(
												toString(),
												Messages
														.getString(
																"JobExportRepository.Log.FolderCreated",
																file
																		.getParent()
																		.toString()));
						} else {
							return result;
						}
					}
				}
			}

			realoutfilename = KettleVFS.getFilename(this.file);

			// connect to repository
			connectRep(log, realrepName, realusername, realpassword);

			if (export_type.equals(Export_All)) {
				if (log.isDetailed())
					log.logDetailed(toString(), Messages.getString(
							"JobExportRepository.Log.StartingExportAllRep",
							realoutfilename));
				this.repo.exportAllObjects(null, realoutfilename, null, "all");
				if (log.isDetailed())
					log.logDetailed(toString(), Messages.getString(
							"JobExportRepository.Log.EndExportAllRep",
							realoutfilename));

				if (add_result_filesname)
					addFileToResultFilenames(realoutfilename, log, result,
							parentJob);
			} else if (export_type.equals(Export_Jobs)) {
				if (log.isDetailed())
					log.logDetailed(toString(), Messages.getString(
							"JobExportRepository.Log.StartingExportJobsRep",
							realoutfilename));
				this.repo.exportAllObjects(null, realoutfilename, null, "jobs");
				if (log.isDetailed())
					log.logDetailed(toString(), Messages.getString(
							"JobExportRepository.Log.EndExportJobsRep",
							realoutfilename));

				if (add_result_filesname)
					addFileToResultFilenames(realoutfilename, log, result,
							parentJob);
			} else if (export_type.equals(Export_Trans)) {
				if (log.isDetailed())
					log.logDetailed(toString(), Messages.getString(
							"JobExportRepository.Log.StartingExportTransRep",
							realoutfilename));
				this.repo
						.exportAllObjects(null, realoutfilename, null, "trans");
				if (log.isDetailed())
					log.logDetailed(toString(), Messages.getString(
							"JobExportRepository.Log.EndExportTransRep",
							realoutfilename));

				if (add_result_filesname)
					addFileToResultFilenames(realoutfilename, log, result,
							parentJob);
			} else if (export_type.equals(Export_One_Folder)) {
				RepositoryDirectory directory = new RepositoryDirectory();
				directory = repo.getDirectoryTree().findDirectory(
						realfoldername);
				if (directory != null) {
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"JobExportRepository.Log.ExpAllFolderRep",
								directoryPath, realoutfilename));
					this.repo.exportAllObjects(null, realoutfilename,
							directory, "all");
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"JobExportRepository.Log.EndExpAllFolderRep",
								directoryPath, realoutfilename));

					if (add_result_filesname)
						addFileToResultFilenames(realoutfilename, log, result,
								parentJob);
				} else {
					log.logError(toString(), Messages.getString(
							"JobExportRepository.Error.CanNotFindFolderInRep",
							realfoldername, realrepName));
					return result;
				}
			} else if (export_type.equals(Export_By_Folder)) {
				// User must give a destination folder..

				RepositoryDirectory directory = new RepositoryDirectory();
				directory = this.repo.getDirectoryTree().findRoot();
				// Loop over all the directory id's
				long dirids[] = directory.getDirectoryIDs();
				if (log.isDetailed())
					log.logDetailed(toString(), Messages.getString(
							"JobExportRepository.Log.TotalFolders", ""
									+ dirids.length));
				for (int d = 0; d < dirids.length && !parentJob.isStopped(); d++) {
					// Success condition broken?
					if (successConditionBroken) {
						log
								.logError(
										toString(),
										Messages
												.getString(
														"JobExportRepository.Error.SuccessConditionbroken",
														"" + NrErrors));
						throw new Exception(
								Messages
										.getString(
												"JobExportRepository.Error.SuccessConditionbroken",
												"" + NrErrors));
					}

					RepositoryDirectory repdir = directory
							.findDirectory(dirids[d]);
					if (!processOneFolder(parentJob, result, log, repdir,
							realoutfilename, d, dirids.length)) {
						// updateErrors
						updateErrors();
					}
				}// end for
			}

		} catch (Exception e) {
			updateErrors();
			log.logError(toString(), Messages.getString(
					"JobExportRepository.UnExpectedError", e.toString()));
			log.logError(toString(), "Stack trace: " + Const.CR
					+ Const.getStackTracker(e));
		} finally {
			if (this.repo != null)
				this.repo.disconnect();
			if (this.repinfo != null)
				this.repinfo = null;
			if (this.userinfo != null)
				this.userinfo = null;
			if (this.repsinfo != null) {
				this.repsinfo.clear();
				this.repsinfo = null;
			}
			if (this.file != null)
				try {
					this.file.close();
				} catch (Exception e) {
				}
		}

		// Success Condition
		result.setNrErrors(NrErrors);
		if (getSuccessStatus())
			result.setResult(true);

		return result;
	}

	private boolean getSuccessStatus() {
		boolean retval = false;

		if ((this.NrErrors == 0 && getSuccessCondition().equals(
				SUCCESS_IF_NO_ERRORS))
				|| (this.NrErrors <= this.limitErr && getSuccessCondition()
						.equals(SUCCESS_IF_ERRORS_LESS))) {
			retval = true;
		}

		return retval;
	}

	private void updateErrors() {
		this.NrErrors++;
		if (checkIfSuccessConditionBroken()) {
			// Success condition was broken
			this.successConditionBroken = true;
		}
	}

	private boolean processOneFolder(Job parentJob, Result result,
			LogWriter log, RepositoryDirectory repdir, String realoutfilename,
			int folderno, int totalfolders) {
		boolean retval = false;
		try {
			if (!repdir.isRoot()) {
				if (repdir.toString().lastIndexOf("/") == 0) {
					String filename = repdir.toString().replace("/", "");
					String foldername = realoutfilename;
					if (newfolder) {
						foldername = realoutfilename + Const.FILE_SEPARATOR
								+ filename;
						this.file = KettleVFS.getFileObject(foldername);
						if (!this.file.exists()) {
							this.file.createFolder();
						}
					}

					filename = foldername + Const.FILE_SEPARATOR
							+ buildFilename(filename) + ".xml";
					this.file = KettleVFS.getFileObject(filename);

					if (this.file.exists()) {
						if (iffileexists.equals(If_FileExists_Skip)) {
							// Skip this folder
							return true;
						} else if (iffileexists
								.equals(If_FileExists_Uniquename)) {
							filename = realoutfilename + Const.FILE_SEPARATOR
									+ buildUniqueFilename(filename) + ".xml";
						} else if (iffileexists.equals(If_FileExists_Fail)) {
							// Fail
							return false;
						}
					}

					// System.out.print(filename + "\n");
					if (log.isDetailed()) {
						log.logDetailed(toString(), "---");
						log.logDetailed(toString(), Messages.getString(
								"JobExportRepository.Log.FolderProcessing", ""
										+ folderno, "" + totalfolders));
						log.logDetailed(toString(), Messages.getString(
								"JobExportRepository.Log.OutFilename", repdir
										.toString(), filename));
					}

					// System.out.println("Directory ID #"+d+" : "+dirids[d]+" :
					// "+repdir + "\n");
					this.repo.exportAllObjects(null, filename, repdir, "all");
					if (log.isDetailed())
						log.logDetailed(toString(), Messages.getString(
								"JobExportRepository.Log.OutFilenameEnd",
								repdir.toString(), filename));

					if (add_result_filesname)
						addFileToResultFilenames(filename, log, result,
								parentJob);

				}
			} // end if root
			retval = true;
		} catch (Exception e) {
			// Update errors
			updateErrors();
			log.logError(toString(), Messages.getString(
					"JobExportRepository.ErrorExportingFolder", repdir
							.toString(), e.toString()));
		}
		return retval;
	}

	private boolean checkIfSuccessConditionBroken() {
		boolean retval = false;
		if ((this.NrErrors > 0 && getSuccessCondition().equals(
				SUCCESS_IF_NO_ERRORS))
				|| (this.NrErrors >= this.limitErr && getSuccessCondition()
						.equals(SUCCESS_IF_ERRORS_LESS))) {
			retval = true;
		}
		return retval;
	}

	private void connectRep(LogWriter log, String realrepName,
			String realusername, String realpassword) throws Exception {
		this.repsinfo = new RepositoriesMeta(log);
		if (!this.repsinfo.readData()) {
			log.logError(toString(), Messages
					.getString("JobExportRepository.Error.NoRep"));
			throw new Exception(Messages
					.getString("JobExportRepository.Error.NoRep"));
		}
		this.repinfo = this.repsinfo.findRepository(realrepName);
		if (this.repinfo == null) {
			log.logError(toString(), Messages
					.getString("JobExportRepository.Error.NoRepSystem"));
			throw new Exception(Messages
					.getString("JobExportRepository.Error.NoRepSystem"));
		}

		this.repo = new Repository(log, this.repinfo);

		if (!this.repo.connect("Export job entry")) {
			log.logError(toString(), Messages
					.getString("JobExportRepository.Error.CanNotConnectRep"));
			throw new Exception(Messages
					.getString("JobExportRepository.Error.CanNotConnectRep"));
		}

		// Check username, password
		// Just for Job entry security
		// We don't need it at all to export
		if (log.isDebug())
			log
					.logDebug(
							toString(),
							Messages
									.getString("JobExportRepository.Log.CheckSuppliedUserPass"));
		this.userinfo = new UserInfo(this.repo, realusername, realpassword);
		if (log.isDebug())
			log
					.logDebug(toString(), Messages.getString(
							"JobExportRepository.Log.CheckingUser", userinfo
									.getName()));

		if (this.userinfo.getID() <= 0) {
			log
					.logError(
							toString(),
							Messages
									.getString("JobExportRepository.Error.CanNotVerifyUserPass"));
			throw new Exception(
					Messages
							.getString("JobExportRepository.Error.CanNotVerifyUserPass"));
		}
		if (log.isDebug())
			log
					.logDebug(
							toString(),
							Messages
									.getString("JobExportRepository.Log.SuppliedUserPassVerified"));

	}

	private void addFileToResultFilenames(String fileaddentry, LogWriter log,
			Result result, Job parentJob) {
		try {
			ResultFile resultFile = new ResultFile(
					ResultFile.FILE_TYPE_GENERAL, KettleVFS
							.getFileObject(fileaddentry), parentJob
							.getJobname(), toString());
			result.getResultFiles().put(resultFile.getFile().toString(),
					resultFile);
			if (log.isDebug())
				log.logDebug(toString(), Messages.getString(
						"JobExportRepository.Log.FileAddedToResultFilesName",
						fileaddentry));
		} catch (Exception e) {
			log
					.logError(
							Messages
									.getString("JobExportRepository.Error.AddingToFilenameResult"),
							fileaddentry + "" + e.getMessage());
		}
	}

	public boolean evaluates() {
		return true;
	}

	@Override
	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {
		andValidator().validate(this,
				"repositoryname", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$

		ValidatorContext ctx = new ValidatorContext();
		putVariableSpace(ctx, getVariables());
		putValidators(ctx, notBlankValidator(), fileExistsValidator());
		andValidator().validate(this, "targetfilename", remarks, ctx);//$NON-NLS-1$

		andValidator().validate(this,
				"username", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
		andValidator().validate(this,
				"password", remarks, putValidators(notNullValidator())); //$NON-NLS-1$
	}

	public static void main(String[] args) {
		List<CheckResultInterface> remarks = new ArrayList<CheckResultInterface>();
		new JobEntrySFTP().check(remarks, null);
		System.out.printf("Remarks: %s\n", remarks);
	}

}

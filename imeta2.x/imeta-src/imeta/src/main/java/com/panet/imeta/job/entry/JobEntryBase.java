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

package com.panet.imeta.job.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.CheckResultSourceInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.SQLStatement;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.variables.Variables;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.JobEntryCategory;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceDefinition;
import com.panet.imeta.resource.ResourceHolderInterface;
import com.panet.imeta.resource.ResourceNamingInterface;
import com.panet.imeta.resource.ResourceReference;

/**
 * Base class for the different types of job-entries...
 * 
 * @author Matt Created on 18-jun-04
 * 
 */
public class JobEntryBase implements Cloneable, VariableSpace,
		CheckResultSourceInterface, ResourceHolderInterface {
	private String name;

	private String description;

	private String pluginID = null;

	/**
	 * Id as defined in the xml or annotation.
	 */
	private String configId;

	private boolean changed;

	private JobEntryType type;

	private long id;

	protected VariableSpace variables = new Variables();

	public static final String category_order[] = {
			JobEntryCategory.GENERAL.getName(),
			JobEntryCategory.MAIL.getName(),
			JobEntryCategory.FILE_MANAGEMENT.getName(),
			JobEntryCategory.CONDITIONS.getName(),
			JobEntryCategory.SCRIPTING.getName(),
			JobEntryCategory.BULK_LOADING.getName(),
			JobEntryCategory.XML.getName(),
			JobEntryCategory.FILE_TRANSFER.getName(),
			JobEntryCategory.REPOSITORY.getName(),
			JobEntryCategory.EXPERIMENTAL.getName(), };

	public JobEntryBase() {
		name = null;
		description = null;
		type = JobEntryType.NONE;
	}

	public JobEntryBase(String name, String description) {
		setName(name);
		setDescription(description);
		setID(-1L);
		type = JobEntryType.NONE;
	}

	public JobEntryBase(JobEntryBase jeb) {
		setName(jeb.getName());
		setDescription(jeb.getDescription());
		setJobEntryType(jeb.getJobEntryType());
		setID(jeb.getID());
	}

	public void clear() {
		name = null;
		description = null;
		changed = false;
	}

	public void setID(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	public void setJobEntryType(JobEntryType type) {
		this.type = type;
	}

	public String getPluginID() {
		return this.pluginID;
	}

	/**
	 * Support for CheckResultSourceInterface
	 */
	public String getTypeId() {
		return getTypeCode();
	}

	public JobEntryType getJobEntryType() {
		return type;
	}

	public String getTypeCode() {
		if (this.pluginID != null)
			return this.pluginID;
		return type.toString();// JobEntryInterface.typeCode[type];
	}

	public static final String getTypeCode(JobEntryType type) {
		return type.toString();
	}

	public String getTypeDesc() {
		return type.getDescription();
	}

	public static final String getTypeDesc(JobEntryType type) {
		return type.getDescription();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPluginID(String pid) {
		this.pluginID = pid;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String Description) {
		this.description = Description;
	}

	public String getDescription() {
		return description;
	}

	public void setChanged() {
		setChanged(true);
	}

	public void setChanged(boolean ch) {
		changed = ch;
	}

	public boolean hasChanged() {
		return changed;
	}

	public boolean isStart() {
		return false;
	}

	public boolean isDummy() {
		return false;
	}

	public boolean isEvaluation() {
		return getJobEntryType() == JobEntryType.EVAL;
	}

	public boolean isJob() {
		return getJobEntryType() == JobEntryType.JOB;
	}

	public boolean isMail() {
		return getJobEntryType() == JobEntryType.MAIL;
	}

	public boolean isShell() {
		return getJobEntryType() == JobEntryType.MAIL;
	}

	public boolean isSpecial() {
		return getJobEntryType() == JobEntryType.SPECIAL;
	}

	public boolean isTransformation() {
		return getJobEntryType() == JobEntryType.TRANS;
	}

	public boolean isFTP() {
		return getJobEntryType() == JobEntryType.FTP;
	}

	public boolean isSFTP() {
		return getJobEntryType() == JobEntryType.SFTP;
	}

	public boolean isHTTP() {
		return getJobEntryType() == JobEntryType.HTTP;
	}

	// Add here for the new types?

	public String getXML() {
		StringBuffer retval = new StringBuffer();
		retval.append("      ").append(
				XMLHandler.addTagValue("name", getName()));
		retval.append("      ").append(
				XMLHandler.addTagValue("description", getDescription()));
		if (type != JobEntryType.NONE)
			retval.append("      ").append(
					XMLHandler.addTagValue("type", getTypeCode()));
		if (pluginID != null)
			retval.append("      ").append(
					XMLHandler.addTagValue("type", pluginID));
		retval.append("      ").append(
				XMLHandler.addTagValue("launch_in_parallel", getDescription()));

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers) throws KettleXMLException {
		try {
			setName(XMLHandler.getTagValue(entrynode, "name"));
			setDescription(XMLHandler.getTagValue(entrynode, "description"));
			String stype = XMLHandler.getTagValue(entrynode, "type");
			setJobEntryType(JobEntryCopy.getType(stype));
			setDescription(XMLHandler.getTagValue(entrynode, "description"));
		} catch (Exception e) {
			throw new KettleXMLException(
					"Unable to load base info for job entry", e);
		}
	}

	public void parseRepositoryObjects(Repository rep) throws KettleException {
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			setID(rep.insertJobEntry(id_job, this));
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					"Unable to save job entry base information to the repository for id_job="
							+ id_job, dbe);
		}
	}

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			RowMetaAndData r = rep.getJobEntry(id_jobentry);
			if (r != null) {
				setName(r.getString("NAME", null));

				setDescription(r.getString("DESCRIPTION", null));
				int id_jobentry_type = (int) r
						.getInteger("ID_JOBENTRY_TYPE", 0);
				RowMetaAndData jetrow = rep.getJobEntryType(id_jobentry_type);
				if (jetrow != null) {
					type = JobEntryCopy.getType(jetrow.getString("CODE", null));
				}
			}
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					"Unable to load base job entry information from the repository for id_jobentry="
							+ id_jobentry, dbe);
		}
	}

	public Object clone() {
		JobEntryBase je;
		try {
			je = (JobEntryBase) super.clone();
		} catch (CloneNotSupportedException cnse) {
			return null;
		}
		return je;
	}

	public String toString() {
		return name;
	}

	/**
	 * @return true if a reset of the number of errors is required before
	 *         execution.
	 */
	public boolean resetErrorsBeforeExecution() {
		return true;
	}

	/**
	 * check whether or not this job entry evaluates.
	 * 
	 * @return true if the job entry evaluates
	 */
	public boolean evaluates() {
		return false;
	}

	public boolean isUnconditional() {
		return true;
	}

	public List<SQLStatement> getSQLStatements(Repository repository)
			throws KettleException {
		return getSQLStatements(repository, null);
	}

	public List<SQLStatement> getSQLStatements(Repository repository,
			VariableSpace space) throws KettleException {
		return new ArrayList<SQLStatement>();
	}

	public String getFilename() {
		return null;
	}

	public String getRealFilename() {
		return null;
	}

	/**
	 * This method returns all the database connections that are used by the job
	 * entry.
	 * 
	 * @return an array of database connections meta-data. Return an empty array
	 *         if no connections are used.
	 */
	public DatabaseMeta[] getUsedDatabaseConnections() {
		return new DatabaseMeta[] {};
	}

	public void copyVariablesFrom(VariableSpace space) {
		variables.copyVariablesFrom(space);
	}

	public String environmentSubstitute(String aString) {
		return variables.environmentSubstitute(aString);
	}

	public String[] environmentSubstitute(String aString[]) {
		return variables.environmentSubstitute(aString);
	}

	public VariableSpace getParentVariableSpace() {
		return variables.getParentVariableSpace();
	}

	public void setParentVariableSpace(VariableSpace parent) {
		variables.setParentVariableSpace(parent);
	}

	public String getVariable(String variableName, String defaultValue) {
		return variables.getVariable(variableName, defaultValue);
	}

	public String getVariable(String variableName) {
		return variables.getVariable(variableName);
	}

	public boolean getBooleanValueOfVariable(String variableName,
			boolean defaultValue) {
		if (!Const.isEmpty(variableName)) {
			String value = environmentSubstitute(variableName);
			if (!Const.isEmpty(value)) {
				return ValueMeta.convertStringToBoolean(value);
			}
		}
		return defaultValue;
	}

	public void initializeVariablesFrom(VariableSpace parent) {
		variables.initializeVariablesFrom(parent);
	}

	public String[] listVariables() {
		return variables.listVariables();
	}

	public void setVariable(String variableName, String variableValue) {
		variables.setVariable(variableName, variableValue);
	}

	public void shareVariablesWith(VariableSpace space) {
		variables = space;
	}

	public void injectVariables(Map<String, String> prop) {
		variables.injectVariables(prop);
	}

	/**
	 * Support for overrides not having to put in a check method
	 * 
	 * @param remarks
	 *            CheckResults from checking the job entry
	 * @param jobMeta
	 *            JobMeta information letting threading back to the JobMeta
	 *            possible
	 */
	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {

	}

	/**
	 * Get a list of all the resource dependencies that the step is depending
	 * on.
	 * 
	 * @return a list of all the resource dependencies that the step is
	 *         depending on
	 */
	public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
		return new ArrayList<ResourceReference>(5); // default: return an empty
		// resource dependency list.
		// Lower the initial
		// capacity
	}

	public String exportResources(VariableSpace space,
			Map<String, ResourceDefinition> definitions,
			ResourceNamingInterface namingInterface) throws KettleException {
		return null;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	/**
	 * This returns the expected name for the dialog that edits a job entry. The
	 * expected name is in the com.panet.imeta.ui tree and has a class name that
	 * is the name of the job entry with 'Dialog' added to the end.
	 * 
	 * e.g. if the job entry is
	 * com.panet.imeta.job.entries.zipfile.JobEntryZipFile the dialog would be
	 * com.panet.imeta.ui.job.entries.zipfile.JobEntryZipFileDialog
	 * 
	 * If the dialog class for a job entry does not match this pattern it should
	 * override this method and return the appropriate class name
	 * 
	 * @return full class name of the dialog
	 */
	public String getDialogClassName() {
		String className = getClass().getCanonicalName();
		className = className.replaceFirst("\\.di\\.", ".di.ui.");
		className += "Dialog";
		return className;
	}

	public String getHolderType() {
		return "JOBENTRY"; //$NON-NLS-1$
	}

	protected VariableSpace getVariables() {
		return variables;
	}

	/**
	 * 参数的转换--string
	 * 
	 * @param parameter
	 * @return
	 */
	public static String parameterToString(String[] parameter) {
		if (parameter != null && parameter.length > 0) {
			return StringUtils.stripToEmpty(parameter[0]);
		}
		return "";
	}

	/**
	 * 参数的转换--int
	 * 
	 * @param parameter
	 * @return
	 */
	public static int parameterToInt(String[] parameter) {
		if (parameter != null && parameter.length > 0) {
			return Integer.valueOf(parameter[0]);
		}
		return -1;
	}

	/**
	 * 参数的转换--Boolean
	 * 
	 * @param parameter
	 * @return
	 */
	public static boolean parameterToBoolean(String[] parameter) {
		if (parameter != null && parameter.length > 0) {
			return "on".equals(parameter[0]);
		}
		return false;
	}
	
	public static int[] stringArrayToIntArray(String[] str) {
		if (str != null && str.length > 0) {
			int[] rtn = new int[str.length];
			for (int i = 0; i < str.length; i++) {
				try {
					rtn[i] = Integer.valueOf(str[i]);
				} catch (Exception e) {
					rtn[i] = -1;
				}
			}
			return rtn;
		}
		return null;
	}
	
	/**
	 * 参数的转换--long
	 * 
	 * @param parameter
	 * @return
	 */
	public static long parameterToLong(String[] parameter) {
		if (parameter != null && parameter.length > 0) {
			return parameterToLong(parameter[0]);
		}
		return -1L;
	}

	public static long parameterToLong(String parameter) {
		if (StringUtils.isNotEmpty(parameter)) {
			try {
				return Long.valueOf(parameter);
			} catch (Exception e) {
				return -1L;
			}
		}
		return -1L;
	}
}

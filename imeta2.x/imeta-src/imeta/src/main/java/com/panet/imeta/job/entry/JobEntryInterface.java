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

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.SQLStatement;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceDefinition;
import com.panet.imeta.resource.ResourceNamingInterface;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.shared.SharedObjectInterface;

/**
 * Interface for the different JobEntry classes.
 * 
 * @author Matt
 * @since 18-06-04
 * 
 */

public interface JobEntryInterface {

	public Result execute(Result prev_result, int nr, Repository rep,
			Job parentJob) throws KettleException;

	public void clear();

	public long getID();

	public void setID(long id);

	public String getName();

	public void setName(String name);

	public String getConfigId();

	public void setConfigId(String configId);

	public String getDescription();

	public void setDescription(String description);

	public void setChanged();

	public void setChanged(boolean ch);

	public boolean hasChanged();

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers, Repository rep)
			throws KettleXMLException;

	public String getXML();

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException;

	public void saveRep(Repository rep, long id_job) throws KettleException;

	public JobEntryType getJobEntryType();

	public void setJobEntryType(JobEntryType e);

	public String getTypeCode();

	public String getPluginID();

	public boolean isStart();

	public boolean isDummy();

	public Object clone();

	public boolean resetErrorsBeforeExecution();

	public boolean evaluates();

	public boolean isUnconditional();

	public boolean isEvaluation();

	public boolean isTransformation();

	public boolean isJob();

	public boolean isShell();

	public boolean isMail();

	public boolean isSpecial();

	public List<SQLStatement> getSQLStatements(Repository repository)
			throws KettleException;

	public List<SQLStatement> getSQLStatements(Repository repository,
			VariableSpace space) throws KettleException;

	/**
	 * Get the name of the class that implements the dialog for this job entry
	 * JobEntryBase provides a default
	 */
	public String getDialogClassName();

	public String getFilename();

	public String getRealFilename();

	/**
	 * This method returns all the database connections that are used by the job
	 * entry.
	 * 
	 * @return an array of database connections meta-data. Return an empty array
	 *         if no connections are used.
	 */
	public DatabaseMeta[] getUsedDatabaseConnections();

	public void setPluginID(String id);

	/**
	 * Allows JobEntry objects to check themselves for consistency
	 * 
	 * @param remarks
	 *            List of CheckResult objects indicating check status
	 * @param jobMeta
	 *            JobMeta
	 */
	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta);

	/**
	 * Get a list of all the resource dependencies that the step is depending
	 * on.
	 * 
	 * @return a list of all the resource dependencies that the step is
	 *         depending on
	 */
	public List<ResourceReference> getResourceDependencies(JobMeta jobMeta);

	/**
	 * Exports the object to a flat-file system, adding content with filename
	 * keys to a set of definitions. The supplied resource naming interface
	 * allows the object to name appropriately without worrying about those
	 * parts of the implementation specific details.
	 * 
	 * @param space
	 *            The variable space to resolve (environment) variables with.
	 * @param definitions
	 *            The map containing the filenames and content
	 * @param namingInterface
	 *            The resource naming interface allows the object to name
	 *            appropriately
	 * @return The filename for this object. (also contained in the definitions
	 *         map)
	 * @throws KettleException
	 *             in case something goes wrong during the export
	 */
	public String exportResources(VariableSpace space,
			Map<String, ResourceDefinition> definitions,
			ResourceNamingInterface namingInterface) throws KettleException;

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases);
}

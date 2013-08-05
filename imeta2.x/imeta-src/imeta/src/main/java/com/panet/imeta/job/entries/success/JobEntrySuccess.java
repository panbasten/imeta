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

package com.panet.imeta.job.entries.success;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;



/**
 * Job entry type to success a job.
 *
 * @author Samatar
 * @since 12-02-2007
 */
public class JobEntrySuccess extends JobEntryBase implements Cloneable, JobEntryInterface
{

	public JobEntrySuccess(String n, String scr)
	{
		super(n, "");
		setJobEntryType(JobEntryType.SUCCESS);
	}

	public JobEntrySuccess()
	{
		this("", "");
	}

	public JobEntrySuccess(JobEntryBase jeb)
	{
		super(jeb);
	}

    public Object clone()
    {
        JobEntrySuccess je = (JobEntrySuccess) super.clone();
        return je;
    }

	public String getXML()
	{
        StringBuffer retval = new StringBuffer();

		retval.append(super.getXML());

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep)
		throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
		}
		catch(Exception e)
		{
			throw new KettleXMLException(Messages.getString("JobEntrySuccess.Meta.UnableToLoadFromXML"), e);
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {};
	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
		throws KettleException
	{
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("JobEntrySuccess.Meta.UnableToLoadFromRep")+id_jobentry, dbe);


		}
	}

	// Save the attributes of this job entry
	//
	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);

		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("JobEntrySuccess.Meta.UnableToSaveToRep")+id_job, dbe);
		}
	}


	/**
	 * Execute this job entry and return the result.
	 * In this case it means, just set the result boolean in the Result class.
	 * @param previousResult The result of the previous execution
	 * @return The Result of the execution.
	 */
	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		Result result = previousResult;
		result.setNrErrors(0);
		result.setResult(true);
		
		return result;
	}

	public boolean evaluates()
	{
		return true;
	}

	public boolean isUnconditional()
	{
		return false;
	}

  @Override
  public void check(List<CheckResultInterface> remarks, JobMeta jobMeta)
  {

  }



}

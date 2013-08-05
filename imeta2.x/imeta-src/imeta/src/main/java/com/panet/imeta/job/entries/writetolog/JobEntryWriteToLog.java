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
 
package com.panet.imeta.job.entries.writetolog;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.addOkRemark;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
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
import com.panet.imeta.shared.SharedObjectInterface;



/**
 * Job entry type to output message to the job log.
 * 
 * @author Samatar
 * @since 08-08-2007
 */

public class JobEntryWriteToLog extends JobEntryBase implements Cloneable, JobEntryInterface
{
	private String logmessage;
	public  int     loglevel;
	private String logsubject;

	public JobEntryWriteToLog(String n)
	{
		super(n, "");
		logmessage=null;
		logsubject=null;
		setJobEntryType(JobEntryType.WRITE_TO_LOG);
	}
	
	
	public JobEntryWriteToLog()
	{
		this("");
	}
	
	public JobEntryWriteToLog(JobEntryBase jeb)
	{
		super(jeb);
	}
    
    public Object clone()
    {
        JobEntryWriteToLog je = (JobEntryWriteToLog) super.clone();
        return je;
    }
	
	public String getXML()
	{		
        StringBuffer retval = new StringBuffer(200);
	
		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue("logmessage",      logmessage));
		retval.append("      ").append(XMLHandler.addTagValue("loglevel",          LogWriter.getLogLevelDesc(loglevel)));
		retval.append("      ").append(XMLHandler.addTagValue("logsubject",      logsubject));

		return retval.toString();
	}
	
	public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep)
		throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			logmessage = XMLHandler.getTagValue(entrynode, "logmessage");
			loglevel = LogWriter.getLogLevel( XMLHandler.getTagValue(entrynode, "loglevel"));
			logsubject = XMLHandler.getTagValue(entrynode, "logsubject");
		}
		catch(Exception e)
		{
			throw new KettleXMLException(Messages.getString("WriteToLog.Error.UnableToLoadFromXML.Label"), e);
			
		}
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		logmessage = JobEntryBase.parameterToString(p.get(id + ".logmessage"));
    	loglevel = JobEntryBase.parameterToInt(p.get(id + ".loglevel"));
		logsubject = JobEntryBase.parameterToString(p.get(id + ".logsubject"));
		
	}

	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
		throws KettleException
	{
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);

			logmessage = rep.getJobEntryAttributeString(id_jobentry, "logmessage");
			loglevel = LogWriter.getLogLevel( rep.getJobEntryAttributeString(id_jobentry, "loglevel") );
			logsubject = rep.getJobEntryAttributeString(id_jobentry, "logsubject");
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("WriteToLog.Error.UnableToLoadFromRepository.Label")+id_jobentry, dbe);
			
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
			
			rep.saveJobEntryAttribute(id_job, getID(), "logmessage", logmessage);
			rep.saveJobEntryAttribute(id_job, getID(), "loglevel", LogWriter.getLogLevelDesc(loglevel));
			rep.saveJobEntryAttribute(id_job, getID(), "logsubject", logsubject);

		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("WriteToLog.Error.UnableToSaveToRepository.Label")+id_job, dbe);
		}
	}


	/**
	 * Output message to job log.
	 */
	public boolean evaluate(Result result)
	{
		LogWriter log = LogWriter.getInstance();
		
			
		try
		{
			
					
			loglevel=loglevel+1;
			
			if (loglevel==LogWriter.LOG_LEVEL_ERROR)
			{
				// Output message to log
				// Log level = ERREUR	
				log.logError(Const.CR + getRealLogSubject()+ Const.CR, getRealLogMessage()+ Const.CR);
			}
			else if (loglevel==LogWriter.LOG_LEVEL_MINIMAL)
			{
				// Output message to log
				// Log level = MINIMAL	
				log.logMinimal(Const.CR + getRealLogSubject()+ Const.CR, getRealLogMessage()+ Const.CR);
			}
			else if (loglevel==LogWriter.LOG_LEVEL_BASIC)
			{
				// Output message to log
				// Log level = BASIC	
				log.logBasic(Const.CR + getRealLogSubject()+ Const.CR, getRealLogMessage()+ Const.CR);
			}
			else if (loglevel==LogWriter.LOG_LEVEL_DETAILED)
			{
				// Output message to log
				// Log level = DETAILED	
				log.logDetailed(Const.CR + getRealLogSubject()+ Const.CR, getRealLogMessage()+ Const.CR);
			}
			else if (loglevel==LogWriter.LOG_LEVEL_DEBUG)
			{
				// Output message to log
				// Log level = DEBUG	
				log.logDebug(Const.CR + getRealLogSubject()+ Const.CR, getRealLogMessage()+ Const.CR);
				
			}
			else if (loglevel==LogWriter.LOG_LEVEL_ROWLEVEL)
			{
				// Output message to log
				// Log level = ROWLEVEL	
				log.logRowlevel(Const.CR + getRealLogSubject()+ Const.CR, getRealLogMessage()+ Const.CR);
			}



			return true;
			
			

			
					
		}
		catch(Exception e)
		{
			result.setNrErrors(1);
			log.logError(Messages.getString("WriteToLog.Error.Label"), Messages.getString("WriteToLog.Error.Description") + " : "+e.toString());
			return false;
		}
	
	
	}
	
	/**
	 * Execute this job entry and return the result.
	 * In this case it means, just set the result boolean in the Result class.
	 * @param prev_result The result of the previous execution
	 * @return The Result of the execution.
	 */
	public Result execute(Result prev_result, int nr, Repository rep, Job parentJob)
	{
		prev_result.setResult( evaluate(prev_result) );
		return prev_result;
	}
	
	public boolean resetErrorsBeforeExecution()
	{
		// we should be able to evaluate the errors in
		// the previous jobentry.
	    return false;
	}
	
	public boolean evaluates()
	{
		return true;
	}
	
	public boolean isUnconditional()
	{
		return false;
	}
    
   /* public JobEntryDialogInterface getDialog(Shell shell,JobEntryInterface jei,JobMeta jobMeta,String jobName,Repository rep) {
        return new JobEntryWriteToLogDialog(shell,this);
    }
*/
	public String getRealLogMessage()
	{
		return environmentSubstitute(Const.NVL(getLogMessage(), ""));
		
	}
	public String getRealLogSubject()
	{
		return 	environmentSubstitute(Const.NVL(getLogSubject(), ""));
	}
	
	public String getLogMessage()
	{
		if (logmessage == null)
		{
			logmessage="";
		}
		return logmessage;
	
		
	}
	public String getLogSubject()
	{
		if (logsubject == null)
		{
			logsubject="";
		}
		return logsubject;
	
		
	}
	public void setLogMessage(String s)
	{
		
		logmessage=s;
	
	}
	public void setLogSubject(String logsubjectin)
	{
		
		logsubject=logsubjectin;
	
	}
	
	 @Override
	  public void check(List<CheckResultInterface> remarks, JobMeta jobMeta)
	  {
	    addOkRemark(this, "LogMessage", remarks); //$NON-NLS-1$
	    addOkRemark(this, "LogSubject", remarks); //$NON-NLS-1$
	  }
	
}

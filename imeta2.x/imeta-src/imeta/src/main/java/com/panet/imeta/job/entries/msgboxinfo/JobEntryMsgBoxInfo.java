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

package com.panet.imeta.job.entries.msgboxinfo;
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
import com.panet.imeta.core.gui.GUIFactory;
import com.panet.imeta.core.gui.ThreadDialogs;
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
 * Job entry type to display a message box.
 *
 * @author Samatar
 * @since 12-02-2007
 */

public class JobEntryMsgBoxInfo extends JobEntryBase implements Cloneable, JobEntryInterface
{
	private String bodymessage;
	private String titremessage;

	public JobEntryMsgBoxInfo(String n, String scr)
	{
		super(n, "");
		bodymessage=null;
		titremessage=null;
		setJobEntryType(JobEntryType.MSGBOX_INFO);
	}

	public JobEntryMsgBoxInfo()
	{
		this("", "");
	}

	public JobEntryMsgBoxInfo(JobEntryBase jeb)
	{
		super(jeb);
	}

    public Object clone()
    {
        JobEntryMsgBoxInfo je = (JobEntryMsgBoxInfo) super.clone();
        return je;
    }

	public String getXML()
	{
        StringBuffer retval = new StringBuffer();

		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue("bodymessage",      bodymessage));
		retval.append("      ").append(XMLHandler.addTagValue("titremessage",     titremessage));


		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep)
		throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			bodymessage = XMLHandler.getTagValue(entrynode, "bodymessage");
			titremessage = XMLHandler.getTagValue(entrynode, "titremessage");
		}
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to load job entry of type 'Msgbox Info' from XML node", e);
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		bodymessage = JobEntryBase.parameterToString(p.get(id + ".bodymessage"));
		titremessage = JobEntryBase.parameterToString(p.get(id + ".titremessage"));
	
	}
	
	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
		throws KettleException
	{
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);

			bodymessage = rep.getJobEntryAttributeString(id_jobentry, "bodymessage");
			titremessage = rep.getJobEntryAttributeString(id_jobentry, "titremessage");
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'Msgbox Info' from the repository with id_jobentry="+id_jobentry, dbe);
		}
	}

	// Save the attributes of this job entry
	//
	public void saveRep(Repository rep, long id_job) throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);

			rep.saveJobEntryAttribute(id_job, getID(), "bodymessage", bodymessage);
			rep.saveJobEntryAttribute(id_job, getID(), "titremessage", titremessage);

		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to save job entry of type 'Msgbox Info' to the repository for id_job="+id_job, dbe);
		}
	}


	/**
	 * Display the Message Box.
	 */
	public boolean evaluate(Result result)
	{
		LogWriter log = LogWriter.getInstance();


		try
		{
			// default to ok

			// Try to display MSGBOX
			boolean response = true;

			ThreadDialogs dialogs = GUIFactory.getThreadDialogs();
        	if( dialogs != null ) {
        		response = dialogs.threadMessageBox(
        				getRealBodyMessage()+Const.CR,
        				getRealTitleMessage(), true, Const.INFO );
        	}

			return response;

		}
		catch(Exception e)
		{
			result.setNrErrors(1);
			log.logError(toString(), "Couldn't display message box: "+e.toString());
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

	public String getRealTitleMessage()
	{
		return environmentSubstitute(getTitleMessage());
	}

	public String getRealBodyMessage()
	{
		return environmentSubstitute(getBodyMessage());
	}

	public String getTitleMessage()
	{
		if (titremessage == null)
		{
			titremessage="";
		}
		return titremessage;
	}
	public String getBodyMessage()
	{
		if (bodymessage == null)
		{
			bodymessage="";
		}
		return bodymessage;


	}

	public void setBodyMessage(String s)
	{

		bodymessage=s;

	}

	public void setTitleMessage(String s)
	{

		titremessage=s;

	}

  @Override
  public void check(List<CheckResultInterface> remarks, JobMeta jobMeta)
  {
    addOkRemark(this, "bodyMessage", remarks); //$NON-NLS-1$
    addOkRemark(this, "titleMessage", remarks); //$NON-NLS-1$
  }

}

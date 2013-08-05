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

package com.panet.imeta.job.entries.fileexists;

import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
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
import com.panet.imeta.core.vfs.KettleVFS;
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
 * This defines an SQL job entry.
 *
 * @author Matt
 * @since 05-11-2003
 *
 */

public class JobEntryFileExists extends JobEntryBase implements Cloneable, JobEntryInterface
{
	private String filename;

	public JobEntryFileExists(String n)
	{
		super(n, ""); //$NON-NLS-1$
		filename=null;
		setID(-1L);
		setJobEntryType(JobEntryType.FILE_EXISTS);
	}

	public JobEntryFileExists()
	{
		this(""); //$NON-NLS-1$
	}

	public JobEntryFileExists(JobEntryBase jeb)
	{
		super(jeb);
	}

    public Object clone()
    {
        JobEntryFileExists je = (JobEntryFileExists) super.clone();
        return je;
    }

	public String getXML()
	{
        StringBuffer retval = new StringBuffer();

		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue("filename",   filename)); //$NON-NLS-1$ //$NON-NLS-2$

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			filename      = XMLHandler.getTagValue(entrynode, "filename"); //$NON-NLS-1$
		}
		catch(KettleXMLException xe)
		{
			throw new KettleXMLException(Messages.getString("JobEntryFileExists.ERROR_0001_Cannot_Load_Job_Entry_From_Xml_Node"), xe); //$NON-NLS-1$
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		filename = JobEntryBase.parameterToString(p.get(id + ".filename"));
	}
	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers) throws KettleException
	{
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			filename = rep.getJobEntryAttributeString(id_jobentry, "filename"); //$NON-NLS-1$
		}
		catch(KettleException dbe)
		{
			throw new KettleException(Messages.getString("JobEntryFileExists.ERROR_0002_Cannot_Load_Job_From_Repository", Long.toString(id_jobentry)), dbe); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);

			rep.saveJobEntryAttribute(id_job, getID(), "filename", filename); //$NON-NLS-1$
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("JobEntryFileExists.ERROR_0003_Cannot_Save_Job_Entry", Long.toString(id_job)), dbe); //$NON-NLS-1$
		}
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getFilename()
	{
		return filename;
	}

    public String getRealFilename()
    {
        return environmentSubstitute(getFilename());
    }

	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
		result.setResult( false );

		if (filename!=null)
		{
            String realFilename = getRealFilename();
            try
            {
                FileObject file = KettleVFS.getFileObject(realFilename);
                if (file.exists() && file.isReadable())
                {
                    log.logDetailed(toString(), Messages.getString("JobEntryFileExists.File_Exists", realFilename)); //$NON-NLS-1$
                    result.setResult( true );
                }
                else
                {
                    log.logDetailed(toString(), Messages.getString("JobEntryFileExists.File_Does_Not_Exist", realFilename)); //$NON-NLS-1$
                }
            }
            catch (IOException e)
            {
                result.setNrErrors(1);
                log.logError(toString(), Messages.getString("JobEntryFileExists.ERROR_0004_IO_Exception", e.toString())); //$NON-NLS-1$
            }
		}
		else
		{
			result.setNrErrors(1);
			log.logError(toString(), Messages.getString("JobEntryFileExists.ERROR_0005_No_Filename_Defined")); //$NON-NLS-1$
		}

		return result;
	}

	public boolean evaluates()
	{
		return true;
	}

  public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
    List<ResourceReference> references = super.getResourceDependencies(jobMeta);
    if (!Const.isEmpty(filename)) {
      String realFileName = jobMeta.environmentSubstitute(filename);
      ResourceReference reference = new ResourceReference(this);
      reference.getEntries().add( new ResourceEntry(realFileName, ResourceType.FILE));
      references.add(reference);
    }
    return references;
  }

  @Override
  public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {
    andValidator().validate(this, "filename", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
  }


}

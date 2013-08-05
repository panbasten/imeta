 /**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/
 
package com.panet.imeta.job.entries.createfolder;

import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileDoesNotExistValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notNullValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
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
import com.panet.imeta.job.entries.createfile.JobEntryCreateFile;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.job.entry.validator.ValidatorContext;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.step.BaseStepMeta;


/**
 * This defines a 'create folder' job entry. Its main use would be to create empty folder
 * that can be used to control the flow in ETL cycles.
 * 
 * @author Sven/Samatar
 * @since 18-10-2007
 *
 */
public class JobEntryCreateFolder extends JobEntryBase implements Cloneable, JobEntryInterface
{
	private String foldername;
	private boolean failOfFolderExists;
	
	public JobEntryCreateFolder(String n)
	{
		super(n, "");
		foldername=null;
		failOfFolderExists=true;
		setID(-1L);
		setJobEntryType(JobEntryType.CREATE_FOLDER);
	}

	public JobEntryCreateFolder()
	{
		this("");
	}

	public JobEntryCreateFolder(JobEntryBase jeb)
	{
		super(jeb);
	}

    public Object clone()
    {
        JobEntryCreateFolder je = (JobEntryCreateFolder) super.clone();
        return je;
    }
    
	public String getXML()
	{
        StringBuffer retval = new StringBuffer(50);
		
		retval.append(super.getXML());		
		retval.append("      ").append(XMLHandler.addTagValue("foldername",   foldername));
		retval.append("      ").append(XMLHandler.addTagValue("fail_of_folder_exists", failOfFolderExists));
		
		return retval.toString();
	}
	
	  public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	  {
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			foldername = XMLHandler.getTagValue(entrynode, "foldername");
			failOfFolderExists = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "fail_of_folder_exists"));
		}
		catch(KettleXMLException xe)
		{
			throw new KettleXMLException("Unable to load job entry of type 'create folder' from XML node", xe);
		}
	}

	  public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers) throws KettleException
	  {
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			foldername = rep.getJobEntryAttributeString(id_jobentry, "foldername");
			failOfFolderExists = rep.getJobEntryAttributeBoolean(id_jobentry, "fail_of_folder_exists");
		}
		catch(KettleException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'create Folder' from the repository for id_jobentry="+id_jobentry, dbe);
		}
	}
	
	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);
			
			rep.saveJobEntryAttribute(id_job, getID(), "foldername", foldername);
            rep.saveJobEntryAttribute(id_job, getID(), "fail_of_folder_exists", failOfFolderExists);
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to save job entry of type 'create Folder' to the repository for id_job="+id_job, dbe);
		}
	}

	public void setFoldername(String foldername)
	{
		this.foldername = foldername;
	}
	
	public String getFoldername()
	{
		return foldername;
	}
    
    public String getRealFoldername()
    {
        return environmentSubstitute(getFoldername());
    }
	
	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
		result.setResult( false );
		
	
		if (foldername!=null)
		{
            String realFoldername = getRealFoldername();
            FileObject FolderObject = null;
			try {
				FolderObject = KettleVFS.getFileObject(realFoldername);

				if ( FolderObject.exists() )
				{
					boolean isFolder=false;
					
					//Check if it's a folder
					if(FolderObject.getType() == FileType.FOLDER)  isFolder=true;
					
						if ( isFailOfFolderExists() )
						{
							// Folder exists and fail flag is on.
						    result.setResult( false );
						    if(isFolder)
						    	log.logError(toString(), "Folder ["+realFoldername+"] exists, failing.");
						    else
						    	log.logError(toString(), "File ["+realFoldername+"] exists, failing.");
						}
						else
						{
							// Folder already exists, no reason to try to create it
						    result.setResult( true );
						    if(log.isDetailed())
						    	log.logDetailed(toString(), "Folder ["+realFoldername+"] already exists, not recreating.");
						}
				
					
				}
				else
				{
					//  No Folder yet, create an empty Folder.
					FolderObject.createFolder();
					if(log.isDetailed())
						log.logDetailed(toString(), "Folder ["+realFoldername+"] created!");
					result.setResult( true );
				}
			} catch (IOException e) {
				log.logError(toString(), "Could not create Folder ["+realFoldername+"], exception: " + e.getMessage());
				result.setResult( false );
				result.setNrErrors(1);					
			}
            finally {
            	if ( FolderObject != null )
            	{
            		try  {
            		     FolderObject.close();
            		}
            		catch ( IOException ex ) {};
            	}
            }			
		}
		else
		{			
			log.logError(toString(), "No Foldername is defined.");
		}
		
		return result;
	}

	public boolean evaluates()
	{
		return true;
	}
    


	public boolean isFailOfFolderExists() {
		return failOfFolderExists;
	}

	public void setFailOfFolderExists(boolean failIfFolderExists) {
		this.failOfFolderExists = failIfFolderExists;
	}
	
	public static void main(String[] args)
	  {
	    List<CheckResultInterface> remarks = new ArrayList<CheckResultInterface>();
	    new JobEntryCreateFile().check(remarks, null);
	    System.out.printf("Remarks: %s\n", remarks);
	  }

	  public void check(List<CheckResultInterface> remarks, JobMeta jobMeta)
	  {
	    ValidatorContext ctx = new ValidatorContext();
	    putVariableSpace(ctx, getVariables());
	    putValidators(ctx, notNullValidator(), fileDoesNotExistValidator());
	    andValidator().validate(this, "filename", remarks, ctx); //$NON-NLS-1$
	  }


	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		this.foldername = BaseStepMeta.parameterToString(p.get(id
				+ ".foldername"));
		this.failOfFolderExists = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".failOfFolderExists"));
	}
}

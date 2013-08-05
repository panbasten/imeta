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

package com.panet.imeta.job.entries.xsdvalidator;


import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.vfs.FileObject;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

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
import com.panet.imeta.job.entry.validator.ValidatorContext;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;


/**
 * This defines a 'xsdvalidator' job entry.
 *
 * @author Samatar Hassan
 * @since 30-04-2007
 *
 */
public class JobEntryXSDValidator extends JobEntryBase implements Cloneable, JobEntryInterface
{
	public static final String ENTRY_ATTRIBUTE_XMLFILENAME = "xmlfilename";
	public static final String ENTRY_ATTRIBUTE_XSDFILENAME = "xsdfilename";
	
	private String xmlfilename;
	private String xsdfilename;



	public JobEntryXSDValidator(String n)
	{
		super(n, "");
     	xmlfilename=null;
     	xsdfilename=null;

		setID(-1L);
		setJobEntryType(JobEntryType.XSD_VALIDATOR);
	}

	public JobEntryXSDValidator()
	{
		this("");
	}

	public JobEntryXSDValidator(JobEntryBase jeb)
	{
		super(jeb);
	}

    public Object clone()
    {
        JobEntryXSDValidator je = (JobEntryXSDValidator)super.clone();
        return je;
    }

	public String getXML()
	{
        StringBuffer retval = new StringBuffer(50);

		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_XMLFILENAME, xmlfilename));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_XSDFILENAME, xsdfilename));

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep)
		throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			xmlfilename = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_XMLFILENAME);
			xsdfilename = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_XSDFILENAME);


		}
		catch(KettleXMLException xe)
		{
			throw new KettleXMLException("Unable to load job entry of type 'xsdvalidator' from XML node", xe);
		}
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		xmlfilename = JobEntryBase.parameterToString(p.get(id + ".xmlfilename"));
		xsdfilename = JobEntryBase.parameterToString(p.get(id + ".xsdfilename"));
	}

	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
		throws KettleException
	{
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			xmlfilename = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_XMLFILENAME);
			xsdfilename = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_XSDFILENAME);

		}
		catch(KettleException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'xsdvalidator' from the repository for id_jobentry="+id_jobentry, dbe);
		}
	}

	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);

			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_XMLFILENAME, xmlfilename);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_XSDFILENAME, xsdfilename);

		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to save job entry of type 'xsdvalidator' to the repository for id_job="+id_job, dbe);
		}
	}

    public String getRealxmlfilename()
    {
        return environmentSubstitute(getxmlFilename());
    }



    public String getRealxsdfilename()
    {
        return environmentSubstitute(getxsdFilename());
    }

	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
		result.setResult( false );

		String realxmlfilename = getRealxmlfilename();
		String realxsdfilename = getRealxsdfilename();


		FileObject xmlfile = null;
		FileObject xsdfile = null;

		try

		{

			if (xmlfilename!=null && xsdfilename!=null)
			{
				xmlfile = KettleVFS.getFileObject(realxmlfilename);
				xsdfile = KettleVFS.getFileObject(realxsdfilename);

				if ( xmlfile.exists() && xsdfile.exists() )
				{

					SchemaFactory factorytXSDValidator_1 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

					// Get XSD File
					File XSDFile = new File(KettleVFS.getFilename(xsdfile));
					Schema SchematXSD = factorytXSDValidator_1.newSchema(XSDFile);

					Validator XSDValidator = SchematXSD.newValidator();

					// Get XML File
					File xmlfiletXSDValidator_1 = new File(	KettleVFS.getFilename(xmlfile));

					Source sourcetXSDValidator_1 = new StreamSource(xmlfiletXSDValidator_1);


					XSDValidator.validate(sourcetXSDValidator_1);


					// Everything is OK
					result.setResult( true );

				}
				else
				{

					if(	!xmlfile.exists())
					{
						log.logError(toString(),  Messages.getString("JobEntryXSDValidator.FileDoesNotExist1.Label") +
							realxmlfilename +  Messages.getString("JobEntryXSDValidator.FileDoesNotExist2.Label"));
					}
					if(!xsdfile.exists())
					{
						log.logError(toString(),  Messages.getString("JobEntryXSDValidator.FileDoesNotExist1.Label") +
							realxsdfilename +  Messages.getString("JobEntryXSDValidator.FileDoesNotExist2.Label"));
					}
					result.setResult( false );
					result.setNrErrors(1);
				}

			}
			else
			{
				log.logError(toString(),  Messages.getString("JobEntryXSDValidator.AllFilesNotNull.Label"));
				result.setResult( false );
				result.setNrErrors(1);
			}



		}

		catch (SAXException ex) {
			log.logError(toString(),"Error :" + ex.getMessage());
		}
		catch ( Exception e )
		{

			log.logError(toString(), Messages.getString("JobEntryXSDValidator.ErrorXSDValidator.Label") +
				Messages.getString("JobEntryXSDValidator.ErrorXML1.Label") + realxmlfilename +
				Messages.getString("JobEntryXSDValidator.ErrorXML2.Label") +
				Messages.getString("JobEntryXSDValidator.ErrorXSD1.Label") + realxsdfilename +
				Messages.getString("JobEntryXSDValidator.ErrorXSD2.Label") + e.getMessage());
			result.setResult( false );
			result.setNrErrors(1);
		}
		finally
		{
			try
			{
			    if ( xmlfile != null )
			    	xmlfile.close();

			    if ( xsdfile != null )
			    	xsdfile.close();

		    }
			catch ( IOException e ) { }
		}


		return result;
	}

	public boolean evaluates()
	{
		return true;
	}

	public void setxmlFilename(String filename)
	{
		this.xmlfilename = filename;
	}

	public String getxmlFilename()
	{
		return xmlfilename;
	}


	public void setxsdFilename(String filename)
	{
		this.xsdfilename = filename;
	}

	public String getxsdFilename()
	{
		return xsdfilename;
	}

  public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
    List<ResourceReference> references = super.getResourceDependencies(jobMeta);
    if ( (!Const.isEmpty(xsdfilename)) && (!Const.isEmpty(xmlfilename)) ) {
      String realXmlFileName = jobMeta.environmentSubstitute(xmlfilename);
      String realXsdFileName = jobMeta.environmentSubstitute(xsdfilename);
      ResourceReference reference = new ResourceReference(this);
      reference.getEntries().add( new ResourceEntry(realXmlFileName, ResourceType.FILE));
      reference.getEntries().add( new ResourceEntry(realXsdFileName, ResourceType.FILE));
      references.add(reference);
    }
    return references;
  }

  @Override
  public void check(List<CheckResultInterface> remarks, JobMeta jobMeta)
  {
    ValidatorContext ctx = new ValidatorContext();
    putVariableSpace(ctx, getVariables());
    putValidators(ctx, notBlankValidator(), fileExistsValidator());
    andValidator().validate(this, ENTRY_ATTRIBUTE_XSDFILENAME, remarks, ctx);//$NON-NLS-1$
    andValidator().validate(this, ENTRY_ATTRIBUTE_XMLFILENAME, remarks, ctx);//$NON-NLS-1$
  }

}

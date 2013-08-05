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

package com.panet.imeta.job.entries.xslt;

import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.vfs.FileObject;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.ResultFile;
import com.panet.imeta.core.database.DatabaseMeta;
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
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.job.entry.validator.ValidatorContext;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;


/**
 * This defines a 'xslt' job entry.
 *
 * @author Samatar Hassan
 * @since 02-03-2007
 *
 */
public class JobEntryXSLT extends JobEntryBase implements Cloneable, JobEntryInterface
{
	public static final String ENTRY_ATTRIBUTE_XMLFILENAME = "xmlfilename";
	public static final String ENTRY_ATTRIBUTE_XSLFILENAME = "xslfilename";
	public static final String ENTRY_ATTRIBUTE_OUTPUTFILENAME = "outputfilename";
	public static final String ENTRY_ATTRIBUTE_IFFILEEXISTS = "iffileexists";
	public static final String ENTRY_ATTRIBUTE_ADDFILETORESULT = "addfiletoresult";
	public static final String ENTRY_ATTRIBUTE_XSLTFACTORY = "xsltfactory";
	
	public static String FACTORY_JAXP="JAXP";
	public static String FACTORY_SAXON="SAXON";
	
	
	private String xmlfilename;
	private String xslfilename;
	private String outputfilename;
	public int iffileexists;
	private boolean addfiletoresult;
	private String xsltfactory;


	public JobEntryXSLT(String n)
	{
		super(n, "");
     	xmlfilename=null;
     	xslfilename=null;
		outputfilename=null;
		iffileexists=1;
		addfiletoresult = false;
		xsltfactory=FACTORY_JAXP;
		setID(-1L);
		setJobEntryType(JobEntryType.XSLT);
	}

	public JobEntryXSLT()
	{
		this("");
	}

	public JobEntryXSLT(JobEntryBase jeb)
	{
		super(jeb);
	}

    public Object clone()
    {
        JobEntryXSLT je = (JobEntryXSLT)super.clone();
        return je;
    }

	public String getXML()
	{
        StringBuffer retval = new StringBuffer(50);

		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_XMLFILENAME, xmlfilename));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_XSLFILENAME, xslfilename));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_OUTPUTFILENAME, outputfilename));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_IFFILEEXISTS,  iffileexists));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_ADDFILETORESULT,  addfiletoresult));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_XSLTFACTORY, xsltfactory));
		
		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			xmlfilename = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_XMLFILENAME);
			xslfilename = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_XSLFILENAME);
			outputfilename = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_OUTPUTFILENAME);
			iffileexists = Const.toInt(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_IFFILEEXISTS), -1);
			addfiletoresult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_ADDFILETORESULT));
			xsltfactory = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_XSLTFACTORY);
			if(xsltfactory==null) xsltfactory=FACTORY_JAXP;

		}
		catch(KettleXMLException xe)
		{
			throw new KettleXMLException("Unable to load job entry of type 'xslt' from XML node", xe);
		}
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		xmlfilename = JobEntryBase.parameterToString(p.get(id + ".xmlfilename"));
		xslfilename = JobEntryBase.parameterToString(p.get(id + ".xslfilename"));
		outputfilename = JobEntryBase.parameterToString(p.get(id + ".outputfilename"));
		iffileexists = JobEntryBase.parameterToInt(p.get(id + ".iffileexists"));
		addfiletoresult = JobEntryBase.parameterToBoolean(p.get(id + ".addfiletoresult"));
		xsltfactory = JobEntryBase.parameterToString(p.get(id + ".xsltfactory"));
	}

	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
		throws KettleException
	{
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			xmlfilename = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_XMLFILENAME);
			xslfilename = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_XSLFILENAME);
			outputfilename = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_OUTPUTFILENAME);
			iffileexists=(int) rep.getJobEntryAttributeInteger(id_jobentry, ENTRY_ATTRIBUTE_IFFILEEXISTS);
			addfiletoresult=rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_ADDFILETORESULT);
			xsltfactory = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_XSLTFACTORY);
			if(xsltfactory==null) xsltfactory=FACTORY_JAXP;
		}
		catch(KettleException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'xslt' from the repository for id_jobentry="+id_jobentry, dbe);
		}
	}

	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);

			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_XMLFILENAME, xmlfilename);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_XSLFILENAME, xslfilename);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_OUTPUTFILENAME, outputfilename);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_IFFILEEXISTS, iffileexists);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ADDFILETORESULT, addfiletoresult);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_XSLTFACTORY, xsltfactory);
			
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to save job entry of type 'xslt' to the repository for id_job="+id_job, dbe);
		}
	}

	public String getXSLTFactory()
	{
		return xsltfactory;
	}
	
	public void setXSLTFactory(String xsltfactoryin)
	{
		xsltfactory=xsltfactoryin;
	}
	
    public String getRealxmlfilename()
    {
        return environmentSubstitute(getxmlFilename());
    }

	public String getRealoutputfilename()
	{
		return environmentSubstitute(getoutputFilename());
	}


    public String getRealxslfilename()
    {
        return environmentSubstitute(getxslFilename());
    }

	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
		result.setResult( false );

		String realxmlfilename = getRealxmlfilename();
		String realxslfilename = getRealxslfilename();
		String realoutputfilename = getRealoutputfilename();
		
		FileObject xmlfile = null;
		FileObject xslfile = null;
		FileObject outputfile = null;

		try

		{

			if (xmlfilename!=null && xslfilename!=null && outputfilename!=null)
			{
				xmlfile = KettleVFS.getFileObject(realxmlfilename);
				xslfile = KettleVFS.getFileObject(realxslfilename);
				outputfile = KettleVFS.getFileObject(realoutputfilename);

				if ( xmlfile.exists() && xslfile.exists() )
				{
					if (outputfile.exists() && iffileexists==2)
					{
						//Output file exists
						// User want to fail
						log.logError(toString(), Messages.getString("JobEntryXSLT.OuputFileExists1.Label")
										+ realoutputfilename + Messages.getString("JobEntryXSLT.OuputFileExists2.Label"));
						result.setResult( false );
						result.setNrErrors(1);
					}

					else if (outputfile.exists() && iffileexists==1)
					{
						// Do nothing
						if(log.isDebug()) log.logDebug(toString(), Messages.getString("JobEntryXSLT.OuputFileExists1.Label")
								+ realoutputfilename + Messages.getString("JobEntryXSLT.OuputFileExists2.Label"));
						result.setResult( true );
					}
					else
					{
						 if (outputfile.exists() && iffileexists==0)
							{
								// the output file exists and user want to create new one with unique name
								//Format Date

								// Try to clean filename (without wildcard)
								String wildcard = realoutputfilename.substring(realoutputfilename.length()-4,realoutputfilename.length());
								if(wildcard.substring(0,1).equals("."))
								{
									// Find wildcard
									realoutputfilename=realoutputfilename.substring(0,realoutputfilename.length()-4) +
										"_" + StringUtil.getFormattedDateTimeNow(true) + wildcard;
								}
								else
								{
									// did not find wildcard
									realoutputfilename=realoutputfilename + "_" + StringUtil.getFormattedDateTimeNow(true);
								}
							    if(log.isDebug()) log.logDebug(toString(),  Messages.getString("JobEntryXSLT.OuputFileExists1.Label") +
										realoutputfilename +  Messages.getString("JobEntryXSLT.OuputFileExists2.Label"));
								log.logDebug(toString(), Messages.getString("JobEntryXSLT.OuputFileNameChange1.Label") + realoutputfilename +
								Messages.getString("JobEntryXSLT.OuputFileNameChange2.Label"));
							}

						
						// Create transformer factory
						TransformerFactory factory = TransformerFactory.newInstance();	
						
						if (xsltfactory.equals(FACTORY_SAXON))
						{
							// Set the TransformerFactory to the SAXON implementation.
							factory = new net.sf.saxon.TransformerFactoryImpl(); 
						}
						
						if (log.isDetailed()) log.logDetailed(Messages.getString("JobEntryXSL.Log.TransformerFactoryInfos"),Messages.getString("JobEntryXSL.Log.TransformerFactory",factory.getClass().getName()));
				
								
						// Use the factory to create a template containing the xsl file
						Templates template = factory.newTemplates(new StreamSource(	KettleVFS.getInputStream(xslfile))); 

						// Use the template to create a transformer
						Transformer xformer = template.newTransformer();
						
						if (log.isDetailed()) log.logDetailed(Messages.getString("JobEntryXSL.Log.TransformerClassInfos"),Messages.getString("JobEntryXSL.Log.TransformerClass",xformer.getClass().getName()));
											
						
						// Prepare the input and output files
						Source source = new StreamSource(KettleVFS.getInputStream(xmlfile));
						StreamResult resultat = new StreamResult(KettleVFS.getOutputStream(outputfile, false));

						// Apply the xsl file to the source file and write the result to the output file
						xformer.transform(source, resultat);
						
						if (isAddFileToResult())
						{
							// Add output filename to output files
		                	ResultFile resultFile = new ResultFile(ResultFile.FILE_TYPE_GENERAL, KettleVFS.getFileObject(realoutputfilename), parentJob.getJobname(), toString());
		                    result.getResultFiles().put(resultFile.getFile().toString(), resultFile);
						}
						

						// Everything is OK
						result.setResult( true );
					}
				}
				else
				{

					if(	!xmlfile.exists())
					{
						log.logError(toString(),  Messages.getString("JobEntryXSLT.FileDoesNotExist1.Label") +
							realxmlfilename +  Messages.getString("JobEntryXSLT.FileDoesNotExist2.Label"));
					}
					if(!xslfile.exists())
					{
						log.logError(toString(),  Messages.getString("JobEntryXSLT.FileDoesNotExist1.Label") +
							realxslfilename +  Messages.getString("JobEntryXSLT.FileDoesNotExist2.Label"));
					}
					result.setResult( false );
					result.setNrErrors(1);
				}

			}
			else
			{
				log.logError(toString(),  Messages.getString("JobEntryXSLT.AllFilesNotNull.Label"));
				result.setResult( false );
				result.setNrErrors(1);
			}
		}
		catch ( Exception e )
		{
			log.logError(toString(), Messages.getString("JobEntryXSLT.ErrorXLST.Label") +
				Messages.getString("JobEntryXSLT.ErrorXLSTXML1.Label") + realxmlfilename +
				Messages.getString("JobEntryXSLT.ErrorXLSTXML2.Label") +
				Messages.getString("JobEntryXSLT.ErrorXLSTXSL1.Label") + realxslfilename +
				Messages.getString("JobEntryXSLT.ErrorXLSTXSL2.Label") + e.getMessage());
			result.setResult( false );
			result.setNrErrors(1);
		}
		finally
		{
			try
			{
			    if ( xmlfile != null )
			    	xmlfile.close();

			    if ( xslfile != null )
			    	xslfile.close();
				if ( outputfile != null )
					outputfile.close();
				
				// file object is not properly garbaged collected and thus the file cannot
				// be deleted anymore. This is a known problem in the JVM.

				System.gc();
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

	public String getoutputFilename()
	{
		return outputfilename;
	}


	public void setoutputFilename(String outputfilename)
	{
		this.outputfilename = outputfilename;
	}

	public void setxslFilename(String filename)
	{
		this.xslfilename = filename;
	}

	public String getxslFilename()
	{
		return xslfilename;
	}
	
	public void setAddFileToResult(boolean addfiletoresultin)
	{
		this.addfiletoresult = addfiletoresultin;
	}

	public boolean isAddFileToResult()
	{
		return addfiletoresult;
	}


  public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
    List<ResourceReference> references = super.getResourceDependencies(jobMeta);
    if ( (!Const.isEmpty(xslfilename)) && (!Const.isEmpty(xmlfilename)) ) {
      String realXmlFileName = jobMeta.environmentSubstitute(xmlfilename);
      String realXslFileName = jobMeta.environmentSubstitute(xslfilename);
      ResourceReference reference = new ResourceReference(this);
      reference.getEntries().add( new ResourceEntry(realXmlFileName, ResourceType.FILE));
      reference.getEntries().add( new ResourceEntry(realXslFileName, ResourceType.FILE));
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
    andValidator().validate(this, "xmlFilename", remarks, ctx);//$NON-NLS-1$
    andValidator().validate(this, "xslFilename", remarks, ctx);//$NON-NLS-1$

    andValidator().validate(this, "outputFilename", remarks, putValidators(notBlankValidator()));//$NON-NLS-1$
  }

}

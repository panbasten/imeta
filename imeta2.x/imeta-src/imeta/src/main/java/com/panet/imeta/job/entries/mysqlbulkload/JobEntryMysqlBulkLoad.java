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

package com.panet.imeta.job.entries.mysqlbulkload;

import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.provider.local.LocalFile;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.ResultFile;
import com.panet.imeta.core.database.Database;
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
 * This defines a MySQL job entry.
 *
 * @author Samatar Hassan
 * @since Jan-2007
 */
public class JobEntryMysqlBulkLoad extends JobEntryBase implements Cloneable, JobEntryInterface
{
	
	public static final String STEP_ATTRIBUTE_SCHEMANAME  = "schemaname";
	public static final String STEP_ATTRIBUTE_TABLENAME = "tablename";
	public static final String STEP_ATTRIBUTE_FILENAME  = "filename";
	public static final String STEP_ATTRIBUTE_SEPARATOR = "separator";
	public static final String STEP_ATTRIBUTE_ENCLOSED  = "enclosed";
	public static final String STEP_ATTRIBUTE_ESCAPED = "escaped";
	public static final String STEP_ATTRIBUTE_LINESTARTED = "linestarted";
	public static final String STEP_ATTRIBUTE_LINETERMINATED  = "lineterminated";
	public static final String STEP_ATTRIBUTE_REPLACEDATA = "replacedata";
	public static final String STEP_ATTRIBUTE_IGNORELINES = "ignorelines";
	public static final String STEP_ATTRIBUTE_LISTATTRIBUT  = "listattribut";
	public static final String STEP_ATTRIBUTE_LOCALINFILE = "localinfile";
	public static final String STEP_ATTRIBUTE_PRORITYVALUE  = "prorityvalue";
	public static final String STEP_ATTRIBUTE_ADDFILETORESULT = "addfiletoresult";
	
	private String schemaname;
	private String tablename;
	private String filename;
	private String separator;
	private String enclosed;
	private String escaped;
	private String linestarted;
	private String lineterminated;
	private String ignorelines;
	private boolean replacedata;
	private String listattribut;
	private boolean localinfile;
	public int prorityvalue;
	private boolean addfiletoresult;

	private DatabaseMeta connection;

	public JobEntryMysqlBulkLoad(String n)
	{
		super(n, "");
		tablename=null;
		schemaname=null;
		filename=null;
		separator=null;
		enclosed=null;
		escaped=null;
		lineterminated=null;
		linestarted=null;
		replacedata=true;
		ignorelines = "0";
		listattribut=null;
		localinfile=true;
		connection=null;
		addfiletoresult = false;
		setID(-1L);
		setJobEntryType(JobEntryType.MYSQL_BULK_LOAD);
	}

	public JobEntryMysqlBulkLoad()
	{
		this("");
	}

	public JobEntryMysqlBulkLoad(JobEntryBase jeb)
	{
		super(jeb);
	}

	public Object clone()
	{
		JobEntryMysqlBulkLoad je = (JobEntryMysqlBulkLoad) super.clone();
		return je;
	}

	public String getXML()
	{
		StringBuffer retval = new StringBuffer(200);

		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue("schemaname",      schemaname));
		retval.append("      ").append(XMLHandler.addTagValue("tablename",       tablename));
		retval.append("      ").append(XMLHandler.addTagValue("filename",        filename));
		retval.append("      ").append(XMLHandler.addTagValue("separator",       separator));
		retval.append("      ").append(XMLHandler.addTagValue("enclosed",        enclosed));
		retval.append("      ").append(XMLHandler.addTagValue("escaped",        escaped));
		retval.append("      ").append(XMLHandler.addTagValue("linestarted",     linestarted));
		retval.append("      ").append(XMLHandler.addTagValue("lineterminated",  lineterminated));

		retval.append("      ").append(XMLHandler.addTagValue("replacedata",     replacedata));
		retval.append("      ").append(XMLHandler.addTagValue("ignorelines",     ignorelines));
		retval.append("      ").append(XMLHandler.addTagValue("listattribut",    listattribut));

		retval.append("      ").append(XMLHandler.addTagValue("localinfile",     localinfile));

		retval.append("      ").append(XMLHandler.addTagValue("prorityvalue",    prorityvalue));
		
		retval.append("      ").append(XMLHandler.addTagValue("addfiletoresult",  addfiletoresult));

		retval.append("      ").append(XMLHandler.addTagValue("connection",      connection==null?null:connection.getName()));

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			schemaname  = XMLHandler.getTagValue(entrynode, "schemaname");
			tablename   = XMLHandler.getTagValue(entrynode, "tablename");
			filename    = XMLHandler.getTagValue(entrynode, "filename");
			separator   = XMLHandler.getTagValue(entrynode, "separator");
			enclosed    = XMLHandler.getTagValue(entrynode, "enclosed");
			escaped    = XMLHandler.getTagValue(entrynode, "escaped");

			linestarted     = XMLHandler.getTagValue(entrynode, "linestarted");
			lineterminated  = XMLHandler.getTagValue(entrynode, "lineterminated");
			replacedata     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "replacedata"));
			ignorelines     = XMLHandler.getTagValue(entrynode, "ignorelines");
			listattribut    = XMLHandler.getTagValue(entrynode, "listattribut");
			localinfile     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "localinfile"));
			prorityvalue    = Const.toInt(XMLHandler.getTagValue(entrynode, "prorityvalue"), -1);
			String dbname   = XMLHandler.getTagValue(entrynode, "connection");
			addfiletoresult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "addfiletoresult"));
			connection      = DatabaseMeta.findDatabase(databases, dbname);
		}
		catch(KettleException e)
		{
			throw new KettleXMLException("Unable to load job entry of type 'Mysql bulk load' from XML node", e);
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		
		// TODO Auto-generated method stub
		connection = DatabaseMeta.findDatabase((List<DatabaseMeta>) databases, JobEntryBase
				.parameterToLong(p.get(id + ".connection")));
		
		schemaname = JobEntryBase.parameterToString(p.get(id+ ".schemaname"));
		tablename = JobEntryBase.parameterToString(p.get(id+ ".tablename"));
		filename = JobEntryBase.parameterToString(p.get(id+ ".filename"));
		separator = JobEntryBase.parameterToString(p.get(id+ ".separator"));
		enclosed = JobEntryBase.parameterToString(p.get(id+ ".enclosed"));
		escaped = JobEntryBase.parameterToString(p.get(id+ ".escaped"));
		linestarted = JobEntryBase.parameterToString(p.get(id+ ".linestarted"));
		lineterminated = JobEntryBase.parameterToString(p.get(id+ ".lineterminated"));
		replacedata = JobEntryBase.parameterToBoolean(p.get(id+ ".replacedata"));
		ignorelines = JobEntryBase.parameterToString(p.get(id+ ".ignorelines"));
		listattribut = JobEntryBase.parameterToString(p.get(id+ ".listattribut"));
		localinfile = JobEntryBase.parameterToBoolean(p.get(id+ ".localinfile"));
		prorityvalue = JobEntryBase.parameterToInt(p.get(id+ ".prorityvalue"));
		addfiletoresult = JobEntryBase.parameterToBoolean(p.get(id + ".addfiletoresult"));
	}
	
	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
		throws KettleException
	{
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			
			schemaname= rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_SCHEMANAME );
			tablename = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_TABLENAME);
			filename= rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_FILENAME );
			separator = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_SEPARATOR);
			enclosed= rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_ENCLOSED );
			escaped = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_ESCAPED);
			linestarted = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_LINESTARTED);
			lineterminated= rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_LINETERMINATED );
			replacedata = rep.getJobEntryAttributeBoolean(id_jobentry,STEP_ATTRIBUTE_REPLACEDATA);
			ignorelines = rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_IGNORELINES);
			listattribut= rep.getJobEntryAttributeString(id_jobentry, STEP_ATTRIBUTE_LISTATTRIBUT );
			localinfile = rep.getJobEntryAttributeBoolean(id_jobentry,STEP_ATTRIBUTE_LOCALINFILE);
			prorityvalue=(int) rep.getJobEntryAttributeInteger(id_jobentry, STEP_ATTRIBUTE_PRORITYVALUE );
			addfiletoresult=rep.getJobEntryAttributeBoolean(id_jobentry,STEP_ATTRIBUTE_ADDFILETORESULT);

			long id_db = rep.getJobEntryAttributeInteger(id_jobentry, "id_database");
			if (id_db>0)
			{
				connection = DatabaseMeta.findDatabase(databases, id_db);
			}
			else
			{
				// This is were we end up in normally, the previous lines are for backward compatibility.
				connection = DatabaseMeta.findDatabase(databases, rep.getJobEntryAttributeString(id_jobentry, "connection"));
			}

		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'Mysql bulk load' from the repository for id_jobentry="+id_jobentry, dbe);
		}
	}

	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);
			
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_SCHEMANAME , schemaname);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_TABLENAME ,tablename);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_FILENAME , filename);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_SEPARATOR ,separator);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ENCLOSED , enclosed);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ESCAPED , escaped);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_LINESTARTED ,linestarted);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_LINETERMINATED , lineterminated);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_REPLACEDATA ,replacedata);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_IGNORELINES ,ignorelines);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_LISTATTRIBUT , listattribut);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_LOCALINFILE ,localinfile);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_PRORITYVALUE , prorityvalue);
			rep.saveJobEntryAttribute(id_job, getID(),STEP_ATTRIBUTE_ADDFILETORESULT , addfiletoresult);

			if (connection!=null) rep.saveJobEntryAttribute(id_job, getID(), "connection", connection.getName());
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'Mysql Bulk Load' to the repository for id_job="+id_job, dbe);
		}
	}

	public void setTablename(String tablename)
	{
		this.tablename = tablename;
	}

	public void setSchemaname(String schemaname)
	{
		this.schemaname = schemaname;
	}

	public String getSchemaname()
	{
		return schemaname;
	}

	public String getTablename()
	{
		return tablename;
	}

	public void setDatabase(DatabaseMeta database)
	{
		this.connection = database;
	}

	public DatabaseMeta getDatabase()
	{
		return connection;
	}

	public boolean evaluates()
	{
		return true;
	}

	public boolean isUnconditional()
	{
		return true;
	}

	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		String ReplaceIgnore;
		String IgnoreNbrLignes="";
		String ListOfColumn="";
		String LocalExec="";
		String PriorityText="";
		String LineTerminatedby="";
		String FieldTerminatedby="";

		LogWriter log = LogWriter.getInstance();

		Result result = previousResult;
		result.setResult(false);

		String vfsFilename = environmentSubstitute(filename);

		// Let's check the filename ...
		if (!Const.isEmpty(vfsFilename))
		{
			try
			{
				// User has specified a file, We can continue ...
				//
				
				// This is running over VFS but we need a normal file.
				// As such, we're going to verify that it's a local file...
				// We're also going to convert VFS FileObject to File
				//
				FileObject fileObject = KettleVFS.getFileObject(vfsFilename);
				if (!(fileObject instanceof LocalFile)) {
					// MySQL LOAD DATA can only use local files, so that's what we limit ourselves to.
					//
					throw new KettleException("Only local files are supported at this time, file ["+vfsFilename+"] is not a local file.");
				}
				
				// Convert it to a regular platform specific file name
				//
				String realFilename = KettleVFS.getFilename(fileObject);
				
				// Here we go... back to the regular scheduled program...
				//
				File file = new File(realFilename);
				if ((file.exists() && file.canRead()) || isLocalInfile()==false)
				{
					// User has specified an existing file, We can continue ...
					if(log.isDetailed())	
						log.logDetailed(toString(), "File ["+realFilename+"] exists.");
	
					if (connection!=null)
					{
						// User has specified a connection, We can continue ...
						Database db = new Database(connection);
						db.shareVariablesWith(this);
						try
						{
							db.connect();
							// Get schemaname
							String realSchemaname = environmentSubstitute(schemaname);
							// Get tablename
							String realTablename = environmentSubstitute(tablename);
	
							if (db.checkTableExists(realTablename))
							{
								// The table existe, We can continue ...
								if(log.isDetailed())	
									log.logDetailed(toString(), "Table ["+realTablename+"] exists.");
	
								// Add schemaname (Most the time Schemaname.Tablename)
								if (schemaname !=null)
								{
									realTablename= realSchemaname + "." + realTablename;
								}
	
								// Set the REPLACE or IGNORE
								if (isReplacedata())
								{
									ReplaceIgnore="REPLACE";
								}
								else
								{
									ReplaceIgnore="IGNORE";
								}
	
								// Set the IGNORE LINES
								if (Const.toInt(getRealIgnorelines(),0)>0)
								{
									IgnoreNbrLignes = "IGNORE " + getRealIgnorelines() + " LINES";
								}
	
								// Set list of Column
								if (getRealListattribut()!= null)
								{
									ListOfColumn="(" + MysqlString(getRealListattribut()) + ")";
	
								}
	
								// Local File execution
								if (isLocalInfile())
								{
									LocalExec = "LOCAL";
								}
	
								// Prority
								if (prorityvalue == 1)
								{
									//LOW
									PriorityText = "LOW_PRIORITY";
								}
								else if(prorityvalue == 2)
								{
									//CONCURRENT
									PriorityText = "CONCURRENT";
								}
	
								// Fields ....
								if (getRealSeparator() != null || getRealEnclosed() != null || getRealEscaped() != null)
								{
									FieldTerminatedby ="FIELDS ";
	
									if (getRealSeparator() != null )
									{
										FieldTerminatedby = FieldTerminatedby + "TERMINATED BY '" +  Const.replace(getRealSeparator(), "'", "''") +"'";
									}
									if (getRealEnclosed() != null )
									{
										FieldTerminatedby = FieldTerminatedby + " ENCLOSED BY '" +  Const.replace(getRealEnclosed(), "'", "''") +"'";
	
									}
									if (getRealEscaped() != null )
									{
	
											FieldTerminatedby = FieldTerminatedby + " ESCAPED BY '" +  Const.replace(getRealEscaped(), "'", "''") +"'";
	
									}
								}
	
								// LINES ...
								if (getRealLinestarted() != null ||getRealLineterminated() != null )
								{
									LineTerminatedby="LINES ";
	
									// Line starting By
									if (getRealLinestarted() != null)
									{
										LineTerminatedby =LineTerminatedby + "STARTING BY '" +  Const.replace(getRealLinestarted(), "'", "''") +"'";
									}
	
									// Line terminating By
									if (getRealLineterminated() != null)
									{
										LineTerminatedby =LineTerminatedby + " TERMINATED BY '" +  Const.replace(getRealLineterminated(), "'", "''") +"'";
									}
								}
	
								String SQLBULKLOAD="LOAD DATA " + PriorityText + " " + LocalExec + " INFILE '" + realFilename.replace('\\', '/') + 	"' " + ReplaceIgnore +
	                     						   " INTO TABLE " + realTablename + " " + FieldTerminatedby + " " + LineTerminatedby + " " + IgnoreNbrLignes + " " +  ListOfColumn  + ";";
	
								try
								{
									// Run the SQL
									db.execStatements(SQLBULKLOAD);
	
									// Everything is OK...we can deconnect now
									db.disconnect();
									
									if (isAddFileToResult())
									{
										// Add zip filename to output files
					                	ResultFile resultFile = new ResultFile(ResultFile.FILE_TYPE_GENERAL, KettleVFS.getFileObject(realFilename), parentJob.getJobname(), toString());
					                    result.getResultFiles().put(resultFile.getFile().toString(), resultFile);
									}
									
									result.setResult(true);
								}
								catch(KettleDatabaseException je)
								{
									db.disconnect();
									result.setNrErrors(1);
									log.logError(toString(), "An error occurred executing this job entry : "+je.getMessage());
								}
								catch (IOException e)
								{
					       			log.logError(toString(), "An error occurred executing this job entry : " + e.getMessage());
									result.setNrErrors(1);
								}
							}
							else
							{
								// Of course, the table should have been created already before the bulk load operation
								db.disconnect();
								result.setNrErrors(1);
								if(log.isDetailed())	
									log.logDetailed(toString(), "Table ["+realTablename+"] doesn't exist!");
							}
						}
						catch(KettleDatabaseException dbe)
						{
							db.disconnect();
							result.setNrErrors(1);
							log.logError(toString(), "An error occurred executing this entry: "+dbe.getMessage());
						}
					}
					else
					{
						// No database connection is defined
						result.setNrErrors(1);
						log.logError(toString(),  Messages.getString("JobMysqlBulkLoad.Nodatabase.Label"));
					}
				}
				else
				{
					// the file doesn't exist
					result.setNrErrors(1);
					log.logError(toString(), "File ["+realFilename+"] doesn't exist!");
				}
			}
			catch(Exception e)
			{
				// An unexpected error occurred
				result.setNrErrors(1);
				log.logError(toString(), Messages.getString("JobMysqlBulkLoad.UnexpectedError.Label"), e);
			}
		}
		else
		{
			// No file was specified
			result.setNrErrors(1);
			log.logError(toString(), Messages.getString("JobMysqlBulkLoad.Nofilename.Label"));
		}
		return result;
	}

	public DatabaseMeta[] getUsedDatabaseConnections()
	{
		return new DatabaseMeta[] { connection, };
	}

	public boolean isReplacedata()
	{
		return replacedata;
	}

	public void setReplacedata(boolean replacedata)
	{
		this.replacedata = replacedata;
	}


	public void setLocalInfile(boolean localinfile)
	{
		this.localinfile = localinfile;
	}

	public boolean isLocalInfile()
	{
		return localinfile;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

	public void setLineterminated(String lineterminated)
	{
		this.lineterminated = lineterminated;
	}

	public void setLinestarted(String linestarted)
	{
		this.linestarted = linestarted;
	}

	public String getEnclosed()
	{
		return enclosed;
	}
	public String getRealEnclosed()
	{
		return environmentSubstitute(getEnclosed());
	}

	public void setEnclosed(String enclosed)
	{
		this.enclosed = enclosed;
	}

	public String getEscaped()
	{
		return escaped;
	}

	public String getRealEscaped()
	{
		return environmentSubstitute(getEscaped());
	}

	public void setEscaped(String escaped)
	{
		this.escaped = escaped;
	}

	public String getSeparator()
	{
		return separator;
	}

	public String getLineterminated()
	{
		return lineterminated;
	}

	public String getLinestarted()
	{
		return linestarted;
	}

	public String getRealLinestarted()
	{
		return environmentSubstitute(getLinestarted());
	}

	public String getRealLineterminated()
	{
		return environmentSubstitute(getLineterminated());
	}

	public String getRealSeparator()
	{
		return environmentSubstitute(getSeparator());
	}

	public void setIgnorelines(String ignorelines)
	{
		this.ignorelines = ignorelines;
	}

	public String getIgnorelines()
	{
		return ignorelines;
	}

	public String getRealIgnorelines()
	{
		return environmentSubstitute(getIgnorelines());
	}

	public void setListattribut(String listattribut)
	{
		this.listattribut = listattribut;
	}

	public String getListattribut()
	{
		return listattribut;
	}

	public String getRealListattribut()
	{
		return environmentSubstitute(getListattribut());
	}
	
	public void setAddFileToResult(boolean addfiletoresultin)
	{
		this.addfiletoresult = addfiletoresultin;
	}

	public boolean isAddFileToResult()
	{
		return addfiletoresult;
	}

	private String MysqlString(String listcolumns)
	{
		/*
		 * Handle forbiden char like '
		 */
		String returnString="";
		String[] split = listcolumns.split(",");

		for (int i=0;i<split.length;i++)
		{
			if (returnString.equals(""))
				returnString =  "`" + Const.trim(split[i]) + "`";
			else
				returnString = returnString +  ", `" + Const.trim(split[i]) + "`";
		}

		return returnString;
	}

  public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
    List<ResourceReference> references = super.getResourceDependencies(jobMeta);
    ResourceReference reference = null;
    if (connection != null) {
      reference = new ResourceReference(this);
      references.add(reference);
      reference.getEntries().add( new ResourceEntry(connection.getHostname(), ResourceType.SERVER));
      reference.getEntries().add( new ResourceEntry(connection.getDatabaseName(), ResourceType.DATABASENAME));
    }
    if ( filename != null) {
      String realFilename = getRealFilename();
      if (reference == null) {
        reference = new ResourceReference(this);
        references.add(reference);
      }
      reference.getEntries().add( new ResourceEntry(realFilename, ResourceType.FILE));
    }
    return references;
  }

  @Override
  public void check(List<CheckResultInterface> remarks, JobMeta jobMeta)
  {
    ValidatorContext ctx = new ValidatorContext();
    putVariableSpace(ctx, getVariables());
    putValidators(ctx, notBlankValidator(), fileExistsValidator());
    andValidator().validate(this, "filename", remarks, ctx);//$NON-NLS-1$

    andValidator().validate(this, "tablename", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
  }


}

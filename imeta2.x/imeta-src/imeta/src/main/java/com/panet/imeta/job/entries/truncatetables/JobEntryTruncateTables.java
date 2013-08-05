/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package com.panet.imeta.job.entries.truncatetables;

import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notNullValidator;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.database.Database;
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
import com.panet.imeta.job.entry.validator.ValidatorContext;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;

/**
 * This defines a Truncate Tables job entry.
 * 
 * @author Samatar
 * @since 22-07-2008
 *
 */
public class JobEntryTruncateTables extends JobEntryBase implements Cloneable, JobEntryInterface
{
	public static final String ENTRY_ATTRIBUTE_CONNECTION = "connection";
	public static final String ENTRY_ATTRIBUTE_ARG_FROM_PREVIOUS = "arg_from_previous";
	public static final String ENTRY_ATTRIBUTE_NAME = "name";
	public static final String ENTRY_ATTRIBUTE_SCHEMANAME = "schemaname";
	
	public boolean argFromPrevious;
	  
	private DatabaseMeta connection;
	
	public String arguments[];

	public String schemaname[];
	
	private int NrErrors=0;
	private int NrSuccess=0;
	boolean continueProcess=true;

	public JobEntryTruncateTables(String n)
	{
	    super(n, "");
	    this.argFromPrevious=false;
	    this.arguments = null;
	    this.schemaname=null;
	    this.connection=null;
		setID(-1L);
		setJobEntryType(JobEntryType.TRUNCATE_TABLES);
	}

	public JobEntryTruncateTables()
	{
		this("");
	}

	public JobEntryTruncateTables(JobEntryBase jeb)
	{
		super(jeb);
	}
    
    public Object clone()
    {
        JobEntryTruncateTables je = (JobEntryTruncateTables) super.clone();
        return je;
    }

	public String getXML()
	{
        StringBuffer retval = new StringBuffer(200);
		
		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_CONNECTION, this.connection==null?null:this.connection.getName()));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_ARG_FROM_PREVIOUS, this.argFromPrevious));
		retval.append("      <fields>").append(Const.CR); //$NON-NLS-1$
	    if (arguments != null) {
	      for (int i = 0; i < this.arguments.length; i++) {
	        retval.append("        <field>").append(Const.CR); //$NON-NLS-1$
	        retval.append("          ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_NAME, this.arguments[i])); //$NON-NLS-1$ //$NON-NLS-2$
	        retval.append("          ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_SCHEMANAME, this.schemaname[i])); //$NON-NLS-1$ //$NON-NLS-2$
	        retval.append("        </field>").append(Const.CR); //$NON-NLS-1$
	      }
	    }
	    retval.append("      </fields>").append(Const.CR); //$NON-NLS-1$
		return retval.toString();
	}
	
	public void loadXML(Node entrynode, List<DatabaseMeta>  databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);

			String dbname = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_CONNECTION);
			this.connection    = DatabaseMeta.findDatabase(databases, dbname);
			this.argFromPrevious = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_ARG_FROM_PREVIOUS)); 
			  
		    Node fields = XMLHandler.getSubNode(entrynode, "fields"); //$NON-NLS-1$

		      // How many field arguments?
		      int nrFields = XMLHandler.countNodes(fields, "field"); //$NON-NLS-1$
		      this.arguments = new String[nrFields];
		      this.schemaname = new String[nrFields];

		      // Read them all...
		      for (int i = 0; i < nrFields; i++) {
		        Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i); //$NON-NLS-1$
		        this.arguments[i] = XMLHandler.getTagValue(fnode, ENTRY_ATTRIBUTE_NAME); //$NON-NLS-1$
		        this.schemaname[i] = XMLHandler.getTagValue(fnode, ENTRY_ATTRIBUTE_SCHEMANAME); //$NON-NLS-1$
		      }
		}
		catch(KettleException e)
		{
			throw new KettleXMLException(Messages.getString("JobEntryTruncateTables.UnableLoadXML"),e);
		}
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		connection = DatabaseMeta.findDatabase(
				(List<DatabaseMeta>) databases, JobEntryBase.parameterToLong(p
						.get(id + ".connection")));
		argFromPrevious = JobEntryBase.parameterToBoolean(p.get(id + ".argFromPrevious"));
		String[] arguments = p.get(id + "_selectedTablesGrid.arguments");
		String[] schemaname = p.get(id + "_selectedTablesGrid.schemaname");
		this.arguments = (arguments != null) ? arguments : (new String[0]) ;
		this.schemaname = (schemaname != null) ? schemaname : (new String[0]);
	}

	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
	throws KettleException
	{
	try
	{
		super.loadRep(rep, id_jobentry, databases, slaveServers);

			long id_db = rep.getJobEntryAttributeInteger(id_jobentry, "id_database");
			if (id_db>0)
			{
				this.connection = DatabaseMeta.findDatabase(databases, id_db);
			}
			else
			{
				// This is were we end up in normally, the previous lines are for backward compatibility.
				this.connection = DatabaseMeta.findDatabase(databases, rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_CONNECTION));
			}
			this.argFromPrevious = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_ARG_FROM_PREVIOUS);
			 // How many arguments?
		      int argnr = rep.countNrJobEntryAttributes(id_jobentry, ENTRY_ATTRIBUTE_NAME); //$NON-NLS-1$
		      this.arguments = new String[argnr];
		      this.schemaname = new String[argnr];

		      // Read them all...
		      for (int a = 0; a < argnr; a++) {
		    	  this.arguments[a] = rep.getJobEntryAttributeString(id_jobentry, a, ENTRY_ATTRIBUTE_NAME); //$NON-NLS-1$
		    	  this.schemaname[a] = rep.getJobEntryAttributeString(id_jobentry, a, ENTRY_ATTRIBUTE_SCHEMANAME); //$NON-NLS-1$
		      }

		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("JobEntryTruncateTables.UnableLoadRep",""+id_jobentry), dbe);
		}
	}
	
	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);
			
			if (this.connection!=null) rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_CONNECTION, this.connection.getName());
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ARG_FROM_PREVIOUS, this.argFromPrevious);
		      // save the arguments...
		      if (this.arguments != null) {
		        for (int i = 0; i < this.arguments.length; i++) {
		          rep.saveJobEntryAttribute(id_job, getID(), i, ENTRY_ATTRIBUTE_NAME, this.arguments[i]); //$NON-NLS-1$
		          rep.saveJobEntryAttribute(id_job, getID(), i, ENTRY_ATTRIBUTE_SCHEMANAME, this.schemaname[i]); //$NON-NLS-1$
		        }
		      }
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("JobEntryTruncateTables.UnableSaveRep",""+id_job), dbe);
		}
	}
	
	public void setDatabase(DatabaseMeta database)
	{
		this.connection = database;
	}
	
	public DatabaseMeta getDatabase()
	{
		return this.connection;
	}
	
	public boolean evaluates()
	{
		return true;
	}

	public boolean isUnconditional()
	{
		return true;
	}
	private boolean TruncateTables(LogWriter log,String tablename, String schemaname, Database db)
	{
		boolean retval=false;
		String realSchemaname=schemaname;
		String realTablename=tablename;
		try{

			if(!Const.isEmpty(realSchemaname))
                	realTablename = db.getDatabaseMeta().getQuotedSchemaTableCombination(realSchemaname, realTablename);
                
			// check if table exists!
			if(db.checkTableExists(realTablename)){
				if(!Const.isEmpty(realSchemaname))
					db.truncateTable(tablename, realSchemaname);
				else
					db.truncateTable(tablename);
		
				if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryTruncateTables.Log.TableTruncated",realTablename));
				
				retval=true;
			}else{
				log.logError(toString(), Messages.getString("JobEntryTruncateTables.Error.CanNotFindTable",realTablename));
			}
		}catch (Exception e)
		{
			log.logError(toString(), Messages.getString("JobEntryTruncateTables.Error.CanNotTruncateTables",realTablename,e.toString()));
		}
		return retval;
	}

	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
	    List<RowMetaAndData> rows = result.getRows();
	    RowMetaAndData resultRow = null;
		
		result.setResult(true);
		NrErrors=0;
		continueProcess=true;
		NrSuccess=0;
		
	    if (argFromPrevious) {
		      if(log.isDetailed()) 
		    	  log.logDetailed(toString(), Messages.getString("JobEntryTruncateTables.FoundPreviousRows", String.valueOf((rows != null ? rows.size() : 0)))); //$NON-NLS-1$
		      if(rows.size()==0) return result;
	    }
		if (connection!=null)
		{ 
			Database db = new Database(connection);
			db.shareVariablesWith(this);
			try
			{
				db.connect();
			    if (argFromPrevious && rows != null) // Copy the input row to the (command line) arguments
			    {

			      for (int iteration = 0; iteration < rows.size() && !parentJob.isStopped() && continueProcess; iteration++) {  
			    		resultRow = rows.get(iteration);

			    		// Get values from previous result 
			    		String tablename_previous = resultRow.getString(0, null);
			    		String schemaname_previous = resultRow.getString(1, null);
			        
				        if(!Const.isEmpty(tablename_previous))
				        {
				            if(log.isDetailed()) 
				          	  log.logDetailed(toString(), Messages.getString("JobEntryTruncateTables.ProcessingRow", tablename_previous, schemaname_previous)); //$NON-NLS-1$
				        }else{
				      	  log.logError(toString(), Messages.getString("JobEntryTruncateTables.RowEmpty")); //$NON-NLS-1$ 
				        }
			      }
			        
			      }else if (arguments!=null) {
	        		 for (int i = 0; i < arguments.length && !parentJob.isStopped() && continueProcess; i++) {
	                     String realTablename = environmentSubstitute(arguments[i]);                
	             		 String realSchemaname = environmentSubstitute(schemaname[i]); 
	        			 if(!Const.isEmpty(realTablename)) {
		        	    	  if(log.isDetailed()) 
		        	    		  log.logDetailed(toString(), Messages.getString("JobEntryTruncateTables.ProcessingArg", arguments[i], schemaname[i])); //$NON-NLS-1$
		        			
		        	    	  // let's truncate table
		        	    	  if(TruncateTables(log, realTablename, realSchemaname, db)) 
		        	    		  updateSuccess();
		        	    	  else
		        	    		  updateErrors();
	        			 }else{
	        				  log.logError(toString(), Messages.getString("JobEntryTruncateTables.ArgEmpty", arguments[i], schemaname[i])); //$NON-NLS-1$ 
	        			 }
	        	      }	
			      }
			}
			catch(Exception dbe){
				result.setNrErrors(1);
				log.logError(toString(), Messages.getString("JobEntryTruncateTables.Error.RunningEntry",dbe.getMessage()));
			}finally{
				if(db!=null) db.disconnect();
			}
		}
		else {
			result.setNrErrors(1);
			log.logError(toString(),Messages.getString("JobEntryTruncateTables.NoDbConnection"));
		}
		
		result.setNrErrors(NrErrors);
		result.setResult(NrErrors==0);
		return result;
	}
	private void updateErrors()
	{
		NrErrors++;
		continueProcess=false;
	}
	private void updateSuccess()
	{
		NrSuccess++;
	}
    
    public DatabaseMeta[] getUsedDatabaseConnections()
    {
        return new DatabaseMeta[] { connection, };
    }
    public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {
        boolean res = andValidator().validate(this, "arguments", remarks, putValidators(notNullValidator())); //$NON-NLS-1$

        if (res == false) {
          return;
        }

        ValidatorContext ctx = new ValidatorContext();
        putVariableSpace(ctx, getVariables());
        putValidators(ctx, notNullValidator(), fileExistsValidator());

        for (int i = 0; i < arguments.length; i++) {
          andValidator().validate(this, "arguments[" + i + "]", remarks, ctx); //$NON-NLS-1$ //$NON-NLS-2$
        }
      }

      public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
        List<ResourceReference> references = super.getResourceDependencies(jobMeta);
        if (arguments != null) {
          ResourceReference reference = null;
          for (int i=0; i<arguments.length; i++) {
            String filename = jobMeta.environmentSubstitute(arguments[i]);
            if (reference == null) {
              reference = new ResourceReference(this);
              references.add(reference);
            }
            reference.getEntries().add( new ResourceEntry(filename, ResourceType.FILE));
         }
        }
        return references;
      }

}

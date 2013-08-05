/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Samatar HASSAN.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/

package com.panet.imeta.job.entries.waitforsql;

import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.util.ArrayList;
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
import com.panet.imeta.core.row.RowMetaInterface;
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
 * This defines a Wait for SQL data job entry
 * 
 * @author Samatar
 * @since 22-07-2008
 *
 */
public class JobEntryWaitForSQL extends JobEntryBase implements Cloneable, JobEntryInterface
{
	public static final String ENTRY_ATTRIBUTE_CONNECTION = "connection";
	public static final String ENTRY_ATTRIBUTE_SCHEMANAME = "schemaname";
	public static final String ENTRY_ATTRIBUTE_TABLENAME = "tablename";
	public static final String ENTRY_ATTRIBUTE_SUCCESS_CONDITION = "success_condition";
	public static final String ENTRY_ATTRIBUTE_ROWS_COUNT_VALUE = "rows_count_value";
	public static final String ENTRY_ATTRIBUTE_IS_CUSTOM_SQL = "is_custom_sql";
	public static final String ENTRY_ATTRIBUTE_IS_USEVARS = "is_usevars";
	public static final String ENTRY_ATTRIBUTE_CUSTOM_SQL = "custom_sql";
	public static final String ENTRY_ATTRIBUTE_ADD_ROWS_RESULT = "add_rows_result";
	public static final String ENTRY_ATTRIBUTE_MAXIMUM_TIMEOUT = "maximum_timeout";
	public static final String ENTRY_ATTRIBUTE_CHECK_CYCLE_TIME = "check_cycle_time";
	public static final String ENTRY_ATTRIBUTE_SUCCESS_ON_TIMEOUT = "success_on_timeout";
	public static final String ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS = "clear_result_rows";
	
	public boolean isClearResultList;
	
	public boolean isAddRowsResult;
	
	public boolean isUseVars;  
	
	public boolean iscustomSQL;
	
	public String customSQL;
	
	private DatabaseMeta connection;
	
	public String tablename;

	public String schemaname;
	
	private String  maximumTimeout;      // maximum timeout in seconds
	private String  checkCycleTime;      // cycle time in seconds
	private boolean successOnTimeout;
	
	private static final String selectCount="SELECT count(*) FROM ";
	
	public static final String[] successConditionsDesc = new String[] { 
		Messages.getString("JobEntryWaitForSQL.SuccessWhenRowCountEqual.Label"), 
		Messages.getString("JobEntryWaitForSQL.SuccessWhenRowCountDifferent.Label"),
		Messages.getString("JobEntryWaitForSQL.SuccessWhenRowCountSmallerThan.Label"),
		Messages.getString("JobEntryWaitForSQL.SuccessWhenRowCountSmallerOrEqualThan.Label"),
		Messages.getString("JobEntryWaitForSQL.SuccessWhenRowCountGreaterThan.Label"),
		Messages.getString("JobEntryWaitForSQL.SuccessWhenRowCountGreaterOrEqual.Label")
	
	};
	public static final String[] successConditionsCode = new String[] { 
		"rows_count_equal", 
		"rows_count_different",
		"rows_count_smaller",
		"rows_count_smaller_equal",
		"rows_count_greater",
		"rows_count_greater_equal"
	};
	
	public static final int SUCCESS_CONDITION_ROWS_COUNT_EQUAL=0;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_DIFFERENT=1;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_SMALLER=2;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_SMALLER_EQUAL=3;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_GREATER=4;
	public static final int SUCCESS_CONDITION_ROWS_COUNT_GREATER_EQUAL=5;

	public String rowsCountValue;	
	public int successCondition;
	static private String DEFAULT_MAXIMUM_TIMEOUT  = "0";        // infinite timeout
	static private String DEFAULT_CHECK_CYCLE_TIME = "60";       // 1 minute

	public JobEntryWaitForSQL(String n)
	{
	    super(n, "");
	    isClearResultList=true;
	    rowsCountValue="0";
	    successCondition=SUCCESS_CONDITION_ROWS_COUNT_GREATER;
	    iscustomSQL=false;
	    isUseVars=false;
	    isAddRowsResult=false;
	    customSQL=null;
	    schemaname=null;
	    tablename=null;
		connection=null;
		maximumTimeout   = DEFAULT_MAXIMUM_TIMEOUT;
		checkCycleTime   = DEFAULT_CHECK_CYCLE_TIME;
		successOnTimeout = false;
		setID(-1L);
		setJobEntryType(JobEntryType.WAIT_FOR_SQL);
	}

	public JobEntryWaitForSQL()
	{
		this("");
	}

	public JobEntryWaitForSQL(JobEntryBase jeb)
	{
		super(jeb);
	}
    
    public Object clone()
    {
    	JobEntryWaitForSQL je = (JobEntryWaitForSQL) super.clone();
        return je;
    }
    public int getSuccessCondition() {
		return successCondition;
	}
	public static int getSuccessConditionByDesc(String tt) {
		if (tt == null)
			return 0;

		for (int i = 0; i < successConditionsDesc.length; i++) {
			if (successConditionsDesc[i].equalsIgnoreCase(tt))
				return i;
		}

		// If this fails, try to match using the code.
		return getSuccessConditionByCode(tt);
	}
	public String getXML()
	{
        StringBuffer retval = new StringBuffer(200);
		
		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_CONNECTION, connection==null?null:connection.getName()));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_SCHEMANAME, schemaname));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_TABLENAME, tablename));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_SUCCESS_CONDITION,getSuccessConditionCode(successCondition)));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_ROWS_COUNT_VALUE, rowsCountValue));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_IS_CUSTOM_SQL, iscustomSQL));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_IS_USEVARS, isUseVars));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_CUSTOM_SQL, customSQL));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_ADD_ROWS_RESULT, isAddRowsResult));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_MAXIMUM_TIMEOUT, maximumTimeout));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_CHECK_CYCLE_TIME, checkCycleTime));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_SUCCESS_ON_TIMEOUT, successOnTimeout));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS, isClearResultList));
		return retval.toString();
	}
	private static String getSuccessConditionCode(int i) {
		if (i < 0 || i >= successConditionsCode.length)
			return successConditionsCode[0];
		return successConditionsCode[i];
	}
	private static int getSucessConditionByCode(String tt) {
		if (tt == null)
			return 0;

		for (int i = 0; i < successConditionsCode.length; i++) {
			if (successConditionsCode[i].equalsIgnoreCase(tt))
				return i;
		}
		return 0;
	}
	public static String getSuccessConditionDesc(int i) {
		if (i < 0 || i >= successConditionsDesc.length)
			return successConditionsDesc[0];
		return successConditionsDesc[i];
	}

	public boolean isSuccessOnTimeout() {
		return successOnTimeout;
	}

	public void setSuccessOnTimeout(boolean successOnTimeout) {
		this.successOnTimeout = successOnTimeout;
	}

	public String getCheckCycleTime() {
		return checkCycleTime;
	}
	public void setCheckCycleTime(String checkCycleTime) {
		this.checkCycleTime = checkCycleTime;
	}

	public String getMaximumTimeout() {
		return maximumTimeout;
	}
	public void setMaximumTimeout(String maximumTimeout) {
		this.maximumTimeout = maximumTimeout;
	}
	public void loadXML(Node entrynode, List<DatabaseMeta>  databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			String dbname = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_CONNECTION);
			connection    = DatabaseMeta.findDatabase(databases, dbname);
			schemaname =XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_SCHEMANAME); 
			tablename =XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_TABLENAME); 
			successCondition = getSucessConditionByCode(Const.NVL(XMLHandler.getTagValue(entrynode,	ENTRY_ATTRIBUTE_SUCCESS_CONDITION), ""));
			rowsCountValue = Const.NVL(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_ROWS_COUNT_VALUE), "0");	
			iscustomSQL = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_IS_CUSTOM_SQL));
			isUseVars = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_IS_USEVARS));
			customSQL =XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_CUSTOM_SQL); 
			isAddRowsResult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_ADD_ROWS_RESULT)); 
			maximumTimeout = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_MAXIMUM_TIMEOUT);
			checkCycleTime = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_CHECK_CYCLE_TIME);
			successOnTimeout = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_SUCCESS_ON_TIMEOUT));
			isClearResultList = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS)); 
			
		}
		catch(KettleException e)
		{
			throw new KettleXMLException(Messages.getString("JobEntryWaitForSQL.UnableLoadXML"),e);
		}
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		connection = DatabaseMeta.findDatabase((List<DatabaseMeta>) databases, JobEntryBase
				.parameterToLong(p.get(id + ".databaseConnection")));
		
		schemaname = JobEntryBase.parameterToString(p.get(id + ".schemaname"));
		tablename = JobEntryBase.parameterToString(p.get(id + ".tablename"));
		successCondition = JobEntryBase.parameterToInt(p.get(id + ".successCondition"));
		rowsCountValue = JobEntryBase.parameterToString(p.get(id + ".rowsCountValue"));
		iscustomSQL = JobEntryBase.parameterToBoolean(p.get(id + ".iscustomSQL"));
		isUseVars = JobEntryBase.parameterToBoolean(p.get(id + ".isUseVars"));
		isAddRowsResult = JobEntryBase.parameterToBoolean(p.get(id + ".isAddRowsResult"));
		customSQL = JobEntryBase.parameterToString(p.get(id + ".customSQL"));
		maximumTimeout = JobEntryBase.parameterToString(p.get(id + ".maximumTimeout"));
		checkCycleTime = JobEntryBase.parameterToString(p.get(id + ".checkCycleTime"));
		successOnTimeout = JobEntryBase.parameterToBoolean(p.get(id + ".successOnTimeout"));
		isClearResultList = JobEntryBase.parameterToBoolean(p.get(id + ".isClearResultList"));
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
				connection = DatabaseMeta.findDatabase(databases, id_db);
			}
			else
			{
				// This is were we end up in normally, the previous lines are for backward compatibility.
				connection = DatabaseMeta.findDatabase(databases, rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_CONNECTION));
			}

			schemaname = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_SCHEMANAME);
			tablename = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_TABLENAME);
			successCondition = getSuccessConditionByCode(Const.NVL(rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_SUCCESS_CONDITION), ""));
			rowsCountValue = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_ROWS_COUNT_VALUE);
			iscustomSQL = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_IS_CUSTOM_SQL);
			isUseVars = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_IS_USEVARS);
			isAddRowsResult = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_ADD_ROWS_RESULT);
			customSQL = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_CUSTOM_SQL);
			maximumTimeout = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_MAXIMUM_TIMEOUT);
			checkCycleTime = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_CHECK_CYCLE_TIME);
			successOnTimeout = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_SUCCESS_ON_TIMEOUT);
			isClearResultList = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS);
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("JobEntryWaitForSQL.UnableLoadRep",""+id_jobentry), dbe);
		}
	}
	private static int getSuccessConditionByCode(String tt) {
		if (tt == null)
			return 0;

		for (int i = 0; i < successConditionsCode.length; i++) {
			if (successConditionsCode[i].equalsIgnoreCase(tt))
				return i;
		}
		return 0;
	}

	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);
			
			if (connection!=null) rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_CONNECTION, connection.getName());

			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_SCHEMANAME, schemaname);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_TABLENAME, tablename);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_SUCCESS_CONDITION, getSuccessConditionCode(successCondition));
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ROWS_COUNT_VALUE, rowsCountValue); 
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_IS_CUSTOM_SQL, customSQL);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_CUSTOM_SQL, iscustomSQL);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_IS_USEVARS, isUseVars);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ADD_ROWS_RESULT, isAddRowsResult);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_MAXIMUM_TIMEOUT, maximumTimeout);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_CHECK_CYCLE_TIME, checkCycleTime);
            rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_SUCCESS_ON_TIMEOUT, successOnTimeout);
            rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_CLEAR_RESULT_ROWS, isClearResultList);
					
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException(Messages.getString("JobEntryWaitForSQL.UnableSaveRep",""+id_job), dbe);
		}
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
		return false;
	}

	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
		result.setResult(false);
		result.setNrErrors(1);
		String realCustomSQL=null;
        String realTablename = environmentSubstitute(tablename);                
        String realSchemaname = environmentSubstitute(schemaname); 
		
		if (connection==null)
		{
			log.logError(toString(),Messages.getString("JobEntryWaitForSQL.NoDbConnection"));
			return result;
		}
		
		
		if(iscustomSQL)
        {
			// clear result list rows
			if(isClearResultList) result.getRows().clear();
			
        	realCustomSQL=customSQL;
        	if(isUseVars) realCustomSQL=environmentSubstitute(realCustomSQL);
        	if(log.isDebug()) log.logDebug(toString(), Messages.getString("JobEntryWaitForSQL.Log.EnteredCustomSQL",realCustomSQL));
        	
        	if(Const.isEmpty(realCustomSQL))
        	{
        		log.logError(toString(), Messages.getString("JobEntryWaitForSQL.Error.NoCustomSQL"));
        		return result;
        	}
        	
        }else
        {   
	        if(Const.isEmpty(realTablename))
        	{
        		log.logError(toString(), Messages.getString("JobEntryWaitForSQL.Error.NoTableName"));
        		return result;
        	}
        }
		
		try
		{
			// check connection
			// connect and disconnect
			Database dbchecked = null;
			try
			{
				dbchecked = new Database(connection);	
				dbchecked.connect();
			}
			finally
			{
				if(dbchecked!=null) dbchecked.disconnect();
			}
			
	    	// starttime (in seconds)
	    	long timeStart = System.currentTimeMillis() / 1000;
			
			int nrRowsLimit=Const.toInt(environmentSubstitute(rowsCountValue),0);
			if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryWaitForSQL.Log.nrRowsLimit",""+nrRowsLimit));
	    	
	
			long iMaximumTimeout = Const.toInt(environmentSubstitute(maximumTimeout), Const.toInt(DEFAULT_MAXIMUM_TIMEOUT, 0));
			long iCycleTime = Const.toInt(environmentSubstitute(checkCycleTime), 	Const.toInt(DEFAULT_CHECK_CYCLE_TIME, 0));
			
			//
			// Sanity check on some values, and complain on insanity
			//
			if ( iMaximumTimeout < 0 )
			{
				iMaximumTimeout = Const.toInt(DEFAULT_MAXIMUM_TIMEOUT, 0);
				log.logBasic(toString(), "Maximum timeout invalid, reset to " + iMaximumTimeout);
			}
	
			if ( iCycleTime < 1 )
			{
				// If lower than 1 set to the default
				iCycleTime = Const.toInt(DEFAULT_CHECK_CYCLE_TIME, 1);
				log.logBasic(toString(), "Check cycle time invalid, reset to " + iCycleTime);
			}
			
	
			if ( iMaximumTimeout == 0 )
			{
				log.logBasic(toString(), "Waiting indefinitely for SQL data");
			}
			else 
			{
				log.logBasic(toString(), "Waiting " + iMaximumTimeout + " seconds for SQL data");
			}
	
			boolean continueLoop = true;
			while ( continueLoop && !parentJob.isStopped() )
			{
				if(SQLDataOK(log,result,nrRowsLimit, realSchemaname,realTablename, realCustomSQL))
				{
					// SQL data exists, we're happy to exit
					log.logBasic(toString(), "Detected SQL data within timeout");
					result.setResult( true );
					continueLoop = false;
				}else
				{
					long now = System.currentTimeMillis() / 1000;
	
					if ( (iMaximumTimeout > 0) && 
							(now > (timeStart + iMaximumTimeout)))
					{													
						continueLoop = false;
	
						// SQL data doesn't exist after timeout, either true or false						
						if ( isSuccessOnTimeout() )
						{
							log.logBasic(toString(), "Didn't detect SQL data before timeout, success");
							result.setResult( true );
						}
						else
						{
							log.logBasic(toString(), "Didn't detect SQL data before timeout, failure");
							result.setResult( false );
						}						
					}
					// sleep algorithm					
					long sleepTime = 0;
	
					if ( iMaximumTimeout == 0 )
					{
						sleepTime = iCycleTime;
					}
					else
					{						
						if ( (now + iCycleTime) < (timeStart + iMaximumTimeout) )
						{
							sleepTime = iCycleTime;
						}
						else
						{
							sleepTime = iCycleTime - ((now + iCycleTime) - 
									(timeStart + iMaximumTimeout));
						}
					}
					try 
					{
						if ( sleepTime > 0 )
						{
							if ( log.isDetailed() )
							{
								log.logDetailed(toString(), "Sleeping " + sleepTime + " seconds before next check for SQL data");							
							}						   
							Thread.sleep(sleepTime * 1000);
						}
					} catch (InterruptedException e) {
						// something strange happened
						result.setResult( false );
						continueLoop = false;						
					}		
				}
				
			}
		}
   		catch (Exception e )
		{
			log.logBasic(toString(), "Exception while waiting for SQL data: " + e.getMessage());
		}
		
		return result;
	}
	private boolean SQLDataOK(LogWriter log,Result result,long nrRowsLimit, String realSchemaName,
			String realTableName, String customSQL) throws KettleException
	{
		String countStatement=null;
		long rowsCount=0;
		boolean successOK=false;
		List<Object[]> ar = null;
		RowMetaInterface rowMeta=null;
		Database db = new Database(connection);
	
		try
		{
			db.connect();
			if(iscustomSQL)
			{
				countStatement=customSQL;
			}else
			{
	        	if(!Const.isEmpty(realSchemaName))
	        	{
	        		countStatement=selectCount + db.getDatabaseMeta().getQuotedSchemaTableCombination(realSchemaName,realTableName);
	        	}else
	        	{
	        		countStatement=selectCount + db.getDatabaseMeta().quoteField(realTableName);
	        	}
			}
			
			
			if(countStatement!=null)
			{
				if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryWaitForSQL.Log.RunSQLStatement",countStatement));
					
				if(iscustomSQL)
				{
					ar =db.getRows(countStatement, 0);
					if(ar!=null)
					{
						rowsCount=ar.size();
					}else
					{
						if(log.isDebug()) log.logDebug(toString(), Messages.getString("JobEntryWaitForSQL.Log.customSQLreturnedNothing",countStatement));
					}
					
				}else
				{
					RowMetaAndData row=db.getOneRow(countStatement);
					if(row!=null)
					{
						rowsCount=row.getInteger(0);
					}
				}
				if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryWaitForSQL.Log.NrRowsReturned",""+rowsCount));
				
				switch(successCondition)
	             {				
	                case JobEntryWaitForSQL.SUCCESS_CONDITION_ROWS_COUNT_EQUAL: 
	                	successOK=(rowsCount==nrRowsLimit);
	                	break;
	                case JobEntryWaitForSQL.SUCCESS_CONDITION_ROWS_COUNT_DIFFERENT: 
	                	successOK=(rowsCount!=nrRowsLimit);
	                	break;
	                case JobEntryWaitForSQL.SUCCESS_CONDITION_ROWS_COUNT_SMALLER:
	                	successOK=(rowsCount<nrRowsLimit);
	                	break;
	                case JobEntryWaitForSQL.SUCCESS_CONDITION_ROWS_COUNT_SMALLER_EQUAL:
	                	successOK=(rowsCount<=nrRowsLimit);
	                case JobEntryWaitForSQL.SUCCESS_CONDITION_ROWS_COUNT_GREATER:
	                	successOK=(rowsCount>nrRowsLimit);
	                case JobEntryWaitForSQL.SUCCESS_CONDITION_ROWS_COUNT_GREATER_EQUAL:
	                	successOK=(rowsCount>=nrRowsLimit);
	                	break;
	                default: 
	                	break;
	             }	
			} // endif countStatement!=null    
		}
		catch(KettleDatabaseException dbe)
		{
			log.logError(toString(), Messages.getString("JobEntryWaitForSQL.Error.RunningEntry",dbe.getMessage()));
		}finally{
			if(db!=null) 
			{
				if(isAddRowsResult && iscustomSQL && ar!=null) rowMeta=db.getQueryFields(countStatement, false);
				db.disconnect();
			}
		}
	
		if(successOK)
		{
			// ad rows to result
			if(isAddRowsResult && iscustomSQL && ar!=null)
			{
				List<RowMetaAndData> rows=new ArrayList<RowMetaAndData>();;
				for(int i=0;i<ar.size();i++)
				{
					rows.add(new RowMetaAndData(rowMeta,ar.get(i)));
				}
				if(rows!=null) result.getRows().addAll(rows);
			}
		}
		return successOK;

	}
    
    public DatabaseMeta[] getUsedDatabaseConnections()
    {
        return new DatabaseMeta[] { connection, };
    }

    public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
        List<ResourceReference> references = super.getResourceDependencies(jobMeta);
        if (connection != null) {
          ResourceReference reference = new ResourceReference(this);
          reference.getEntries().add( new ResourceEntry(connection.getHostname(), ResourceType.SERVER));
          reference.getEntries().add( new ResourceEntry(connection.getDatabaseName(), ResourceType.DATABASENAME));
          references.add(reference);
        }
        return references;
      }    
      @Override
      public void check(List<CheckResultInterface> remarks, JobMeta jobMeta)
      {
        andValidator().validate(this, "WaitForSQL", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
      }
    
}

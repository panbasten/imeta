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

package com.panet.imeta.trans.steps.tableoutput;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.SQLStatement;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.DatabaseImpact;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Table Output meta data.
 * 
 * @author Matt Casters
 * @since  2-jun-2003
 */
public class TableOutputMeta extends BaseStepMeta implements StepMetaInterface
{
	
	public static final String STEP_ATTRIBUTE_ID_CONNECTION = "id_connection";
	public static final String STEP_ATTRIBUTE_SCHEMA = "schema";
	public static final String STEP_ATTRIBUTE_TABLE = "table";
	public static final String STEP_ATTRIBUTE_COMMIT = "commit";
	public static final String STEP_ATTRIBUTE_TRUNCATE = "truncate";
	public static final String STEP_ATTRIBUTE_IGNORE_ERRORS = "ignore_errors";
	public static final String STEP_ATTRIBUTE_USE_BATCH = "use_batch";
	public static final String STEP_ATTRIBUTE_SPECIFY_FIELDS = "specify_fields";
	public static final String STEP_ATTRIBUTE_PARTITIONING_ENABLED = "partitioning_enabled";
	public static final String STEP_ATTRIBUTE_PARTITIONING_FIELD = "partitioning_field";
	public static final String STEP_ATTRIBUTE_PARTITIONING_DAILY = "partitioning_daily";
	public static final String STEP_ATTRIBUTE_PARTITIONING_MONTHLY = "partitioning_monthly";
	public static final String STEP_ATTRIBUTE_TABLENAME_IN_FIELD = "tablename_in_field";
	public static final String STEP_ATTRIBUTE_TABLENAME_FIELD = "tablename_field";
	public static final String STEP_ATTRIBUTE_TABLENAME_IN_TABLE = "tablename_in_table";
	public static final String STEP_ATTRIBUTE_RETURN_KEYS = "return_keys";
	public static final String STEP_ATTRIBUTE_RETURN_FIELD = "return_field";
	public static final String STEP_ATTRIBUTE_COLUMN_NAME = "column_name";
	public static final String STEP_ATTRIBUTE_STREAM_NAME = "stream_name";
	
	
	private DatabaseMeta databaseMeta;
    private String       schemaName;
	private String       tablename;
	private String       commitSize;
	private boolean      truncateTable;
	private boolean      ignoreErrors;
	private boolean      useBatchUpdate;
    
    private boolean      partitioningEnabled;
    private String       partitioningField;
    private boolean      partitioningDaily;
    private boolean      partitioningMonthly;
    	
    private boolean      tableNameInField;
    private String       tableNameField;
    private boolean      tableNameInTable;
    
    private boolean      returningGeneratedKeys;
    private String       generatedKeyField;
    
    /** Do we explicitly select the fields to update in the database */
    private boolean      specifyFields;
    
    /** Fields containing the values in the input stream to insert */
    private String[]     fieldStream;

    /** Fields in the table to insert */
    private String[]     fieldDatabase;

    /**
	 * @return Returns the generatedKeyField.
	 */
	public String getGeneratedKeyField() {
		return generatedKeyField;
	}

	/**
	 * @param generatedKeyField The generatedKeyField to set.
	 */
	public void setGeneratedKeyField(String generatedKeyField) {
		this.generatedKeyField = generatedKeyField;
	}

	/**
	 * @return Returns the returningGeneratedKeys.
	 */
	public boolean isReturningGeneratedKeys() {
		return returningGeneratedKeys;
	}

	/**
	 * @param returningGeneratedKeys The returningGeneratedKeys to set.
	 */
	public void setReturningGeneratedKeys(boolean returningGeneratedKeys) {
		this.returningGeneratedKeys = returningGeneratedKeys;
	}

	/**
     * @return Returns the tableNameInTable.
     */
    public boolean isTableNameInTable()
    {
        return tableNameInTable;
    }

    /**
     * @param tableNameInTable The tableNameInTable to set.
     */
    public void setTableNameInTable(boolean tableNameInTable)
    {
        this.tableNameInTable = tableNameInTable;
    }

    /**
     * @return Returns the tableNameField.
     */
    public String getTableNameField()
    {
        return tableNameField;
    }

    /**
     * @param tableNameField The tableNameField to set.
     */
    public void setTableNameField(String tableNameField)
    {
        this.tableNameField = tableNameField;
    }

    /**
     * @return Returns the tableNameInField.
     */
    public boolean isTableNameInField()
    {
        return tableNameInField;
    }

    /**
     * @param tableNameInField The tableNameInField to set.
     */
    public void setTableNameInField(boolean tableNameInField)
    {
        this.tableNameInField = tableNameInField;
    }

    
    /**
     * @return Returns the partitioningDaily.
     */
    public boolean isPartitioningDaily()
    {
        return partitioningDaily;
    }

    /**
     * @param partitioningDaily The partitioningDaily to set.
     */
    public void setPartitioningDaily(boolean partitioningDaily)
    {
        this.partitioningDaily = partitioningDaily;
    }

    /**
     * @return Returns the partitioningMontly.
     */
    public boolean isPartitioningMonthly()
    {
        return partitioningMonthly;
    }

    /**
     * @param partitioningMontly The partitioningMontly to set.
     */
    public void setPartitioningMonthly(boolean partitioningMontly)
    {
        this.partitioningMonthly = partitioningMontly;
    }

    /**
     * @return Returns the partitioningEnabled.
     */
    public boolean isPartitioningEnabled()
    {
        return partitioningEnabled;
    }

    /**
     * @param partitioningEnabled The partitioningEnabled to set.
     */
    public void setPartitioningEnabled(boolean partitioningEnabled)
    {
        this.partitioningEnabled = partitioningEnabled;
    }

    /**
     * @return Returns the partitioningField.
     */
    public String getPartitioningField()
    {
        return partitioningField;
    }

    /**
     * @param partitioningField The partitioningField to set.
     */
    public void setPartitioningField(String partitioningField)
    {
        this.partitioningField = partitioningField;
    }

    
    public TableOutputMeta()
	{
		super(); // allocate BaseStepMeta
		useBatchUpdate=true;
		commitSize="1000";
		
		fieldStream   = new String[0];
		fieldDatabase = new String[0];
	}
    
	public void allocate(int nrRows)
	{
		fieldStream   = new String[nrRows];
		fieldDatabase = new String[nrRows];
	}    
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode, databases);
	}

	public Object clone()
	{
		TableOutputMeta retval = (TableOutputMeta)super.clone();
		
		int nrStream   = fieldStream.length;
		int nrDatabase = fieldDatabase.length;

		retval.fieldStream   = new String[nrStream];
		retval.fieldDatabase = new String[nrDatabase];

		for (int i = 0; i < nrStream; i++)
		{
			retval.fieldStream[i] = fieldStream[i];
		}
		
		for (int i = 0; i < nrDatabase; i++)
		{
			retval.fieldDatabase[i] = fieldDatabase[i];
		}	
		
		return retval;
	}
	
	/**
     * @return Returns the database.
     */
    public DatabaseMeta getDatabaseMeta()
    {
        return databaseMeta;
    }
    
    /**
     * @param database The database to set.
     */
    public void setDatabaseMeta(DatabaseMeta database)
    {
        this.databaseMeta = database;
    }
    
    /**
     * @return Returns the commitSize.
     */
    public String getCommitSize()
    {
        return commitSize;
    }
    
    /**
     * @param commitSize The commitSize to set.
     */
    public void setCommitSize(int commitSizeInt)
    {
        this.commitSize = Integer.toString(commitSizeInt);
    }

    /**
     * @param commitSize The commitSize to set.
     */
    public void setCommitSize(String commitSize)
    {
        this.commitSize = commitSize;
    }

    /**
     * @return Returns the tablename.
     */
    public String getTablename()
    {
        return tablename;
    }
    
    /**
     * @param tablename The tablename to set.
     */
    public void setTablename(String tablename)
    {
        this.tablename = tablename;
    }
    
    /**
     * @return Returns the truncate table flag.
     */
    public boolean truncateTable()
    {
        return truncateTable;
    }
    
    /**
     * @param truncateTable The truncate table flag to set.
     */
    public void setTruncateTable(boolean truncateTable)
    {
        this.truncateTable = truncateTable;
    }
    
    /**
     * @param ignoreErrors The ignore errors flag to set.
     */
    public void setIgnoreErrors(boolean ignoreErrors)
    {
        this.ignoreErrors = ignoreErrors;
    }
    
    /**
     * @return Returns the ignore errors flag.
     */
    public boolean ignoreErrors()
    {
        return ignoreErrors;
    }
    
    /**
     * @param specifyFields The specify fields flag to set.
     */
    public void setSpecifyFields(boolean specifyFields)
    {
        this.specifyFields = specifyFields;
    }
    
    /**
     * @return Returns the specify fields flag.
     */
    public boolean specifyFields()
    {
        return specifyFields;
    }
	
    /**
     * @param useBatchUpdate The useBatchUpdate flag to set.
     */
    public void setUseBatchUpdate(boolean useBatchUpdate)
    {
        this.useBatchUpdate = useBatchUpdate;
    }
    
    /**
     * @return Returns the useBatchUpdate flag.
     */
    public boolean useBatchUpdate()
    {
        return useBatchUpdate;
    }
    
    
	private void readData(Node stepnode, List<? extends SharedObjectInterface> databases) throws KettleXMLException
	{
		try
		{
			String con     = XMLHandler.getTagValue(stepnode, "connection");
			databaseMeta   = DatabaseMeta.findDatabase(databases, con);
            schemaName     = XMLHandler.getTagValue(stepnode, "schema");
			tablename      = XMLHandler.getTagValue(stepnode, "table");
			commitSize     = XMLHandler.getTagValue(stepnode, "commit");
			truncateTable  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "truncate"));
			ignoreErrors   = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "ignore_errors"));
			useBatchUpdate = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "use_batch"));
			
			// If not present it will be false to be compatible with pre-v3.2
			specifyFields  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "specify_fields"));

            partitioningEnabled  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "partitioning_enabled"));
            partitioningField    = XMLHandler.getTagValue(stepnode, "partitioning_field");
            partitioningDaily    = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "partitioning_daily"));
            partitioningMonthly  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "partitioning_monthly"));
            
            tableNameInField     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "tablename_in_field"));
            tableNameField       = XMLHandler.getTagValue(stepnode, "tablename_field");
            tableNameInTable     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "tablename_in_table"));
            
            returningGeneratedKeys = "Y".equalsIgnoreCase( XMLHandler.getTagValue(stepnode, "return_keys"));
            generatedKeyField      = XMLHandler.getTagValue(stepnode, "return_field");
            
			Node fields = XMLHandler.getSubNode(stepnode, "fields");   //$NON-NLS-1$
			int nrRows  = XMLHandler.countNodes(fields, "field");      //$NON-NLS-1$
			
			allocate(nrRows);
			
			for (int i=0;i<nrRows;i++)
			{
				Node knode = XMLHandler.getSubNodeByNr(fields, "field", i);         //$NON-NLS-1$
				
				fieldDatabase   [i] = XMLHandler.getTagValue(knode, "column_name");  //$NON-NLS-1$
				fieldStream     [i] = XMLHandler.getTagValue(knode, "stream_name"); //$NON-NLS-1$
			}
        }
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void setDefault()
	{
		databaseMeta = null;
		tablename      = "";
		commitSize = "1000";
        
        partitioningEnabled = false;
        partitioningMonthly = true;
        partitioningField   = "";
        tableNameInTable    = true;
        tableNameField      = "";
        
        // To be compatible with pre-v3.2 (SB)
        specifyFields       = false;        
	}

	public String getXML()
	{
		StringBuilder retval=new StringBuilder();
		
		retval.append("    "+XMLHandler.addTagValue("connection",     databaseMeta==null?"":databaseMeta.getName()));
        retval.append("    "+XMLHandler.addTagValue("schema",         schemaName));
		retval.append("    "+XMLHandler.addTagValue("table",          tablename));
		retval.append("    "+XMLHandler.addTagValue("commit",         commitSize));
		retval.append("    "+XMLHandler.addTagValue("truncate",       truncateTable));
		retval.append("    "+XMLHandler.addTagValue("ignore_errors",  ignoreErrors));
		retval.append("    "+XMLHandler.addTagValue("use_batch",      useBatchUpdate));
		retval.append("    "+XMLHandler.addTagValue("specify_fields", specifyFields));

        retval.append("    "+XMLHandler.addTagValue("partitioning_enabled",   partitioningEnabled));
        retval.append("    "+XMLHandler.addTagValue("partitioning_field",     partitioningField));
        retval.append("    "+XMLHandler.addTagValue("partitioning_daily",     partitioningDaily));
        retval.append("    "+XMLHandler.addTagValue("partitioning_monthly",   partitioningMonthly));
        
        retval.append("    "+XMLHandler.addTagValue("tablename_in_field", tableNameInField));
        retval.append("    "+XMLHandler.addTagValue("tablename_field", tableNameField));
        retval.append("    "+XMLHandler.addTagValue("tablename_in_table", tableNameInTable));

		retval.append("    "+XMLHandler.addTagValue("return_keys", returningGeneratedKeys));
        retval.append("    "+XMLHandler.addTagValue("return_field", generatedKeyField));
        
		retval.append("    <fields>").append(Const.CR); //$NON-NLS-1$

		for (int i=0;i<fieldDatabase.length;i++)
		{
			retval.append("        <field>").append(Const.CR); //$NON-NLS-1$
			retval.append("          ").append(XMLHandler.addTagValue("column_name", fieldDatabase[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("          ").append(XMLHandler.addTagValue("stream_name", fieldStream[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        </field>").append(Const.CR); //$NON-NLS-1$
		}
		retval.append("    </fields>").append(Const.CR); //$NON-NLS-1$

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {

		databaseMeta  = DatabaseMeta.findDatabase(
					(List<DatabaseMeta>) databases, BaseStepMeta.parameterToLong(p
							.get(id + ".connection")));   
		schemaName = BaseStepMeta.parameterToString(p.get(id + ".schemaName"));
		tablename = BaseStepMeta.parameterToString(p.get(id + ".tablename"));
		commitSize = BaseStepMeta.parameterToString(p.get(id + ".commitSize"));	
		truncateTable = BaseStepMeta.parameterToBoolean(p.get( id + ".truncateTable"));
		ignoreErrors = BaseStepMeta.parameterToBoolean( p.get( id + ".ignoreErrors"));
		useBatchUpdate = BaseStepMeta.parameterToBoolean( p.get( id + ".useBatchUpdate"));
		specifyFields = BaseStepMeta.parameterToBoolean(p.get( id + ".specifyFields"));
		partitioningEnabled = BaseStepMeta.parameterToBoolean(p.get( id + ".partitioningEnabled"));
		partitioningField = BaseStepMeta.parameterToString( p.get( id + ".partitioningField"));
		partitioningDaily = BaseStepMeta.parameterToBoolean(p.get( id + ".partitioningDaily"));
		partitioningMonthly = BaseStepMeta.parameterToBoolean(p.get( id + ".partitioningMonthly"));
		tableNameInField = BaseStepMeta.parameterToBoolean( p.get( id + ".tableNameInField "));
		tableNameField = BaseStepMeta.parameterToString(p.get( id + ".tableNameField"));
		tableNameInTable = BaseStepMeta.parameterToBoolean( p.get( id + ".tableNameInTable"));
		returningGeneratedKeys = BaseStepMeta.parameterToBoolean( p.get( id + ".returningGeneratedKeys"));
		generatedKeyField = BaseStepMeta.parameterToString( p.get( id + ".generatedKeyField"));
		
		String[] fieldDatabase = p.get(id+"_fieldToInsert.fieldDatabase");
		String[] fieldStream = p.get(id+"_fieldToInsert.fieldStream");
		
		if(fieldDatabase==null) fieldDatabase = new String[0];
		if(fieldStream==null) fieldStream = new String[0];
		
        this.fieldDatabase= fieldDatabase;
		this.fieldStream=fieldStream;
		
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException
	{
		try
		{
			long id_connection = rep.getStepAttributeInteger(id_step,STEP_ATTRIBUTE_ID_CONNECTION);
			databaseMeta = DatabaseMeta.findDatabase(databases,id_connection);
			schemaName = rep.getStepAttributeString(id_step,STEP_ATTRIBUTE_SCHEMA);
			tablename = rep.getStepAttributeString(id_step,STEP_ATTRIBUTE_TABLE);
			long commitSizeInt = rep.getStepAttributeInteger(id_step,STEP_ATTRIBUTE_COMMIT);
			commitSize = rep.getStepAttributeString(id_step,STEP_ATTRIBUTE_COMMIT);
			
			if (Const.isEmpty(commitSize)) {
				commitSize=Long.toString(commitSizeInt);
			}
			
			truncateTable = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_TRUNCATE);
			ignoreErrors = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_IGNORE_ERRORS);
			useBatchUpdate = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_USE_BATCH);
			specifyFields = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_SPECIFY_FIELDS);
			partitioningEnabled = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_PARTITIONING_ENABLED);
			partitioningField = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_PARTITIONING_FIELD);
			partitioningDaily = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_PARTITIONING_DAILY);
			partitioningMonthly = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_PARTITIONING_MONTHLY);
			tableNameInField = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_TABLENAME_IN_FIELD);
			tableNameField = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_TABLENAME_FIELD);
			tableNameInTable = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_TABLENAME_IN_TABLE);
			returningGeneratedKeys = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_RETURN_KEYS);
			generatedKeyField = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_RETURN_FIELD);
            
            int nrCols    = rep.countNrStepAttributes(id_step, STEP_ATTRIBUTE_COLUMN_NAME); 
			int nrStreams = rep.countNrStepAttributes(id_step, STEP_ATTRIBUTE_STREAM_NAME); 
			
			int nrRows = (nrCols < nrStreams ? nrStreams : nrCols);
			allocate(nrRows);
			
			for (int idx=0; idx < nrRows; idx++)
			{
				fieldDatabase[idx] = Const.NVL(rep.getStepAttributeString(id_step, 
						                                                  idx, STEP_ATTRIBUTE_COLUMN_NAME), "");  
				fieldStream[idx]   = Const.NVL(rep.getStepAttributeString(id_step, 
						                                                  idx, STEP_ATTRIBUTE_STREAM_NAME), "");  
			}
		}
		catch(Exception e)
		{
			throw new KettleException("Unexpected error reading step information from the repository", e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ID_CONNECTION, databaseMeta==null?-1:databaseMeta.getID());
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_SCHEMA,schemaName);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_TABLE, tablename);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_COMMIT,commitSize);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_TRUNCATE,truncateTable);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_IGNORE_ERRORS, ignoreErrors);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_USE_BATCH, useBatchUpdate);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_SPECIFY_FIELDS,specifyFields);			
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_PARTITIONING_ENABLED, partitioningEnabled);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_PARTITIONING_FIELD, partitioningField);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_PARTITIONING_DAILY, partitioningDaily);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_PARTITIONING_MONTHLY, partitioningMonthly);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_TABLENAME_IN_FIELD, tableNameInField);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_TABLENAME_FIELD , tableNameField);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_TABLENAME_IN_TABLE, tableNameInTable);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_RETURN_KEYS, returningGeneratedKeys);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_RETURN_FIELD, generatedKeyField);
            
        	int nrRows = (fieldDatabase.length < fieldStream.length ? fieldStream.length :
        		                                                      fieldDatabase.length);
			for (int idx=0; idx < nrRows; idx++)
			{
				String columnName = (idx < fieldDatabase.length ? fieldDatabase[idx] : "");
				String streamName = (idx < fieldStream.length   ? fieldStream[idx] : "");
				rep.saveStepAttribute(id_transformation, id_step, idx, STEP_ATTRIBUTE_COLUMN_NAME, columnName); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, idx, STEP_ATTRIBUTE_STREAM_NAME, streamName); //$NON-NLS-1$
			}
            
			// Also, save the step-database relationship!
			if (databaseMeta!=null)  { 
				rep.insertStepDatabase(id_transformation, id_step, databaseMeta.getID());			
			}
		}
		catch(Exception e)
		{
			throw new KettleException("Unable to save step information to the repository for id_step="+id_step, e);
		}
	}

    public void getFields(RowMetaInterface row, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException 
    {
    	// Just add the returning key field...
		if (returningGeneratedKeys && generatedKeyField!=null && generatedKeyField.length()>0)
		{
			ValueMetaInterface key = new ValueMeta(space.environmentSubstitute(generatedKeyField), ValueMetaInterface.TYPE_INTEGER);
			key.setOrigin(origin);
			row.addValueMeta(key);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		if (databaseMeta!=null)
		{
			CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.ConnectionExists"), stepMeta);
			remarks.add(cr);

			Database db = new Database(databaseMeta);
			db.shareVariablesWith(transMeta);
			try
			{
				db.connect();
				
				cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.ConnectionOk"), stepMeta);
				remarks.add(cr);

				if (!Const.isEmpty(tablename))
				{
                    String schemaTable = databaseMeta.getQuotedSchemaTableCombination(schemaName, tablename);
					// Check if this table exists...
					if (db.checkTableExists(schemaTable))
					{
						cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.TableAccessible", schemaTable), stepMeta);
						remarks.add(cr);

						RowMetaInterface r = db.getTableFields(schemaTable);
						if (r!=null)
						{
							cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.TableOk", schemaTable), stepMeta);
							remarks.add(cr);

							String error_message = "";
							boolean error_found = false;
							// OK, we have the table fields.
							// Now see what we can find as previous step...
							if (prev!=null && prev.size()>0)
							{
								cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.FieldsReceived", ""+prev.size()), stepMeta);
								remarks.add(cr);
	
								if ( ! specifyFields() )  {
									// Starting from prev...
									for (int i=0;i<prev.size();i++)
									{
										ValueMetaInterface pv = prev.getValueMeta(i);
										int idx = r.indexOfValue(pv.getName());
										if (idx<0) 
										{
											error_message+="\t\t"+pv.getName()+" ("+pv.getTypeDesc()+")"+Const.CR;
											error_found=true;
										} 
									}
									if (error_found) 
									{
										error_message=Messages.getString("TableOutputMeta.CheckResult.FieldsNotFoundInOutput", error_message);

										cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepMeta);
										remarks.add(cr);
									}
									else
									{
										cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.AllFieldsFoundInOutput"), stepMeta);
										remarks.add(cr);
									}
								}
								else  {
									// Specifying the column names explicitly
									for (int i=0;i<getFieldDatabase().length;i++)
									{
										int idx = r.indexOfValue(getFieldDatabase()[i]);
										if (idx<0) 
										{
											error_message+="\t\t"+ getFieldDatabase()[i] + Const.CR;
											error_found=true;
										} 
									}
									if (error_found) 
									{
										error_message=Messages.getString("TableOutputMeta.CheckResult.FieldsSpecifiedNotInTable", error_message);

										cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepMeta);
										remarks.add(cr);
									}
									else
									{
										cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.AllFieldsFoundInOutput"), stepMeta);
										remarks.add(cr);
									}
								}
	
								error_message="";
								if ( ! specifyFields() )  {
									// Starting from table fields in r...
									for (int i=0;i<getFieldDatabase().length;i++)
									{
										ValueMetaInterface rv = r.getValueMeta(i);
										int idx = prev.indexOfValue(rv.getName());
										if (idx<0) 
										{
											error_message+="\t\t"+rv.getName()+" ("+rv.getTypeDesc()+")"+Const.CR;
											error_found=true;
										} 
									}
									if (error_found) 
									{
										error_message=Messages.getString("TableOutputMeta.CheckResult.FieldsNotFound", error_message);

										cr = new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING, error_message, stepMeta);
										remarks.add(cr);
									}
									else
									{
										cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.AllFieldsFound"), stepMeta);
										remarks.add(cr);
									}
								}
								else  {
									// Specifying the column names explicitly									
									for (int i=0;i<getFieldStream().length;i++)
									{
										int idx = prev.indexOfValue(getFieldStream()[i]);
										if (idx<0) 
										{
											error_message+="\t\t"+getFieldStream()[i]+Const.CR;
											error_found=true;
										} 
									}
									if (error_found) 
									{
										error_message=Messages.getString("TableOutputMeta.CheckResult.FieldsSpecifiedNotFound", error_message);

										cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepMeta);
										remarks.add(cr);
									}
									else
									{
										cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.AllFieldsFound"), stepMeta);
										remarks.add(cr);
									}									
								}							
							}
							else
							{
								cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("TableOutputMeta.CheckResult.NoFields"), stepMeta);
								remarks.add(cr);
							}
						}
						else
						{
							cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("TableOutputMeta.CheckResult.TableNotAccessible"), stepMeta);
							remarks.add(cr);
						}
					}
					else
					{
						cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("TableOutputMeta.CheckResult.TableError", schemaTable), stepMeta);
						remarks.add(cr);
					}
				}
				else
				{
					cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("TableOutputMeta.CheckResult.NoTableName"), stepMeta);
					remarks.add(cr);
				}
			}
			catch(KettleException e)
			{
				cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("TableOutputMeta.CheckResult.UndefinedError", e.getMessage()), stepMeta);
				remarks.add(cr);
			}
			finally
			{
				db.disconnect();
			}
		}
		else
		{
			CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("TableOutputMeta.CheckResult.NoConnection"), stepMeta);
			remarks.add(cr);
		}
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("TableOutputMeta.CheckResult.ExpectedInputOk"), stepMeta);
			remarks.add(cr);
		}
		else
		{
			CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("TableOutputMeta.CheckResult.ExpectedInputError"), stepMeta);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new TableOutput(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}
	
	public StepDataInterface getStepData()
	{
		return new TableOutputData();
	}
	
	public void analyseImpact(List<DatabaseImpact> impact, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		if (truncateTable)
		{
			DatabaseImpact ii = new DatabaseImpact( DatabaseImpact.TYPE_IMPACT_TRUNCATE, 
											transMeta.getName(),
											stepMeta.getName(),
											databaseMeta.getDatabaseName(),
											tablename,
											"",
											"",
											"",
											"",
											"Truncate of table"
											);
			impact.add(ii);

		}
		// The values that are entering this step are in "prev":
		if (prev!=null)
		{
			for (int i=0;i<prev.size();i++)
			{
				ValueMetaInterface v = prev.getValueMeta(i);
				DatabaseImpact ii = new DatabaseImpact( DatabaseImpact.TYPE_IMPACT_WRITE, 
												transMeta.getName(),
												stepMeta.getName(),
												databaseMeta.getDatabaseName(),
												tablename,
												v.getName(),
												v.getName(),
                                                v!=null?v.getOrigin():"?",
												"",
												"Type = "+v.toStringMeta()
												);
				impact.add(ii);
			}
		}
	}

	public SQLStatement getSQLStatements(TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev)
	{
		SQLStatement retval = new SQLStatement(stepMeta.getName(), databaseMeta, null); // default: nothing to do!
	
		if (databaseMeta!=null)
		{
			if (prev!=null && prev.size()>0)
			{
				if (!Const.isEmpty(tablename))
				{
					Database db = new Database(databaseMeta);
					db.shareVariablesWith(transMeta);
					try
					{
						db.connect();
						
                        String schemaTable = databaseMeta.getQuotedSchemaTableCombination(schemaName, tablename);
                        String cr_table = db.getDDL(schemaTable, prev);
						
						// Empty string means: nothing to do: set it to null...
						if (cr_table==null || cr_table.length()==0) cr_table=null;
						
						retval.setSQL(cr_table);
					}
					catch(KettleDatabaseException dbe)
					{
						retval.setError(Messages.getString("TableOutputMeta.Error.ErrorConnecting", dbe.getMessage()));
					}
					finally
					{
						db.disconnect();
					}
				}
				else
				{
					retval.setError(Messages.getString("TableOutputMeta.Error.NoTable"));
				}
			}
			else
			{
				retval.setError(Messages.getString("TableOutputMeta.Error.NoInput"));
			}
		}
		else
		{
			retval.setError(Messages.getString("TableOutputMeta.Error.NoConnection"));
		}

		return retval;
	}

    public RowMetaInterface getRequiredFields() throws KettleException
    {
        if (databaseMeta!=null)
        {
            Database db = new Database(databaseMeta);
            try
            {
                db.connect();
                
                if (!Const.isEmpty(tablename))
                {
                    String schemaTable = databaseMeta.getQuotedSchemaTableCombination(schemaName, tablename);
                    
                    // Check if this table exists...
                    if (db.checkTableExists(schemaTable))
                    {
                        return db.getTableFields(schemaTable);
                    }
                    else
                    {
                        throw new KettleException(Messages.getString("TableOutputMeta.Exception.TableNotFound"));
                    }
                }
                else
                {
                    throw new KettleException(Messages.getString("TableOutputMeta.Exception.TableNotSpecified"));
                }
            }
            catch(Exception e)
            {
                throw new KettleException(Messages.getString("TableOutputMeta.Exception.ErrorGettingFields"), e);
            }
            finally
            {
                db.disconnect();
            }
        }
        else
        {
            throw new KettleException(Messages.getString("TableOutputMeta.Exception.ConnectionNotDefined"));
        }

    }
    
    public DatabaseMeta[] getUsedDatabaseConnections()
    {
        if (databaseMeta!=null) 
        {
            return new DatabaseMeta[] { databaseMeta };
        }
        else
        {
            return super.getUsedDatabaseConnections();
        }
    }
    
    /**
     * @return Fields containing the values in the input stream to insert.
     */
    public String[] getFieldStream()
    {
        return fieldStream;
    }
    
    /**
     * @param fieldStream The fields containing the values in the input stream to insert in the table.
     */
    public void setFieldStream(String[] fieldStream)
    {
        this.fieldStream = fieldStream;
    }

    /**
     * @return Fields containing the fieldnames in the database insert.
     */
    public String[] getFieldDatabase()
    {
        return fieldDatabase;
    }
    
    /**
     * @param fieldDatabase The fields containing the names of the fields to insert.
     */
    public void setFieldDatabase(String[] fieldDatabase)
    {
        this.fieldDatabase = fieldDatabase;
    }    

    /**
     * @return the schemaName
     */
    public String getSchemaName()
    {
        return schemaName;
    }

    /**
     * @param schemaName the schemaName to set
     */
    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }
    
    public boolean supportsErrorHandling()
    {
        return true;
    }

}

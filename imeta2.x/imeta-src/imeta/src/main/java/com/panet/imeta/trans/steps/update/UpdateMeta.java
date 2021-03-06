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

package com.panet.imeta.trans.steps.update;

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



/*
 * Created on 26-apr-2003
 *
 */

public class UpdateMeta extends BaseStepMeta implements StepMetaInterface
{
	
	    public static final String STEP_ATTRIBUTE_ID_CONNECTION ="id_connection";
	    public static final String STEP_ATTRIBUTE_SKIP_LOOKUP ="skip_lookup";
		public static final String STEP_ATTRIBUTE_COMMIT ="commit";
		public static final String STEP_ATTRIBUTE_SCHEMA ="schema";
		public static final String STEP_ATTRIBUTE_TABLE ="table";
		public static final String STEP_ATTRIBUTE_ERROR_IGNORED ="error_ignored";
	    public static final String STEP_ATTRIBUTE_IGNORE_FLAG_FIELD ="ignore_flag_field";

		public static final String STEP_ATTRIBUTE_KEY_NAME ="key_name";
		public static final String STEP_ATTRIBUTE_VALUE_NAME ="value_name";
		public static final String STEP_ATTRIBUTE_KEY_FIELD ="key_field";
		public static final String STEP_ATTRIBUTE_KEY_CONDITION ="key_condition";
		public static final String STEP_ATTRIBUTE_KEY_NAME2 ="key_name2";
		public static final String STEP_ATTRIBUTE_VALUE_RENAME ="value_rename";
		public static final String STEP_ATTRIBUTE_VALUE_UPDATE ="value_update";
		
    /** The lookup table name */
    private String schemaName;

    /** The lookup table name */
	private String tableName;
	
	/** database connection */
	private DatabaseMeta databaseMeta;
	
	/** which field in input stream to compare with? */
	private String keyStream[];
	
	/** field in table  */
	private String keyLookup[];
	
	/** Comparator: =, <>, BETWEEN, ... */
	private String keyCondition[];
	
	/** Extra field for between... */
	private String keyStream2[];
	
	/** Field value to update after lookup */
	private String updateLookup[];
	
	/** Stream name to update value with */
	private String updateStream[];
	
	/** Commit size for inserts/updates */
	private int    commitSize; 
    
    /** update errors are ignored if this flag is set to true */
    private boolean errorIgnored;
    
    /** adds a boolean field to the output indicating success of the update */
    private String  ignoreFlagField;
    
    /** adds a boolean field to skip lookup and directly update selected fields */
    private boolean  skipLookup;
	
	public UpdateMeta()
	{
		super(); // allocate BaseStepMeta
	}
	
    /**
     * @return Returns the commitSize.
     */
    public int getCommitSize()
    {
        return commitSize;
    }
    
    /**
     * @param commitSize The commitSize to set.
     */
    public void setCommitSize(int commitSize)
    {
        this.commitSize = commitSize;
    }
    /**
     * @return Returns the skipLookup.
     */
    public boolean isSkipLookup()
    {
        return skipLookup;
    }

    /**
     * @param skipLookup The skipLookup to set.
     */
    public void setSkipLookup(boolean skipLookup)
    {
        this.skipLookup = skipLookup;
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
     * @return Returns the keyCondition.
     */
    public String[] getKeyCondition()
    {
        return keyCondition;
    }
    
    /**
     * @param keyCondition The keyCondition to set.
     */
    public void setKeyCondition(String[] keyCondition)
    {
        this.keyCondition = keyCondition;
    }
    
    /**
     * @return Returns the keyLookup.
     */
    public String[] getKeyLookup()
    {
        return keyLookup;
    }
    
    /**
     * @param keyLookup The keyLookup to set.
     */
    public void setKeyLookup(String[] keyLookup)
    {
        this.keyLookup = keyLookup;
    }
    
    /**
     * @return Returns the keyStream.
     */
    public String[] getKeyStream()
    {
        return keyStream;
    }
    
    /**
     * @param keyStream The keyStream to set.
     */
    public void setKeyStream(String[] keyStream)
    {
        this.keyStream = keyStream;
    }
    
    /**
     * @return Returns the keyStream2.
     */
    public String[] getKeyStream2()
    {
        return keyStream2;
    }
    
    /**
     * @param keyStream2 The keyStream2 to set.
     */
    public void setKeyStream2(String[] keyStream2)
    {
        this.keyStream2 = keyStream2;
    }
    
    /**
     * @return Returns the tableName.
     */
    public String getTableName()
    {
        return tableName;
    }
    
    /**
     * @param tableName The tableName to set.
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    /**
     * @return Returns the updateLookup.
     */
    public String[] getUpdateLookup()
    {
        return updateLookup;
    }
    
    /**
     * @param updateLookup The updateLookup to set.
     */
    public void setUpdateLookup(String[] updateLookup)
    {
        this.updateLookup = updateLookup;
    }
    
    /**
     * @return Returns the updateStream.
     */
    public String[] getUpdateStream()
    {
        return updateStream;
    }
    
    /**
     * @param updateStream The updateStream to set.
     */
    public void setUpdateStream(String[] updateStream)
    {
        this.updateStream = updateStream;
    }
    
    /**
     * @return Returns the ignoreError.
     */
    public boolean isErrorIgnored()
    {
        return errorIgnored;
    }

    /**
     * @param ignoreError The ignoreError to set.
     */
    public void setErrorIgnored(boolean ignoreError)
    {
        this.errorIgnored = ignoreError;
    }

    /**
     * @return Returns the ignoreFlagField.
     */
    public String getIgnoreFlagField()
    {
        return ignoreFlagField;
    }



    /**
     * @param ignoreFlagField The ignoreFlagField to set.
     */
    public void setIgnoreFlagField(String ignoreFlagField)
    {
        this.ignoreFlagField = ignoreFlagField;
    }



    public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode, databases);
	}

	public void allocate(int nrkeys, int nrvalues)
	{
		keyStream          = new String[nrkeys];
		keyLookup    = new String[nrkeys];
		keyCondition = new String[nrkeys];
		keyStream2         = new String[nrkeys];
		updateLookup        = new String[nrvalues];
		updateStream    = new String[nrvalues];
	}

	public Object clone()
	{
		UpdateMeta retval = (UpdateMeta)super.clone();
		int nrkeys    = keyStream.length;
		int nrvalues  = updateLookup.length;

		retval.allocate(nrkeys, nrvalues);
		
		for (int i=0;i<nrkeys;i++)
		{
			retval.keyStream         [i] = keyStream[i];
			retval.keyLookup   [i] = keyLookup[i];
			retval.keyCondition[i] = keyCondition[i];
			retval.keyStream2        [i] = keyStream2[i];
		}

		for (int i=0;i<nrvalues;i++)
		{
			retval.updateLookup[i]        = updateLookup[i];
			retval.updateStream[i]    = updateStream[i];
		}
		return retval;
	}
	
	private void readData(Node stepnode, List<? extends SharedObjectInterface> databases)
		throws KettleXMLException
	{
		try
		{
			String csize;
			int nrkeys, nrvalues;
			
			String con = XMLHandler.getTagValue(stepnode, "connection"); //$NON-NLS-1$
			databaseMeta = DatabaseMeta.findDatabase(databases, con);
			csize      = XMLHandler.getTagValue(stepnode, "commit"); //$NON-NLS-1$
			commitSize=Const.toInt(csize, 0);
			skipLookup = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "skip_lookup")); 
            errorIgnored = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "error_ignored")); //$NON-NLS-1$ //$NON-NLS-2$
            ignoreFlagField = XMLHandler.getTagValue(stepnode, "ignore_flag_field"); //$NON-NLS-1$
            schemaName     = XMLHandler.getTagValue(stepnode, "lookup", "schema"); //$NON-NLS-1$ //$NON-NLS-2$
			tableName      = XMLHandler.getTagValue(stepnode, "lookup", "table"); //$NON-NLS-1$ //$NON-NLS-2$
	
			Node lookup = XMLHandler.getSubNode(stepnode, "lookup"); //$NON-NLS-1$
			nrkeys    = XMLHandler.countNodes(lookup, "key"); //$NON-NLS-1$
			nrvalues  = XMLHandler.countNodes(lookup, "value"); //$NON-NLS-1$
			
			allocate(nrkeys, nrvalues);
			
			for (int i=0;i<nrkeys;i++)
			{
				Node knode = XMLHandler.getSubNodeByNr(lookup, "key", i); //$NON-NLS-1$
				
				keyStream         [i] = XMLHandler.getTagValue(knode, "name"); //$NON-NLS-1$
				keyLookup   [i] = XMLHandler.getTagValue(knode, "field"); //$NON-NLS-1$
				keyCondition[i] = XMLHandler.getTagValue(knode, "condition"); //$NON-NLS-1$
				if (keyCondition[i]==null) keyCondition[i]="="; //$NON-NLS-1$
				keyStream2        [i] = XMLHandler.getTagValue(knode, "name2"); //$NON-NLS-1$
			}
	
			for (int i=0;i<nrvalues;i++)
			{
				Node vnode = XMLHandler.getSubNodeByNr(lookup, "value", i); //$NON-NLS-1$
				
				updateLookup[i]        = XMLHandler.getTagValue(vnode, "name"); //$NON-NLS-1$
				updateStream[i]    = XMLHandler.getTagValue(vnode, "rename"); //$NON-NLS-1$
				if (updateStream[i]==null) updateStream[i]=updateLookup[i]; // default: the same name!
			}
		}
		catch(Exception e)
		{
			throw new KettleXMLException(Messages.getString("UpdateMeta.Exception.UnableToReadStepInfoFromXML"), e); //$NON-NLS-1$
		}
	}

	public void setDefault()
	{
		skipLookup=false;
		keyStream    = null;
		updateLookup = null;
		databaseMeta = null;
		commitSize   = 100;
        schemaName   = ""; //$NON-NLS-1$
		tableName    = Messages.getString("UpdateMeta.DefaultTableName"); //$NON-NLS-1$

		int nrkeys   = 0;
		int nrvalues = 0;

		allocate(nrkeys, nrvalues);
		
		for (int i=0;i<nrkeys;i++)
		{
			keyLookup[i]    = "age"; //$NON-NLS-1$
			keyCondition[i] = "BETWEEN"; //$NON-NLS-1$
			keyStream[i]    = "age_from"; //$NON-NLS-1$
			keyStream2[i]   = "age_to"; //$NON-NLS-1$
		}

		for (int i=0;i<nrvalues;i++)
		{
			updateLookup[i]=Messages.getString("UpdateMeta.ColumnName.ReturnField")+i; //$NON-NLS-1$
			updateStream[i]=Messages.getString("UpdateMeta.ColumnName.NewName")+i; //$NON-NLS-1$
		}
	}

	public String getXML()
	{
		StringBuffer retval=new StringBuffer();
		
		retval.append("    "+XMLHandler.addTagValue("connection", databaseMeta==null?"":databaseMeta.getName())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		retval.append("    "+XMLHandler.addTagValue("skip_lookup", skipLookup));
		retval.append("    "+XMLHandler.addTagValue("commit", commitSize)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    "+XMLHandler.addTagValue("error_ignored", errorIgnored)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    "+XMLHandler.addTagValue("ignore_flag_field", ignoreFlagField)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    <lookup>"+Const.CR); //$NON-NLS-1$
        retval.append("      "+XMLHandler.addTagValue("schema", schemaName)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("      "+XMLHandler.addTagValue("table", tableName)); //$NON-NLS-1$ //$NON-NLS-2$

		for (int i=0;i<keyStream.length;i++)
		{
			retval.append("      <key>"+Const.CR); //$NON-NLS-1$
			retval.append("        "+XMLHandler.addTagValue("name", keyStream[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        "+XMLHandler.addTagValue("field", keyLookup[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        "+XMLHandler.addTagValue("condition", keyCondition[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        "+XMLHandler.addTagValue("name2", keyStream2[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        </key>"+Const.CR); //$NON-NLS-1$
		}

		for (int i=0;i<updateLookup.length;i++)
		{
			retval.append("      <value>"+Const.CR); //$NON-NLS-1$
			retval.append("        "+XMLHandler.addTagValue("name", updateLookup[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        "+XMLHandler.addTagValue("rename", updateStream[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        </value>"+Const.CR); //$NON-NLS-1$
		}

		retval.append("      </lookup>"+Const.CR); //$NON-NLS-1$

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		databaseMeta  = DatabaseMeta.findDatabase(
				(List<DatabaseMeta>) databases, BaseStepMeta.parameterToLong(p
						.get(id + ".connection")));   
		skipLookup = BaseStepMeta.parameterToBoolean(p.get(id + ".schemaName"));
		commitSize = BaseStepMeta.parameterToInt(p.get(id + ".commitSize"));
		schemaName = BaseStepMeta.parameterToString(p.get(id + ".schemaName"));
		tableName = BaseStepMeta.parameterToString(p.get(id + ".tableName"));
		errorIgnored = BaseStepMeta.parameterToBoolean(p.get(id + ".errorIgnored"));
		ignoreFlagField = BaseStepMeta.parameterToString(p.get(id + ".ignoreFlagField"));
		
		String[] keyLookup = p.get(id+"_keywords.keyLookup");
		String[] keyCondition = p.get(id+"_keywords.keyCondition");
		String[] keyStream = p.get(id+"_keywords.keyStream");
		String[] keyStream2 = p.get(id+"_keywords.keyStream2");
		
		String[] updateLookup = p.get(id+"_refreshwords.updateLookup");
		String[] updateStream = p.get(id+"_refreshwords.updateStream");
		
		this.keyLookup = keyLookup;
		this.keyCondition = keyCondition;
		this.keyStream = keyStream;
		this.keyStream2 = keyStream2;
		
		this.updateLookup = updateLookup;
		this.updateStream = updateStream;
	
	
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleException
	{
		try
		{
			long id_connection = rep.getStepAttributeInteger(id_step, STEP_ATTRIBUTE_ID_CONNECTION);  //$NON-NLS-1$
			databaseMeta = DatabaseMeta.findDatabase( databases, id_connection);
			skipLookup = rep.getStepAttributeBoolean (id_step, STEP_ATTRIBUTE_SKIP_LOOKUP);
			commitSize = (int)rep.getStepAttributeInteger(id_step,STEP_ATTRIBUTE_COMMIT); //$NON-NLS-1$
            schemaName = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_SCHEMA); //$NON-NLS-1$
			tableName = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_TABLE); //$NON-NLS-1$ 
            errorIgnored = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_ERROR_IGNORED); //$NON-NLS-1$
            ignoreFlagField =rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_IGNORE_FLAG_FIELD); //$NON-NLS-1$
		
            int nrkeys   = rep.countNrStepAttributes(id_step, STEP_ATTRIBUTE_KEY_NAME); //$NON-NLS-1$
			int nrvalues = rep.countNrStepAttributes(id_step, STEP_ATTRIBUTE_VALUE_NAME); //$NON-NLS-1$
			
			allocate(nrkeys, nrvalues);
			
			for (int i=0;i<nrkeys;i++)
			{
				keyStream[i]          = rep.getStepAttributeString(id_step, i, STEP_ATTRIBUTE_KEY_NAME); //$NON-NLS-1$
				keyLookup[i]    = rep.getStepAttributeString(id_step, i, STEP_ATTRIBUTE_KEY_FIELD); //$NON-NLS-1$
				keyCondition[i] = rep.getStepAttributeString(id_step, i, STEP_ATTRIBUTE_KEY_CONDITION); //$NON-NLS-1$
				keyStream2[i]         = rep.getStepAttributeString(id_step, i,STEP_ATTRIBUTE_KEY_NAME2); //$NON-NLS-1$
			}
			
			for (int i=0;i<nrvalues;i++)
			{
				updateLookup[i]        = rep.getStepAttributeString(id_step, i, STEP_ATTRIBUTE_VALUE_NAME); //$NON-NLS-1$
				updateStream[i]    = rep.getStepAttributeString(id_step, i,STEP_ATTRIBUTE_VALUE_RENAME); //$NON-NLS-1$
			}
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("UpdateMeta.Exception.UnexpectedErrorReadingStepInfoFromRepository"), e); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ID_CONNECTION, databaseMeta==null?-1:databaseMeta.getID()); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_SKIP_LOOKUP,    skipLookup);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_COMMIT,        commitSize); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_SCHEMA,        schemaName); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_TABLE,         tableName); //$NON-NLS-1$

            rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ERROR_IGNORED,        errorIgnored); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_IGNORE_FLAG_FIELD,    ignoreFlagField); //$NON-NLS-1$

			for (int i=0;i<keyStream.length;i++)
			{
				rep.saveStepAttribute(id_transformation, id_step, i, STEP_ATTRIBUTE_KEY_NAME,      keyStream[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i, STEP_ATTRIBUTE_KEY_FIELD,     keyLookup[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i, STEP_ATTRIBUTE_KEY_CONDITION, keyCondition[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i, STEP_ATTRIBUTE_KEY_NAME2,     keyStream2[i]); //$NON-NLS-1$
			}
	
			for (int i=0;i<updateLookup.length;i++)
			{
				rep.saveStepAttribute(id_transformation, id_step, i, STEP_ATTRIBUTE_VALUE_NAME,    updateLookup[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i, STEP_ATTRIBUTE_VALUE_RENAME,  updateStream[i]); //$NON-NLS-1$
			}
			
			// Also, save the step-database relationship!
			if (databaseMeta!=null) rep.insertStepDatabase(id_transformation, id_step, databaseMeta.getID());
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("UpdateMeta.Exception.UnableToSaveStepInfoToRepository")+id_step, e); //$NON-NLS-1$
		}
	}
    
    public void getFields(RowMetaInterface row, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException
    {
        if (ignoreFlagField!=null && ignoreFlagField.length()>0)
        {
            ValueMetaInterface v = new ValueMeta(ignoreFlagField, ValueMetaInterface.TYPE_BOOLEAN);
            v.setOrigin(name);
            
            row.addValueMeta( v );
        }
    }

    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		String error_message = ""; //$NON-NLS-1$
		
		if (databaseMeta!=null)
		{
			Database db = new Database(databaseMeta);
			db.shareVariablesWith(transMeta);
			try
			{
				db.connect();
				
				if (!Const.isEmpty(tableName))
				{
					cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("UpdateMeta.CheckResult.TableNameOK"), stepinfo); //$NON-NLS-1$
					remarks.add(cr);
			
					boolean first=true;
					boolean error_found=false;
					error_message = ""; //$NON-NLS-1$
					
					// Check fields in table
                    String schemaTable = databaseMeta.getQuotedSchemaTableCombination(schemaName, tableName);
					RowMetaInterface r = db.getTableFields( schemaTable );
					if (r!=null)
					{
						cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("UpdateMeta.CheckResult.TableExists"), stepinfo); //$NON-NLS-1$
						remarks.add(cr);
			
						for (int i=0;i<keyLookup.length;i++)
						{
							String lufield = keyLookup[i];

							ValueMetaInterface v = r.searchValueMeta(lufield);
							if (v==null)
							{
								if (first)
								{
									first=false;
									error_message+=Messages.getString("UpdateMeta.CheckResult.MissingCompareFieldsInTargetTable")+Const.CR; //$NON-NLS-1$
								}
								error_found=true;
								error_message+="\t\t"+lufield+Const.CR;  //$NON-NLS-1$
							}
						}
						if (error_found)
						{
							cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
						}
						else
						{
							cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("UpdateMeta.CheckResult.AllLookupFieldsFound"), stepinfo); //$NON-NLS-1$
						}
						remarks.add(cr);
						
						// How about the fields to insert/update in the table?
						first=true;
						error_found=false;
						error_message = ""; //$NON-NLS-1$

						for (int i=0;i<updateLookup.length;i++)
						{
							String lufield = updateLookup[i];

							ValueMetaInterface v = r.searchValueMeta(lufield);
							if (v==null)
							{
								if (first)
								{
									first=false;
									error_message+=Messages.getString("UpdateMeta.CheckResult.MissingFieldsToUpdateInTargetTable")+Const.CR; //$NON-NLS-1$
								}
								error_found=true;
								error_message+="\t\t"+lufield+Const.CR;  //$NON-NLS-1$
							}
						}
						if (error_found)
						{
							cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
						}
						else
						{
							cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("UpdateMeta.CheckResult.AllFieldsToUpdateFoundInTargetTable"), stepinfo); //$NON-NLS-1$
						}
						remarks.add(cr);
					}
					else
					{
						error_message=Messages.getString("UpdateMeta.CheckResult.CouldNotReadTableInfo"); //$NON-NLS-1$
						cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
						remarks.add(cr);
					}
				}
				
				// Look up fields in the input stream <prev>
				if (prev!=null && prev.size()>0)
				{
					cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("UpdateMeta.CheckResult.StepReceivingDatas",prev.size()+""), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
					remarks.add(cr);
			
					boolean first=true;
					error_message = ""; //$NON-NLS-1$
					boolean error_found = false;
					
					for (int i=0;i<keyStream.length;i++)
					{
						ValueMetaInterface v = prev.searchValueMeta(keyStream[i]);
						if (v==null)
						{
							if (first)
							{
								first=false;
								error_message+=Messages.getString("UpdateMeta.CheckResult.MissingFieldsInInput")+Const.CR; //$NON-NLS-1$
							}
							error_found=true;
							error_message+="\t\t"+keyStream[i]+Const.CR;  //$NON-NLS-1$
						}
					}
					for (int i=0;i<keyStream2.length;i++)
					{
						if (keyStream2[i]!=null && keyStream2[i].length()>0)
						{
							ValueMetaInterface v = prev.searchValueMeta(keyStream2[i]);
							if (v==null)
							{
								if (first)
								{
									first=false;
									error_message+=Messages.getString("UpdateMeta.CheckResult.MissingFieldsInInput2")+Const.CR; //$NON-NLS-1$
								}
								error_found=true;
								error_message+="\t\t"+keyStream[i]+Const.CR;  //$NON-NLS-1$
							}
						}
					}
					if (error_found)
					{
						cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
					}
					else
					{
						cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("UpdateMeta.CheckResult.AllFieldsFoundInInput"), stepinfo); //$NON-NLS-1$
					}
					remarks.add(cr);

					// How about the fields to insert/update the table with?
					first=true;
					error_found=false;
					error_message = ""; //$NON-NLS-1$

					for (int i=0;i<updateStream.length;i++)
					{
						String lufield = updateStream[i];

						ValueMetaInterface v = prev.searchValueMeta(lufield);
						if (v==null)
						{
							if (first)
							{
								first=false;
								error_message+=Messages.getString("UpdateMeta.CheckResult.MissingInputStreamFields")+Const.CR; //$NON-NLS-1$
							}
							error_found=true;
							error_message+="\t\t"+lufield+Const.CR;  //$NON-NLS-1$
						}
					}
					if (error_found)
					{
						cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
					}
					else
					{
						cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("UpdateMeta.CheckResult.AllFieldsFoundInInput2"), stepinfo); //$NON-NLS-1$
					}
					remarks.add(cr);
				}
				else
				{
					error_message=Messages.getString("UpdateMeta.CheckResult.MissingFieldsInInput3")+Const.CR; //$NON-NLS-1$
					cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
					remarks.add(cr);
				}
			}
			catch(KettleException e)
			{
				error_message = Messages.getString("UpdateMeta.CheckResult.DatabaseErrorOccurred")+e.getMessage(); //$NON-NLS-1$
				cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
				remarks.add(cr);
			}
			finally
			{
				db.disconnect();
			}
		}
		else
		{
			error_message = Messages.getString("UpdateMeta.CheckResult.InvalidConnection"); //$NON-NLS-1$
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
			remarks.add(cr);
		}

		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("UpdateMeta.CheckResult.StepReceivingInfoFromOtherSteps"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("UpdateMeta.CheckResult.NoInputError"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
	}
	
	public SQLStatement getSQLStatements(TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev)
	{
		SQLStatement retval = new SQLStatement(stepMeta.getName(), databaseMeta, null); // default: nothing to do!
	
		if (databaseMeta!=null)
		{
			if (prev!=null && prev.size()>0)
			{
				if (!Const.isEmpty(tableName))
				{
                    String schemaTable = databaseMeta.getQuotedSchemaTableCombination(schemaName, tableName);

					Database db = new Database(databaseMeta);
					db.shareVariablesWith(transMeta);
					try
					{
						db.connect();
						
						if ( getIgnoreFlagField()!=null && 
							 getIgnoreFlagField().length()>0)
						{
						    prev.addValueMeta(new ValueMeta(getIgnoreFlagField(), ValueMetaInterface.TYPE_BOOLEAN));
						}
						
						String cr_table = db.getDDL(schemaTable, 
													prev, 
													null, 
													false, 
													null,
													true
													);
						
						String cr_index = ""; //$NON-NLS-1$
						String idx_fields[] = null;
						
						if (keyLookup!=null && keyLookup.length>0)
						{
							idx_fields = new String[keyLookup.length];
							for (int i=0;i<keyLookup.length;i++) idx_fields[i] = keyLookup[i];
						}
						else
						{
							retval.setError(Messages.getString("UpdateMeta.CheckResult.MissingKeyFields")); //$NON-NLS-1$
						}

						// Key lookup dimensions...
						if (idx_fields!=null && idx_fields.length>0 && !db.checkIndexExists(schemaName, tableName, idx_fields)
						   )
						{
							String indexname = "idx_"+tableName+"_lookup"; //$NON-NLS-1$ //$NON-NLS-2$
							cr_index = db.getCreateIndexStatement(schemaName, tableName, indexname, idx_fields, false, false, false, true);
						}
						
						String sql = cr_table+cr_index;
						if (sql.length()==0) retval.setSQL(null); else retval.setSQL(sql);
					}
					catch(KettleException e)
					{
						retval.setError(Messages.getString("UpdateMeta.ReturnValue.ErrorOccurred")+e.getMessage()); //$NON-NLS-1$
					}
				}
				else
				{
					retval.setError(Messages.getString("UpdateMeta.ReturnValue.NoTableDefinedOnConnection")); //$NON-NLS-1$
				}
			}
			else
			{
				retval.setError(Messages.getString("UpdateMeta.ReturnValue.NotReceivingAnyFields")); //$NON-NLS-1$
			}
		}
		else
		{
			retval.setError(Messages.getString("UpdateMeta.ReturnValue.NoConnectionDefined")); //$NON-NLS-1$
		}

		return retval;
	}
	
	@Override
    public void analyseImpact(List<DatabaseImpact> impact, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info) throws KettleStepException
    {
        if (prev != null)
        {
            // Lookup: we do a lookup on the natural keys 
            for (int i = 0; i < keyLookup.length; i++)
            {
                ValueMetaInterface v = prev.searchValueMeta(keyStream[i]);

                DatabaseImpact ii = new DatabaseImpact(DatabaseImpact.TYPE_IMPACT_READ, transMeta.getName(), stepMeta.getName(), databaseMeta
                        .getDatabaseName(), tableName, keyLookup[i], keyStream[i], v!=null?v.getOrigin():"?", "", "Type = " + v.toStringMeta()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                impact.add(ii);
            }

            // Update fields : read/write
            for (int i = 0; i < updateLookup.length; i++)
            {
                ValueMetaInterface v = prev.searchValueMeta(updateStream[i]);

                DatabaseImpact ii = new DatabaseImpact(DatabaseImpact.TYPE_IMPACT_UPDATE, transMeta.getName(), stepMeta.getName(), databaseMeta
                        .getDatabaseName(), tableName, updateLookup[i], updateStream[i], v!=null?v.getOrigin():"?", "", "Type = " + v.toStringMeta()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                impact.add(ii);
            }
        }
    }

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new Update(stepMeta, stepDataInterface, cnr, tr, trans);
	}
	
	public StepDataInterface getStepData()
	{
		return new UpdateData();
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

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
package com.panet.imeta.trans.steps.execsqlrow;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;
import com.panet.imeta.trans.steps.sql.ExecSQL;


/***
 * Contains meta-data to execute arbitrary SQL from a specified field.
 * 
 * Created on 10-sep-2008
 */
 
public class ExecSQLRowMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_ID_CONNECTION = "id_connection";
	public static final String STEP_ATTRIBUTE_COMMIT = "commit";
	public static final String STEP_ATTRIBUTE_SQL_FIELD = "sql_field";
	public static final String STEP_ATTRIBUTE_INSERT_FIELD = "insert_field";
	public static final String STEP_ATTRIBUTE_UPDATE_FIELD = "update_field";
	public static final String STEP_ATTRIBUTE_DELETE_FIELD = "delete_field";
	public static final String STEP_ATTRIBUTE_READ_FIELD = "read_field";
	
//	long id_connection    = rep.getStepAttributeInteger(id_step, "id_connection");  //$NON-NLS-1$
//	commitSize     		= (int)rep.getStepAttributeInteger(id_step, "commit");
//	databaseMeta          = DatabaseMeta.findDatabase( databases, id_connection);
//    sqlField              = rep.getStepAttributeString (id_step, "sql_field"); //$NON-NLS-1$
//
//    insertField           = rep.getStepAttributeString (id_step, "insert_field"); //$NON-NLS-1$
//    updateField           = rep.getStepAttributeString (id_step, "update_field"); //$NON-NLS-1$
//    deleteField           = rep.getStepAttributeString (id_step, "delete_field"); //$NON-NLS-1$
//    readField             = rep.getStepAttributeString (id_step, "read_field"); //$NON-NLS-1$
	private DatabaseMeta databaseMeta;
	private String       sqlField;

    private String       updateField;
    private String       insertField;
    private String       deleteField;
    private String       readField;
    
	/** Commit size for inserts/updates */
	private int    commitSize; 
	
	public ExecSQLRowMeta()
	{
		super();
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
     * @return Returns the sqlField.
     */
    public String getSqlFieldName()
    {
        return sqlField;
    }

    /**
     * @param sql The sqlField to sqlField.
     */
    public void setSqlFieldName(String sqlField)
    {
        this.sqlField = sqlField;
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
     * @return Returns the deleteField.
     */
    public String getDeleteField()
    {
        return deleteField;
    }

    /**
     * @param deleteField The deleteField to set.
     */
    public void setDeleteField(String deleteField)
    {
        this.deleteField = deleteField;
    }

    /**
     * @return Returns the insertField.
     */
    public String getInsertField()
    {
        return insertField;
    }

    /**
     * @param insertField The insertField to set.
     */
    public void setInsertField(String insertField)
    {
        this.insertField = insertField;
    }

    /**
     * @return Returns the readField.
     */
    public String getReadField()
    {
        return readField;
    }

    /**
     * @param readField The readField to set.
     */
    public void setReadField(String readField)
    {
        this.readField = readField;
    }

    /**
     * @return Returns the updateField.
     */
    public String getUpdateField()
    {
        return updateField;
    }

    /**
     * @param updateField The updateField to set.
     */
    public void setUpdateField(String updateField)
    {
        this.updateField = updateField;
    }

    public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException
	{
		readData(stepnode, databases);
	}

	public Object clone()
	{
		ExecSQLRowMeta retval = (ExecSQLRowMeta)super.clone();
		return retval;
	}
	
	private void readData(Node stepnode, List<? extends SharedObjectInterface> databases) throws KettleXMLException
	{
		try
		{
			String csize;
			String con            = XMLHandler.getTagValue(stepnode, "connection"); //$NON-NLS-1$
			databaseMeta          = DatabaseMeta.findDatabase(databases, con);
			csize      = XMLHandler.getTagValue(stepnode, "commit"); //$NON-NLS-1$
			commitSize=Const.toInt(csize, 0);
            sqlField                   = XMLHandler.getTagValue(stepnode, "sql_field"); //$NON-NLS-1$

			insertField           = XMLHandler.getTagValue(stepnode, "insert_field"); //$NON-NLS-1$
			updateField           = XMLHandler.getTagValue(stepnode, "update_field"); //$NON-NLS-1$			
			deleteField           = XMLHandler.getTagValue(stepnode, "delete_field"); //$NON-NLS-1$
			readField             = XMLHandler.getTagValue(stepnode, "read_field"); //$NON-NLS-1$
        }
		catch(Exception e)
		{
			throw new KettleXMLException(Messages.getString("ExecSQLRowMeta.Exception.UnableToLoadStepInfoFromXML"), e); //$NON-NLS-1$
		}
	}

	public void setDefault()
	{
		commitSize   = 1;
		databaseMeta = null;
		sqlField     = null; //$NON-NLS-1$
	}

	public void getFields(RowMetaInterface r, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException
	{
		RowMetaAndData add = ExecSQL.getResultRow(new Result(), getUpdateField(), getInsertField(), getDeleteField(),
				getReadField());
		
		r.mergeRowMeta(add.getRowMeta());
	}

	public String getXML()
	{
        StringBuffer retval = new StringBuffer(300);
        retval.append("    ").append(XMLHandler.addTagValue("commit", commitSize));
		retval.append("    ").append(XMLHandler.addTagValue("connection", databaseMeta==null?"":databaseMeta.getName())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		retval.append("    ").append(XMLHandler.addTagValue("sql_field",        sqlField)); //$NON-NLS-1$ //$NON-NLS-2$
        
        retval.append("    ").append(XMLHandler.addTagValue("insert_field",  insertField)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    ").append(XMLHandler.addTagValue("update_field",  updateField)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    ").append(XMLHandler.addTagValue("delete_field",  deleteField)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    ").append(XMLHandler.addTagValue("read_field",    readField)); //$NON-NLS-1$ //$NON-NLS-2$

		return retval.toString();
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		databaseMeta  = DatabaseMeta.findDatabase(
				(List<DatabaseMeta>) databases, BaseStepMeta.parameterToLong(p
						.get(id + ".connectionId")));
		commitSize = BaseStepMeta.parameterToInt(p.get(id + ".commitSize"));
		sqlField = BaseStepMeta.parameterToString(p.get(id + ".sqlField"));
		insertField = BaseStepMeta.parameterToString(p.get(id + ".insertField"));
		updateField = BaseStepMeta.parameterToString(p.get(id + ".updateField"));
		deleteField = BaseStepMeta.parameterToString(p.get(id + ".deleteField"));
		readField = BaseStepMeta.parameterToString(p.get(id + ".readField"));
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleException
	{
		try
		{
			long id_connection    = rep.getStepAttributeInteger(id_step, "id_connection");  //$NON-NLS-1$
			commitSize     		= (int)rep.getStepAttributeInteger(id_step, "commit");
			databaseMeta          = DatabaseMeta.findDatabase( databases, id_connection);
            sqlField              = rep.getStepAttributeString (id_step, "sql_field"); //$NON-NLS-1$

            insertField           = rep.getStepAttributeString (id_step, "insert_field"); //$NON-NLS-1$
            updateField           = rep.getStepAttributeString (id_step, "update_field"); //$NON-NLS-1$
            deleteField           = rep.getStepAttributeString (id_step, "delete_field"); //$NON-NLS-1$
            readField             = rep.getStepAttributeString (id_step, "read_field"); //$NON-NLS-1$
           
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("ExecSQLRowMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
		}
	}
	
	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, "id_connection",   databaseMeta==null?-1:databaseMeta.getID()); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "commit",        commitSize);
			rep.saveStepAttribute(id_transformation, id_step, "sql_field",             sqlField); //$NON-NLS-1$
 
            rep.saveStepAttribute(id_transformation, id_step, "insert_field", insertField); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "update_field", updateField); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "delete_field", deleteField); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "read_field",   readField); //$NON-NLS-1$
            
			// Also, save the step-database relationship!
			if (databaseMeta!=null) rep.insertStepDatabase(id_transformation, id_step, databaseMeta.getID());
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("ExecSQLRowMeta.Exception.UnableToSaveStepInfo")+id_step, e); //$NON-NLS-1$
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
		if (databaseMeta!=null)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("ExecSQLRowMeta.CheckResult.ConnectionExists"), stepMeta); //$NON-NLS-1$
			remarks.add(cr);

			Database db = new Database(databaseMeta);
            databases = new Database[] { db }; // keep track of it for cancelling purposes...

			try
			{
				db.connect();
				cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("ExecSQLRowMeta.CheckResult.DBConnectionOK"), stepMeta); //$NON-NLS-1$
				remarks.add(cr);

				if (sqlField!=null && sqlField.length()!=0)
					cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("ExecSQLRowMeta.CheckResult.SQLFieldNameEntered"), stepMeta); //$NON-NLS-1$
				else
					cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("ExecSQLRowMeta.CheckResult.SQLFieldNameMissing"), stepMeta); //$NON-NLS-1$
					remarks.add(cr);
			}
			catch(KettleException e)
			{
				cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("ExecSQLRowMeta.CheckResult.ErrorOccurred")+e.getMessage(), stepMeta); //$NON-NLS-1$
				remarks.add(cr);
			}
			finally
			{
				db.disconnect();
			}
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("ExecSQLRowMeta.CheckResult.ConnectionNeeded"), stepMeta); //$NON-NLS-1$
			remarks.add(cr);
		}

            if (input.length>0)
            {
                cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("ExecSQLRowMeta.CheckResult.StepReceivingInfoOK"), stepMeta); //$NON-NLS-1$
                remarks.add(cr);
            }
            else
            {
                cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("ExecSQLRowMeta.CheckResult.NoInputReceivedError"), stepMeta); //$NON-NLS-1$
                remarks.add(cr);
            }
      
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new ExecSQLRow(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData()
	{
		return new ExecSQLRowData();
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
    
    public boolean supportsErrorHandling()
    {
        return true;
    }
   
}

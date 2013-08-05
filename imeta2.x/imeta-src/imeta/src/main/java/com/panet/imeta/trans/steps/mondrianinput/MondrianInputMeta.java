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


package com.panet.imeta.trans.steps.mondrianinput;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
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
 * Created on 2-jun-2003
 *
 */
public class MondrianInputMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_ID_CONNECTION = "id_connection";
	public static final String STEP_ATTRIBUTE_SQL = "sql";
	public static final String STEP_ATTRIBUTE_CATALOG = "catalog";
	public static final String STEP_ATTRIBUTE_VARIABLES_ACTIVE = "variables_active";
	
	private DatabaseMeta databaseMeta;
	private String sql;
	private String catalog;
    private boolean variableReplacementActive;

	public MondrianInputMeta()
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
     * @return Returns the variableReplacementActive.
     */
    public boolean isVariableReplacementActive()
    {
        return variableReplacementActive;
    }

    /**
     * @param variableReplacementActive The variableReplacementActive to set.
     */
    public void setVariableReplacementActive(boolean variableReplacementActive)
    {
        this.variableReplacementActive = variableReplacementActive;
    }

	
	/**
	 * @return Returns the sql.
	 */
	public String getSQL()
	{
		return sql;
	}
	
	/**
	 * @param sql The sql to set.
	 */
	public void setSQL(String sql)
	{
		this.sql = sql;
	}
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode, databases);
	}

	public Object clone()
	{
		MondrianInputMeta retval = (MondrianInputMeta)super.clone();
		return retval;
	}
	
	public void setInfo(Map<String, String[]> p, String id,
 			List<? extends SharedObjectInterface> databases) {
		databaseMeta = DatabaseMeta.findDatabase( (List<DatabaseMeta>) databases, BaseStepMeta.parameterToLong(p
				.get(id + ".connection")));
		sql = BaseStepMeta.parameterToString(p.get(id + ".sql"));
		catalog = BaseStepMeta.parameterToString(p.get(id + ".catalog"));
        variableReplacementActive = BaseStepMeta.parameterToBoolean(p.get(id + ".variableReplacementActive"));
    }
	
	private void readData(Node stepnode, List<? extends SharedObjectInterface> databases)
		throws KettleXMLException
	{
		try
		{
			databaseMeta              = DatabaseMeta.findDatabase(databases, XMLHandler.getTagValue(stepnode, "connection"));
			sql                       = XMLHandler.getTagValue(stepnode, "sql");
			catalog                   = XMLHandler.getTagValue(stepnode, "catalog");
            variableReplacementActive = "Y".equals(XMLHandler.getTagValue(stepnode, "variables_active"));
		}
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void setDefault()
	{
		databaseMeta = null;
		sql        =  "select\n" +
				      " {([Gender].[F], [Measures].[Unit Sales]),\n" +
				      "  ([Gender].[M], [Measures].[Store Sales]),\n" +
				      "  ([Gender].[F], [Measures].[Unit Sales])} on columns,\n" +
				      " CrossJoin([Marital Status].Members,\n" +
				      "           [Product].Children) on rows\n" +
				      "from [Sales]";
		variableReplacementActive=false;
	}
    
    public void getFields(RowMetaInterface row, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException 
    {
		if (databaseMeta==null) return; // TODO: throw an exception here

		RowMetaInterface add=null;
		
		try
		{
			MondrianHelper helper = new MondrianHelper(databaseMeta, catalog, sql);
			add = helper.getCachedRowMeta();
			if (add==null) {
				helper.openQuery();
				helper.createRectangularOutput();
				add = helper.getOutputRowMeta();
			}
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleStepException("Unable to get query result for MDX query: "+Const.CR+sql, dbe);
		}

		// Set the origin
		//
		for (int i=0;i<add.size();i++)
		{
			ValueMetaInterface v=add.getValueMeta(i);
			v.setOrigin(origin);
		}
		
		row.addRowMeta( add );
	}

	public String getXML()
	{
        StringBuffer retval = new StringBuffer();
		
		retval.append("    "+XMLHandler.addTagValue("connection", databaseMeta==null?"":databaseMeta.getName()));
		retval.append("    "+XMLHandler.addTagValue("sql",        sql));
		retval.append("    "+XMLHandler.addTagValue("catalog",    catalog));
        retval.append("    "+XMLHandler.addTagValue("variables_active",   variableReplacementActive));
        
		return retval.toString();
	}

	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleException
	{
		try
		{
			long id_connection = rep.getStepAttributeInteger(id_step, STEP_ATTRIBUTE_ID_CONNECTION); 
			databaseMeta = DatabaseMeta.findDatabase( databases, id_connection);
			
			sql                       =      rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_SQL);
			catalog                   =      rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CATALOG);
            variableReplacementActive =      rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_VARIABLES_ACTIVE);
		}
		catch(Exception e)
		{
			throw new KettleException("Unexpected error reading step information from the repository", e);
		}
	}
	
	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, "id_connection",    databaseMeta==null?-1:databaseMeta.getID());
			rep.saveStepAttribute(id_transformation, id_step, "sql",              sql);
			rep.saveStepAttribute(id_transformation, id_step, "limit",            catalog);
            rep.saveStepAttribute(id_transformation, id_step, "variables_active", variableReplacementActive);
			
			// Also, save the step-database relationship!
			if (databaseMeta!=null) rep.insertStepDatabase(id_transformation, id_step, databaseMeta.getID());
		}
		catch(Exception e)
		{
			throw new KettleException("Unable to save step information to the repository for id_step="+id_step, e);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
		if (databaseMeta!=null)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, "Connection exists", stepMeta);
			remarks.add(cr);

			//TODO: perform lookup to see if it all works fine.
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, "Please select or create a connection to use", stepMeta);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new MondrianInput(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData()
	{
		return new MondrianData();
	}

	public void analyseImpact(List<DatabaseImpact> impact, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info) throws KettleStepException
	{
		// you can't really analyze the database impact since it runs on a Mondrian server
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
	 * @return the catalog
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * @param catalog the catalog to set
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
}

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

package com.panet.imeta.trans.steps.getvariable;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
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

/*
 * Created on 05-aug-2003
 */
public class GetVariableMeta extends BaseStepMeta implements StepMetaInterface
{
	private String fieldName[];
	private String variableString[];
	
	public GetVariableMeta()
	{
		super(); // allocate BaseStepMeta
	}
	
	/**
     * @return Returns the fieldName.
     */
    public String[] getFieldName()
    {
        return fieldName;
    }
    
    /**
     * @param fieldName The fieldName to set.
     */
    public void setFieldName(String[] fieldName)
    {
        this.fieldName = fieldName;
    }
    
    /**
     * @return Returns the strings containing variables.
     */
    public String[] getVariableString()
    {
        return variableString;
    }
    
    /**
     * @param variableString The variable strings to set.
     */
    public void setVariableString(String[] variableString)
    {
        this.variableString = variableString;
    }
    
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode);
	}

	public void allocate(int count)
	{
		fieldName = new String[count];
		variableString = new String[count];
	}

	public Object clone()
	{
		GetVariableMeta retval = (GetVariableMeta)super.clone();

		int count=fieldName.length;
		
		retval.allocate(count);
				
		for (int i=0;i<count;i++)
		{
			retval.fieldName[i] = fieldName[i];
			retval.variableString[i] = variableString[i];
		}
		
		return retval;
	}
	
	private void readData(Node stepnode)
		throws KettleXMLException
	{
		try
		{
			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int count= XMLHandler.countNodes(fields, "field");

			allocate(count);
					
			for (int i=0;i<count;i++)
			{
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);
				
				fieldName[i] = XMLHandler.getTagValue(fnode, "name");
                variableString[i] = XMLHandler.getTagValue(fnode, "variable");
			}
		}
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to read step information from XML", e);
		}
	}

	public void setDefault()
	{
		int count=0;
		
		allocate(count);

		for (int i=0;i<count;i++)
		{
			fieldName[i] = "field"+i;
			variableString[i] = "";
		}
	}

	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface info[], StepMeta nextStep, VariableSpace space) throws KettleStepException
	{
        // Determine the maximum length...
        // 
        int length = -1;
		for (int i=0;i<fieldName.length;i++)
		{
            if (variableString[i]!=null)
            {
                String string = space.environmentSubstitute(variableString[i]);
                if (string.length()>length) length=string.length();
            }
		}
        
		RowMetaInterface row=new RowMeta();
		for (int i=0;i<fieldName.length;i++)
		{
			ValueMetaInterface v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_STRING);
            v.setLength(length);
            v.setOrigin(name);
            row.addValueMeta(v);
		}

        inputRowMeta.mergeRowMeta(row);
    }

	public String getXML()
	{
        StringBuffer retval = new StringBuffer(300);

		retval.append("    <fields>").append(Const.CR);
		
		for (int i=0;i<fieldName.length;i++)
		{
			retval.append("      <field>").append(Const.CR);
			retval.append("        ").append(XMLHandler.addTagValue("name", fieldName[i]));
			retval.append("        ").append(XMLHandler.addTagValue("variable", variableString[i]));
			retval.append("      </field>").append(Const.CR);
		}
		retval.append("    </fields>").append(Const.CR);

		return retval.toString();
	}
	
	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		String[] fieldName = p.get(id + "_fields.fieldName");
    	String[] variableString = p.get(id + "_fields.variableString");

		this.fieldName = (fieldName != null) ? fieldName : (new String[0]);
		this.variableString = (variableString != null) ? variableString : (new String[0]);
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleException
	{
		try
		{
			int nrfields = rep.countNrStepAttributes(id_step, "field_name");
			
			allocate(nrfields);
	
			for (int i=0;i<nrfields;i++)
			{
				fieldName[i] = rep.getStepAttributeString(id_step, i, "field_name");
				variableString[i] = rep.getStepAttributeString(id_step, i, "field_variable");
			}
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
			for (int i=0;i<fieldName.length;i++)
			{
				rep.saveStepAttribute(id_transformation, id_step, i, "field_name",      fieldName[i]);
				rep.saveStepAttribute(id_transformation, id_step, i, "field_variable",  variableString[i]);
			}
		}
		catch(Exception e)
		{
			throw new KettleException("Unable to save step information to the repository for id_step="+id_step, e);
		}

	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		// See if we have input streams leading to this step!
		int nrRemarks = remarks.size();
		for (int i=0;i<fieldName.length;i++)
		{
			if (Const.isEmpty(variableString[i]))
			{
				CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("GetVariableMeta.CheckResult.VariableNotSpecified", fieldName[i]), stepMeta);
				remarks.add(cr);
			}
		}
		if (remarks.size()==nrRemarks)
		{
			CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("GetVariableMeta.CheckResult.AllVariablesSpecified"), stepMeta);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new GetVariable(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData()
	{
		return new GetVariableData();
	}
}

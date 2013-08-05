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

package com.panet.imeta.trans.steps.systemdata;

import java.text.DecimalFormat;
import java.util.HashMap;
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
 *
 */

public class SystemDataMeta extends BaseStepMeta implements StepMetaInterface
{
    public final static int TYPE_SYSTEM_INFO_NONE             =  0;
    public final static int TYPE_SYSTEM_INFO_SYSTEM_DATE      =  1;
    public final static int TYPE_SYSTEM_INFO_SYSTEM_START     =  2;
    public final static int TYPE_SYSTEM_INFO_TRANS_DATE_FROM  =  3;
    public final static int TYPE_SYSTEM_INFO_TRANS_DATE_TO    =  4;
    public final static int TYPE_SYSTEM_INFO_JOB_DATE_FROM    =  5;
    public final static int TYPE_SYSTEM_INFO_JOB_DATE_TO      =  6;
    public final static int TYPE_SYSTEM_INFO_PREV_DAY_START   =  7;
    public final static int TYPE_SYSTEM_INFO_PREV_DAY_END     =  8;
    public final static int TYPE_SYSTEM_INFO_THIS_DAY_START   =  9;
    public final static int TYPE_SYSTEM_INFO_THIS_DAY_END     = 10;
    public final static int TYPE_SYSTEM_INFO_NEXT_DAY_START   = 11;
    public final static int TYPE_SYSTEM_INFO_NEXT_DAY_END     = 12;
    public final static int TYPE_SYSTEM_INFO_PREV_MONTH_START = 13;
    public final static int TYPE_SYSTEM_INFO_PREV_MONTH_END   = 14;
    public final static int TYPE_SYSTEM_INFO_THIS_MONTH_START = 15;
    public final static int TYPE_SYSTEM_INFO_THIS_MONTH_END   = 16;
    public final static int TYPE_SYSTEM_INFO_NEXT_MONTH_START = 17;
    public final static int TYPE_SYSTEM_INFO_NEXT_MONTH_END   = 18;
    public final static int TYPE_SYSTEM_INFO_COPYNR           = 19;
    public final static int TYPE_SYSTEM_INFO_TRANS_NAME       = 20;
    public final static int TYPE_SYSTEM_INFO_FILENAME         = 21;
    public final static int TYPE_SYSTEM_INFO_MODIFIED_USER    = 22;
    public final static int TYPE_SYSTEM_INFO_MODIFIED_DATE    = 23;
    public final static int TYPE_SYSTEM_INFO_TRANS_BATCH_ID   = 24;
    public final static int TYPE_SYSTEM_INFO_JOB_BATCH_ID     = 25;
    public final static int TYPE_SYSTEM_INFO_HOSTNAME         = 26;
    public final static int TYPE_SYSTEM_INFO_IP_ADDRESS       = 27;
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_01      = 28; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_02      = 29; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_03      = 30; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_04      = 31; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_05      = 32; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_06      = 33; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_07      = 34; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_08      = 35; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_09      = 36; 
    public final static int TYPE_SYSTEM_INFO_ARGUMENT_10      = 37; 
    
    public final static int TYPE_SYSTEM_INFO_IMETA_VERSION        = 38; 
    public final static int TYPE_SYSTEM_INFO_IMETA_BUILD_VERSION  = 39; 
    public final static int TYPE_SYSTEM_INFO_IMETA_BUILD_DATE     = 40; 
    public final static int TYPE_SYSTEM_INFO_CURRENT_PID     = 41; 

    public static final SystemDataMetaFunction functions[] = new SystemDataMetaFunction[] {
            null,
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_SYSTEM_DATE      , "system date (variable)", Messages.getString("SystemDataMeta.TypeDesc.SystemDateVariable")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_SYSTEM_START     , "system date (fixed)", Messages.getString("SystemDataMeta.TypeDesc.SystemDateFixed")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_TRANS_DATE_FROM  , "start date range", Messages.getString("SystemDataMeta.TypeDesc.StartDateRange")),
        	new SystemDataMetaFunction(TYPE_SYSTEM_INFO_TRANS_DATE_TO    , "end date range", Messages.getString("SystemDataMeta.TypeDesc.EndDateRange")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_JOB_DATE_FROM    , "job start date range",    Messages.getString("SystemDataMeta.TypeDesc.JobStartDateRange")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_JOB_DATE_TO      , "job end date range",    Messages.getString("SystemDataMeta.TypeDesc.JobEndDateRange")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_PREV_DAY_START   , "yesterday start",    Messages.getString("SystemDataMeta.TypeDesc.YesterdayStart")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_PREV_DAY_END     , "yesterday end",    Messages.getString("SystemDataMeta.TypeDesc.YesterdayEnd")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_THIS_DAY_START   , "today start",    Messages.getString("SystemDataMeta.TypeDesc.TodayStart")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_THIS_DAY_END     , "today end",    Messages.getString("SystemDataMeta.TypeDesc.TodayEnd")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_NEXT_DAY_START   , "tomorrow start",    Messages.getString("SystemDataMeta.TypeDesc.TomorrowStart")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_NEXT_DAY_END     , "tomorrow end",    Messages.getString("SystemDataMeta.TypeDesc.TomorrowEnd")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_PREV_MONTH_START , "last month start",    Messages.getString("SystemDataMeta.TypeDesc.LastMonthStart")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_PREV_MONTH_END   , "last month end",    Messages.getString("SystemDataMeta.TypeDesc.LastMonthEnd")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_THIS_MONTH_START , "this month start",    Messages.getString("SystemDataMeta.TypeDesc.ThisMonthStart")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_THIS_MONTH_END   , "this month end",    Messages.getString("SystemDataMeta.TypeDesc.ThisMonthEnd")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_NEXT_MONTH_START , "next month start",    Messages.getString("SystemDataMeta.TypeDesc.NextMonthStart")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_NEXT_MONTH_END   , "next month end",    Messages.getString("SystemDataMeta.TypeDesc.NextMonthEnd")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_COPYNR           , "copy of step",    Messages.getString("SystemDataMeta.TypeDesc.CopyOfStep")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_TRANS_NAME       , "transformation name",    Messages.getString("SystemDataMeta.TypeDesc.TransformationName")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_FILENAME         , "transformation file name",    Messages.getString("SystemDataMeta.TypeDesc.TransformationFileName")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_MODIFIED_USER    , "User modified",    Messages.getString("SystemDataMeta.TypeDesc.UserModified")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_MODIFIED_DATE    , "Date modified",    Messages.getString("SystemDataMeta.TypeDesc.DateModified")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_TRANS_BATCH_ID   , "batch ID",    Messages.getString("SystemDataMeta.TypeDesc.BatchID")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_JOB_BATCH_ID     , "job batch ID",    Messages.getString("SystemDataMeta.TypeDesc.JobBatchID")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_HOSTNAME         , "Hostname",    Messages.getString("SystemDataMeta.TypeDesc.Hostname")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_IP_ADDRESS       , "IP address",    Messages.getString("SystemDataMeta.TypeDesc.IPAddress")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_01      , "command line argument 1",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument1")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_02      , "command line argument 2",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument2")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_03      , "command line argument 3",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument3")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_04      , "command line argument 4",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument4")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_05      , "command line argument 5",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument5")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_06      , "command line argument 6",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument6")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_07      , "command line argument 7",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument7")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_08      , "command line argument 8",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument8")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_09      , "command line argument 9",    Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument9")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_ARGUMENT_10      , "command line argument 10",   Messages.getString("SystemDataMeta.TypeDesc.CommandLineArgument10")),

            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_IMETA_VERSION       , "imeta version",       Messages.getString("SystemDataMeta.TypeDesc.ImetaVersion")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_IMETA_BUILD_VERSION , "imeta build version", Messages.getString("SystemDataMeta.TypeDesc.ImetaBuildVersion")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_IMETA_BUILD_DATE    , "imeta build date",    Messages.getString("SystemDataMeta.TypeDesc.ImetaBuildDate")),
            new SystemDataMetaFunction(TYPE_SYSTEM_INFO_CURRENT_PID    , "Current PID",    Messages.getString("SystemDataMeta.TypeDesc.CurrentPID")),
};
    
    public static final String[] fieldTypeDesc = new String[] { "",
		functions[1].getDescription(), functions[2].getDescription(),
		functions[3].getDescription(), functions[4].getDescription(),
		functions[5].getDescription(), functions[6].getDescription(),
		functions[7].getDescription(), functions[8].getDescription(),
		functions[9].getDescription(), functions[10].getDescription(),
		functions[11].getDescription(), functions[12].getDescription(),
		functions[13].getDescription(), functions[14].getDescription(),
		functions[15].getDescription(), functions[16].getDescription(),
		functions[17].getDescription(), functions[18].getDescription(),
		functions[19].getDescription(), functions[20].getDescription(),
		functions[21].getDescription(), functions[22].getDescription(),
		functions[23].getDescription(), functions[24].getDescription(),
		functions[25].getDescription(), functions[26].getDescription(),
		functions[27].getDescription(), functions[28].getDescription(),
		functions[29].getDescription(), functions[30].getDescription(),
		functions[31].getDescription(), functions[32].getDescription(),
		functions[33].getDescription(), functions[34].getDescription(),
		functions[35].getDescription(), functions[36].getDescription(),
		functions[37].getDescription(), functions[38].getDescription(),
		functions[39].getDescription(), functions[40].getDescription(),
		functions[41].getDescription() };
    
	private String fieldName[];
	private int    fieldType[];
	
	public SystemDataMeta()
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
     * @return Returns the fieldType.
     */
    public int[] getFieldType()
    {
        return fieldType;
    }
    
    /**
     * @param fieldType The fieldType to set.
     */
    public void setFieldType(int[] fieldType)
    {
        this.fieldType = fieldType;
    }
    
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode);
	}

	public void allocate(int count)
	{
		fieldName = new String[count];
		fieldType = new int   [count];
	}

	public Object clone()
	{
		SystemDataMeta retval = (SystemDataMeta)super.clone();

		int count=fieldName.length;
		
		retval.allocate(count);
				
		for (int i=0;i<count;i++)
		{
			retval.fieldName[i] = fieldName[i];
			retval.fieldType[i] = fieldType[i];
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
			String type;
			
			allocate(count);
					
			for (int i=0;i<count;i++)
			{
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);
				
				fieldName[i] = XMLHandler.getTagValue(fnode, "name");
				type         = XMLHandler.getTagValue(fnode, "type");
				fieldType[i] = getType(type);
			}
		}
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to read step information from XML", e);
		}
	}
	
	public static final int getType(String type)
	{
		for (int i=1;i<functions.length;i++)
		{
			if (functions[i].getCode().equalsIgnoreCase(type)) return i;
			if (functions[i].getDescription().equalsIgnoreCase(type)) return i;
		}
		return 0;
	}
	
	public static final String getTypeDesc(int t)
	{
        if (functions == null || functions.length == 0) return null;
		if (t<0 || t>=functions.length || functions[t] == null) return null;
		return functions[t].getDescription();
	}

	public void setDefault()
	{
		int count=0;
		
		allocate(count);

		for (int i=0;i<count;i++)
		{
			fieldName[i] = "field"+i;
			fieldType[i] = TYPE_SYSTEM_INFO_SYSTEM_DATE;
		}
	}

	public void getFields(RowMetaInterface row, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException
	{
		for (int i=0;i<fieldName.length;i++)
		{
			ValueMetaInterface v;

			switch(fieldType[i])
			{
			case TYPE_SYSTEM_INFO_SYSTEM_START:      // All date values...
			case TYPE_SYSTEM_INFO_SYSTEM_DATE:  
			case TYPE_SYSTEM_INFO_TRANS_DATE_FROM: 
			case TYPE_SYSTEM_INFO_TRANS_DATE_TO: 
            case TYPE_SYSTEM_INFO_JOB_DATE_FROM: 
            case TYPE_SYSTEM_INFO_JOB_DATE_TO: 
			case TYPE_SYSTEM_INFO_PREV_DAY_START: 
			case TYPE_SYSTEM_INFO_PREV_DAY_END: 
			case TYPE_SYSTEM_INFO_THIS_DAY_START: 
			case TYPE_SYSTEM_INFO_THIS_DAY_END: 
			case TYPE_SYSTEM_INFO_NEXT_DAY_START: 
			case TYPE_SYSTEM_INFO_NEXT_DAY_END: 
			case TYPE_SYSTEM_INFO_PREV_MONTH_START: 
			case TYPE_SYSTEM_INFO_PREV_MONTH_END: 
			case TYPE_SYSTEM_INFO_THIS_MONTH_START: 
			case TYPE_SYSTEM_INFO_THIS_MONTH_END: 
			case TYPE_SYSTEM_INFO_NEXT_MONTH_START: 
			case TYPE_SYSTEM_INFO_NEXT_MONTH_END: 
			case TYPE_SYSTEM_INFO_MODIFIED_DATE:
            case TYPE_SYSTEM_INFO_IMETA_BUILD_DATE:
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_DATE); 
				break;
				
			case TYPE_SYSTEM_INFO_COPYNR:
			case TYPE_SYSTEM_INFO_TRANS_BATCH_ID:
            case TYPE_SYSTEM_INFO_JOB_BATCH_ID:
            case TYPE_SYSTEM_INFO_IMETA_BUILD_VERSION:
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_INTEGER);
				v.setLength(ValueMetaInterface.DEFAULT_INTEGER_LENGTH, 0);
				break;
				
			case TYPE_SYSTEM_INFO_TRANS_NAME :
			case TYPE_SYSTEM_INFO_FILENAME   : 
			case TYPE_SYSTEM_INFO_ARGUMENT_01: 
			case TYPE_SYSTEM_INFO_ARGUMENT_02: 
			case TYPE_SYSTEM_INFO_ARGUMENT_03: 
			case TYPE_SYSTEM_INFO_ARGUMENT_04: 
			case TYPE_SYSTEM_INFO_ARGUMENT_05: 
			case TYPE_SYSTEM_INFO_ARGUMENT_06: 
			case TYPE_SYSTEM_INFO_ARGUMENT_07: 
			case TYPE_SYSTEM_INFO_ARGUMENT_08: 
			case TYPE_SYSTEM_INFO_ARGUMENT_09: 
			case TYPE_SYSTEM_INFO_ARGUMENT_10: 
			case TYPE_SYSTEM_INFO_MODIFIED_USER:
			case TYPE_SYSTEM_INFO_HOSTNAME:
			case TYPE_SYSTEM_INFO_IP_ADDRESS:
            case TYPE_SYSTEM_INFO_IMETA_VERSION:
				v=new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_STRING);
				break;
            case TYPE_SYSTEM_INFO_CURRENT_PID:
				v=new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_INTEGER);
				v.setLength(ValueMetaInterface.DEFAULT_INTEGER_LENGTH, 0);
				break;
			default: 
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_NONE);
				break;
			}
			v.setOrigin(name);
			row.addValueMeta(v);
		}
	}

	public String getXML()
	{
        StringBuffer retval = new StringBuffer();

		retval.append("    <fields>"+Const.CR);
		
		for (int i=0;i<fieldName.length;i++)
		{
			retval.append("      <field>"+Const.CR);
			retval.append("        "+XMLHandler.addTagValue("name", fieldName[i]));
			retval.append("        "+XMLHandler.addTagValue("type", functions[fieldType[i]]!=null ? functions[fieldType[i]].getCode() : "") );
			retval.append("        </field>"+Const.CR);
		}
		retval.append("      </fields>"+Const.CR);

		return retval.toString();
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		String[] fieldName = p.get(id + "_fields.fieldName");
		String[] fieldType = p.get(id + "_fields.fieldType");

		this.fieldName = (fieldName != null) ? fieldName : (new String[0]);
		this.fieldType = stringArrayToIntArray(fieldType,0);
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException
	{
		try
		{
			int nrfields = rep.countNrStepAttributes(id_step, "field_name");
			
			allocate(nrfields);
	
			for (int i=0;i<nrfields;i++)
			{
				fieldName[i] =          rep.getStepAttributeString(id_step, i, "field_name");
				fieldType[i] = getType( rep.getStepAttributeString(id_step, i, "field_type"));
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
			for (int i=0;i<fieldName.length;i++)
			{
				rep.saveStepAttribute(id_transformation, id_step, i, "field_name",      fieldName[i]);
				rep.saveStepAttribute(id_transformation, id_step, i, "field_type",      functions[fieldType[i]]!=null ? functions[fieldType[i]].getCode() : "");
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
			if (fieldType[i]<=TYPE_SYSTEM_INFO_NONE)
			{
				CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("SystemDataMeta.CheckResult.FieldHasNoType", fieldName[i]), stepMeta);
				remarks.add(cr);
			}
		}
		if (remarks.size()==nrRemarks)
		{
			CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("SystemDataMeta.CheckResult.AllTypesSpecified"), stepMeta);
			remarks.add(cr);
		}
	}
    
    /**
     * Default a step doesn't use any arguments.
     * Implement this to notify the GUI that a window has to be displayed BEFORE launching a transformation.
     * You can also use this to specify certain Environment variable values.
     * 
     * @return A row of argument values. (name and optionally a default value)
     *         Put up to 10 values in the row for the possible 10 arguments.
     *         The name of the value is "1" through "10" for the 10 possible arguments.
     */
    public Map<String, String> getUsedArguments()
    {
    	Map<String, String> stepArgs = new HashMap<String, String>();
    	DecimalFormat df = new DecimalFormat("00");
    	
        for (int argNr=0;argNr<10;argNr++)
        {
            boolean found = false;
            for (int i=0;i<fieldName.length;i++)
            {
                if (fieldType[i]==TYPE_SYSTEM_INFO_ARGUMENT_01+argNr) found=true;
            }
            if (found)
            {
            	stepArgs.put(df.format(argNr+1), "");
            }
        }
        
        return stepArgs;
    }

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new SystemData(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData()
	{
		return new SystemDataData();
	}

}

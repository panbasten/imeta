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
package com.panet.imeta.trans.steps.samplerows;

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
 * Created on 02-jun-2008
 *
 */

public class SampleRowsMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_LINESRANGE = "linesrange";
	public static final String STEP_ATTRIBUTE_LINENUMFIELD = "linenumfield";
//	linesrange = rep.getStepAttributeString(id_step, "linesrange");
//	linenumfield = rep.getStepAttributeString(id_step, "linenumfield");
	private String linesrange;
	private String linenumfield;
	public static String DEFAULT_RANGE="1";
	
	public SampleRowsMeta()
	{
		super(); // allocate BaseStepMeta
	}

    
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode, databases);
	}
	public Object clone()
	{
		Object retval = super.clone();
		return retval;
	}
	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface info[], StepMeta nextStep, VariableSpace space) throws KettleStepException
	{   
        if (!Const.isEmpty(linenumfield))
        {
        	
			ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(linenumfield), ValueMeta.TYPE_INTEGER);
			v.setLength(ValueMetaInterface.DEFAULT_INTEGER_LENGTH, 0);
			v.setOrigin(name);
			inputRowMeta.addValueMeta(v);
        }
	 }
		
	 private void readData(Node stepnode, List<? extends SharedObjectInterface> databases)
		throws KettleXMLException
		{
		try{
			linesrange = XMLHandler.getTagValue(stepnode, "linesrange");
			linenumfield = XMLHandler.getTagValue(stepnode, "linenumfield");
		}
	    catch (Exception e)
        {
            throw new KettleXMLException(Messages.getString("SampleRowsMeta.Exception.UnableToReadStepInfo"), e);
        }
	}
	public String getLinesRange()
	{
		return this.linesrange;
	}
	public void setLinesRange(String linesrange)
	{
		this.linesrange=linesrange;
	}
	public String getLineNumberField()
	{
		return this.linenumfield;
	}
	public void setLineNumberField(String linenumfield)
	{
		this.linenumfield=linenumfield;
	}
	
	public void setDefault()
	{
		linesrange=DEFAULT_RANGE;
		linenumfield=null;
	}

	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException
	{
		try{
			linesrange = rep.getStepAttributeString(id_step, "linesrange");
			linenumfield = rep.getStepAttributeString(id_step, "linenumfield");
			
		}
		 catch (Exception e)
	     {
	        throw new KettleException(Messages.getString("SampleRowsMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
	     }
	}
	
	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try{
			rep.saveStepAttribute(id_transformation, id_step, "linesrange", linesrange);
			rep.saveStepAttribute(id_transformation, id_step, "linenumfield", linenumfield);
			
			
		}
		catch (Exception e)
        {
            throw new KettleException(Messages.getString("SampleRowsMeta.Exception.UnexpectedErrorSavingStepInfo"), e); //$NON-NLS-1$
        }
	}
	 public String getXML()
	    {
	        StringBuffer retval = new StringBuffer();
	        retval.append("    " + XMLHandler.addTagValue("linesrange", linesrange));
	        retval.append("    " + XMLHandler.addTagValue("linenumfield", linenumfield));
	        
	        return retval.toString();
	    }
	
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
		
		if (Const.isEmpty(linesrange))
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SampleRowsMeta.CheckResult.LinesRangeMissing"), stepinfo);
        else
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK,  Messages.getString("SampleRowsMeta.CheckResult.LinesRangeOk"), stepinfo);
		remarks.add(cr);
		
		if (prev==null || prev.size()==0)
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, Messages.getString("SampleRowsMeta.CheckResult.NotReceivingFields"), stepinfo); //$NON-NLS-1$
		else
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SampleRowsMeta.CheckResult.StepRecevingData",prev.size()+""), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
		remarks.add(cr);
		
		
		// See if we have input streams leading to this step!
		if (input.length>0)
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SampleRowsMeta.CheckResult.StepRecevingData2"), stepinfo); //$NON-NLS-1$
		else
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SampleRowsMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepinfo); //$NON-NLS-1$
		remarks.add(cr);
	}
	

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new SampleRows(stepMeta, stepDataInterface, cnr, tr, trans);
	}
	
	public StepDataInterface getStepData()
	{
		return new SampleRowsData();
	}


	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		linesrange = BaseStepMeta.parameterToString(p.get(id
				+ ".linesrange"));
		linenumfield = BaseStepMeta.parameterToString(p.get(id
				+ ".linenumfield"));
	}

}

 /* Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Samatar Hassan.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.*/
 
package com.panet.imeta.trans.steps.clonerow;

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
 * Created on 27-06-2008
 *
 */

public class CloneRowMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_NRCLONES = "nrclones";
	public static final String STEP_ATTRIBUTE_NRCLONEINFIELD = "nrcloneinfield";
	public static final String STEP_ATTRIBUTE_NRCLONEFIELD = "nrclonefield";
	public static final String STEP_ATTRIBUTE_ADDCLONEFLAG = "addcloneflag";
	public static final String STEP_ATTRIBUTE_CLONEFLAGFIELD = "cloneflagfield";
	
	/** nr of clone rows */
	private String nrclones;
	
	/** Flag: add clone flag */
	
	private boolean addcloneflag;
	
	/** clone flag field*/
	private String cloneflagfield;
	
	private boolean nrcloneinfield;
	
	private String nrclonefield;
	
	public CloneRowMeta()
	{
		super(); // allocate BaseStepMeta
	}
   public String getXML()
    {
        StringBuffer retval = new StringBuffer();
        retval.append("    " + XMLHandler.addTagValue("nrclones", nrclones));
        retval.append("    " + XMLHandler.addTagValue("addcloneflag",   addcloneflag));
        retval.append("    " + XMLHandler.addTagValue("cloneflagfield", cloneflagfield));
        retval.append("    " + XMLHandler.addTagValue("nrcloneinfield",   nrcloneinfield));
        retval.append("    " + XMLHandler.addTagValue("nrclonefield", nrclonefield));
        return retval.toString();
    }
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode);
	}

	public Object clone()
	{
		Object retval = super.clone();
		return retval;
	}
	public String getNrClones()
	{
		return nrclones;
	}
	
	public void setNrClones(String nrclones)
	{
		this.nrclones=nrclones;
	}
	
	public boolean isAddCloneFlag()
	{
		return addcloneflag;
	}
	public void setAddCloneFlag(boolean addcloneflag)
	{
		this.addcloneflag=addcloneflag;
	}

	
	public boolean isNrCloneInField()
	{
		return nrcloneinfield;
	}
	public void setNrCloneInField(boolean nrcloneinfield)
	{
		this.nrcloneinfield=nrcloneinfield;
	}
	public String getNrCloneField()
	{
		return nrclonefield;
	}
	public void setNrCloneField(String nrclonefield)
	{
		this.nrclonefield=nrclonefield;
	}
	public String getCloneFlagField()
	{
		return cloneflagfield;
	}
	public void setCloneFlagField(String cloneflagfield)
	{
		this.cloneflagfield=cloneflagfield;
	}
	
	private void readData(Node stepnode) throws KettleXMLException
	{
		try{
			nrclones = XMLHandler.getTagValue(stepnode, "nrclones");
			addcloneflag = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "addcloneflag"));
			cloneflagfield = XMLHandler.getTagValue(stepnode, "cloneflagfield");
			nrcloneinfield = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "nrcloneinfield"));
			nrclonefield = XMLHandler.getTagValue(stepnode, "nrclonefield");
		}
	    catch (Exception e)
        {
            throw new KettleXMLException(Messages.getString("CloneRowMeta.Exception.UnableToReadStepInfo"), e);
        }
	}

	public void setDefault()
	{
		nrclones="0";
		cloneflagfield=null;
		nrclonefield=null;
		nrcloneinfield=false;
		addcloneflag=false;
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		nrclones = BaseStepMeta.parameterToString(p.get(id + ".nrclones"));
		addcloneflag = BaseStepMeta.parameterToBoolean(p.get(id + ".addcloneflag"));
		cloneflagfield = BaseStepMeta.parameterToString(p.get(id + ".cloneflagfield"));
		nrcloneinfield = BaseStepMeta.parameterToBoolean(p.get(id + ".nrcloneinfield"));
		nrclonefield = BaseStepMeta.parameterToString(p.get(id + ".nrclonefield"));
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleException
	{
		try{
			nrclones = rep.getStepAttributeString(id_step, "nrclones");
			addcloneflag =  rep.getStepAttributeBoolean(id_step, "addcloneflag");
			cloneflagfield = rep.getStepAttributeString(id_step, "cloneflagfield");
			nrcloneinfield =  rep.getStepAttributeBoolean(id_step, "nrcloneinfield");
			nrclonefield = rep.getStepAttributeString(id_step, "nrclonefield");
		}
		 catch (Exception e)
	     {
	        throw new KettleException(Messages.getString("CloneRowMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
	     }
	}
	
	
	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try{
			rep.saveStepAttribute(id_transformation, id_step, "nrclones", nrclones);
			rep.saveStepAttribute(id_transformation, id_step, "addcloneflag",    addcloneflag);
			rep.saveStepAttribute(id_transformation, id_step, "cloneflagfield", cloneflagfield);	
			rep.saveStepAttribute(id_transformation, id_step, "nrcloneinfield",    nrcloneinfield);
			rep.saveStepAttribute(id_transformation, id_step, "nrclonefield", nrclonefield);
		}
		catch (Exception e)
        {
            throw new KettleException(Messages.getString("CloneRowMeta.Exception.UnexpectedErrorSavingStepInfo"), e); //$NON-NLS-1$
        }
	}
	
	public void getFields(RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException
	{
		 // Output field (boolean) ?
		if(addcloneflag)
		{
			 if (!Const.isEmpty(cloneflagfield))
		     {
				 ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(cloneflagfield), ValueMeta.TYPE_BOOLEAN);
				 v.setOrigin(origin);
				 rowMeta.addValueMeta(v);
		     }
		}
	}
	
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		String error_message="";
		
		if (Const.isEmpty(nrclones))
        {
            error_message = Messages.getString("CloneRowMeta.CheckResult.NrClonesdMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
        }
        else
        {
            error_message = Messages.getString("CloneRowMeta.CheckResult.NrClonesOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, error_message, stepinfo);
        }
		remarks.add(cr);
		
		if(addcloneflag)
		{
			if (Const.isEmpty(cloneflagfield))
	        {
	            error_message = Messages.getString("CloneRowMeta.CheckResult.CloneFlagFieldMissing"); //$NON-NLS-1$
	            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepinfo);
	        }
	        else
	        {
	            error_message = Messages.getString("CloneRowMeta.CheckResult.CloneFlagFieldOk"); //$NON-NLS-1$
	            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, error_message, stepinfo);
	        }
			remarks.add(cr);
		}
		if(nrcloneinfield)
		{
			if (Const.isEmpty(nrclonefield))
	        {
	            error_message = Messages.getString("CloneRowMeta.CheckResult.NrCloneFieldMissing"); //$NON-NLS-1$
	            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepinfo);
	        }
	        else
	        {
	            error_message = Messages.getString("CloneRowMeta.CheckResult.NrCloneFieldOk"); //$NON-NLS-1$
	            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepinfo);
	        }
			remarks.add(cr);
		}
	
		if (prev==null || prev.size()==0)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING, Messages.getString("CloneRowMeta.CheckResult.NotReceivingFields"), stepinfo); //$NON-NLS-1$
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("CloneRowMeta.CheckResult.StepRecevingData",prev.size()+""), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
		}
		remarks.add(cr);
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("CloneRowMeta.CheckResult.StepRecevingData2"), stepinfo); //$NON-NLS-1$
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("CloneRowMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepinfo); //$NON-NLS-1$
		}
		remarks.add(cr);
	}
	
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new CloneRow(stepMeta, stepDataInterface, cnr, tr, trans);
	}
	
	public StepDataInterface getStepData()
	{
		return new CloneRowData();
	}


}

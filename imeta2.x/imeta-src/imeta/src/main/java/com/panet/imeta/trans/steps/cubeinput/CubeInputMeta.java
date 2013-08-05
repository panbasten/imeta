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


package com.panet.imeta.trans.steps.cubeinput;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.annotations.Step;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleFileException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.vfs.KettleVFS;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepCategory;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/*
 * Created on 2-jun-2003
 *
 */
@Step(name="CubeInput",image="CIP.png",tooltip="BaseStep.TypeTooltipDesc.Cubeinput",description="BaseStep.TypeLongDesc.CubeInput",
		category=StepCategory.CATEGORY_INPUT)
public class CubeInputMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_FILE_NAME = "filename";
	public static final String STEP_ATTRIBUTE_LIMIT = "limit";
	public static final String STEP_ATTRIBUTE_ADDFILENAMERESULT = "addfilenameresult";
	private String filename;
	private int rowLimit;
	private boolean addfilenameresult;

	public CubeInputMeta()
	{
		super(); // allocate BaseStepMeta
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String,Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode);
	}

	/**
	 * @return Returns the filename.
	 */
	public String getFilename()
	{
		return filename;
	}
	
	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
	/**
	 * @param rowLimit The rowLimit to set.
	 */
	public void setRowLimit(int rowLimit)
	{
		this.rowLimit = rowLimit;
	}
	
	/**
	 * @return Returns the rowLimit.
	 */
	public int getRowLimit()
	{
		return rowLimit;
	}
	
	/**
	 * @return Returns the addfilenameresult.
	 */
	public boolean isAddResultFile()
	{
		return addfilenameresult;
	}
	
	/**
	 * @param addfilenameresult The addfilenameresult to set.
	 */
	public void setAddResultFile(boolean addfilenameresult)
	{
		this.addfilenameresult=addfilenameresult;
	}
	
	public Object clone()
	{
		CubeInputMeta retval = (CubeInputMeta)super.clone();
		return retval;
	}
	
	public void setInfo(Map<String, String[]> p, String id,
 			List<? extends SharedObjectInterface> databases) {
		filename  = BaseStepMeta.parameterToString(p.get(id + ".filename"));
		rowLimit  = BaseStepMeta.parameterToInt(p.get(id + ".rowLimit"));
		addfilenameresult = BaseStepMeta.parameterToBoolean(p.get(id + ".addfilenameresult"));
    }
	
	private void readData(Node stepnode)
		throws KettleXMLException
	{
		try
		{
			filename  = XMLHandler.getTagValue(stepnode, "file", "name"); //$NON-NLS-1$ //$NON-NLS-2$
			rowLimit  = Const.toInt( XMLHandler.getTagValue(stepnode, "limit"), 0); //$NON-NLS-1$
			addfilenameresult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "addfilenameresult"));
			
		}
		catch(Exception e)
		{
			throw new KettleXMLException(Messages.getString("CubeInputMeta.Exception.UnableToLoadStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void setDefault()
	{
		filename = "file"; //$NON-NLS-1$
		rowLimit = 0;
		addfilenameresult=false;
	}
	
	public void getFields(RowMetaInterface r, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
		throws KettleStepException
	{
		GZIPInputStream fis = null;
		DataInputStream dis = null;
		try
		{
			InputStream is = KettleVFS.getInputStream(space.environmentSubstitute(filename));
			fis = new GZIPInputStream(is);
			dis = new DataInputStream(fis);
	
			RowMetaInterface add = new RowMeta(dis);		
				
			if (add==null) return;
			for (int i=0;i<add.size();i++)
			{
				add.getValueMeta(i).setOrigin(name);
			}
			r.mergeRowMeta(add);
		}
		catch(KettleFileException kfe)
		{
			throw new KettleStepException(Messages.getString("CubeInputMeta.Exception.UnableToReadMetaData"), kfe); //$NON-NLS-1$
		}
		catch(IOException e)
		{
			throw new KettleStepException(Messages.getString("CubeInputMeta.Exception.ErrorOpeningOrReadingCubeFile"), e); //$NON-NLS-1$
		}
		finally
		{
			try
			{
				if (fis!=null) fis.close();
				if (dis!=null) dis.close();
			}
			catch(IOException ioe)
			{
				throw new KettleStepException(Messages.getString("CubeInputMeta.Exception.UnableToCloseCubeFile"), ioe); //$NON-NLS-1$
			}
		}
	}
	
	public String getXML()
	{
        StringBuffer retval = new StringBuffer(300);
		
		retval.append("    <file>").append(Const.CR); //$NON-NLS-1$
		retval.append("      ").append(XMLHandler.addTagValue("name", filename)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    </file>").append(Const.CR); //$NON-NLS-1$
		retval.append("    ").append(XMLHandler.addTagValue("limit",  rowLimit)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    ").append(XMLHandler.addTagValue("addfilenameresult", addfilenameresult));
		

		return retval.toString();
	}
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String,Counter> counters)
		throws KettleException
	{
		try
		{
			filename         =      rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_FILE_NAME); //$NON-NLS-1$
			rowLimit         = (int)rep.getStepAttributeInteger(id_step, STEP_ATTRIBUTE_LIMIT); //$NON-NLS-1$
			addfilenameresult = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_ADDFILENAMERESULT);
			
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("CubeInputMeta.Exception.UnexpectedErrorWhileReadingStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, "file_name",   filename); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "limit",       rowLimit); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "addfilenameresult", addfilenameresult);
			
		}
		catch(KettleException e)
		{
			throw new KettleException(Messages.getString("CubeInputMeta.Exception.UnableToSaveStepInfo")+id_step, e); //$NON-NLS-1$
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
		cr = new CheckResult(CheckResult.TYPE_RESULT_COMMENT, Messages.getString("CubeInputMeta.CheckResult.FileSpecificationsNotChecked"), stepinfo); //$NON-NLS-1$
		remarks.add(cr);
	}
	
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new CubeInput(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData()
	{
		return new CubeInputData();
	}

}

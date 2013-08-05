/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package com.panet.imeta.trans.steps.closure;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.annotations.Step;
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
import com.panet.imeta.trans.step.StepCategory;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/*
 * Created on 19-Sep-2007
 *
 */
@Step(name="ClosureGenerator",image="CLG.png",tooltip="BaseStep.TypeTooltipDesc.ClosureGenerator",description="BaseStep.TypeLongDesc.ClosureGenerator",
		category=StepCategory.CATEGORY_TRANSFORM)
public class ClosureGeneratorMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_PARENT_ID_FIELD = "parent_id_field";
	public static final String STEP_ATTRIBUTE_CHILD_ID_FIELD = "child_id_field";
	public static final String STEP_ATTRIBUTE_DISTANCE_FIELD = "distance_field";
	public static final String STEP_ATTRIBUTE_IS_ROOT_ZERO = "is_root_zero";
	private boolean rootIdZero;
	
	private String parentIdFieldName;
	private String childIdFieldName;
	private String distanceFieldName;
	
	public ClosureGeneratorMeta()
	{
		super();
	}
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode, databases);
	}

	public Object clone()
	{
		ClosureGeneratorMeta retval = (ClosureGeneratorMeta)super.clone();
		return retval;
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		parentIdFieldName = BaseStepMeta.parameterToString(p.get(id + ".parentIdFieldName"));
		childIdFieldName = BaseStepMeta.parameterToString(p.get(id + ".childIdFieldName"));
		distanceFieldName = BaseStepMeta.parameterToString(p.get(id + ".distanceFieldName"));
		rootIdZero = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".rootIdZero"));
	}
	private void readData(Node stepnode, List<? extends SharedObjectInterface> databases)
		throws KettleXMLException
	{
		try
		{
			parentIdFieldName = XMLHandler.getTagValue(stepnode, "parent_id_field");
			childIdFieldName = XMLHandler.getTagValue(stepnode, "child_id_field");
			distanceFieldName = XMLHandler.getTagValue(stepnode, "distance_field");
			rootIdZero = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "is_root_zero"));
		}
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void setDefault()
	{
	}

    public void getFields(RowMetaInterface row, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException 
    {
    	// The output for the closure table is:
    	//
    	// - parentId
    	// - childId
    	// - distance
    	//
    	// Nothing else.
    	//
    	RowMetaInterface result = new RowMeta();
    	ValueMetaInterface parentValueMeta = row.searchValueMeta(parentIdFieldName);
    	if (parentValueMeta!=null) result.addValueMeta(parentValueMeta);
    	
    	ValueMetaInterface childValueMeta = row.searchValueMeta(childIdFieldName);
    	if (childValueMeta!=null) result.addValueMeta(childValueMeta);

    	ValueMetaInterface distanceValueMeta = new ValueMeta(distanceFieldName, ValueMetaInterface.TYPE_INTEGER);
    	distanceValueMeta.setLength(ValueMetaInterface.DEFAULT_INTEGER_LENGTH);
    	result.addValueMeta(distanceValueMeta);

    	row.clear();
    	row.addRowMeta(result);
	}

	public String getXML()
	{
        StringBuffer retval = new StringBuffer(300);

		retval.append("    ").append(XMLHandler.addTagValue("parent_id_field", parentIdFieldName));
		retval.append("    ").append(XMLHandler.addTagValue("child_id_field", childIdFieldName));
		retval.append("    ").append(XMLHandler.addTagValue("distance_field", distanceFieldName));
		retval.append("    ").append(XMLHandler.addTagValue("is_root_zero", rootIdZero));
        
		return retval.toString();
	}

	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException
	{
		try
		{
			parentIdFieldName = rep.getStepAttributeString (id_step, "parent_id_field");
			childIdFieldName  = rep.getStepAttributeString (id_step, "child_id_field");
			distanceFieldName = rep.getStepAttributeString (id_step, "distance_field");
			rootIdZero        = rep.getStepAttributeBoolean(id_step, "is_root_zero");
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
			rep.saveStepAttribute(id_transformation, id_step, "parent_id_field", parentIdFieldName);
			rep.saveStepAttribute(id_transformation, id_step, "child_id_field", childIdFieldName);
			rep.saveStepAttribute(id_transformation, id_step, "distance_field", distanceFieldName);
			rep.saveStepAttribute(id_transformation, id_step, "is_root_zero", rootIdZero);
		}
		catch(Exception e)
		{
			throw new KettleException("Unable to save step information to the repository for id_step="+id_step, e);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface row, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
    	ValueMetaInterface parentValueMeta = row.searchValueMeta(parentIdFieldName);
    	if (parentValueMeta!=null) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, "The fieldname of the parent id could not be found.", stepMeta);
			remarks.add(cr);
    	}
    	else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, "The fieldname of the parent id could be found", stepMeta);
			remarks.add(cr);
    	}

    	ValueMetaInterface childValueMeta = row.searchValueMeta(childIdFieldName);
    	if (childValueMeta!=null) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, "The fieldname of the child id could not be found.", stepMeta);
			remarks.add(cr);
    	}
    	else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, "The fieldname of the child id could be found", stepMeta);
			remarks.add(cr);
    	}
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new ClosureGenerator(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData()
	{
		return new ClosureGeneratorData();
	}

	/**
	 * @return the rootIdZero
	 */
	public boolean isRootIdZero() {
		return rootIdZero;
	}

	/**
	 * @param rootIdZero the rootIdZero to set
	 */
	public void setRootIdZero(boolean rootIdZero) {
		this.rootIdZero = rootIdZero;
	}

	/**
	 * @return the parentIdFieldName
	 */
	public String getParentIdFieldName() {
		return parentIdFieldName;
	}

	/**
	 * @param parentIdFieldName the parentIdFieldName to set
	 */
	public void setParentIdFieldName(String parentIdFieldName) {
		this.parentIdFieldName = parentIdFieldName;
	}

	/**
	 * @return the childIdFieldName
	 */
	public String getChildIdFieldName() {
		return childIdFieldName;
	}

	/**
	 * @param childIdFieldName the childIdFieldName to set
	 */
	public void setChildIdFieldName(String childIdFieldName) {
		this.childIdFieldName = childIdFieldName;
	}

	/**
	 * @return the distanceFieldName
	 */
	public String getDistanceFieldName() {
		return distanceFieldName;
	}

	/**
	 * @param distanceFieldName the distanceFieldName to set
	 */
	public void setDistanceFieldName(String distanceFieldName) {
		this.distanceFieldName = distanceFieldName;
	}
}

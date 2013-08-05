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

package com.panet.imeta.trans.steps.rowsfromresult;

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
 * Created on 02-jun-2003
 * 
 */

public class RowsFromResultMeta extends BaseStepMeta implements
		StepMetaInterface {
	private String name[];
	private int type[];
	private int length[];
	private int precision[];

	/**
	 * @return Returns the length.
	 */
	public int[] getLength() {
		return length;
	}

	/**
	 * @param length
	 *            The length to set.
	 */
	public void setLength(int[] length) {
		this.length = length;
	}

	/**
	 * @return Returns the name.
	 */
	public String[] getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String[] name) {
		this.name = name;
	}

	/**
	 * @return Returns the precision.
	 */
	public int[] getPrecision() {
		return precision;
	}

	/**
	 * @param precision
	 *            The precision to set.
	 */
	public void setPrecision(int[] precision) {
		this.precision = precision;
	}

	/**
	 * @return Returns the type.
	 */
	public int[] getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(int[] type) {
		this.type = type;
	}

	public RowsFromResultMeta() {
		super(); // allocate BaseStepMeta
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public Object clone() {
		Object retval = super.clone();
		return retval;
	}

	public void allocate(int nrFields) {
		name = new String[nrFields];
		type = new int[nrFields];
		length = new int[nrFields];
		precision = new int[nrFields];
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();
		retval.append("    <fields>"); //$NON-NLS-1$
		for (int i = 0; i < name.length; i++) {
			retval.append("      <field>"); //$NON-NLS-1$
			retval.append("        " + XMLHandler.addTagValue("name", name[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval
					.append("        " + XMLHandler.addTagValue("type", ValueMeta.getTypeDesc(type[i]))); //$NON-NLS-1$ //$NON-NLS-2$
			retval
					.append("        " + XMLHandler.addTagValue("length", length[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval
					.append("        " + XMLHandler.addTagValue("precision", precision[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        </field>"); //$NON-NLS-1$
		}
		retval.append("      </fields>"); //$NON-NLS-1$

		return retval.toString();
	}

	private void readData(Node stepnode) {
		Node fields = XMLHandler.getSubNode(stepnode, "fields"); //$NON-NLS-1$
		int nrfields = XMLHandler.countNodes(fields, "field"); //$NON-NLS-1$

		allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			Node line = XMLHandler.getSubNodeByNr(fields, "field", i); //$NON-NLS-1$
			name[i] = XMLHandler.getTagValue(line, "name"); //$NON-NLS-1$
			type[i] = ValueMeta.getType(XMLHandler.getTagValue(line, "type")); //$NON-NLS-1$
			length[i] = Const.toInt(XMLHandler.getTagValue(line, "length"), -2); //$NON-NLS-1$
			precision[i] = Const.toInt(XMLHandler
					.getTagValue(line, "precision"), -2); //$NON-NLS-1$
		}

	}

	public void setDefault() {
		allocate(0);
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		String[] name = p.get(id + "_fields.name");
		String[] type = p.get(id + "_fields.type");
		String[] length = p.get(id + "_fields.length");
		String[] precision = p.get(id + "_fields.precision");

		this.name = (name != null) ? name : (new String[0]);
		this.type = stringArrayToIntArray(type, 0);
		this.length = stringArrayToIntArray(length, -1);
		this.precision = stringArrayToIntArray(precision, -1);
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			int nrfields = rep.countNrStepAttributes(id_step, "field_name"); //$NON-NLS-1$
			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				name[i] = rep.getStepAttributeString(id_step, i, "field_name"); //$NON-NLS-1$
				type[i] = ValueMeta.getType(rep.getStepAttributeString(id_step,
						i, "field_type")); //$NON-NLS-1$
				length[i] = (int) rep.getStepAttributeInteger(id_step, i,
						"field_length"); //$NON-NLS-1$
				precision[i] = (int) rep.getStepAttributeInteger(id_step, i,
						"field_precision"); //$NON-NLS-1$
			}
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("RowsFromResultMeta.Exception.ErrorReadingStepInfoFromRepository"), e); //$NON-NLS-1$
		}

	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			for (int i = 0; i < name.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", name[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_type", ValueMeta.getTypeDesc(type[i])); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_length", length[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_precision", precision[i]); //$NON-NLS-1$
			}
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("RowsFromResultMeta.Exception.UnableToSaveStepInfoToRepository") + id_step, e); //$NON-NLS-1$
		}
	}

	public void getFields(RowMetaInterface r, String origin,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		for (int i = 0; i < this.name.length; i++) {
			ValueMeta v = new ValueMeta(name[i], type[i], length[i],
					precision[i]);
			v.setOrigin(origin);
			r.addValueMeta(v);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		// See if we have input streams leading to this step!
		if (input.length > 0) {
			CheckResult cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("RowsFromResultMeta.CheckResult.StepExpectingNoReadingInfoFromOtherSteps"), stepMeta); //$NON-NLS-1$
			remarks.add(cr);
		} else {
			CheckResult cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_OK,
					Messages
							.getString("RowsFromResultMeta.CheckResult.NoInputReceivedError"), stepMeta); //$NON-NLS-1$
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new RowsFromResult(stepMeta, stepDataInterface, cnr, transMeta,
				trans);
	}

	public StepDataInterface getStepData() {
		return new RowsFromResultData();
	}

}

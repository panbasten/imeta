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
package com.panet.imeta.trans.steps.uniquerows;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
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
 * Created on 02-jun-2003
 * 
 */

public class UniqueRowsMeta extends BaseStepMeta implements StepMetaInterface {
	public static final String STEP_ATTRIBUTE_COUNT_ROWS = "count_rows";
	public static final String STEP_ATTRIBUTE_COUNT_FIELDS = "count_fields";
	/** Indicate that we want to count the number of doubles */
	private boolean countRows;

	/** The fieldname that will contain the number of doubles */
	private String countField;

	/** The fields to compare for double, null means all */
	private String compareFields[];

	/** The fields to compare for double, null means all */
	private boolean caseInsensitive[];

	public UniqueRowsMeta() {
		super(); // allocate BaseStepMeta
	}

	/**
	 * @return Returns the countRows.
	 */
	public boolean isCountRows() {
		return countRows;
	}

	/**
	 * @param countRows
	 *            The countRows to set.
	 */
	public void setCountRows(boolean countRows) {
		this.countRows = countRows;
	}

	/**
	 * @return Returns the countField.
	 */
	public String getCountField() {
		return countField;
	}

	/**
	 * @param countField
	 *            The countField to set.
	 */
	public void setCountField(String countField) {
		this.countField = countField;
	}

	/**
	 * @param compareField
	 *            The compareField to set.
	 */
	public void setCompareFields(String[] compareField) {
		this.compareFields = compareField;
	}

	/**
	 * @return Returns the compareField.
	 */
	public String[] getCompareFields() {
		return compareFields;
	}

	public void allocate(int nrfields) {
		compareFields = new String[nrfields];
		caseInsensitive = new boolean[nrfields];
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public Object clone() {
		UniqueRowsMeta retval = (UniqueRowsMeta) super.clone();

		int nrfields = compareFields.length;

		retval.allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			retval.getCompareFields()[i] = compareFields[i];
			retval.getCaseInsensitive()[i] = caseInsensitive[i];
		}

		return retval;
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			countRows = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "count_rows")); //$NON-NLS-1$ //$NON-NLS-2$
			countField = XMLHandler.getTagValue(stepnode, "count_field"); //$NON-NLS-1$

			Node fields = XMLHandler.getSubNode(stepnode, "fields"); //$NON-NLS-1$
			int nrfields = XMLHandler.countNodes(fields, "field"); //$NON-NLS-1$

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i); //$NON-NLS-1$

				compareFields[i] = XMLHandler.getTagValue(fnode, "name"); //$NON-NLS-1$
				caseInsensitive[i] = !"N".equalsIgnoreCase(XMLHandler.getTagValue(fnode, "case_insensitive")); //$NON-NLS-1$ //$NON-NLS-2$
			}

		} catch (Exception e) {
			throw new KettleXMLException(
					Messages
							.getString("UniqueRowsMeta.Exception.UnableToLoadStepInfoFromXML"), e); //$NON-NLS-1$
		}
	}

	public void setDefault() {
		countRows = false;
		countField = ""; //$NON-NLS-1$

		int nrfields = 0;

		allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			compareFields[i] = "field" + i; //$NON-NLS-1$
			caseInsensitive[i] = true;
		}
	}

	public void getFields(RowMetaInterface row, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		// change the case insensitive flag too
		for (int i = 0; i < compareFields.length; i++) {
			int idx = row.indexOfValue(compareFields[i]);
			if (idx >= 0) {
				row.getValueMeta(idx).setCaseInsensitive(caseInsensitive[i]);
			}
		}
		if (countRows) {
			ValueMetaInterface v = new ValueMeta(countField,
					ValueMetaInterface.TYPE_INTEGER);
			v.setLength(ValueMetaInterface.DEFAULT_INTEGER_LENGTH, 0);
			v.setOrigin(name);
			row.addValueMeta(v);
		}
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();

		retval
				.append("      " + XMLHandler.addTagValue("count_rows", countRows)); //$NON-NLS-1$ //$NON-NLS-2$
		retval
				.append("      " + XMLHandler.addTagValue("count_field", countField)); //$NON-NLS-1$ //$NON-NLS-2$

		retval.append("    <fields>"); //$NON-NLS-1$
		for (int i = 0; i < compareFields.length; i++) {
			retval.append("      <field>"); //$NON-NLS-1$
			retval
					.append("        " + XMLHandler.addTagValue("name", compareFields[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval
					.append("        " + XMLHandler.addTagValue("case_insensitive", caseInsensitive[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        </field>"); //$NON-NLS-1$
		}
		retval.append("      </fields>"); //$NON-NLS-1$

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		countRows = BaseStepMeta.parameterToBoolean(p.get(id + ".countRows"));
		countField = BaseStepMeta.parameterToString(p.get(id + ".countField"));

		String[] compareFields = p.get(id + "_fields.compareFields");
		String[] caseInsensitive = p.get(id + "_fields.caseInsensitive");

		this.compareFields = (compareFields != null) ? compareFields : (new String[0]);
		this.caseInsensitive = stringArrayToBooleanArray(caseInsensitive, false);
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			countRows = rep.getStepAttributeBoolean(id_step, "count_rows"); //$NON-NLS-1$
			countField = rep.getStepAttributeString(id_step, "count_fields"); //$NON-NLS-1$

			int nrfields = rep.countNrStepAttributes(id_step, "field_name"); //$NON-NLS-1$

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				compareFields[i] = rep.getStepAttributeString(id_step, i,
						"field_name"); //$NON-NLS-1$
				caseInsensitive[i] = rep.getStepAttributeBoolean(id_step, i,
						"case_insensitive", true); //$NON-NLS-1$
			}
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("UniqueRowsMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step,
					"count_rows", countRows); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step,
					"count_fields", countField); //$NON-NLS-1$

			for (int i = 0; i < compareFields.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", compareFields[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i,
						"case_insensitive", caseInsensitive[i]); //$NON-NLS-1$
			}
		} catch (KettleException e) {
			throw new KettleException(
					Messages
							.getString("UniqueRowsMeta.Exception.UnableToSaveStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepinfo, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;

		if (input.length > 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_OK,
					Messages
							.getString("UniqueRowsMeta.CheckResult.StepReceivingInfoFromOtherSteps"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("UniqueRowsMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new UniqueRows(stepMeta, stepDataInterface, cnr, transMeta,
				trans);
	}

	public StepDataInterface getStepData() {
		return new UniqueRowsData();
	}

	/**
	 * @return Returns the caseInsensitive.
	 */
	public boolean[] getCaseInsensitive() {
		return caseInsensitive;
	}

	/**
	 * @param caseInsensitive
	 *            The caseInsensitive to set.
	 */
	public void setCaseInsensitive(boolean[] caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

}

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

package com.panet.imeta.trans.steps.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.w3c.dom.Node;

import com.opensymphony.xwork2.validator.annotations.Validations;
import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
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

/**
 * Contains the meta-data for the Validator step: calculates predefined
 * formula's
 * 
 * Created on 08-sep-2005
 */

public class ValidatorMeta extends BaseStepMeta implements StepMetaInterface {
	/** The calculations to be performed */
	private Validation[] validations;

	/** Checkbox to have all rules validated, with all the errors in the output */
	private boolean validatingAll;

	public ValidatorMeta() {
		super(); // allocate BaseStepMeta
	}

	public void allocate(int nrValidations) {
		validations = new Validation[nrValidations];
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		int nrCalcs = XMLHandler.countNodes(stepnode, Validation.XML_TAG);
		allocate(nrCalcs);
		validatingAll = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
				"validate_all"));
		for (int i = 0; i < nrCalcs; i++) {
			Node calcnode = XMLHandler.getSubNodeByNr(stepnode,
					Validation.XML_TAG, i);
			validations[i] = new Validation(calcnode);
		}
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(300);

		retval.append(XMLHandler.addTagValue("validate_all", validatingAll));

		if (validations != null)
			for (int i = 0; i < validations.length; i++) {
				retval.append("       ").append(validations[i].getXML())
						.append(Const.CR);
			}

		return retval.toString();
	}

	public boolean equals(Object obj) {
		if (obj != null && (obj.getClass().equals(this.getClass()))) {
			ValidatorMeta m = (ValidatorMeta) obj;
			return (getXML() == m.getXML());
		}

		return false;
	}

	public Object clone() {
		ValidatorMeta retval = (ValidatorMeta) super.clone();
		if (validations != null) {
			retval.allocate(validations.length);
			for (int i = 0; i < validations.length; i++)
				retval.validations[i] = validations[i].clone();
		} else {
			retval.allocate(0);
		}
		return retval;
	}

	public void setDefault() {
		validations = new Validation[0];
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		int nrValidationFields = rep.countNrStepAttributes(id_step,
				"validator_field_name");
		allocate(nrValidationFields);
		validatingAll = rep.getStepAttributeBoolean(id_step, "validate_all");

		for (int i = 0; i < nrValidationFields; i++) {
			validations[i] = new Validation(rep, id_step, i);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		rep.saveStepAttribute(id_transformation, id_step, "validate_all",
				validatingAll);
		for (int i = 0; i < validations.length; i++) {
			validations[i].saveRep(rep, id_transformation, id_step, i);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;
		if (prev == null || prev.size() == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_WARNING,
					Messages
							.getString("ValidatorMeta.CheckResult.ExpectedInputError"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("ValidatorMeta.CheckResult.FieldsReceived", ""
							+ prev.size()), stepMeta);
			remarks.add(cr);
		}

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("ValidatorMeta.CheckResult.ExpectedInputOk"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("ValidatorMeta.CheckResult.ExpectedInputError"),
					stepMeta);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new Validator(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData() {
		return new ValidatorData();
	}

	public boolean supportsErrorHandling() {
		return true;
	}

	/**
	 * @return the validations
	 */
	public Validation[] getValidations() {
		return validations;
	}

	/**
	 * @param validations
	 *            the validations to set
	 */
	public void setValidations(Validation[] validations) {
		this.validations = validations;
	}

	public boolean excludeFromRowLayoutVerification() {
		return true;
	}

	/**
	 * @param steps
	 *            optionally search the info step in a list of steps
	 */
	public void searchInfoAndTargetSteps(List<StepMeta> steps) {
		for (Validation validation : validations) {
			validation.setSourcingStep(StepMeta.findStep(steps, validation
					.getSourcingStepName()));
		}
	}

	/**
	 * @return the informational source steps, if any. Null is the default:
	 *         none.
	 */
	public String[] getInfoSteps() {
		List<String> infoSteps = new ArrayList<String>();
		for (Validation validation : validations) {
			if (validation.getSourcingStep() != null) {
				String stepname = validation.getSourcingStep().getName();
				if (!infoSteps.contains(stepname)) {
					infoSteps.add(stepname);
				}
			}
		}
		if (infoSteps.isEmpty())
			return null;

		return infoSteps.toArray(new String[infoSteps.size()]);
	}

	/**
	 * @return the validatingAll
	 */
	public boolean isValidatingAll() {
		return validatingAll;
	}

	/**
	 * @param validatingAll
	 *            the validatingAll to set
	 */
	public void setValidatingAll(boolean validatingAll) {
		this.validatingAll = validatingAll;
	}

	@Override
	// public void setInfo(Map<String, String[]> p, String id,
	// List<? extends SharedObjectInterface> databases) {
	// // TODO Auto-generated method stub
	// validatingAll = BaseStepMeta.parameterToBoolean(p.get(id
	// + ".validatingAll"));
	// String name = BaseStepMeta.parameterToString(p.get(id + ".valName"));
	// String fieldName = BaseStepMeta.parameterToString(p.get(id
	// + ".fieldName"));
	// String errorCode = BaseStepMeta.parameterToString(p.get(id
	// + ".errorCode"));
	//
	// String errorDescription = BaseStepMeta.parameterToString(p.get(id
	// + ".errorDescription"));
	//
	// boolean dataTypeVerified = BaseStepMeta.parameterToBoolean(p.get(id
	// + ".dataTypeVerified"));
	//
	// int dataType = BaseStepMeta.parameterToInt(p
	// .get(id + ".dataType"));
	//
	// String conversionMask = BaseStepMeta.parameterToString(p.get(id
	// + ".conversionMask"));
	//
	// String decimalSymbol = BaseStepMeta.parameterToString(p.get(id
	// + ".decimalSymbol"));
	//
	// String groupingSymbol = BaseStepMeta.parameterToString(p.get(id
	// + ".groupingSymbol"));
	//
	// boolean nullAllowed = BaseStepMeta.parameterToBoolean(p.get(id
	// + ".nullAllowed"));
	//
	// boolean onlyNullAllowed = BaseStepMeta.parameterToBoolean(p.get(id
	// + ".onlyNullAllowed"));
	//
	// boolean onlyNumericAllowed = BaseStepMeta.parameterToBoolean(p.get(id
	// + ".onlyNumericAllowed"));
	//
	// int maximumLength = BaseStepMeta.parameterToInt(p.get(id
	// + ".maximumLength"));
	//
	// int minimumLength = BaseStepMeta.parameterToInt(p.get(id
	// + ".minimumLength"));
	//
	// String minimumValue = BaseStepMeta.parameterToString(p.get(id
	// + ".minimumValue"));
	//
	// String maximumValue = BaseStepMeta.parameterToString(p.get(id
	// + ".maximumValue"));
	//
	// String startString = BaseStepMeta.parameterToString(p.get(id
	// + ".startString"));
	//
	// String startStringNotAllowed = BaseStepMeta.parameterToString(p.get(id
	// + ".startStringNotAllowed"));
	// String endString = BaseStepMeta.parameterToString(p.get(id
	// + ".endString"));
	//
	// String endStringNotAllowed = BaseStepMeta.parameterToString(p.get(id
	// + ".endStringNotAllowed"));
	//
	// String regularExpression = BaseStepMeta.parameterToString(p.get(id
	// + ".regularExpression"));
	// String regularExpressionNotAllowed = BaseStepMeta.parameterToString(p
	// .get(id + ".regularExpressionNotAllowed"));
	// boolean sourcingValues = BaseStepMeta.parameterToBoolean(p.get(id
	// + ".sourcingValues"));
	// String sourcingStepName = BaseStepMeta.parameterToString(p.get(id
	// + ".sourcingStepName"));
	// String sourcingField = BaseStepMeta.parameterToString(p.get(id
	// + ".sourcingField"));
	//
	// String[] allowedValues = p.get(id + "_allowedValues.value");
	//		
	// Validation validation = new Validation();
	// validation.setAllowedValues(allowedValues);
	// validation.setConversionMask(conversionMask);
	// validation.setDataType(dataType);
	// validation.setDataTypeVerified(dataTypeVerified);
	// validation.setDecimalSymbol(decimalSymbol);
	// validation.setEndString(endString);
	// validation.setEndStringNotAllowed(endStringNotAllowed);
	// validation.setErrorCode(errorCode);
	// validation.setErrorDescription(errorDescription);
	// validation.setFieldName(fieldName);
	// validation.setGroupingSymbol(groupingSymbol);
	// validation.setMaximumLength(maximumLength);
	// validation.setMaximumValue(maximumValue);
	// validation.setMinimumLength(minimumLength);
	// validation.setMinimumValue(minimumValue);
	// validation.setName(name);
	// validation.setNullAllowed(nullAllowed);
	// validation.setOnlyNullAllowed(onlyNullAllowed);
	// validation.setOnlyNumericAllowed(onlyNumericAllowed);
	// validation.setRegularExpression(regularExpression);
	// validation.setRegularExpressionNotAllowed(regularExpressionNotAllowed);
	// validation.setSourcingField(sourcingField);
	// validation.setSourcingStepName(sourcingStepName);
	// validation.setSourcingValues(sourcingValues);
	// validation.setStartString(startString);
	// validation.setStartStringNotAllowed(startStringNotAllowed);
	//		
	// this.validations = new Validation[]{validation};
	// }
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub

		String[] validations = p.get("validations");
		if (null != validations) {
			Validation[] results = new Validation[validations.length];
			for (int i = 0; i < validations.length; i++) {
				String s = validations[i];

				try {
					if (s.length() == 0)
						break;
					JSONObject jsonObj = JSONObject.fromObject(s);
					String name = jsonObj.getString("valName");
					String fieldName = jsonObj.getString("fieldName");
					String errorCode = jsonObj.getString("errorCode");
					String errorDescription = jsonObj
							.getString("errorDescription");
					int dataType = jsonObj.getInt("dataType");
					boolean dataTypeVerified = jsonObj
							.getBoolean("dataTypeVerified");
					String conversionMask = jsonObj.getString("conversionMask");
					String decimalSymbol = jsonObj.getString("decimalSymbol");
					String groupingSymbol = jsonObj.getString("groupingSymbol");
					boolean nullAllowed = jsonObj.getBoolean("nullAllowed");
					boolean onlyNullAllowed = jsonObj
							.getBoolean("onlyNullAllowed");
					boolean onlyNumericAllowed = jsonObj
							.getBoolean("onlyNumericAllowed");
					int maximumLength = -1;
					try {
						maximumLength = jsonObj.getInt("maximumLength");
					} catch (Exception e) {
					}
					int minimumLength = -1;
					try {
						minimumLength = jsonObj.getInt("minimumLength");
					} catch (Exception e) {
					}
					String maximumValue = jsonObj.getString("maximumValue");
					String minimumValue = jsonObj.getString("minimumValue");
					String startString = jsonObj.getString("startString");
					String endString = jsonObj.getString("endString");
					String startStringNotAllowed = jsonObj
							.getString("startStringNotAllowed");
					String endStringNotAllowed = jsonObj
							.getString("endStringNotAllowed");
					String regularExpression = jsonObj
							.getString("regularExpression");
					String regularExpressionNotAllowed = jsonObj
							.getString("regularExpressionNotAllowed");
					boolean sourcingValues = jsonObj
							.getBoolean("sourcingValues");
					String sourcingStepName = jsonObj
							.getString("sourcingStepName");
					String sourcingField = jsonObj.getString("sourcingField");

					JSONArray jsonArr = jsonObj.getJSONArray("allowedValues");
					Validation validation = new Validation();
					validation.setName(name);
					validation.setErrorCode(errorCode);
					validation.setErrorDescription(errorDescription);
					validation.setFieldName(fieldName);
					validation.setDataType(dataType);
					validation.setDataTypeVerified(dataTypeVerified);
					validation.setConversionMask(conversionMask);
					validation.setDecimalSymbol(decimalSymbol);
					validation.setGroupingSymbol(groupingSymbol);
					validation.setNullAllowed(nullAllowed);
					validation.setOnlyNullAllowed(onlyNullAllowed);
					validation.setOnlyNumericAllowed(onlyNumericAllowed);
					validation.setMaximumLength(maximumLength);
					validation.setMinimumLength(minimumLength);
					validation.setMaximumValue(maximumValue);
					validation.setMinimumValue(minimumValue);
					validation.setStartString(startString);
					validation.setEndString(endString);
					validation.setStartStringNotAllowed(startStringNotAllowed);
					validation.setEndStringNotAllowed(endStringNotAllowed);
					validation.setRegularExpression(regularExpression);
					validation
							.setRegularExpressionNotAllowed(regularExpressionNotAllowed);
					validation.setSourcingValues(sourcingValues);
					validation.setSourcingField(sourcingField);
					validation.setSourcingStepName(sourcingStepName);

					Object[] objArr = jsonArr.toArray();
					String[] arr = new String[objArr.length];
					for (int j = 0; j < objArr.length; j++) {
						arr[j] = (String) objArr[j];
					}
					validation.setAllowedValues(arr);

					results[i] = validation;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			this.validations = results;
		} else {
			this.validations = new Validation[0];
		}
		// this.validations = results;
		validatingAll = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".validatingAll"));
	}

}

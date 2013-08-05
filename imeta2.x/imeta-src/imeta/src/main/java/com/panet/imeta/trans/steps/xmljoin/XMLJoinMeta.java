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

package com.panet.imeta.trans.steps.xmljoin;

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

/**
 * This class knows how to handle the MetaData for the XML join step
 * 
 * @since 30-04-2008
 * 
 */

public class XMLJoinMeta extends BaseStepMeta implements StepMetaInterface {
	public static final String STEP_ATTRIBUTE_TARGETXMLSTEP = "targetXMLstep";
	public static final String STEP_ATTRIBUTE_TARGETXMLFIELD = "targetXMLfield";
	public static final String STEP_ATTRIBUTE_SOURCEXMLSTEP = "sourceXMLstep";
	public static final String STEP_ATTRIBUTE_SOURCEXMLFIELD = "sourceXMLfield";
	public static final String STEP_ATTRIBUTE_TARGETXPATH = "targetXPath";
	public static final String STEP_ATTRIBUTE_COMPLEXJOIN = "complexJoin";
	public static final String STEP_ATTRIBUTE_JOINCOMPAREFIELD = "joinCompareField";
	public static final String STEP_ATTRIBUTE_VALUEXMLFIELD = "valueXMLfield";
	public static final String STEP_ATTRIBUTE_ENCODING = "encoding";
	public static final String STEP_ATTRIBUTE_OMITXMLHEADER = "omitXMLHeader";

	/** The base name of the output file */

	/** Flag: execute complex join */
	private boolean complexJoin;

	/** What step holds the xml string to join into */
	private String targetXMLstep;

	/** What field holds the xml string to join into */
	private String targetXMLfield;

	/** What field holds the XML tags to join */
	private String sourceXMLfield;

	/** The name value containing the resulting XML fragment */
	private String valueXMLfield;

	/** The name of the repeating row XML element */
	private String targetXPath;

	/** What step holds the xml strings to join */
	private String sourceXMLstep;

	private boolean sourceNotIncludeRoot;

	/** What field holds the join compare value */
	private String joinCompareField;

	/**
	 * The encoding to use for reading: null or empty string means system
	 * default encoding
	 */
	private String encoding;

	/** Flag: execute complex join */
	private boolean omitXMLHeader;

	public XMLJoinMeta() {
		super(); // allocate BaseStepMeta
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public Object clone() {
		XMLJoinMeta retval = (XMLJoinMeta) super.clone();
		return retval;
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			valueXMLfield = XMLHandler.getTagValue(stepnode, "valueXMLfield");
			targetXMLstep = XMLHandler.getTagValue(stepnode, "targetXMLstep");
			targetXMLfield = XMLHandler.getTagValue(stepnode, "targetXMLfield");
			sourceXMLstep = XMLHandler.getTagValue(stepnode, "sourceXMLstep");
			sourceXMLfield = XMLHandler.getTagValue(stepnode, "sourceXMLfield");
			sourceNotIncludeRoot = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "sourceNotIncludeRoot"));
			targetXPath = XMLHandler.getTagValue(stepnode, "targetXPath");
			joinCompareField = XMLHandler.getTagValue(stepnode,
					"joinCompareField");
			encoding = XMLHandler.getTagValue(stepnode, "encoding");
			complexJoin = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"complexJoin"));
			omitXMLHeader = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "omitXMLHeader"));

		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void setDefault() {
		// complexJoin = false;
		encoding = Const.XML_ENCODING;
	}

	public void getFields(RowMetaInterface row, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {

		ValueMetaInterface v = new ValueMeta(this.getValueXMLfield(),
				ValueMetaInterface.TYPE_STRING);
		v.setOrigin(name);
		row.addValueMeta(v);
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(500);

		retval.append("    ").append(
				XMLHandler.addTagValue("valueXMLField", valueXMLfield));
		retval.append("    ").append(
				XMLHandler.addTagValue("targetXMLstep", targetXMLstep));
		retval.append("    ").append(
				XMLHandler.addTagValue("targetXMLfield", targetXMLfield));
		retval.append("    ").append(
				XMLHandler.addTagValue("sourceXMLstep", sourceXMLstep));
		retval.append("    ").append(
				XMLHandler.addTagValue("sourceXMLfield", sourceXMLfield));
		retval.append("    ").append(
				XMLHandler.addTagValue("sourceNotIncludeRoot",
						sourceNotIncludeRoot));
		retval.append("    ").append(
				XMLHandler.addTagValue("complexJoin", complexJoin));
		retval.append("    ").append(
				XMLHandler.addTagValue("joinCompareField", joinCompareField));
		retval.append("    ").append(
				XMLHandler.addTagValue("targetXPath", targetXPath));
		retval.append("    ").append(
				XMLHandler.addTagValue("encoding", encoding));
		retval.append("    ").append(
				XMLHandler.addTagValue("omitXMLHeader", omitXMLHeader));

		return retval.toString();
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			targetXMLstep = rep
					.getStepAttributeString(id_step, "targetXMLstep");
			targetXMLfield = rep.getStepAttributeString(id_step,
					"targetXMLfield");
			sourceXMLstep = rep
					.getStepAttributeString(id_step, "sourceXMLstep");
			sourceXMLfield = rep.getStepAttributeString(id_step,
					"sourceXMLfield");
			sourceNotIncludeRoot = rep.getStepAttributeBoolean(id_step,
					"sourceNotIncludeRoot");
			targetXPath = rep.getStepAttributeString(id_step, "targetXPath");
			complexJoin = rep.getStepAttributeBoolean(id_step, "complexJoin");
			joinCompareField = rep.getStepAttributeString(id_step,
					"joinCompareField");
			valueXMLfield = rep
					.getStepAttributeString(id_step, "valueXMLfield");
			encoding = rep.getStepAttributeString(id_step, "encoding");
			omitXMLHeader = rep.getStepAttributeBoolean(id_step,
					"omitXMLHeader ");

		} catch (Exception e) {
			throw new KettleException(
					"Unexpected error reading step information from the repository",
					e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "valueXMLfield",
					valueXMLfield);
			rep.saveStepAttribute(id_transformation, id_step, "targetXMLstep",
					targetXMLstep);
			rep.saveStepAttribute(id_transformation, id_step, "targetXMLfield",
					targetXMLfield);
			rep.saveStepAttribute(id_transformation, id_step, "sourceXMLstep",
					sourceXMLstep);
			rep.saveStepAttribute(id_transformation, id_step, "sourceXMLfield",
					sourceXMLfield);
			rep.saveStepAttribute(id_transformation, id_step,
					"sourceNotIncludeRoot", sourceNotIncludeRoot);
			rep.saveStepAttribute(id_transformation, id_step, "complexJoin",
					complexJoin);
			rep.saveStepAttribute(id_transformation, id_step, "targetXPath",
					targetXPath);
			rep.saveStepAttribute(id_transformation, id_step,
					"joinCompareField", joinCompareField);
			rep.saveStepAttribute(id_transformation, id_step, "encoding",
					encoding);
			rep.saveStepAttribute(id_transformation, id_step, "omitXMLHeader",
					omitXMLHeader);
		} catch (Exception e) {
			throw new KettleException(
					"Unable to save step information to the repository for id_step="
							+ id_step, e);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String[] input,
			String[] output, RowMetaInterface info) {

		CheckResult cr;
		// checks for empty field which are required
		if (this.targetXMLstep == null || this.targetXMLstep.length() == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("XMLJoin.CheckResult.TargetXMLStepNotSpecified"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("XMLJoin.CheckResult.TargetXMLStepSpecified"),
					stepMeta);
			remarks.add(cr);
		}
		if (this.targetXMLfield == null || this.targetXMLfield.length() == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("XMLJoin.CheckResult.TargetXMLFieldNotSpecified"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("XMLJoin.CheckResult.TargetXMLFieldSpecified"),
					stepMeta);
			remarks.add(cr);
		}
		if (this.sourceXMLstep == null || this.sourceXMLstep.length() == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("XMLJoin.CheckResult.SourceXMLStepNotSpecified"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("XMLJoin.CheckResult.SourceXMLStepSpecified"),
					stepMeta);
			remarks.add(cr);
		}
		if (this.sourceXMLfield == null || this.sourceXMLfield.length() == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("XMLJoin.CheckResult.SourceXMLFieldNotSpecified"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("XMLJoin.CheckResult.SourceXMLFieldSpecified"),
					stepMeta);
			remarks.add(cr);
		}
		if (this.valueXMLfield == null || this.valueXMLfield.length() == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("XMLJoin.CheckResult.ResultFieldNotSpecified"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("XMLJoin.CheckResult.ResultFieldSpecified"),
					stepMeta);
			remarks.add(cr);
		}
		if (this.targetXPath == null || this.targetXPath.length() == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("XMLJoin.CheckResult.TargetXPathNotSpecified"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("XMLJoin.CheckResult.TargetXPathSpecified"),
					stepMeta);
			remarks.add(cr);
		}

		// See if we have the right input streams leading to this step!
		if (input.length > 0) {
			boolean targetStepFound = false;
			boolean sourceStepFound = false;
			for (int i = 0; i < input.length; i++) {
				if (this.targetXMLstep != null
						&& this.targetXMLstep.equals(input[i])) {
					targetStepFound = true;
					cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
							.getString(
									"XMLJoin.CheckResult.TargetXMLStepFound",
									this.targetXMLstep), stepMeta);
					remarks.add(cr);
				}
				if (this.sourceXMLstep != null
						&& this.sourceXMLstep.equals(input[i])) {
					sourceStepFound = true;
					cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
							.getString(
									"XMLJoin.CheckResult.SourceXMLStepFound",
									this.sourceXMLstep), stepMeta);
					remarks.add(cr);
				}
			}

			if (!targetStepFound) {
				cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
						Messages.getString(
								"XMLJoin.CheckResult.TargetXMLStepNotFound",
								this.targetXMLstep), stepMeta);
				remarks.add(cr);
			}
			if (!sourceStepFound) {
				cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
						Messages.getString(
								"XMLJoin.CheckResult.SourceXMLStepNotFound",
								this.sourceXMLstep), stepMeta);
				remarks.add(cr);
			}
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("XMLJoin.CheckResult.ExpectedInputError"),
					stepMeta);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new XMLJoin(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData() {
		return new XMLJoinData();
	}

	public boolean isComplexJoin() {
		return complexJoin;
	}

	public void setComplexJoin(boolean complexJoin) {
		this.complexJoin = complexJoin;
	}

	public String getTargetXMLstep() {
		return targetXMLstep;
	}

	public void setTargetXMLstep(String targetXMLstep) {
		this.targetXMLstep = targetXMLstep;
	}

	public String getTargetXMLfield() {
		return targetXMLfield;
	}

	public void setTargetXMLfield(String targetXMLfield) {
		this.targetXMLfield = targetXMLfield;
	}

	public String getSourceXMLstep() {
		return sourceXMLstep;
	}

	public void setSourceXMLstep(String targetXMLstep) {
		this.sourceXMLstep = targetXMLstep;
	}

	public String getSourceXMLfield() {
		return sourceXMLfield;
	}

	public void setSourceXMLfield(String sourceXMLfield) {
		this.sourceXMLfield = sourceXMLfield;
	}

	public String getValueXMLfield() {
		return valueXMLfield;
	}

	public void setValueXMLfield(String valueXMLfield) {
		this.valueXMLfield = valueXMLfield;
	}

	public String getTargetXPath() {
		return targetXPath;
	}

	public void setTargetXPath(String targetXPath) {
		this.targetXPath = targetXPath;
	}

	public String getJoinCompareField() {
		return joinCompareField;
	}

	public void setJoinCompareField(String joinCompareField) {
		this.joinCompareField = joinCompareField;
	}

	public boolean excludeFromRowLayoutVerification() {
		return true;
	}

	public boolean isOmitXMLHeader() {
		return omitXMLHeader;
	}

	public void setOmitXMLHeader(boolean omitXMLHeader) {
		this.omitXMLHeader = omitXMLHeader;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		this.targetXMLstep = BaseStepMeta.parameterToString(p.get(id
				+ ".targetXMLstep"));
		this.targetXMLfield = BaseStepMeta.parameterToString(p.get(id
				+ ".targetXMLfield"));

		this.sourceXMLstep = BaseStepMeta.parameterToString(p.get(id
				+ ".sourceXMLstep"));
		this.sourceXMLfield = BaseStepMeta.parameterToString(p.get(id
				+ ".sourceXMLfield"));

		this.sourceNotIncludeRoot = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".sourceNotIncludeRoot"));

		this.targetXPath = BaseStepMeta.parameterToString(p.get(id
				+ ".targetXPath"));
		this.complexJoin = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".complexJoin"));
		this.joinCompareField = BaseStepMeta.parameterToString(p.get(id
				+ ".joinCompareField"));

		this.valueXMLfield = BaseStepMeta.parameterToString(p.get(id
				+ ".valueXMLfield"));
		this.encoding = BaseStepMeta.parameterToString(p.get(id + ".encoding"));
		this.omitXMLHeader = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".omitXMLHeader"));

	}

	public boolean isSourceNotIncludeRoot() {
		return sourceNotIncludeRoot;
	}

	public void setSourceNotIncludeRoot(boolean sourceNotIncludeRoot) {
		this.sourceNotIncludeRoot = sourceNotIncludeRoot;
	}
}

/* * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.*/

package com.panet.imeta.trans.steps.addxml;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sun.misc.BASE64Encoder;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleValueException;
import com.panet.imeta.core.row.RowDataUtil;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Converts input rows to one or more XML files.
 * 
 * @author Matt
 * @since 14-jan-2006
 */
public class AddXML extends BaseStep implements StepInterface {
	private AddXMLMeta meta;
	private AddXMLData data;

	private DOMImplementation domImplentation;
	private Transformer serializer;

	public AddXML(StepMeta stepMeta, StepDataInterface stepDataInterface,
			int copyNr, TransMeta transMeta, Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
			throws KettleException {
		meta = (AddXMLMeta) smi;
		data = (AddXMLData) sdi;

		Object[] r = getRow(); // This also waits for a row to be finished.
		if (r == null) // no more input to be expected...
		{
			setOutputDone();
			return false;
		}

		if (first) {
			first = false;

			data.outputRowMeta = getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

			// Cache the field name indexes
			//
			data.fieldIndexes = new int[meta.getOutputFields().length];
			for (int i = 0; i < data.fieldIndexes.length; i++) {
				data.fieldIndexes[i] = getInputRowMeta().indexOfValue(
						meta.getOutputFields()[i].getFieldName());
				if (data.fieldIndexes[i] < 0) {
					throw new KettleException(Messages
							.getString("AddXML.Exception.FieldNotFound"));
				}
			}
		}

		Document xmldoc = getDomImplentation().createDocument(null,
				meta.getRootNode(), null);
		Element root = xmldoc.getDocumentElement();

		List<String[]> attributFieldList = new ArrayList<String[]>();
		List<Object[]> subFieldList = new ArrayList<Object[]>();

		for (int i = 0; i < meta.getOutputFields().length; i++) {
			XMLField outputField = meta.getOutputFields()[i];
			String fieldname = outputField.getFieldName();

			ValueMetaInterface v = getInputRowMeta().getValueMeta(
					data.fieldIndexes[i]);
			v.setStringEncoding(meta.getEncoding());
			Object valueData = r[data.fieldIndexes[i]];

			String value = null;

			// 如果是二进制类型
			if (valueData != null && v.isBinary()) {
				byte[] b = v.getBinary(valueData);
				value = new BASE64Encoder().encodeBuffer(b);
			} else {
				value = formatField(v, valueData, outputField);
			}

			String element = outputField.getElementName();
			String attributeParentName = outputField.getAttributeParentName();
			if (element == null || element.length() == 0)
				element = fieldname;

			if (element == null || element.length() == 0) {
				throw new KettleException(
						"XML does not allow empty strings for element names.");
			}

			// 先暂存结果
			// 如果是属性，请不是二进制类型
			if (outputField.isAttribute() && !v.isBinary()) {
				attributFieldList.add(new String[] { attributeParentName,
						element, value, "N" });
			}
			// 如果不是属性
			else {
				String fieldName = outputField.getFieldName();

				if (element.equals(meta.getRootNode())
						&& meta.isAttributeNode()) {
					Node n = xmldoc.createTextNode(value);
					root.appendChild(n);
				} else {
					Element e = xmldoc.createElement(element);
					Node n = xmldoc.createTextNode(value);
					e.appendChild(n);

					subFieldList.add(new Object[] { fieldName,
							attributeParentName, e });
				}
			}
		}

		// 注入节点
		if (subFieldList.size() > 0) {
			String fieldName, attributeParentName;
			Element el = null;
			for (Object[] subField : subFieldList) {
				fieldName = (String) subField[0];
				attributeParentName = (String) subField[1];
				el = (Element) subField[2];

				// 注入属性
				for (String[] attributeField : attributFieldList) {
					if (fieldName.equals(attributeField[0])) {
						el.setAttribute(attributeField[1], attributeField[2]);
						attributeField[3] = "Y";
					}
				}

				// 注入节点
				if (Const.isEmpty(attributeParentName)) {
					root.appendChild(el);
				} else {
					boolean inject = false;
					for (Object[] sf : subFieldList) {
						if (attributeParentName.equals(sf[0])) {
							inject = true;
							((Element) sf[2]).appendChild(el);
						}
					}
					if (!inject) {
						root.appendChild(el);
					}
				}
			}
		}

		// 注入属性
		if (attributFieldList.size() > 0) {
			String element, value;
			for (String[] attributeField : attributFieldList) {
				if ("N".equals(attributeField[3])) {
					element = attributeField[1];
					value = attributeField[2];
					root.setAttribute(element, value);
				}
			}
		}

		StringWriter sw = new StringWriter();
		DOMSource domSource = new DOMSource(xmldoc);
		try {
			this.getSerializer().transform(domSource, new StreamResult(sw));
		} catch (TransformerException e) {
			throw new KettleException(e);
		}

		Object[] outputRowData = RowDataUtil.addValueData(r, getInputRowMeta()
				.size(), sw.toString());

		putRow(data.outputRowMeta, outputRowData);

		return true;
	}

	private String formatField(ValueMetaInterface valueMeta, Object valueData,
			XMLField field) throws KettleValueException {
		String retval = "";
		if (field == null)
			return "";

		if (valueMeta == null || valueMeta.isNull(valueData)) {
			String defaultNullValue = field.getNullString();
			return Const.isEmpty(defaultNullValue) ? "" : defaultNullValue;
		}

		if (valueMeta.isNumeric()) {
			// Formatting
			if (!Const.isEmpty(field.getFormat())) {
				data.df.applyPattern(field.getFormat());
			} else {
				data.df.applyPattern(data.defaultDecimalFormat.toPattern());
			}
			// Decimal
			if (!Const.isEmpty(field.getDecimalSymbol())) {
				data.dfs
						.setDecimalSeparator(field.getDecimalSymbol().charAt(0));
			} else {
				data.dfs.setDecimalSeparator(data.defaultDecimalFormatSymbols
						.getDecimalSeparator());
			}
			// Grouping
			if (!Const.isEmpty(field.getGroupingSymbol())) {
				data.dfs.setGroupingSeparator(field.getGroupingSymbol().charAt(
						0));
			} else {
				data.dfs.setGroupingSeparator(data.defaultDecimalFormatSymbols
						.getGroupingSeparator());
			}
			// Currency symbol
			if (!Const.isEmpty(field.getCurrencySymbol())) {
				data.dfs.setCurrencySymbol(field.getCurrencySymbol());
			} else {
				data.dfs.setCurrencySymbol(data.defaultDecimalFormatSymbols
						.getCurrencySymbol());
			}

			data.df.setDecimalFormatSymbols(data.dfs);

			if (valueMeta.isBigNumber()) {
				retval = data.df.format(valueMeta.getBigNumber(valueData));
			} else if (valueMeta.isNumber()) {
				retval = data.df.format(valueMeta.getNumber(valueData));
			} else // Integer
			{
				retval = data.df.format(valueMeta.getInteger(valueData));
			}
		} else if (valueMeta.isDate()) {
			if (field != null && !Const.isEmpty(field.getFormat())
					&& valueMeta.getDate(valueData) != null) {
				if (!Const.isEmpty(field.getFormat())) {
					data.daf.applyPattern(field.getFormat());
				} else {
					data.daf.applyPattern(data.defaultDateFormat
							.toLocalizedPattern());
				}
				data.daf.setDateFormatSymbols(data.dafs);
				retval = data.daf.format(valueMeta.getDate(valueData));
			} else {
				if (valueMeta.isNull(valueData)) {
					if (field != null && !Const.isEmpty(field.getNullString())) {
						retval = field.getNullString();
					}
				} else {
					retval = valueMeta.getString(valueData);
				}
			}
		} else if (valueMeta.isString()) {
			retval = valueMeta.getString(valueData);
		} else if (valueMeta.isBinary()) {
			if (valueMeta.isNull(valueData)) {
				if (!Const.isEmpty(field.getNullString())) {
					retval = field.getNullString();
				} else {
					retval = Const.NULL_BINARY;
				}
			} else {
				try {
					retval = new String(valueMeta.getBinary(valueData), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// chances are small we'll get here. UTF-8 is
					// mandatory.
					retval = Const.NULL_BINARY;
				}
			}
		} else // Boolean
		{
			retval = valueMeta.getString(valueData);
		}

		return retval;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (AddXMLMeta) smi;
		data = (AddXMLData) sdi;
		if (!super.init(smi, sdi))
			return false;

		try {
			setSerializer(TransformerFactory.newInstance().newTransformer());
			setDomImplentation(DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().getDOMImplementation());

			if (meta.getEncoding() != null) {
				getSerializer().setOutputProperty(OutputKeys.ENCODING,
						meta.getEncoding());
			}

			if (meta.isOmitXMLheader()) {
				getSerializer().setOutputProperty(
						OutputKeys.OMIT_XML_DECLARATION, "yes");
			}
		} catch (TransformerConfigurationException e) {
			return false;
		} catch (ParserConfigurationException e) {
			return false;
		}

		return true;
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (AddXMLMeta) smi;
		data = (AddXMLData) sdi;

		super.dispose(smi, sdi);

	}

	private void setDomImplentation(DOMImplementation domImplentation) {
		this.domImplentation = domImplentation;
	}

	private DOMImplementation getDomImplentation() {
		return domImplentation;
	}

	private void setSerializer(Transformer serializer) {
		this.serializer = serializer;
	}

	private Transformer getSerializer() {
		return serializer;
	}

	//
	// Run is were the action happens!
	public void run() {
		BaseStep.runStepThread(this, meta, data);
	}
}

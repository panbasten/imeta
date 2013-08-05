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

package com.panet.imeta.trans.steps.rowgenerator;

import java.util.ArrayList;
import java.util.List;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleValueException;
import com.panet.imeta.core.row.RowDataUtil;
import com.panet.imeta.core.row.RowMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Generates a number of (empty or the same) rows
 * 
 * @author Matt
 * @since 4-apr-2003
 */
public class RowGenerator extends BaseStep implements StepInterface {
	private RowGeneratorMeta meta;
	private RowGeneratorData data;

	private static RowGenerator rowGenerator;

	public static final RowGenerator getInstance() {
		if (rowGenerator == null) {
			rowGenerator = new RowGenerator();
		}
		return rowGenerator;
	}

	public RowGenerator() {
		super();
	}

	public RowGenerator(StepMeta stepMeta, StepDataInterface stepDataInterface,
			int copyNr, TransMeta transMeta, Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);

		meta = (RowGeneratorMeta) getStepMeta().getStepMetaInterface();
		data = (RowGeneratorData) stepDataInterface;
	}

	public RowMetaAndData buildRow(RowGeneratorMeta meta,
			List<CheckResultInterface> remarks, String origin) {
		RowMetaInterface rowMeta = new RowMeta();
		Object[] rowData = RowDataUtil
				.allocateRowData(meta.getFieldName().length);

		for (int i = 0; i < meta.getFieldName().length; i++) {
			int valtype = ValueMeta.getType(meta.getFieldType()[i]);
			if (meta.getFieldName()[i] != null) {
				ValueMetaInterface valueMeta = new ValueMeta(meta
						.getFieldName()[i], valtype); // build a value!
				valueMeta.setLength(meta.getFieldLength()[i]);
				valueMeta.setPrecision(meta.getFieldPrecision()[i]);
				valueMeta.setConversionMask(meta.getFieldFormat()[i]);
				valueMeta.setGroupingSymbol(meta.getGroup()[i]);
				valueMeta.setDecimalSymbol(meta.getDecimal()[i]);
				valueMeta.setOrigin(origin);

				ValueMetaInterface stringMeta = valueMeta.clone();
				stringMeta.setType(ValueMetaInterface.TYPE_STRING);
				
				String stringValue = environmentSubstitute(meta.getValue()[i]);

				// If the value is empty: consider it to be NULL.
				if (Const.isEmpty(stringValue)) {
					rowData[i] = null;

					if (valueMeta.getType() == ValueMetaInterface.TYPE_NONE) {
						String message = Messages.getString(
								"RowGenerator.CheckResult.SpecifyTypeError",
								valueMeta.getName(), stringValue);
						remarks.add(new CheckResult(
								CheckResultInterface.TYPE_RESULT_ERROR,
								message, null));
					}
				} else {
					// Convert the data from String to the specified type ...
					//
					try {
						rowData[i] = valueMeta.convertData(stringMeta,
								stringValue);
					} catch (KettleValueException e) {
						switch (valueMeta.getType()) {
						case ValueMetaInterface.TYPE_NUMBER: {
							String message = Messages
									.getString(
											"RowGenerator.BuildRow.Error.Parsing.Number",
											valueMeta.getName(), stringValue, e
													.toString());
							remarks.add(new CheckResult(
									CheckResultInterface.TYPE_RESULT_ERROR,
									message, null));
						}
							break;
						case ValueMetaInterface.TYPE_DATE: {
							String message = Messages.getString(
									"RowGenerator.BuildRow.Error.Parsing.Date",
									valueMeta.getName(), stringValue, e
											.toString());
							remarks.add(new CheckResult(
									CheckResultInterface.TYPE_RESULT_ERROR,
									message, null));
						}
							break;
						case ValueMetaInterface.TYPE_INTEGER: {
							String message = Messages
									.getString(
											"RowGenerator.BuildRow.Error.Parsing.Integer",
											valueMeta.getName(), stringValue, e
													.toString());
							remarks.add(new CheckResult(
									CheckResultInterface.TYPE_RESULT_ERROR,
									message, null));
						}
							break;
						case ValueMetaInterface.TYPE_BIGNUMBER: {
							String message = Messages
									.getString(
											"RowGenerator.BuildRow.Error.Parsing.BigNumber",
											valueMeta.getName(), stringValue, e
													.toString());
							remarks.add(new CheckResult(
									CheckResultInterface.TYPE_RESULT_ERROR,
									message, null));
						}
							break;
						default:
							// Boolean and binary don't throw errors normally,
							// so it's probably an unspecified error problem...
						{
							String message = Messages
									.getString(
											"RowGenerator.CheckResult.SpecifyTypeError",
											valueMeta.getName(), stringValue);
							remarks.add(new CheckResult(
									CheckResultInterface.TYPE_RESULT_ERROR,
									message, null));
						}
							break;
						}
					}
				}
				// Now add value to the row!
				// This is in fact a copy from the fields row, but now with
				// data.
				rowMeta.addValueMeta(valueMeta);
			}
		}

		return new RowMetaAndData(rowMeta, rowData);
	}

	public synchronized boolean processRow(StepMetaInterface smi,
			StepDataInterface sdi) throws KettleException {
		meta = (RowGeneratorMeta) smi;
		data = (RowGeneratorData) sdi;

		Object[] r = null;
		boolean retval = true;

		if (data.rowsWritten < data.rowLimit) {
			r = data.outputRowMeta.cloneRow(data.outputRowData);
		} else {
			setOutputDone(); // signal end to receiver(s)
			return false;
		}

		putRow(data.outputRowMeta, r);
		data.rowsWritten++;

		if (log.isRowLevel()) {
			log.logRowlevel(toString(), Messages.getString(
					"RowGenerator.Log.Wrote.Row", Long
							.toString(data.rowsWritten), data.outputRowMeta
							.getString(r)));
		}

		if (checkFeedback(getLinesRead())) {
			if (log.isBasic())
				logBasic(Messages.getString("RowGenerator.Log.LineNr", Long
						.toString(data.rowsWritten)));
		}

		return retval;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (RowGeneratorMeta) smi;
		data = (RowGeneratorData) sdi;

		if (super.init(smi, sdi)) {
			// Determine the number of rows to generate...
			data.rowLimit = Const.toLong(environmentSubstitute(meta
					.getRowLimit()), -1L);
			data.rowsWritten = 0L;

			if (data.rowLimit < 0L) // Unable to parse
			{
				logError(Messages
						.getString("RowGenerator.Wrong.RowLimit.Number"));
				return false; // fail
			}

			// Create a row (constants) with all the values in it...
			List<CheckResultInterface> remarks = new ArrayList<CheckResultInterface>(); // stores
			// the
			// errors...
			RowMetaAndData outputRow = buildRow(meta, remarks, getStepname());
			if (!remarks.isEmpty()) {
				for (int i = 0; i < remarks.size(); i++) {
					CheckResult cr = (CheckResult) remarks.get(i);
					logError(cr.getText());
				}
				return false;
			}
			data.outputRowData = outputRow.getData();
			data.outputRowMeta = outputRow.getRowMeta();
			return true;
		}
		return false;
	}

	//
	// Run is were the action happens!
	public void run() {
		BaseStep.runStepThread(this, meta, data);
	}
}

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

/*
 * Created on 26-apr-2003
 *
 */
package com.panet.imeta.trans.steps.databasejoin;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.row.RowMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.DatabaseImpact;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

public class DatabaseJoinMeta extends BaseStepMeta implements StepMetaInterface {
	public static final String STEP_ATTRIBUTE_ID_CONNECTION = "id_connection";
	public static final String STEP_ATTRIBUTE_ROWLIMIT = "rowlimit";
	public static final String STEP_ATTRIBUTE_SQL = "sql";
	public static final String STEP_ATTRIBUTE_OUTER_JOIN = "outer_join";
	public static final String STEP_ATTRIBUTE_REPLACE_VARS = "replace_vars";
	public static final String STEP_ATTRIBUTE_PARAMETER_FIELD = "parameter_field";
	public static final String STEP_ATTRIBUTE_PARAMETER_TYPE = "parameter_type";

	/** database connection */
	private DatabaseMeta databaseMeta;

	/** SQL Statement */
	private String sql;

	/** Number of rows to return (0=ALL) */
	private int rowLimit;

	/**
	 * false: don't return rows where nothing is found true: at least return one
	 * source row, the rest is NULL
	 */
	private boolean outerJoin;

	/** Fields to use as parameters (fill in the ? markers) */
	private String parameterField[];

	/** Type of the paramenters */
	private int parameterType[];

	/**
	 * false: don't replave variable in scrip true: replace variable in script
	 */
	private boolean replacevars;

	public DatabaseJoinMeta() {
		super(); // allocate BaseStepMeta
	}

	/**
	 * @return Returns the database.
	 */
	public DatabaseMeta getDatabaseMeta() {
		return databaseMeta;
	}

	/**
	 * @param database
	 *            The database to set.
	 */
	public void setDatabaseMeta(DatabaseMeta database) {
		this.databaseMeta = database;
	}

	/**
	 * @return Returns the outerJoin.
	 */
	public boolean isOuterJoin() {
		return outerJoin;
	}

	/**
	 * @param outerJoin
	 *            The outerJoin to set.
	 */
	public void setOuterJoin(boolean outerJoin) {
		this.outerJoin = outerJoin;
	}

	/**
	 * @return Returns the replacevars.
	 */
	public boolean isVariableReplace() {
		return replacevars;
	}

	/**
	 * @param replacevars
	 *            The replacevars to set.
	 */
	public void setVariableReplace(boolean replacevars) {
		this.replacevars = replacevars;
	}

	/**
	 * @return Returns the parameterField.
	 */
	public String[] getParameterField() {
		return parameterField;
	}

	/**
	 * @param parameterField
	 *            The parameterField to set.
	 */
	public void setParameterField(String[] parameterField) {
		this.parameterField = parameterField;
	}

	/**
	 * @return Returns the parameterType.
	 */
	public int[] getParameterType() {
		return parameterType;
	}

	/**
	 * @param parameterType
	 *            The parameterType to set.
	 */
	public void setParameterType(int[] parameterType) {
		this.parameterType = parameterType;
	}

	/**
	 * @return Returns the rowLimit.
	 */
	public int getRowLimit() {
		return rowLimit;
	}

	/**
	 * @param rowLimit
	 *            The rowLimit to set.
	 */
	public void setRowLimit(int rowLimit) {
		this.rowLimit = rowLimit;
	}

	/**
	 * @return Returns the sql.
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql
	 *            The sql to set.
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		parameterField = null;
		parameterType = null;
		outerJoin = false;
		replacevars = false;
		readData(stepnode, databases);
	}

	public void allocate(int nrparam) {
		parameterField = new String[nrparam];
		parameterType = new int[nrparam];
	}

	public Object clone() {
		DatabaseJoinMeta retval = (DatabaseJoinMeta) super.clone();

		int nrparam = parameterField.length;

		retval.allocate(nrparam);

		for (int i = 0; i < nrparam; i++) {
			retval.parameterField[i] = parameterField[i];
			retval.parameterType[i] = parameterType[i];
		}

		return retval;
	}

	private void readData(Node stepnode, List<DatabaseMeta> databases)
			throws KettleXMLException {
		try {
			String con = XMLHandler.getTagValue(stepnode, "connection"); //$NON-NLS-1$
			databaseMeta = DatabaseMeta.findDatabase(databases, con);
			sql = XMLHandler.getTagValue(stepnode, "sql"); //$NON-NLS-1$
			outerJoin = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "outer_join")); //$NON-NLS-1$ //$NON-NLS-2$
			replacevars = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"replace_vars"));
			rowLimit = Const.toInt(
					XMLHandler.getTagValue(stepnode, "rowlimit"), 0); //$NON-NLS-1$

			Node param = XMLHandler.getSubNode(stepnode, "parameter"); //$NON-NLS-1$
			int nrparam = XMLHandler.countNodes(param, "field"); //$NON-NLS-1$

			allocate(nrparam);

			for (int i = 0; i < nrparam; i++) {
				Node pnode = XMLHandler.getSubNodeByNr(param, "field", i); //$NON-NLS-1$
				parameterField[i] = XMLHandler.getTagValue(pnode, "name"); //$NON-NLS-1$
				String ptype = XMLHandler.getTagValue(pnode, "type"); //$NON-NLS-1$
				parameterType[i] = ValueMeta.getType(ptype);
			}
		} catch (Exception e) {
			throw new KettleXMLException(
					Messages
							.getString("DatabaseJoinMeta.Exception.UnableToLoadStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void setDefault() {
		databaseMeta = null;
		rowLimit = 0;
		sql = ""; //$NON-NLS-1$
		outerJoin = false;
		parameterField = null;
		parameterType = null;
		outerJoin = false;
		replacevars = false;

		int nrparam = 0;

		allocate(nrparam);

		for (int i = 0; i < nrparam; i++) {
			parameterField[i] = "param" + i; //$NON-NLS-1$
			parameterType[i] = ValueMetaInterface.TYPE_NUMBER;
		}
	}

	public RowMetaInterface getParameterRow(RowMetaInterface fields) {
		RowMetaInterface param = new RowMeta();

		if (fields != null) {
			for (int i = 0; i < parameterField.length; i++) {
				ValueMetaInterface v = fields
						.searchValueMeta(parameterField[i]);
				if (v != null)
					param.addValueMeta(v);
			}
		}
		return param;
	}

	@Override
	public void getFields(RowMetaInterface row, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {

		if (databaseMeta == null)
			return;

		Database db = new Database(databaseMeta);
		databases = new Database[] { db }; // Keep track of this one for
		// cancelQuery

		// Which fields are parameters?
		// info[0] comes from the database connection.
		//
		RowMetaInterface param = getParameterRow(row);

		// First try without connecting to the database... (can be S L O W)
		// See if it's in the cache...
		//
		RowMetaInterface add = null;
		try {
			add = db.getQueryFields(sql, true, param, new Object[param.size()]);
		} catch (KettleDatabaseException dbe) {
			throw new KettleStepException(
					Messages
							.getString("DatabaseJoinMeta.Exception.UnableToDetermineQueryFields") + Const.CR + sql, dbe); //$NON-NLS-1$
		}

		if (add != null) // Cache hit, just return it this...
		{
			for (int i = 0; i < add.size(); i++) {
				ValueMetaInterface v = add.getValueMeta(i);
				v.setOrigin(name);
			}
			row.addRowMeta(add);
		} else

			// No cache hit, connect to the database, do it the hard way...
			//
			try {
				db.connect();
				add = db.getQueryFields(sql, true, param, new Object[param
						.size()]);
				for (int i = 0; i < add.size(); i++) {
					ValueMetaInterface v = add.getValueMeta(i);
					v.setOrigin(name);
				}
				row.addRowMeta(add);
				db.disconnect();
			} catch (KettleDatabaseException dbe) {
				throw new KettleStepException(
						Messages
								.getString("DatabaseJoinMeta.Exception.ErrorObtainingFields"), dbe); //$NON-NLS-1$
			}
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(300);

		retval
				.append("    ").append(XMLHandler.addTagValue("connection", databaseMeta == null ? "" : databaseMeta.getName())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		retval
				.append("    ").append(XMLHandler.addTagValue("rowlimit", rowLimit)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    ").append(XMLHandler.addTagValue("sql", sql)); //$NON-NLS-1$ //$NON-NLS-2$
		retval
				.append("    ").append(XMLHandler.addTagValue("outer_join", outerJoin)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    ").append(
				XMLHandler.addTagValue("replace_vars", replacevars));
		retval.append("    <parameter>").append(Const.CR); //$NON-NLS-1$
		for (int i = 0; i < parameterField.length; i++) {
			retval.append("      <field>").append(Const.CR); //$NON-NLS-1$
			retval
					.append("        ").append(XMLHandler.addTagValue("name", parameterField[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval
					.append("        ").append(XMLHandler.addTagValue("type", ValueMeta.getTypeDesc(parameterType[i]))); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("      </field>").append(Const.CR); //$NON-NLS-1$
		}
		retval.append("    </parameter>").append(Const.CR); //$NON-NLS-1$

		return retval.toString();
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		databaseMeta = DatabaseMeta.findDatabase(
				(List<DatabaseMeta>) databases, BaseStepMeta.parameterToLong(p
						.get(id + ".connectionId")));
		rowLimit = BaseStepMeta.parameterToInt(p.get(id + ".rowLimit"));
		sql = BaseStepMeta.parameterToString(p.get(id + ".sql"));
		outerJoin = BaseStepMeta.parameterToBoolean(p.get(id + ".outerJoin"));
		replacevars = BaseStepMeta.parameterToBoolean(p
				.get(id + ".replacevars"));

		String[] parameterField = p.get(id + "_param.parameterField");
		String[] parameterType = p.get(id + "_param.parameterType");

		this.parameterField = (parameterField != null) ? parameterField : (new String[0]);
		this.parameterType = (parameterType != null) ? stringArrayToIntArray(parameterType, 0) 
				: (new int[0]);
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			long id_connection = rep.getStepAttributeInteger(id_step,
					"ID_CONNECTION"); //$NON-NLS-1$
			databaseMeta = DatabaseMeta.findDatabase(databases, id_connection);
			rowLimit = (int) rep.getStepAttributeInteger(id_step, "ROWLIMIT"); //$NON-NLS-1$
			sql = rep.getStepAttributeString(id_step, "SQL"); //$NON-NLS-1$
			outerJoin = rep.getStepAttributeBoolean(id_step, "OUTER_JOIN"); //$NON-NLS-1$
			replacevars = rep.getStepAttributeBoolean(id_step, "REPLACE_VARS");

			int nrparam = rep.countNrStepAttributes(id_step, "PARAMETER_FIELD"); //$NON-NLS-1$

			allocate(nrparam);

			for (int i = 0; i < nrparam; i++) {
				parameterField[i] = rep.getStepAttributeString(id_step, i,
						"PARAMETER_FIELD"); //$NON-NLS-1$
				String stype = rep.getStepAttributeString(id_step, i,
						"PARAMETER_TYPE"); //$NON-NLS-1$
				parameterType[i] = ValueMeta.getType(stype);
			}
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("DatabaseJoinMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			rep
					.saveStepAttribute(
							id_transformation,
							id_step,
							"ID_CONNECTION", databaseMeta == null ? -1 : databaseMeta.getID()); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step,
					"ROWLIMIT", rowLimit); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "SQL", sql); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step,
					"OUTER_JOIN", outerJoin); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "REPLACE_VARS",
					replacevars);

			for (int i = 0; i < parameterField.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"PARAMETER_FIELD", parameterField[i]); //$NON-NLS-1$
				rep
						.saveStepAttribute(
								id_transformation,
								id_step,
								i,
								"PARAMETER_TYPE", ValueMeta.getTypeDesc(parameterType[i])); //$NON-NLS-1$
			}

			// Also, save the step-database relationship!
			if (databaseMeta != null)
				rep.insertStepDatabase(id_transformation, id_step, databaseMeta
						.getID());
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("DatabaseJoinMeta.Exception.UnableToSaveStepInfo") + id_step, e); //$NON-NLS-1$
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String[] input,
			String[] output, RowMetaInterface info) {

		CheckResult cr;
		String error_message = ""; //$NON-NLS-1$

		if (databaseMeta != null) {
			Database db = new Database(databaseMeta);
			databases = new Database[] { db }; // Keep track of this one for
			// cancelQuery

			try {
				db.connect();
				if (sql != null && sql.length() != 0) {
					RowMetaInterface param = getParameterRow(prev);

					error_message = ""; //$NON-NLS-1$

					RowMetaInterface r = db.getQueryFields(sql, true, param,
							new Object[param.size()]);
					if (r != null) {
						cr = new CheckResult(
								CheckResult.TYPE_RESULT_OK,
								Messages
										.getString("DatabaseJoinMeta.CheckResult.QueryOK"), stepMeta); //$NON-NLS-1$
						remarks.add(cr);
					} else {
						error_message = Messages
								.getString("DatabaseJoinMeta.CheckResult.InvalidDBQuery"); //$NON-NLS-1$
						cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
								error_message, stepMeta);
						remarks.add(cr);
					}

					int q = db.countParameters(sql);
					if (q != parameterField.length) {
						error_message = Messages
								.getString("DatabaseJoinMeta.CheckResult.DismatchBetweenParametersAndQuestion") + Const.CR; //$NON-NLS-1$
						error_message += Messages
								.getString("DatabaseJoinMeta.CheckResult.DismatchBetweenParametersAndQuestion2") + q + Const.CR; //$NON-NLS-1$
						error_message += Messages
								.getString("DatabaseJoinMeta.CheckResult.DismatchBetweenParametersAndQuestion3") + parameterField.length; //$NON-NLS-1$

						cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
								error_message, stepMeta);
						remarks.add(cr);
					} else {
						cr = new CheckResult(
								CheckResult.TYPE_RESULT_OK,
								Messages
										.getString("DatabaseJoinMeta.CheckResult.NumberOfParamCorrect") + q + ")", stepMeta); //$NON-NLS-1$ //$NON-NLS-2$
						remarks.add(cr);
					}
				}

				// Look up fields in the input stream <prev>
				if (prev != null && prev.size() > 0) {
					boolean first = true;
					error_message = ""; //$NON-NLS-1$
					boolean error_found = false;

					for (int i = 0; i < parameterField.length; i++) {
						ValueMetaInterface v = prev
								.searchValueMeta(parameterField[i]);
						if (v == null) {
							if (first) {
								first = false;
								error_message += Messages
										.getString("DatabaseJoinMeta.CheckResult.MissingFields") + Const.CR; //$NON-NLS-1$
							}
							error_found = true;
							error_message += "\t\t" + parameterField[i] + Const.CR; //$NON-NLS-1$
						}
					}
					if (error_found) {
						cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
								error_message, stepMeta);
					} else {
						cr = new CheckResult(
								CheckResult.TYPE_RESULT_OK,
								Messages
										.getString("DatabaseJoinMeta.CheckResult.AllFieldsFound"), stepMeta); //$NON-NLS-1$
					}
					remarks.add(cr);
				} else {
					error_message = Messages
							.getString("DatabaseJoinMeta.CheckResult.CounldNotReadFields") + Const.CR; //$NON-NLS-1$
					cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
							error_message, stepMeta);
					remarks.add(cr);
				}
			} catch (KettleException e) {
				error_message = Messages
						.getString("DatabaseJoinMeta.CheckResult.ErrorOccurred") + e.getMessage(); //$NON-NLS-1$
				cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
						error_message, stepMeta);
				remarks.add(cr);
			} finally {
				db.disconnect();
			}
		} else {
			error_message = Messages
					.getString("DatabaseJoinMeta.CheckResult.InvalidConnection"); //$NON-NLS-1$
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message,
					stepMeta);
			remarks.add(cr);
		}

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_OK,
					Messages
							.getString("DatabaseJoinMeta.CheckResult.ReceivingInfo"), stepMeta); //$NON-NLS-1$
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_ERROR,
					Messages
							.getString("DatabaseJoinMeta.CheckResult.NoInputReceived"), stepMeta); //$NON-NLS-1$
			remarks.add(cr);
		}

	}

	public RowMetaInterface getTableFields() {
		LogWriter log = LogWriter.getInstance();

		// Build a dummy parameter row...
		//
		RowMetaInterface param = new RowMeta();
		for (int i = 0; i < parameterField.length; i++) {
			param.addValueMeta(new ValueMeta(parameterField[i],
					parameterType[i]));
		}

		RowMetaInterface fields = null;
		if (databaseMeta != null) {
			Database db = new Database(databaseMeta);
			databases = new Database[] { db }; // Keep track of this one for
			// cancelQuery

			try {
				db.connect();
				fields = db.getQueryFields(sql, true, param, new Object[param
						.size()]);
			} catch (KettleDatabaseException dbe) {
				log
						.logError(
								toString(),
								Messages
										.getString("DatabaseJoinMeta.Log.DatabaseErrorOccurred") + dbe.getMessage()); //$NON-NLS-1$
			} finally {
				db.disconnect();
			}
		}
		return fields;
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new DatabaseJoin(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData() {
		return new DatabaseJoinData();
	}

	@Override
	public void analyseImpact(List<DatabaseImpact> impact, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String[] input,
			String[] output, RowMetaInterface info) throws KettleStepException {

		// Find the lookupfields...
		//
		RowMetaInterface out = prev.clone();
		getFields(out, stepMeta.getName(), new RowMetaInterface[] { info, },
				null, transMeta);

		if (out != null) {
			for (int i = 0; i < out.size(); i++) {
				ValueMetaInterface outvalue = out.getValueMeta(i);
				DatabaseImpact di = new DatabaseImpact(
						DatabaseImpact.TYPE_IMPACT_READ,
						transMeta.getName(),
						stepMeta.getName(),
						databaseMeta.getDatabaseName(),
						"", //$NON-NLS-1$
						outvalue.getName(),
						outvalue.getName(),
						stepMeta.getName(),
						sql,
						Messages
								.getString("DatabaseJoinMeta.DatabaseImpact.Title") //$NON-NLS-1$
				);
				impact.add(di);

			}
		}
	}

	public DatabaseMeta[] getUsedDatabaseConnections() {
		if (databaseMeta != null) {
			return new DatabaseMeta[] { databaseMeta };
		} else {
			return super.getUsedDatabaseConnections();
		}
	}

	public boolean supportsErrorHandling() {
		return true;
	}

}

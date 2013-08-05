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

package com.panet.imeta.job.entries.setvariables;
import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notNullValidator;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleJobException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.job.entry.validator.ValidatorContext;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;



/**
 * This defines a 'Set variables' job entry.
 *
 * @author Samatar Hassan
 * @since 06-05-2007
 */
public class JobEntrySetVariables extends JobEntryBase implements Cloneable, JobEntryInterface
{
	public static final String ENTRY_ATTRIBUTE_REPLACEVARS = "replacevars";
	public static final String ENTRY_ATTRIBUTE_VARIABLE_NAME = "variable_name";
	public static final String ENTRY_ATTRIBUTE_VARIABLE_VALUE = "variable_value";
	public static final String ENTRY_ATTRIBUTE_VARIABLE_TYPE = "variable_type";
	
	public boolean replaceVars;

	public String variableName[];

	public String variableValue[];

	public int variableType[];

	public static final int VARIABLE_TYPE_JVM             = 0;
	public static final int VARIABLE_TYPE_CURRENT_JOB     = 1;
	public static final int VARIABLE_TYPE_PARENT_JOB 		= 2;
	public static final int VARIABLE_TYPE_ROOT_JOB        = 3;

	
	private static final String variableTypeCode[] = { "JVM", "CURRENT_JOB","PARENT_JOB", "ROOT_JOB" };
	private static final String variableTypeDesc[] = 
	{ 
		Messages.getString("JobEntrySetVariables.VariableType.JVM"),
		Messages.getString("JobEntrySetVariables.VariableType.CurrentJob"),
		Messages.getString("JobEntrySetVariables.VariableType.ParentJob"),
		Messages.getString("JobEntrySetVariables.VariableType.RootJob"),
	};

	public static final String[] fieldTypeDesc = new String[] { "",
		 variableTypeCode[0],variableTypeCode[1],
		 variableTypeCode[2],variableTypeCode[3]
		 
	    	};
	
	public JobEntrySetVariables(String n) {
		super(n, ""); //$NON-NLS-1$
		replaceVars = false;
		variableName = null;
		variableValue=null;

		setID(-1L);
		setJobEntryType(JobEntryType.SET_VARIABLES);
	}

	public JobEntrySetVariables() {
		this(""); //$NON-NLS-1$
	}

	public JobEntrySetVariables(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntrySetVariables je = (JobEntrySetVariables) super.clone();
		return je;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(300);
		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_REPLACEVARS, replaceVars));
		retval.append("      <fields>").append(Const.CR);
		if (variableName != null) {
			for (int i = 0; i < variableName.length; i++) {
				retval.append("        <field>").append(Const.CR); 
				retval.append("          ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_VARIABLE_NAME, variableName[i]));
				retval.append("          ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_VARIABLE_VALUE, variableValue[i]));
				retval.append("          ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_VARIABLE_TYPE, getVariableTypeCode(variableType[i]))); 
				retval.append("        </field>").append(Const.CR);
			}
		}
		retval.append("      </fields>").append(Const.CR); 

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta>  databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	{
		try
		{
			super.loadXML(entrynode, databases, slaveServers);
			replaceVars = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_REPLACEVARS)); 

			Node fields = XMLHandler.getSubNode(entrynode, "fields"); 
			// How many field variableName?
			int nrFields = XMLHandler.countNodes(fields, "field"); 
			variableName = new String[nrFields];
			variableValue = new String[nrFields];
			variableType = new int[nrFields];

			// Read them all...
			for (int i = 0; i < nrFields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i); //$NON-NLS-1$

				variableName[i] = XMLHandler.getTagValue(fnode, ENTRY_ATTRIBUTE_VARIABLE_NAME); //$NON-NLS-1$
				variableValue[i] = XMLHandler.getTagValue(fnode, ENTRY_ATTRIBUTE_VARIABLE_VALUE); //$NON-NLS-1$
				variableType[i] = getVariableType(XMLHandler.getTagValue(fnode, ENTRY_ATTRIBUTE_VARIABLE_TYPE));

			}
		} catch (KettleXMLException xe) {
			throw new KettleXMLException(Messages.getString("JobEntrySetVariables.Meta.UnableLoadXML",xe.getMessage()), xe); //$NON-NLS-1$
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		replaceVars = JobEntryBase.parameterToBoolean(p.get(id + ".replaceVars"));
		//TODO
		String[] variableName = p.get(id + "_variablesGrid.name");
		String[] variableType = p.get(id + "_variablesGrid.type");
		String[] variableValue = p.get(id + "_variablesGrid.value");
		 
		this.variableName = (variableName != null) ? variableName : (new String[0]);
		this.variableType = (variableType != null) ? stringArrayToIntArray(variableType) : (new int[0]);
		this.variableValue = (variableValue != null) ? variableValue : (new String[0]);
	}

	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
	throws KettleException
	{
		try
		{
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			replaceVars = rep.getJobEntryAttributeBoolean(id_jobentry, ENTRY_ATTRIBUTE_REPLACEVARS); //$NON-NLS-1$

			// How many variableName?
			int argnr = rep.countNrJobEntryAttributes(id_jobentry, ENTRY_ATTRIBUTE_VARIABLE_NAME); //$NON-NLS-1$
			variableName = new String[argnr];
			variableValue = new String[argnr];
			variableType = new int[argnr];

			// Read them all...
			for (int a = 0; a < argnr; a++) {
				variableName[a] = rep.getJobEntryAttributeString(id_jobentry, a, ENTRY_ATTRIBUTE_VARIABLE_NAME); //$NON-NLS-1$
				variableValue[a] = rep.getJobEntryAttributeString(id_jobentry, a, ENTRY_ATTRIBUTE_VARIABLE_VALUE); //$NON-NLS-1$
				variableType[a] = getVariableType(rep.getJobEntryAttributeString(id_jobentry, a, ENTRY_ATTRIBUTE_VARIABLE_TYPE)); 
			}
		} catch (KettleException dbe) {
			throw new KettleException(Messages.getString("JobEntrySetVariables.Meta.UnableLoadRep", String.valueOf(id_jobentry),dbe.getMessage()), dbe); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			super.saveRep(rep, id_job);

			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_REPLACEVARS, replaceVars); //$NON-NLS-1$


			// save the variableName...
			if (variableName != null) {
				for (int i = 0; i < variableName.length; i++) {
					rep.saveJobEntryAttribute(id_job, getID(), i, ENTRY_ATTRIBUTE_VARIABLE_NAME, variableName[i]); //$NON-NLS-1$
					rep.saveJobEntryAttribute(id_job, getID(), i, ENTRY_ATTRIBUTE_VARIABLE_VALUE, variableValue[i]); //$NON-NLS-1$
					rep.saveJobEntryAttribute(id_job, getID(), i, ENTRY_ATTRIBUTE_VARIABLE_TYPE,   getVariableTypeCode(variableType[i]));
				}
			}
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					Messages.getString("JobEntrySetVariables.Meta.UnableSaveRep", String.valueOf(id_job),dbe.getMessage()), dbe); //$NON-NLS-1$
		}
	}

	public Result execute(Result result, int nr, Repository rep, Job parentJob) throws KettleException {
		LogWriter log = LogWriter.getInstance();

		result.setResult(false);
		result.setNrErrors(1);
		try{
			if (variableName != null) {

				for (int i = 0; i < variableName.length && !parentJob.isStopped(); i++) {
					String varname=variableName[i];
					String value=variableValue[i];
					if(replaceVars){
						varname=environmentSubstitute(varname);
						value=environmentSubstitute(value);
					}

					// OK, where do we set this value...
					switch(getVariableType()[i]) {
					case VARIABLE_TYPE_JVM:  {
						System.setProperty(varname, value); 
						setVariable(varname, value);
						Job parentjob=parentJob;
						while (parentjob!=null) {                           
							parentjob.setVariable(varname, value);
							parentjob = parentJob.getParentJob();
						}
					}
					break;
					case VARIABLE_TYPE_ROOT_JOB: {
						// set variable in this job entry
						setVariable(varname, value);
						Job rootJob = parentJob;
						while (rootJob!=null)
						{                           
							rootJob.setVariable(varname, value);
							rootJob = rootJob.getParentJob();
						}
					}
					break;
					case VARIABLE_TYPE_CURRENT_JOB: {
						setVariable(varname, value);
						if (parentJob!=null)
							parentJob.setVariable(varname, value);
						else
							throw new KettleJobException(Messages.getString("JobEntrySetVariables.Error.UnableSetVariableCurrentJob",varname));
					}
					break;
					case VARIABLE_TYPE_PARENT_JOB: {
						setVariable(varname, value);

						if (parentJob!=null) {
							parentJob.setVariable(varname, value);
							Job gpJob = parentJob.getParentJob();
							if (gpJob!=null)
								gpJob.setVariable(varname, value);
							else
								throw new KettleJobException(Messages.getString("JobEntrySetVariables.Error.UnableSetVariableParentJob",varname));
						}
						else {
							throw new KettleJobException(Messages.getString("JobEntrySetVariables.Error.UnableSetVariableCurrentJob",varname));
						}  
					}
					break;
					}

					result.setResult(true);
					result.setNrErrors(0);

					// ok we can process this line
					if(log.isDetailed()) 
						log.logDetailed(toString(), Messages.getString("JobEntrySetVariables.Log.SetVariableToValue", varname, value)); //$NON-NLS-1$
				}
			}
		}catch(Exception e)
		{
			log.logError(toString(),Messages.getString("JobEntrySetVariables.UnExcpectedError",e.getMessage()));
		}

		return result;
	}



	public void setReplaceVars(boolean replaceVars) {
		this.replaceVars = replaceVars;
	}

	public boolean evaluates() {
		return true;
	}



	public boolean isReplaceVars()
	{
		return replaceVars;
	}



	public String[] getVariableValue()
	{
		return variableValue;
	}

	/**
	 * @param fieldValue The fieldValue to set.
	 */
	public void setVariableName(String[] fieldValue)
	{
		this.variableName = fieldValue;
	}



	/**
	 * @return Returns the local variable flag: true if this variable is only valid in the parents job.
	 */
	public int[] getVariableType()
	{
		return variableType;
	}

	/**
	 * @param variableType The variable type, see also VARIABLE_TYPE_...
	 * @return the variable type code for this variable type
	 */
	public static final String getVariableTypeCode(int variableType)
	{
		return variableTypeCode[variableType];
	}

	/**
	 * @param variableType The variable type, see also VARIABLE_TYPE_...
	 * @return the variable type description for this variable type
	 */
	public static final String getVariableTypeDescription(int variableType)
	{
		return variableTypeDesc[variableType];
	}

	/**
	 * @param variableType The code or description of the variable type 
	 * @return The variable type
	 */
	public static final int getVariableType(String variableType)
	{
		for (int i=0;i<variableTypeCode.length;i++)
		{
			if (variableTypeCode[i].equalsIgnoreCase(variableType)) return i;
		}
		for (int i=0;i<variableTypeDesc.length;i++)
		{
			if (variableTypeDesc[i].equalsIgnoreCase(variableType)) return i;
		}
		return VARIABLE_TYPE_JVM;
	}

	/**
	 * @param localVariable The localVariable to set.
	 */
	public void setVariableType(int[] localVariable)
	{
		this.variableType = localVariable;
	}

	public static final String[] getVariableTypeDescriptions()
	{
		return variableTypeDesc;
	}
	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {
		boolean res = andValidator().validate(this, "variableName", remarks, putValidators(notNullValidator())); //$NON-NLS-1$

		if (res == false) {
			return;
		}

		ValidatorContext ctx = new ValidatorContext();
		putVariableSpace(ctx, getVariables());
		putValidators(ctx, notNullValidator(), fileExistsValidator());

		for (int i = 0; i < variableName.length; i++) {
			andValidator().validate(this, "variableName[" + i + "]", remarks, ctx); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
		List<ResourceReference> references = super.getResourceDependencies(jobMeta);
		if (variableName != null) {
			ResourceReference reference = null;
			for (int i=0; i<variableName.length; i++) {
				String filename = jobMeta.environmentSubstitute(variableName[i]);
				if (reference == null) {
					reference = new ResourceReference(this);
					references.add(reference);
				}
				reference.getEntries().add( new ResourceEntry(filename, ResourceType.FILE));
			}
		}
		return references;
	}
}

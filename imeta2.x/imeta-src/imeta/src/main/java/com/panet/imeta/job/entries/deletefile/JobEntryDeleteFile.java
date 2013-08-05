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

package com.panet.imeta.job.entries.deletefile;

import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.FileExistsValidator.putFailIfDoesNotExist;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notNullValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.vfs.KettleVFS;
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
import com.panet.imeta.trans.step.BaseStepMeta;

/**
 * This defines a 'delete file' job entry. Its main use would be to delete
 * trigger files, but it will delete any file.
 * 
 * @author Sven Boden
 * @since 10-02-2007
 * 
 */
public class JobEntryDeleteFile extends JobEntryBase implements Cloneable,
		JobEntryInterface {
	private String filename;
	private boolean failIfFileNotExists;

	public JobEntryDeleteFile(String n) {
		super(n, ""); //$NON-NLS-1$
		filename = null;
		failIfFileNotExists = false;
		setID(-1L);
		setJobEntryType(JobEntryType.DELETE_FILE);
	}

	public JobEntryDeleteFile() {
		this(""); //$NON-NLS-1$
	}

	public JobEntryDeleteFile(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntryDeleteFile je = (JobEntryDeleteFile) super.clone();
		return je;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(50);

		retval.append(super.getXML());
		retval
				.append("      ").append(XMLHandler.addTagValue("filename", filename)); //$NON-NLS-1$ //$NON-NLS-2$
		retval
				.append("      ").append(XMLHandler.addTagValue("fail_if_file_not_exists", failIfFileNotExists)); //$NON-NLS-1$ //$NON-NLS-2$

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers, Repository rep)
			throws KettleXMLException {
		try {
			super.loadXML(entrynode, databases, slaveServers);
			filename = XMLHandler.getTagValue(entrynode, "filename"); //$NON-NLS-1$
			failIfFileNotExists = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "fail_if_file_not_exists")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (KettleXMLException xe) {
			throw new KettleXMLException(
					Messages
							.getString("JobEntryDeleteFile.Error_0001_Unable_To_Load_Job_From_Xml_Node"), xe); //$NON-NLS-1$
		}
	}

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			filename = rep.getJobEntryAttributeString(id_jobentry, "filename"); //$NON-NLS-1$
			failIfFileNotExists = rep.getJobEntryAttributeBoolean(id_jobentry,
					"fail_if_file_not_exists"); //$NON-NLS-1$
		} catch (KettleException dbe) {
			throw new KettleException(
					Messages
							.getString(
									"JobEntryDeleteFile.ERROR_0002_Unable_To_Load_From_Repository", Long.toString(id_jobentry)), dbe); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			super.saveRep(rep, id_job);

			rep.saveJobEntryAttribute(id_job, getID(), "filename", filename); //$NON-NLS-1$
			rep.saveJobEntryAttribute(id_job, getID(),
					"fail_if_file_not_exists", failIfFileNotExists); //$NON-NLS-1$
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					Messages
							.getString(
									"JobEntryDeleteFile.ERROR_0003_Unable_To_Save_Job_To_Repository", Long.toString(id_job)), dbe); //$NON-NLS-1$
		}
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public String getRealFilename() {
		return environmentSubstitute(getFilename());
	}

	public Result execute(Result previousResult, int nr, Repository rep,
			Job parentJob) {
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
		result.setResult(false);

		if (filename != null) {
			String realFilename = getRealFilename();

			FileObject fileObject = null;
			try {
				fileObject = KettleVFS.getFileObject(realFilename);

				if (!fileObject.exists()) {
					if (isFailIfFileNotExists()) {
						// File doesn't exist and fail flag is on.
						result.setResult(false);
						log
								.logError(
										toString(),
										Messages
												.getString(
														"JobEntryDeleteFile.ERROR_0004_File_Does_Not_Exist", realFilename)); //$NON-NLS-1$
					} else {
						// File already deleted, no reason to try to delete it
						result.setResult(true);
						if (log.isBasic())
							log
									.logBasic(
											toString(),
											Messages
													.getString(
															"JobEntryDeleteFile.File_Already_Deleted", realFilename)); //$NON-NLS-1$
					}
				} else {
					// Here gc() is explicitly called if e.g. createfile is used
					// in the same
					// job for the same file. The problem is that after creating
					// the file the
					// file object is not properly garbaged collected and thus
					// the file cannot
					// be deleted anymore. This is a known problem in the JVM.
					System.gc();

					boolean deleted = fileObject.delete();
					if (!deleted) {
						log
								.logError(
										toString(),
										Messages
												.getString(
														"JobEntryDeleteFile.ERROR_0005_Could_Not_Delete_File", realFilename)); //$NON-NLS-1$
						result.setResult(false);
						result.setNrErrors(1);
					}
					if (log.isBasic())
						log
								.logBasic(
										toString(),
										Messages
												.getString(
														"JobEntryDeleteFile.File_Deleted", realFilename)); //$NON-NLS-1$
					result.setResult(true);
				}
			} catch (IOException e) {
				log
						.logError(
								toString(),
								Messages
										.getString(
												"JobEntryDeleteFile.ERROR_0006_Exception_Deleting_File", realFilename, e.getMessage())); //$NON-NLS-1$
				result.setResult(false);
				result.setNrErrors(1);
			} finally {
				if (fileObject != null) {
					try {
						fileObject.close();
					} catch (IOException ex) {
					}
					;
				}
			}
		} else {
			log
					.logError(
							toString(),
							Messages
									.getString("JobEntryDeleteFile.ERROR_0007_No_Filename_Is_Defined")); //$NON-NLS-1$
		}

		return result;
	}

	public boolean isFailIfFileNotExists() {
		return failIfFileNotExists;
	}

	public void setFailIfFileNotExists(boolean failIfFileExists) {
		this.failIfFileNotExists = failIfFileExists;
	}

	public boolean evaluates() {
		return true;
	}

	public List<ResourceReference> getResourceDependencies(JobMeta jobMeta) {
		List<ResourceReference> references = super
				.getResourceDependencies(jobMeta);
		if (!Const.isEmpty(filename)) {
			String realFileName = jobMeta.environmentSubstitute(filename);
			ResourceReference reference = new ResourceReference(this);
			reference.getEntries().add(
					new ResourceEntry(realFileName, ResourceType.FILE));
			references.add(reference);
		}
		return references;
	}

	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {
		ValidatorContext ctx = new ValidatorContext();
		putVariableSpace(ctx, getVariables());
		putValidators(ctx, notNullValidator(), fileExistsValidator());
		if (isFailIfFileNotExists()) {
			putFailIfDoesNotExist(ctx, true);
		}
		andValidator().validate(this, "filename", remarks, ctx); //$NON-NLS-1$
	}

	public static void main(String[] args) {
		List<CheckResultInterface> remarks = new ArrayList<CheckResultInterface>();
		new JobEntryDeleteFile().check(remarks, null);
		System.out.printf("Remarks: %s\n", remarks);
	}


	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		this.failIfFileNotExists = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".failIfFileNotExists"));
		this.filename = BaseStepMeta.parameterToString(p.get(id + ".filename"));
	}
}

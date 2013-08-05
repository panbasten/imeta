/**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/

package com.panet.imeta.job.entries.deleteresultfilenames;

import static com.panet.imeta.job.entry.validator.AbstractFileValidator.putVariableSpace;
import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileDoesNotExistValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notNullValidator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.vfs.FileObject;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.ResultFile;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
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
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.step.BaseStepMeta;

/**
 * This defines a 'deleteresultfilenames' job entry. Its main use would be to
 * create empty folder that can be used to control the flow in ETL cycles.
 * 
 * @author Samatar
 * @since 26-10-2007
 * 
 */
public class JobEntryDeleteResultFilenames extends JobEntryBase implements
		Cloneable, JobEntryInterface {
	private String foldername;
	private boolean specifywildcard;
	private String wildcard;
	private String wildcardexclude;

	public JobEntryDeleteResultFilenames(String n) {
		super(n, "");
		foldername = null;
		wildcardexclude = null;
		wildcard = null;
		specifywildcard = false;
		setID(-1L);
		setJobEntryType(JobEntryType.DELETE_RESULT_FILENAMES);
	}

	public JobEntryDeleteResultFilenames() {
		this("");
	}

	public JobEntryDeleteResultFilenames(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntryDeleteResultFilenames je = (JobEntryDeleteResultFilenames) super
				.clone();
		return je;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(50);

		retval.append(super.getXML());
		retval.append("      ").append(
				XMLHandler.addTagValue("foldername", foldername));
		retval.append("      ").append(
				XMLHandler.addTagValue("specify_wildcard", specifywildcard));
		retval.append("      ").append(
				XMLHandler.addTagValue("wildcard", wildcard));
		retval.append("      ").append(
				XMLHandler.addTagValue("wildcardexclude", wildcardexclude));

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers, Repository rep)
			throws KettleXMLException {
		try {
			super.loadXML(entrynode, databases, slaveServers);
			foldername = XMLHandler.getTagValue(entrynode, "foldername");
			specifywildcard = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "specify_wildcard"));
			wildcard = XMLHandler.getTagValue(entrynode, "wildcard");
			wildcardexclude = XMLHandler.getTagValue(entrynode,
					"wildcardexclude");

		} catch (KettleXMLException xe) {
			throw new KettleXMLException(Messages.getString(
					"JobEntryDeleteResultFilenames.CanNotLoadFromXML", xe
							.getMessage()));
		}
	}

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			super.loadRep(rep, id_jobentry, databases, slaveServers);
			foldername = rep.getJobEntryAttributeString(id_jobentry,
					"foldername");
			specifywildcard = rep.getJobEntryAttributeBoolean(id_jobentry,
					"specify_wildcard");
			wildcard = rep.getJobEntryAttributeString(id_jobentry, "wildcard");
			wildcardexclude = rep.getJobEntryAttributeString(id_jobentry,
					"wildcardexclude");

		} catch (KettleException dbe) {
			throw new KettleXMLException(Messages.getString(
					"JobEntryDeleteResultFilenames.CanNotLoadFromRep", ""
							+ id_jobentry, dbe.getMessage()));
		}
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			super.saveRep(rep, id_job);

			rep
					.saveJobEntryAttribute(id_job, getID(), "foldername",
							foldername);
			rep.saveJobEntryAttribute(id_job, getID(), "specify_wildcard",
					specifywildcard);
			rep.saveJobEntryAttribute(id_job, getID(), "wildcard", wildcard);
			rep.saveJobEntryAttribute(id_job, getID(), "wildcardexclude",
					wildcardexclude);

		} catch (KettleDatabaseException dbe) {
			throw new KettleXMLException(Messages.getString(
					"JobEntryDeleteResultFilenames.CanNotSaveToRep", ""
							+ id_job, dbe.getMessage()));
		}
	}

	public void setSpecifyWildcard(boolean specifywildcard) {
		this.specifywildcard = specifywildcard;
	}

	public boolean isSpecifyWildcard() {
		return specifywildcard;
	}

	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}

	public String getFoldername() {
		return foldername;
	}

	public String getWildcard() {
		return wildcard;
	}

	public String getWildcardExclude() {
		return wildcardexclude;
	}

	public String getRealWildcard() {
		return environmentSubstitute(getWildcard());
	}

	public void setWildcard(String wildcard) {
		this.wildcard = wildcard;
	}

	public void setWildcardExclude(String wildcardexclude) {
		this.wildcardexclude = wildcardexclude;
	}

	public Result execute(Result previousResult, int nr, Repository rep,
			Job parentJob) {
		LogWriter log = LogWriter.getInstance();
		Result result = previousResult;
		result.setResult(false);

		if (previousResult != null) {
			try {
				int size = previousResult.getResultFiles().size();
				if (log.isBasic())
					log.logBasic(toString(), Messages.getString(
							"JobEntryDeleteResultFilenames.log.FilesFound", ""
									+ size));
				if (!specifywildcard) {
					// Delete all files
					previousResult.getResultFiles().clear();
					if (log.isDetailed())
						log
								.logDetailed(
										toString(),
										Messages
												.getString(
														"JobEntryDeleteResultFilenames.log.DeletedFiles",
														"" + size));
				} else {

					List<ResultFile> resultFiles = result.getResultFilesList();
					if (resultFiles != null && resultFiles.size() > 0) {
						for (Iterator<ResultFile> it = resultFiles.iterator(); it
								.hasNext()
								&& !parentJob.isStopped();) {
							ResultFile resultFile = (ResultFile) it.next();
							FileObject file = resultFile.getFile();
							if (file != null && file.exists()) {
								if (CheckFileWildcard(file.getName()
										.getBaseName(),
										environmentSubstitute(wildcard), true)
										&& !CheckFileWildcard(
												file.getName().getBaseName(),
												environmentSubstitute(wildcardexclude),
												false)) {
									// Remove file from result files list
									result.getResultFiles().remove(
											resultFile.getFile().toString());

									if (log.isDetailed())
										log
												.logDetailed(
														toString(),
														Messages
																.getString(
																		"JobEntryDeleteResultFilenames.log.DeletedFile",
																		file
																				.toString()));
								}

							}
						}
					}
				}
				result.setResult(true);
			} catch (Exception e) {
				log.logError(toString(), Messages.getString(
						"JobEntryDeleteResultFilenames.Error", e.toString()));
			}
		}
		return result;
	}

	/***************************************************************************
	 * 
	 * @param selectedfile
	 * @param wildcard
	 * @return True if the selectedfile matches the wildcard
	 **************************************************************************/
	private boolean CheckFileWildcard(String selectedfile, String wildcard,
			boolean include) {
		Pattern pattern = null;
		boolean getIt = include;

		if (!Const.isEmpty(wildcard)) {
			pattern = Pattern.compile(wildcard);
			// First see if the file matches the regular expression!
			if (pattern != null) {
				Matcher matcher = pattern.matcher(selectedfile);
				getIt = matcher.matches();
			}
		}

		return getIt;
	}

	public boolean evaluates() {
		return true;
	}

	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {
		ValidatorContext ctx = new ValidatorContext();
		putVariableSpace(ctx, getVariables());
		putValidators(ctx, notNullValidator(), fileDoesNotExistValidator());
		andValidator().validate(this, "filename", remarks, ctx); //$NON-NLS-1$
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		this.specifywildcard = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".specifywildcard"));
		this.wildcard = BaseStepMeta.parameterToString(p.get(id + ".wildcard"));
		this.wildcardexclude = BaseStepMeta.parameterToString(p.get(id
				+ ".wildcardexclude"));
	}

}

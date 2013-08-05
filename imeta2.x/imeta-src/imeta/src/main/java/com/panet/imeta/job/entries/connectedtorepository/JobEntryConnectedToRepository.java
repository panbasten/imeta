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

package com.panet.imeta.job.entries.connectedtorepository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
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
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.shared.SharedObjectInterface;

/**
 * Job entry connected to repositoryb.
 * 
 * @author Samatar
 * @since 23-06-2008
 */
public class JobEntryConnectedToRepository extends JobEntryBase implements
		Cloneable, JobEntryInterface {
	
	public static final String ENTRY_ATTRIBUTE_ISSPECIFICREP = "isspecificrep";
	public static final String ENTRY_ATTRIBUTE_REPNAME = "repname";
	public static final String ENTRY_ATTRIBUTE_ISSPECIFICUSER = "isspecificuser";
	public static final String ENTRY_ATTRIBUTE_USERNAME = "username";
	
	private boolean isspecificrep;
	private String repname;
	private boolean isspecificuser;
	private String username;

	public JobEntryConnectedToRepository(String n, String scr) {
		super(n, "");
		isspecificrep = false;
		repname = null;
		isspecificuser = false;
		username = null;
		setJobEntryType(JobEntryType.CONNECTED_TO_REPOSITORY);
	}

	public JobEntryConnectedToRepository() {
		this("", "");
	}

	public void setSpecificRep(boolean isspecificrep) {
		this.isspecificrep = isspecificrep;
	}

	public String getRepName() {
		return repname;
	}

	public void setRepName(String repname) {
		this.repname = repname;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public boolean isSpecificRep() {
		return isspecificrep;
	}

	public boolean isSpecificUser() {
		return isspecificuser;
	}

	public void setSpecificUser(boolean isspecificuser) {
		this.isspecificuser = isspecificuser;
	}

	public JobEntryConnectedToRepository(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntryConnectedToRepository je = (JobEntryConnectedToRepository) super
				.clone();
		return je;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();
		retval.append("      ").append(
				XMLHandler.addTagValue("isspecificrep", isspecificrep));
		retval.append("      ").append(
				XMLHandler.addTagValue("repname", repname));
		retval.append("      ").append(
				XMLHandler.addTagValue("isspecificuser", isspecificuser));
		retval.append("      ").append(
				XMLHandler.addTagValue("username", username));

		retval.append(super.getXML());

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers, Repository rep)
			throws KettleXMLException {
		try {
			super.loadXML(entrynode, databases, slaveServers);
			isspecificrep = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "isspecificrep"));
			repname = XMLHandler.getTagValue(entrynode, "repname");
			isspecificuser = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					entrynode, "isspecificuser"));
			username = XMLHandler.getTagValue(entrynode, "username");

		} catch (Exception e) {
			throw new KettleXMLException(
					Messages
							.getString("JobEntryConnectedToRepository.Meta.UnableToLoadFromXML"),
					e);
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		
		this.isspecificrep = JobEntryBase.parameterToBoolean(p.get(id+ ".isspecificrep"));
		this.repname = JobEntryBase.parameterToString(p.get(id + ".repname"));
		this.isspecificuser = JobEntryBase.parameterToBoolean(p.get(id+ ".isspecificuser"));
		this.username = JobEntryBase.parameterToString(p.get(id + ".username"));
	}

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			super.loadRep(rep, id_jobentry, databases, slaveServers);

			isspecificrep = rep.getJobEntryAttributeBoolean(id_jobentry,ENTRY_ATTRIBUTE_ISSPECIFICREP);
			repname = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_REPNAME);
			isspecificuser = rep.getJobEntryAttributeBoolean(id_jobentry,ENTRY_ATTRIBUTE_ISSPECIFICUSER);
			username = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_USERNAME);

		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					Messages
							.getString("JobEntryConnectedToRepository.Meta.UnableToLoadFromRep")
							+ id_jobentry, dbe);

		}
	}

	// Save the attributes of this job entry
	//
	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			super.saveRep(rep, id_job);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ISSPECIFICREP,isspecificrep);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_REPNAME, repname);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ISSPECIFICUSER,isspecificuser);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_USERNAME, username);

		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					Messages
							.getString("JobEntryConnectedToRepository.Meta.UnableToSaveToRep")
							+ id_job, dbe);
		}
	}

	/**
	 * Execute this job entry and return the result. In this case it means, just
	 * set the result boolean in the Result class.
	 * 
	 * @param previousResult
	 *            The result of the previous execution
	 * @return The Result of the execution.
	 */
	public Result execute(Result previousResult, int nr, Repository rep,
			Job parentJob) {
		Result result = previousResult;
		result.setNrErrors(1);
		result.setResult(false);
		LogWriter log = LogWriter.getInstance();

		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfo userInfo = (UserInfo) request.getSession(false).getAttribute(
				UserInfo.STRING_USERINFO);
		String login = (userInfo != null) ? userInfo.getLogin() : "";

		if (rep == null) {
			log
					.logError(
							toString(),
							Messages
									.getString("JobEntryConnectedToRepository.Log.NotConnected"));
			return result;
		}
		if (isspecificrep) {
			if (Const.isEmpty(repname)) {
				log
						.logError(
								toString(),
								Messages
										.getString("JobEntryConnectedToRepository.Error.NoRep"));
				return result;
			}
			String Reponame = environmentSubstitute(repname);
			if (!Reponame.equals(rep.getName())) {
				log.logError(toString(), Messages.getString(
						"JobEntryConnectedToRepository.Error.DiffRep", rep
								.getName(), Reponame));
				return result;
			}
		}
		if (isspecificuser) {
			if (Const.isEmpty(this.username)) {
				log
						.logError(
								toString(),
								Messages
										.getString("JobEntryConnectedToRepository.Error.NoUser"));
				return result;
			}
			String username = environmentSubstitute(this.username);

			if (!username.equals(login)) {
				log.logError(toString(), Messages.getString(
						"JobEntryConnectedToRepository.Error.DiffUser", login,
						username));
				return result;
			}
		}

		if (log.isDetailed())
			log.logDetailed(toString(), Messages.getString(
					"JobEntryConnectedToRepository.Log.Connected", rep
							.getName(), login));

		result.setResult(true);
		result.setNrErrors(0);

		return result;
	}

	public boolean evaluates() {
		return true;
	}

	public boolean isUnconditional() {
		return false;
	}

	@Override
	public void check(List<CheckResultInterface> remarks, JobMeta jobMeta) {

	}

}

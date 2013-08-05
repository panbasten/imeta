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
package com.panet.imeta.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.variables.Variables;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.RepositoriesMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryMeta;
import com.panet.imeta.repository.UserInfo;

public class JobExecutionConfiguration implements Cloneable {
	public static final String XML_TAG = "job_execution_configuration";

	private boolean executingLocally;

	private boolean executingRemotely;
	private SlaveServer remoteServer;

	private Map<String, String> arguments;
	private Map<String, String> variables;

	private Date replayDate;
	private boolean safeModeEnabled;
	private int logLevel;
	private boolean clearingLog;

	private Result previousResult;
	private Repository repository;

	public JobExecutionConfiguration() {
		executingLocally = true;
		executingRemotely = false;

		arguments = new HashMap<String, String>();
		variables = new HashMap<String, String>();

		logLevel = LogWriter.LOG_LEVEL_BASIC;

		clearingLog = true;
	}

	public Object clone() {
		try {
			JobExecutionConfiguration configuration = (JobExecutionConfiguration) super
					.clone();

			configuration.arguments = new HashMap<String, String>();
			configuration.arguments.putAll(arguments);

			configuration.variables = new HashMap<String, String>();
			configuration.variables.putAll(variables);

			if (previousResult != null) {
				configuration.previousResult = previousResult.clone();
			}

			return configuration;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/**
	 * @return the arguments
	 */
	public Map<String, String> getArguments() {
		return arguments;
	}

	/**
	 * @param arguments
	 *            the arguments to set
	 */
	public void setArguments(Map<String, String> arguments) {
		this.arguments = arguments;
	}

	/**
	 * @param arguments
	 *            the arguments to set
	 */
	public void setArgumentStrings(String[] arguments) {
		this.arguments = new HashMap<String, String>();
		if (arguments != null) {
			for (int i = 0; i < arguments.length; i++) {
				this.arguments.put("arg " + (i + 1), arguments[i]);
			}
		}
	}

	/**
	 * @return the variables
	 */
	public Map<String, String> getVariables() {
		return variables;
	}

	/**
	 * @param variables
	 *            the variables to set
	 */
	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

	public void setVariables(VariableSpace space) {
		this.variables = new HashMap<String, String>();

		for (String name : space.listVariables()) {
			String value = space.getVariable(name);
			this.variables.put(name, value);
		}
	}

	/**
	 * @return the remoteExecution
	 */
	public boolean isExecutingRemotely() {
		return executingRemotely;
	}

	/**
	 * @param remoteExecution
	 *            the remoteExecution to set
	 */
	public void setExecutingRemotely(boolean remoteExecution) {
		this.executingRemotely = remoteExecution;
	}

	/**
	 * @return the localExecution
	 */
	public boolean isExecutingLocally() {
		return executingLocally;
	}

	/**
	 * @param localExecution
	 *            the localExecution to set
	 */
	public void setExecutingLocally(boolean localExecution) {
		this.executingLocally = localExecution;
	}

	/**
	 * @return the remoteServer
	 */
	public SlaveServer getRemoteServer() {
		return remoteServer;
	}

	/**
	 * @param remoteServer
	 *            the remoteServer to set
	 */
	public void setRemoteServer(SlaveServer remoteServer) {
		this.remoteServer = remoteServer;
	}

	public void getUsedVariables(JobMeta jobMeta) {
		Properties sp = new Properties();
		VariableSpace space = Variables.getADefaultVariableSpace();

		String keys[] = space.listVariables();
		for (int i = 0; i < keys.length; i++) {
			sp.put(keys[i], space.getVariable(keys[i]));
		}

		List<String> vars = jobMeta.getUsedVariables();
		if (vars != null && vars.size() > 0) {
			HashMap<String, String> newVariables = new HashMap<String, String>();

			for (int i = 0; i < vars.size(); i++) {
				String varname = (String) vars.get(i);
				if (!varname.startsWith(Const.INTERNAL_VARIABLE_PREFIX)) {
					newVariables.put(varname, Const.NVL(variables.get(varname),
							sp.getProperty(varname, "")));
				}
			}
			// variables.clear();
			variables.putAll(newVariables);
		}
	}

	/**
	 * @return the replayDate
	 */
	public Date getReplayDate() {
		return replayDate;
	}

	/**
	 * @param replayDate
	 *            the replayDate to set
	 */
	public void setReplayDate(Date replayDate) {
		this.replayDate = replayDate;
	}

	/**
	 * @return the usingSafeMode
	 */
	public boolean isSafeModeEnabled() {
		return safeModeEnabled;
	}

	/**
	 * @param usingSafeMode
	 *            the usingSafeMode to set
	 */
	public void setSafeModeEnabled(boolean usingSafeMode) {
		this.safeModeEnabled = usingSafeMode;
	}

	/**
	 * @return the logLevel
	 */
	public int getLogLevel() {
		return logLevel;
	}

	/**
	 * @param logLevel
	 *            the logLevel to set
	 */
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public String getXML() throws IOException {
		StringBuffer xml = new StringBuffer(160);

		xml.append("  <" + XML_TAG + ">").append(Const.CR);

		xml.append("    ").append(
				XMLHandler.addTagValue("exec_local", executingLocally));

		xml.append("    ").append(
				XMLHandler.addTagValue("exec_remote", executingRemotely));
		if (remoteServer != null) {
			xml.append("    ").append(remoteServer.getXML()).append(Const.CR);
		}

		// Serialize the variables...
		//
		xml.append("    <variables>").append(Const.CR);
		List<String> variableNames = new ArrayList<String>(variables.keySet());
		Collections.sort(variableNames);
		for (String name : variableNames) {
			String value = variables.get(name);
			xml.append("    <variable>");
			xml.append(XMLHandler.addTagValue("name", name, false));
			xml.append(XMLHandler.addTagValue("value", value, false));
			xml.append("</variable>").append(Const.CR);
		}
		xml.append("    </variables>").append(Const.CR);

		// Serialize the variables...
		//
		xml.append("    <arguments>").append(Const.CR);
		List<String> argumentNames = new ArrayList<String>(arguments.keySet());
		Collections.sort(argumentNames);
		for (String name : argumentNames) {
			String value = arguments.get(name);
			xml.append("    <argument>");
			xml.append(XMLHandler.addTagValue("name", name, false));
			xml.append(XMLHandler.addTagValue("value", value, false));
			xml.append("</argument>").append(Const.CR);
		}
		xml.append("    </arguments>").append(Const.CR);

		xml.append("    ").append(
				XMLHandler.addTagValue("replay_date", replayDate));
		xml.append("    ").append(
				XMLHandler.addTagValue("safe_mode", safeModeEnabled));
		xml.append("    ").append(
				XMLHandler.addTagValue("log_level", LogWriter
						.getLogLevelDesc(logLevel)));
		xml.append("    ").append(
				XMLHandler.addTagValue("clear_log", clearingLog));

		// The source rows...
		//
		if (previousResult != null) {
			xml.append(previousResult.getXML());
		}

		// Send the repository name and user to the remote site...
		//
		if (repository != null) {
			HttpServletRequest request = ServletActionContext.getRequest();
			UserInfo userInfo = (UserInfo) request.getSession(false)
					.getAttribute(UserInfo.STRING_USERINFO);
			xml.append(XMLHandler.openTag("repository"));
			xml.append(XMLHandler.addTagValue("name", repository.getName()));
			xml.append(XMLHandler.addTagValue("login",
					(userInfo != null) ? userInfo.getLogin() : ""));
			xml.append(XMLHandler.addTagValue("password", Encr
					.encryptPassword((userInfo != null) ? userInfo
							.getPassword() : "")));
			xml.append(XMLHandler.closeTag("repository"));
		}

		xml.append("</" + XML_TAG + ">").append(Const.CR);
		return xml.toString();
	}

	public JobExecutionConfiguration(Node trecNode) throws KettleException {
		this();

		executingLocally = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
				trecNode, "exec_local"));

		executingRemotely = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
				trecNode, "exec_remote"));
		Node remoteHostNode = XMLHandler.getSubNode(trecNode,
				SlaveServer.XML_TAG);
		if (remoteHostNode != null) {
			remoteServer = new SlaveServer(remoteHostNode);
		}

		// Read the variables...
		//
		Node varsNode = XMLHandler.getSubNode(trecNode, "variables");
		int nrVariables = XMLHandler.countNodes(varsNode, "variable");
		for (int i = 0; i < nrVariables; i++) {
			Node argNode = XMLHandler.getSubNodeByNr(varsNode, "variable", i);
			String name = XMLHandler.getTagValue(argNode, "name");
			String value = XMLHandler.getTagValue(argNode, "value");
			if (!Const.isEmpty(name) && !Const.isEmpty(value)) {
				variables.put(name, value);
			}
		}

		// Read the arguments...
		//
		Node argsNode = XMLHandler.getSubNode(trecNode, "arguments");
		int nrArguments = XMLHandler.countNodes(argsNode, "argument");
		for (int i = 0; i < nrArguments; i++) {
			Node argNode = XMLHandler.getSubNodeByNr(argsNode, "argument", i);
			String name = XMLHandler.getTagValue(argNode, "name");
			String value = XMLHandler.getTagValue(argNode, "value");
			if (!Const.isEmpty(name) && !Const.isEmpty(value)) {
				arguments.put(name, value);
			}
		}

		replayDate = XMLHandler.stringToDate(XMLHandler.getTagValue(trecNode,
				"replay_date"));
		safeModeEnabled = "Y".equalsIgnoreCase(XMLHandler.getTagValue(trecNode,
				"safe_mode"));
		logLevel = LogWriter.getLogLevel(XMLHandler.getTagValue(trecNode,
				"log_level"));
		clearingLog = "Y".equalsIgnoreCase(XMLHandler.getTagValue(trecNode,
				"clear_log"));

		Node resultNode = XMLHandler.getSubNode(trecNode, Result.XML_TAG);
		if (resultNode != null) {
			try {
				previousResult = new Result(resultNode);
			} catch (IOException e) {
				LogWriter.getInstance().logError("Job execution configuration",
						"Unable to hydrate previous result", e);
			}
		}

		// Try to get a handle to the repository from here...
		//
		Node repNode = XMLHandler.getSubNode(trecNode, "repository");
		if (repNode != null) {
			String repositoryName = XMLHandler.getTagValue(repNode, "name");
			String username = XMLHandler.getTagValue(repNode, "login");
			String password = Encr.decryptPassword(XMLHandler.getTagValue(
					repNode, "password"));

			// Verify that the repository exists on the slave server...
			//
			RepositoriesMeta repositoriesMeta = new RepositoriesMeta(LogWriter
					.getInstance());
			if (!repositoriesMeta.readData()) {
				throw new KettleException(
						"Unable to get a list of repositories to locate repository '"
								+ repositoryName + "'");
			}
			RepositoryMeta repositoryMeta = repositoriesMeta
					.findRepository(repositoryName);
			if (repositoryMeta == null) {
				throw new KettleException(
						"I couldn't find the repository with name '"
								+ repositoryName + "'");
			}
			Repository rep = new Repository(LogWriter.getInstance(),
					repositoryMeta);
			if (!rep.connect("Job execution configuration")) {
				throw new KettleException(
						"Unable to connect to the repository with name '"
								+ repositoryName + "'");
			}
			UserInfo userInfo = new UserInfo(rep, username, password);
			if (userInfo.getID() <= 0) {
				rep.disconnect();
				throw new KettleException("Unable to verify username '"
						+ username
						+ "' credentials for the repository with name '"
						+ repositoryName + "'");
			}

			// Confirmed access:
			//
			repository = rep;
		}

	}

	public String[] getArgumentStrings() {
		if (arguments == null || arguments.size() == 0)
			return null;

		String[] argNames = arguments.keySet().toArray(
				new String[arguments.size()]);
		Arrays.sort(argNames);

		String[] values = new String[argNames.length];
		for (int i = 0; i < argNames.length; i++) {
			values[i] = arguments.get(argNames[i]);
		}

		return values;
	}

	/**
	 * @return the previousResult
	 */
	public Result getPreviousResult() {
		return previousResult;
	}

	/**
	 * @param previousResult
	 *            the previousResult to set
	 */
	public void setPreviousResult(Result previousResult) {
		this.previousResult = previousResult;
	}

	/**
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 *            the repository to set
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	/**
	 * @return the clearingLog
	 */
	public boolean isClearingLog() {
		return clearingLog;
	}

	/**
	 * @param clearingLog
	 *            the clearingLog to set
	 */
	public void setClearingLog(boolean clearingLog) {
		this.clearingLog = clearingLog;
	}

}

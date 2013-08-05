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

package com.panet.imeta.repository;

import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;

/*
 * Created on 7-apr-2004
 * 
 */

public class UserInfo {
	public static final String STRING_USERINFO = "UserInfo";
	
	public static final String STRING_DEFAULT_USER_LOGIN = "admin";
	
	public static final String STRING_USER_TYPE_ADMIN = "admin";
	public static final String STRING_USER_TYPE_EDITOR = "editor";
	public static final String STRING_USER_TYPE_OPERATOR = "operator";

	private long id;

	private String login; // Login ID
	private String password; // Password
	private String name; // Long name
	private String description; // Description
	private boolean enabled; // Enabled: yes or no

	private ProfileMeta profile; // user profile information

	private Repository rep;
	
	private String accountType;

	private Database database = null;

	private PreparedStatement psStepAttributesLookup = null;
	private PreparedStatement psStepAttributesInsert = null;
	private PreparedStatement psTransAttributesLookup = null;
	private PreparedStatement psTransAttributesInsert = null;
	private PreparedStatement pstmt_entry_attributes = null;

	public PreparedStatement getPsStepAttributesLookup() {
		return psStepAttributesLookup;
	}

	public void setPsStepAttributesLookup(
			PreparedStatement psStepAttributesLookup) {
		this.psStepAttributesLookup = psStepAttributesLookup;
	}

	public PreparedStatement getPsStepAttributesInsert() {
		return psStepAttributesInsert;
	}

	public void setPsStepAttributesInsert(
			PreparedStatement psStepAttributesInsert) {
		this.psStepAttributesInsert = psStepAttributesInsert;
	}

	public PreparedStatement getPsTransAttributesLookup() {
		return psTransAttributesLookup;
	}

	public void setPsTransAttributesLookup(
			PreparedStatement psTransAttributesLookup) {
		this.psTransAttributesLookup = psTransAttributesLookup;
	}

	public PreparedStatement getPsTransAttributesInsert() {
		return psTransAttributesInsert;
	}

	public void setPsTransAttributesInsert(
			PreparedStatement psTransAttributesInsert) {
		this.psTransAttributesInsert = psTransAttributesInsert;
	}

	public PreparedStatement getPstmt_entry_attributes() {
		return pstmt_entry_attributes;
	}

	public void setPstmt_entry_attributes(
			PreparedStatement pstmt_entry_attributes) {
		this.pstmt_entry_attributes = pstmt_entry_attributes;
	}

	public UserInfo(String login, String password, String name,
			String description, String accountType, boolean enabled, ProfileMeta profile) {
		this.login = login;
		this.password = password;
		this.name = name;
		this.description = description;
		this.enabled = enabled;
		this.profile = profile;
		this.accountType = accountType;
	}

	public UserInfo() {
		this.login = null;
		this.password = null;
		this.name = null;
		this.description = null;
		this.enabled = true;
		this.profile = null;
		this.accountType = null;
	}

	public static UserInfo getLoginUser() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			UserInfo userInfo = (UserInfo) request.getSession(false)
					.getAttribute(UserInfo.STRING_USERINFO);
			return userInfo;
		} catch (Exception ex) {
			return null;
		}
	}

	// Load user with login from repository, don't verify password...
	public UserInfo(Repository rep, String login) throws KettleException {
		try {
			this.rep = rep;
			long id_profile;

			setID(rep.getUserID(login));
			if (getID() > 0) {
				RowMetaAndData r = rep.getUser(getID());
				if (r != null) {
					this.login = r.getString("LOGIN", null);
					password = Encr.decryptPassword(r.getString("PASSWORD",
							null));
					name = r.getString("NAME", null);
					description = r.getString("DESCRIPTION", null);
					accountType = r.getString("ACCOUNTTYPE", null);
					enabled = r.getBoolean("ENABLED", false);
					id_profile = r.getInteger("ID_PROFILE", 0);
					profile = new ProfileMeta(rep, id_profile);
				} else {
					setID(-1L);
					throw new KettleDatabaseException(Messages.getString(
							"UserInfo.Error.UserNotFound", login));
				}
			} else {
				setID(-1L);
				throw new KettleDatabaseException(Messages.getString(
						"UserInfo.Error.UserNotFound", login));
			}

			// 初始化用户的一些属性
			this.database = new Database(rep.getDatabaseMeta());
			this.database.connect();
			this.database.setAutoCommit(false);
			this.database.setCommit(99999999);
			this.psStepAttributesLookup = rep.setLookupStepAttribute();
			this.psTransAttributesLookup = rep.setLookupTransAttribute();
			this.pstmt_entry_attributes = rep.setLookupJobEntryAttribute();

		} catch (KettleDatabaseException dbe) {
			rep.log.logError(toString(), Messages.getString(
					"UserInfo.Error.UserNotLoaded", login, dbe.getMessage()));
			throw new KettleException(Messages.getString(
					"UserInfo.Error.UserNotLoaded", login, rep.getName()), dbe);
		}
	}

	// Load user with login from repository and verify the password...
	public UserInfo(Repository rep, String login, String passwd)
			throws KettleException {
		this(rep, login);

		// Verify the password:
		if (getID() < 0 || !passwd.equals(getPassword())) {
			throw new KettleDatabaseException(Messages
					.getString("UserInfo.Error.IncorrectPasswortLogin"));
		}

	}

	public void saveRep() throws KettleException {
		try {
			// Do we have a user id already?
			if (getID() <= 0) {
				setID(rep.getUserID(login)); // Get userid in the repository
			}

			if (getID() <= 0) {
				// This means the login doesn't exist in the database
				// and we have no id, so we don't know the old one...
				// Just grab the next user ID and do an insert:
				setID(rep.getNextUserID());
				rep.insertTableRow("R_USER", fillTableRow());
			} else {
				rep.updateTableRow("R_USER", "ID_USER", fillTableRow());
			}
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(Messages.getString(
					"UserInfo.Error.SavingUser", login), dbe);
		}

	}

	protected void finalize() {
		this.finalize();
		try {
			logout();
		} catch (KettleException e) {
			e.printStackTrace();
		}
	}

	public void logout() throws KettleException {
		try {
			if (rep != null) {
				// 提交
				if (!this.database.isAutoCommit()) {
					rep.commit();
				}
				// 关闭用户的语句
				this.database.closePreparedStatement(psTransAttributesLookup);
				this.database.closePreparedStatement(psTransAttributesInsert);
				this.database.closePreparedStatement(psStepAttributesLookup);
				this.database.closePreparedStatement(psStepAttributesInsert);
				this.database.closePreparedStatement(pstmt_entry_attributes);
				psTransAttributesLookup = null;
				psTransAttributesInsert = null;
				psStepAttributesLookup = null;
				psStepAttributesInsert = null;
				pstmt_entry_attributes = null;
				
				// 关闭数据库连接
				this.database.disconnect();
				this.database = null;
			}
		} catch (KettleException e) {
			throw new KettleException(Messages.getString(
					"UserInfo.Error.LogoutUser", login), e);
		}
	}

	public RowMetaAndData fillTableRow() {
		RowMetaAndData r = new RowMetaAndData();
		r.addValue(new ValueMeta("ID_USER", ValueMetaInterface.TYPE_INTEGER),
				new Long(getID()));
		r.addValue(new ValueMeta("LOGIN", ValueMetaInterface.TYPE_STRING),
				login);
		r.addValue(new ValueMeta("PASSWORD", ValueMetaInterface.TYPE_STRING),
				Encr.encryptPassword(password));
		r.addValue(new ValueMeta("NAME", ValueMetaInterface.TYPE_STRING), name);
		r.addValue(
				new ValueMeta("DESCRIPTION", ValueMetaInterface.TYPE_STRING),
				description);
		r.addValue(
				new ValueMeta("ACCOUNTTYPE", ValueMetaInterface.TYPE_STRING),
				accountType);
		r.addValue(new ValueMeta("ENABLED", ValueMetaInterface.TYPE_BOOLEAN),
				Boolean.valueOf(enabled));
		r.addValue(
				new ValueMeta("ID_PROFILE", ValueMetaInterface.TYPE_INTEGER),
				Long.valueOf(profile.getID()));

		return r;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setEnabled() {
		setEnabled(true);
	}

	public void setDisabled() {
		setEnabled(false);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setProfile(ProfileMeta profile) {
		this.profile = profile;
	}

	public ProfileMeta getProfile() {
		return profile;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	// Helper functions...

	public boolean isReadonly() {
		if (profile == null)
			return true;
		return profile.isReadonly();
	}

	public boolean isAdministrator() {
		if (profile == null)
			return false;
		return profile.isAdministrator();
	}

	public boolean useTransformations() {
		if (profile == null)
			return false;
		return profile.useTransformations();
	}

	public boolean useJobs() {
		if (profile == null)
			return false;
		return profile.useJobs();
	}

	public boolean useSchemas() {
		if (profile == null)
			return false;
		return profile.useSchemas();
	}

	public Repository getRep() {
		return rep;
	}

	public void setRep(Repository rep) {
		this.rep = rep;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	
}

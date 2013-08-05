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
package com.panet.imeta.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Contains some common object details, extracted from a repository
 * 
 * @author Matt
 */
public class RepositoryObject {

	public static final String STRING_OBJECT_TYPE_TRANSFORMATION = "Transformation";
	public static final String STRING_OBJECT_TYPE_JOB = "Job";

	public static final String STRING_OBJECT_TYPE_TRANSFORMATION_DESP = Messages
			.getString("Repository.ObjectType.Transformation");
	public static final String STRING_OBJECT_TYPE_JOB_DESP = Messages
			.getString("Repository.ObjectType.Job");

	public static final String[] STRING_OBJECT_TYPES = new String[] {
			STRING_OBJECT_TYPE_TRANSFORMATION, STRING_OBJECT_TYPE_JOB };
	public static final String[] STRING_OBJECT_TYPES_DESP = new String[] {
			STRING_OBJECT_TYPE_TRANSFORMATION_DESP, STRING_OBJECT_TYPE_JOB_DESP };

	public static final String STRING_ELEMENT_TYPE_DATABASE = "database";
	public static final String STRING_ELEMENT_TYPE_STEP = "step";
	public static final String STRING_ELEMENT_TYPE_JOBENTRY = "jobentry";

	private long repId;
	private String name;
	private String createdUser;
	private Date createdDate;
	private String modifiedUser;
	private Date modifiedDate;
	private String objectType;
	private String description;

	public RepositoryObject() {
	}

	public RepositoryObject(long repId, String name, String createdUser,
			Date createdDate, String modifiedUser, Date modifiedDate,
			String objectType, String description) {
		this();
		this.repId = repId;
		this.name = name;
		this.createdUser = createdUser;
		this.createdDate = createdDate;
		this.modifiedUser = modifiedUser;
		this.modifiedDate = modifiedDate;
		this.objectType = objectType;
		this.description = description;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the modifiedUser
	 */
	public String getModifiedUser() {
		return modifiedUser;
	}

	/**
	 * @param modifiedUser
	 *            the modifiedUser to set
	 */
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public static final int compareStrings(String one, String two) {
		if (one == null && two == null)
			return 0;
		if (one == null && two != null)
			return -1;
		if (one != null && two == null)
			return 1;
		return one.compareToIgnoreCase(two);
	}

	public static final int compareDates(Date one, Date two) {
		if (one == null && two == null)
			return 0;
		if (one == null && two != null)
			return -1;
		if (one != null && two == null)
			return 1;
		return one.compareTo(two);
	}

	public static final void sortRepositoryObjects(
			List<RepositoryObject> objects, final int sortPosition,
			final boolean ascending) {
		Collections.sort(objects, new Comparator<RepositoryObject>() {
			public int compare(RepositoryObject r1, RepositoryObject r2) {
				int result = 0;

				switch (sortPosition) {
				case 0:
					result = compareStrings(r1.getName(), r2.getName());
					break;
				case 1:
					result = compareStrings(r1.getObjectType(), r2
							.getObjectType());
					break;
				case 2:
					result = compareStrings(r1.getModifiedUser(), r2
							.getModifiedUser());
					break;
				case 3:
					result = compareDates(r1.getModifiedDate(), r2
							.getModifiedDate());
					break;
				case 4:
					result = compareStrings(r1.getDescription(), r2
							.getDescription());
					break;
				}

				if (!ascending)
					result *= -1;

				return result;
			}
		});
	}

	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType
	 *            the objectType to set
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public long getRepId() {
		return repId;
	}

	public void setRepId(long repId) {
		this.repId = repId;
	}

}
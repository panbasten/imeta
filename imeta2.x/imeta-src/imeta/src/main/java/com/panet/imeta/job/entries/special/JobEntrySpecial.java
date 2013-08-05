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

package com.panet.imeta.job.entries.special;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleJobException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;

/**
 * This class can contain a few special job entries such as Start and Dummy.
 * 
 * @author Matt
 * @since 05-11-2003
 * 
 */

public class JobEntrySpecial extends JobEntryBase implements Cloneable,
		JobEntryInterface {
	public static final String ENTRY_ATTRIBUTE_START = "start";
	public static final String ENTRY_ATTRIBUTE_DUMMY = "dummy";
	public static final String ENTRY_ATTRIBUTE_REPEAT = "repeat";
	public static final String ENTRY_ATTRIBUTE_SCHEDULER_TYPE = "schedulerType";
	public static final String ENTRY_ATTRIBUTE_INTERVAL_SECONDS = "intervalSeconds";
	public static final String ENTRY_ATTRIBUTE_INTERVAL_MINUTES = "intervalMinutes";
	public static final String ENTRY_ATTRIBUTE_HOUR = "hour";
	public static final String ENTRY_ATTRIBUTE_MINUTES = "minutes";
	public static final String ENTRY_ATTRIBUTE_WEEK_DAY = "weekDay";
	public static final String ENTRY_ATTRIBUTE_DAY_OF_MONTH = "dayOfMonth";

	public final static int NOSCHEDULING = 0;
	public final static int INTERVAL = 1;
	public final static int DAILY = 2;
	public final static int WEEKLY = 3;
	public final static int MONTHLY = 4;

	private boolean start;
	private boolean dummy;
	private boolean repeat = false;
	private int schedulerType = NOSCHEDULING;
	private int intervalSeconds = 0;
	private int intervalMinutes = 60;
	private int dayOfMonth = 1;
	private int weekDay = 1;
	private int minutes = 0;
	private int hour = 12;

	public JobEntrySpecial() {
		this(null, false, false);
	}

	public JobEntrySpecial(String name, boolean start, boolean dummy) {
		super(name, "");
		this.start = start;
		this.dummy = dummy;
		setJobEntryType(JobEntryType.SPECIAL);
	}

	public JobEntrySpecial(JobEntryBase jeb) {
		super(jeb);
	}

	public Object clone() {
		JobEntrySpecial je = (JobEntrySpecial) super.clone();
		return je;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(200);

		retval.append(super.getXML());

		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_START, start));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_DUMMY, dummy));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_REPEAT, repeat));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_SCHEDULER_TYPE,
						schedulerType));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_INTERVAL_SECONDS,
						intervalSeconds));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_INTERVAL_MINUTES,
						intervalMinutes));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_HOUR, hour));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_MINUTES, minutes));
		retval.append("      ").append(
				XMLHandler.addTagValue(ENTRY_ATTRIBUTE_WEEK_DAY, weekDay));
		retval.append("      ").append(
				XMLHandler
						.addTagValue(ENTRY_ATTRIBUTE_DAY_OF_MONTH, dayOfMonth));

		return retval.toString();
	}

	public void loadXML(Node entrynode, List<DatabaseMeta> databases,
			List<SlaveServer> slaveServers, Repository rep)
			throws KettleXMLException {
		try {
			super.loadXML(entrynode, databases, slaveServers);
			start = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_START));
			dummy = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_DUMMY));
			repeat = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_REPEAT));
			setSchedulerType(Const.toInt(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_SCHEDULER_TYPE), NOSCHEDULING));
			setIntervalSeconds(Const.toInt(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_INTERVAL_SECONDS), 0));
			setIntervalMinutes(Const.toInt(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_INTERVAL_MINUTES), 0));
			setHour(Const.toInt(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_HOUR), 0));
			setMinutes(Const.toInt(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_MINUTES), 0));
			setWeekDay(Const.toInt(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_WEEK_DAY), 0));
			setDayOfMonth(Const.toInt(XMLHandler.getTagValue(entrynode,
					ENTRY_ATTRIBUTE_DAY_OF_MONTH), 0));
		} catch (KettleException e) {
			throw new KettleXMLException(
					"Unable to load job entry of type 'special' from XML node",
					e);
		}
	}

	public void loadRep(Repository rep, long id_jobentry,
			List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
			throws KettleException {
		try {
			super.loadRep(rep, id_jobentry, databases, slaveServers);

			start = rep.getJobEntryAttributeBoolean(id_jobentry,
					ENTRY_ATTRIBUTE_START);
			dummy = rep.getJobEntryAttributeBoolean(id_jobentry,
					ENTRY_ATTRIBUTE_DUMMY);
			repeat = rep.getJobEntryAttributeBoolean(id_jobentry,
					ENTRY_ATTRIBUTE_REPEAT);
			schedulerType = (int) rep.getJobEntryAttributeInteger(id_jobentry,
					ENTRY_ATTRIBUTE_SCHEDULER_TYPE);
			intervalSeconds = (int) rep.getJobEntryAttributeInteger(
					id_jobentry, ENTRY_ATTRIBUTE_INTERVAL_SECONDS);
			intervalMinutes = (int) rep.getJobEntryAttributeInteger(
					id_jobentry, ENTRY_ATTRIBUTE_INTERVAL_MINUTES);
			hour = (int) rep.getJobEntryAttributeInteger(id_jobentry,
					ENTRY_ATTRIBUTE_HOUR);
			minutes = (int) rep.getJobEntryAttributeInteger(id_jobentry,
					ENTRY_ATTRIBUTE_MINUTES);
			weekDay = (int) rep.getJobEntryAttributeInteger(id_jobentry,
					ENTRY_ATTRIBUTE_WEEK_DAY);
			dayOfMonth = (int) rep.getJobEntryAttributeInteger(id_jobentry,
					ENTRY_ATTRIBUTE_DAY_OF_MONTH);
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					"Unable to load job entry of type 'special' from the repository for id_jobentry="
							+ id_jobentry, dbe);
		}
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		this.start = JobEntryBase.parameterToBoolean(p.get(id + ".start"));
		this.repeat = JobEntryBase.parameterToBoolean(p.get(id + ".repeat"));
		this.schedulerType = JobEntryBase.parameterToInt(p.get(id+ ".schedulerType"));
		this.intervalSeconds = JobEntryBase.parameterToInt(p.get(id+ ".intervalSecond"));
		this.intervalMinutes = JobEntryBase.parameterToInt(p.get(id+ ".intervalMinute"));

		String timeOfDayStr = JobEntryBase.parameterToString(p.get(id+ ".timeOfDay"));
		
		int hour = 0, minutes = 0;
		if (StringUtils.isNotEmpty(timeOfDayStr)) {
			String[] times = timeOfDayStr.split(":");
			hour = Integer.valueOf(times[0]);
			minutes = Integer.valueOf(times[1]);
		}
		this.hour = hour;
		this.minutes = minutes;
		this.weekDay = JobEntryBase.parameterToInt(p.get(id + ".dayOfWeek"));
		this.dayOfMonth = JobEntryBase.parameterToInt(p.get(id + ".dayOfMonth"));
	}

	// Save the attributes of this job entry
	//
	public void saveRep(Repository rep, long id_job) throws KettleException {
		super.saveRep(rep, id_job);

		try {
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_START,
					start);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_DUMMY,
					dummy);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_REPEAT,
					repeat);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_SCHEDULER_TYPE, schedulerType);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_INTERVAL_SECONDS, intervalSeconds);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_INTERVAL_MINUTES, intervalMinutes);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_HOUR,
					hour);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_MINUTES,
					minutes);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_WEEK_DAY, weekDay);
			rep.saveJobEntryAttribute(id_job, getID(),
					ENTRY_ATTRIBUTE_DAY_OF_MONTH, dayOfMonth);
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					"Unable to save job entry of type 'special' to the repository with id_job="
							+ id_job, dbe);
		}
	}

	public boolean isStart() {
		return start;
	}

	public boolean isDummy() {
		return dummy;
	}

	public Result execute(Result previousResult, int nr, Repository rep,
			Job parentJob) throws KettleJobException {
		Result result = previousResult;

		if (isStart()) {
			try {
				long sleepTime = getNextExecutionTime();
				if (sleepTime > 0) {
					parentJob.getLog()
							.logBasic(
									parentJob.getJobname(),
									"Sleeping: " + (sleepTime / 1000 / 60)
											+ " minutes (sleep time="
											+ sleepTime + ")");
					long totalSleep = 0L;
					while (totalSleep < sleepTime && !parentJob.isStopped()) {
						Thread.sleep(1000L);
						totalSleep += 1000L;
					}
				}
			} catch (InterruptedException e) {
				throw new KettleJobException(e);
			}
			result = previousResult;
			result.setResult(true);
		} else if (isDummy()) {
			result = previousResult;
		}
		return result;
	}

	private long getNextExecutionTime() {
		switch (schedulerType) {
		case NOSCHEDULING:
			return 0;
		case INTERVAL:
			return getNextIntervalExecutionTime();
		case DAILY:
			return getNextDailyExecutionTime();
		case WEEKLY:
			return getNextWeeklyExecutionTime();
		case MONTHLY:
			return getNextMonthlyExecutionTime();
		default:
			break;
		}
		return 0;
	}

	private long getNextIntervalExecutionTime() {
		return intervalSeconds * 1000 + intervalMinutes * 1000 * 60;
	}

	private long getNextMonthlyExecutionTime() {
		Calendar calendar = Calendar.getInstance();

		long nowMillis = calendar.getTimeInMillis();
		int amHour = hour;
		if (amHour > 12) {
			amHour = amHour - 12;
			calendar.set(Calendar.AM_PM, Calendar.PM);
		} else {
			calendar.set(Calendar.AM_PM, Calendar.AM);
		}
		calendar.set(Calendar.HOUR, amHour);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		if (calendar.getTimeInMillis() <= nowMillis) {
			calendar.add(Calendar.MONTH, 1);
		}
		return calendar.getTimeInMillis() - nowMillis;
	}

	private long getNextWeeklyExecutionTime() {
		Calendar calendar = Calendar.getInstance();

		long nowMillis = calendar.getTimeInMillis();
		int amHour = hour;
		if (amHour > 12) {
			amHour = amHour - 12;
			calendar.set(Calendar.AM_PM, Calendar.PM);
		} else {
			calendar.set(Calendar.AM_PM, Calendar.AM);
		}
		calendar.set(Calendar.HOUR, amHour);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.DAY_OF_WEEK, weekDay + 1);
		if (calendar.getTimeInMillis() <= nowMillis) {
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
		}
		return calendar.getTimeInMillis() - nowMillis;
	}

	private long getNextDailyExecutionTime() {
		Calendar calendar = Calendar.getInstance();

		long nowMillis = calendar.getTimeInMillis();
		int amHour = hour;
		if (amHour > 12) {
			amHour = amHour - 12;
			calendar.set(Calendar.AM_PM, Calendar.PM);
		} else {
			calendar.set(Calendar.AM_PM, Calendar.AM);
		}
		calendar.set(Calendar.HOUR, amHour);
		calendar.set(Calendar.MINUTE, minutes);
		if (calendar.getTimeInMillis() <= nowMillis) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return calendar.getTimeInMillis() - nowMillis;
	}

	public boolean evaluates() {
		return false;
	}

	public boolean isUnconditional() {
		return true;
	}

	public int getSchedulerType() {
		return schedulerType;
	}

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getWeekDay() {
		return weekDay;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public void setSchedulerType(int schedulerType) {
		this.schedulerType = schedulerType;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public int getIntervalSeconds() {
		return intervalSeconds;
	}

	public void setIntervalSeconds(int intervalSeconds) {
		this.intervalSeconds = intervalSeconds;
	}

	public int getIntervalMinutes() {
		return intervalMinutes;
	}

	public void setIntervalMinutes(int intervalMinutes) {
		this.intervalMinutes = intervalMinutes;
	}

	/**
	 * @param dummy
	 *            the dummy to set
	 */
	public void setDummy(boolean dummy) {
		this.dummy = dummy;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(boolean start) {
		this.start = start;
	}

}

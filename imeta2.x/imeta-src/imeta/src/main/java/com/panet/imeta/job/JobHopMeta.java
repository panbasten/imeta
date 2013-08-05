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

package com.panet.imeta.job;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.json.JSONInterface;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.core.xml.XMLInterface;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.repository.Repository;

/**
 * This class defines a hop from one job entry copy to another.
 * 
 * @author Matt
 * @since 19-06-2003
 * 
 */
public class JobHopMeta implements Cloneable, XMLInterface, JSONInterface {
	public static final String CUSTOM_TYPE_UNCONDITIONAL = "unconditional";
	public static final String CUSTOM_TYPE_SUCCESS = "success";
	public static final String CUSTOM_TYPE_FAILURE = "failure";

	public JobEntryCopy from_entry, to_entry;
	private boolean enabled;
	private boolean split;
	private boolean evaluation;
	private boolean unconditional;

	private boolean changed;

	private long id;

	private String tempId;

	private String guiMidLocationX, guiMidLocationY;

	public JobHopMeta() {
		this((JobEntryCopy) null, (JobEntryCopy) null);
	}

	public JobHopMeta(JobEntryCopy from, JobEntryCopy to) {
		from_entry = from;
		to_entry = to;
		enabled = true;
		split = false;
		evaluation = true;
		unconditional = false;
		id = -1L;
		guiMidLocationX = "";
		guiMidLocationY = "";

		if (from.isStart())
			setUnconditional();
	}

	private JobEntryCopy searchJobEntry(List<JobEntryCopy> entries, String name) {
		for (JobEntryCopy jobEntry : entries)
			if (jobEntry.getName().equalsIgnoreCase(name))
				return jobEntry;

		return null;
	}

	public JobHopMeta(Node hopnode, JobMeta job) throws KettleXMLException {
		try {
			String from_name = XMLHandler.getTagValue(hopnode, "from");
			String to_name = XMLHandler.getTagValue(hopnode, "to");
			String senabled = XMLHandler.getTagValue(hopnode, "enabled");
			String sevaluation = XMLHandler.getTagValue(hopnode, "evaluation");
			String sunconditional = XMLHandler.getTagValue(hopnode,
					"unconditional");
			String guiMidLocationX = XMLHandler.getTagValue(hopnode,
					"gui_mid_location_x");
			String guiMidLocationY = XMLHandler.getTagValue(hopnode,
					"gui_mid_location_y");

			from_entry = searchJobEntry(job.getJobEntries(), from_name);
			to_entry = searchJobEntry(job.getJobEntries(), to_name);

			if (senabled == null)
				enabled = true;
			else
				enabled = "Y".equalsIgnoreCase(senabled);
			if (sevaluation == null)
				evaluation = true;
			else
				evaluation = "Y".equalsIgnoreCase(sevaluation);
			unconditional = "Y".equalsIgnoreCase(sunconditional);
			this.guiMidLocationX = guiMidLocationX;
			this.guiMidLocationY = guiMidLocationY;
		} catch (Exception e) {
			throw new KettleXMLException(Messages
					.getString("JobHopMeta.Exception.UnableToLoadHopInfoXML"),
					e);
		}
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(200);
		if ((null != from_entry) && (null != to_entry)) {
			retval.append("    <hop>").append(Const.CR);
			retval.append("      ").append(
					XMLHandler.addTagValue("from", from_entry.getName()));
			retval.append("      ").append(
					XMLHandler.addTagValue("to", to_entry.getName()));
			retval.append("      ").append(
					XMLHandler.addTagValue("from_nr", from_entry.getNr()));
			retval.append("      ").append(
					XMLHandler.addTagValue("to_nr", to_entry.getNr()));
			retval.append("      ").append(
					XMLHandler.addTagValue("enabled", enabled));
			retval.append("      ").append(
					XMLHandler.addTagValue("evaluation", evaluation));
			retval.append("      ").append(
					XMLHandler.addTagValue("unconditional", unconditional));
			retval.append("      ").append(
					XMLHandler.addTagValue("gui_mid_location_x",
							guiMidLocationX));
			retval.append("      ").append(
					XMLHandler.addTagValue("gui_mid_location_y",
							guiMidLocationY));
			retval.append("    </hop>").append(Const.CR);
		}

		return retval.toString();
	}

	public JobHopMeta(Repository rep, long id_job_hop, JobMeta job,
			List<JobEntryCopy> jobcopies) throws KettleException {
		try {
			long id_jobentry_copy_from;
			long id_jobentry_copy_to;

			RowMetaAndData r = rep.getJobHop(id_job_hop);
			if (r != null) {
				id_jobentry_copy_from = r.getInteger("ID_JOBENTRY_COPY_FROM",
						-1L);
				id_jobentry_copy_to = r.getInteger("ID_JOBENTRY_COPY_TO", -1L);
				enabled = r.getBoolean("ENABLED", true);
				evaluation = r.getBoolean("EVALUATION", true);
				unconditional = r.getBoolean("UNCONDITIONAL", !evaluation);
				guiMidLocationX = r.getString("GUI_MID_LOCATION_X", "");
				guiMidLocationY = r.getString("GUI_MID_LOCATION_Y", "");

				from_entry = JobMeta.findJobEntryCopy(jobcopies,
						id_jobentry_copy_from);
				to_entry = JobMeta.findJobEntryCopy(jobcopies,
						id_jobentry_copy_to);
			}
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(Messages.getString(
					"JobHopMeta.Exception.UnableToLoadHopInfoRep", ""
							+ id_job_hop), dbe);

		}
	}

	public void saveRep(Repository rep, long id_job) throws KettleException {
		try {
			long id_jobentry_from = -1, id_jobentry_to = -1;

			id_jobentry_from = from_entry == null ? -1 : from_entry.getID();
			id_jobentry_to = to_entry == null ? -1 : to_entry.getID();

			// Insert new transMeta hop in repository
			setID(rep.insertJobHop(id_job, id_jobentry_from, id_jobentry_to,
					enabled, evaluation, unconditional, guiMidLocationX,
					guiMidLocationY));
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					Messages.getString(
							"JobHopMeta.Exception.UnableToSaveHopInfoRep", ""
									+ id_job), dbe);

		}
	}

	public void setID(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	public Object clone() {
		try {
			JobHopMeta retval = (JobHopMeta) super.clone();
			retval.setTempId(this.tempId);
			retval.setID(-1L);
			return retval;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void setChanged() {
		setChanged(true);
	}

	public void setChanged(boolean ch) {
		changed = ch;
	}

	public boolean hasChanged() {
		return changed;
	}

	public void setEnabled() {
		setEnabled(true);
	}

	public void setEnabled(boolean en) {
		enabled = en;
		setChanged();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean getEvaluation() {
		return evaluation;
	}

	public void setEvaluation() {
		setEvaluation(true);
	}

	public void setEvaluation(boolean e) {
		evaluation = e;
	}

	public void setUnconditional() {
		if (!unconditional)
			setChanged();
		unconditional = true;
	}

	public void setConditional() {
		if (unconditional)
			setChanged();
		unconditional = false;
	}

	public boolean isUnconditional() {
		return unconditional;
	}

	public void setSplit(boolean split) {
		if (this.split != split)
			setChanged();
		this.split = split;
	}

	public boolean isSplit() {
		return split;
	}

	public String getDescription() {
		if (isUnconditional())
			return Messages
					.getString("JobHopMeta.Msg.ExecNextJobEntryUncondition");
		else {
			if (getEvaluation())
				return Messages
						.getString("JobHopMeta.Msg.ExecNextJobEntryFlawLess");
			else
				return Messages
						.getString("JobHopMeta.Msg.ExecNextJobEntryFailed");
		}
	}

	public String toString() {
		return getDescription();
		// return from_entry.getName()+"."+from_entry.getNr()+" -->
		// "+to_entry.getName()+"."+to_entry.getNr();
	}

	@Override
	public JSONObject getJSON() throws KettleException {
		JSONObject hopJo = new JSONObject();
		hopJo.put("id", this.getGraphId());
		hopJo.put("path", "canvasEls.hops");
		hopJo.put("elType", "hop");
		JSONArray x = new JSONArray();
		x.add(0);
		int[] xs = getGuiMidLocationX();
		if (xs != null)
			for (int xi : xs) {
				x.add(xi);
			}
		x.add(0);
		hopJo.put("x", x);
		JSONArray y = new JSONArray();
		y.add(0);
		int[] ys = getGuiMidLocationY();
		if (ys != null)
			for (int yi : ys) {
				y.add(yi);
			}
		y.add(0);
		hopJo.put("y", y);
		hopJo.put("fromEl", this.from_entry.getGraphId());
		hopJo.put("toEl", this.to_entry.getGraphId());
		hopJo.put("fromElWidth", 32);
		hopJo.put("fromElHeight", 32);
		hopJo.put("toElWidth", 32);
		hopJo.put("toElHeight", 32);
		hopJo.put("hopId", this.getID());
		hopJo.put("isNew", (this.getID() > 0) ? false : true);
		hopJo.put("isEnable", true);
		String cType = "", style = "";
		if (unconditional) {
			cType = CUSTOM_TYPE_UNCONDITIONAL;
			style = "blue";
		} else {
			if (evaluation) {
				cType = CUSTOM_TYPE_SUCCESS;
				style = "green";
			} else {
				cType = CUSTOM_TYPE_FAILURE;
				style = "red";
			}
		}
		hopJo.put("customType", cType);
		hopJo.put("style", style);
		return hopJo;
	}

	public int[] getGuiMidLocationX() {
		if (StringUtils.isNotEmpty(guiMidLocationX)) {
			String[] xstrs = guiMidLocationX.split(",");
			int[] xs = new int[xstrs.length];
			for (int i = 0; i < xstrs.length; i++) {
				xs[i] = Integer.valueOf(xstrs[i]);
			}
			return xs;
		}
		return null;
	}

	public void setGuiMidLocationX(int[] xs) {
		StringBuffer xsb = new StringBuffer();
		if (xs != null)
			for (int x : xs) {
				xsb.append(x).append(",");
			}
		this.guiMidLocationX = xsb.toString();
	}

	public int[] getGuiMidLocationY() {
		if (StringUtils.isNotEmpty(guiMidLocationY)) {
			String[] ystrs = guiMidLocationY.split(",");
			int[] ys = new int[ystrs.length];
			for (int i = 0; i < ystrs.length; i++) {
				ys[i] = Integer.valueOf(ystrs[i]);
			}
			return ys;
		}
		return null;
	}

	public void setGuiMidLocationY(int[] ys) {
		StringBuffer ysb = new StringBuffer();
		if (ys != null)
			for (int y : ys) {
				ysb.append(y).append(",");
			}
		this.guiMidLocationY = ysb.toString();
	}

	public String getGraphId() {
		return (this.getID() > 0) ? ("jobhop_" + this.getID()) : this.tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getTempId() {
		return tempId;
	}
}

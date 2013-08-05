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

package com.panet.imeta.trans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.panet.imeta.core.Counter;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.json.JSONInterface;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.core.xml.XMLInterface;
import com.panet.imeta.partition.PartitionSchema;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.trans.step.StepMeta;

/*
 * Created on 19-jun-2003
 * 
 */

/**
 * Defines a link between 2 steps in a transformation
 */
public class TransHopMeta implements Cloneable, XMLInterface, JSONInterface,
		Comparable<TransHopMeta> {
	public static final String XML_TAG = "hop";

	private StepMeta from_step;

	private StepMeta to_step;

	private boolean enabled;

	public boolean split = false;

	private boolean changed;

	private long id;

	private String tempId;

	private String guiMidLocationX, guiMidLocationY;

	public TransHopMeta(StepMeta from, StepMeta to, boolean en) {
		from_step = from;
		to_step = to;
		enabled = en;

		guiMidLocationX = "";
		guiMidLocationY = "";
	}

	public TransHopMeta(StepMeta from, StepMeta to) {
		from_step = from;
		to_step = to;
		enabled = true;

		guiMidLocationX = "";
		guiMidLocationY = "";
	}

	public TransHopMeta(Repository rep, long id_trans_hop, List<StepMeta> steps)
			throws KettleException {
		try {
			setID(id_trans_hop);

			RowMetaAndData r = rep.getTransHop(id_trans_hop);

			long id_step_from = r.getInteger("ID_STEP_FROM", 0); //$NON-NLS-1$
			long id_step_to = r.getInteger("ID_STEP_TO", 0); //$NON-NLS-1$
			enabled = r.getBoolean("ENABLED", false); //$NON-NLS-1$
			guiMidLocationX = r.getString("GUI_MID_LOCATION_X", "");
			guiMidLocationY = r.getString("GUI_MID_LOCATION_Y", "");

			from_step = StepMeta.findStep(steps, id_step_from);
			if (from_step == null && id_step_from > 0) // Links to a shared
			// objects, try again by
			// looking up the
			// name...
			{
				// Simply load this, we only want the name, we don't care about
				// the rest...
				StepMeta stepMeta = new StepMeta(rep, id_step_from,
						new ArrayList<DatabaseMeta>(),
						new Hashtable<String, Counter>(),
						new ArrayList<PartitionSchema>());
				from_step = StepMeta.findStep(steps, stepMeta.getName());
			}
			from_step.setDraw(true);

			to_step = StepMeta.findStep(steps, id_step_to);
			if (to_step == null && id_step_to > 0) // Links to a shared
			// objects, try again by
			// looking up the name...
			{
				// Simply load this, we only want the name, we don't care about
				// the rest...
				StepMeta stepMeta = new StepMeta(rep, id_step_to,
						new ArrayList<DatabaseMeta>(),
						new Hashtable<String, Counter>(),
						new ArrayList<PartitionSchema>());
				to_step = StepMeta.findStep(steps, stepMeta.getName());
			}
			to_step.setDraw(true);
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					Messages
							.getString("TransHopMeta.Exception.LoadTransformationHopInfo") + id_trans_hop, dbe); //$NON-NLS-1$
		}
	}

	public TransHopMeta() {
		this(null, null, false);
	}

	public TransHopMeta(Node hopnode, List<StepMeta> steps)
			throws KettleXMLException {
		try {
			from_step = searchStep(steps, XMLHandler.getTagValue(hopnode,
					"from")); //$NON-NLS-1$
			to_step = searchStep(steps, XMLHandler.getTagValue(hopnode, "to")); //$NON-NLS-1$
			String en = XMLHandler.getTagValue(hopnode, "enabled"); //$NON-NLS-1$

			if (en == null)
				enabled = true;
			else
				enabled = en.equalsIgnoreCase("Y"); //$NON-NLS-1$

			guiMidLocationX = "";
			guiMidLocationY = "";
		} catch (Exception e) {
			throw new KettleXMLException(Messages
					.getString("TransHopMeta.Exception.UnableToLoadHopInfo"), e); //$NON-NLS-1$
		}
	}

	public void setFromStep(StepMeta from) {
		from_step = from;
	}

	public void setToStep(StepMeta to) {
		to_step = to;
	}

	public StepMeta getFromStep() {
		return from_step;
	}

	public StepMeta getToStep() {
		return to_step;
	}

	private StepMeta searchStep(List<StepMeta> steps, String name) {
		for (StepMeta stepMeta : steps)
			if (stepMeta.getName().equalsIgnoreCase(name))
				return stepMeta;

		return null;
	}

	public void saveRep(Repository rep, long id_transformation)
			throws KettleException {
		try {
			// See if a transformation hop with the same fromstep and tostep is
			// already available...
			long id_step_from = from_step == null ? -1 : from_step.getID();
			long id_step_to = to_step == null ? -1 : to_step.getID();

			// Insert new transMeta hop in repository
			setID(rep.insertTransHop(id_transformation, id_step_from,
					id_step_to, enabled, guiMidLocationX, guiMidLocationY));
		} catch (KettleDatabaseException dbe) {
			throw new KettleException(
					Messages
							.getString("TransHopMeta.Exception.UnableToSaveTransformationHopInfo") + id_transformation, dbe); //$NON-NLS-1$
		}
	}

	public Object clone() {
		try {
			TransHopMeta retval = (TransHopMeta) super.clone();
			retval.setID(-1L);
			retval.setTempId(this.tempId);
			return retval;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public boolean equals(Object obj) {
		TransHopMeta other = (TransHopMeta) obj;
		if (from_step == null || to_step == null)
			return false;
		return from_step.equals(other.getFromStep())
				&& to_step.equals(other.getToStep());
	}

	/**
	 * Compare 2 hops.
	 */
	public int compareTo(TransHopMeta obj) {
		return toString().compareTo(obj.toString());
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
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

	public void flip() {
		StepMeta dummy = from_step;
		from_step = to_step;
		to_step = dummy;
	}

	public String toString() {
		String str_fr = (from_step == null) ? "(empty)" : from_step.getName(); //$NON-NLS-1$
		String str_to = (to_step == null) ? "(empty)" : to_step.getName(); //$NON-NLS-1$
		return str_fr + " --> " + str_to + " (" + (enabled ? "有效" : "无效") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	public String getXML() {
		StringBuilder retval = new StringBuilder(200);

		if (from_step != null && to_step != null) {
			retval.append("  <hop> "); //$NON-NLS-1$
			retval.append(XMLHandler.addTagValue(
					"from", from_step.getName(), false)); //$NON-NLS-1$
			retval.append(XMLHandler
					.addTagValue("to", to_step.getName(), false)); //$NON-NLS-1$
			retval.append(XMLHandler.addTagValue("enabled", enabled, false)); //$NON-NLS-1$
			retval.append(XMLHandler.addTagValue(
					"gui_mid_location_x", guiMidLocationY, false)); //$NON-NLS-1$
			retval.append(XMLHandler.addTagValue(
					"gui_mid_location_y", guiMidLocationY, false)); //$NON-NLS-1$
			retval.append(" </hop>"); //$NON-NLS-1$
		}

		return retval.toString();
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
		hopJo.put("fromEl", this.getFromStep().getGraphId());
		hopJo.put("toEl", this.getToStep().getGraphId());
		if (!this.getFromStep().isDistributes()) {
			JSONArray text = new JSONArray();
			text.add("复制");
			hopJo.put("text", text);
		}
		hopJo.put("fromElWidth", 32);
		hopJo.put("fromElHeight", 32);
		hopJo.put("toElWidth", 32);
		hopJo.put("toElHeight", 32);
		hopJo.put("hopId", this.getID());
		hopJo.put("isNew", (this.id > 0) ? false : true);
		hopJo.put("isEnable", true);
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
		return (this.getID() > 0) ? ("hop_" + this.getID()) : this.tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getTempId() {
		return tempId;
	}
}

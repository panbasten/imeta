package com.panet.imeta.job;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.repository.RepositoryObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JobGraph {

	private JobMeta jobMeta;

	private boolean editable = false;

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public JobGraph(JobMeta jobMeta) {
		this.jobMeta = jobMeta;
	}

	public JSONObject open() throws KettleException {
		JSONObject ro = new JSONObject();
		ro.put("objectType", RepositoryObject.STRING_OBJECT_TYPE_JOB);
		ro.put("objectName", jobMeta.getName());
		ro.put("directoryId", jobMeta.getDirectory().getID());
		ro.put("directoryName", jobMeta.getDirectory().getDirectoryName());
		ro.put("directoryPath", jobMeta.getDirectory().getPath());
		// 载入节点
		List<JobEntryCopy> jes = jobMeta.getJobEntries();
		JSONArray jesJo = new JSONArray();
		JobEntryCopy je;
		int index = 0;
		for (int i = 0; i < jes.size(); i++) {
			je = jes.get(i);
			if (je.getID() < 1L && StringUtils.isEmpty(je.getTempId())) {
				je.setTempId("ng_jobentry_" + (index++));
			}
			JSONObject jeJo = je.getJSON();
			jeJo.put("index", i);
			jesJo.add(jeJo);
		}
		ro.put("steps", jesJo);

		// 载入连接
		List<JobHopMeta> hops = jobMeta.getJobHops();
		JSONArray hopsJo = new JSONArray();
		JobHopMeta hopMeta;
		for (int i = 0; i < hops.size(); i++) {
			hopMeta = hops.get(i);
			if (hopMeta.getID() < 1L
					&& StringUtils.isEmpty(hopMeta.getTempId())) {
				hopMeta.setTempId("ng_jobhop_" + (index++));
			}
			JSONObject hopJo = hopMeta.getJSON();
			hopJo.put("index", i);
			hopsJo.add(hopJo);
		}
		ro.put("hops", hopsJo);

		ro.put("x0", jobMeta.getGuiLocationX());
		ro.put("y0", jobMeta.getGuiLocationY());
		ro.put("scale", jobMeta.getGuiScale());
		ro.put("editable", editable);
		return ro;
	}
}

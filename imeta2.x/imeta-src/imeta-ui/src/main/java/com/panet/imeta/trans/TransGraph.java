package com.panet.imeta.trans;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.step.StepMeta;

public class TransGraph {

	private TransMeta transMeta;

	private boolean editable;

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public TransGraph(TransMeta transMeta) {
		this.transMeta = transMeta;
	}

	public JSONObject open() throws KettleException {
		JSONObject ro = new JSONObject();
		ro
				.put("objectType",
						RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
		ro.put("objectName", transMeta.getName());
		ro.put("directoryId", transMeta.getDirectory().getID());
		ro.put("directoryName", transMeta.getDirectory().getDirectoryName());
		ro.put("directoryPath", transMeta.getDirectory().getPath());
		// 载入节点
		List<StepMeta> steps = transMeta.getSteps();
		JSONArray stepsJo = new JSONArray();
		StepMeta stepMeta;
		int index = 0;
		for (int i = 0; i < steps.size(); i++) {
			stepMeta = steps.get(i);
			if (stepMeta.getID() < 1L
					&& StringUtils.isEmpty(stepMeta.getTempId())) {
				stepMeta.setTempId("ng_step_" + (index++));
			}
			JSONObject stepJo = stepMeta.getJSON();
			stepJo.put("index", i);
			stepsJo.add(stepJo);
		}
		ro.put("steps", stepsJo);

		// 载入连接
		List<TransHopMeta> hops = transMeta.getHops();
		JSONArray hopsJo = new JSONArray();
		TransHopMeta hopMeta;
		for (int i = 0; i < hops.size(); i++) {
			hopMeta = hops.get(i);
			if (hopMeta.getID() < 1L
					&& StringUtils.isEmpty(hopMeta.getTempId())) {
				hopMeta.setTempId("ng_hop_" + (index++));
			}
			JSONObject hopJo = hopMeta.getJSON();
			hopJo.put("index", i);
			hopsJo.add(hopJo);
		}
		ro.put("hops", hopsJo);

		// 载入组
		JSONArray groupsJo = new JSONArray();
		ro.put("groups", groupsJo);

		ro.put("x0", transMeta.getGuiLocationX());
		ro.put("y0", transMeta.getGuiLocationY());
		ro.put("scale", transMeta.getGuiScale());
		ro.put("editable", editable);
		return ro;
	}
}

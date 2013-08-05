/**
 * @(#)InputMeta.java 1.00 2009/06/22
 *
 * Copyright (c) 2009 中科软科技股份有限公司 版权所有
 * Sinosoft Co.,LTD. All rights reserved.
 * 
 * This software was developed by Sinosoft Corporation. 
 * You shall not disclose and decompile such software 
 * information or code and shall use it only in accordance 
 * with the terms of the contract agreement you entered 
 * into with Sinosoft.
 */
package com.panet.iform.forms.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.core.ComplexFormDataMeta;
import com.panet.iform.core.base.ButtonDataMeta;

/**
* 树形节点数据
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class TreeNodeDataMeta extends ComplexFormDataMeta {
	
	/**
	 * 节点ID
	 */
	private String nodeId;

	/**
	 * 节点图标
	 */
	private String nodeIcon;

	/**
	 * 节点文字
	 */
	private String nodeText;

	/**
	 * 节点描述
	 */
	private String nodeDesc;

	/**
	 * 节点是否默认关闭
	 */
	private boolean closed = true;
	
	/**
	 * 初始化
	 */
	private String init;

	/**
	 * 节点扩展
	 */
	private Map<String, Object> nodeExtends;

	/**
	 * 分节点
	 */
	private List<TreeNodeDataMeta> subNode;

	/**
	 * 按键
	 */
	private List<ButtonDataMeta> buttons;

	/**
	 * 左右 标示
	 */
	private boolean leaf = false;
	
	/**
	 * 树节点
	 * 
	 * @param nodeId
	 *            字段ID
	 * @param nodeIcon
	 *            标签名
	 * @param nodeText
	 *            节点文字
	 * @param nodeDesc
	 *            节点文字注释
	 * @param closed
	 *            是否默认关闭
	 * @param leaf
	 *            是否叶子节点
	 */
	public TreeNodeDataMeta(String nodeId, String nodeIcon, String nodeText,
			String nodeDesc, boolean closed, boolean leaf) {
		super(nodeId);
		this.nodeId = nodeId;
		this.nodeIcon = nodeIcon;
		this.nodeText = nodeText;
		this.nodeDesc = nodeDesc;
		this.closed = closed;
		this.subNode = new ArrayList<TreeNodeDataMeta>();
		this.nodeExtends = new HashMap<String, Object>();
		this.buttons = new ArrayList<ButtonDataMeta>();
		this.leaf = leaf;
	}

	/**
	 * 设置按键
	 * 
	 * @param btn
	 */
	public void putButton(ButtonDataMeta btn) {
		this.buttons.add(btn);
	}

	/**
	 * 设置按键
	 * 
	 * @param btns
	 */
	public void putButtons(ButtonDataMeta[] btns) {
		if (btns != null && btns.length > 0) {
			for (ButtonDataMeta btn : btns) {
				this.buttons.add(btn);
			}
		}
	}

	/**
	 * 设置扩展节点
	 * @param k
	 * @param v
	 */
	public void putNodeExtend(String k, Object v) {
		this.nodeExtends.put(k, v);
	}

	public boolean isLeaf() {
		return this.subNode.size() == 0 && leaf;
	}

	public void putSubNode(TreeNodeDataMeta subNode) {
		this.subNode.add(subNode);
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeIcon() {
		return nodeIcon;
	}

	public void setNodeIcon(String nodeIcon) {
		this.nodeIcon = nodeIcon;
	}

	public String getNodeText() {
		return nodeText;
	}

	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public String getNodeDesc() {
		return nodeDesc;
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}

	public List<TreeNodeDataMeta> getSubNode() {
		return subNode;
	}

	public void setSubNode(List<TreeNodeDataMeta> subNode) {
		this.subNode = subNode;
	}

	public Map<String, Object> getNodeExtends() {
		return nodeExtends;
	}

	public List<ButtonDataMeta> getButtons() {
		return buttons;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public void addInit(String init) {
		if (!StringUtils.isEmpty(init)) {
			this.init = StringUtils.join(new String[] { this.init, init });
		}
	}
}

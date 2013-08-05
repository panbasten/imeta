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
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.SimpleFormMeta;
import com.panet.iform.core.base.ButtonDataMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.LiMeta;
import com.panet.iform.core.base.SpanMeta;
import com.panet.iform.core.base.UlMeta;
import com.panet.iform.exception.ImetaFormException;

/**
 * 树形功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class TreeMeta extends ComplexFormMeta {

	/**
	 * 根
	 */
	private SimpleFormMeta[] root;

	/**
	 * 树状
	 */
	private List<TreeNodeDataMeta> treeDataMetas;

	/**
	 * 标示是否根节点
	 */
	private boolean isRootTree = false;

	/**
	 * 树结构
	 * 
	 * @param treeDataMetas
	 *            treeDataMetas类型
	 */
	public TreeMeta(List<TreeNodeDataMeta> treeDataMetas) {
		super.setMultiRoot(true);
		this.treeDataMetas = treeDataMetas;
	}

	/**
	 * 树结构 初始化
	 * 
	 * @param treeDataMetas
	 *            treeDataMetas类型
	 */
	public TreeMeta(TreeNodeDataMeta[] treeDataMetas) {
		super.setMultiRoot(true);
		this.treeDataMetas = new ArrayList<TreeNodeDataMeta>();
		for (TreeNodeDataMeta node : treeDataMetas) {
			this.treeDataMetas.add(node);
		}
	}

	public List<TreeNodeDataMeta> getTreeDataMetas() {
		return this.treeDataMetas;
	}

	@Override
	public JSONArray getFormJa() throws ImetaFormException {
		if (this.isRootTree) {
			int n = this.treeDataMetas.size();
			this.root = new SimpleFormMeta[n];
			int index = 0;
			for (TreeNodeDataMeta nodeMeta : this.treeDataMetas) {
				UlMeta ul = new UlMeta(new SimpleFormDataMeta("treeRoot."
						+ nodeMeta.getId(), "treeRoot." + nodeMeta.getId(),
						null, null, null, null), false);
				if (StringUtils.isNotEmpty(nodeMeta.getInit())) {
					ul.setInit(nodeMeta.getInit());
				}
				createLiMetas(ul, nodeMeta);
				this.root[index++] = ul;
			}
		} else {
			this.root = new SimpleFormMeta[1];
			UlMeta ul = new UlMeta(new NullSimpleFormDataMeta(), false);
			for (TreeNodeDataMeta nodeMeta : this.treeDataMetas) {
				createLiMetas(ul, nodeMeta);
			}
			this.root[0] = ul;
		}

		super.setRoots(this.root);
		return super.getFormJa();
	}

	/**
	 * 创建li
	 * 
	 * @param ul
	 * @param nodeMeta
	 */
	private void createLiMetas(UlMeta ul, TreeNodeDataMeta nodeMeta) {
		LiMeta li = new LiMeta(new SimpleFormDataMeta(nodeMeta.getId(),
				nodeMeta.getId(), getLiClass(nodeMeta), null, nodeMeta
						.getNodeDesc(), null), false);
		li.appendTo(ul);

		SpanMeta span = new SpanMeta(new SimpleFormDataMeta(null, null,
				getSpanClass(nodeMeta), null, nodeMeta.getNodeText(), nodeMeta
						.getNodeText()), false);
		if (nodeMeta.getNodeExtends().size() > 0) {
			span.putProperties(nodeMeta.getNodeExtends());
		}

		if (nodeMeta.getButtons().size() > 0) {
			for (ButtonDataMeta b : nodeMeta.getButtons()) {
				ButtonMeta buttonMeta = new ButtonMeta(b);
				buttonMeta.putProperties(nodeMeta.getNodeExtends());
				buttonMeta.appendTo(span);
			}
		}

		span.appendTo(li);

		if (!nodeMeta.isLeaf()) {
			if (nodeMeta.getSubNode().size() > 0) {
				TreeMeta sub = new TreeMeta(nodeMeta.getSubNode());
				sub.appendTo(li);
			} else {
				UlMeta subUl = new UlMeta(new SimpleFormDataMeta("loadingNode."
						+ nodeMeta.getId(), "loadingNode." + nodeMeta.getId(),
						null, null, null, null), false);
				subUl.appendTo(li);
			}
		}

	}

	/**
	 * 得到跨度类
	 * 
	 * @param nodeMeta
	 * @return
	 */
	private String[] getSpanClass(TreeNodeDataMeta nodeMeta) {
		String[] clazz = null;
		List<String> classSpan = new ArrayList<String>();
		if (!nodeMeta.isLeaf()) {
			classSpan.add("folder");
		}
		if (!StringUtils.isEmpty(nodeMeta.getNodeIcon())) {
			classSpan.add(nodeMeta.getNodeIcon());
		} else {
			if (nodeMeta.isLeaf()) {
				classSpan.add("normal-file");
			} else {
				classSpan.add("normal-folder");
			}
		}
		if (classSpan.size() > 0) {
			clazz = new String[classSpan.size()];
			for (int i = 0; i < classSpan.size(); i++) {
				clazz[i] = classSpan.get(i);
			}
		}
		return clazz;
	}

	/**
	 * 得到 li 类
	 * 
	 * @param nodeMeta
	 * @return
	 */
	private String[] getLiClass(TreeNodeDataMeta nodeMeta) {
		String[] clazz = null;
		List<String> classLi = new ArrayList<String>();
		if (nodeMeta.isClosed()) {
			classLi.add("closed");
		}
		if (!StringUtils.isEmpty(nodeMeta.getNodeIcon())) {
			classLi.add(nodeMeta.getNodeIcon());
		}
		if (classLi.size() > 0) {
			clazz = new String[classLi.size()];
			for (int i = 0; i < classLi.size(); i++) {
				clazz[i] = classLi.get(i);
			}
		}
		return clazz;
	}

	@Override
	public void setWidth(int width) {

	}

	public boolean isRootTree() {
		return isRootTree;
	}

	public void setRootTree(boolean isRoot) {
		this.isRootTree = isRoot;
	}

}

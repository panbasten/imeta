package com.panet.imeta.ui.dialog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.base.ButtonDataMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.forms.tree.TreeMeta;
import com.panet.iform.forms.tree.TreeNodeDataMeta;
import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.ui.exception.ImetaException;

public class RepDirectoryDialog implements BaseDialogInterface {

	private Repository rep;

	private String id;

	private String parentId;

	private TreeMeta dbTree;

	private String customOkFn;

	private ButtonMeta ok;

	private ButtonMeta flush;

	private RepositoryDirectory directory;

	private boolean modify;

	public RepDirectoryDialog(Repository rep, String id) throws KettleException {
		this.id = id;
		this.rep = rep;
		this.customOkFn = "";
		this.parentId = "";
		this.directory = new RepositoryDirectory(rep);
	}

	public boolean isModify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

	/**
	 * @param pTree
	 * @return
	 * @throws KettleException
	 */
	public TreeNodeDataMeta getDirectoryTree(RepositoryDirectory pTree)
			throws KettleException {
		if (pTree != null) {
			TreeNodeDataMeta tree = new TreeNodeDataMeta(
					new Long(pTree.getID()).toString(), null, pTree
							.getDirectoryName(), pTree.getDirectoryName(),
					true, false);
			tree.putNodeExtend("directoryId", pTree.getID());
			tree.putNodeExtend("directoryName", pTree.getDirectoryName());
			tree.putNodeExtend("directoryPath", pTree.getPath());

			if (modify) {
				ButtonDataMeta addFolderBtn = new ButtonDataMeta(null, null,
						new String[] { "file-folder-add" }, null, "添加目录", null);
				if (pTree.getID() != 0) {
					ButtonDataMeta deleteFolderBtn = new ButtonDataMeta(null,
							null, new String[] { "file-folder-delete" }, null,
							"删除目录", null);
					ButtonDataMeta editFolderBtn = new ButtonDataMeta(null,
							null, new String[] { "file-folder-edit" }, null,
							"更改目录名称", null);
					tree.putButtons(new ButtonDataMeta[] { addFolderBtn,
							deleteFolderBtn, editFolderBtn });
				} else {
					tree.putButtons(new ButtonDataMeta[] { addFolderBtn });
				}
			}

			// 遍历子目录
			if (pTree.getChildren() != null && pTree.getChildren().size() > 0) {
				for (RepositoryDirectory cTree : pTree.getChildren()) {
					TreeNodeDataMeta subNode = getDirectoryTree(cTree);
					if (subNode != null) {
						tree.putSubNode(subNode);
					}
				}
			}
			return tree;
		}
		return null;
	}
	
	public void createRepTree() throws KettleException{
		TreeNodeDataMeta nodes_0 = new TreeNodeDataMeta(id + "_root",
				"db-folder", rep.getName(), rep.getName(), false, false);

		nodes_0
				.addInit("$('[id=treeRoot."
						+ id
						+ "_root]').treeview({animated: 'fast',collapsed: false});");

		nodes_0
				.addInit("$('[id=treeRoot."
						+ id
						+ "_root]').find('span.normal-folder').click(jQuery.imenu.iMenuFn.file.detectRepBtn.itemClick);");

		nodes_0
				.addInit("$('[id=treeRoot."
						+ id
						+ "_root]').find('button.file-folder-add').click(jQuery.imenu.iMenuFn.file.detectRepBtn.addFolder);");
		nodes_0
				.addInit("$('[id=treeRoot."
						+ id
						+ "_root]').find('button.file-folder-delete').click(jQuery.imenu.iMenuFn.file.detectRepBtn.deleteFolder);");
		nodes_0
				.addInit("$('[id=treeRoot."
						+ id
						+ "_root]').find('button.file-folder-edit').click(jQuery.imenu.iMenuFn.file.detectRepBtn.editFolder);");
		nodes_0
				.addInit("$('[id=treeRoot."
						+ id
						+ "_root]').find('button.file-object-delete').click(jQuery.imenu.iMenuFn.file.detectRepBtn.deleteObject);");

		nodes_0.putSubNode(getDirectoryTree(directory));

		this.dbTree = new TreeMeta(new TreeNodeDataMeta[] { nodes_0 });
		this.dbTree.setRootTree(true);
	}

	public TreeMeta getDbTree() {
		return dbTree;
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			// 资源库的树
			DivMeta dbDiv = new DivMeta(new NullSimpleFormDataMeta(), true);

			DivMeta dbTreeDiv = new DivMeta(new SimpleFormDataMeta("repDirectoryTreeDiv", null,
					new String[] { "toolbar-content" }, null, null, null),
					false);
			dbTreeDiv.setStyle("width", "60%");
			dbTreeDiv.setStyle("height", "100%");
			dbTreeDiv.setStyle("float", "left");
			dbTreeDiv.setStyle("font-size", "12px");
			dbTreeDiv.appendTo(dbDiv);

			// 按钮
			DivMeta dbBtnDiv = new DivMeta(new NullSimpleFormDataMeta(), false);
			dbBtnDiv.setStyle("width", "33%");
			dbBtnDiv.setStyle("padding-left", "10px");
			dbBtnDiv.setStyle("float", "left");
			dbBtnDiv.setStyle("margin", "10px");
			dbBtnDiv.appendTo(dbDiv);

			createRepTree();

			dbTreeDiv.append(this.dbTree);

			this.ok = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定", "确定");
			this.ok.putProperty("customOkFn", customOkFn);
			this.ok.putProperty("parentId", parentId);
			this.ok.addClick("jQuery.imenu.iMenuFn.file.detectRepBtn.ok");
			this.ok.appendTo(dbBtnDiv);

			this.flush = new ButtonMeta(id + ".btn.flush", id + ".btn.flush",
					"刷新", "刷新");
			this.flush.putProperty("customOkFn", customOkFn);
			this.flush.putProperty("parentId", parentId);
			this.flush.putProperty("modify", modify);
			this.flush.addClick("jQuery.imenu.iMenuFn.file.detectRepBtn.flushDirectory");
			this.flush.appendTo(dbBtnDiv);

			cArr.add(dbDiv.getFormJo());
			rtn.put("items", cArr);

			rtn.put("title", "探测资源库");

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

	public String getCustomOkFn() {
		return customOkFn;
	}

	public void setCustomOkFn(String customOkFn) {
		this.customOkFn = customOkFn;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}

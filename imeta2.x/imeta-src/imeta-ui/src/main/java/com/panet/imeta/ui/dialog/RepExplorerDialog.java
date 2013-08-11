package com.panet.imeta.ui.dialog;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.base.ButtonDataMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.forms.tree.TreeMeta;
import com.panet.iform.forms.tree.TreeNodeDataMeta;
import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.ui.exception.ImetaException;

public class RepExplorerDialog implements BaseDialogInterface {

	private Repository rep;

	private String id;

	private String parentId;

	private TreeMeta dbTree;

	private String customOkFn;

	private ButtonMeta ok;

	private ButtonMeta flush;

	private String showTypes;

	private boolean database, partition, slave, cluster, trans, job, user,
			profile;

	private boolean modify;

	public boolean isModify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

	private RepositoryDirectory directory;

	public RepExplorerDialog(Repository rep, String id) throws KettleException {
		this.id = id;
		this.rep = rep;
		this.customOkFn = "";
		this.parentId = "";
		this.directory = new RepositoryDirectory(rep);
	}

	public void setShowTypes(String types) {
		this.showTypes = types;
		if (StringUtils.isNotEmpty(types))
			if ("all".equalsIgnoreCase(types)) {
				database = true;
				partition = true;
				slave = true;
				cluster = true;
				trans = true;
				job = true;
				user = true;
				profile = true;
			} else {
				String[] tArr = types.split(",");
				for (String t : tArr) {
					if ("database".equalsIgnoreCase(t)) {
						database = true;
					} else if ("partition".equalsIgnoreCase(t)) {
						partition = true;
					} else if ("slave".equalsIgnoreCase(t)) {
						slave = true;
					} else if ("cluster".equalsIgnoreCase(t)) {
						cluster = true;
					} else if ("trans".equalsIgnoreCase(t)) {
						trans = true;
					} else if ("job".equalsIgnoreCase(t)) {
						job = true;
					} else if ("user".equalsIgnoreCase(t)) {
						user = true;
					} else if ("profile".equalsIgnoreCase(t)) {
						profile = true;
					}
				}
			}
	}

	/**
	 * @param pTree
	 * @return
	 * @throws KettleException
	 */
	public TreeNodeDataMeta getTransTree(RepositoryDirectory pTree)
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
				ButtonDataMeta addFolderBtn = new ButtonDataMeta(id
						+ ".btn.addDirectory", id + ".btn.addDirectory",
						new String[] { "file-folder-add" }, null, "添加目录", null);
				if (pTree.getID() != 0) {
					ButtonDataMeta deleteFolderBtn = new ButtonDataMeta(id
							+ ".btn.deleteDirectory", id
							+ ".btn.deleteDirectory",
							new String[] { "file-folder-delete" }, null,
							"删除目录", null);
					ButtonDataMeta editFolderBtn = new ButtonDataMeta(id
							+ ".btn.modifyDirectory", id
							+ ".btn.modifyDirectory",
							new String[] { "file-folder-edit" }, null,
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
					TreeNodeDataMeta subNode = getTransTree(cTree);
					if (subNode != null) {
						tree.putSubNode(subNode);
					}
				}
			}
			// 查找当前目录下的转换
			List<RepositoryObject> transList = this.rep
					.getTransformationObjects(pTree.getID());
			if (transList != null && transList.size() > 0) {
				// 遍历得到的转换
				for (RepositoryObject ro : transList) {
					TreeNodeDataMeta roLeft = new TreeNodeDataMeta(null,
							"file trans-file " + id + "-element", ro.getName(),
							ro.getDescription(), true, true);
					roLeft.putNodeExtend("rootId", id);
					roLeft.putNodeExtend("elType",
							RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
					roLeft.putNodeExtend("directoryId", pTree.getID());
					roLeft.putNodeExtend("directoryPath", pTree.getPath());
					roLeft.putNodeExtend("elName", ro.getName());
					roLeft.putNodeExtend("repId", ro.getRepId());

					if (modify) {
						ButtonDataMeta deleteBtn = new ButtonDataMeta(id
								+ ".btn.deleteTrans", id + ".btn.deleteTrans",
								new String[] { "file-object-delete" }, null,
								"删除", null);
						roLeft.putButtons(new ButtonDataMeta[] { deleteBtn });
					}
					tree.putSubNode(roLeft);
				}
			}
			return tree;
		}
		return null;
	}

	public TreeNodeDataMeta getJobTree(RepositoryDirectory pTree)
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
				ButtonDataMeta addFolderBtn = new ButtonDataMeta(id
						+ ".btn.addDirectory", id + ".btn.addDirectory",
						new String[] { "file-folder-add" }, null, "添加目录", null);
				if (pTree.getID() != 0) {
					ButtonDataMeta deleteFolderBtn = new ButtonDataMeta(id
							+ ".btn.deleteDirectory", id
							+ ".btn.deleteDirectory",
							new String[] { "file-folder-delete" }, null,
							"删除目录", null);
					ButtonDataMeta editFolderBtn = new ButtonDataMeta(id
							+ ".btn.modifyDirectory", id
							+ ".btn.modifyDirectory",
							new String[] { "file-folder-edit" }, null, "修改目录",
							null);
					tree.putButtons(new ButtonDataMeta[] { addFolderBtn,
							deleteFolderBtn, editFolderBtn });
				} else {
					tree.putButtons(new ButtonDataMeta[] { addFolderBtn });
				}
			}
			// 遍历子目录
			if (pTree.getChildren() != null && pTree.getChildren().size() > 0) {
				for (RepositoryDirectory cTree : pTree.getChildren()) {
					TreeNodeDataMeta subNode = getJobTree(cTree);
					if (subNode != null) {
						tree.putSubNode(subNode);
					}

				}
			}
			// 查找当前目录下的作业
			List<RepositoryObject> jobList = this.rep.getJobObjects(pTree
					.getID());
			if (jobList != null && jobList.size() > 0) {
				// 遍历得到的作业
				for (RepositoryObject ro : jobList) {
					TreeNodeDataMeta roLeft = new TreeNodeDataMeta(null,
							"file job-file " + id + "-element", ro.getName(),
							ro.getDescription(), true, true);
					roLeft.putNodeExtend("rootId", id);
					roLeft.putNodeExtend("elType",
							RepositoryObject.STRING_OBJECT_TYPE_JOB);
					roLeft.putNodeExtend("directoryId", pTree.getID());
					roLeft.putNodeExtend("directoryPath", pTree.getPath());
					roLeft.putNodeExtend("elName", ro.getName());
					roLeft.putNodeExtend("repId", ro.getRepId());

					if (modify) {
						ButtonDataMeta deleteBtn = new ButtonDataMeta(id
								+ ".btn.deleteJob", id + ".btn.deleteJob",
								new String[] { "file-object-delete" }, null,
								"删除", null);
						roLeft.putButtons(new ButtonDataMeta[] { deleteBtn });
					}
					tree.putSubNode(roLeft);
				}
			}
			return tree;
		}
		return null;
	}

	/**
	 * 创建资源库的树
	 * 
	 * @throws KettleException
	 */
	public void createRepTree() throws KettleException {
		TreeNodeDataMeta nodes_0 = new TreeNodeDataMeta(id + "_root",
				"db-folder", rep.getName(), rep.getName(), false, false);

		nodes_0.addInit("$('[id=treeRoot." + id
				+ "_root]').treeview({animated: 'fast',collapsed: false});");
		nodes_0
				.addInit("$('."
						+ id
						+ "-element').mouseover(jQuery.imenu.iMenuFn.file.detectRepBtn.itemMouseOver);");
		nodes_0
				.addInit("$('."
						+ id
						+ "-element').mouseout(jQuery.imenu.iMenuFn.file.detectRepBtn.itemMouseOut);");
		nodes_0
				.addInit("$('."
						+ id
						+ "-element').click(jQuery.imenu.iMenuFn.file.detectRepBtn.itemClick);");
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

		// 0 数据库连接
		if (database) {
			List<DatabaseMeta> dbs = rep.getDatabases();
			TreeNodeDataMeta dbNodes = new TreeNodeDataMeta(id + "_database",
					null, "数据库连接", "数据库连接", true, false);
			if (dbs != null) {
				for (DatabaseMeta db : dbs) {
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id
							+ ".database." + db.getID(), "database-file " + id
							+ "-element", db.getName(), db.getName(), false,
							true);
					tree.putNodeExtend("rootId", id);
					tree.putNodeExtend("elType", "database");
					tree.putNodeExtend("elId", db.getID());
					tree.putNodeExtend("elName", db.getName());

					if (modify) {
						ButtonDataMeta deleteBtn = new ButtonDataMeta(id
								+ ".btn.deleteDatabase", id
								+ ".btn.deleteDatabase",
								new String[] { "file-object-delete" }, null,
								"删除", null);
						tree.putButtons(new ButtonDataMeta[] { deleteBtn });
					}
					dbNodes.putSubNode(tree);
				}
			}
			nodes_0.putSubNode(dbNodes);

		}

		// 1 分区模式
		if (partition) {
			String[] partNames = rep.getPartitionSchemaNames();
			TreeNodeDataMeta partNodes = new TreeNodeDataMeta(
					id + "_partition", null, "分区模式", "分区模式", true, false);
			long partId;
			if (partNames != null) {
				for (String partName : partNames) {
					partId = rep.getPartitionSchemaID(partName);
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id
							+ ".partition." + partId, "partition-file " + id
							+ "-element", partName, partName, false, true);
					tree.putNodeExtend("rootId", id);
					tree.putNodeExtend("elType", "partition");
					tree.putNodeExtend("elId", partId);
					tree.putNodeExtend("elName", partName);
					partNodes.putSubNode(tree);
				}
			}
			nodes_0.putSubNode(partNodes);
		}

		// 2 从服务器
		if (slave) {
			List<SlaveServer> sServers = rep.getSlaveServers();
			TreeNodeDataMeta ssNodes = new TreeNodeDataMeta(id + "_slave",
					null, "从服务器", "从服务器", true, false);
			long slaveId;
			if (sServers != null) {
				for (SlaveServer sServer : sServers) {
					slaveId = sServer.getId();
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id + ".slave."
							+ slaveId, "slave-file " + id + "-element", sServer
							.getName(), sServer.getName(), false, true);
					tree.putNodeExtend("rootId", id);
					tree.putNodeExtend("elType", "slave");
					tree.putNodeExtend("elId", slaveId);
					tree.putNodeExtend("elName", sServer.getName());
					ssNodes.putSubNode(tree);
				}
			}
			nodes_0.putSubNode(ssNodes);
		}

		// 3 集群
		if (cluster) {
			String[] clusterNames = rep.getClusterNames();
			TreeNodeDataMeta cNodes = new TreeNodeDataMeta(id + "_cluster",
					null, "集群", "集群", true, false);
			long clusterId;
			if (clusterNames != null) {
				for (String clusterName : clusterNames) {
					clusterId = rep.getClusterID(clusterName);
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id
							+ ".cluster." + clusterId, "cluster-file " + id
							+ "-element", clusterName, clusterName, false, true);
					tree.putNodeExtend("rootId", id);
					tree.putNodeExtend("elType", "cluster");
					tree.putNodeExtend("elId", clusterId);
					tree.putNodeExtend("elName", clusterName);
					cNodes.putSubNode(tree);
				}
			}
			nodes_0.putSubNode(cNodes);
		}

		// 4 转换
		if (trans) {
			TreeNodeDataMeta transNodes = new TreeNodeDataMeta(id + "_trans",
					null, "转换", "转换", true, false);

			transNodes.putSubNode(getTransTree(directory));

			nodes_0.putSubNode(transNodes);

		}
		// 5 作业
		if (job) {
			TreeNodeDataMeta jobNodes = new TreeNodeDataMeta(id + "_job", null,
					"作业", "作业", true, false);
			jobNodes.putSubNode(getJobTree(directory));

			nodes_0.putSubNode(jobNodes);
		}

		// 6 用户
		if (user) {
			String[] userLogins = rep.getUserLogins();
			TreeNodeDataMeta userNodes = new TreeNodeDataMeta(id + "_user",
					null, "用户", "用户", true, false);
			long userId;
			if (userLogins != null) {
				for (String userLogin : userLogins) {
					userId = rep.getUserID(userLogin);
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id + ".user."
							+ userId, "user-file " + id + "-element",
							userLogin, userLogin, false, true);
					tree.putNodeExtend("rootId", id);
					tree.putNodeExtend("elType", "user");
					tree.putNodeExtend("elId", userId);
					tree.putNodeExtend("elName", userLogin);
					userNodes.putSubNode(tree);
				}
			}
			nodes_0.putSubNode(userNodes);
		}

		// 7 档案
		if (profile) {
			String[] profiles = rep.getProfiles();
			TreeNodeDataMeta profilesNodes = new TreeNodeDataMeta(id
					+ "_profile", null, "档案", "档案", true, false);
			long profileId;
			if (profiles != null) {
				for (String profile : profiles) {
					profileId = rep.getProfileID(profile);
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id
							+ ".profile." + profileId, "profile-file " + id
							+ "-element", profile, profile, false, true);
					tree.putNodeExtend("rootId", id);
					tree.putNodeExtend("elType", "profile");
					tree.putNodeExtend("elId", profileId);
					tree.putNodeExtend("elName", profile);
					profilesNodes.putSubNode(tree);
				}
			}
			nodes_0.putSubNode(profilesNodes);
		}

		this.dbTree = new TreeMeta(new TreeNodeDataMeta[] { nodes_0 });
		this.dbTree.setRootTree(true);
	}

	public TreeMeta getDbTree() {
		return dbTree;
	}

	public void setDbTree(TreeMeta dbTree) {
		this.dbTree = dbTree;
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			// 资源库的树
			DivMeta dbDiv = new DivMeta(new NullSimpleFormDataMeta(), true);

			DivMeta dbTreeDiv = new DivMeta(new SimpleFormDataMeta(
					"repExplorerTreeDiv", null,
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
			this.flush.putProperty("showTypes", showTypes);
			this.flush.putProperty("modify", modify);
			this.flush.addClick("jQuery.imenu.iMenuFn.file.detectRepBtn.flush");
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

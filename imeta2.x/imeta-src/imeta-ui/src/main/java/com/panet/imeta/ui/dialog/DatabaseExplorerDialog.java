package com.panet.imeta.ui.dialog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.tree.TreeMeta;
import com.panet.iform.forms.tree.TreeNodeDataMeta;
import com.panet.imeta.core.database.Catalog;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.database.DatabaseMetaInformation;
import com.panet.imeta.core.database.Schema;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.trans.BaseDatabaseDialog;
import com.panet.imeta.trans.DatabaseDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class DatabaseExplorerDialog extends BaseDatabaseDialog implements
		DatabaseDialogInterface {
	private static final String STRING_CATALOG = "Catalog";
	private static final String STRING_SCHEMAS = "Schemas";
	private static final String STRING_TABLES = "Tables";
	private static final String STRING_VIEWS = "Views";
	private static final String STRING_SYNONYMS = "Synonyms";

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	private TreeMeta dbTree;
	private ButtonMeta okBtn, flushBtn, previewFirst100, previewFirstN;

	public DatabaseExplorerDialog(DatabaseMeta databaseMeta, Repository rep) {
		super(databaseMeta, rep);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			String id = this.getId();
			id = id + "_explorer";

			DatabaseMeta dbMeta = super.getDatabaseMeta();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 数据库schema的树
			DivMeta dbDiv = new DivMeta(new NullSimpleFormDataMeta(), true);

			DivMeta dbTreeDiv = new DivMeta(new SimpleFormDataMeta(null, null,
					new String[] { "toolbar-content" }, null, null, null),
					false);
			dbTreeDiv.setStyle("width", "65%");
			dbTreeDiv.setStyle("height", "400px");
			dbTreeDiv.setStyle("float", "left");
			dbTreeDiv.appendTo(dbDiv);

			// 按钮
			DivMeta dbBtnDiv = new DivMeta(new NullSimpleFormDataMeta(), false);
			dbBtnDiv.setStyle("width", "33%");
			dbBtnDiv.setStyle("padding-left", "10px");
			dbBtnDiv.setStyle("float", "left");
			dbBtnDiv.appendTo(dbDiv);

			// TODO
			this.previewFirst100 = new ButtonMeta(id + ".btn.previewFirst100",
					id + ".btn.previewFirst100", "预览前100行", "预览前100行");
			this.previewFirst100
					.addClick("jQuery.imeta.db.dbexplorer.btn.previewFirst100");
			this.previewFirst100.setButtonWidthStyle(99);
			this.previewFirst100.setDisabled(true);
			dbBtnDiv.append(this.previewFirst100);

			this.previewFirstN = new ButtonMeta(id + ".btn.previewFirstN", id
					+ ".btn.previewFirstN", "预览前n行", "预览前n行");
			this.previewFirstN
					.addClick("jQuery.imeta.db.dbexplorer.btn.previewFirstN");
			this.previewFirstN.setButtonWidthStyle(99);
			this.previewFirstN.setDisabled(true);
			dbBtnDiv.append(this.previewFirstN);

			TreeNodeDataMeta nodes_0 = new TreeNodeDataMeta(id + "_root",
					"db-folder", dbMeta.getName(), dbMeta.getName(), false,
					false);
			nodes_0
					.addInit("$('[id=treeRoot."
							+ id
							+ "_root]').treeview({animated: 'fast',collapsed: false});");

			DatabaseMetaInformation dmi = new DatabaseMetaInformation(dbMeta);
			dmi.getData(null);

			// Catalogs
			Catalog[] catalogs = dmi.getCatalogs();
			if (catalogs != null) {
				TreeNodeDataMeta subNode = new TreeNodeDataMeta(
						id + "_catalog", null, STRING_CATALOG, STRING_CATALOG,
						true, false);
				String cataStr;
				for (Catalog cata : catalogs) {
					cataStr = cata.getCatalogName();
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id + "."
							+ cataStr, "catalog-file " + id + "-catalog",
							cataStr, cataStr, false, true);
					subNode.putSubNode(tree);
				}

				nodes_0.putSubNode(subNode);
			}

			// Schemas
			Schema[] schemas = dmi.getSchemas();
			if (schemas != null) {
				TreeNodeDataMeta subNode = new TreeNodeDataMeta(id + "_schema",
						null, STRING_SCHEMAS, STRING_SCHEMAS, true, false);
				String sStr;
				for (Schema s : schemas) {
					sStr = s.getSchemaName();
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id + "."
							+ sStr, "schema-file " + id + "-schema", sStr,
							sStr, false, true);
					subNode.putSubNode(tree);
				}

				nodes_0.putSubNode(subNode);
			}

			// Tables
			String[] tabnames = dmi.getTables();
			if (tabnames != null) {
				TreeNodeDataMeta subNode = new TreeNodeDataMeta(id + "_table",
						null, STRING_TABLES, STRING_TABLES, true, false);

				for (String tab : tabnames) {
					TreeNodeDataMeta tree = new TreeNodeDataMeta(
							id + "." + tab, "table-file " + id + "-table", tab,
							tab, false, true);

					tree.putNodeExtend("rootId", id);
					subNode.putSubNode(tree);
				}

				nodes_0.putSubNode(subNode);

				nodes_0
						.addInit("$('."
								+ id
								+ "-table').mouseover(jQuery.imeta.db.dbexplorer.listeners.itemMouseOver);");
				nodes_0
						.addInit("$('."
								+ id
								+ "-table').mouseout(jQuery.imeta.db.dbexplorer.listeners.itemMouseOut);");
				nodes_0
						.addInit("$('."
								+ id
								+ "-table').click(jQuery.imeta.db.dbexplorer.listeners.itemClick);");
			}

			// Views
			String[] views = dmi.getViews();
			if (views != null) {
				TreeNodeDataMeta subNode = new TreeNodeDataMeta(id + "_view",
						null, STRING_VIEWS, STRING_VIEWS, true, false);

				for (String view : views) {
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id + "."
							+ view, "view-file " + id + "-view", view, view,
							false, true);
					subNode.putSubNode(tree);
				}

				nodes_0.putSubNode(subNode);
			}

			// Synonyms
			String[] syn = dmi.getSynonyms();
			if (syn != null) {
				TreeNodeDataMeta subNode = new TreeNodeDataMeta(id + "_syn",
						null, STRING_SYNONYMS, STRING_SYNONYMS, true, false);

				for (String s : syn) {
					TreeNodeDataMeta tree = new TreeNodeDataMeta(id + "." + s,
							"syn-file " + id + "-syn", s, s, false, true);
					subNode.putSubNode(tree);
				}

				nodes_0.putSubNode(subNode);
			}

			this.dbTree = new TreeMeta(new TreeNodeDataMeta[] { nodes_0 });
			this.dbTree.setRootTree(true);

			dbTreeDiv.append(this.dbTree);

			this.columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { dbDiv });

			this.okBtn = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定",
					"确定");
			this.okBtn.addClick("jQuery.imeta.db.dbexplorer.btn.ok");

			this.flushBtn = new ButtonMeta(id + ".btn.flush",
					id + ".btn.flush", "刷新", "刷新");
			this.flushBtn.addClick("jQuery.imeta.db.dbexplorer.btn.flush");
			this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					this.okBtn, this.flushBtn });

			cArr.add(this.columnFormMeta.getFormJo());
			rtn.put("items", cArr);

			rtn.put("title", this.getDatabaseMeta().getName() + "设置");

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}

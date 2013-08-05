package com.panet.imeta.ui.dialog;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridRowDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.job.JobEntryLoader;
import com.panet.imeta.ui.exception.ImetaException;

public class EntryPluginDialog {
	private String id;

	private ColumnFormMeta columnFormMeta;

	private ColumnFormDataMeta columnFormDataMeta;

	private LabelGridMeta EntryPluginList;
	public EntryPluginDialog() throws KettleException {
	}

	public JSONObject open() throws ImetaException {
		try {

			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			
			List<Object[]> pluginInformation = JobEntryLoader.getInstance().getPluginInformation();
			this.id = "EntryPluginList";

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			this.EntryPluginList = new LabelGridMeta(id + "_EntryPluginList", "可以使用的作业项插件", 400);
			this.EntryPluginList.addHeaders(new GridHeaderDataMeta[] {

					new GridHeaderDataMeta(this.id + "#", "#", null,false, 50),
					new GridHeaderDataMeta(this.id + "TypeDesc", "类型", null, false,100),
					new GridHeaderDataMeta(this.id + "ID", "ID", null,false, 150),
					new GridHeaderDataMeta(this.id + "Description", "描述", null,false, 200),
					new GridHeaderDataMeta(this.id + "Tooltip", "提示", null, false,400),
					new GridHeaderDataMeta(this.id + "Directory", "目录", null,false, 100),
					new GridHeaderDataMeta(this.id + "JarfilesList", "Jar文件类表", null,false, 150),
					new GridHeaderDataMeta(this.id + "IconFilename", "图标文件", null, false,100),
					new GridHeaderDataMeta(this.id + "Classname", "类名", null,false, 260),
					new GridHeaderDataMeta(this.id + "Category", "步骤类别", null,false, 100),
					new GridHeaderDataMeta(this.id + "ErrorHelpFile", "错误帮助文件", null, false,150),
					new GridHeaderDataMeta(this.id + "isSeparateClassloaderNeeded", "分离的类加载器？", null,false, 150),
			});
			for (int i=0;i<pluginInformation.size();i++) {
				int index = 0;
		        Object[] stepPlugin = (Object[])pluginInformation.get(i);
				GridRowDataMeta rowDataMeta = new GridRowDataMeta(12);
				rowDataMeta.setCell(index++,i+1+"");
				rowDataMeta.setCell(index++,stepPlugin[0]!=null?stepPlugin[0].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[1]!=null?stepPlugin[1].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[2]!=null?stepPlugin[2].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[3]!=null?stepPlugin[3].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[4]!=null?stepPlugin[4].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[5]!=null?stepPlugin[5].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[6]!=null?stepPlugin[6].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[7]!=null?stepPlugin[7].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[8]!=null?stepPlugin[8].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[9]!=null?stepPlugin[9].toString():"" );
				rowDataMeta.setCell(index++,stepPlugin[10].toString()=="true"?"Y":"N" );
				
				this.EntryPluginList.addRow(rowDataMeta);
			}
			this.EntryPluginList.setSingle(true);

			this.columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.EntryPluginList });

			cArr.add(this.columnFormMeta.getFormJo());

			rtn.put("items", cArr);

			rtn.put("title", "作业项插件列表");
			rtn.put("id", this.id);

			return rtn;

		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}

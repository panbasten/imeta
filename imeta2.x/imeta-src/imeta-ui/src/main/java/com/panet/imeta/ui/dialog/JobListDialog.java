package com.panet.imeta.ui.dialog;

import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridRowDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.job.Job;
import com.panet.imeta.ui.exception.ImetaException;

public class JobListDialog implements BaseDialogInterface {

	private String id;

	private ColumnFormMeta columnFormMeta;

	private ColumnFormDataMeta columnFormDataMeta;

	private LabelGridMeta jobList;
	Map<String, Job> activeJobsMap;

	public JobListDialog(Map<String, Job> activeJobsMap) throws KettleException {
		this.activeJobsMap = activeJobsMap;
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {

			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			this.id = "joblist";

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			this.jobList = new LabelGridMeta(id + "_userlist", "作业列表", 300);
			this.jobList.addHeaders(new GridHeaderDataMeta[] {

					new GridHeaderDataMeta(this.id + "jobname", "作业名", null,
							false, 220),
					new GridHeaderDataMeta(this.id + "jobstat", "状态", null,
							false, 220),
					new GridHeaderDataMeta(this.id + "changestat", "操作", null,
							false, 100),

			});
			String jobstat = "", key;
			boolean running = false;
			Job job;
			for (Iterator<String> keys = activeJobsMap.keySet().iterator(); keys
					.hasNext();) {
				key = keys.next();
				job = activeJobsMap.get(key);
				if (job.isActive() || job.isAlive()) {
					jobstat = "正在运行";
					running = true;
				} else {
					jobstat = "已经停止";
					running = false;
				}

				GridCellDataMeta settingBtn = null;

				String name = job.getJobMeta().getDirectory().getPath();
				if ("/".equals(name)) {
					name = name + job.getJobname();
				} else {
					name = name + "/" + job.getJobname();
				}

				if (running) {
					settingBtn = new GridCellDataMeta("setting", "",
							GridCellDataMeta.CELL_TYPE_BUTTON);
					settingBtn
							.setCellClickFn("jQuery.imenu.iMenuFn.start.jobListStop(\""
									+ job.getJobMeta().getDirectory().getID()
									+ "\",\"" + job.getJobname() + "\");");
					settingBtn
							.setSrc("framework/images/icons/control_stop_blue.png");
				}

				this.jobList.addRow(new Object[] { name, jobstat,
						(settingBtn == null) ? "" : settingBtn });
			}
			this.jobList.setSingle(true);

			this.columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.jobList });

			cArr.add(this.columnFormMeta.getFormJo());

			rtn.put("items", cArr);

			rtn.put("title", "作业列表");
			rtn.put("id", this.id);

			return rtn;

		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}

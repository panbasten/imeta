package com.panet.imeta.ui.dialog;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.job.Job;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.impl.ImetaJobService;

public class JobStartDialog implements BaseDialogInterface {
	private String id;

	private ColumnFormMeta columnFormMeta;

	private ColumnFormDataMeta columnFormDataMeta;

	private LabelGridMeta jobList;

	private Map<String, Job> activeJobsMap;

	private Map<String, String> autoRunJobs;

	private Repository rep;

	public JobStartDialog(Repository rep, Map<String, Job> activeJobsMap,
			Map<String, String> autoRunJobs) throws KettleException {
		this.rep = rep;
		this.activeJobsMap = activeJobsMap;
		this.autoRunJobs = autoRunJobs;
	}

	private void putSubJobs(RepositoryDirectory rep) throws KettleException {
		// 子目录
		if (rep.getChildren() != null && rep.getChildren().size() > 0) {
			for (RepositoryDirectory subDir : rep.getChildren()) {
				if (subDir != null) {
					putSubJobs(subDir);
				}
			}
		}
		// 查找该目录
		List<RepositoryObject> list = this.rep.getJobObjects(rep.getID());
		for (RepositoryObject obj : list) {
			String jobstat = "未运行";

			String name;
			if ("/".equals(rep.getPath())) {
				name = rep.getPath() + obj.getName();
			} else {
				name = rep.getPath() + "/" + obj.getName();
			}

			String key = this.rep.getName() + "." + rep.getID() + "."
					+ obj.getName();
			Job job = activeJobsMap.get(key);
			if (job != null) {
				if (job.isActive() || job.isAlive()) {
					jobstat = "正在运行";
				}
				if (!job.isActive() || !job.isAlive()) {
					jobstat = "已经停止";
				}
			}

			GridCellDataMeta settingBtn = new GridCellDataMeta("setting", "",
					GridCellDataMeta.CELL_TYPE_BUTTON);
			settingBtn
					.setCellClickFn("jQuery.imenu.iMenuFn.wizard.jobStartFn.btn.settingOpen(\""
							+ rep.getID()
							+ "\",\""
							+ obj.getRepId()
							+ "\",\""
							+ obj.getName()
							+ "\");");
			settingBtn.setSrc("framework/images/icons/asterisk_orange.png");

			this.jobList.addRow(new Object[] {
					name,
					jobstat,
					new GridCellDataMeta("" + obj.getRepId(), String
							.valueOf(ImetaJobService.AUTO_RUN_TYPE_AUTO
									.equals(autoRunJobs.get(this.rep.getName()
											+ "." + obj.getRepId()))),
							GridCellDataMeta.CELL_TYPE_CHECKBOX), settingBtn });
		}
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {

			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			this.id = "jobStartDialog";

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			this.jobList = new LabelGridMeta(id + "_list", "任务列表", 300);
			this.jobList.addHeaders(new GridHeaderDataMeta[] {

					new GridHeaderDataMeta(this.id + "_jobname", "任务名", null,
							false, 220),
					new GridHeaderDataMeta(this.id + "_jobstat", "状态", null,
							false, 220),
					new GridHeaderDataMeta(this.id + "_autostart", "自动启动",
							null, false, 100),
					new GridHeaderDataMeta(this.id + "_oper", "配置参数", null,
							false, 100)

			});

			putSubJobs(new RepositoryDirectory(rep));
			this.jobList.setSingle(true);

			this.columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.jobList });

			ButtonMeta okBtn = new ButtonMeta(id + ".btn.ok", id + ".btn.ok",
					"确定", "确定");
			okBtn.addClick("jQuery.imenu.iMenuFn.wizard.jobStartFn.btn.ok");

			ButtonMeta cancelBtn = new ButtonMeta(id + ".btn.cancel", id
					+ ".btn.cancel", "取消", "取消");
			cancelBtn
					.addClick("jQuery.imenu.iMenuFn.wizard.jobStartFn.btn.cancel");

			this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					okBtn, cancelBtn });

			cArr.add(this.columnFormMeta.getFormJo());

			rtn.put("items", cArr);

			rtn.put("title", "任务列表");
			rtn.put("id", this.id);

			return rtn;

		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}

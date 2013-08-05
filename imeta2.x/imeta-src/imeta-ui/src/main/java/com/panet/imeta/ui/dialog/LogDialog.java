package com.panet.imeta.ui.dialog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class LogDialog implements BaseDialogInterface {

	private MenuTabMeta meta;

	/**
	 * 执行历史
	 */
	private GridMeta executeHistory;

	/**
	 * 日志
	 */
	private DivMeta showLog;

	/**
	 * 步骤规格
	 */
	private GridMeta stepMetrics;

	/**
	 * 性能图
	 */
	private DivMeta capabilityChart;

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			// String id = this.getId();
			String id = "log";

			// 得到tab
			this.meta = new MenuTabMeta(id, new String[] { "执行历史", "日志",
					"步骤规格", "性能图" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 0 执行历史
			 ******************************************************************/
			this.executeHistory = new GridMeta(id + ".executeHistory", 140, 10,
					true);
			this.executeHistory.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(null, "序号", null, true, 28),
					new GridHeaderDataMeta(null, "转换名称", null, true, 50),
					new GridHeaderDataMeta(null, "批次ID", null, true, 50),
					new GridHeaderDataMeta(null, "状态", null, true, 50),
					new GridHeaderDataMeta(null, "持续", null, true, 50),
					new GridHeaderDataMeta(null, "读", null, true, 50),
					new GridHeaderDataMeta(null, "写", null, true, 50),
					new GridHeaderDataMeta(null, "更新", null, true, 50),
					new GridHeaderDataMeta(null, "输入", null, true, 50),
					new GridHeaderDataMeta(null, "输出", null, true, 50),
					new GridHeaderDataMeta(null, "错误", null, true, 50),
					new GridHeaderDataMeta(null, "开始日期", null, true, 50),
					new GridHeaderDataMeta(null, "结束日期", null, true, 50),
					new GridHeaderDataMeta(null, "日志日期", null, true, 50),
					new GridHeaderDataMeta(null, "依赖日期", null, true, 50),
					new GridHeaderDataMeta(null, "重放日期", null, true, 50), });

			this.meta.putTabContent(0,
					new BaseFormMeta[] { this.executeHistory, });

			/*******************************************************************
			 * 1 日志
			 ******************************************************************/
			this.showLog = new DivMeta(new NullSimpleFormDataMeta(), true);
			this.meta.putTabContent(1, new BaseFormMeta[] { this.showLog, });

			/*******************************************************************
			 * 2 步骤规格
			 ******************************************************************/
			this.stepMetrics = new GridMeta(id + ".executeHistory", 140, 10,
					true);
			this.stepMetrics
					.addHeaders(new GridHeaderDataMeta[] {
							new GridHeaderDataMeta(null, "序号", null, true, 28),
							new GridHeaderDataMeta(null, "步骤名称", null, true, 50),
							new GridHeaderDataMeta(null, "复制的记录行数", null, true,
									80),
							new GridHeaderDataMeta(null, "读", null, true, 50),
							new GridHeaderDataMeta(null, "写", null, true, 50),
							new GridHeaderDataMeta(null, "输入", null, true, 50),
							new GridHeaderDataMeta(null, "输出", null, true, 50),
							new GridHeaderDataMeta(null, "更新", null, true, 50),
							new GridHeaderDataMeta(null, "拒绝", null, true, 50),
							new GridHeaderDataMeta(null, "错误", null, true, 50),
							new GridHeaderDataMeta(null, "激活", null, true, 50),
							new GridHeaderDataMeta(null, "时间", null, true, 50),
							new GridHeaderDataMeta(null, "速度（条记录/秒）", null,
									true, 80),
							new GridHeaderDataMeta(null, "优先级/输入/输出", null,
									true, 80), });

			this.meta
					.putTabContent(2, new BaseFormMeta[] { this.stepMetrics, });

			/*******************************************************************
			 * 3 性能图
			 ******************************************************************/
			this.capabilityChart = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			this.meta.putTabContent(3,
					new BaseFormMeta[] { this.capabilityChart, });

			cArr.add(this.meta.getFormJo());
			rtn.put("items", cArr);

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}

package com.panet.imeta.job.entries.special;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntrySpecialDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	private final static String NOSCHEDULING = Messages
			.getString("JobSpecial.Type.NoScheduling");

	private final static String INTERVAL = Messages
			.getString("JobSpecial.Type.Interval");

	private final static String DAILY = Messages
			.getString("JobSpecial.Type.Daily");

	private final static String WEEKLY = Messages
			.getString("JobSpecial.Type.Weekly");

	private final static String MONTHLY = Messages
			.getString("JobSpecial.Type.Monthly");

	private final static String SUNDAY = Messages
			.getString("JobSpecial.DayOfWeek.Sunday");
	private final static String MONDAY = Messages
			.getString("JobSpecial.DayOfWeek.Monday");
	private final static String TUESDAY = Messages
			.getString("JobSpecial.DayOfWeek.Tuesday");
	private final static String WEDNESDAY = Messages
			.getString("JobSpecial.DayOfWeek.Wednesday");
	private final static String THURSDAY = Messages
			.getString("JobSpecial.DayOfWeek.Thursday");
	private final static String FRIDAY = Messages
			.getString("JobSpecial.DayOfWeek.Friday");
	private final static String SATURDAY = Messages
			.getString("JobSpecial.DayOfWeek.Saturday");

	private final static String[] week = new String[] { SUNDAY, MONDAY,
			TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY };

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	// private LabelInputMeta name;

	private LabelInputMeta repeat;

	private LabelSelectMeta schedulerType;

	private LabelInputMeta intervalSecond;

	private LabelInputMeta intervalMinute;

	private LabelInputMeta timeOfDay;

	private LabelSelectMeta dayOfWeek;

	private LabelSelectMeta dayOfMonth;

	private LabelInputMeta start;

	public JobEntrySpecialDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntrySpecial entry = (JobEntrySpecial) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 重复
			this.repeat = new LabelInputMeta(id + ".repeat", "重复", null, null,
					null, String.valueOf(entry.isRepeat()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 是否为开始
			this.start = new LabelInputMeta(id + ".start", "是否为开始", null, null,
					null, String.valueOf(true),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.start.setReadonly(true);

			// 类型
			List<OptionDataMeta> typeOptionList = new ArrayList<OptionDataMeta>();
			typeOptionList.add(new OptionDataMeta(String
					.valueOf(JobEntrySpecial.NOSCHEDULING), NOSCHEDULING));
			typeOptionList.add(new OptionDataMeta(String
					.valueOf(JobEntrySpecial.INTERVAL), INTERVAL));
			typeOptionList.add(new OptionDataMeta(String
					.valueOf(JobEntrySpecial.DAILY), DAILY));
			typeOptionList.add(new OptionDataMeta(String
					.valueOf(JobEntrySpecial.WEEKLY), WEEKLY));
			typeOptionList.add(new OptionDataMeta(String
					.valueOf(JobEntrySpecial.MONTHLY), MONTHLY));
			long typeV = entry.getSchedulerType();
			this.schedulerType = new LabelSelectMeta(id + ".schedulerType",
					"类型", null, null, null, String.valueOf(typeV), null,
					typeOptionList);
			this.schedulerType.setSingle(true);
			this.schedulerType.addListener("change",
					"jQuery.imeta.jobEntries.special.schedulerTypeChange");

			// 以秒计算的时间间隔
			String sec;
			if (typeV != JobEntrySpecial.INTERVAL) {
				sec = "0";
			} else {
				sec = String.valueOf(entry.getIntervalSeconds());
			}
			this.intervalSecond = new LabelInputMeta(id + ".intervalSecond",
					"以秒计算的间隔", null, null, null, sec, null, ValidateForm
							.getInstance().setNumberDE(true));
			if (typeV != JobEntrySpecial.INTERVAL) {
				this.intervalSecond.setDisabled(true);
			}

			// 以分计算的时间间隔
			String min;
			if (typeV != JobEntrySpecial.INTERVAL) {
				min = "60";
			} else {
				min = String.valueOf(entry.getIntervalMinutes());
			}
			this.intervalMinute = new LabelInputMeta(id + ".intervalMinute",
					"以分计算的间隔", null, null, null, min, null, ValidateForm
							.getInstance().setNumberDE(true));
			if (typeV != JobEntrySpecial.INTERVAL) {
				this.intervalMinute.setDisabled(true);
			}

			// 一天的某个时间
			String day;
			if (typeV != JobEntrySpecial.DAILY) {
				day = "12：0";
			} else {
				day = String
						.valueOf(entry.getHour() + ":" + entry.getMinutes());
			}
			this.timeOfDay = new LabelInputMeta(id + ".timeOfDay",
					"一天的某个时间(HH:MM)", null, null, "格式：(HH:MM)", day, null, null);
			this.timeOfDay.setSingle(true);
			if (typeV != JobEntrySpecial.DAILY) {
				this.timeOfDay.setDisabled(true);
			}

			// 一周的某天
			String wee;
			if (typeV != JobEntrySpecial.WEEKLY) {
				wee = "星期一";
			} else {
				wee = String.valueOf(entry.getWeekDay());
			}
			List<OptionDataMeta> dayOfWeekList = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < week.length; i++) {
				dayOfWeekList
						.add(new OptionDataMeta(String.valueOf(i), week[i]));
			}
			this.dayOfWeek = new LabelSelectMeta(id + ".dayOfWeek", "一周的某天",
					null, null, null, wee, null, dayOfWeekList);
			this.dayOfWeek.setSingle(true);
			if (typeV != JobEntrySpecial.WEEKLY) {
				this.dayOfWeek.setDisabled(true);
			}

			// 一月的某天
			List<OptionDataMeta> dayOfMonthList = new ArrayList<OptionDataMeta>();
			for (int i = 1; i <= 31; i++) {
				dayOfMonthList.add(new OptionDataMeta(String.valueOf(i), String
						.valueOf(i)));
			}
			this.dayOfMonth = new LabelSelectMeta(id + ".dayOfMonth", "一月的某天",
					null, null, null, String.valueOf(entry.getDayOfMonth()),
					null, dayOfMonthList);
			this.dayOfMonth.setSingle(true);
			if (typeV != JobEntrySpecial.MONTHLY) {
				this.dayOfMonth.setDisabled(true);
			}

			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					// this.name,
					this.repeat, this.start, this.schedulerType,
					this.intervalSecond, this.intervalMinute, this.timeOfDay,
					this.dayOfWeek, this.dayOfMonth });

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}

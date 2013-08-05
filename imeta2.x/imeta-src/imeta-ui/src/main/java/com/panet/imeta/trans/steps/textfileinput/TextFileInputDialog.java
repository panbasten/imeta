package com.panet.imeta.trans.steps.textfileinput;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class TextFileInputDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 步骤名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	// 文件 0

	/**
	 * 文件或目录
	 */
	// private LabelInputMeta fieldsorcatalog;
	// private ButtonMeta add;
	// private ButtonMeta browse;
	/**
	 * 规则表达式
	 */
	// private LabelInputMeta codexexpression;
	/**
	 * 选中的文件
	 */
	private LabelGridMeta pitch;

	// private ButtonMeta delete;
	//
	// private ButtonMeta edit;

	/**
	 * 从以前的步骤接受文件名
	 */
	private LabelInputMeta acceptingFilenames;

	/**
	 * 通过以前的步骤接收字段
	 */
	private LabelInputMeta passingThruFields;

	/**
	 * 步骤读取的文件名来自
	 */
	private LabelSelectMeta acceptingStepName;

	/**
	 * 在输入里的字段被当做文件名
	 */
	private LabelInputMeta acceptingField;

	// /**
	// * 显示文件名
	// */
	// private ButtonMeta showname;
	//
	// /**
	// * 显示文件内容
	// */
	// private ButtonMeta showcontent;
	//
	// /**
	// * 显示来自第一条数据行的内容
	// */
	// private ButtonMeta showfirstlinecontent;

	// 内容 1

	/**
	 * 文件类型
	 */
	private LabelSelectMeta fileType;

	/**
	 * 分隔符
	 */
	private LabelInputMeta separator;

	/**
	 * 插入TAB
	 */
//	private ButtonMeta inserttab;

	/**
	 * 文本限定符
	 */
	private LabelInputMeta enclosure;

	/**
	 * 在文本限定符里允许换行？
	 */
	private LabelInputMeta breakInEnclosureAllowed;

	/**
	 * 逃逸字符
	 */
	private LabelInputMeta escapeCharacter;

	/**
	 * 头部
	 */
	private LabelInputMeta header;

	/**
	 * 头部行数量
	 */
	private LabelInputMeta nrHeaderLines;

	/**
	 * 尾部
	 */
	private LabelInputMeta footer;

	/**
	 * 尾部行数量
	 */
	private LabelInputMeta nrFooterLines;

	/**
	 * 包装行？
	 */
	private LabelInputMeta lineWrapped;

	/**
	 * 以时间包装的行数
	 */
	private LabelInputMeta nrWraps;

	/**
	 * 分页布局？
	 */
	private LabelInputMeta layoutPaged;

	/**
	 * 每页记录行数
	 */
	private LabelInputMeta nrLinesPerPage;

	/**
	 * 文档头部行
	 */
	private LabelInputMeta nrLinesDocHeader;

	/**
	 * 压缩
	 */
	private LabelSelectMeta fileCompression;

	/**
	 * 没有空行？
	 */
	private LabelInputMeta noEmptyLines;

	/**
	 * 在输出包括字段名？
	 */
	private LabelInputMeta includeFilename;

	/**
	 * 包含文件名的字段名称
	 */
	private LabelInputMeta filenameField;

	/**
	 * 输出包含行数？
	 */
	private LabelInputMeta includeRowNumber;

	/**
	 * 行数字段名称
	 */
	private LabelInputMeta rowNumberByFile;

	/**
	 * 按文件取行号
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * 格式
	 */
	private LabelSelectMeta fileFormat;

	/**
	 * 编码方式
	 */
	private LabelSelectMeta encoding;

	/**
	 * 记录数量限制
	 */
	private LabelInputMeta rowLimit;

	/**
	 * 解析日期时是否严格要求？
	 */
	private LabelInputMeta dateFormatLenient;

	/**
	 * 本地日期格式
	 */
	private LabelSelectMeta dateLocale;

	/**
	 * 结果文件名
	 */
	private ColumnFieldsetMeta addResultFilenameColum;

	/**
	 * 添加文件名？
	 */
	private LabelInputMeta isaddresult;

	// 错误处理 3

	/**
	 * 忽略错误
	 */
	private LabelInputMeta errorIgnored;
	/**
	 * 跳过错误行
	 */
	private LabelInputMeta errorLineSkipped;

	/**
	 * 错误计数字段
	 */
	private LabelInputMeta errorCountField;

	/**
	 * 错误文件名
	 */
	private LabelInputMeta errorFieldsField;

	/**
	 * 错误文本字段
	 */
	private LabelInputMeta errorTextField;

	/**
	 * 告警文件目录
	 */
	private LabelInputMeta warningFilesDestinationDirectory;

	/**
	 * 扩展名a
	 */
	private LabelInputMeta warningFilesExtension;

	// private ButtonMeta variable;

	/**
	 * 错误文件目录
	 */
	private LabelInputMeta errorFilesDestinationDirectory;

	/**
	 * 扩展名b
	 */
	private LabelInputMeta errorFilesExtension;

	/**
	 * 失败行数文件目录
	 */
	private LabelInputMeta lineNumberFilesDestinationDirectory;

	/**
	 * 扩展名c
	 */
	private LabelInputMeta lineNumberFilesExtension;

	// 字段 3
	/**
	 * 过滤
	 */
	private LabelGridMeta sift;

	// 字段 4
	/**
	 * 获取字段
	 */

	private LabelGridMeta fields;

//	private ButtonMeta getfields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public TextFileInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			TextFileInputMeta step = (TextFileInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "错误处理",
					"过滤", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0
			 ******************************************************************/
			// 文件或目录
			// this.fieldsorcatalog = new LabelInputMeta(id +
			// ".fieldsorcatalog", "文件或目录", null, null,
			// "字段必须填写", String.valueOf(step.getFileName()), null, ValidateForm
			// .getInstance().setRequired(true));
			// this.fieldsorcatalog.setSingle(true);
			// this.fieldsorcatalog.setDisabled(step.isAcceptingFilenames());
			// this.add = new ButtonMeta(id + ".btn.add", id
			// + ".btn.add", "增加", "增加");
			// this.add.setDisabled(!step.isAcceptingFilenames());
			// this.browse = new ButtonMeta(id + ".btn.browse", id
			// + ".btn.browse", "浏览", "浏览");
			// this.browse.setDisabled(!step.isAcceptingFilenames());
			//
			// this.fieldsorcatalog.addButton(new ButtonMeta[] {
			// this.add,this.browse});
			// 规则表达式
			//
			// this.codexexpression = new LabelInputMeta(id +
			// ".codexexpression", "规则表达式", null, null,
			// "规则表达式必须填写", String.valueOf(step.getFileMask()), null,
			// ValidateForm
			// .getInstance().setRequired(true));
			// this.codexexpression.setSingle(true);
			// this.codexexpression.setDisabled(step.isAcceptingFilenames());
			// 选中的文件
			boolean isAccepting = step.isAcceptingFilenames();
			this.pitch = new LabelGridMeta(id + "_pitch", "选中的文件", 200);
			this.pitch.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_pitch.fileId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_pitch.fileName", "文件/目录",
							null, false, 150),
					new GridHeaderDataMeta(id + "_pitch.fileMask", "通配符", null,
							false, 150),
					new GridHeaderDataMeta(id + "_pitch.fileRequired", "要求",
							null, false, 150), });
			this.pitch.setSingle(true);
			this.pitch.setHasBottomBar(true);
			this.pitch.setHasAdd(true, !isAccepting,
					"jQuery.imeta.steps.textfileinput.pitch.btn.fieldAdd");
			this.pitch.setHasDelete(true, !isAccepting,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] fileName = step.getFileName();
			if (fileName != null && fileName.length > 0) {
				for (int i = 0; i < fileName.length; i++) {
					GridCellDataMeta name, mask, required;
					name = new GridCellDataMeta(null, step.getFileName()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					name.setDisabled(isAccepting);
					mask = new GridCellDataMeta(null, step.getFileMask()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					mask.setDisabled(isAccepting);
					required = new GridCellDataMeta(null, step
							.getFileRequired()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					required.setDisabled(isAccepting);
					this.pitch.addRow(new Object[] { String.valueOf(i + 1),
							name, mask, required });
				}
			}
			// DivMeta pitchbtn = new DivMeta(new NullSimpleFormDataMeta(),
			// true);
			// this.delete = new ButtonMeta(id + ".btn.delete", id
			// + ".btn.delete", "删除", "删除");
			// this.delete.appendTo(pitchbtn);
			// this.delete.setDisabled(step.isAcceptingFilenames());
			//
			// this.edit = new ButtonMeta(id + ".btn.edit", id
			// + ".btn.compilation", "编辑", "编辑");
			// this.edit.appendTo(pitchbtn);
			// this.edit.setDisabled(step.isAcceptingFilenames());

			// 从以前的步骤接收文件名
			this.acceptingFilenames = new LabelInputMeta(id
					+ ".acceptingFilenames", "从以前的步骤接收文件名", null, null, null,
					String.valueOf(step.isAcceptingFilenames()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.acceptingFilenames.setSingle(true);
			this.acceptingFilenames
					.addClick("jQuery.imeta.steps.textfileinput.listeners.acceptingFilenamesListeners");

			// Pass through fields from previous step从前面的步骤获取文件名
			this.passingThruFields = new LabelInputMeta(id
					+ ".passingThruFields", "通过以前的步骤获得字段", null, null, null,
					String.valueOf(step.isPassingThruFields()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.passingThruFields.setSingle(true);
			this.passingThruFields.setDisabled(!step.isAcceptingFilenames());
			// 从哪个步骤读文件名
			this.acceptingStepName = new LabelSelectMeta(id
					+ ".acceptingStepName", "步骤读取的文件名来自", null, null, null,
					step.getAcceptingStepName(), null, super
							.getPrevStepNames());
			this.acceptingStepName.setSingle(true);
			this.acceptingStepName.setDisabled(!step.isAcceptingFilenames());

			// 在输入里的字段被当做文件名
			this.acceptingField = new LabelInputMeta(id + ".acceptingField",
					"在输入里的字段被当做文件名", null, null, "在输入里的字段必须填写", step
							.getAcceptingField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.acceptingField.setSingle(true);
			this.acceptingField.setDisabled(!step.isAcceptingFilenames());

			// button
			// DivMeta showBtn = new DivMeta(new NullSimpleFormDataMeta(),
			// true);
			// this.showname = new ButtonMeta(id + ".btn.showname", id
			// + ".btn.showname", "显示文件名", "显示文件名");
			// this.showname.appendTo(showBtn);
			// this.showname.setDisabled(step.isAcceptingFilenames());
			// this.showcontent = new ButtonMeta(id + ".btn.showcontent", id
			// + ".btn.showcontent", "显示文件内容", "显示文件内容");
			// this.showcontent.appendTo(showBtn);
			// this.showcontent.setDisabled(step.isAcceptingFilenames());
			// this.showfirstlinecontent = new ButtonMeta(id +
			// ".btn.showfirstlinecontent", id
			// + ".btn.showfirstlinecontent", "显示来自第一条数据行的内容", "显示来自第一条数据行的内容");
			// this.showfirstlinecontent.appendTo(showBtn);
			// this.showfirstlinecontent.setDisabled(step.isAcceptingFilenames());

			// this.meta.putTabContent(0, new BaseFormMeta[] {
			// this.fieldsorcatalog,
			// this.codexexpression,this.pitch,this.acceptingFilenames,
			// this.passingThruFields,this.acceptingStepName,this.acceptingField
			// });

			this.meta.putTabContent(0, new BaseFormMeta[] { this.pitch,
					this.acceptingFilenames, this.passingThruFields,
					this.acceptingStepName, this.acceptingField });

			/*******************************************************************
			 * 标签1
			 ******************************************************************/
			// 文件类型
			List<OptionDataMeta> fileTypeOptions = new ArrayList<OptionDataMeta>();
			fileTypeOptions.add(new OptionDataMeta(TextFileInputMeta.FILE_TYPE_CSV_TEXT, "CSV格式"));
			fileTypeOptions.add(new OptionDataMeta(TextFileInputMeta.FILE_TYPE_FIXED_TEXT, "固定长度格式"));
			this.fileType = new LabelSelectMeta(id + ".fileType", "文件类型", null,
					null, null, step.getFileType(), null, fileTypeOptions);
			this.fileType.setSingle(true);

			// 分隔符
			this.separator = new LabelInputMeta(id + ".separator", "分隔符", null,
					null, "分隔符", step.getSeparator(), null, ValidateForm
							.getInstance().setRequired(true));
			this.separator.setSingle(true);
//			this.inserttab = new ButtonMeta(id + ".btn.inserttab", id
//					+ ".btn.inserttab", "插入TAB", "插入TAB");
//			this.separator.addButton(new ButtonMeta[] { this.inserttab });

			this.separator.setSingle(true);

			// 文本限定符

			this.enclosure = new LabelInputMeta(id + ".enclosure", "文本限定符",
					null, null, "文本限定符必须填写", step.getEnclosure(), null,
					ValidateForm.getInstance().setRequired(true));
			this.enclosure.setSingle(true);

			// 在文本限定符中允许换行

			this.breakInEnclosureAllowed = new LabelInputMeta(id
					+ ".breakInEnclosureAllowed", "在文本限定符中允许换行", null, null,
					null, String.valueOf(step.isBreakInEnclosureAllowed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.breakInEnclosureAllowed.setSingle(false);

			// 逃逸字符

			this.escapeCharacter = new LabelInputMeta(id + ".escapeCharacter",
					"转义字符", null, null, "转义字符必须填写", step.getEscapeCharacter(),
					null, ValidateForm.getInstance().setRequired(true));
			this.escapeCharacter.setSingle(true);

			// 头部

			this.header = new LabelInputMeta(id + ".header", "头部", null, null,
					null, String.valueOf(step.hasHeader()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.header.setSingle(false);
			this.header
					.addClick("jQuery.imeta.steps.textfileinput.listeners.header");

			// 头部行数量

			this.nrHeaderLines = new LabelInputMeta(id + ".nrHeaderLines",
					"头部行数量", null, null, "头部行数量必须填写", String.valueOf(step
							.getNrHeaderLines()), null, ValidateForm
							.getInstance().setNumber(true));

			this.nrHeaderLines.setSingle(false);
			this.nrHeaderLines.setDisabled(!step.hasHeader());

			// 尾部

			this.footer = new LabelInputMeta(id + ".footer", "尾部", null, null,
					null, String.valueOf(step.hasFooter()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.footer.setSingle(false);
			this.footer
					.addClick("jQuery.imeta.steps.textfileinput.listeners.footer");

			// 尾部行数量

			this.nrFooterLines = new LabelInputMeta(id + ".nrFooterLines",
					"尾部行数量", null, null, null, String.valueOf(step
							.getNrFooterLines()), null, ValidateForm
							.getInstance().setNumber(true));
			this.nrFooterLines.setSingle(false);
			this.nrFooterLines.setDisabled(!step.hasFooter());

			// 包装行

			this.lineWrapped = new LabelInputMeta(id + ".lineWrapped", "包装行",
					null, null, null, String.valueOf(step.isLineWrapped()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.lineWrapped.setSingle(false);
			this.lineWrapped
					.addClick("jQuery.imeta.steps.textfileinput.listeners.lineWrapped");

			// 以时间包装的行数

			this.nrWraps = new LabelInputMeta(id + ".nrWraps", "以时间包装的行数",
					null, null, null, String.valueOf(step
							.getNrWraps()), null, ValidateForm.getInstance()
							.setNumber(true));
			this.nrWraps.setSingle(false);
			this.nrWraps.setDisabled(!step.isLineWrapped());

			// 分页布局

			this.layoutPaged = new LabelInputMeta(id + ".layoutPaged", "分页布局",
					null, null, null, String.valueOf(step.isLayoutPaged()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.layoutPaged.setSingle(false);
			this.layoutPaged
					.addClick("jQuery.imeta.steps.textfileinput.listeners.layoutPaged");

			// 每页记录行数

			this.nrLinesPerPage = new LabelInputMeta(id + ".nrLinesPerPage",
					"每页记录数量", null, null, null, String.valueOf(step
							.getNrLinesPerPage()), null, ValidateForm
							.getInstance().setNumber(true));
			this.nrLinesPerPage.setSingle(false);
			this.nrLinesPerPage.setDisabled(!step.isLayoutPaged());

			// 文档头部行

			this.nrLinesDocHeader = new LabelInputMeta(
					id + ".nrLinesDocHeader", "文档头部行", null, null, "文档头部行必须填写",
					String.valueOf(step.getNrLinesDocHeader()), null,
					ValidateForm.getInstance().setNumber(true));
			this.nrLinesDocHeader.setSingle(true);
			this.nrLinesDocHeader.setDisabled(!step.isLayoutPaged());

			// 压缩
			List<OptionDataMeta> FCOption = new ArrayList<OptionDataMeta>();
			FCOption.add(new OptionDataMeta("0", "Zip"));
			FCOption.add(new OptionDataMeta("1", "GZip"));
			this.fileCompression = new LabelSelectMeta(id + ".fileCompression",
					"压缩", null, null, null, String.valueOf(step
							.getFileCompression()), null, FCOption);
			this.fileCompression.setSingle(true);

			// 没有空行
			this.noEmptyLines = new LabelInputMeta(id + ".noEmptyLines",
					"没有空行", null, null, null, String.valueOf(step
							.noEmptyLines()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.noEmptyLines.setSingle(true);

			// 在输出包括字段名

			this.includeFilename = new LabelInputMeta(id + ".includeFilename",
					"输出包含字段名", null, null, null, String.valueOf(step
							.includeFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includeFilename.setSingle(false);
			this.includeFilename
					.addClick("jQuery.imeta.steps.textfileinput.listeners.includeFilename");

			// 包含文件名的字段名称

			this.filenameField = new LabelInputMeta(id + ".filenameField",
					"包含文件名的字段名称", null, null, "头部行数量必须填写", step
							.getFilenameField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.filenameField.setSingle(false);
			this.filenameField.setDisabled(!step.includeFilename());

			// 输出包含行数

			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "输出包含行数?", null, null, null,
					String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includeRowNumber.setSingle(false);
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.textfileinput.listeners.includeRowNumber");

			// 行数字段名称

			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行数字段名称", null, null, "行数字段名称必须填写", step
							.getRowNumberField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.rowNumberField.setSingle(false);
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			// 按文件取行号

			this.rowNumberByFile = new LabelInputMeta(id + ".rowNumberByFile",
					"按文件取行号", null, null, null, String.valueOf(step
							.isRowNumberByFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.rowNumberByFile.setSingle(true);
			this.rowNumberByFile.setDisabled(!step.includeRowNumber());

			// 格式
			List<OptionDataMeta> formatOptions = new ArrayList<OptionDataMeta>();
			formatOptions.add(new OptionDataMeta(String
					.valueOf(TextFileInputMeta.FILE_FORMAT_DOS), "DOS"));
			formatOptions.add(new OptionDataMeta(String
					.valueOf(TextFileInputMeta.FILE_FORMAT_UNIX), "Unix"));
			formatOptions.add(new OptionDataMeta(String
					.valueOf(TextFileInputMeta.FILE_FORMAT_MIXED), "mixed"));
			this.fileFormat = new LabelSelectMeta(id + ".fileFormat", "格式",
					null, null, null, String.valueOf(step.getFileFormat()),
					null, formatOptions);
			this.fileFormat.setSingle(true);

			// 编码方式
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码方式", null,
					null, null, step.getEncoding(), null, super.getEncoding());
			this.encoding.setSingle(true);

			// 记录数限制
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "记录数限制", null,
					null, null, String.valueOf(step.getRowLimit()),
					null, ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);

			// 解析日期时是否严格要求？

			this.dateFormatLenient = new LabelInputMeta(id
					+ ".dateFormatLenient", "解析日期时是否严格要求", null, null, null,
					String.valueOf(step.isDateFormatLenient()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.dateFormatLenient.setSingle(false);

			// 本地日期格式
			this.dateLocale = new LabelSelectMeta(id + ".dateLocale", "本地日期格式",
					null, null, null, String
							.valueOf(step.getDateFormatLocale()), null, super
							.getDateFormats());
			this.dateLocale.setSingle(true);

			// 结果文件名
			this.addResultFilenameColum = new ColumnFieldsetMeta(id, "添加到结果文件名");
			this.addResultFilenameColum.setSingle(true);

			// 添加文件名
			this.isaddresult = new LabelInputMeta(id + ".isaddresult", "添加文件名",
					null, null, null, String.valueOf(step.isAddResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.isaddresult.setSingle(false);
			this.addResultFilenameColum
					.putFieldsetsContent(new BaseFormMeta[] { this.isaddresult });

			this.meta.putTabContent(1, new BaseFormMeta[] { this.fileType,
					this.separator, this.enclosure,
					this.breakInEnclosureAllowed, this.escapeCharacter,
					this.header, this.nrHeaderLines, this.footer,
					this.nrFooterLines, this.lineWrapped, this.nrWraps,
					this.layoutPaged, this.nrLinesPerPage,
					this.nrLinesDocHeader, this.fileCompression,
					this.noEmptyLines, this.includeFilename,
					this.filenameField, this.includeRowNumber,
					this.rowNumberField, this.rowNumberByFile, this.fileFormat,
					this.encoding, this.rowLimit, this.dateFormatLenient,
					this.dateLocale, this.addResultFilenameColum });

			/*******************************************************************
			 * 标签2
			 ******************************************************************/

			// 忽略错误？
			this.errorIgnored = new LabelInputMeta(id + ".errorIgnored",
					"忽略错误", null, null, null, String.valueOf(step
							.isErrorIgnored()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.errorIgnored.setSingle(false);
			this.errorIgnored
					.addClick("jQuery.imeta.steps.textfileinput.listeners.errorIgnored");

			// 跳过错误行
			this.errorLineSkipped = new LabelInputMeta(
					id + ".errorLineSkipped", "跳过错误行", null, null, null, String
							.valueOf(step.isErrorLineSkipped()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.errorLineSkipped.setSingle(true);
			this.errorLineSkipped.setDisabled(!step.isErrorIgnored());

			// 错误计数字段
			this.errorCountField = new LabelInputMeta(id + ".errorCountField",
					"错误计数字段", null, null, "记录数限制必须填写", step
							.getErrorCountField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.errorCountField.setSingle(true);
			this.errorCountField.setDisabled(!step.isErrorIgnored());

			// 错误字段名
			this.errorFieldsField = new LabelInputMeta(
					id + ".errorFieldsField", "错误字段名", null, null, "记录数限制必须填写",
					step.getErrorFieldsField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.errorFieldsField.setSingle(true);
			this.errorFieldsField.setDisabled(!step.isErrorIgnored());

			// 错误文本字段
			this.errorTextField = new LabelInputMeta(id + ".errorTextField",
					"错误文本字段", null, null, "记录数限制必须填写",
					step.getErrorTextField(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.errorTextField.setSingle(true);
			this.errorTextField.setDisabled(!step.isErrorIgnored());

			// 警告文件目录
			this.warningFilesDestinationDirectory = new LabelInputMeta(id
					+ ".warningFilesDestinationDirectory", "警告文件目录", null,
					null, "警告文件目录必须填写", step
							.getWarningFilesDestinationDirectory(), null,
					ValidateForm.getInstance().setRequired(true));
			this.warningFilesDestinationDirectory.setSingle(false);
			this.warningFilesDestinationDirectory.setDisabled(!step
					.isErrorIgnored());

			// 警告文件目录扩展名
			this.warningFilesExtension = new LabelInputMeta(id
					+ ".warningFilesExtension", "扩展名", null, null, "扩展名必须填写",
					step.getWarningFilesExtension(), null, ValidateForm
							.getInstance().setRequired(true));
			this.warningFilesExtension.setSingle(false);
			this.warningFilesExtension.setDisabled(!step.isErrorIgnored());

			// this.variable = new ButtonMeta(id + ".btn.variable", id
			// + ".btn.variable", "变量", "变量");
			// this.browse = new ButtonMeta(id + ".btn.browse", id
			// + ".btn.browse", "浏览", "浏览");

			// this.warningFilesExtension.addButton(new ButtonMeta[] {
			// this.variable,this.browse});

			// 错误文件目录
			this.errorFilesDestinationDirectory = new LabelInputMeta(id
					+ ".errorFilesDestinationDirectory", "错误文件目录", null, null,
					"错误文件目录必须填写", step.getErrorFilesDestinationDirectory(),
					null, ValidateForm.getInstance().setRequired(true));
			this.errorFilesDestinationDirectory.setSingle(false);
			this.errorFilesDestinationDirectory.setDisabled(!step
					.isErrorIgnored());

			// 错误文件目录扩展名
			this.errorFilesExtension = new LabelInputMeta(id
					+ ".errorFilesExtension", "扩展名", null, null, "扩展名必须填写",
					step.getErrorLineFilesExtension(), null, ValidateForm
							.getInstance().setRequired(true));
			this.errorFilesExtension.setSingle(false);
			this.errorFilesExtension.setDisabled(!step.isErrorIgnored());

			// this.variable = new ButtonMeta(id + ".btn.variable", id
			// + ".btn.variable", "变量", "变量");
			// this.browse = new ButtonMeta(id + ".btn.browse", id
			// + ".btn.browse", "浏览", "浏览");
			//
			// this.errorFilesExtension.addButton(new ButtonMeta[] {
			// this.variable,this.browse});

			// 失败行数文件目录
			this.lineNumberFilesDestinationDirectory = new LabelInputMeta(id
					+ ".lineNumberFilesDestinationDirectory", "失败行数文件目录", null,
					null, "失败行数文件目录必须填写", step
							.getLineNumberFilesDestinationDirectory(), null,
					ValidateForm.getInstance().setRequired(true));
			this.lineNumberFilesDestinationDirectory.setSingle(false);
			this.lineNumberFilesDestinationDirectory.setDisabled(!step
					.isErrorIgnored());

			// 失败行数文件目录扩展名
			this.lineNumberFilesExtension = new LabelInputMeta(id
					+ ".lineNumberFilesExtension", "扩展名", null, null,
					"扩展名必须填写", step.getLineNumberFilesExtension(), null,
					ValidateForm.getInstance().setRequired(true));
			this.lineNumberFilesExtension.setSingle(false);
			this.lineNumberFilesExtension.setDisabled(!step.isErrorIgnored());

			// this.variable = new ButtonMeta(id + ".btn.variable", id
			// + ".btn.variable", "变量", "变量");
			// this.browse = new ButtonMeta(id + ".btn.browse", id
			// + ".btn.browse", "浏览", "浏览");
			//
			// this.lineNumberFilesExtension.addButton(new ButtonMeta[] {
			// this.variable,this.browse});

			this.meta.putTabContent(2, new BaseFormMeta[] { this.errorIgnored,
					this.errorLineSkipped, this.errorCountField,
					this.errorFieldsField, this.errorTextField,
					this.warningFilesDestinationDirectory,
					this.warningFilesExtension,
					this.errorFilesDestinationDirectory,
					this.errorFilesExtension,
					this.lineNumberFilesDestinationDirectory,
					this.lineNumberFilesExtension });

			/*******************************************************************
			 * 标签3
			 ******************************************************************/
			// 过滤
			this.sift = new LabelGridMeta(id + "_sift", null, 200);
			this.sift.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_sift.siftId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_sift.filterString", "过滤字符串",
							null, false, 100),
					new GridHeaderDataMeta(id + "_sift.locationFilter",
							"过滤器位置", null, false, 100),
					(new GridHeaderDataMeta(id + "_sift.stopFilter", "停止在过滤器",
							null, false, 100)).setOptions(super
							.getOptionsByTrueAndFalse(false)) });
			this.sift.setSingle(true);
			this.sift.setHasBottomBar(true);
			this.sift.setHasAdd(true, true,
					"jQuery.imeta.steps.textfileinput.sift.btn.fieldAdd");
			this.sift.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			TextFileFilter[] excelInputField = step.getFilter();
			if (excelInputField != null && excelInputField.length > 0) {
				for (int i = 0; i < excelInputField.length; i++) {
					this.sift.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, excelInputField[i]
									.getFilterString(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(excelInputField[i]
											.getFilterPosition()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(excelInputField[i]
											.isFilterLastLine()),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			}

			this.meta.putTabContent(3, new BaseFormMeta[] { this.sift });

			/*******************************************************************
			 * 标签4
			 ******************************************************************/
			// 获取字段
			this.fields = new LabelGridMeta(id + "_fields", null, 200);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldType", "类型",
							null, false, 50))
							.setOptions(super.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldPosition", "位置",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency",
							"货币类型", null, false, 60),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "十进制",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "分组",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.nullIf", "空值",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.ifNull", "默认", null,
							false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldTrimType",
							"去空格类型", null, false, 120)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.trimTypeDesc, false)),
					(new GridHeaderDataMeta(id + "_fields.fieldRepeat", "重复",
							null, false, 50)).setOptions(super
							.getOptionsByTrueAndFalse(false)) });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.textfileinput.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			TextFileInputField[] textFileInputField = step.getInputFields();
			if (textFileInputField != null && textFileInputField.length > 0)
				for (int i = 0; i < textFileInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, textFileInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,
									String.valueOf(textFileInputField[i]
											.getFormat()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i]
											.getPosition()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(textFileInputField[i]
											.getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i]
											.getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i]
											.getCurrencySymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, textFileInputField[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, textFileInputField[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i]
											.getNullString()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i]
											.getIfNullValue()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i]
											.getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,
									String.valueOf(textFileInputField[i]
											.isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			DivMeta getfieldsbtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.textfileinput.btn.getfields")
			.appendTo(getfieldsbtn);

			this.meta.putTabContent(4, new BaseFormMeta[] { this.fields,
					getfieldsbtn });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

			// 确定取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}

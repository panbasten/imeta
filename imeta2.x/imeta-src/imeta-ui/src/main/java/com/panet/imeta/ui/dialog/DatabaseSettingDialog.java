package com.panet.imeta.ui.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.FormLoader;
import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.exception.ImetaFormException;
import com.panet.iform.forms.columnDiv.ColumnDivMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridRowDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.database.BaseDatabaseMeta;
import com.panet.imeta.core.database.DatabaseConnectionPoolParameter;
import com.panet.imeta.core.database.DatabaseInterface;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.database.GenericDatabaseMeta;
import com.panet.imeta.core.database.PartitionDatabaseMeta;
import com.panet.imeta.core.database.SAPR3DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryUtil;
import com.panet.imeta.trans.BaseDatabaseDialog;
import com.panet.imeta.trans.DatabaseDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class DatabaseSettingDialog extends BaseDatabaseDialog implements
		DatabaseDialogInterface {

	public static final String DOT = ".";

	public static final String DATADASE_CONNECTION_NAME = "connectionName";
	public static final String DATADASE_CONNECTION_TYPE = "connectionType";
	public static final String DATADASE_ACCESS = "access";
	public static final String DATADASE_HOST_NAME = "hostName";
	public static final String DATADASE_DB_NAME = "dbName";
	public static final String DATADASE_PORT = "port";
	public static final String DATADASE_USERNAME = "username";
	public static final String DATADASE_PASSWORD = "password";
	public static final String DATADASE_SERVERNAME = "servername";
	public static final String DATADASE_STREAM_RESULT = "streamResult";
	public static final String DATADASE_DATA = "data";
	public static final String DATADASE_INDEX = "index";
	public static final String DATADASE_MSSQL_INSTANCE = "mssqlInstance";
	public static final String DATADASE_DECIMAL_SEPARATOR = "decimalSeparator";

	public static final String SUPPORTS_BOOLEAN_DATA_TYPE = "supportBooleanDataType";
	public static final String ADVANCE_QUOTE_ALL_FIELDS = "quoteAllFields";
	public static final String ADVANCE_FORCE_IDENTIFIERS_LOWER_CASE = "forceIdentifiersLowerCase";
	public static final String ADVANCE_FORCE_IDENTIFIERS_UPPER_CASE = "forceIdentifiersUpperCase";
	public static final String ADVANCE_SQL_STATEMENTS = "sqlStatements";

	public static final String OPTION_PARAMETER_NAME = "optionParametersName";
	public static final String OPTION_PARAMETER_VALUE = "optionParametersValue";

	public static final String POOLING_USE_POOL = "usePool";
	public static final String POOLING_INIT_POOL_SIZE = "initPool";
	public static final String POOLING_MAX_POOL_SIZE = "maxPool";

	public static final String POOLING_PARAMETER_CHECK = "poolParametersCheck";
	public static final String POOLING_PARAMETER_NAME = "poolParametersName";
	public static final String POOLING_PARAMETER_VALUE = "poolParametersValue";

	public static final String CLUSTER_USE_PARTITIONED = "partitioned";

	public static final String CLUSTER_PARAMETER_PARTITION_ID = "partitionId";
	public static final String CLUSTER_PARAMETER_HOSTNAME = "hostname";
	public static final String CLUSTER_PARAMETER_PORT = "port";
	public static final String CLUSTER_PARAMETER_DB_NAME = "dbName";
	public static final String CLUSTER_PARAMETER_USERNAME = "username";
	public static final String CLUSTER_PARAMETER_PASSWORD = "password";

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private MenuTabMeta menu;

	private LabelInputMeta connectionName;

	private LabelSelectMeta connectionType;

	private LabelSelectMeta access;

	/**
	 * 属性
	 */
	private LabelInputMeta hostName;

	private LabelInputMeta dbName;

	private LabelInputMeta port;

	private LabelInputMeta customUrl, customClass, instance;

	private LabelInputMeta username, password, servername, streamResult, data,
			index;

	private LabelInputMeta sAPLanguage, sAPSystemNumber, sAPClient;

	private LabelInputMeta quoteAllFields, forceIdentifiersLowerCase,
			forceIdentifiersUpperCase;

	private LabelInputMeta usePool, initPoolSize, maxPoolSize, useCluster,
			decimalSeparator;

	private LabelTextareaMeta description, sqlStatemetns;

	/**
	 * 数据库按钮，属性列表，浏览
	 */
	private ButtonMeta dbTest, dbExplore, okBtn, cancelBtn, resetPoolBtn;

	private ColumnFieldsetMeta propertiesField;

	private LabelGridMeta parameters, poolParameters, clusterParameters;

	public DatabaseSettingDialog(DatabaseMeta databaseMeta, Repository rep) {
		super(databaseMeta, rep);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			String id = this.getId();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到tab
			this.menu = new MenuTabMeta(id, new String[] { "基本", "高级", "选项",
					"连接池", "集群" });
			this.menu.setSingle(true);

			/*******************************************************************
			 * 0 基本
			 ******************************************************************/
			addGeneralTab(id, 0);
			/*******************************************************************
			 * 1 高级
			 ******************************************************************/
			addAdvancedTab(id, 1);
			/*******************************************************************
			 * 2 选项
			 ******************************************************************/
			addOptionTab(id, 2);
			/*******************************************************************
			 * 3 连接池
			 ******************************************************************/
			addPoolingTab(id, 3);
			/*******************************************************************
			 * 4 集群
			 ******************************************************************/
			addClusteringTab(id, 4);

			/*******************************************************************
			 * 数据库按钮
			 ******************************************************************/
			DivMeta dbBtnDiv = new DivMeta(new NullSimpleFormDataMeta(), true);
			this.dbTest = new ButtonMeta(id + ".btn.dbTest", null, "测试",
					"测试数据库");
			this.dbTest.addClick("jQuery.imeta.db.dbSetting.btn.dbTest");
			this.dbTest.appendTo(dbBtnDiv);

			this.dbExplore = new ButtonMeta(id + ".btn.dbExplore", null, "浏览",
					"浏览数据库");
			this.dbExplore.addClick("jQuery.imeta.db.dbSetting.btn.dbExplore");
			this.dbExplore.appendTo(dbBtnDiv);

			this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					this.menu, dbBtnDiv });

			this.okBtn = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定",
					"确定");
			this.okBtn.addClick("jQuery.imeta.db.dbSetting.btn.ok");
			this.cancelBtn = new ButtonMeta(id + ".btn.cancel", id
					+ ".btn.cancel", "取消", "取消");
			this.cancelBtn.addClick("jQuery.imeta.db.dbSetting.btn.cancel");

			this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					this.okBtn, this.cancelBtn });

			cArr.add(this.columnFormMeta.getFormJo());
			rtn.put("items", cArr);

			rtn.put("title", this.getDatabaseMeta().getName() + "设置");

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

	/**
	 * 基本
	 * 
	 * @param id
	 * @throws ImetaFormException
	 */
	private void addGeneralTab(String id, int tabNum) throws ImetaFormException {
		// 数据库类型
		int databaseType = -1;
		int databaseAccessType = -1;
		DatabaseInterface databaseInterface = null;

		if (super.getDatabaseMeta() != null) {
			databaseType = super.getDatabaseMeta().getDatabaseType();
			databaseAccessType = super.getDatabaseMeta().getAccessType();
		}

		this.connectionName = new LabelInputMeta(id + DOT
				+ DATADASE_CONNECTION_NAME, "连接名称", null, null, null, super
				.getDatabaseMeta().getName(), null, ValidateForm.getInstance()
				.setRequired(true));
		this.connectionName.setSingle(true);

		/**
		 * 连接类型
		 */
		ColumnDivMeta columnDivMeta = new ColumnDivMeta();
		this.connectionType = new LabelSelectMeta(id + DOT
				+ DATADASE_CONNECTION_TYPE, "连接类型", null, null, null, null,
				null, null);
		this.connectionType.setSingle(true);
		this.connectionType.setLayout(LabelSelectMeta.LAYOUT_COLUMN);
		this.connectionType.setSize(10);
		this.connectionType.addListener("change",
				"jQuery.imeta.db.dbSetting.connTypeChange");
		// 连接类型数据
		DatabaseInterface[] dbtypes = DatabaseMeta.getDatabaseInterfaces();
		if (databaseType == -1) {
			databaseType = dbtypes[0].getDatabaseType();
		}

		int temp;
		for (DatabaseInterface dbtype : dbtypes) {
			temp = dbtype.getDatabaseType();
			this.connectionType.appendOption(String.valueOf(temp), dbtype
					.getDatabaseTypeDescLong());
			if (temp == databaseType) {
				this.connectionType.setDefaultValue(String
						.valueOf(databaseType));
				databaseInterface = dbtype;
			}
		}

		/**
		 * 连接类型
		 */
		this.access = new LabelSelectMeta(id + DOT + DATADASE_ACCESS, "连接方式",
				null, null, null, null, null, null);
		this.access.setSingle(true);
		this.access.setLayout(LabelSelectMeta.LAYOUT_COLUMN);
		this.access.setSize(4);
		this.access.addListener("change",
				"jQuery.imeta.db.dbSetting.accessChange");
		// 连接类型数据
		int[] accessList = databaseInterface.getAccessTypeList();
		if (databaseAccessType == -1) {
			databaseAccessType = accessList[0];
		}
		for (int a : accessList) {
			this.access.appendOption(String.valueOf(a),
					DatabaseMeta.dbAccessTypeDesc[a]);
			if (a == databaseAccessType) {
				this.access.setDefaultValue(String.valueOf(a));
			}

		}

		columnDivMeta.putDivContent(new BaseFormMeta[] { this.connectionType,
				this.access });
		columnDivMeta.getRoot().setStyle("float", "left");

		this.propertiesField = new ColumnFieldsetMeta(id + "_propertiesField",
				"设置");

		this.propertiesField.putFieldsetsContent(initFields(id, super
				.getDatabaseMeta(), databaseType, databaseAccessType));

		this.propertiesField.getRoot().setStyle("float", "left");

		this.menu.putTabContent(tabNum, new BaseFormMeta[] {
				this.connectionName, columnDivMeta, this.propertiesField });
	}

	private void addAdvancedTab(String id, int tabNum)
			throws ImetaFormException {
		ColumnFieldsetMeta fieldMeta = new ColumnFieldsetMeta(id
				+ "_identifiers", "标识符");
		this.quoteAllFields = new LabelInputMeta(
				id + DOT + ADVANCE_QUOTE_ALL_FIELDS,
				"将所有字段用引号括起",
				null,
				null,
				null,
				(super.getDatabaseMeta().isQuoteAllFields()) ? "true" : "false",
				InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.quoteAllFields.setLabelAfter(true);

		this.forceIdentifiersLowerCase = new LabelInputMeta(id + DOT
				+ ADVANCE_FORCE_IDENTIFIERS_LOWER_CASE, "强制标识符小写", null, null,
				null, (super.getDatabaseMeta()
						.isForcingIdentifiersToLowerCase()) ? "true" : "false",
				InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.forceIdentifiersLowerCase.setLabelAfter(true);

		this.forceIdentifiersUpperCase = new LabelInputMeta(id + DOT
				+ ADVANCE_FORCE_IDENTIFIERS_UPPER_CASE, "强制标识符大写", null, null,
				null, (super.getDatabaseMeta()
						.isForcingIdentifiersToUpperCase()) ? "true" : "false",
				InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.forceIdentifiersUpperCase.setLabelAfter(true);

		this.sqlStatemetns = new LabelTextareaMeta(id + DOT
				+ ADVANCE_SQL_STATEMENTS, "输入SQL语句（以“;”分割），在连接后执行", null, null,
				null, Const.NVL(super.getDatabaseMeta().getConnectSQL(), ""),
				8, null);
		this.sqlStatemetns.setSingle(true);
		this.sqlStatemetns.setLayout(LabelTextareaMeta.LAYOUT_COLUMN);

		fieldMeta.putFieldsetsContent(new BaseFormMeta[] { this.quoteAllFields,
				this.forceIdentifiersLowerCase, this.forceIdentifiersUpperCase,
				this.sqlStatemetns });
		fieldMeta.setSingle(true);

		this.menu.putTabContent(tabNum, new BaseFormMeta[] { fieldMeta });
	}

	private void addOptionTab(String id, int tabNum) throws ImetaFormException {
		this.parameters = new LabelGridMeta(id + "_parameters", "参数", 200);
		this.parameters.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(null, "序号",
						GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
				new GridHeaderDataMeta(id + DOT + OPTION_PARAMETER_NAME, "参数名",
						null, false, 220),
				new GridHeaderDataMeta(id + DOT + OPTION_PARAMETER_VALUE,
						"参数值", null, false, 220) });

		Map<String, String> extraOptions = this.getDatabaseMeta()
				.getExtraOptions();
		int index = 1;
		String parameter, parameterArr[], value;
		Iterator<String> keys = extraOptions.keySet().iterator();
		String dbDesc = super.getDatabaseMeta().getDatabaseTypeDesc();
		while (keys.hasNext()) {
			parameter = (String) keys.next();
			if (StringUtils.isEmpty(parameter)) {
				continue;
			}
			parameterArr = StringUtils.split(parameter, ".");
			if (dbDesc != null && dbDesc.equalsIgnoreCase(parameterArr[0])) {
				value = extraOptions.get(parameter);
				parameter = parameterArr[parameterArr.length - 1];
				if (!Const.isEmpty(value)
						&& value.equals(DatabaseMeta.EMPTY_OPTIONS_STRING))
					value = "";

				this.parameters.addRow(new Object[] {
						String.valueOf(index++),
						parameter,
						new GridCellDataMeta(null, value,
								GridCellDataMeta.CELL_TYPE_INPUT) });
			}
		}

		this.parameters.setSingle(true);
		this.menu.putTabContent(tabNum, new BaseFormMeta[] { this.parameters });
	}

	private void addPoolingTab(String id, int tabNum) throws ImetaFormException {
		boolean isUsePool = super.getDatabaseMeta().isUsingConnectionPool();
		this.usePool = new LabelInputMeta(id + DOT + POOLING_USE_POOL, "使用连接池",
				null, null, null, (super.getDatabaseMeta()
						.isUsingConnectionPool()) ? "true" : "false",
				InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.usePool.setLabelAfter(true);
		this.usePool
				.addClick("jQuery.imeta.db.dbSetting.listeners.usePoolClick");

		ColumnFieldsetMeta fieldMeta = new ColumnFieldsetMeta(id + "_poolSize",
				"连接池数量");

		this.initPoolSize = new LabelInputMeta(id + DOT
				+ POOLING_INIT_POOL_SIZE, "初始值", null, null, null, String
				.valueOf(super.getDatabaseMeta().getInitialPoolSize()), null,
				ValidateForm.getInstance().setRequired(true));

		this.maxPoolSize = new LabelInputMeta(id + DOT + POOLING_MAX_POOL_SIZE,
				"最大值", null, null, null, String.valueOf(super.getDatabaseMeta()
						.getMaximumPoolSize()), null, ValidateForm
						.getInstance().setRequired(true));

		this.initPoolSize.setDisabled(!isUsePool);
		this.maxPoolSize.setDisabled(!isUsePool);

		fieldMeta.putFieldsetsContent(new BaseFormMeta[] { this.initPoolSize,
				this.maxPoolSize });
		fieldMeta.setSingle(true);

		this.poolParameters = new LabelGridMeta(id + "_poolParameters", "参数",
				200);

		this.poolParameters
				.addHeaders(new GridHeaderDataMeta[] {
						new GridHeaderDataMeta(id + DOT
								+ POOLING_PARAMETER_CHECK, " ",
								GridHeaderDataMeta.HEADER_TYPE_CHECKBOX, false,
								50),
						new GridHeaderDataMeta(id + DOT
								+ POOLING_PARAMETER_NAME, "参数名", null, false,
								220),
						new GridHeaderDataMeta(id + DOT
								+ POOLING_PARAMETER_VALUE, "参数值",
								GridHeaderDataMeta.HEADER_TYPE_INPUT, false,
								220) });
		Properties properties = this.getDatabaseMeta()
				.getConnectionPoolingProperties();

		String parameter, value, defValue;
		boolean pSelect = false;

		GridCellDataMeta parameterCheckbox, parameterValue;
		for (DatabaseConnectionPoolParameter p : BaseDatabaseMeta.poolingParameters) {
			GridRowDataMeta rowDataMeta = new GridRowDataMeta(3);
			rowDataMeta.addExtendMap("description", p.getDescription());
			rowDataMeta.addExtendMap("rootId", id);
			rowDataMeta
					.setRowClickFn("jQuery.imeta.db.dbSetting.listeners.poolParameter.rowClick");

			parameter = Const.NVL(p.getParameter(), "");
			value = (String) properties.get(parameter);
			pSelect = (value != null) ? true : false;
			defValue = DatabaseConnectionPoolParameter.findParameter(parameter,
					BaseDatabaseMeta.poolingParameters).getDefaultValue();
			value = Const.NVL((value != null) ? value : defValue, "");
			parameterCheckbox = new GridCellDataMeta(parameter, String
					.valueOf(pSelect));
			parameterCheckbox.setType(GridCellDataMeta.CELL_TYPE_CHECKBOX);
			parameterCheckbox.setDisabled(!isUsePool);
			parameterValue = new GridCellDataMeta(parameter, value);
			parameterValue.setDisabled(!isUsePool);

			rowDataMeta.setCell(0, parameterCheckbox);
			rowDataMeta.setCell(1, parameter);
			rowDataMeta.setCell(2, parameterValue);
			this.poolParameters.addRow(rowDataMeta);
		}

		this.poolParameters.setSingle(true);

		DivMeta resetPoolDiv = new DivMeta(new NullSimpleFormDataMeta(), true);
		this.resetPoolBtn = new ButtonMeta(id + ".btn.poolReset", null, "重置选项",
				"重置数据库连接池选项");
		this.resetPoolBtn
				.addClick("jQuery.imeta.db.dbSetting.btn.resetPoolParameters");
		this.resetPoolBtn.appendTo(resetPoolDiv);

		this.description = new LabelTextareaMeta(id + ".description", "描述",
				null, null, null, null, 3, null);
		this.description.setSingle(true);
		this.description.setLayout(LabelTextareaMeta.LAYOUT_COLUMN);
		this.description.setReadonly(true);

		this.menu.putTabContent(tabNum,
				new BaseFormMeta[] { this.usePool, fieldMeta,
						this.poolParameters, resetPoolDiv, this.description });
	}

	private void addClusteringTab(String id, int tabNum)
			throws ImetaFormException {
		boolean isPartitioned = super.getDatabaseMeta().isPartitioned();
		this.useCluster = new LabelInputMeta(
				id + DOT + CLUSTER_USE_PARTITIONED, "使用集群", null, null, null,
				(isPartitioned) ? "true" : "false",
				InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.useCluster.setLabelAfter(true);
		this.useCluster
				.addClick("jQuery.imeta.db.dbSetting.listeners.useClusterClick");

		this.clusterParameters = new LabelGridMeta(id + "_clusterParameters",
				"参数", 200);
		this.clusterParameters.setHasBottomBar(true);
		this.clusterParameters.setHasAdd(true, isPartitioned,
				"jQuery.imeta.db.dbSetting.btn.clusterAdd");
		this.clusterParameters.setHasDelete(true, isPartitioned,
				"jQuery.imeta.db.dbSetting.btn.clusterDelete");
		this.clusterParameters.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + DOT
						+ CLUSTER_PARAMETER_PARTITION_ID, "分区ID", null, false,
						50),
				new GridHeaderDataMeta(id + DOT + CLUSTER_PARAMETER_HOSTNAME,
						"主机名", null, false, 80),
				new GridHeaderDataMeta(id + DOT + CLUSTER_PARAMETER_PORT, "端口",
						null, false, 60),
				new GridHeaderDataMeta(id + DOT + CLUSTER_PARAMETER_DB_NAME,
						"数据库名", null, false, 80),
				new GridHeaderDataMeta(id + DOT + CLUSTER_PARAMETER_USERNAME,
						"用户名", null, false, 80),
				new GridHeaderDataMeta(id + DOT + CLUSTER_PARAMETER_PASSWORD,
						"密码", null, false, 80) });

		this.clusterParameters.setSingle(true);

		PartitionDatabaseMeta[] clusterInformation = super.getDatabaseMeta()
				.getPartitioningInformation();
		if (clusterInformation != null) {
			GridCellDataMeta cPartitionId, cHostname, cPort, cDatabaseName, cUsername, cPassword;
			for (int i = 0; i < clusterInformation.length; i++) {
				PartitionDatabaseMeta meta = clusterInformation[i];
				cPartitionId = new GridCellDataMeta(Const.NVL(meta
						.getPartitionId(), ""));
				cPartitionId.setDisabled(!isPartitioned);
				cHostname = new GridCellDataMeta(Const.NVL(meta.getHostname(),
						""));
				cHostname.setDisabled(!isPartitioned);
				cPort = new GridCellDataMeta(Const.NVL(meta.getPort(), ""));
				cPort.setDisabled(!isPartitioned);
				cDatabaseName = new GridCellDataMeta(Const.NVL(meta
						.getDatabaseName(), ""));
				cDatabaseName.setDisabled(!isPartitioned);
				cUsername = new GridCellDataMeta(Const.NVL(meta.getUsername(),
						""));
				cUsername.setDisabled(!isPartitioned);
				cPassword = new GridCellDataMeta(Const.NVL(meta.getPassword(),
						""));
				cPassword.setType(GridCellDataMeta.CELL_TYPE_PASSWORD);
				cPassword.setDisabled(!isPartitioned);
				clusterParameters
						.addRow(new Object[] { cPartitionId, cHostname, cPort,
								cDatabaseName, cUsername, cPassword });
			}
		}

		this.menu.putTabContent(tabNum, new BaseFormMeta[] { this.useCluster,
				this.clusterParameters });
	}

	public JSONObject getFields(int connType, int access) throws ImetaException {
		DatabaseInterface[] dbtypes = DatabaseMeta.getDatabaseInterfaces();
		DatabaseInterface databaseInterface = null;
		for (DatabaseInterface dbtype : dbtypes) {
			if (dbtype.getDatabaseType() == connType) {
				databaseInterface = dbtype;
				break;
			}
		}
		this.getDatabaseMeta().setDatabaseInterface(databaseInterface);
		this.getDatabaseMeta().setAccessType(access);
		this.getDatabaseMeta().addOptions();
		return open();
	}

	private BaseFormMeta[] fieldsVectorToArray(Vector<BaseFormMeta> v) {
		if (v != null && v.size() > 0) {
			BaseFormMeta[] b = new BaseFormMeta[v.size()];
			for (int i = 0; i < v.size(); i++) {
				b[i] = v.get(i);
			}
			return b;
		}
		return new BaseFormMeta[] {};
	}

	private void genericNativeFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * 自定义URL
		 */
		this.customUrl = new LabelInputMeta(id + DOT
				+ GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL, "自定义URL", null,
				null, null, databaseMeta.getAttributes().getProperty(
						GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL), null,
				ValidateForm.getInstance().setRequired(true));
		this.customUrl.setSingle(true);
		this.customUrl.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.customUrl);

		/**
		 * 自定义驱动类
		 */
		this.customClass = new LabelInputMeta(id + DOT
				+ GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS, "自定义驱动类",
				null, null, null, databaseMeta.getAttributes().getProperty(
						GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS),
				null, ValidateForm.getInstance().setRequired(true));
		this.customClass.setSingle(true);
		this.customClass.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.customClass);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);
	}

	private void informixNativeFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * 主机地址
		 */
		this.hostName = new LabelInputMeta(id + DOT + DATADASE_HOST_NAME,
				"主机地址", null, null, null, databaseMeta.getHostname(), null,
				ValidateForm.getInstance().setRequired(true));
		this.hostName.setSingle(true);
		this.hostName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.hostName);

		/**
		 * 数据库名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "数据库名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 服务器名
		 */
		this.servername = new LabelInputMeta(id + DOT + DATADASE_SERVERNAME,
				"服务器名称", null, null, null, databaseMeta.getServername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.servername.setSingle(true);
		this.servername.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.servername);

		/**
		 * 端口号
		 */
		this.port = new LabelInputMeta(id + DOT + DATADASE_PORT, "端口号", null,
				null, null, "1526", null, ValidateForm.getInstance()
						.setRequired(true));
		this.port.setSingle(true);
		this.port.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.port);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);

	}

	private void informixJndiFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * 数据库名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "JNDI名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 服务器名
		 */
		this.servername = new LabelInputMeta(id + DOT + DATADASE_SERVERNAME,
				"服务器名称", null, null, null, databaseMeta.getServername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.servername.setSingle(true);
		this.servername.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.servername);
	}

	private void mssqlNativeFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * 主机地址
		 */
		this.hostName = new LabelInputMeta(id + DOT + DATADASE_HOST_NAME,
				"主机地址", null, null, null, databaseMeta.getHostname(), null,
				ValidateForm.getInstance().setRequired(true));
		this.hostName.setSingle(true);
		this.hostName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.hostName);

		/**
		 * 数据库名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "数据库名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 实例名
		 */
		this.instance = new LabelInputMeta(id + DOT + DATADASE_MSSQL_INSTANCE,
				"实例名", null, null, null, databaseMeta.getSQLServerInstance(),
				null, null);
		this.instance.setSingle(true);
		this.instance.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.instance);

		/**
		 * 端口号
		 */
		this.port = new LabelInputMeta(id + DOT + DATADASE_PORT, "端口号", null,
				null, null, "1433", null, ValidateForm.getInstance()
						.setRequired(true));
		this.port.setSingle(true);
		this.port.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.port);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);

		/**
		 * 分割
		 */
		this.decimalSeparator = new LabelInputMeta(id + DOT
				+ DATADASE_DECIMAL_SEPARATOR, "使用“..”分割模式(Schema)和表(Table)",
				null, null, null, (databaseMeta
						.isUsingDoubleDecimalAsSchemaTableSeparator()) ? "true"
						: "false", InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.decimalSeparator.setSingle(true);
		this.decimalSeparator.setLabelAfter(true);
		v.add(this.decimalSeparator);
	}

	private void mssqlJndiFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * JNDI名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "JNDI名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 分割
		 */
		this.decimalSeparator = new LabelInputMeta(id + DOT
				+ DATADASE_DECIMAL_SEPARATOR, "使用“..”分割模式(Schema)和表(Table)",
				null, null, null, (databaseMeta
						.isUsingDoubleDecimalAsSchemaTableSeparator()) ? "true"
						: "false", InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.decimalSeparator.setSingle(true);
		this.decimalSeparator.setLabelAfter(true);
		v.add(this.decimalSeparator);

	}

	private void mysqlNativeFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * 主机地址
		 */
		this.hostName = new LabelInputMeta(id + DOT + DATADASE_HOST_NAME,
				"主机地址", null, null, null, databaseMeta.getHostname(), null,
				ValidateForm.getInstance().setRequired(true));
		this.hostName.setSingle(true);
		this.hostName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.hostName);

		/**
		 * 数据库名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "数据库名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 端口号
		 */
		this.port = new LabelInputMeta(id + DOT + DATADASE_PORT, "端口号", null,
				null, null, "3306", null, ValidateForm.getInstance()
						.setRequired(true));
		this.port.setSingle(true);
		this.port.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.port);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);

		/**
		 * 结果流
		 */
		this.streamResult = new LabelInputMeta(id + DOT
				+ DATADASE_STREAM_RESULT, "使用结果流游标", null, null, null,
				(databaseMeta.isStreamingResults()) ? "true" : "false",
				InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.streamResult.setSingle(true);
		this.streamResult.setLabelAfter(true);
		v.add(this.streamResult);

	}

	private void mysqlJndiFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * JNDI名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "JNDI名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 结果流
		 */
		this.streamResult = new LabelInputMeta(id + DOT
				+ DATADASE_STREAM_RESULT, "使用结果流游标", null, null, null,
				(databaseMeta.isStreamingResults()) ? "true" : "false",
				InputDataMeta.INPUT_TYPE_CHECKBOX, null);
		this.streamResult.setSingle(true);
		this.streamResult.setLabelAfter(true);
		v.add(this.streamResult);
	}

	private void oracleNativeFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * 主机地址
		 */
		this.hostName = new LabelInputMeta(id + DOT + DATADASE_HOST_NAME,
				"主机地址", null, null, null, databaseMeta.getHostname(), null,
				ValidateForm.getInstance().setRequired(true));
		this.hostName.setSingle(true);
		this.hostName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.hostName);

		/**
		 * 数据库名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "数据库名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 数据表空间
		 */
		this.data = new LabelInputMeta(id + DOT + DATADASE_DATA, "数据表空间", null,
				null, null, databaseMeta.getDataTablespace(), null, null);
		this.data.setSingle(true);
		this.data.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.data);

		/**
		 * 索引表空间
		 */
		this.index = new LabelInputMeta(id + DOT + DATADASE_INDEX, "索引表空间",
				null, null, null, databaseMeta.getIndexTablespace(), null, null);
		this.index.setSingle(true);
		this.index.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.index);

		/**
		 * 端口号
		 */
		this.port = new LabelInputMeta(id + DOT + DATADASE_PORT, "端口号", null,
				null, null, "1521", null, ValidateForm.getInstance()
						.setRequired(true));
		this.port.setSingle(true);
		this.port.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.port);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);
	}

	private void oracleJndiFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * JNDI名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "JNDI名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 数据表空间
		 */
		this.data = new LabelInputMeta(id + DOT + DATADASE_DATA, "数据表空间", null,
				null, null, databaseMeta.getDataTablespace(), null, null);
		this.data.setSingle(true);
		this.data.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.data);

		/**
		 * 索引表空间
		 */
		this.index = new LabelInputMeta(id + DOT + DATADASE_INDEX, "索引表空间",
				null, null, null, databaseMeta.getIndexTablespace(), null, null);
		this.index.setSingle(true);
		this.index.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.index);
	}

	private void oracleOdbcFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * ODBC DNS源名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME,
				"ODBC DNS源名称", null, null, null,
				databaseMeta.getDatabaseName(), null, ValidateForm
						.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 数据表空间
		 */
		this.data = new LabelInputMeta(id + DOT + DATADASE_DATA, "数据表空间", null,
				null, null, databaseMeta.getDataTablespace(), null, null);
		this.data.setSingle(true);
		this.data.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.data);

		/**
		 * 索引表空间
		 */
		this.index = new LabelInputMeta(id + DOT + DATADASE_INDEX, "索引表空间",
				null, null, null, databaseMeta.getIndexTablespace(), null, null);
		this.index.setSingle(true);
		this.index.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.index);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);
	}

	private void oracleOciFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * SID
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "SID",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 数据表空间
		 */
		this.data = new LabelInputMeta(id + DOT + DATADASE_DATA, "数据表空间", null,
				null, null, databaseMeta.getDataTablespace(), null, null);
		this.data.setSingle(true);
		this.data.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.data);

		/**
		 * 索引表空间
		 */
		this.index = new LabelInputMeta(id + DOT + DATADASE_INDEX, "索引表空间",
				null, null, null, databaseMeta.getIndexTablespace(), null, null);
		this.index.setSingle(true);
		this.index.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.index);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);
	}

	private void sapr3NativeFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * 主机地址
		 */
		this.hostName = new LabelInputMeta(id + DOT + DATADASE_HOST_NAME,
				"主机地址", null, null, null, databaseMeta.getHostname(), null,
				ValidateForm.getInstance().setRequired(true));
		this.hostName.setSingle(true);
		this.hostName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.hostName);

		/**
		 * 系统数目
		 */
		this.sAPSystemNumber = new LabelInputMeta(id + DOT
				+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER, "系统数目", null,
				null, null, databaseMeta.getAttributes().getProperty(
						SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER), null,
				ValidateForm.getInstance().setRequired(true));
		this.sAPSystemNumber.setSingle(true);
		this.sAPSystemNumber.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.sAPSystemNumber);

		/**
		 * SAP客户端
		 */
		this.sAPClient = new LabelInputMeta(id + DOT
				+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT, "SAP客户端", null, null,
				null, databaseMeta.getAttributes().getProperty(
						SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT), null,
				ValidateForm.getInstance().setRequired(true));
		this.sAPClient.setSingle(true);
		this.sAPClient.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.sAPClient);

		/**
		 * 语言
		 */
		this.sAPLanguage = new LabelInputMeta(id + DOT
				+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE, "语言", null, null,
				null, databaseMeta.getAttributes().getProperty(
						SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE), null,
				ValidateForm.getInstance().setRequired(true));
		this.sAPLanguage.setSingle(true);
		this.sAPLanguage.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.sAPLanguage);
	}

	private void commonNativeFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * 主机地址
		 */
		this.hostName = new LabelInputMeta(id + DOT + DATADASE_HOST_NAME,
				"主机地址", null, null, null, databaseMeta.getHostname(), null,
				ValidateForm.getInstance().setRequired(true));
		this.hostName.setSingle(true);
		this.hostName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.hostName);

		/**
		 * 数据库名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "数据库名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 端口号
		 */
		this.port = new LabelInputMeta(id + DOT + DATADASE_PORT, "端口号", null,
				null, null, databaseMeta.getDatabasePortNumberString(), null,
				ValidateForm.getInstance().setRequired(true));
		this.port.setSingle(true);
		this.port.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.port);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);
	}

	private void commonJndiFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * JNDI名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME, "JNDI名称",
				null, null, null, databaseMeta.getDatabaseName(), null,
				ValidateForm.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);
	}

	private void commonOdbcFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v) {
		/**
		 * ODBC DNS源名称
		 */
		this.dbName = new LabelInputMeta(id + DOT + DATADASE_DB_NAME,
				"ODBC DNS源名称", null, null, null,
				databaseMeta.getDatabaseName(), null, ValidateForm
						.getInstance().setRequired(true));
		this.dbName.setSingle(true);
		this.dbName.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.dbName);

		/**
		 * 用户名
		 */
		this.username = new LabelInputMeta(id + DOT + DATADASE_USERNAME, "用户名",
				null, null, null, databaseMeta.getUsername(), null,
				ValidateForm.getInstance().setRequired(true));
		this.username.setSingle(true);
		this.username.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.username);

		/**
		 * 密码
		 */
		this.password = new LabelInputMeta(id + DOT + DATADASE_PASSWORD, "密码",
				null, null, null, databaseMeta.getPassword(),
				InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
						.setRequired(true));
		this.password.setSingle(true);
		this.password.setLayout(LabelInputMeta.LAYOUT_COLUMN);
		v.add(this.password);
	}

	private BaseFormMeta[] initFields(String id, DatabaseMeta databaseMeta,
			int dbtype, int acctype) throws ImetaFormException {
		Vector<BaseFormMeta> v = new Vector<BaseFormMeta>();

		switch (dbtype) {
		case DatabaseMeta.TYPE_DATABASE_GENERIC:
			if (acctype == DatabaseMeta.TYPE_ACCESS_NATIVE) {
				genericNativeFields(id, databaseMeta, v);
			} else {
				commonFields(id, databaseMeta, v, acctype);
			}
			break;
		case DatabaseMeta.TYPE_DATABASE_INFORMIX:
			if (acctype == DatabaseMeta.TYPE_ACCESS_NATIVE) {
				informixNativeFields(id, databaseMeta, v);
			} else if (acctype == DatabaseMeta.TYPE_ACCESS_JNDI) {
				informixJndiFields(id, databaseMeta, v);
			} else {
				commonFields(id, databaseMeta, v, acctype);
			}
			break;
		case DatabaseMeta.TYPE_DATABASE_MSSQL:
			if (acctype == DatabaseMeta.TYPE_ACCESS_NATIVE) {
				mssqlNativeFields(id, databaseMeta, v);
			} else if (acctype == DatabaseMeta.TYPE_ACCESS_JNDI) {
				mssqlJndiFields(id, databaseMeta, v);
			} else {
				commonFields(id, databaseMeta, v, acctype);
			}
			break;
		case DatabaseMeta.TYPE_DATABASE_MYSQL:
			if (acctype == DatabaseMeta.TYPE_ACCESS_NATIVE) {
				mysqlNativeFields(id, databaseMeta, v);
			} else if (acctype == DatabaseMeta.TYPE_ACCESS_JNDI) {
				mysqlJndiFields(id, databaseMeta, v);
			} else {
				commonFields(id, databaseMeta, v, acctype);
			}
			break;
		case DatabaseMeta.TYPE_DATABASE_ORACLE:
			if (acctype == DatabaseMeta.TYPE_ACCESS_NATIVE) {
				oracleNativeFields(id, databaseMeta, v);
			} else if (acctype == DatabaseMeta.TYPE_ACCESS_JNDI) {
				oracleJndiFields(id, databaseMeta, v);
			} else if (acctype == DatabaseMeta.TYPE_ACCESS_ODBC) {
				oracleOdbcFields(id, databaseMeta, v);
			} else if (acctype == DatabaseMeta.TYPE_ACCESS_OCI) {
				oracleOciFields(id, databaseMeta, v);
			} else {
				commonFields(id, databaseMeta, v, acctype);
			}
			break;
		case DatabaseMeta.TYPE_DATABASE_SAPR3:
			if (acctype == DatabaseMeta.TYPE_ACCESS_PLUGIN) {
				sapr3NativeFields(id, databaseMeta, v);
			} else {
				commonFields(id, databaseMeta, v, acctype);
			}
			break;
		default:
			commonFields(id, databaseMeta, v, acctype);
		}

		return fieldsVectorToArray(v);

	}

	private void commonFields(String id, DatabaseMeta databaseMeta,
			Vector<BaseFormMeta> v, int acctype) {
		if (acctype == DatabaseMeta.TYPE_ACCESS_NATIVE) {
			commonNativeFields(id, databaseMeta, v);
			return;
		}
		if (acctype == DatabaseMeta.TYPE_ACCESS_JNDI) {
			commonJndiFields(id, databaseMeta, v);
			return;
		}
		if (acctype == DatabaseMeta.TYPE_ACCESS_ODBC) {
			commonOdbcFields(id, databaseMeta, v);
			return;
		}
	}

	public LabelSelectMeta getAccess() {
		return access;
	}

	public void setAccess(LabelSelectMeta access) {
		this.access = access;
	}

	public ColumnFieldsetMeta getPropertiesMeta() {
		return propertiesField;
	}

	public void setPropertiesMeta(ColumnFieldsetMeta propertiesMeta) {
		this.propertiesField = propertiesMeta;
	}

	/**
	 * 测试数据库
	 * 
	 * @param parameters
	 * @return
	 * @throws KettleException
	 */
	public String test(Map<String, String[]> parameters) throws KettleException {

		DatabaseMeta database = new DatabaseMeta();
		getInfo(database, parameters);
		String[] remarks = database.checkParameters();
		String message = "";

		if (remarks.length != 0) {
			for (int i = 0; i < remarks.length; i++) {
				message = message.concat("* ").concat(remarks[i]).concat(
						System.getProperty("line.separator"));
			}
		} else {
			message = database.testConnection();
		}
		return message;

	}

	/**
	 * 保存数据库
	 * 
	 * @param parameters
	 * @return
	 * @throws KettleException
	 */
	public String save(Map<String, String[]> parameters) throws KettleException {

		DatabaseMeta database = new DatabaseMeta();
		getInfo(database, parameters);
		RepositoryUtil.saveDatabaseMeta(database, rep);
		String[] remarks = database.checkParameters();
		String message = "保存数据库成功";

		if (remarks.length != 0) {
			for (int i = 0; i < remarks.length; i++) {
				message = message.concat("* ").concat(remarks[i]).concat(
						System.getProperty("line.separator"));
			}
			return message;
		} else {
			database.setChanged();
			return message;
		}
	}

	private void getConnectionSpecificInfo(DatabaseMeta databaseMeta,
			Map<String, String[]> parameters, String id) {
		// Hostname:
		if (parameters.containsKey(id + DOT + DATADASE_HOST_NAME)) {
			databaseMeta.setHostname(FormLoader.parameterToString(parameters
					.get(id + DOT + DATADASE_HOST_NAME)));
		}

		// Database name:
		if (parameters.containsKey(id + DOT + DATADASE_DB_NAME)) {
			databaseMeta.setDBName(FormLoader.parameterToString(parameters
					.get(id + DOT + DATADASE_DB_NAME)));
		}

		// Username:
		if (parameters.containsKey(id + DOT + DATADASE_USERNAME)) {
			databaseMeta.setUsername(FormLoader.parameterToString(parameters
					.get(id + DOT + DATADASE_USERNAME)));
		}

		// Password:
		if (parameters.containsKey(id + DOT + DATADASE_PASSWORD)) {
			databaseMeta.setPassword(FormLoader.parameterToString(parameters
					.get(id + DOT + DATADASE_PASSWORD)));
		}

		// Streaming result cursor:
		if (parameters.containsKey(id + DOT + DATADASE_STREAM_RESULT)) {
			databaseMeta.setStreamingResults(FormLoader
					.parameterToBoolean(parameters.get(id + DOT
							+ DATADASE_STREAM_RESULT)));
		}

		// Data tablespace:
		if (parameters.containsKey(id + DOT + DATADASE_DATA)) {
			databaseMeta
					.setDataTablespace(FormLoader.parameterToString(parameters
							.get(id + DOT + DATADASE_DATA)));
		}

		// Index tablespace:
		if (parameters.containsKey(id + DOT + DATADASE_INDEX)) {
			databaseMeta.setIndexTablespace(FormLoader
					.parameterToString(parameters
							.get(id + DOT + DATADASE_INDEX)));
		}

		// The SQL Server instance name overrides the option.
		if (parameters.containsKey(id + DOT + DATADASE_MSSQL_INSTANCE)) {
			databaseMeta.setSQLServerInstance(FormLoader
					.parameterToString(parameters.get(id + DOT
							+ DATADASE_MSSQL_INSTANCE)));
		}

		// SQL Server double decimal separator
		if (parameters.containsKey(id + DOT + DATADASE_DECIMAL_SEPARATOR)) {
			databaseMeta.setUsingDoubleDecimalAsSchemaTableSeparator(FormLoader
					.parameterToBoolean(parameters.get(id + DOT
							+ DATADASE_DECIMAL_SEPARATOR)));
		}
		// SAP Attributes...
		if (parameters.containsKey(id + DOT
				+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE)) {
			databaseMeta.getAttributes().put(
					SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE,
					FormLoader.parameterToString(parameters.get(id + DOT
							+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE)));
		}
		if (parameters.containsKey(id + DOT
				+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER)) {
			databaseMeta.getAttributes().put(
					SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER,
					FormLoader.parameterToString(parameters.get(id + DOT
							+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER)));
		}
		if (parameters.containsKey(id + DOT
				+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT)) {
			databaseMeta.getAttributes().put(
					SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT,
					FormLoader.parameterToString(parameters.get(id + DOT
							+ SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT)));
		}

		// Generic settings...
		if (parameters.containsKey(id + DOT
				+ GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL)) {
			databaseMeta.getAttributes().put(
					GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL,
					FormLoader.parameterToString(parameters.get(id + DOT
							+ GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL)));
		}
		if (parameters.containsKey(id + DOT
				+ GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS)) {
			databaseMeta
					.getAttributes()
					.put(
							GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS,
							FormLoader
									.parameterToString(parameters
											.get(id
													+ DOT
													+ GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS)));
		}
		// Servername:
		if (parameters.containsKey(id + DOT + DATADASE_SERVERNAME)) {
			databaseMeta.setServername(FormLoader.parameterToString(parameters
					.get(id + DOT + DATADASE_SERVERNAME)));
		}
	}

	private void getOptionInfo(DatabaseMeta databaseMeta,
			Map<String, String[]> parameters, String id) {
		String[] keys, values;
		String key, value;
		keys = parameters.get(id + DOT + OPTION_PARAMETER_NAME);
		values = parameters.get(id + DOT + OPTION_PARAMETER_VALUE);
		if (keys != null && keys.length > 0) {
			int dbType = databaseMeta.getDatabaseType();
			for (int i = 0; i < keys.length; i++) {
				key = Const.NVL(keys[i].trim(), "");
				value = Const.NVL(values[i].trim(),
						DatabaseMeta.EMPTY_OPTIONS_STRING);
				if (StringUtils.isNotEmpty(key)) {
					databaseMeta.addExtraOption(DatabaseMeta
							.getDatabaseTypeCode(dbType), key, value);
				}
			}
		}
	}

	private void getClusterInfo(DatabaseMeta databaseMeta,
			Map<String, String[]> parameters, String id) {
		boolean isPartitioned = FormLoader.parameterToBoolean(parameters.get(id
				+ DOT + CLUSTER_USE_PARTITIONED));
		databaseMeta.setPartitioned(isPartitioned);
		if (isPartitioned) {
			String[] partitionIds, hostnames, ports, dbNames, usernames, passwords;
			partitionIds = parameters.get(id + "_clusterParameters" + DOT
					+ CLUSTER_PARAMETER_PARTITION_ID);
			hostnames = parameters.get(id + "_clusterParameters" + DOT
					+ CLUSTER_PARAMETER_HOSTNAME);
			ports = parameters.get(id + "_clusterParameters" + DOT
					+ CLUSTER_PARAMETER_PORT);
			dbNames = parameters.get(id + "_clusterParameters" + DOT
					+ CLUSTER_PARAMETER_DB_NAME);
			usernames = parameters.get(id + "_clusterParameters" + DOT
					+ CLUSTER_PARAMETER_USERNAME);
			passwords = parameters.get(id + "_clusterParameters" + DOT
					+ CLUSTER_PARAMETER_PASSWORD);

			List<PartitionDatabaseMeta> pdms = new ArrayList<PartitionDatabaseMeta>();

			int rowNum = partitionIds.length;
			for (int i = 0; i < rowNum; i++) {
				if (StringUtils.isEmpty(partitionIds[i])) {
					continue;
				}

				PartitionDatabaseMeta pdm = new PartitionDatabaseMeta(
						partitionIds[i].trim(), hostnames[i].trim(), ports[i]
								.trim(), dbNames[i].trim());
				pdm.setUsername(usernames[i].trim());
				pdm.setPassword(passwords[i]);
				pdms.add(pdm);
			}

			PartitionDatabaseMeta[] pdmArray = new PartitionDatabaseMeta[pdms
					.size()];
			databaseMeta.setPartitioningInformation(pdms.toArray(pdmArray));

		}
	}

	private void getPoolingInfo(DatabaseMeta databaseMeta,
			Map<String, String[]> parameters, String id) {
		boolean isUsePool = FormLoader.parameterToBoolean(parameters.get(id
				+ DOT + POOLING_USE_POOL));
		databaseMeta.setUsingConnectionPool(isUsePool);
		if (isUsePool) {
			if (parameters.containsKey(id + DOT + POOLING_INIT_POOL_SIZE)) {
				databaseMeta.setInitialPoolSize(FormLoader
						.parameterToInt(parameters.get(id + DOT
								+ POOLING_INIT_POOL_SIZE)));
			}

			if (parameters.containsKey(id + DOT + POOLING_MAX_POOL_SIZE)) {
				databaseMeta.setMaximumPoolSize(FormLoader
						.parameterToInt(parameters.get(id + DOT
								+ POOLING_MAX_POOL_SIZE)));
			}

			// pool parameter
			Properties properties = new Properties();

			String parameter, parameterValue[], parameterCheck[];
			for (DatabaseConnectionPoolParameter p : BaseDatabaseMeta.poolingParameters) {
				parameter = Const.NVL(p.getParameter(), "");
				parameterCheck = parameters.get(id + DOT
						+ POOLING_PARAMETER_CHECK + DOT + parameter);
				parameterValue = parameters.get(id + DOT
						+ POOLING_PARAMETER_VALUE + DOT + parameter);

				if (FormLoader.parameterToBoolean(parameterCheck)) {
					properties.put(parameter, FormLoader
							.parameterToString(parameterValue));
				}
			}

			databaseMeta.setConnectionPoolingProperties(properties);
		}
	}

	public void getInfo(DatabaseMeta databaseMeta,
			Map<String, String[]> parameters) throws KettleException {
		if (super.getDatabaseMeta() != null
				&& super.getDatabaseMeta() != databaseMeta) {
			databaseMeta.initializeVariablesFrom(super.getDatabaseMeta());
		}

		String id = parameters.get("id")[0];
		// Name:
		if (parameters.containsKey(id + DOT + DATADASE_CONNECTION_NAME)) {
			databaseMeta.setName(FormLoader.parameterToString(parameters.get(id
					+ DOT + DATADASE_CONNECTION_NAME)));
		}

		// Connection type:
		if (parameters.containsKey(id + DOT + DATADASE_CONNECTION_TYPE)) {
			databaseMeta.setDatabaseType(getConnectionTypeDesc(FormLoader
					.parameterToInt(parameters.get(id + DOT
							+ DATADASE_CONNECTION_TYPE))));
		}

		// Access type:
		if (parameters.containsKey(id + DOT + DATADASE_ACCESS)) {
			databaseMeta.setAccessType(FormLoader.parameterToInt(parameters
					.get(id + DOT + DATADASE_ACCESS)));
		}

		getConnectionSpecificInfo(databaseMeta, parameters, id);

		// Port number:
		if (parameters.containsKey(id + DOT + DATADASE_PORT)) {
			databaseMeta.setDBPort(FormLoader.parameterToString(parameters
					.get(id + DOT + DATADASE_PORT)));
		}

		// Advanced panel settings:
		if (parameters.containsKey(id + DOT + ADVANCE_QUOTE_ALL_FIELDS)) {
			databaseMeta.setQuoteAllFields(FormLoader
					.parameterToBoolean(parameters.get(id + DOT
							+ ADVANCE_QUOTE_ALL_FIELDS)));
		}

		if (parameters.containsKey(id + DOT
				+ ADVANCE_FORCE_IDENTIFIERS_LOWER_CASE)) {
			databaseMeta.setForcingIdentifiersToLowerCase(FormLoader
					.parameterToBoolean(parameters.get(id + DOT
							+ ADVANCE_FORCE_IDENTIFIERS_LOWER_CASE)));
		}

		if (parameters.containsKey(id + DOT
				+ ADVANCE_FORCE_IDENTIFIERS_UPPER_CASE)) {
			databaseMeta.setForcingIdentifiersToUpperCase(FormLoader
					.parameterToBoolean(parameters.get(id + DOT
							+ ADVANCE_FORCE_IDENTIFIERS_UPPER_CASE)));
		}

		if (parameters.containsKey(id + DOT + ADVANCE_SQL_STATEMENTS)) {
			databaseMeta.setConnectSQL(FormLoader.parameterToString(parameters
					.get(id + DOT + ADVANCE_SQL_STATEMENTS)));
		}

		// Option parameters:
		getOptionInfo(databaseMeta, parameters, id);

		// Cluster settings:
		getClusterInfo(databaseMeta, parameters, id);

		// Pooling settings:
		getPoolingInfo(databaseMeta, parameters, id);

	}

	private String getConnectionTypeDesc(int t) {
		DatabaseInterface[] dbtypes = DatabaseMeta.getDatabaseInterfaces();
		for (DatabaseInterface dbtype : dbtypes) {
			if (dbtype.getDatabaseType() == t) {
				return dbtype.getDatabaseTypeDesc();
			}
		}
		return String.valueOf(t);
	}

}

package com.panet.imeta.ui.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.Props;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.row.RowMeta;
import com.panet.imeta.core.util.EnvUtil;
import com.panet.imeta.job.JobEntryLoader;
import com.panet.imeta.repository.RepositoriesMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.trans.StepLoader;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.meta.XMLFile;
import com.panet.imeta.ui.service.ImetaDelegates;

/**
 * 元数据服务层接口实现类
 * 
 * @author Peter Pan
 * 
 */
@Service("imeta.ui.imetaServices")
public class ImetaServices implements ImetaDelegates {

	private static boolean inited;

	private Map<String, XMLFile> xMLFiles;

	/**
	 * 应用名称
	 */
	private static final String APP_NAME = Messages
			.getString("Spoon.Application.Name");

	/**
	 * 日志对象
	 */
	private LogWriter log;

	/**
	 * 属性对象
	 */
	private Props props;

	/**
	 * 行元模型和数据对象
	 */
	private RowMetaAndData variables;

	/**
	 * 资源库组元模型
	 */
	private RepositoriesMeta repsMeta;

	private Map<String, Repository> reps;

	public ImetaServices() throws KettleException {
		if (!inited) {
			inited = true;
			init();
		}
		String logLevel = (String) System.getProperties().get(
				Props.STRING_LOG_LEVEL);

		this.log = LogWriter.getInstance(LogWriter.getLogLevel(logLevel));
		this.variables = new RowMetaAndData(new RowMeta(), new Object[] {});
		this.props = Props.getInstance();
		initRep();
		this.xMLFiles = new HashMap<String, XMLFile>();
	}

	public void putXMLFile(String key, String name, String xml) {
		this.xMLFiles.put(key, new XMLFile(name, xml));
	}

	public XMLFile getXMLFile(String key) {
		return this.xMLFiles.get(key);
	}

	/**
	 * 初始化
	 * 
	 * @throws KettleException
	 */
	private void init() throws KettleException {
		// 环境变量初始化
		EnvUtil.environmentInit();
		// Log初始化
		initLogging();
		// 插件初始化
		initPlugins();
	}

	/**
	 * Log初始化
	 * 
	 * @throws KettleException
	 */
	private void initLogging() throws KettleException {

		// Set default Locale:
		Locale.setDefault(Const.DEFAULT_LOCALE);

		LogWriter.setConsoleAppenderDebug();
		LogWriter log = LogWriter.getInstance(Const.SPOON_LOG_FILE, false,
				LogWriter.LOG_LEVEL_BASIC);

		if (log.getRealFilename() != null) {
			if (log.isBasic())
				log.logBasic(APP_NAME, Messages
						.getString("Spoon.Log.LoggingToFile")
						+ log.getRealFilename());// "Logging goes to "
		}
	}

	/**
	 * 插件初始化
	 * 
	 * @throws KettleException
	 */
	private void initPlugins() throws KettleException {
		/* Load the plugins etc. */
		try {
			StepLoader.init();
		} catch (KettleException e) {
			throw new KettleException(Messages
					.getString("Spoon.Log.ErrorLoadingAndHaltSystem"), e);
		}

		/* Load the plugins etc. we need to load jobentry */
		try {
			JobEntryLoader.init();
		} catch (KettleException e) {
			throw new KettleException(
					"Error loading job entries & plugins... halting Spoon!", e);
		}
	}

	/**
	 * 资源库初始化
	 */
	private void initRep() {
		// 创建资源库组元模型
		if (repsMeta == null) {
			repsMeta = new RepositoriesMeta(this.log);
			repsMeta.readData();
			reps = new HashMap<String, Repository>();
		}
	}

	public LogWriter getLog() {
		return log;
	}

	public void setLog(LogWriter log) {
		this.log = log;
	}

	public Props getProps() {
		return props;
	}

	public void setProps(Props props) {
		this.props = props;
	}

	public RowMetaAndData getVariables() {
		return variables;
	}

	public void setVariables(RowMetaAndData variables) {
		this.variables = variables;
	}

	public RepositoriesMeta getRepsMeta() {
		return repsMeta;
	}

	public void setRepsMeta(RepositoriesMeta repsMeta) {
		this.repsMeta = repsMeta;
	}

	public Repository getRep(UserInfo userInfo) {

		if (userInfo != null && userInfo.getRep() != null) {
			return userInfo.getRep();
		}
		return null;
	}

	public Repository getRep(String repName) {
		Repository rep;
		rep = reps.get(repName);
		if (rep == null) {
			rep = createRep(repName);
			reps.put(repName, rep);
		}
		return rep;
	}

	private Repository createRep(String repName) {
		Repository rep = null;
		for (int i = 0; i < repsMeta.nrRepositories(); i++) {
			if (repsMeta.getRepository(i).getName().equals(repName)) {
				rep = new Repository(log, repsMeta.getRepository(i));
				try {
					rep.connect(APP_NAME);
				} catch (KettleException ke) {
					rep = null;
				}
				return rep;
			}
		}
		return rep;
	}

	public List<String> getRepNames() {
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < repsMeta.nrRepositories(); i++) {
			names.add(repsMeta.getRepository(i).getName());
		}
		return names;
	}

	public JSONArray getReps() {
		JSONArray repsJo = new JSONArray();
		if (repsMeta != null) {
			for (int i = 0; i < repsMeta.nrRepositories(); i++) {
				repsJo.add(repsMeta.getRepository(i).getName());
			}
		}
		return repsJo;
	}

}

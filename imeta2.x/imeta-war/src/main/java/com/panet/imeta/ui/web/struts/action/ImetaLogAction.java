package com.panet.imeta.ui.web.struts.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.dialog.LogDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.meta.ImetaLog;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;

/**
 * iMeta的日志控制类
 * 
 * @author PeterPan
 * 
 */
@Controller("imeta.ui.imetaLogAction")
public class ImetaLogAction extends ActionSupport {

	private static final long serialVersionUID = -6126181680401279357L;

	// 心跳循环次数
	public static final int HEART_LOOP_NUM = 50;

	// 每次循环间隔时间
	public static final int SLEEP_TIME = 1000;

	// 日志对象类的名称
	public static final String LOG_OBJECT_NAME = "logObject";

	// 空日志返回信息
	public static final String EMPTY_RETURN = "{'success':true,'msg':'empty'}";

	/**
	 * 元模型服务代理
	 */
	@Autowired
	private ImetaDelegates delegates;

	@Autowired
	private ImetaJobDelegate jobDelegate;

	@PostConstruct
	public void init() {
		try {
			if (!jobDelegate.isAutoRun()) {
				jobDelegate.clearAutoRunJobs();
				List<String> names = delegates.getRepNames();
				if (names != null && names.size() > 0) {
					for (String n : names) {
						Repository rep = delegates.getRep(n);
						if (rep != null) {
							jobDelegate
									.initAutoRunJobs(rep, delegates.getLog());
						}
					}
				}
				jobDelegate.setAutoRun();
			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(), "加载自动启动信息错误");
		}
	}

	/**
	 * 获得日志
	 * 
	 * @throws ImetaException
	 */
	@SuppressWarnings("static-access")
	public void loadLog() throws ImetaException {
		delegates.getLog().logDebug(toString(), "开始获得会话日志的轮询.");

		boolean flag = true;
		int heartbeat = 0;

		String backstr = null;

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");

		try {
			// 延迟返回结果
			while (flag) {
				heartbeat++;

				backstr = getLogInfo();
				if (backstr != null) {
					break;
				}

				Thread.currentThread().sleep(SLEEP_TIME);

				if (heartbeat == HEART_LOOP_NUM) {
					backstr = EMPTY_RETURN;
					flag = false;// 心跳时间到，跳出循环
				}
			}

			response.getWriter().write(backstr);
		} catch (Exception e) {
			delegates.getLog().logError(toString(), "加载日志出现错误");
		}
	}

	private String getLogInfo() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		ImetaLog log = (ImetaLog) session.getAttribute(LOG_OBJECT_NAME);
		if (log != null) {

		}
		return null;
	}
	
	/**
	 * 载入日志对话框
	 */
	public void loadLogDialog() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			LogDialog log = new LogDialog();

			response.getWriter().write(log.open().toString());
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "loadLog"));
		}
	}
}

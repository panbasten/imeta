package com.panet.imeta.ui.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.dialog.AnalyseImpactProgressDialog;
import com.panet.imeta.ui.dialog.CheckTransProgressDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;

@Controller("imeta.ui.imetaTransAction")
public class ImetaTransAction extends ActionSupport {

	private static final long serialVersionUID = 4430732371055383615L;

	@Autowired
	private ImetaDelegates delegates;

	@Autowired
	private ImetaTransformationDelegate trans;

	/**
	 * 验证转换
	 * 
	 * @throws ImetaException
	 */
	public void checkTrans() throws ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		try {
			String rootId = request.getParameter("taskId");
			String objectName = request.getParameter("objectName");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			TransMeta transMeta = trans.getEditTransMetaByName(delegates
					.getRep(UserInfo.getLoginUser()), d, objectName);
			CheckTransProgressDialog dialog = new CheckTransProgressDialog(
					transMeta);
			dialog.setRootId(rootId);
			response.getWriter().write(dialog.open().toString());
		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"checkTrans"));
		}
	}

	/**
	 * 分析转换
	 * 
	 * @throws ImetaException
	 */
	public void analyseImpactTrans() throws ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		try {
			String rootId = request.getParameter("taskId");
			String objectName = request.getParameter("objectName");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			TransMeta transMeta = trans.getEditTransMetaByName(delegates
					.getRep(UserInfo.getLoginUser()), d, objectName);
			AnalyseImpactProgressDialog dialog = new AnalyseImpactProgressDialog(
					transMeta);
			dialog.setRootId(rootId);
			response.getWriter().write(dialog.open().toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"analyseImpactTrans"));
		}
	}

}

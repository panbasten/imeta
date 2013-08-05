package com.panet.imeta.ui.web.struts.action;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.opensymphony.xwork2.ActionSupport;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.meta.ImetaUIUtils;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;

/**
 * 处理文件上传的Action类
 * 
 * @author panwei
 */
@Controller("imeta.ui.fileUploadAction")
public class FileUploadAction extends ActionSupport {
	private static final long serialVersionUID = -5930340660887095067L;
	@Autowired
	private ImetaDelegates delegates;

	@Autowired
	private ImetaJobDelegate jobs;

	@Autowired
	private ImetaTransformationDelegate trans;

	// 上传文件域对象
	private File upload;

	public void uploadXMLFile() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		Repository rep = getLoginUser().getRep();
		try {
			String id = request.getParameter("id");
			String objectType = request.getParameter(id + ".objectType");
			String path = request.getParameter(id + ".path");
			Document doc = XMLHandler.loadXMLFile(this.upload);
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(objectType)) {
				Node node = XMLHandler.getSubNode(doc, TransMeta.XML_TAG);
				if (node == null) {
					return;
				}
				TransMeta newTransMeta = new TransMeta(node, rep);
				newTransMeta.clearChanged();
				newTransMeta.setRepository(rep);
				Date now = new Date();
				newTransMeta.setCreatedDate(now);
				newTransMeta.setModifiedDate(now);
				newTransMeta.setCreatedUser(getLoginUser().getLogin());
				newTransMeta.setModifiedUser(getLoginUser().getLogin());

				RepositoryDirectory d = ImetaUIUtils.getDirectoryByPath(rep,
						path);
				long directoryId = d.getID();
				newTransMeta.setDirectory(d);

				String newname = ImetaUIUtils.getCheckedTransMetaName(rep,
						directoryId, newTransMeta.getName(), trans);
				newTransMeta.setName(newname);

				trans.putEditTransMeta(rep, newTransMeta);
				response
						.getWriter()
						.write(
								drawRepObjectByNameAndType(
										directoryId,
										newname,
										RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION,
										true));
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
					.equals(objectType)) {
				Node node = XMLHandler.getSubNode(doc, JobMeta.XML_TAG);
				if (node == null) {
					return;
				}
				JobMeta newJobMeta = new JobMeta(delegates.getLog(), node, rep,
						null);
				newJobMeta.clearChanged();
				newJobMeta.setRep(rep);
				newJobMeta.readSharedObjects(rep);

				Date now = new Date();
				newJobMeta.setCreatedDate(now);
				newJobMeta.setModifiedDate(now);
				newJobMeta.setCreatedUser(getLoginUser().getLogin());
				newJobMeta.setModifiedUser(getLoginUser().getLogin());

				RepositoryDirectory d = ImetaUIUtils.getDirectoryByPath(rep,
						path);
				long directoryId = d.getID();
				newJobMeta.setDirectory(d);

				String newname = ImetaUIUtils.getCheckedJobMetaName(rep,
						directoryId, newJobMeta.getName(), jobs);
				newJobMeta.setName(newname);
				jobs.putEditJobMeta(rep, newJobMeta);
				response.getWriter().write(
						drawRepObjectByNameAndType(directoryId, newname,
								RepositoryObject.STRING_OBJECT_TYPE_JOB, true));
			}
		} catch (Exception e) {
			return;
		}
	}

	private String drawRepObjectByNameAndType(long directoryId, String roName,
			String roType, boolean exeditable) throws ImetaException {
		Repository rep = delegates.getRep(getLoginUser());
		UserInfo user = getLoginUser();
		return ImetaUIUtils.drawRepObjectByNameAndType(directoryId, roName,
				roType, exeditable, rep, user, trans, jobs);
	}

	private UserInfo getLoginUser() {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfo userInfo = (UserInfo) request.getSession(false).getAttribute(
				UserInfo.STRING_USERINFO);
		return userInfo;

	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

}

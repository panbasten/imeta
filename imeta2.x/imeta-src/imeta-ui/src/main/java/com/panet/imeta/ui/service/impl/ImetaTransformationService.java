package com.panet.imeta.ui.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.panet.iform.exception.ImetaFormException;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.plugins.Plugin;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.trans.StepLoader;
import com.panet.imeta.trans.StepPlugin;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransDialogInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.dialog.TransSettingDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;

@Service("imeta.ui.imetaTransformationService")
public class ImetaTransformationService implements ImetaTransformationDelegate {

	/**
	 * 缓存的转换
	 */
	private static Map<String, TransMeta> cacheTransMetaMap = new HashMap<String, TransMeta>();
	/**
	 * 正在编辑的转换
	 */
	private static Map<String, TransMeta> editTransMetaMap = new HashMap<String, TransMeta>();

	/**
	 * 正在运行的转换实例
	 */
	private static Map<String, Trans> activeTransMap = new HashMap<String, Trans>();

	private JSONArray transStepTab = null;

	public ImetaTransformationService() {
		super();
	}

	/**
	 * 得到正在运行的Trans
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @return
	 * @throws ImetaException
	 */
	public Trans getActiveTransByName(Repository rep, long directoryId,
			String transname) throws ImetaException {
		try {
			String lookupId = rep.getName() + "." + directoryId + "."
					+ transname;
			Trans trans = activeTransMap.get(lookupId);
			if (trans != null) {
				if (trans.isFinished()) {
					activeTransMap.remove(lookupId);
					return null;
				}
			}
			return trans;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 清除Trans
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 */
	public void clearActiveTransByName(Repository rep, long directoryId,
			String transname) {
		synchronized (activeTransMap) {
			String lookupId = rep.getName() + "." + directoryId + "."
					+ transname;
			activeTransMap.remove(lookupId);
		}
	}

	/**
	 * 清除完成的Trans
	 */
	public void clearActiveTrans() {
		synchronized (activeTransMap) {
			if (activeTransMap != null && activeTransMap.size() > 0) {
				String key;
				Trans trans;
				for (Iterator<String> keys = activeTransMap.keySet().iterator(); keys
						.hasNext();) {
					key = keys.next();
					trans = activeTransMap.get(key);
					if (trans.isFinished()) {
						activeTransMap.remove(key);
					}
				}
			}
		}
	}

	/**
	 * 保存Trans
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @param trans
	 */
	public void putActiveTransByName(Repository rep, long directoryId,
			String transname, Trans trans) {
		synchronized (activeTransMap) {
			String lookupId = rep.getName() + "." + directoryId + "."
					+ transname;
			activeTransMap.put(lookupId, trans);
		}
	}

	/**
	 * 通过名称获得Transformation
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @return
	 * @throws ImetaException
	 */
	public TransMeta getEditTransMetaByName(Repository rep, long directoryId,
			String transname) throws ImetaException {
		try {
			String lookupId = rep.getName() + "." + directoryId + "."
					+ transname;
			TransMeta transMeta = editTransMetaMap.get(lookupId);
			if (transMeta == null) {
				return null;
			}
			if (transMeta.getRepository() == null) {
				transMeta.setRepository(rep);
			}
			return transMeta;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 通过名字创建转换
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @return
	 * @throws KettleException
	 */
	public TransMeta newTransMetaByName(Repository rep, long directoryId,
			String transname) throws KettleException {
		RepositoryDirectory directory = new RepositoryDirectory(rep);
		TransMeta transMeta = new TransMeta(rep, transname, directory
				.findDirectory(directoryId));
		return transMeta;
	}

	/**
	 * 得到转换
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @return
	 * @throws ImetaException
	 */
	public TransMeta getTransMetaByName(Repository rep, long directoryId,
			String transname) throws ImetaException {
		try {
			String lookupId = rep.getName() + "." + directoryId + "."
					+ transname;
			TransMeta transMeta = cacheTransMetaMap.get(lookupId);
			if (transMeta == null) {
				transMeta = newTransMetaByName(rep, directoryId, transname);
				cacheTransMetaMap.put(lookupId, transMeta);
			}
			return transMeta;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 增加或替换数据库
	 * 
	 * @param databaseMeta
	 */
	public void addOrReplaceDatabase(DatabaseMeta databaseMeta) {
		synchronized (editTransMetaMap) {
			for (TransMeta trans : editTransMetaMap.values()) {
				trans.addOrReplaceDatabase(databaseMeta);
			}
		}
		synchronized (cacheTransMetaMap) {
			cacheTransMetaMap.clear();
		}
	}

	/**
	 * 移除数据库
	 * 
	 * @param i
	 */
	public void removeDatabases(long i) {
		synchronized (editTransMetaMap) {
			for (TransMeta trans : editTransMetaMap.values()) {
				trans.removeDatabase(i);
			}
		}
		synchronized (cacheTransMetaMap) {
			cacheTransMetaMap.clear();
		}
	}

	/**
	 * 通过名称清除一个Transformation
	 * 
	 * @param rep
	 * @param transname
	 */
	public void clearTransMetaByName(Repository rep, long directoryId,
			String transname) {
		synchronized (cacheTransMetaMap) {
			String lookupId = rep.getName() + "." + directoryId + "."
					+ transname;
			cacheTransMetaMap.remove(lookupId);
		}
	}

	public void clearEditTransMetaByName(Repository rep, long directoryId,
			String transname) {
		synchronized (editTransMetaMap) {
			String lookupId = rep.getName() + "." + directoryId + "."
					+ transname;
			editTransMetaMap.remove(lookupId);
		}
	}

	/**
	 * 通过用户登录名清除正在编辑的转换
	 * 
	 * @param rep
	 * @param loginName
	 */
	public void clearEditTransMetaByLoginName(Repository rep, String loginName) {
		synchronized (editTransMetaMap) {
			String lookupId = null;
			for (Iterator<String> iter = editTransMetaMap.keySet().iterator(); iter
					.hasNext();) {
				lookupId = iter.next();
				if (lookupId.startsWith(rep.getName() + ".")) {
					TransMeta trans = editTransMetaMap.get(lookupId);
					if (trans != null
							&& loginName.equals(trans.getModifiedUser())) {
						editTransMetaMap.remove(lookupId);
					}
				}
			}
		}
	}

	/**
	 * 设置一个Transformation
	 * 
	 * @param rep
	 * @param transMeta
	 * @throws ImetaException
	 */
	public void putEditTransMeta(Repository rep, TransMeta transMeta)
			throws ImetaException {
		try {
			synchronized (editTransMetaMap) {
				RepositoryDirectory directory = transMeta.getDirectory();
				String lookupId = rep.getName() + "." + directory.getID() + "."
						+ transMeta.getName();
				try {
					TransMeta editTransMeta = newTransMetaByName(rep,directory.getID(), transMeta
							.getName());
					editTransMetaMap.put(lookupId, editTransMeta);
				} catch (KettleException ke) {
					editTransMetaMap.put(lookupId, transMeta);
				}
			}

		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 得到转换设置的JSON表达式
	 * 
	 * @param rep
	 * @param directoryId
	 * @param trnasname
	 * @param winId
	 * @return
	 * @throws ImetaFormException
	 */
	public JSONObject getSettingContent(Repository rep,long directoryId, String trnasname,
			String winId) throws ImetaFormException {

		try {
			TransMeta transMeta = getTransMetaByName(rep, directoryId, trnasname);
			TransDialogInterface transDialog = new TransSettingDialog(
					transMeta, winId);
			return transDialog.open();
		} catch (ImetaException ex) {
			throw new ImetaFormException("转换错误", ex);
		}

	}

	/**
	 * 加入转换的步骤
	 * 
	 * @throws ImetaException
	 */
	public JSONArray getTransStepTab() throws ImetaException {
		if (this.transStepTab != null) {
			return this.transStepTab;
		}
		StepLoader sl = StepLoader.getInstance();
		String[] c = sl.getCategories(Plugin.TYPE_ALL);
		StepPlugin[] sps;
		StepPlugin sp;
		this.transStepTab = new JSONArray();
		for (int i = 0; i < c.length; i++) {
			JSONObject tabTitle = new JSONObject();
			tabTitle.put("createTag", "<a class='toolbar-trans'></a>");
			tabTitle.put("html", c[i]);
			this.transStepTab.add(tabTitle);

			JSONObject tabBody = new JSONObject();
			tabBody.put("createTag",
					"<div class='toolbar-trans toolbar-content'></div>");

			JSONArray tabBody_items = new JSONArray();
			sps = sl.getStepsWithCategory(c[i]);
			if (sps != null && sps.length > 0) {
				for (int j = 0; j < sps.length; j++) {
					sp = sps[j];
					if (sp != null) {
						JSONObject item = new JSONObject();
						item.put("createTag", "<div></div>");

						JSONObject item_frame = new JSONObject();
						item_frame.put("id", sp.getID()[0]);
						item_frame.put("img", sp.getIconFilename());
						item_frame.put("name", sp.getDescription());
						item_frame.put("title", sp.getTooltip());

						item.put("frame", item_frame);

						tabBody_items.add(item);
					}
				}
			}

			tabBody.put("items", tabBody_items);
			this.transStepTab.add(tabBody);
		}
		return this.transStepTab;
	}
}

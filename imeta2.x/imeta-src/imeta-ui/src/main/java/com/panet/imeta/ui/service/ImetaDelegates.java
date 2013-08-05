package com.panet.imeta.ui.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.panet.imeta.core.Props;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.repository.RepositoriesMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.ui.meta.XMLFile;

/**
 * 元数据服务层接口类
 * 
 * @author Peter Pan
 * 
 */
public interface ImetaDelegates {

	public LogWriter getLog();

	public Props getProps();

	public RowMetaAndData getVariables();

	public RepositoriesMeta getRepsMeta();

	public Repository getRep(String repName);

	public Repository getRep(UserInfo userInfo);

	public JSONArray getReps();
	
	public void putXMLFile(String key, String name, String xml);
	
	public XMLFile getXMLFile(String key);
	
	public List<String> getRepNames();

}

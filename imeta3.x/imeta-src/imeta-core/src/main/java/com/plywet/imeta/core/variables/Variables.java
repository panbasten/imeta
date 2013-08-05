package com.plywet.imeta.core.variables;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.plywet.imeta.utils.Const;
import com.plywet.imeta.utils.StringUtil;

/**
 * 该类是VariableSpace的一个实现类
 * 
 * @author 潘巍（Peter Pan）
 * @since 2010-9-9 上午08:24:14
 */
public class Variables implements VariableSpace {
	private Map<String, String> properties;
	private VariableSpace parent;
	private Map<String, String> injection;
	private boolean initialized;

	public Variables() {
		properties = new Hashtable<String, String>();
		parent = null;
		injection = null;
		initialized = false;
	}

	public void copyVariablesFrom(VariableSpace space) {
		if (space != null && this != space) {
			// If space is not null and this variable is not already
			// the same object as the argument.
			// 如果空间不为null，并且该变量没有相同名称的对象已经注入
			String[] variableNames = space.listVariables();
			for (int idx = 0; idx < variableNames.length; idx++) {
				properties.put(variableNames[idx], space
						.getVariable(variableNames[idx]));
			}
		}
	}

	public VariableSpace getParentVariableSpace() {
		return parent;
	}

	public void setParentVariableSpace(VariableSpace parent) {
		this.parent = parent;
	}

	public String getVariable(String variableName, String defaultValue) {
		String var = properties.get(variableName);
		if (var == null)
			return defaultValue;
		return var;
	}

	public String getVariable(String variableName) {
		return properties.get(variableName);
	}

	public boolean getBooleanValueOfVariable(String variableName,
			boolean defaultValue) {
		if (!Const.isEmpty(variableName)) {
			String value = environmentSubstitute(variableName);
			if (!Const.isEmpty(value)) {
				return StringUtil.convertStringToBoolean(value);
			}
		}
		return defaultValue;
	}

	public void initializeVariablesFrom(VariableSpace parent) {
		this.parent = parent;

		// 添加所有系统属性
		for (Object key : System.getProperties().keySet()) {
			properties.put((String) key, System.getProperties().getProperty(
					(String) key));
		}

		if (parent != null) {
			copyVariablesFrom(parent);
		}
		if (injection != null) {
			properties.putAll(injection);
			injection = null;
		}
		initialized = true;
	}

	public String[] listVariables() {
		List<String> list = new ArrayList<String>();
		for (String name : properties.keySet()) {
			list.add(name);
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public void setVariable(String variableName, String variableValue) {
		if (variableValue != null) {
			properties.put(variableName, variableValue);
		} else {
			properties.remove(variableName);
		}
	}

	public String environmentSubstitute(String aString) {
		if (aString == null || aString.length() == 0)
			return aString;

		return StringUtil.environmentSubstitute(aString, properties);
	}

	public String[] environmentSubstitute(String string[]) {
		String retval[] = new String[string.length];
		for (int i = 0; i < string.length; i++) {
			retval[i] = environmentSubstitute(string[i]);
		}
		return retval;
	}

	public void shareVariablesWith(VariableSpace space) {
		// 此处未实现
	}

	public void injectVariables(Map<String, String> prop) {
		if (initialized) {
			// 变量已经初始化
			if (prop != null) {
				properties.putAll(prop);
				injection = null;
			}
		} else {
			// 因为我们自己个人的复制，所以事后改变属性不会影响我们
			injection = new Hashtable<String, String>();
			injection.putAll(prop);
		}
	}

	/**
	 * 获得默认的变量空间。当我们需要一个新的实例时使用
	 * 
	 * @return 默认工作空间
	 */
	synchronized public static VariableSpace getADefaultVariableSpace() {
		VariableSpace space = new Variables();

		space.initializeVariablesFrom(null);

		return space;
	}
}
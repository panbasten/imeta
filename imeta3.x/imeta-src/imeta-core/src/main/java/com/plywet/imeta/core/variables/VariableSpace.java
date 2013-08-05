package com.plywet.imeta.core.variables;

import java.util.Map;

/**
 * 该接口用于参数敏感对象
 * 
 * @author 潘巍（Peter Pan）
 * @since 2010-9-9 上午01:29:02
 */
public interface VariableSpace {
	/**
	 * 使用默认来创建变量空间，复制覆盖父对象的变量（使用copyVariablesFrom()），
	 * 然后将"injected"变量插入其中（injectVariables()）。
	 * 
	 * parent设置为父变量空间
	 * 
	 * @param parent
	 *            开始于parent，如果是根parent为null
	 */
	void initializeVariablesFrom(VariableSpace parent);

	/**
	 * 从其他空间复制变量，不带有默认的初始化。这不影响然后parent关系。
	 * 
	 * @param space
	 *            从space中复制变量
	 */
	void copyVariablesFrom(VariableSpace space);

	/**
	 * Share a variable space from another variable space. This means that the
	 * object should take over the space used as argument.
	 * 从其他变量空间中共享一个变量空间。这意味着该对象应该作为一个变量接管它。
	 * 
	 * @param space
	 *            要共享的变量空间
	 */
	void shareVariablesWith(VariableSpace space);

	/**
	 * 获得变量空间的父
	 * 
	 * @return parent.
	 */
	VariableSpace getParentVariableSpace();

	/**
	 * 设置变量空间的父
	 * 
	 * @param parent
	 */
	void setParentVariableSpace(VariableSpace parent);

	/**
	 * 添加一个变量
	 * 
	 * @param variableName
	 *            变量名
	 * @param variableValue
	 *            变量值。如果变量值是null，该变量将会从列表中清除
	 */
	void setVariable(String variableName, String variableValue);

	/**
	 * 获得一个变量值，如果变量没有找到，返回默认值
	 * 
	 * @param variableName
	 *            变量名
	 * @param defaultValue
	 *            当变量没有找到时，返回的默认值
	 * @return
	 */
	String getVariable(String variableName, String defaultValue);

	/**
	 * 获得一个变量值
	 * 
	 * @param variableName
	 *            变量名
	 * @return 变量值，如果变量没有找到返回null
	 */
	String getVariable(String variableName);

	/**
	 * 该方法返回一个布尔值。如果如果变量名没有设置或者没有指定变量名，该方法会简单返回默认值。
	 * 否则，它将转换获得的变量值为布尔值。"Y", "YES" 和 "TRUE"都会转换成true。
	 * 
	 * @see also static method ValueMeta.convertStringToBoolean()
	 * 
	 * @param variableName
	 *            要查找的变量名
	 * @param defaultValue
	 *            返回的默认值
	 * @return
	 */
	boolean getBooleanValueOfVariable(String variableName, boolean defaultValue);

	/**
	 * 当前变量空间中的变量名列表（不是值）
	 * 
	 * @return 变量名列表
	 */
	String[] listVariables();

	/**
	 * 用当前的变量空间替代该string
	 * 
	 * @param aString
	 *            要替代的string
	 * 
	 * @return 替代后的string
	 */
	String environmentSubstitute(String aString);

	/**
	 * 用环境变量替换string数组
	 * 
	 * See also: environmentSubstitute(String string)
	 * 
	 * @param string
	 *            要替换的string数组
	 * @return 替换后的string数组
	 */
	public String[] environmentSubstitute(String string[]);

	/**
	 * 注入变量。该功能用于属性对象存储和变量空间初始化（或者在空间已经初始化后手工调用）。
	 * 注入之后，属性对象的链接应该被移除。
	 * 
	 * @param prop
	 *            包含key-value对的属性对象
	 */
	void injectVariables(Map<String, String> prop);
}
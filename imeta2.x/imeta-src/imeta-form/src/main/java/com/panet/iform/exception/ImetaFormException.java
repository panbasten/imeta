/**
 * @(#)InputMeta.java 1.00 2009/06/22
 *
 * Copyright (c) 2009 中科软科技股份有限公司 版权所有
 * Sinosoft Co.,LTD. All rights reserved.
 * 
 * This software was developed by Sinosoft Corporation. 
 * You shall not disclose and decompile such software 
 * information or code and shall use it only in accordance 
 * with the terms of the contract agreement you entered 
 * into with Sinosoft.
 */
package com.panet.iform.exception;

import com.panet.imeta.core.Const;

/**
* Imeta 表单抛出的异常
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class ImetaFormException extends Exception {

	private static final long serialVersionUID = 3904039433128047791L;

	public ImetaFormException() {
		super();
	}

	public ImetaFormException(Throwable cause) {
		super(cause);
	}

	public ImetaFormException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 得到原始原因的返回信息
	 */
	public String getMessage() {
		String retval = Const.CR;
		retval += super.getMessage() + Const.CR;

		Throwable cause = getCause();
		if (cause != null) {
			String message = cause.getMessage();
			if (message != null) {
				retval += message + Const.CR;
			} else {
				// 原因轨迹
				StackTraceElement ste[] = cause.getStackTrace();
				for (int i = ste.length - 1; i >= 0; i--) {
					retval += "	at " + ste[i].getClassName() + "."
							+ ste[i].getMethodName() + " ("
							+ ste[i].getFileName() + ":"
							+ ste[i].getLineNumber() + ")" + Const.CR;
				}
			}
		}

		return retval;
	}

	public String getSuperMessage() {
		return super.getMessage();
	}
}

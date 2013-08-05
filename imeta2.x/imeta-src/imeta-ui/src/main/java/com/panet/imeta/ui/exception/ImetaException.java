package com.panet.imeta.ui.exception;

import com.panet.imeta.core.Const;

public class ImetaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4618399891452374841L;

	public ImetaException() {
		super();
	}
	
	public ImetaException(String message){
		super(message);
	}

	public ImetaException(Throwable cause) {
		super(cause);
	}

	public ImetaException(String message, Throwable cause) {
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

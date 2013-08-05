package com.plywet.imeta.core.exception;

import com.plywet.imeta.utils.Const;

/**
 * 这是一个通用的异常
 * 
 * @since 1.0 2010-1-20
 * @author 潘巍（Peter Pan）
 * 
 */
public class ImetaException extends Exception {
	private static final long serialVersionUID = -2260895195255402040L;

	private String message = "";

	/**
	 * 构造一个新的可抛出的空异常，作为它的详细信息。
	 */
	public ImetaException() {
		super();
	}

	/**
	 * 构造一个新的指定详细信息的抛出异常
	 * 
	 * @param message
	 *            - 详细信息。该详细信息被保存用来供以后的getMessage()方法调用。
	 */
	public ImetaException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * 构造一个新的指定原因和详细信息(cause==null ? null :
	 * cause.toString())的可抛出异常。（典型包括该类和原因的详细信息）
	 * 
	 * @param cause
	 *            原因（被保存用来供以后的getCause()方法调用）。（允许空值，表明该原因不存在或者未知）
	 */
	public ImetaException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造一个新的指定详细信息和原先的可抛出异常
	 * 
	 * @param message
	 *            详细信息（被保存用来供以后的getMessage()方法调用）
	 * @param cause
	 *            原因（被保存用来供以后的getCause()方法调用）。（允许空值，表明该原因不存在或者未知）
	 */
	public ImetaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 返回原始的原因信息。
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
				// 增加原因元素的堆栈
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

	public String getString() {
		return this.getClass().getName() + ":" + message;
	}

	public String getSuperMessage() {
		return super.getMessage();
	}

}

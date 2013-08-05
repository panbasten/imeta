package com.plywet.imeta.core.exception;

/**
 * 该异常在查找XML片段时发生的错误被抛出的。
 * 
 * @since 1.0 2010-1-27
 * @author 潘巍（Peter Pan）
 * 
 */
public class ImetaXMLException extends ImetaException {

	private static final long serialVersionUID = 7599692421258551215L;

	public ImetaXMLException() {
		super();
	}

	public ImetaXMLException(String message) {
		super(message);
	}

	public ImetaXMLException(Throwable cause) {
		super(cause);
	}

	public ImetaXMLException(String message, Throwable cause) {
		super(message, cause);
	}
}

package com.plywet.imeta.platform.exception;

import com.plywet.imeta.core.exception.ImetaException;

public class ImetaViewBeanException extends ImetaException {

	private static final long serialVersionUID = 5335906234702494919L;

	public ImetaViewBeanException() {
		super();
	}

	public ImetaViewBeanException(String message) {
		super(message);
	}

	public ImetaViewBeanException(Throwable cause) {
		super(cause);
	}

	public ImetaViewBeanException(String message, Throwable cause) {
		super(message, cause);
	}
}

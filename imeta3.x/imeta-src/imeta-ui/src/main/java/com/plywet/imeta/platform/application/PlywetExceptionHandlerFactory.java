package com.plywet.imeta.platform.application;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class PlywetExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory base;

	public PlywetExceptionHandlerFactory(ExceptionHandlerFactory base) {
		this.base = base;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return new PlywetExceptionHandler(base.getExceptionHandler());
	}

}

package com.plywet.imeta.core.log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author 潘巍（PeterPan）
 * @since 2011-7-12 下午03:17:50
 * 
 */
public class Jdk14Log extends Log {

	Logger log;

	public Jdk14Log(Logger logger) {
		this.log = logger;
	}

	public void error(String msg) {
		log.log(Level.SEVERE, msg);
	}

	public void error(String msg, Throwable exception) {
		log.log(Level.SEVERE, msg, exception);
	}

	public boolean isInfoEnabled() {
		return log.isLoggable(Level.INFO);
	}

	public void info(String msg) {
		log.log(Level.INFO, msg);
	}

	public void info(String msg, Throwable exception) {
		log.log(Level.INFO, msg, exception);
	}

	public boolean isDebugEnabled() {
		return log.isLoggable(Level.FINE);
	}

	public void debug(String msg) {
		log.log(Level.FINE, msg);
	}

	public void debug(String msg, Throwable exception) {
		log.log(Level.FINE, msg, exception);
	}

	public boolean isTraceEnabled() {
		return log.isLoggable(Level.FINEST);
	}

	public void trace(String msg) {
		log.log(Level.FINEST, msg);
	}

	public void trace(String msg, Throwable exception) {
		log.log(Level.FINEST, msg, exception);
	}

	public boolean isWarnEnabled() {
		return log.isLoggable(Level.WARNING);
	}

	public void warn(String msg) {
		log.warning(msg);
	}

	public void warn(String msg, Throwable exception) {
		log.log(Level.WARNING, msg, exception);
	}

}

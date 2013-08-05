package com.plywet.imeta.core.log;

import org.apache.log4j.LogManager;

/**
 * 
 * @author 潘巍（PeterPan）
 * @since 2011-7-12 下午03:19:22
 * 
 */
public class Log4jLogFactory implements LogFactory {

	public Log getLog(String name) {
		return new Log4jLog(LogManager.getLogger(name));
	}

}

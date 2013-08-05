package com.plywet.imeta.core.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * 
 * @author 潘巍（PeterPan）
 * @since 2011-7-12 下午03:18:06
 * 
 */
public class Jdk14LogFactory implements LogFactory {

	public Jdk14LogFactory() {
		initializeJdk14Logging();
	}

	public Log getLog(String name) {
		return new Jdk14Log(Logger.getLogger(name));
	}

	/**
	 * redirects commons logging to JDK 1.4 logging. This can be handy when you
	 * have log4j on the classpath, but still want to use the JDK logging.
	 */
	public static synchronized void redirectCommonsToJdk14() {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.Jdk14Logger");
	}

	/**
	 * configures JDK 1.4 logging from the resource file
	 * <code>logging.properties</code>
	 */
	public static synchronized void initializeJdk14Logging() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream inputStream = classLoader
				.getResourceAsStream("logging.properties");
		try {
			if (inputStream != null) {
				LogManager.getLogManager().readConfiguration(inputStream);

				String redirectCommons = LogManager.getLogManager()
						.getProperty("redirect.commons.logging");
				if ((redirectCommons != null)
						&& (!redirectCommons.equalsIgnoreCase("disabled"))
						&& (!redirectCommons.equalsIgnoreCase("off"))
						&& (!redirectCommons.equalsIgnoreCase("false"))) {
					redirectCommonsToJdk14();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("couldn't initialize logging properly",
					e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

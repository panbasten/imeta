package com.plywet.imeta.core.log;

/**
 * 
 * @author 潘巍（PeterPan）
 * @since 2011-7-12 下午03:18:21
 * 
 */
public abstract class Log {

	static LogFactory logFactory;

	public static synchronized Log getLog(String name) {
		if (logFactory == null) {

			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();

			// 如果logging.properties在classpath中存在
			if (classLoader.getResource("logging.properties") != null) {
				logFactory = new Jdk14LogFactory();
			}
			// 如果log4j在classpath中存在
			else if (isLog4jAvailable(classLoader)) {
				logFactory = new Log4jLogFactory();
			} 
			// 否则使用默认的Log
			else {
				logFactory = new Jdk14LogFactory();

			}
		}
		return logFactory.getLog(name);
	}

	static boolean isLog4jAvailable(ClassLoader classLoader) {
		try {
			Class.forName("org.apache.log4j.LogManager", false, classLoader);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public abstract void error(String msg);

	public abstract void error(String msg, Throwable exception);

	public abstract boolean isInfoEnabled();

	public abstract void info(String msg);

	public abstract void info(String msg, Throwable exception);

	public abstract boolean isDebugEnabled();

	public abstract void debug(String msg);

	public abstract void debug(String msg, Throwable exception);

	public abstract boolean isTraceEnabled();

	public abstract void trace(String msg);

	public abstract void trace(String msg, Throwable exception);

	public abstract boolean isWarnEnabled();

	public abstract void warn(String msg);

	public abstract void warn(String msg, Throwable exception);

}

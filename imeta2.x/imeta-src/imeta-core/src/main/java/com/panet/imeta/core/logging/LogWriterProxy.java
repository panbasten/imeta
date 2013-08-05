package com.panet.imeta.core.logging;

import org.apache.log4j.Appender;

import com.panet.imeta.core.Const;

public class LogWriterProxy implements LogWriterInterface {

	public static final String IVOKE_TYPE_MANUAL = "Manual";
	
	public static final String IVOKE_TYPE_WEBSERVICE = "WebService";

	private int logLevel;

	private LogWriter log;

	private String operateUser;

	private String ivokeType;

	public LogWriterProxy() {
		this.log = LogWriter.getInstance();
		this.logLevel = log.getLogLevel();
	}

	public LogWriterProxy(LogWriter log) {
		this();
		if (log != null) {
			this.log = log;
			this.logLevel = log.getLogLevel();
		}
	}

	public LogWriterProxy(LogWriter log, int level) {
		this(log);
		if (level >= LogWriter.LOG_LEVEL_NOTHING
				&& level <= LogWriter.LOG_LEVEL_ROWLEVEL) {
			this.logLevel = level;
		}
	}

	public LogWriterProxy(LogWriter log, int level, String operateUser,
			String ivokeType) {
		this(log, level);
		this.operateUser = operateUser;
		this.ivokeType = ivokeType;
	}

	public String getOperateUser() {
		return operateUser;
	}

	public String getIvokeType() {
		return ivokeType;
	}

	public void addAppender(Appender appender) {
		log.addAppender(appender);
	}

	public boolean isBasic() {
		return logLevel >= LogWriter.LOG_LEVEL_BASIC;
	}

	public boolean isDebug() {
		return logLevel >= LogWriter.LOG_LEVEL_DEBUG;
	}

	public boolean isDetailed() {
		return logLevel >= LogWriter.LOG_LEVEL_DETAILED;
	}

	public boolean isRowLevel() {
		return logLevel >= LogWriter.LOG_LEVEL_ROWLEVEL;
	}

	public void logBasic(String subject, String message, Object... args) {
		log
				.println(logLevel, LogWriter.LOG_LEVEL_BASIC, subject, message,
						args);
	}

	public void logDebug(String subject, String message, Object... args) {
		log
				.println(logLevel, LogWriter.LOG_LEVEL_DEBUG, subject, message,
						args);
	}

	public void logDetailed(String subject, String message, Object... args) {
		log.println(logLevel, LogWriter.LOG_LEVEL_DETAILED, subject, message,
				args);
	}

	public void logError(String subject, String message, Object... args) {
		log
				.println(logLevel, LogWriter.LOG_LEVEL_ERROR, subject, message,
						args);
	}

	public void logError(String subject, String message, Throwable e) {
		String stackTrace = Const.getStackTracker(e);
		log.println(logLevel, LogWriter.LOG_LEVEL_ERROR, subject, message);
		log.println(logLevel, LogWriter.LOG_LEVEL_ERROR, subject, stackTrace);
	}

	public void logMinimal(String subject, String message, Object... args) {
		log.println(logLevel, LogWriter.LOG_LEVEL_MINIMAL, subject, message,
				args);
	}

	public void logRowlevel(String subject, String message, Object... args) {
		log.println(logLevel, LogWriter.LOG_LEVEL_ROWLEVEL, subject, message,
				args);
	}

	public void removeAppender(Appender appender) {
		log.removeAppender(appender);
	}

}

package com.panet.imeta.core.logging;

import org.apache.log4j.Appender;

public interface LogWriterInterface {

	public void logMinimal(String subject, String message, Object... args);

	public void logBasic(String subject, String message, Object... args);

	public void logDetailed(String subject, String message, Object... args);

	public void logDebug(String subject, String message, Object... args);

	public void logRowlevel(String subject, String message, Object... args);

	public void logError(String subject, String message, Object... args);

	public void logError(String subject, String message, Throwable e);

	public boolean isBasic();

	public boolean isDetailed();

	public boolean isDebug();

	public boolean isRowLevel();
	
	public void addAppender(Appender appender);
	
	public void removeAppender(Appender appender);
}

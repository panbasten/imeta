package com.plywet.imeta.core.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.MemoryHandler;

/**
 * 
 * @author 潘巍（PeterPan）
 * @since 2011-7-12 下午03:17:26
 * 
 */
public class ErrorTriggeredFileHandler extends Handler {

	private static final int DEFAULT_SIZE = 500;
	private static final Level DEFAULT_PUSH_LEVEL = Level.SEVERE;
	private static final String DEFAULT_PATTERN = "%h/dsp%u.log";

	DecoratedMemoryHandler memoryHandler = null;
	FileHandler fileHandler = null;

	public ErrorTriggeredFileHandler() throws SecurityException, IOException {
		fileHandler = getConfiguredTarget();
		memoryHandler = new DecoratedMemoryHandler(fileHandler,
				getConfiguredSize(), getConfiguredPushLevel());
	}

	private static Level getConfiguredPushLevel() {
		LogManager manager = LogManager.getLogManager();
		String pushLevelText = manager
				.getProperty(ErrorTriggeredFileHandler.class.getName()
						+ ".push");
		if (pushLevelText == null) {
			return DEFAULT_PUSH_LEVEL;
		}
		try {
			return Level.parse(pushLevelText.trim());
		} catch (Exception ex) {
			return DEFAULT_PUSH_LEVEL;
		}
	}

	protected static int getConfiguredSize() {
		LogManager manager = LogManager.getLogManager();
		String sizeText = manager.getProperty(ErrorTriggeredFileHandler.class
				.getName()
				+ ".size");
		if (sizeText == null) {
			return DEFAULT_SIZE;
		}
		try {
			return Integer.parseInt(sizeText.trim());
		} catch (Exception ex) {
			return DEFAULT_SIZE;
		}
	}

	protected static FileHandler getConfiguredTarget()
			throws SecurityException, IOException {
		LogManager manager = LogManager.getLogManager();
		String pattern = manager.getProperty(ErrorTriggeredFileHandler.class
				.getName()
				+ ".pattern");
		if (pattern == null) {
			pattern = DEFAULT_PATTERN;
		}
		return new FileHandler(pattern);
	}

	public class DecoratedMemoryHandler extends MemoryHandler {
		public DecoratedMemoryHandler(FileHandler target, int size,
				Level pushLevel) {
			super(target, size, pushLevel);
		}

		public void push() {
			fileHandler.setFormatter(new LogFormatter());
			super.push();
			LogRecord emptyLine = new LogRecord(Level.INFO, "");
			emptyLine.setLoggerName("");
			fileHandler.publish(emptyLine);
			LogRecord line = new LogRecord(
					Level.INFO,
					"---- END OF TRIGGERED PUSH ---------------------------------------------------");
			line.setLoggerName("");
			fileHandler.publish(line);
			fileHandler.publish(emptyLine);
			fileHandler.publish(emptyLine);
		}
	}

	public void close() throws SecurityException {
		memoryHandler.close();
	}

	public void flush() {
		memoryHandler.flush();
	}

	public void publish(LogRecord record) {
		memoryHandler.publish(record);
	}
}

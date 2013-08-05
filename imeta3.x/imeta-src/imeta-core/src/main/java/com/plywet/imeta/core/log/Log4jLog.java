package com.plywet.imeta.core.log;

import org.apache.log4j.Level;

/**
 * 
 * @author 潘巍（PeterPan）
 * @since 2011-7-12 下午03:19:05
 *
 */
public class Log4jLog extends Log {

  org.apache.log4j.Logger log;
  
  public Log4jLog(org.apache.log4j.Logger log) {
    this.log = log;
  }

  public void error(String msg) {
    log.error(msg);
  }

  public void error(String msg, Throwable exception) {
    log.error(msg, exception);
  }

  public boolean isInfoEnabled() {
    return log.isInfoEnabled();
  }

  public void info(String msg) {
    log.info(msg);
  }

  public void info(String msg, Throwable exception) {
    log.info(msg, exception);
  }

  public boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  public void debug(String msg) {
    log.debug(msg);
  }

  public void debug(String msg, Throwable exception) {
    log.debug(msg, exception);
  }

  public boolean isTraceEnabled() {
    return log.isTraceEnabled();
  }

  public void trace(String msg) {
    log.trace(msg);
  }

  public void trace(String msg, Throwable exception) {
    log.trace(msg, exception);
  }
  
  public boolean isWarnEnabled() {
    return log.isEnabledFor(Level.WARN);
  }
  
  public void warn(String msg) {
    log.warn(msg);
  }
  
  public void warn(String msg, Throwable exception) {
    log.warn(msg, exception);
  }
  
}

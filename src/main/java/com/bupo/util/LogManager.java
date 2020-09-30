package com.bupo.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LocationInfo;

public class LogManager {
	private static Logger logger;

	public static void initLogger() {

		try {
			PropertyConfigurator.configure("/log4j.properties");
			System.out.println("LogManager:Logging successfully configured");
		} catch (Exception e) {
			System.err.println("Could not find log4j.properties");
		}

	}

	private LogManager(Class loggingClass) {
		logger = Logger.getLogger(loggingClass);
	}

	public static LogManager getLogger(Class loggingClass) {
		return new LogManager(loggingClass);
	}

	public String getName() {
		return logger.getName();
	}

	private String getLocationInfo() {
		LocationInfo locInfo = new LocationInfo(new Throwable(), this.getName());
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(locInfo.getFileName() + ":");
		buffer.append(locInfo.getLineNumber() + "::");
		buffer.append(locInfo.getMethodName() + "()");
		buffer.append("] ");
		return buffer.toString();
	}

	public void fatal(Object argMessage) {
		logger.fatal(getLocationInfo() + argMessage);
	}

	public void fatal(Object argMessage, Throwable argThrowable) {
		logger.fatal(getLocationInfo() + argMessage, argThrowable);
	}

	public void error(Object argMessage) {
		this.error(argMessage, null);
	}

	public void error(Object argMessage, Throwable argThrowable) {
		logger.error(getLocationInfo() + argMessage, argThrowable);
	}

	public void error(Throwable argThrowable) {
		StringBuffer exceptionMessage = new StringBuffer("Caught Exception:Message [ ");
		exceptionMessage.append(argThrowable.getMessage());
		exceptionMessage.append(" ]");
		this.error(exceptionMessage.toString(), argThrowable);
	}

	public void debug(Object argMessage) {
		logger.debug(getLocationInfo() + argMessage);
	}

	public void debug(Object argMessage, Throwable argThrowable) {
		logger.debug(getLocationInfo() + argMessage, argThrowable);
	}

	public void debug(Throwable argThrowable) {
		StringBuffer exceptionMessage = new StringBuffer("Caught Exception:Message [ ");
		exceptionMessage.append(argThrowable.getMessage());
		exceptionMessage.append(" ]");
		logger.debug(exceptionMessage.toString(), argThrowable);
	}

	public void info(Object argMessage) {
		logger.info(getLocationInfo() + argMessage);
	}

	public void info(Object argMessage, Throwable argThrowable) {
		logger.info(getLocationInfo() + argMessage, argThrowable);
	}

	public void info(Throwable argThrowable) {
		StringBuffer exceptionMessage = new StringBuffer("Caught Exception:Message [ ");
		exceptionMessage.append(argThrowable.getMessage());
		exceptionMessage.append(" ]");
		logger.info(exceptionMessage.toString(), argThrowable);
	}

	public void warn(Object argMessage) {
		logger.warn(getLocationInfo() + argMessage);
	}

	public void warn(Object argMessage, Throwable argThrowable) {
		logger.warn(getLocationInfo() + argMessage, argThrowable);
	}

	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}
}

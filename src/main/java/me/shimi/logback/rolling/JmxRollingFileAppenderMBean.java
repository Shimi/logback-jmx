package me.shimi.logback.rolling;

public interface JmxRollingFileAppenderMBean {

	public String getName();
	
	public void rollover();
	
	public String getFile();
	
	public boolean isStarted();
}

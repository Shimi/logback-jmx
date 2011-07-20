package me.shimi.logback.rolling;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;

public class JmxRollingFileAppender<E> extends RollingFileAppender<E> implements
		JmxRollingFileAppenderMBean {

	private String fileNamePattern;

	public void start() {
		super.setTriggeringPolicy(new NoTriggeringPolicy<E>());
		
		FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
		rollingPolicy.setParent(this);
		rollingPolicy.setContext(this.getContext());
		rollingPolicy.setFileNamePattern(fileNamePattern);
		rollingPolicy.start();
		super.setRollingPolicy(rollingPolicy);

		try {
			ObjectName objectName = objectName(getName());

			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			mbs.registerMBean(this, objectName);
		} catch (Exception e) {
			addWarn("Failed to register with the MBean Server", e);
		}
		super.start();
	}

	private final ObjectName objectName(final String name) throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(String.format(
				"%s:type=%s,name=%s", JmxRollingFileAppender.class.getPackage().getName(),
				JmxRollingFileAppender.class.getSimpleName(), name));
	}

	public final void setFileNamePattern(final String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public final String getFileNamePattern() {
		return fileNamePattern;
	}

	/**
	 * This method makes sure that we never roll based on log events
	 */
	@Override
	protected void subAppend(final E event) {
		super.subAppend(event);
	}

	@Override
	public void setRollingPolicy(final RollingPolicy policy) {
		throw new UnsupportedOperationException(
				"Setting RollingPolicy is not supported in JmxRollingFileAppender");

	}

	@Override
	public void setTriggeringPolicy(final TriggeringPolicy<E> policy) {
		throw new UnsupportedOperationException(
				"Setting TriggeringPolicy is not supported in JmxRollingFileAppender");
	}
}

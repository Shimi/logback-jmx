package me.shimi.logback.rolling;

import java.io.File;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;

public class NoTriggeringPolicy<E> extends TriggeringPolicyBase<E> {
	public boolean isTriggeringEvent(File activeFile, E event) {
		return false;
	}
}

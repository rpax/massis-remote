package com.massisframework.massis.remote.commands;

import java.util.function.Consumer;

import com.massisframework.massis.remote.common.CommandResponse;
import com.massisframework.massis.remote.common.CommandResponse.ExecutionStatus;

public interface CommandResponseCallback<T>
		extends Consumer<CommandResponse<T>> {

	public default void received(long l, T data) {
		this.accept(new CommandResponse<T>(ExecutionStatus.RECEIVED, l, data));
	}

	public default void received(long l) {
		received(l, null);
	}

	public default void running(long tick, T data) {
		this.accept(
				new CommandResponse<T>(ExecutionStatus.RUNNING, tick, data));
	}

	public default void finished(long tick, T data, String msg) {
		this.accept(
				new CommandResponse<T>(ExecutionStatus.FINISHED, tick, data));
	}

	public default void finished(long tick, T data) {
		this.finished(tick, data, null);
	}

	public default void error(long tick, T data, String errorMsg) {
		this.accept(new CommandResponse<T>(ExecutionStatus.ERROR, tick, data,
				errorMsg));
	}

}

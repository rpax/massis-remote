package com.massisframework.massis.remote.common;

public class CommandResponse<T> {

	public enum ExecutionStatus {
		RECEIVED, RUNNING, FINISHED, ERROR
	}

	private ExecutionStatus status;
	private long tick;
	private T data;
	private String info;

	public CommandResponse(ExecutionStatus status, long tick, T data) {
		this(status, tick, data, null);
	}

	public CommandResponse(ExecutionStatus status, long tick, T data,
			String extraInfo) {
		this.status = status;
		this.tick = tick;
		this.data = data;
		this.info = extraInfo;
	}

	public long getTick() {
		return tick;
	}

	public T getData() {
		return data;
	}

	public ExecutionStatus getStatus() {
		return status;
	}

	public String getInfo() {
		return info;
	}

}

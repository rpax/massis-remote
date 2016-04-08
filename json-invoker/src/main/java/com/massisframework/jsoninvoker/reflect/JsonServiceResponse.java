package com.massisframework.jsoninvoker.reflect;

public class JsonServiceResponse<T> {

	public enum ResponseType {
		RECEIVED, EXECUTING, FINISHED, ERROR
	}

	// private Class<T> clazz;
	private T data;
	private final long responseId;
	private final long responseNumber;
	private final ResponseType responseType;

	public JsonServiceResponse(long responseId, long responseNumber,
			ResponseType responseType, T data) {
		this.data = data;
		this.responseId = responseId;
		this.responseNumber = responseNumber;
		this.responseType = responseType;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public long getResponseId() {
		return responseId;
	}

	public long getResponseNumber() {
		return responseNumber;
	}

	public ResponseType getResponseType() {
		return responseType;
	}

}

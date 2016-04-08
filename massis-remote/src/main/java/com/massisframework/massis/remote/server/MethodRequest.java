package com.massisframework.massis.remote.server;

import com.google.gson.JsonObject;

public class MethodRequest {

	private String method;
	private JsonObject params;

	public MethodRequest(String method, JsonObject params) {
		this.method = method;
		this.params = params;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public JsonObject getParams() {
		return params;
	}

	public void setParams(JsonObject params) {
		this.params = params;
	}

}

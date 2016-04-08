package com.massisframework.jsoninvoker.reflect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonClientMessage<T> {

	protected List<Object> params;
	protected String method;
	private transient JsonServiceResponseHandler<T> handler;
	private transient Class<T> dataClass;

	public JsonClientMessage(String methodName,
			JsonServiceResponseHandler<T> handler, Class<T> dataClass,
			Object... params) {
		this.method = methodName;
		this.handler = handler;
		this.dataClass = dataClass;
		this.params=new ArrayList<>();
		for (Object object : params) {
			this.params.add(object);
		}
	}

	public Class<T> getDataClass() {
		return dataClass;
	}

	public JsonServiceResponseHandler<T> getHandler() {
		return handler;
	}

	public List<Object> getParams() {
		return params;
	}


	public String getMethod() {
		return method;
	}
}

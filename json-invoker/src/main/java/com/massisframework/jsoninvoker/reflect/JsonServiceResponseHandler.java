package com.massisframework.jsoninvoker.reflect;

public interface JsonServiceResponseHandler<T> {

	public void handle(JsonServiceResponse<T> obj);

}

package com.massisframework.jsoninvoker.tests;

import com.massisframework.jsoninvoker.annotations.JsonMethodParam;
import com.massisframework.jsoninvoker.annotations.JsonServiceMethod;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;

public interface IAnotherServiceTest {

	@JsonServiceMethod(name = "anotherMethod", description = "this is a description of the method")
	public void yuhu(
			@JsonMethodParam("paramArr") String[] paramArray,
			@JsonMethodParam("zz") double Zz,
			@JsonMethodParam("u") int u,
			JsonServiceResponseHandler<String> handler);
}

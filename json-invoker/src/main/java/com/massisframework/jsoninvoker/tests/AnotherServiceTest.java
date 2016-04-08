package com.massisframework.jsoninvoker.tests;

import com.massisframework.jsoninvoker.annotations.JsonService;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;

@JsonService(name = "AnotherServiceTest", description = "Another stupid description")
public class AnotherServiceTest implements IAnotherServiceTest {

	@Override
	public void yuhu(String[] paramArray, double Zz, int u,
			JsonServiceResponseHandler<String> handler) {
		
		handler.handle(new JsonServiceResponse<String>(0L, 2L,ResponseType.RECEIVED, "Yuhu"));
	}

}

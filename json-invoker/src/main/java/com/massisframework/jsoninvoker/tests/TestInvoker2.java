package com.massisframework.jsoninvoker.tests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod2;
import com.massisframework.jsoninvoker.services.JsonMethodInvoker;

public class TestInvoker2 {

	/**
	 * @formatter:on
	 * @author rpax
	 *
	 */
	public static void main(String... args) {

//		JsonMethodInvoker invoker = new JsonMethodInvoker();
//		invoker.register("example",
//				(JsonMethod2<String, Integer, String>) (p0, p1, handler) -> {
//					handler.handle(5, 5, ResponseType.FINISHED,
//							"Funciona con " + p0 + " " + p1);
//				});
//
//		JsonArray params = new Gson().toJsonTree(new Object[] { "Hola", 9D })
//				.getAsJsonArray();
//		JsonObject obj = new JsonObject();
//		obj.addProperty("name", "example");
//		obj.add("params", params);
//
//		invoker.invoke(obj.get("name").getAsString(), params, (res) ->
//
//		{
//			System.out.println("Hola: " + res.getData());
//		});

	}

}

package com.massisframework.jsoninvoker.services;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod1;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod2;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod3;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod4;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod5;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JsonMethodInvoker {

	private HashMap<String, MethodInfo> methodMap;
	private Gson gson = new Gson();

	public JsonMethodInvoker() {
		this.methodMap = new HashMap<>();
	}

//	public <JM extends JsonMethod<?>> void register(JM impl) {
//		this.register(impl.getClass().getAnnotation(JsonService.class).name(),
//				impl);
//	}

//	public void register(String name, JsonMethod1<T> impl) {
//		register_int(name, impl);
//	}
//	public void register(String name, JsonMethod2 impl) {
//		register_int(name, impl);
//	}
//	public void register(String name, JsonMethod3 impl) {
//		register_int(name, impl);
//	}
//	public void register(String name, JsonMethod4 impl) {
//		register_int(name, impl);
//	}
	
	public void register(String name, JsonMethod impl) {
		Method method = impl.getClass().getMethods()[0];
		// TODO verificacion
		this.methodMap.put(name,
				new MethodInfo(impl, method.getParameterTypes()));
	}

	public <T2> void invoke(String name, JsonArray params,
			JsonServiceResponseHandler<T2> handler) {

		MethodInfo info = this.methodMap.get(name);
		Object[] args = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			args[i] = gson.fromJson(params.get(i), info.paramsClasses[i]);
		}
		invokeM(info.impl, args, handler);
	}

	private static class MethodInfo {
		private final JsonMethod impl;
		private final Class[] paramsClasses;

		public MethodInfo(JsonMethod impl, Class[] paramsClasses) {
			this.impl = impl;
			this.paramsClasses = paramsClasses;
		}
	}

	private static <R> void invokeM(JsonMethod<R> method, Object[] p,
			JsonServiceResponseHandler<R> handler) {
		/**@formatter:off**/
		switch (p.length) {
			case 1: ((JsonMethod1) method).invoke(p[0], handler);break;
			case 2: ((JsonMethod2) method).invoke(p[0],p[1], handler);break;
			case 3: ((JsonMethod3) method).invoke(p[0],p[1],p[2], handler);break;
			case 4: ((JsonMethod4) method).invoke(p[0],p[1],p[2],p[3], handler);break;
			case 5: ((JsonMethod5) method).invoke(p[0],p[1],p[2],p[3],p[4], handler);break;
			default: throw new UnsupportedOperationException("Parameter length out of range: "+p.length);
		}
		/**@formatter:on**/
	}
}

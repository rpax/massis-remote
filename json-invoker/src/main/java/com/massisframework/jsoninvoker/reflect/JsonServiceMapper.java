package com.massisframework.jsoninvoker.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.massisframework.jsoninvoker.annotations.JsonMethodParam;
import com.massisframework.jsoninvoker.annotations.JsonService;
import com.massisframework.jsoninvoker.annotations.JsonServiceMethod;

public class JsonServiceMapper<IfType, ImplType extends IfType> {
	private Logger logger = Logger.getLogger(JsonServiceMapper.class.getName());
	private JsonService serviceAnnotation;
	private List<Method> methods;
	private Gson gson;
	private ImplType service;

	public JsonServiceMapper(Class<IfType> serviceClass, ImplType service) {

		Objects.requireNonNull(serviceClass);
		Objects.requireNonNull(service);
		
		this.serviceAnnotation = service.getClass()
				.getAnnotation(JsonService.class);
		this.service = service;
		this.gson = new Gson();
		this.methods = Arrays.stream(serviceClass.getMethods())
				.filter(m -> m.isAnnotationPresent(JsonServiceMethod.class))
				.collect(Collectors.toList());

	}

	public String getName() {
		return this.serviceAnnotation.name();
	}

	public String getDescription() {
		return this.serviceAnnotation.description();
	}

	public static Map<String, Class<?>> getMethodParams(Method method) {
		Map<String, Class<?>> paramTypes = new HashMap<>();
		for (Parameter param : method.getParameters()) {
			JsonMethodParam mp = param.getAnnotation(JsonMethodParam.class);
			if (JsonServiceResponseHandler.class.isAssignableFrom(param.getType()))
				continue;
			paramTypes.put(mp.value(), param.getType());
		}
		return paramTypes;
	}

	private static String getMethodName(Method m) {
		return m.getAnnotation(JsonServiceMethod.class).name();
	}

	public Optional<Method> getMethodMatching(String methodName,
			JsonObject jsonParams) {
		Objects.requireNonNull(methodName);
		Objects.requireNonNull(jsonParams);
		/*
		 * must be an object
		 */
		if (!jsonParams.isJsonObject())
			throw new IllegalArgumentException(
					"parameters must be provided as a jsonObject. found "
							+ jsonParams.getClass());
		/*
		 * Filter method matching parameters.
		 */
		return this.methods.stream()
				.filter(m -> getMethodName(m).equals(methodName))
				.filter(m -> matches(getMethodParams(m), jsonParams))
				.findFirst();
	}

	// TODO think again
	private boolean matches(Map<String, Class<?>> methodMap,
			JsonObject jsonParams) {

		for (Entry<String, Class<?>> entry : methodMap.entrySet()) {
			String paramName = entry.getKey();
			Class<?> paramClass = entry.getClass();
			JsonElement member = jsonParams.get(paramName);
			if (paramClass.isArray() && (!member.isJsonArray())) {
				return false;
			}
			if (paramClass.isPrimitive() && (!member.isJsonPrimitive())) {
				return false;
			}

		}
		return true;
	}

	public void invoke(String methodName, JsonObject jsonParams,
			JsonServiceResponseHandler<?> handler) {
		Method method = getMethodMatching(methodName, jsonParams)
				.orElseThrow(() -> new RuntimeException("Method not found"));
		//logger.info(() -> "Invoking method " + method);
		/*
		 * Map method params to json params
		 */
		Parameter[] parameters = method.getParameters();
		Object[] args = new Object[parameters.length];
		// --
		for (int i = 0; i < parameters.length - 1; i++) {
			JsonMethodParam annotation = parameters[i]
					.getAnnotation(JsonMethodParam.class);
			String name = annotation.value();
			args[i] = gson.fromJson(jsonParams.get(name),
					parameters[i].getType());
		}
		// --
		args[parameters.length - 1] = handler;

		try {
			method.invoke(this.service, args);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public JsonServiceInfo getInfo() {
		return new JsonServiceInfo();
	}

	@SuppressWarnings("unused")
	public class JsonMethodInfo {
		private String name;
		private HashMap<String, String> params;

		protected JsonMethodInfo(String name,
				Map<String, Class<?>> p) {
			this.name = name;
			this.params = new HashMap<>();
			p.forEach((k, v) -> this.params.put(k, v.getName()));
		}

	}

	@SuppressWarnings("unused")
	public class JsonServiceInfo {

		private String name = JsonServiceMapper.this.getName();
		private String description = JsonServiceMapper.this.getDescription();
		private List<JsonMethodInfo> methods;

		public JsonServiceInfo() {
			this.methods = JsonServiceMapper.this.methods.stream()
					.map(m -> new JsonMethodInfo(getMethodName(m),
							getMethodParams(m)))
					.collect(Collectors.toList());
		}

	}

}

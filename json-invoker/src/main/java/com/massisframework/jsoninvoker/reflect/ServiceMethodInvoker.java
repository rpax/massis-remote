package com.massisframework.jsoninvoker.reflect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.massisframework.jsoninvoker.annotations.JsonService;
import com.massisframework.jsoninvoker.reflect.JsonServiceMapper.JsonServiceInfo;

public class ServiceMethodInvoker {

	private Map<String, JsonServiceMapper> serviceMap;

	public ServiceMethodInvoker() {
		this.serviceMap = new HashMap<>();
	}

	public <IfType, ImplType extends IfType> void registerService(
			Class<IfType> serviceClass, ImplType service) {

		Objects.requireNonNull(service);
		InvokerUtils.requireImplementsInterface(service.getClass(),
				serviceClass);
		Class<?> clazz = service.getClass();

		/*
		 * Annotation retrieval
		 */
		JsonService annotation = service.getClass()
				.getAnnotation(JsonService.class);
		if (annotation == null) {
			throw new IllegalArgumentException(
					clazz + " does not have an annotation of type "
							+ JsonService.class);
		}
		String serviceName = annotation.name();
		Objects.requireNonNull(service);
		if (this.serviceMap.containsKey(serviceName)) {
			throw new IllegalArgumentException(
					serviceName + " is already registered");
		}
		this.serviceMap.put(serviceName,
				new JsonServiceMapper(serviceClass, service));
	}

	public void invokeMethod(String serviceName, String methodName,
			JsonObject jsonParams, JsonServiceResponseHandler<?> handler) {
		JsonServiceMapper<?,?> service = this.serviceMap.get(serviceName);
		service.invoke(methodName, jsonParams, handler);
	}

	public void printInformation() {
		List<JsonServiceInfo> info = this.serviceMap.values().stream()
				.map(JsonServiceMapper::getInfo)
				.collect(Collectors.toList());

		String infostr = new GsonBuilder().setPrettyPrinting().create()
				.toJson(info).toString();

		System.out.println(infostr);
	}

}

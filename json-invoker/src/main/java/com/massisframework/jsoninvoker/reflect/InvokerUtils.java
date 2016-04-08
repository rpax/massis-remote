package com.massisframework.jsoninvoker.reflect;

import java.util.Objects;

import com.massisframework.jsoninvoker.annotations.JsonService;

final class InvokerUtils {

	private InvokerUtils() {
	};

	public static boolean isInterface(Object obj) {
		Objects.requireNonNull(obj);
		return obj.getClass().isInterface();
	}

	public static boolean isEnum(Object obj) {
		Objects.requireNonNull(obj);
		return obj.getClass().isEnum();
	}

	public static void requireIsInterface(Object obj) {
		if (!isInterface(obj)) {
			throw new IllegalArgumentException("object of class "
					+ obj.getClass() + " it is not an interface");
		}

	}

	public static void requireIsClass(Object obj) {
		if (isInterface(obj) || isEnum(obj)) {
			throw new IllegalArgumentException("object of class "
					+ obj.getClass() + " it is not an interface");
		}

	}

	public static void requireImplementsInterface(
			Class<?> implClass, Class<?> interfaceClass) {

		Objects.requireNonNull(implClass);
		Objects.requireNonNull(interfaceClass);
		if (!interfaceClass.isInterface()) {
			throw new IllegalArgumentException(
					implClass + " must be an interface");
		}
		if (!interfaceClass.isAssignableFrom(implClass)) {
			throw new IllegalArgumentException(
					implClass + " must implement " + interfaceClass);
		}
	}

	public static boolean isJsonService(Class<?> clazz) {
		return clazz.getAnnotation(JsonService.class) != null;
	}

	public static void requireIsJsonService(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		if (!isJsonService(clazz))
			throw new IllegalArgumentException(
					clazz + " does not have an annotation of type "
							+ JsonService.class);
	}
	
	

}

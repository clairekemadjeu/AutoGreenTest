package com.skysoft.app.bdd;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContext {
	public static Map<String, Object> context = new HashMap<>();

	public static void addToContext(final String key, final Object value) {
		context.put(key, value);
	}
	public static Object getFromContext(final String key) {
		return context.get(key);
	}
}

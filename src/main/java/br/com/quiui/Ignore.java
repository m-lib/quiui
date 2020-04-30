package br.com.quiui;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;

public final class Ignore {

	private static boolean primitives;
	private static Map<Class<?>, Collection<String>> map;

	private Ignore() {
		//map = new HashMap<Class<?>, Collection<String>>();
	}

	public static boolean clean() {
		primitives = Boolean.FALSE;
		map = new HashMap();

		return true;
	}

	public static boolean include(Class<?> key, String attribute) throws Exception {
		key.getDeclaredField(attribute);

		if (!map.containsKey(key)) {
			map.put(key, new HashSet<String>());
		}

		Collection<String> attributes = map.get(key);
		return attributes.add(attribute);
	}

	public static boolean contains(Class<?> key, String attribute) {
		if (map.containsKey(key)) {
			return map.get(key).contains(attribute);
		}

		return false;
	}

	public static boolean includePrimitives() {
		return primitives = true;
	}

	public static boolean primitives() {
		return primitives;
	}

}

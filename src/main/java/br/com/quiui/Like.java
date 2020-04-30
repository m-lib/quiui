package br.com.quiui;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;

public class Like {

	private boolean enable;
	private Set<Class<?>> entities;
	private Map<Class<?>, Collection<String>> attributes;

	public Like() {
		entities = new HashSet<Class<?>>();
		attributes = new HashMap<Class<?>, Collection<String>>();
	}

	public boolean put(Class<?> key, String attribute) throws Exception {
		if (isCharacter(key.getDeclaredField(attribute).getType())) {
			if (!attributes.containsKey(key)) {
				attributes.put(key, new HashSet<String>());
			}

			Collection<String> collection = attributes.get(key);
			return collection.add(attribute);
		}

		throw new IllegalArgumentException(attribute + " is not a String");
	}

	public boolean contains(Class<?> key, String attribute) {
		if (attributes.containsKey(key)) {
			return attributes.get(key).contains(attribute);
		}

		return false;
	}

	public boolean contains(Class<?> value) {
		return entities.contains(value);
	}

	public boolean addClass(Class<?> value) {
		return entities.add(value);
	}

	private boolean isCharacter(Class<?> type) {
		return String.class.isAssignableFrom(type);
	}

	public void enable() {
		enable = true;
	}

	public boolean isLikeOn() {
		return enable;
	}

}

/*
 * Copyright 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

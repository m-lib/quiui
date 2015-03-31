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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Ignore {
	
	private boolean primitives;
	private Map<Class<?>, Collection<String>> map;
	
	public Ignore() {
		map = new HashMap<Class<?>, Collection<String>>();
	}
	
	public boolean put(Class<?> key, String attribute) throws Exception {
		key.getDeclaredField(attribute);
		
		if (!map.containsKey(key)) {
			map.put(key, new HashSet<String>());
		}
		
		Collection<String> attributes = map.get(key);
		return attributes.add(attribute);
	}
	
	public boolean contains(Class<?> key, String attribute) {
		if (map.containsKey(key)) {
			return map.get(key).contains(attribute);
		}
		
		return false;
	}
	
	public void ignorePrimitives() {
		primitives = true;
	}
	
	public boolean primitives() {
		return primitives;
	}

}

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

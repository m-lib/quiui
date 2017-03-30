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

public class Internals {
	
	private Map<String, Collection<InnerCriteria<?>>> criterias;
	
	public Internals() {
		criterias = new HashMap<String, Collection<InnerCriteria<?>>>();
	}
	
	public boolean put(String key, InnerCriteria<?> internal) {
		if (!criterias.containsKey(key)) {
			criterias.put(key, new HashSet<InnerCriteria<?>>());
		}
		
		Collection<InnerCriteria<?>> collection = criterias.get(key);
		return collection.add(internal);
	}
	
	public Collection<InnerCriteria<?>> getCriterias(String key) {
		return criterias.get(key);
	}

	public boolean contains(String key) {
		return criterias.containsKey(key);
	}

	public Collection<String> getKeys() {
		return criterias.keySet();
	}

}

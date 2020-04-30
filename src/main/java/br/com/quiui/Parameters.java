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

@SuppressWarnings("all")
public class Parameters {

	private Map<String, Collection<Parameter>> parameters;

	public Parameters() {
		parameters = new HashMap<String, Collection<Parameter>>();
	}

	public boolean put(String key, Parameter parameter) {
		if (!parameters.containsKey(key)) {
			parameters.put(key, new HashSet<Parameter>());
		}

		Collection<Parameter> collection = parameters.get(key);
		return collection.add(parameter);
	}

	public Collection<Parameter> getParameters(String key) {
		return parameters.get(key);
	}

	public Collection<String> getKeys() {
		return parameters.keySet();
	}

	public void remove(String key) {
		parameters.remove(key);
	}

}

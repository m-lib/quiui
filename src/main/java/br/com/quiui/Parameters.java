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

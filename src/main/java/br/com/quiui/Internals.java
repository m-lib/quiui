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

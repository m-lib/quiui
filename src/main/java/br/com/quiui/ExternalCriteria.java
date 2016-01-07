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

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

@SuppressWarnings("all")
public class ExternalCriteria<T> extends Criteria<T> {

	private Collection<Order> ordering;
	private CriteriaQuery query;
	private T example;
	
	public ExternalCriteria(EntityManager manager, CriteriaBuilder builder, CriteriaQuery criteria, Root from) {
		super(manager, builder, criteria, from);
		this.ordering = new ArrayList<Order>();
		this.query = criteria;
	}
	
	public void setExample(T example) { 
		this.example = example;
	}
	
	public void asc(String... attributes) {
		Ordination ordination = Ordination.ASC;
		for (String attribute : attributes) {
			Metamodel model = manager.getMetamodel();
			EntityType<?> entity = from.getModel();
			From<?,?> join = from;

			for (String name : attribute.split("\\.")) {
				Attribute<?,?> attr = entity.getAttribute(name);
				if (attr.isAssociation()) {
					entity = model.entity(attr.getJavaType());
					join = join.join(name);
				}
				attribute = name;
			}
			ordering.add(ordination.order(builder, join.get(attribute)));
		}
	}
	
	public void desc(String... attributes) {
		Ordination ordination = Ordination.DESC;
		for (String attribute : attributes) {
			Metamodel model = manager.getMetamodel();
			EntityType<?> entity = from.getModel();
			From<?,?> join = from;

			for (String name : attribute.split("\\.")) {
				Attribute<?,?> attr = entity.getAttribute(name);
				if (attr.isAssociation()) {
					entity = model.entity(attr.getJavaType());
					join = join.join(name);
				}
				attribute = name;
			}
			ordering.add(ordination.order(builder, join.get(attribute)));
		}
	}
	
	public CriteriaQuery getQuery() throws Exception {
		preparePredicates(example);
		prepareQuery();
		
		if (ordering.isEmpty() == false) {
			query.orderBy(ordering.toArray(new Order[0]));
		}
		
		return query;
	}

}

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

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

@SuppressWarnings("all")
public abstract class Criteria<T> {
	
	Collection<Predicate> predicates;
	Map<String, Join> joins;
	EntityManager manager;
	
	Parameters parameters = new Parameters();
	Internals internals = new Internals();
	Ignore ignore = new Ignore();
	Like like = new Like();
	
	CriteriaBuilder builder;
	AbstractQuery query;
	Root from;
	
	public Criteria(EntityManager manager, CriteriaBuilder builder, AbstractQuery query, Root from) {
		this.predicates = new HashSet<Predicate>();
		this.joins = new HashMap<String, Join>();
		this.manager = manager;
		this.builder = builder;
		this.query = query;
		this.from = from;
	}
	
	public void preparePredicates(T entity) throws Exception {
		if (entity != null) {
			Class<?> type = entity.getClass();
			preparePredicates(entity, type, from);
		}
	}
	
	private void preparePredicates(Object entity, Class type, From from) throws Exception {
		EntityType<?> metamodel = manager.getMetamodel().entity(type);
		for (Attribute attribute : metamodel.getAttributes()) {
			
			PropertyDescriptor descriptor = new PropertyDescriptor(attribute.getName(), type);
			Object attributeValue = descriptor.getReadMethod().invoke(entity);
			
			if (!isEmpty(attributeValue)) {
				if (attribute.isAssociation()) {
					if (attribute.isCollection()) {
						Collection collection = Collection.class.cast(attributeValue);
						
						if (!collection.isEmpty()) {
							Join path = from.join(attribute.getName());
							joins.put(attribute.getName(), path);
						}
						
						for (Object value : collection) {
							Subquery subquery = query.subquery(value.getClass());
							Root root = subquery.from(value.getClass());
							subquery.select(root);
							
							InnerCriteria internal = new InnerCriteria(manager, builder, subquery, root);
							internal.setLike(like);
							
							internal.preparePredicates(value);
							internals.put(attribute.getName(), internal);
	                    }
					} else {
						Join join = from.join(attribute.getName());
						joins.put(attribute.getName(), join);
						preparePredicates(attributeValue, attributeValue.getClass(), join);
					}
				} else {
					if (ignore.primitives() && attribute.getJavaType().isPrimitive()) {
						continue;
					} else if (ignore.contains(type, attribute.getName())) {
						continue;
					}
					
					if (isLike(attribute)) {
						predicates.add(builder.like(from.get(attribute.getName()), likeConcatenation(attributeValue.toString())));
					} else {
						predicates.add(builder.equal(from.get(attribute.getName()), attributeValue));
					}
				}
			}
		}
	}
	
	private boolean isLike(Attribute attribute) {
		Class declaring = attribute.getDeclaringType().getJavaType();
		
		if (isCharacter(attribute)) {
			return like.isLikeOn() || like.contains(declaring) || like.contains(declaring, attribute.getName());
		}
		
		return false;
	}
	
	private boolean isCharacter(Attribute attribute) {
		return String.class.isAssignableFrom(attribute.getJavaType());
	}
	
	private String likeConcatenation(String value) {
		return "%" + value.trim().replaceAll(" +", "%") + "%";
	}
	
	private boolean isEmpty(Object value) {
		if (value == null || value.toString().trim().replaceAll(" +", "").isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public void enableLike() {
		like.enable();
	}
	
	public void enableLikeFor(Class<?> entity) {
		like.addClass(entity);
	}
	
	public void enableLikeForAttribute(Class<?> key, String attribute) throws Exception {
		like.put(key, attribute);
	}
	
	public boolean ignoreAttribute(Class<?> key, String attribute) throws Exception {
		return ignore.put(key, attribute);
	}
	
	public void ignorePrimitives() {
		ignore.ignorePrimitives();
	}
	
	public Predicate[] getPredicates() {
		Integer size = predicates.size();
		return predicates.toArray(new Predicate[size]);
	}
	
	public void execute(Parameter parameter) {
		Metamodel model = manager.getMetamodel();
		EntityType<?> entity = from.getModel();
		
		From<?,?> join = from;
		for (String name : parameter.getChain()) {
			Attribute<?,?> attribute = entity.getAttribute(name);
			if (attribute.isAssociation()) {
				if (!containsPath(name)) {
					joins.put(name, join.join(name));
				}
				
				join = joins.get(name);
				
				if (attribute.isCollection()) {
					String path = parameter.getPath().substring(parameter.getPath().indexOf(name) + name.length() + 1);
					Parameter param = new Parameter(path, parameter.getValue());
					param.setOperation(parameter.getOperation());
					parameters.put(name, param);
					break;
				} else if (parameter.next(name)) {
					entity = model.entity(attribute.getJavaType());
				} else {
					predicates.add(parameter.getOperation().execute(join, parameter.getValue()));
				}
			} else {
				Path<?> path = join.get(name);
				predicates.add(parameter.getOperation().execute(path, parameter.getValue()));
			}
		}
	}
	
	public void prepareQuery() {
		Collection<Predicate> or = new HashSet<Predicate>();
		
		for (String key : parameters.getKeys()) {
			for (Parameter parameter : parameters.getParameters(key)) {
				if (internals.contains(key)) {
					for (InnerCriteria<?> criteria : internals.getCriterias(key)) {
						criteria.execute(parameter);
					}
				} else {
					Path path = joins.get(key);
					Subquery subquery = query.subquery(path.getJavaType());
					Root root = subquery.from(path.getJavaType());
					subquery.select(root);
					
					InnerCriteria internal = new InnerCriteria(manager, builder, subquery, root);
					internal.execute(parameter);
					predicates.add(path.in(internal.getQuery()));
				}
			}
		}
		
		for (String key : internals.getKeys()) {
			for (InnerCriteria<?> criteria : internals.getCriterias(key)) {
				or.add(joins.get(key).in(criteria.getQuery()));
			}
			
			predicates.add(builder.or(or.toArray(new Predicate[or.size()])));
		}
		
		query.where(builder.and(getPredicates()));
	}
	
	private boolean containsPath(String key) {
		return joins.containsKey(key);
	}
	
	public void equal(String attribute, Object value) {
		Operation operation = OperationFactory.equal(builder);
		Parameter parameter = new Parameter(attribute, value);
		parameter.setOperation(operation);
		execute(parameter);
	}
	
	public void notEqual(String attribute, Object value) {
		Operation operation = OperationFactory.notEqual(builder);
		Parameter parameter = new Parameter(attribute, value);
		parameter.setOperation(operation);
		execute(parameter);
	}
	
	public void greaterThan(String attribute, Comparable<?> value) {
		Parameter<Comparable> parameter = new Parameter<Comparable>(attribute, value);
		Operation<Comparable> operation = OperationFactory.greaterThan(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}
	
	public void lessThan(String attribute, Comparable<?> value) {
		Parameter<Comparable> parameter = new Parameter<Comparable>(attribute, value);
		Operation<Comparable> operation = OperationFactory.lessThan(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}
	
	public void lessOrEqual(String attribute, Comparable<?> value) {
		Parameter<Comparable> parameter = new Parameter<Comparable>(attribute, value);
		Operation<Comparable> operation = OperationFactory.lessOrEqual(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}
	
	public void greaterOrEqual(String attribute, Comparable<?> value) {
		Parameter<Comparable> parameter = new Parameter<Comparable>(attribute, value);
		Operation<Comparable> operation = OperationFactory.greaterOrEqual(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}
	
	public void like(String attribute, String pattern) {
		Parameter<String> parameter = new Parameter<String>(attribute, likeConcatenation(pattern));
		Operation<String> operation = OperationFactory.like(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}
	
	public void notLike(String attribute, String pattern) {
		Parameter<String> parameter = new Parameter<String>(attribute, likeConcatenation(pattern));
		Operation<String> operation = OperationFactory.notLike(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}

}

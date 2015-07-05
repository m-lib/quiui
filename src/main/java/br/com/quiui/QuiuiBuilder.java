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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@SuppressWarnings("all")
public class QuiuiBuilder<T> {
	
	private ExternalCriteria<T> select;
	private ExternalCriteria<T> count;
	private EntityManager manager;
	private Class<T> type;
	
	private int first;
	private int max = Integer.MAX_VALUE;
	
	public QuiuiBuilder(EntityManager manager, Class<T> type) {
		this.select = CriteriaFactory.createCriteria(manager, type);
		this.count = CriteriaFactory.createCounter(manager, type);
		
		this.manager = manager;
		this.type = type;
	}
	
	public void create(T entity) throws Exception {
		select.preparePredicates(entity, type);
		count.preparePredicates(entity, type);
	}
	
	public Collection<T> select() {
		TypedQuery<T> query = manager.createQuery(select.getQuery());
		
		query.setFirstResult(first);
		query.setMaxResults(max);
		
		return query.getResultList();
	}
	
	public Long count() {
		TypedQuery<Long> query = manager.createQuery(count.getQuery());
		return query.getSingleResult();
	}
	
	public T unique() {
		TypedQuery<T> query = manager.createQuery(select.getQuery());
		return query.getSingleResult();
	}
	
	public QuiuiBuilder<T> setMax(int max) {
		this.max = max;
		return this;
	}

	public QuiuiBuilder<T> setFirst(int first) {
		this.first = first;
		return this;
	}

	public QuiuiBuilder<T> ignoreAttribute(Class<?> key, String attribute) throws Exception {
		select.ignoreAttribute(key, attribute);
		count.ignoreAttribute(key, attribute);
		return this;
	}
	
	public QuiuiBuilder<T> ignorePrimitives() {
		select.ignorePrimitives();
		count.ignorePrimitives();
		return this;
	}
	
	public QuiuiBuilder<T> enableLike() {
		select.enableLike();
		count.enableLike();
		return this;
	}
	
	public QuiuiBuilder<T> enableLikeFor(Class<?> entity) {
		select.enableLikeFor(entity);
		count.enableLikeFor(entity);
		return this;
	}
	
	public QuiuiBuilder<T> enableLikeForAttribute(Class<?> key, String attribute) throws Exception {
		select.enableLikeForAttribute(key, attribute);
		count.enableLikeForAttribute(key, attribute);
		return this;
	}
	
	public QuiuiBuilder<T> equal(String attribute, Object value) {
		select.equal(attribute, value);
		count.equal(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> notEqual(String attribute, Object value) {
		select.notEqual(attribute, value);
		count.notEqual(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> like(String attribute, String pattern) {
		select.like(attribute, pattern);
		count.like(attribute, pattern);
		return this;
	}

	public QuiuiBuilder<T> notLike(String attribute, String pattern) {
		select.notLike(attribute, pattern);
		count.notLike(attribute, pattern);
		return this;
	}

	public QuiuiBuilder<T> lessThan(String attribute, Comparable<?> value) {
		select.lessThan(attribute, value);
		count.lessThan(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> greaterThan(String attribute, Comparable<?> value) {
		select.greaterThan(attribute, value);
		count.greaterThan(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> lessOrEqual(String attribute, Comparable<?> value) {
		select.lessOrEqual(attribute, value);
		count.lessOrEqual(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> greaterOrEqual(String attribute, Comparable<?> value) {
		select.greaterOrEqual(attribute, value);
		count.greaterOrEqual(attribute, value);
		return this;
	}
	
	public QuiuiBuilder<T> asc(String... attributes) {
		select.asc(attributes);
		return this;
	}
	
	public QuiuiBuilder<T> desc(String... attributes) {
		select.desc(attributes);
		return this;
	}

}

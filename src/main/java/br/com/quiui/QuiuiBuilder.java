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

import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;

@SuppressWarnings("all")
public class QuiuiBuilder<T> {

	private ExternalCriteria<T> selection;
	private ExternalCriteria<T> counting;
	private EntityManager manager;
	private Class<T> type;

	private int first;
	private int max = Integer.MAX_VALUE;

	public QuiuiBuilder(EntityManager manager, Class<T> type) {
		this.selection = CriteriaFactory.createCriteria(manager, type);
		this.counting = CriteriaFactory.createCounter(manager, type);

		this.manager = manager;
		this.type = type;

		Ignore.clean();
	}

	public QuiuiBuilder<T> create(T entity) throws Exception {
		selection.setExample(entity);
		counting.setExample(entity);
		return this;
	}

	public Collection<T> select() throws Exception {
		TypedQuery<T> query = manager.createQuery(selection.getQuery());

		query.setFirstResult(first);
		query.setMaxResults(max);

		return query.getResultList();
	}

	public Long count() throws Exception {
		TypedQuery<Long> query = manager.createQuery(counting.getQuery());
		return query.getSingleResult();
	}

	public T unique() throws Exception {
		TypedQuery<T> query = manager.createQuery(selection.getQuery());
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
		selection.ignoreAttribute(key, attribute);
		counting.ignoreAttribute(key, attribute);
		return this;
	}

	public QuiuiBuilder<T> ignorePrimitives() {
		selection.ignorePrimitives();
		counting.ignorePrimitives();
		return this;
	}

	public QuiuiBuilder<T> enableLike() {
		selection.enableLike();
		counting.enableLike();
		return this;
	}

	public QuiuiBuilder<T> enableLikeFor(Class<?> entity) {
		selection.enableLikeFor(entity);
		counting.enableLikeFor(entity);
		return this;
	}

	public QuiuiBuilder<T> enableLikeForAttribute(Class<?> key, String attribute) throws Exception {
		selection.enableLikeForAttribute(key, attribute);
		counting.enableLikeForAttribute(key, attribute);
		return this;
	}

	public QuiuiBuilder<T> equal(String attribute, Object value) {
		selection.equal(attribute, value);
		counting.equal(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> notEqual(String attribute, Object value) {
		selection.notEqual(attribute, value);
		counting.notEqual(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> like(String attribute, String pattern) {
		selection.like(attribute, pattern);
		counting.like(attribute, pattern);
		return this;
	}

	public QuiuiBuilder<T> notLike(String attribute, String pattern) {
		selection.notLike(attribute, pattern);
		counting.notLike(attribute, pattern);
		return this;
	}

	public QuiuiBuilder<T> lessThan(String attribute, Comparable<?> value) {
		selection.lessThan(attribute, value);
		counting.lessThan(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> greaterThan(String attribute, Comparable<?> value) {
		selection.greaterThan(attribute, value);
		counting.greaterThan(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> lessOrEqual(String attribute, Comparable<?> value) {
		selection.lessOrEqual(attribute, value);
		counting.lessOrEqual(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> greaterOrEqual(String attribute, Comparable<?> value) {
		selection.greaterOrEqual(attribute, value);
		counting.greaterOrEqual(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> asc(String... attributes) {
		selection.asc(attributes);
		return this;
	}

	public QuiuiBuilder<T> desc(String... attributes) {
		selection.desc(attributes);
		return this;
	}

	public QuiuiBuilder<T> isMember(String attribute, Collection value) {
		selection.isMember(attribute, value);
		counting.isMember(attribute, value);
		return this;
	}

	public QuiuiBuilder<T> isMemberOrEmpty(String attribute, Collection value) {
		selection.isMemberOrEmpty(attribute, value);
		counting.isMemberOrEmpty(attribute, value);
		return this;
	}

}

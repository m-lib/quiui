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

import javax.persistence.EntityManager;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;

public class CriteriaFactory {

	public static <T> ExternalCriteria<T> createCriteria(EntityManager manager, Class<T> type) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(type);

		Root<T> from = query.from(type);
		query.select(from).distinct(true);
		return new ExternalCriteria<T>(manager, builder, query, from);
	}

	public static <T> ExternalCriteria<T> createCounter(EntityManager manager, Class<T> type) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);

		Root<T> from = query.from(type);
		query.select(builder.count(from)).distinct(true);
		return new ExternalCriteria<T>(manager, builder, query, from);
	}

}

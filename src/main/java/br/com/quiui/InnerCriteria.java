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
import javax.persistence.criteria.Subquery;
import javax.persistence.criteria.CriteriaBuilder;

@SuppressWarnings("all")
public class InnerCriteria<T> extends Criteria<T> {
	
	private Subquery query;
	
	public InnerCriteria(EntityManager manager, CriteriaBuilder builder, Subquery query, Root from) {
		super(manager, builder, query, from);
		this.query = query;
	}
	
	public Subquery getQuery() {
		prepareQuery();
		return query;
	}
	
	public void setLike(Like like) {
		this.like = like;
	}

}

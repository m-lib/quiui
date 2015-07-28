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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

@SuppressWarnings("all")
public class OperationFactory {
	
	public static Operation equal(final CriteriaBuilder builder) {
		return new Operation() {
			public Predicate execute(Path path, Object value) {
				if (value == null) {
					return builder.isNull(path);
				} else {
					return builder.equal(path, value);
				}
			}
		};
	}
	
	public static Operation notEqual(final CriteriaBuilder builder) {
		return new Operation() {
			public Predicate execute(Path path, Object value) {
				if (value == null) {
					return builder.isNotNull(path);
				} else {
					return builder.notEqual(path, value);
				}
			}
		};
	}
	
	public static Operation greaterThan(final CriteriaBuilder builder) {
		return new Operation<Comparable>() {
			public Predicate execute(Path path, Comparable value) {
				return builder.greaterThan(path, value);
			}
		};
	}
	
	public static Operation lessThan(final CriteriaBuilder builder) {
		return new Operation<Comparable>() {
			public Predicate execute(Path path, Comparable value) {
				return builder.lessThan(path, value);
			}
		};
	}
	
	public static Operation lessOrEqual(final CriteriaBuilder builder) {
		return new Operation<Comparable>() {
			public Predicate execute(Path path, Comparable value) {
				return builder.lessThanOrEqualTo(path, value);
			}
		};
	}
	
	public static Operation greaterOrEqual(final CriteriaBuilder builder) {
		return new Operation<Comparable>() {
			public Predicate execute(Path path, Comparable value) {
				return builder.greaterThanOrEqualTo(path, value);
			}
		};
	}
	
	public static Operation like(final CriteriaBuilder builder) {
		return new Operation<String>() {
			public Predicate execute(Path path, String value) {
				return builder.like(path, value);
			}
		};
	}
	
	public static Operation notLike(final CriteriaBuilder builder) {
		return new Operation<String>() {
			public Predicate execute(Path path, String value) {
				return builder.notLike(path, value);
			}
		};
	}

}

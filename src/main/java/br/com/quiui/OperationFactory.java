package br.com.quiui;

import java.util.Collection;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;

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

			public OperationType getType() {
				return OperationType.EQUAL;
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

			public OperationType getType() {
				return OperationType.NOT_EQUAL;
			}
		};
	}

	public static Operation greaterThan(final CriteriaBuilder builder) {
		return new Operation<Comparable>() {
			public Predicate execute(Path path, Comparable value) {
				return builder.greaterThan(path, value);
			}

			public OperationType getType() {
				return OperationType.GREATER_THAN;
			}
		};
	}

	public static Operation lessThan(final CriteriaBuilder builder) {
		return new Operation<Comparable>() {
			public Predicate execute(Path path, Comparable value) {
				return builder.lessThan(path, value);
			}

			public OperationType getType() {
				return OperationType.LESS_THAN;
			}
		};
	}

	public static Operation lessOrEqual(final CriteriaBuilder builder) {
		return new Operation<Comparable>() {
			public Predicate execute(Path path, Comparable value) {
				return builder.lessThanOrEqualTo(path, value);
			}

			public OperationType getType() {
				return OperationType.LESS_OR_EQUAL;
			}
		};
	}

	public static Operation greaterOrEqual(final CriteriaBuilder builder) {
		return new Operation<Comparable>() {
			public Predicate execute(Path path, Comparable value) {
				return builder.greaterThanOrEqualTo(path, value);
			}

			public OperationType getType() {
				return OperationType.GREATER_OR_EQUAL;
			}
		};
	}

	public static Operation like(final CriteriaBuilder builder) {
		return new Operation<String>() {
			public Predicate execute(Path path, String value) {
				return builder.like(path, value);
			}

			public OperationType getType() {
				return OperationType.LIKE;
			}
		};
	}

	public static Operation notLike(final CriteriaBuilder builder) {
		return new Operation<String>() {
			public Predicate execute(Path path, String value) {
				return builder.notLike(path, value);
			}

			public OperationType getType() {
				return OperationType.NOT_LIKE;
			}
		};
	}

	public static Operation isMember(final CriteriaBuilder builder) {
		return new Operation<Collection>() {
			public Predicate execute(Path path, Collection value) {
				return path.in(value);
			}

			public OperationType getType() {
				return OperationType.IS_MEMBER;
			}
		};
	}

	public static Operation isMemberOrEmpty(final CriteriaBuilder builder) {
		return new Operation<Collection>() {
			public Predicate execute(Path path, Collection value) {
				return builder.or(path.in(value), path.isNull());
			}

			public OperationType getType() {
				return OperationType.IS_MEMBER_OR_EMPTY;
			}
		};
	}

}

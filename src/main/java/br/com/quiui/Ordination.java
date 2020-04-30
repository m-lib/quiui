package br.com.quiui;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.CriteriaBuilder;

public enum Ordination {

	ASC(new Orderable() {
		@Override
		public Order order(CriteriaBuilder builder, Expression<?> expression) {
			return builder.asc(expression);
		}
	}),

	DESC(new Orderable() {
		@Override
		public Order order(CriteriaBuilder builder, Expression<?> expression) {
			return builder.desc(expression);
		}
	});

	private Orderable ordering;

	private Ordination(Orderable ordering) {
		this.ordering = ordering;
	}

	public Order order(CriteriaBuilder builder, Expression<?> expression) {
		return ordering.order(builder, expression);
	}

}

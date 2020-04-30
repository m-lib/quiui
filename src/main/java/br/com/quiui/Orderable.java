package br.com.quiui;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.CriteriaBuilder;

public interface Orderable {

	public Order order(CriteriaBuilder builder, Expression<?> expression);

}

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
		query.select(from);//.distinct(true);
		return new ExternalCriteria<T>(manager, builder, query, from);
	}

	public static <T> ExternalCriteria<T> createCounter(EntityManager manager, Class<T> type) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);

		Root<T> from = query.from(type);
		query.select(builder.count(from));//.distinct(true);
		return new ExternalCriteria<T>(manager, builder, query, from);
	}

}

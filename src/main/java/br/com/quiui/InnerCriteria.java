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

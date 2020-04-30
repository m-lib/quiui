package br.com.quiui;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;

import java.beans.PropertyDescriptor;

import javax.persistence.EntityManager;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Subquery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

@SuppressWarnings("all")
public abstract class Criteria<T> {

	Collection<Predicate> predicates;
	Map<String, Join> joins;
	EntityManager manager;

	Parameters parameters = new Parameters();
	Internals internals = new Internals();
	Like like = new Like();

	CriteriaBuilder builder;
	AbstractQuery query;
	Root from;

	public Criteria(EntityManager manager, CriteriaBuilder builder, AbstractQuery query, Root from) {
		this.predicates = new HashSet<Predicate>();
		this.joins = new HashMap<String, Join>();
		this.manager = manager;
		this.builder = builder;
		this.query = query;
		this.from = from;
	}

	public void preparePredicates(T entity) throws Exception {
		if (entity != null) {
			Class<?> type = entity.getClass();
			preparePredicates(entity, type, from);
		}
	}

	private void preparePredicates(Object entity, Class type, From from) throws Exception {
		EntityType<?> metamodel = manager.getMetamodel().entity(type);
		for (Attribute attribute : metamodel.getAttributes()) {

			PropertyDescriptor descriptor = new PropertyDescriptor(attribute.getName(), type);
			Object value = descriptor.getReadMethod().invoke(entity);

			if (Ignore.contains(type, attribute.getName()) || (Ignore.primitives() && attribute.getJavaType().isPrimitive())) {
				continue;
			}

			if (!isEmpty(value)) {
				if (attribute.isAssociation()) {
					if (attribute.isCollection()) {
						Collection collection = (Collection) value;

						if (!collection.isEmpty()) {
							Join path = from.join(attribute.getName());
							joins.put(attribute.getName(), path);

							for (Object item : collection) {
								Subquery subquery = query.subquery(item.getClass());
								Root root = subquery.from(item.getClass());
								subquery.select(root);//.distinct(true);

								InnerCriteria internal = new InnerCriteria(manager, builder, subquery, root);
								internal.setLike(like);

								internal.preparePredicates(item);
								internals.put(attribute.getName(), internal);
							}
						}
					} else {
						Join join = from.join(attribute.getName());

						joins.put(attribute.getName(), join);
						preparePredicates(value, value.getClass(), join);
					}
				} else {
					if (isLike(attribute)) {
						predicates.add(builder.like(from.get(attribute.getName()), likeConcatenation(value.toString())));
					} else {
						predicates.add(builder.equal(from.get(attribute.getName()), value));
					}
				}
			}
		}
	}

	private boolean isLike(Attribute attribute) {
		Class declaring = attribute.getDeclaringType().getJavaType();

		if (isCharacter(attribute)) {
			return like.isLikeOn() || like.contains(declaring) || like.contains(declaring, attribute.getName());
		}

		return false;
	}

	private boolean isCharacter(Attribute attribute) {
		return String.class.isAssignableFrom(attribute.getJavaType());
	}

	private String likeConcatenation(String value) {
		return "%" + value.trim().replaceAll(" +", "%") + "%";
	}

	private boolean isEmpty(Object value) {
		if (value == null || value.toString().trim().replaceAll(" +", "").isEmpty()) {
			return true;
		}

		return false;
	}

	public void enableLike() {
		like.enable();
	}

	public void enableLikeFor(Class<?> entity) {
		like.addClass(entity);
	}

	public void enableLikeForAttribute(Class<?> key, String attribute) throws Exception {
		like.put(key, attribute);
	}

	public boolean ignoreAttribute(Class<?> key, String attribute) throws Exception {
		return Ignore.include(key, attribute);
	}

	public void ignorePrimitives() {
		Ignore.includePrimitives();
	}

	public Predicate[] getPredicates() {
		Integer size = predicates.size();
		return predicates.toArray(new Predicate[size]);
	}

	public void execute(Parameter parameter) {
		Metamodel model = manager.getMetamodel();
		EntityType<?> entity = from.getModel();
		From<?,?> join = from;

		for (String name : parameter.getChain()) {
			Attribute<?,?> attribute = entity.getAttribute(name);
			if (attribute.isAssociation()) {
				if (attribute.isCollection() && parameter.isMemberOperation()) {
					predicates.add(parameter.getOperation().execute(join.join(name, JoinType.LEFT), parameter.getValue()));
					break;
				} else {
					if (!containsPath(name)) {
						joins.put(name, join.join(name));
					}

					join = joins.get(name);

					if (attribute.isCollection()) {
						String path = parameter.getPath().substring(parameter.getPath().indexOf(name) + name.length() + 1);
						Parameter param = new Parameter(path, parameter.getValue());
						param.setOperation(parameter.getOperation());
						parameters.put(name, param);
						break;
					} else if (parameter.next(name)) {
						entity = model.entity(attribute.getJavaType());
					} else {
						predicates.add(parameter.getOperation().execute(join, parameter.getValue()));
					}
				}
			} else {
				predicates.add(parameter.getOperation().execute(join.get(name), parameter.getValue()));
			}
		}
	}

	public void prepareQuery() {
		Collection<Predicate> or = new HashSet<Predicate>();

		for (String key : parameters.getKeys()) {
			for (Parameter parameter : parameters.getParameters(key)) {
				if (internals.contains(key)) {
					for (InnerCriteria<?> criteria : internals.getCriterias(key)) {
						criteria.execute(parameter);
					}
				} else if (!parameter.isMemberOperation()) {
					Path path = joins.get(key);

					Subquery subquery = query.subquery(path.getJavaType());
					Root root = subquery.from(path.getJavaType());
					subquery.select(root);//.distinct(true);

					InnerCriteria internal = new InnerCriteria(manager, builder, subquery, root);

					internal.execute(parameter);
					predicates.add(path.in(internal.getQuery()));
				}
			}
		}

		for (String key : internals.getKeys()) {
			for (InnerCriteria<?> criteria : internals.getCriterias(key)) {
				or.add(joins.get(key).in(criteria.getQuery()));
			}

			predicates.add(builder.or(or.toArray(new Predicate[or.size()])));
		}

		query.where(builder.and(getPredicates()));
	}

	private boolean containsPath(String key) {
		return joins.containsKey(key);
	}

	public void equal(String attribute, Object value) {
		Operation operation = OperationFactory.equal(builder);
		Parameter parameter = new Parameter(attribute, value);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void unequal(String attribute, Object value) {
		Operation operation = OperationFactory.unequal(builder);
		Parameter parameter = new Parameter(attribute, value);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void greaterThan(String attribute, Comparable<?> value) {
		Parameter<Comparable> parameter = new Parameter<Comparable>(attribute, value);
		Operation<Comparable> operation = OperationFactory.greaterThan(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void lessThan(String attribute, Comparable<?> value) {
		Parameter<Comparable> parameter = new Parameter<Comparable>(attribute, value);
		Operation<Comparable> operation = OperationFactory.lessThan(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void lessOrEqual(String attribute, Comparable<?> value) {
		Parameter<Comparable> parameter = new Parameter<Comparable>(attribute, value);
		Operation<Comparable> operation = OperationFactory.lessOrEqual(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void greaterOrEqual(String attribute, Comparable<?> value) {
		Parameter<Comparable> parameter = new Parameter<Comparable>(attribute, value);
		Operation<Comparable> operation = OperationFactory.greaterOrEqual(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void like(String attribute, String pattern) {
		Parameter<String> parameter = new Parameter<String>(attribute, likeConcatenation(pattern));
		Operation<String> operation = OperationFactory.like(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void notLike(String attribute, String pattern) {
		Parameter<String> parameter = new Parameter<String>(attribute, likeConcatenation(pattern));
		Operation<String> operation = OperationFactory.notLike(builder);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void isMember(String attribute, Collection value) {
		Operation operation = OperationFactory.isMember(builder);
		Parameter parameter = new Parameter(attribute, value);
		parameter.setOperation(operation);
		execute(parameter);
	}

	public void isMemberOrEmpty(String attribute, Collection value) {
		Operation operation = OperationFactory.isMemberOrEmpty(builder);
		Parameter parameter = new Parameter(attribute, value);
		parameter.setOperation(operation);
		execute(parameter);
	}

}

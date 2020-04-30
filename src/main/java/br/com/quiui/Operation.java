package br.com.quiui;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public interface Operation<T> {

	OperationType getType();
	Predicate execute(Path<?> path, T value);

}

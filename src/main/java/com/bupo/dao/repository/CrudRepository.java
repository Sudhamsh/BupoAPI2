package com.bupo.dao.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {

	<S extends T> S save(S entity);

	Optional<T> findById(ID primaryKey);

	Iterable<T> findAll();

	long count();

	void delete(T entity);

	boolean existsById(ID primaryKey);

	// … more functionality omitted.
}
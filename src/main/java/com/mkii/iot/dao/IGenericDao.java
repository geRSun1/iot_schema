package com.mkii.iot.dao;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/** Shared data operations, similar to the generic DAO layer in the sample project. */
public interface IGenericDao<T, ID> {

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    T findByPk(ID id);

    T getById(ID id);

    List<T> getAll();

    long count();

    T getByField(String field, Object value);

    List<T> findByField(String field, Object value);

    Page<T> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction);

    List<T> searchBySpecification(String search);

    List<T> searchBySpecificationSorted(String search, String sortBy, String direction);

    Page<T> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction);

    List<T> findAllSorted(String sortBy, String direction);

    Page<T> findAllPaged(int page, int size, String sortBy, String direction);
}

package com.mkii.iot.dao.impl;

import com.mkii.iot.dao.IGenericDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public abstract class JpaGenericDao<T, ID> implements IGenericDao<T, ID> {

    private final JpaRepository<T, ID> repository;
    private final JpaSpecificationExecutor<T> specificationExecutor;

    protected JpaGenericDao(JpaRepository<T, ID> repository, JpaSpecificationExecutor<T> specificationExecutor) {
        this.repository = repository;
        this.specificationExecutor = specificationExecutor;
    }

    @Override public T save(T entity) { return repository.save(entity); }
    @Override public T update(T entity) { return repository.save(entity); }
    @Override public void delete(T entity) { repository.delete(entity); }
    @Override public Optional<T> findById(ID id) { return repository.findById(id); }
    @Override public boolean existsById(ID id) { return repository.existsById(id); }
    @Override public T findByPk(ID id) { return repository.findById(id).orElse(null); }
    @Override public T getById(ID id) { return repository.findById(id).orElse(null); }
    @Override public List<T> getAll() { return repository.findAll(); }
    @Override public long count() { return repository.count(); }

    @Override public T getByField(String field, Object value) {
        List<T> matches = findByField(field, value);
        return matches.isEmpty() ? null : matches.get(0);
    }

    @Override public List<T> findByField(String field, Object value) {
        return specificationExecutor.findAll((root, query, cb) -> cb.equal(resolvePath(root, field), convert(value, resolvePath(root, field).getJavaType())));
    }

    @Override public Page<T> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction) {
        return specificationExecutor.findAll((root, query, cb) -> cb.equal(resolvePath(root, field), convert(value, resolvePath(root, field).getJavaType())), pageRequest(page, size, sortBy, direction));
    }

    @Override public List<T> searchBySpecification(String search) {
        return specificationExecutor.findAll(buildSpecification(search));
    }

    @Override public List<T> searchBySpecificationSorted(String search, String sortBy, String direction) {
        return specificationExecutor.findAll(buildSpecification(search), sort(sortBy, direction));
    }

    @Override public Page<T> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        return specificationExecutor.findAll(buildSpecification(search), pageRequest(page, size, sortBy, direction));
    }

    @Override public List<T> findAllSorted(String sortBy, String direction) { return repository.findAll(sort(sortBy, direction)); }
    @Override public Page<T> findAllPaged(int page, int size, String sortBy, String direction) { return repository.findAll(pageRequest(page, size, sortBy, direction)); }

    private Specification<T> buildSpecification(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return cb.conjunction();
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            for (String condition : search.split(",")) {
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("^\\s*([\\w.]+)\\s*(>=|<=|>|<|:|=)\\s*(.*?)\\s*$").matcher(condition);
                if (!matcher.matches()) throw new IllegalArgumentException("Điều kiện tìm kiếm không hợp lệ: " + condition);
                String field = matcher.group(1);
                String operator = matcher.group(2);
                String rawValue = matcher.group(3).replaceAll("^[\\\"']|[\\\"']$", "");
                jakarta.persistence.criteria.Path<?> path = resolvePath(root, field);
                Class<?> javaType = path.getJavaType();
                Object value = convert(rawValue, javaType);
                if (operator.equals(":") && javaType == String.class) {
                    predicates.add(cb.like(cb.lower(path.as(String.class)), "%" + rawValue.toLowerCase() + "%"));
                } else if (operator.equals("=") || operator.equals(":")) {
                    predicates.add(cb.equal(path, value));
                } else if (Comparable.class.isAssignableFrom(javaType)) {
                    @SuppressWarnings("unchecked") jakarta.persistence.criteria.Expression<? extends Comparable<Object>> expression = (jakarta.persistence.criteria.Expression<? extends Comparable<Object>>) path;
                    @SuppressWarnings("unchecked") Comparable<Object> comparable = (Comparable<Object>) value;
                    predicates.add(switch (operator) {
                        case ">" -> cb.greaterThan(expression, comparable);
                        case ">=" -> cb.greaterThanOrEqualTo(expression, comparable);
                        case "<" -> cb.lessThan(expression, comparable);
                        case "<=" -> cb.lessThanOrEqualTo(expression, comparable);
                        default -> throw new IllegalArgumentException("Toán tử không hợp lệ: " + operator);
                    });
                } else {
                    throw new IllegalArgumentException("Trường " + field + " không hỗ trợ toán tử " + operator);
                }
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private jakarta.persistence.criteria.Path<?> resolvePath(jakarta.persistence.criteria.Root<T> root, String field) {
        jakarta.persistence.criteria.Path<?> path = root;
        for (String part : field.split("\\.")) path = path.get(part);
        return path;
    }

    private Object convert(Object value, Class<?> type) {
        if (value == null || type.isInstance(value)) return value;
        String text = value.toString().trim();
        if (type == String.class) return text;
        if (type == Long.class || type == long.class) return Long.valueOf(text);
        if (type == Integer.class || type == int.class) return Integer.valueOf(text);
        if (type == Double.class || type == double.class) return Double.valueOf(text);
        if (type == Boolean.class || type == boolean.class) return Boolean.valueOf(text);
        if (type == java.math.BigDecimal.class) return new java.math.BigDecimal(text);
        if (type == java.util.UUID.class) return java.util.UUID.fromString(text);
        if (type.isEnum()) return Enum.valueOf((Class<Enum>) type, text);
        return value;
    }

    private Sort sort(String sortBy, String direction) {
        return Sort.by(Sort.Direction.fromString(direction), sortBy);
    }

    private Pageable pageRequest(int page, int size, String sortBy, String direction) {
        return PageRequest.of(page, size, sort(sortBy, direction));
    }
}

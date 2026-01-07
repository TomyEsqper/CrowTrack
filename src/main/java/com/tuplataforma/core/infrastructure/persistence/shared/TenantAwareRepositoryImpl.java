package com.tuplataforma.core.infrastructure.persistence.shared;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class TenantAwareRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements TenantAwareRepository<T, ID> {

    private final EntityManager entityManager;

    public TenantAwareRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findById(ID id) {
        // Override to force JPQL query, which respects Hibernate Filters
        Class<T> domainType = getDomainClass();
        String jpql = "select e from " + domainType.getName() + " e where e.id = :id";
        TypedQuery<T> query = entityManager.createQuery(jpql, domainType);
        query.setParameter("id", id);
        
        List<T> results = query.getResultList();
        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.get(0));
        }
    }
}

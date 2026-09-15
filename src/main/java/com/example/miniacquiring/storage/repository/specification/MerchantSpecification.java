package com.example.miniacquiring.storage.repository.specification;

import com.example.miniacquiring.core.dto.MerchantFilter;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public final class MerchantSpecification {

    private MerchantSpecification() {

    }

    public static Specification<MerchantEntity> filter(
            MerchantFilter filter) {
        Specification<MerchantEntity> specification = Specification.allOf();

        if (filter.id() != null) {
            specification = specification.and(hasId(filter.id()));
        }
        if (filter.name() != null && !filter.name().isBlank()) {
            specification = specification.and(nameContains(filter.name()));
        }
        if (filter.statusId() != null) {
            specification = specification.and(hasStatus(filter.statusId()));
        }
        if (filter.commissionTypeId() != null) {
            specification = specification.and(hasCommissionType(filter.commissionTypeId()));
        }
        if (filter.minCommissionValue() != null) {
            specification = specification.and(commissionValueGreaterOrEqual(filter.minCommissionValue()));
        }
        if (filter.maxCommissionValue() != null) {
            specification = specification.and(commissionValueLessOrEqual(filter.maxCommissionValue()));
        }

        return specification;
    }

    private static Specification<MerchantEntity> hasId(Long id) {
        return (root, query, cb) ->
                cb.equal(root.get("id"), id);
    }

    private static Specification<MerchantEntity> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    private static Specification<MerchantEntity> hasStatus(Long statusId) {
        return (root, query, cb) ->
                cb.equal(root.get("status").get("id"), statusId);
    }

    private static Specification<MerchantEntity> hasCommissionType(Long commissionTypeId) {
        return (root, query, cb) ->
                cb.equal(root.get("commissionType").get("id"), commissionTypeId);
    }

    private static Specification<MerchantEntity> commissionValueGreaterOrEqual(BigDecimal minCommissionValue) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("commissionValue"), minCommissionValue);
    }

    private static Specification<MerchantEntity> commissionValueLessOrEqual(BigDecimal maxCommissionValue) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("commissionValue"), maxCommissionValue);
    }

}


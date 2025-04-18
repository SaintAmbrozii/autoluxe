package com.example.autoluxe.dto.specifications;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.searchcriteria.PaymentSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PaymentSpecs {

    public static Specification<Payments> accordingToReportProperties(User user, PaymentSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("managerId"), user.getId()));

            if (criteria.getUserId() != null)
                predicates.add(cb.equal(root.get("userId"), criteria.getUserId()));

            if (criteria.getFrom() != null && criteria.getTo() != null)
                predicates.add(cb.between(root.get("timestamp"), criteria.getFrom(), criteria.getTo().plusDays(1).minusNanos(100)));


            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Payments> accordingToCriteriaProperties(PaymentSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getUserId() != null)
                predicates.add(cb.equal(root.get("userId"), criteria.getUserId()));

            if (criteria.getFrom() != null && criteria.getTo() != null)
                predicates.add(cb.between(root.get("timestamp"), criteria.getFrom(), criteria.getTo().plusDays(1).minusNanos(100)));


            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}

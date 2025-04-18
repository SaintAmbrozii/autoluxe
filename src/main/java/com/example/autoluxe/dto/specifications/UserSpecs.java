package com.example.autoluxe.dto.specifications;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.searchcriteria.UserSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecs {

    public static Specification<User> accordingToReportProperties(UserSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(criteria.getEmail()))
                predicates.add(cb.equal(root.get("name"), criteria.getEmail()));

            if (!ObjectUtils.isEmpty(criteria.getPhone()))
                predicates.add(cb.equal(root.get("phone"), criteria.getPhone()));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}

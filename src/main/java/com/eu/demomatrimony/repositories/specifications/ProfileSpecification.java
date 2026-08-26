package com.eu.demomatrimony.repositories.specifications;

import com.eu.demomatrimony.models.Profile;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ProfileSpecification {

    public static Specification<Profile> searchProfiles(
            String name, Long minAge, Long maxAge, String gender, String address,
            String education, String ethnicity, String maritalStatus, String nationality,
            String city, String country
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (minAge != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("age"), minAge));
            }
            if (maxAge != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("age"), maxAge));
            }
            if (gender != null && !gender.isBlank()) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }
            if (city != null && !city.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }
            if (country != null && !country.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("country")), country.toLowerCase()));
            }
            if (maritalStatus != null && !maritalStatus.isBlank()) {
                predicates.add(cb.equal(root.get("maritalStatus"), maritalStatus));
            }

            predicates.add(cb.isNull(root.get("deletedAt"))); // Soft delete check

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

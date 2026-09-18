package com.eu.matrimonybackend.repositories;

import com.eu.matrimonybackend.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    List<Profile> findByDeletedAtIsNull();

    Optional<Profile> findByEmail(String email);

    @Query("SELECT p FROM Profile p WHERE p.deletedAt IS NULL " +
            "AND (:gender IS NULL OR :gender = '' OR LOWER(p.gender) = LOWER(:gender)) " +
            "AND (:minAge IS NULL OR p.age >= :minAge) " +
            "AND (:maxAge IS NULL OR p.age <= :maxAge) " +
            "AND (:city IS NULL OR :city = '' OR LOWER(p.city) = LOWER(:city)) " +
            "AND (:education IS NULL OR :education = '' OR LOWER(p.education) = LOWER(:education)) " +
            "AND (:maritalStatus IS NULL OR :maritalStatus = '' OR LOWER(p.maritalStatus) = LOWER(:maritalStatus))")
    Page<Profile> searchProfiles(@Param("gender") String gender,
                                 @Param("minAge") Long minAge,
                                 @Param("maxAge") Long maxAge,
                                 @Param("city") String city,
                                 @Param("education") String education,
                                 @Param("maritalStatus") String maritalStatus,
                                 Pageable pageable);
}
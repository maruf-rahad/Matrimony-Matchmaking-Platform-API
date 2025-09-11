package com.eu.demomatrimony.repositories;

import com.eu.demomatrimony.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchRepository extends JpaRepository<Profile, Long> {

    @Query(value = """
                    SELECT * FROM profile p
                    WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
                      AND (:minAge IS NULL OR p.age >= :minAge)
                      AND (:maxAge IS NULL OR p.age <= :maxAge)
                      AND (:gender IS NULL OR p.gender = :gender)
                      AND (:address IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :address, '%')))
                      AND (:education IS NULL OR LOWER(p.education) LIKE LOWER(CONCAT('%', :education, '%')))
                      AND (:ethnicity IS NULL OR LOWER(p.ethnicity) LIKE LOWER(CONCAT('%', :ethnicity, '%')))
                      AND (:maritalStatus IS NULL OR p.marital_status = :maritalStatus)
                      AND (:nationality IS NULL OR p.nationality = :nationality)
                      AND (:secondNationality IS NULL OR p.second_nationality = :secondNationality)
                      AND (:fatherOccupation IS NULL OR LOWER(p.father_occupation) LIKE LOWER(CONCAT('%', :fatherOccupation, '%')))
                      AND (:motherOccupation IS NULL OR LOWER(p.mother_occupation) LIKE LOWER(CONCAT('%', :motherOccupation, '%')))
                      AND (:numberOfSiblings IS NULL OR p.number_of_siblings = :numberOfSiblings
                      AND (:city IS NULL OR p.city = :city)
                      AND (:country IS NULL OR p.country = :country)
            )
            """, nativeQuery = true)
    List<Profile> searchProfiles(
            @Param("name") String name,
            @Param("minAge") Long minAge,
            @Param("maxAge") Long maxAge,
            @Param("gender") String gender,
            @Param("address") String address,
            @Param("education") String education,
            @Param("ethnicity") String ethnicity,
            @Param("maritalStatus") String maritalStatus,
            @Param("nationality") String nationality,
            @Param("secondNationality") String secondNationality,
            @Param("fatherOccupation") String fatherOccupation,
            @Param("motherOccupation") String motherOccupation,
            @Param("numberOfSiblings") String numberOfSiblings,
            @Param("city") String city,
            @Param("country") String country
    );
}

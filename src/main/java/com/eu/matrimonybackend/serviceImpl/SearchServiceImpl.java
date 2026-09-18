package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.service.SearchService;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.repositories.SearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;

    public SearchServiceImpl(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    /**
     * Performs a flexible, multi-criteria profile search where every parameter is optional:
     * a {@code null} value excludes that field from the filter entirely, text fields match as
     * case-insensitive partial substrings, and the rest (gender, marital status, nationality,
     * etc.) require an exact match.
     *
     * @param name partial, case-insensitive match against profile name
     * @param minAge inclusive lower bound on age
     * @param maxAge inclusive upper bound on age
     * @param gender exact match on gender
     * @param address partial, case-insensitive match against address
     * @param education partial, case-insensitive match against education
     * @param ethnicity partial, case-insensitive match against ethnicity
     * @param maritalStatus exact match on marital status
     * @param nationality exact match on nationality
     * @param secondNationality exact match on second nationality
     * @param fatherOccupation partial, case-insensitive match against father's occupation
     * @param motherOccupation partial, case-insensitive match against mother's occupation
     * @param numberOfSiblings exact match on number of siblings
     * @param city exact match on city
     * @param country exact match on country
     * @return every profile satisfying all supplied (non-null) criteria
     */
    @Override
    public List<Profile> searchProfiles(
            String name,
            Long minAge,
            Long maxAge,
            String gender,
            String address,
            String education,
            String ethnicity,
            String maritalStatus,
            String nationality,
            String secondNationality,
            String fatherOccupation,
            String motherOccupation,
            String numberOfSiblings,
            String city,
            String country
    ) {
        return searchRepository.searchProfiles(
                name, minAge, maxAge, gender,
                address, education, ethnicity, maritalStatus,
                nationality, secondNationality, fatherOccupation, motherOccupation, numberOfSiblings,
                city, country
        );
    }
}

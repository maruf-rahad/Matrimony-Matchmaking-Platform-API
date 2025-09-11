package com.eu.demomatrimony.serviceImpl;

import com.eu.demomatrimony.service.SearchService;
import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.SearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;

    public SearchServiceImpl(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

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

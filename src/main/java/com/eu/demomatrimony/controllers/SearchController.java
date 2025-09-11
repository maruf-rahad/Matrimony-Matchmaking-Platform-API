package com.eu.demomatrimony.controllers;

import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.service.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Search Apis", description = "Search your desired partner")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<Profile> searchProfiles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long minAge,
            @RequestParam(required = false) Long maxAge,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String ethnicity,
            @RequestParam(required = false) String maritalStatus,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) String secondNationality,
            @RequestParam(required = false) String fatherOccupation,
            @RequestParam(required = false) String motherOccupation,
            @RequestParam(required = false) String numberOfSiblings,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country
    ) {
        return searchService.searchProfiles(
                name, minAge, maxAge, gender,
                address, education, ethnicity, maritalStatus,
                nationality, secondNationality, fatherOccupation, motherOccupation, numberOfSiblings,
                city, country
        );
    }

}

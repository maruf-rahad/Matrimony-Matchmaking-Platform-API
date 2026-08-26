package com.eu.demomatrimony.controllers;

import com.eu.demomatrimony.dto.MatchResultDto;
import com.eu.demomatrimony.dto.PartnerPreferenceDto;
import com.eu.demomatrimony.service.MatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
@Tag(name = "Match & Compatibility APIs", description = "Endpoints for managing partner preferences and match scoring")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/preferences/{profileId}")
    public ResponseEntity<PartnerPreferenceDto> savePreferences(
            @PathVariable Long profileId,
            @RequestBody PartnerPreferenceDto preferenceDto) {
        return ResponseEntity.ok(matchService.saveOrUpdatePreferences(profileId, preferenceDto));
    }

    @GetMapping("/preferences/{profileId}")
    public ResponseEntity<PartnerPreferenceDto> getPreferences(@PathVariable Long profileId) {
        return ResponseEntity.ok(matchService.getPreferencesByProfileId(profileId));
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<List<MatchResultDto>> getMatches(@PathVariable Long profileId) {
        return ResponseEntity.ok(matchService.getTopMatchesForProfile(profileId));
    }
}
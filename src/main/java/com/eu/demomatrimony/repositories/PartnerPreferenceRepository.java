package com.eu.demomatrimony.repositories;

import com.eu.demomatrimony.models.PartnerPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerPreferenceRepository extends JpaRepository<PartnerPreference, Long> {
    Optional<PartnerPreference> findByProfileId(Long profileId);
}
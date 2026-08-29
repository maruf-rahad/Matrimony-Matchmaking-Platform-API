package com.eu.matrimonybackend.controllers;

import com.eu.matrimonybackend.dto.InterestDto;
import com.eu.matrimonybackend.enums.InterestStatus;
import com.eu.matrimonybackend.service.InterestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interests")
@Tag(name = "Interest & Match APIs", description = "Endpoints for managing user connections")
public class InterestController {

    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    @PostMapping("/send")
    public ResponseEntity<InterestDto> sendInterest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(interestService.sendInterest(senderId, receiverId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InterestDto> updateStatus(@PathVariable Long id, @RequestParam InterestStatus status) {
        return ResponseEntity.ok(interestService.updateInterestStatus(id, status));
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<InterestDto>> getReceivedInterests(
            @PathVariable Long receiverId,
            @RequestParam(defaultValue = "PENDING") InterestStatus status) {
        return ResponseEntity.ok(interestService.getReceivedInterests(receiverId, status));
    }
}
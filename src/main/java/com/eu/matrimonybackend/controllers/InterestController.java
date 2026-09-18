package com.eu.matrimonybackend.controllers;

import com.eu.matrimonybackend.dto.InterestDto;
import com.eu.matrimonybackend.dto.NotificationDto;
import com.eu.matrimonybackend.enums.InterestStatus;
import com.eu.matrimonybackend.enums.NotificationType;
import com.eu.matrimonybackend.service.InterestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/interests")
@Tag(name = "Interest & Match APIs", description = "Endpoints for managing user connections")
public class InterestController {

    private final InterestService interestService;
    private final SimpMessagingTemplate messagingTemplate;

    public InterestController(InterestService interestService, SimpMessagingTemplate messagingTemplate) {
        this.interestService = interestService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send")
    public ResponseEntity<InterestDto> sendInterest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        InterestDto interest = interestService.sendInterest(senderId, receiverId);

        NotificationDto notification = new NotificationDto(
                NotificationType.INTEREST_RECEIVED, senderId, receiverId, interest.getId(), LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/notifications/" + receiverId, notification);

        return ResponseEntity.ok(interest);
    }

    @PutMapping("/mark-seen")
    public ResponseEntity<Void> markSeen(@RequestParam Long receiverId) {
        interestService.markReceivedInterestsSeen(receiverId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InterestDto> updateStatus(@PathVariable Long id, @RequestParam InterestStatus status) {
        return ResponseEntity.ok(interestService.updateInterestStatus(id, status));
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<InterestDto>> getReceivedInterests(
            @PathVariable Long receiverId,
            @RequestParam(required = false) InterestStatus status) {
        return ResponseEntity.ok(interestService.getReceivedInterests(receiverId, status));
    }

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<InterestDto>> getSentInterests(
            @PathVariable Long senderId,
            @RequestParam(required = false) InterestStatus status) {
        return ResponseEntity.ok(interestService.getSentInterests(senderId, status));
    }
}
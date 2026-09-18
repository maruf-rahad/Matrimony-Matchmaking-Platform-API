package com.eu.matrimonybackend.controllers;

import com.eu.matrimonybackend.dto.UnreadCountsDto;
import com.eu.matrimonybackend.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification APIs", description = "Unread interest and chat message counters")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread-count")
    public ResponseEntity<UnreadCountsDto> getUnreadCount(@RequestParam Long profileId) {
        return ResponseEntity.ok(notificationService.getUnreadCounts(profileId));
    }

    @GetMapping("/chat-counts")
    public ResponseEntity<Map<Long, Long>> getChatCounts(@RequestParam Long profileId) {
        return ResponseEntity.ok(notificationService.getChatCountsBySender(profileId));
    }
}

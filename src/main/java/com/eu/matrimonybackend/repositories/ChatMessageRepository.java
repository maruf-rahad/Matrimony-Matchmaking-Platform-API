package com.eu.matrimonybackend.repositories;

import com.eu.matrimonybackend.models.ChatMessage;
import com.eu.matrimonybackend.repositories.projections.UnreadCountBySender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR " +
            "(m.sender.id = :user2Id AND m.receiver.id = :user1Id) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    List<ChatMessage> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

    long countByReceiverIdAndReadFalse(Long receiverId);

    @Query("SELECT m.sender.id AS senderId, COUNT(m) AS count FROM ChatMessage m " +
            "WHERE m.receiver.id = :receiverId AND m.read = false GROUP BY m.sender.id")
    List<UnreadCountBySender> countUnreadGroupedBySender(@Param("receiverId") Long receiverId);

    @Modifying
    @Query("UPDATE ChatMessage m SET m.read = true " +
            "WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.read = false")
    int markMessagesRead(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
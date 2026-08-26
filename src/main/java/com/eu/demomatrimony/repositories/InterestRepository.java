package com.eu.demomatrimony.repositories;

import com.eu.demomatrimony.models.Interest;
import com.eu.demomatrimony.enums.InterestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<Interest> findBySenderId(Long senderId);

    List<Interest> findByReceiverId(Long receiverId);

    boolean existsBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, InterestStatus status);

    List<Interest> findByReceiverIdAndStatus(Long receiverId, InterestStatus status);
}
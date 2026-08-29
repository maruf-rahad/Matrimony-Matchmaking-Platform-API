package com.eu.matrimonybackend.models;

import com.eu.matrimonybackend.enums.InterestStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interests")
@NoArgsConstructor
@AllArgsConstructor
public class Interest extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_profile_id", nullable = false)
    private Profile sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_profile_id", nullable = false)
    private Profile receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterestStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getSender() {
        return sender;
    }

    public void setSender(Profile sender) {
        this.sender = sender;
    }

    public Profile getReceiver() {
        return receiver;
    }

    public void setReceiver(Profile receiver) {
        this.receiver = receiver;
    }

    public InterestStatus getStatus() {
        return status;
    }

    public void setStatus(InterestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", status=" + status +
                '}';
    }
}

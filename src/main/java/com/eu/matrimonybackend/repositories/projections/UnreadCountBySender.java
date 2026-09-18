package com.eu.matrimonybackend.repositories.projections;

public interface UnreadCountBySender {
    Long getSenderId();
    Long getCount();
}

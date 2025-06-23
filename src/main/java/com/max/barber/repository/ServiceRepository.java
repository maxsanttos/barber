package com.max.barber.repository;

import com.max.barber.model.services.Services;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Services, Long> {
    
}
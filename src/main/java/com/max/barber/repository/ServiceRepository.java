package com.max.barber.repository;

import com.max.barber.model.services.Services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Services, Long> {
    List<String> findAllServices();
}
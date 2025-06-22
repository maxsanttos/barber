package com.max.barber.repository;

import com.max.barber.model.people.Barber;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Long> {
    Optional<Barber> findByUsername(String name);
}

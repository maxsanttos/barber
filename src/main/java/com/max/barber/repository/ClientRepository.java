package com.max.barber.repository;

import com.max.barber.model.people.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
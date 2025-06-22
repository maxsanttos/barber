package com.max.barber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.max.barber.model.people.Barber;
import com.max.barber.model.user.User;
import com.max.barber.repository.BarberRepository;
import com.max.barber.repository.UserRepository;

import jakarta.transaction.Transactional;

public class BarberService {

     
    @Autowired
    private BarberRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // listar todos os clientes

    public List<Barber> getAllClients() {
        return repository.findAll();
    }

    
    public Barber getBarberByLoggedUser() {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return repository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado para o usuário: " + username));
    }


    public Barber getBarberById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado com id: " + id));
    }

    @Transactional
    public Barber updateBarber(Long id, String specialty, String username, String password, User currentUser) {
        Barber barber = getBarberById(id);

        // Só permite se for o próprio usuário
        if (!barber.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Você não tem permissão para atualizar este cadastro.");
        }

        if (specialty != null && !specialty.isBlank()) {
            barber.setSpecialty(specialty);
        }
        if (username != null && !username.isBlank()) {
            if (!username.equals(barber.getUser().getUsername()) && userRepository.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Nome já está em uso.");
            }
            barber.getUser().setUsername(username);
        }
        if (password != null && !password.isBlank()) {
            barber.getUser().setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(barber.getUser());
        return repository.save(barber);
    }

}

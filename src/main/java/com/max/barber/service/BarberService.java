package com.max.barber.service;

import java.util.List;

import com.max.barber.model.people.dtos.UpdateBarberDTO;
import com.max.barber.model.user.RoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.max.barber.model.people.Barber;
import com.max.barber.model.user.User;
import com.max.barber.repository.BarberRepository;
import com.max.barber.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BarberService {

     
    @Autowired
    private BarberRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public Barber getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String login = ((UserDetails) principal).getUsername();
            return repository.findByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro logado não encontrado: " + login));
        }
        throw new SecurityException("Barbeiro não autenticado.");
    }

    @Transactional
    public Barber updateBarber(Long id, UpdateBarberDTO dto, Barber currentBarber) {
        Barber barber = getBarberById(id);
        User user = barber.getUser();

        boolean isBarber = currentBarber.getRole().equals(RoleUser.BARBER);
        boolean isOwner = currentBarber.getId().equals(barber.getId());

        if (!isBarber && !isOwner) {
            throw new SecurityException("Você não tem permissão para atualizar este Barbeiro.");
        }

        // Atualiza username
        if (dto.username() != null && !dto.username().isBlank()) {
            if (!dto.username().equals(user.getUsername()) && userRepository.findByUsername(dto.username()).isPresent()) {
                throw new IllegalArgumentException("Nome de usuário já está em uso.");
            }
            user.setUsername(dto.username());
        }

        // Atualiza senha
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        // Atualiza especialidade
        if (dto.specialty() != null && !dto.specialty().isBlank()) {
            barber.setSpecialty(dto.specialty());
        }

        // Atualiza role se fornecida (admin ou o próprio barbeiro)
        if (dto.role() != null && isBarber) {
            user.setRole(dto.role());
            barber.setRole(dto.role());
        }

        userRepository.save(user);
        return repository.save(barber);
    }



    public List<Barber> getAllBarbers() {
        return repository.findAll();
    }


    public Barber getBarberById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado com id: " + id));
    }

    @Transactional
    public void deleteBarberById(Long id){
        Barber barber = getBarberById(id);
        repository.delete(barber);
    }
}

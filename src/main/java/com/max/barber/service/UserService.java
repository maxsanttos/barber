package com.max.barber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.max.barber.model.user.RoleUser;
import com.max.barber.model.user.User;
import com.max.barber.model.user.dtos.RegisterDTO;
import com.max.barber.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String login = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException("Usuário logado não encontrado: " + login));
        }
        throw new SecurityException("Usuário não autenticado.");
    }

    @Transactional
    public User registerUser(RegisterDTO dto){
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException("Nome já em uso.");
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role() != null ? dto.role() : RoleUser.CLIENT);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, String name, String password, RoleUser role, User currentUser){
        User user = getUserById(id);

        boolean isAdmin = currentUser.getRole().equals(RoleUser.ADMIN);
        boolean isOwner = currentUser.getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new SecurityException("Você não tem permissão para atualizar este usuário.");
        }

        if (name != null && !name.isBlank()) {
            if (!name.equals(user.getUsername()) && userRepository.findByUsername(name).isPresent()) {
                throw new IllegalArgumentException("Nome já está em uso.");
            }
            user.setUsername(name);
        }

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (isAdmin && role != null) {
            user.setRole(role);
        }

        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));
    }
}


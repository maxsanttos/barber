package com.max.barber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.max.barber.model.people.Client;
import com.max.barber.model.user.User;
import com.max.barber.repository.ClientRepository;
import com.max.barber.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

  
    // buscar cliente pelo usuário logado
    public Client getClientByLoggedUser() {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return clientRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado para o usuário: " + username));
    }
    @Transactional
    public Client updateClient(Long id, String phoneNumber, String username, String password, User currentUser) {
        Client client = getClientById(id);

        // Só permite se for o próprio usuário
        if (!client.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Você não tem permissão para atualizar este cadastro.");
        }

        if (phoneNumber != null && !phoneNumber.isBlank()) {
            client.setPhoneNumber(phoneNumber);
        }
        if (username != null && !username.isBlank()) {
            if (!username.equals(client.getUser().getUsername()) && userRepository.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Nome já está em uso.");
            }
            client.getUser().setUsername(username);
        }
        if (password != null && !password.isBlank()) {
            client.getUser().setPassword(passwordEncoder.encode(password));
        }
        userRepository.save(client.getUser());
        return clientRepository.save(client);
    }

    // buscar cliente por id
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com id: " + id));
    }

     @Transactional
    public void deleteClientById(Long id){
        Client client = getClientById(id);
        clientRepository.delete(client);
    }
}

package com.max.barber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.max.barber.model.people.Client;
import com.max.barber.model.people.dtos.UpdateClientDTO;
import com.max.barber.model.user.RoleUser;
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
    public Client updateClient(Long id, UpdateClientDTO dto, Client currentClient) {
        Client client = getClientById(id);
        User user = client.getUser();

        boolean isClient = currentClient.getRole().equals(RoleUser.CLIENT);
        boolean isOwner = currentClient.getId().equals(client.getId());
        if (!isClient && !isOwner) {
            throw new SecurityException("Você não tem permissão para atualizar este Cliente.");
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

        // Atualiza telefone
        if (dto.phoneNumber() != null && !dto.phoneNumber().isBlank()) {
            client.setPhoneNumber(dto.phoneNumber());
            
        }
        if (dto.role() != null && isClient) {
            if (dto.role() != RoleUser.CLIENT) {
                throw new IllegalArgumentException("Apenas clientes podem ter o papel CLIENT.");
            }
            user.setRole(dto.role());
            client.setRole(dto.role());
        }

        // Salva as alterações
        userRepository.save(user);
        return clientRepository.save(client);

    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
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

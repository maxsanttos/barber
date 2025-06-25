package com.max.barber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.max.barber.model.people.Client;
import com.max.barber.model.people.dtos.UpdateClientDTO;
import com.max.barber.service.ClientService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/clients")
public class ClientController {
    
    @Autowired
    private ClientService clientService;

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id,@Valid @RequestBody UpdateClientDTO dto) {
        // Aqui você obteria o cliente logado (ex: pelo token JWT via Spring Security)
        Client currentClient = clientService.getLoggedInUser();
        Client updatedClient = clientService.updateClient(id, dto, currentClient);
        
        return ResponseEntity.ok(updatedClient);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients(){
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }
}

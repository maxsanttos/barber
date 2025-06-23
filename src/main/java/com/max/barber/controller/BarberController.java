package com.max.barber.controller;

import com.max.barber.model.people.Barber;
import com.max.barber.model.people.dtos.UpdateBarberDTO;
import com.max.barber.model.user.dtos.RegisterDTO;
import com.max.barber.model.user.dtos.UserInfoDTO;
import com.max.barber.service.BarberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barberS")
public class BarberController {

    @Autowired
    private BarberService service;

    @PutMapping("/{id}")
    public ResponseEntity<Barber> updateBarber(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBarberDTO dto) {

        // Aqui você obteria o barbeiro logado (ex: pelo token JWT via Spring Security)
        Barber currentBarber = service.getLoggedInUser();
        Barber updatedBarber = service.updateBarber(id, dto, currentBarber);

        return ResponseEntity.ok(updatedBarber);
    }

    @GetMapping
    public ResponseEntity<List<Barber>> getAllBarbers(){
        List<Barber> barbers = service.getAllBarbers();
        return ResponseEntity.ok(barbers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barber> getBarberById(@PathVariable Long id) {
        Barber barber = service.getBarberById(id);
        return ResponseEntity.ok(barber);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarber(@PathVariable Long id) {
        service.deleteBarberById(id);
        return ResponseEntity.noContent().build();
    }
}

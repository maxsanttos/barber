package com.max.barber.controller;

import com.max.barber.model.services.Services;
import com.max.barber.model.services.dtos.UpdateServiceDTO;
import com.max.barber.service.ServicesService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServicesController {

    @Autowired
    private ServicesService service;

    @GetMapping
    public ResponseEntity<List<Services>> getAllServices(){
        List<Services> servicesList = service.getAllServices();
        return ResponseEntity.ok(servicesList);
    }

     @GetMapping("/{id}")
    public ResponseEntity<Services> getServicesById(@PathVariable Long id) {
        Services services = service.getServiceById(id);
        return ResponseEntity.ok(services);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> updateServices(@PathVariable Long id, @RequestBody UpdateServiceDTO dto){
        Services updatedService = service.updateServices(id, dto);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        service.deleteService(id);
        return ResponseEntity.noContent().build();
    }

}

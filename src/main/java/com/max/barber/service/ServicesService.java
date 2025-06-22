package com.max.barber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.max.barber.model.services.Appointment;
import com.max.barber.model.services.Services;
import com.max.barber.repository.AppointmentRepository;
import com.max.barber.repository.ServiceRepository;


import jakarta.transaction.Transactional;

@Service
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    //Listar todos os serviços disponíveis
    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    // Buscar serviços por ID
    public Services getServiceById(Long id){
            return serviceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

    }

     // Criar novo serviço
    @Transactional
    public Services createService(Services service, Long appointmentId) {
        if (appointmentId != null) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado com id: " + appointmentId));
            service.setAppointment(appointment);
        }
        return serviceRepository.save(service);
    }

    // Atualizar serviço existente
    @Transactional
    public Services updateServices(Long id, String name, String description, Double price, String duration){
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        service.setName(name);
        service.setDescription(description);
        service.setPrice(price);
        service.setDuration(duration);
        
        return serviceRepository.save(service);
    }

    // Deletar serviço por ID
    @Transactional
    public void deleteService(Long id) {
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        serviceRepository.delete(service);
    }
}
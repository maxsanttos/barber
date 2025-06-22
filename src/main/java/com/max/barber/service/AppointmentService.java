package com.max.barber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.max.barber.model.services.Appointment;
import com.max.barber.repository.AppointmentRepository;

public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    // Listar todos os agendamentos
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Buscar agendamento por ID
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado com o ID: " + id));
    }

    // Atualizar agendamento
    public Appointment updateAppointment(Long id, Appointment updated) {
        Appointment appointment = getAppointmentById(id);

        if(updated.getBarber() != null) {
            appointment.setBarber(updated.getBarber());
        }if(updated.getClient() != null) {
            appointment.setClient(updated.getClient());
        }if(updated.getServices() != null) {
            appointment.setServices(updated.getServices());
        }
        return appointmentRepository.save(appointment);
    }

    // Deletar agendamento
    public void deleteAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointmentRepository.delete(appointment);
    }
}

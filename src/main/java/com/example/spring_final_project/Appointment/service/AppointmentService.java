package com.example.spring_final_project.Appointment.service;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Appointment.model.AppointmentStatus;
import com.example.spring_final_project.Appointment.repository.AppointmentRepository;
import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.notification.service.NotificationService;
import com.example.spring_final_project.web.dto.AppointmentRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final DoctorService doctorService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, NotificationService notificationService, UserService userService, DoctorService doctorService) {
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.doctorService = doctorService;
    }

    @CacheEvict(value = {"appointments", "user-appointments"})
    public Appointment registerAppointment(AppointmentRegisterRequest appointmentRegisterRequest, UUID userId, UUID doctorId){

        User user = userService.getById(userId);
        Doctor doctor = doctorService.getById(doctorId);

        Appointment appointment = initAppointment(appointmentRegisterRequest, user, doctor);

        appointmentRepository.save(appointment);

        log.info("An appointment with id [%s] was created successfully!".formatted(appointment.getId()));

        notificationService.sendNotification(userId, "Appointment", "An appointment was created with dr.%s %s and it's going to be on".formatted(doctor.getFirstName(), doctor.getLastName()) +
                " %s".formatted(appointment.getTime()));

        return appointment;
    }


    private Appointment initAppointment(AppointmentRegisterRequest appointmentRegisterRequest, User user, Doctor doctor) {

        LocalDateTime now = LocalDateTime.now();

        return Appointment.builder()
                .user(user)
                .doctor(doctor)
                .status(AppointmentStatus.PENDING)
                .createdOn(now)
                .updatedOn(now)
                .time(appointmentRegisterRequest.getTime())
                .build();
    }

    @Cacheable("appointments")
    public List<Appointment> getAllAppointments(){
        return appointmentRepository.findAll();
    }

    @Cacheable("user-appointments")
    public List<Appointment> getAllAppointmentsForUser(UUID userId){
        Optional<List<Appointment>> allByUserId = appointmentRepository.findAllByUserId(userId);

        return allByUserId.orElseGet(ArrayList::new);
    }


}

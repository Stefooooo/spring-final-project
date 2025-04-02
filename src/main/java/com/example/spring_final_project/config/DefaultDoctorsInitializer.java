package com.example.spring_final_project.config;

import com.example.spring_final_project.Doctor.model.Departament;
import com.example.spring_final_project.Doctor.repository.DoctorRepository;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.web.dto.DoctorRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultDoctorsInitializer implements CommandLineRunner {

    private final DoctorService doctorService;

    @Autowired
    public DefaultDoctorsInitializer(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if there are any doctors already in the database

        if(doctorService.getAllDoctors().isEmpty()){
            DoctorRegisterRequest doctor1 = DoctorRegisterRequest.builder()
                    .email("johnny.bravo@gmail.com")
                    .departament(Departament.Endocrinology)
                    .password("123123")
                    .build();

            doctorService.registerDoctor(doctor1);

            DoctorRegisterRequest doctor2 = DoctorRegisterRequest.builder()
                    .email("johnny.depp@gmail.com")
                    .departament(Departament.Cardiology)
                    .password("123123")
                    .build();

            doctorService.registerDoctor(doctor2);

            DoctorRegisterRequest doctor3 = DoctorRegisterRequest.builder()
                    .email("john.wick@gmail.com")
                    .departament(Departament.Dentistry)
                    .password("123123")
                    .build();

            doctorService.registerDoctor(doctor3);

            DoctorRegisterRequest doctor4 = DoctorRegisterRequest.builder()
                    .email("johnny.walker@gmail.com")
                    .departament(Departament.Gastroenterology)
                    .password("123123")
                    .build();

            doctorService.registerDoctor(doctor4);

            DoctorRegisterRequest doctor5 = DoctorRegisterRequest.builder()
                    .email("johnny.test@gmail.com")
                    .departament(Departament.Ophthalmology)
                    .password("123123")
                    .build();

            doctorService.registerDoctor(doctor5);
        }


    }
}

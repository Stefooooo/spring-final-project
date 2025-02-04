package com.example.spring_final_project.Doctor.model;

import com.example.spring_final_project.Appointment.model.Appointment;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "years_of_expertise")
    private int yearsOfExpertise;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor")
    private Set<Appointment> appointments;

    @Enumerated
    private Specialty specialty;

    public Doctor(String firstName, String lastName, int yearsOfExpertise) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearsOfExpertise = yearsOfExpertise;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getYearsOfExpertise() {
        return yearsOfExpertise;
    }

    public void setYearsOfExpertise(int yearsOfExpertise) {
        this.yearsOfExpertise = yearsOfExpertise;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }


}
